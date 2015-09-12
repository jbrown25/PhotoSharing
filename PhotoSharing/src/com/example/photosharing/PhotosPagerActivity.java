package com.example.photosharing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;


public class PhotosPagerActivity extends FragmentActivity implements Constants {
	private ViewPager viewPager;
	private ArrayList<Photo> photos;
	private int userId;
	private int currentItem = 0;
	private boolean edit = false;
	
	private static String CURRENT_ITEM = "current_item";
	

	
	@Override
	protected void onCreate(Bundle savedState) {
		// TODO Auto-generated method stub
		super.onCreate(savedState);
		if(savedState != null) currentItem = savedState.getInt(CURRENT_ITEM);
		setup();
	}
	



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(CURRENT_ITEM, currentItem);
	}




	public void setup(){
		userId = getIntent().getIntExtra(USER_ID, 0);
		photos = new ArrayList<Photo>();
		
		try{
			getJson();
		}catch(JSONException joe){
			joe.printStackTrace();
		}
	}
	
	//get images for selected user as JSON
	public void getJson() throws JSONException {
		String extension;
		if(userId > 0){
			extension = "api/v1/photos/" + Integer.toString(userId); 
		}else{
			extension = "api/v1/myphotos";
		}
		
		ApiClient.get(ROOT_PATH + extension, null, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				try{
					parse(response);
				}catch(JSONException joe){
					joe.printStackTrace();
				}
			}
		});
	}
	
	//get Photo models from JSON
	public void parse(JSONObject response) throws JSONException {
		if(!response.getBoolean(ERROR_KEY)){
			edit = response.getBoolean(EDIT_KEY);
			JSONArray photosArray = response.getJSONArray(PHOTOS_KEY);
			for(int i = 0; i < photosArray.length(); i++){
				Photo photo = new Photo();
				JSONObject object = photosArray.getJSONObject(i);
				photo = Photo.getFromJson(object);
				photos.add(photo);
			}
			setupViewPager();
		}
	}
	
	
	public void setupViewPager(){
		viewPager = new ViewPager(this);
		viewPager.setId(R.id.viewPager);
		setContentView(viewPager);
		
		
		FragmentManager manager = getSupportFragmentManager();
		viewPager.setAdapter(new FragmentStatePagerAdapter(manager){

			@Override
			public Fragment getItem(int position) {
				Photo photo = photos.get(position);
				return PhotosPagerFragment.newInstance(photo, edit);
			}

			@Override
			public int getCount() {
				return photos.size();
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int pos) {
				currentItem = pos;
			}
		});
		
		viewPager.setCurrentItem(currentItem);
	}
}
