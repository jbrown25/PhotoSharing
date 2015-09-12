package com.example.photosharing;

import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Application;
import android.content.Context;

public class PhotoSharingApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		initImageLoaderConfiguration(getApplicationContext());
		setCookie();
	}
	
	public void initImageLoaderConfiguration(Context context){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.imageDownloader(new BaseImageDownloader(context))
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	public void setCookie(){
		PersistentCookieStore cookie = new PersistentCookieStore(this);
		ApiClient.storeCookie(cookie);
	}
	
}
