package com.oscarliang.elibrary.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

@RunWith(JUnit4.class)
public class ApiResponseTest {

    @Test
    public void exception() {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        assertEquals("foo", ((ApiResponse.ApiErrorResponse<String>) apiResponse.create(new Exception("foo")))
                .getErrorMessage());
    }

    @Test
    public void success() {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        assertEquals("foo", ((ApiResponse.ApiSuccessResponse<String>) apiResponse.create(Response.success("foo")))
                .getBody());
    }

    @Test
    public void error() {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Response<String> response = Response.error(
                400,
                ResponseBody.create(MediaType.parse("application/txt"), "blah"));
        assertEquals("blah", ((ApiResponse.ApiErrorResponse<String>) apiResponse.create(response))
                .getErrorMessage());
    }

}