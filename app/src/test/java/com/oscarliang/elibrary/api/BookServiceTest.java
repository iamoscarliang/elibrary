package com.oscarliang.elibrary.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.oscarliang.elibrary.util.LiveDataCallAdapterFactory;
import com.oscarliang.elibrary.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(JUnit4.class)
public class BookServiceTest {

    private MockWebServer mockWebServer;
    private BookService bookService;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createService() {
        mockWebServer = new MockWebServer();
        bookService = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BookService.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void search() throws IOException, InterruptedException, TimeoutException {
        enqueueResponse("search.json");
        ApiResponse<BookResponse> response = LiveDataTestUtil.getValue(
                bookService.getBooks("java", String.valueOf(10), String.valueOf(0)));

        assertNotNull(response);
        assertEquals(441, ((ApiResponse.ApiSuccessResponse<BookResponse>) response).getBody().getBookCount());
        assertEquals(10, ((ApiResponse.ApiSuccessResponse<BookResponse>) response).getBody().getBooks().size());
    }

    private void enqueueResponse(String fileName) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)));
    }

}