package com.oscarliang.elibrary.api;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    public ApiResponse<T> create(Throwable error) {
        return new ApiErrorResponse<>(error.getMessage() != null ? error.getMessage() : "Unknown error!");
    }

    public ApiResponse<T> create(Response<T> response) {
        if (response.isSuccessful()) {
            T body = response.body();
            if (body == null || response.code() == 204) {
                return new ApiEmptyResponse<>();
            } else {
                return new ApiSuccessResponse<>(body);
            }
        } else {
            String errorMsg = "";
            try {
                errorMsg = response.errorBody().string();
            } catch (IOException e) {
                errorMsg = response.message();
            }
            return new ApiErrorResponse<>(errorMsg);
        }
    }
    //========================================================

    //--------------------------------------------------------
    // Inner Classes
    //--------------------------------------------------------
    public static class ApiSuccessResponse<T> extends ApiResponse<T> {

        private final T mBody;

        ApiSuccessResponse(T body) {
            mBody = body;
        }

        public T getBody() {
            return mBody;
        }

    }

    public static class ApiErrorResponse<T> extends ApiResponse<T> {

        private final String mErrorMessage;

        ApiErrorResponse(String errorMessage) {
            mErrorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }

    }

    public static class ApiEmptyResponse<T> extends ApiResponse<T> {
    }
    //========================================================

}
