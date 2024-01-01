package com.oscarliang.elibrary.repository;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.oscarliang.elibrary.AppExecutors;
import com.oscarliang.elibrary.api.ApiResponse;
import com.oscarliang.elibrary.vo.Resource;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors mAppExecutors;
    private final MediatorLiveData<Resource<ResultType>> mResult = new MediatorLiveData<>();

    //--------------------------------------------------------
    // Constructors
    //--------------------------------------------------------
    public NetworkBoundResource(AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
        init();
    }
    //========================================================

    //--------------------------------------------------------
    // Getter and Setter
    //--------------------------------------------------------
    public LiveData<Resource<ResultType>> getLiveData() {
        return mResult;
    }
    //========================================================

    //--------------------------------------------------------
    // Methods
    //--------------------------------------------------------
    private void init() {

        // Update LiveData to loading state
        setValue(Resource.loading(null));

        // Observe LiveData source from local db
        final LiveData<ResultType> dbSource = loadFromDb();

        mResult.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType data) {
                mResult.removeSource(dbSource);

                if (shouldFetch(data)) {
                    // Fetch data from the network
                    fetchFromNetwork(dbSource);
                } else {
                    // Fetch data from the local db
                    mResult.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(ResultType newData) {
                            setValue(Resource.success(newData));
                        }
                    });
                }
            }
        });
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {

        // Update LiveData to loading state
        mResult.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType newData) {
                setValue(Resource.loading(newData));
            }
        });

        // Observe LiveData source from api call
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        mResult.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(ApiResponse<RequestType> response) {
                mResult.removeSource(dbSource);
                mResult.removeSource(apiResponse);

                if (response instanceof ApiResponse.ApiSuccessResponse) {
                    mAppExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            // Save the response to the local db
                            saveCallResult(((ApiResponse.ApiSuccessResponse<RequestType>) response).getBody());

                            mAppExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    mResult.addSource(loadFromDb(), new Observer<ResultType>() {
                                        @Override
                                        public void onChanged(ResultType newData) {
                                            setValue(Resource.success(newData));
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else if (response instanceof ApiResponse.ApiEmptyResponse) {
                    mAppExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mResult.addSource(loadFromDb(), new Observer<ResultType>() {
                                @Override
                                public void onChanged(ResultType newData) {
                                    setValue(Resource.success(newData));
                                }
                            });
                        }
                    });
                } else if (response instanceof ApiResponse.ApiErrorResponse) {
                    mResult.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(ResultType newData) {
                            setValue(Resource.error(
                                    ((ApiResponse.ApiErrorResponse<RequestType>) response).getErrorMessage(),
                                    newData));
                        }
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
//        Log.d("test", "Update resource:" +
//                " state=" + newValue.mState +
//                ", msg=" + newValue.mMessage +
//                ", data=" + newValue.mData);
        if (mResult.getValue() != newValue) {
            mResult.setValue(newValue);
        }
    }

    @WorkerThread
    protected abstract void saveCallResult(RequestType item);

    @MainThread
    protected abstract boolean shouldFetch(ResultType data);

    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
    //========================================================

}
