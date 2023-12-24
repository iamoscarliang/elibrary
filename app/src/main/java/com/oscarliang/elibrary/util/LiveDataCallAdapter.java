package com.oscarliang.elibrary.util;

import androidx.lifecycle.LiveData;

import com.oscarliang.elibrary.api.ApiResponse;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {

    private final Type mResponseType;

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public LiveDataCallAdapter(Type responseType) {
        mResponseType = responseType;
    }
    //========================================================

    //--------------------------------------------------------
    // Overriding methods
    //--------------------------------------------------------
    @Override
    public Type responseType() {
        return mResponseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(Call<R> call) {
        return new LiveData<ApiResponse<R>>() {
            @Override
            protected void onActive() {
                super.onActive();
                final ApiResponse<R> apiResponse = new ApiResponse<>();
                if (!call.isExecuted()) {
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
            }
        };
    }
    //========================================================

}
