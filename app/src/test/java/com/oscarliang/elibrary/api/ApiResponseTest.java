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
        assertEquals("foo", ((ApiResponse.ApiErrorResponse<Object>) ApiResponse.create(new Exception("foo")))
                .getErrorMessage());
    }

    @Test
    public void success() {
        assertEquals("foo", ((ApiResponse.ApiSuccessResponse<String>) ApiResponse.create(Response.success("foo")))
                .getBody());
    }

    @Test
    public void error() {
        Response<String> response = Response.error(
                400,
                ResponseBody.create(MediaType.parse("application/txt"), "blah"));
        assertEquals("blah", ((ApiResponse.ApiErrorResponse<String>) ApiResponse.create(response))
                .getErrorMessage());
    }

}
