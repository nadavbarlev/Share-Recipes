package com.example.sharecipes.util.Network;

import com.example.sharecipes.util.AppExecutors;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

/**
 * Implement Single Source of Truth Principal
 **/

public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    /* Data Members */

    private AppExecutors executors;
    private MediatorLiveData<Resource<CacheObject>> results; // The Single Source of Truth

    /* Constructor*/
    NetworkBoundResource(AppExecutors executors) {
        this.executors = executors;
        this.results = new MediatorLiveData<>();
        setup();
    }

    /* Methods */
    public void setup() {

        // Update LiveData for Loading Status
        results.setValue((Resource<CacheObject>)Resource.loading(null));

        // Observe LiveData source from local DB
        final LiveData<CacheObject> cacheFromDB = loadFromDb();

        results.addSource(cacheFromDB, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {

                // Stop Observing
                results.removeSource(cacheFromDB);

                // Fetch from network if needed
                if (shouldFetch(cacheObject)) {
                    fetchFromNetwork(cacheFromDB);
                }
                else {
                    results.addSource(cacheFromDB, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            }
        });
    }

    private void fetchFromNetwork(final LiveData<CacheObject> cacheFromDB) {

        // Update LiveData for Loading Status
        results.addSource(cacheFromDB, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponseLiveData = createCall();

        results.addSource(apiResponseLiveData, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<RequestObject> requestObjectApiResponse) {
                results.removeSource(cacheFromDB);
                results.removeSource(apiResponseLiveData);
                handleNetworkResponse(requestObjectApiResponse);
            }
        });
    }

    private void handleNetworkResponse(final ApiResponse<RequestObject> requestObjectApiResponse) {

        // Success
        if (requestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse) {

            // Save to the local DB
            AppExecutors.getInstance().background().execute(new Runnable() {
                @Override
                public void run() {
                   RequestObject requestObject = (RequestObject)
                           ((ApiResponse.ApiSuccessResponse)requestObjectApiResponse).getBody();
                    saveCallResult(requestObject);

                    // Start observing again to see the refreshed data
                    AppExecutors.getInstance().main().execute(new Runnable() {
                        @Override
                        public void run() {
                            results.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(@Nullable CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                }
            });
        }

        // Empty
        else if (requestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse) {

            AppExecutors.getInstance().main().execute(new Runnable() {
                @Override
                public void run() {
                    results.addSource(loadFromDb(), new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }
            });
        }

        // Error
        else if (requestObjectApiResponse instanceof ApiResponse.ApiErrorResponse) {

            final ApiResponse.ApiErrorResponse apiErrorResponse =
                    (ApiResponse.ApiErrorResponse)requestObjectApiResponse;
            AppExecutors.getInstance().main().execute(new Runnable() {
                @Override
                public void run() {
                    results.addSource(loadFromDb(), new Observer<CacheObject>() {
                        @Override
                        public void onChanged(@Nullable CacheObject cacheObject) {
                            setValue(Resource.error(apiErrorResponse.getErrorMsg(), cacheObject));
                        }
                    });
                }
            });
        }
    }

    private void setValue(Resource<CacheObject> newValue) {
        if (results.getValue() != newValue) {
            results.setValue(newValue);
        }
    }

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return results;
    };

    /* Abstract Methods */

    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call - Converting Call object to LiveData object
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();
}
