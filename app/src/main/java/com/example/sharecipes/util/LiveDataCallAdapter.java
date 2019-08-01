package com.example.sharecipes.util;

import com.example.sharecipes.util.network.ApiResponse;

import java.lang.reflect.Type;

import androidx.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {

    // Data Members
    private Type responseType;

    // Constructor
    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    // Methods
    @Override
    public Type responseType() {
        return this.responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(final Call<R> call) {
        return new LiveData<ApiResponse<R>>() {
            @Override
            protected void onActive() {
                super.onActive();
                final ApiResponse apiResponse = new ApiResponse();
                call.enqueue(new Callback<R>() {
                    @Override
                    public void onResponse(Call<R> call, Response<R> response) {
                        postValue(apiResponse.create(response));
                    }

                    @Override
                    public void onFailure(Call<R> call, Throwable t) {
                        postValue(apiResponse.create(t));
                    }
                });
            }
        };
    }
}
