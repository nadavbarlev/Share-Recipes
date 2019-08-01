package com.example.sharecipes.util.network;


import java.io.IOException;

import retrofit2.Response;

/**
 * Generic class for handling responses from Retrofit
 **/
public class ApiResponse<T> {

    /* Constants */
    private int EMPTY_RESPONSE_CODE = 204;
    private String GENERIC_ERROR_MSG = "Unknown error\nCheck network connection";

    /* Methods */

    /* Create ApiResponse from Retrofit Response */
    public ApiResponse<T> create(Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();
            if (body == null || response.code() == EMPTY_RESPONSE_CODE)
                return new ApiEmptyResponse<>();
            else
                return new ApiSuccessResponse<>(body);
        } else {
            try { return new ApiErrorResponse<>(response.errorBody().string()); }
            catch (IOException e) {
                e.printStackTrace();
                return new ApiErrorResponse<>(response.message());
            }
        }
    }

    /* Create ApiResponse from Throwable */
    public ApiResponse<T> create(Throwable error) {
        String errorMsg = error.getMessage().equals("") ?  GENERIC_ERROR_MSG : error.getMessage();
        return new ApiErrorResponse<>(errorMsg);
    }

    /**
     * Generic success response
     **/
    public class ApiSuccessResponse<T> extends ApiResponse<T> {

        // Data Members
        private T mBody;

        // Constructor
        ApiSuccessResponse(T body) {
            this.mBody = body;
        }

        // Getters and Setters
        public T getBody() {
            return this.mBody;
        }
    }

    /**
     * Generic Error response
     **/
    public class ApiErrorResponse<T> extends ApiResponse<T> {

        // Data Members
        private String mErrorMsg;

        // Constructor
        ApiErrorResponse(String errorMsg) {
            this.mErrorMsg = errorMsg;
        }

        // Getters and Setters
        public String getErrorMsg() {
            return this.mErrorMsg;
        }
    }

    /**
     * Generic Empty response
     * For HTTP 204 successful responses
     **/
    public class ApiEmptyResponse<T> extends ApiResponse<T> {}
}
