package com.example.photosharing;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class ApiClient {
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		client.get(url, params, responseHandler);
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
	
	public static void delete(String url, AsyncHttpResponseHandler responseHandler){
		client.delete(url, responseHandler);
	}
	
	public static void storeCookie(PersistentCookieStore cookie){
		client.setCookieStore(cookie);
	}
}
