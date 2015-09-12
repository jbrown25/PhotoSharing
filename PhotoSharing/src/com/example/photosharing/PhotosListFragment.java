package com.example.photosharing;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PhotosListFragment extends ListFragment implements Constants {
	private ArrayList<Photo> photos = new ArrayList<Photo>();
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder()
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .build();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		try{
			getIndex();
		}catch(JSONException joe){
			joe.printStackTrace();
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list_menu, menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.your_photos:
			Intent yourPhotosIntent = new Intent(getActivity(), PhotosPagerActivity.class);
			yourPhotosIntent.putExtra(USER_ID, -100);
			startActivity(yourPhotosIntent);
			return true;
		case R.id.upload_photo:
			Intent uploadPhotosIntent = new Intent(getActivity(), UploadActivity.class);
			startActivity(uploadPhotosIntent);
			return true;
		case R.id.logout_menu:
			try{
				logout();
			}catch(JSONException joe){
				joe.printStackTrace();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	//get index JSON
	public void getIndex() throws JSONException {
		ApiClient.get(ROOT_PATH + "api/v1/photos", null, new JsonHttpResponseHandler(){

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
	
	//parse index JSON, convert to model and push to ArrayList
	public void parse(JSONObject response) throws JSONException {
		if(!response.getBoolean(ERROR_KEY)){
			JSONArray photosArray = response.getJSONArray(PHOTOS_KEY);
			for(int i = 0; i < photosArray.length(); i++){
				Photo photo = new Photo();
				JSONObject object = photosArray.getJSONObject(i);
				photo = Photo.getFromJson(object);
				photos.add(photo);
			}
		}
		
		//create list
		PhotosListAdapter adapter = new PhotosListAdapter(photos);
		setListAdapter(adapter);
	}
	
	public void logout() throws JSONException{
		ApiClient.post(ROOT_PATH + "logout", null, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONObject response) {
				try{
					if(!response.getBoolean(ERROR_KEY)){
						getActivity().finish();
					}
				}catch(JSONException joe){
					joe.printStackTrace();
				}
				
			}
		});
	}
	
	
	
	private class PhotosListAdapter extends ArrayAdapter<Photo> {
		public PhotosListAdapter(ArrayList<Photo> photosArrayList){
			super(getActivity(), 0, photos);
		}
		
		public View getView(int position, View newView, ViewGroup parent){
			if(newView == null){
				newView = getActivity().getLayoutInflater().inflate(R.layout.list_photo_item, null);
			}
			
			Photo photo = getItem(position);
			TextView listUserName = (TextView)newView.findViewById(R.id.list_username);
			listUserName.setText("user ID:  " + Integer.toString(photo.getId()));
			
			ImageView thumbnail = (ImageView)newView.findViewById(R.id.list_thumb);
			String imagePath = THUMB_PATH + photo.getPath();
			imageLoader.displayImage(imagePath, thumbnail, options);
			
			return newView;
		}
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
			
		Photo photo = (Photo)(getListAdapter().getItem(position));
		
		Intent intent = new Intent(getActivity(), PhotosPagerActivity.class);
		intent.putExtra(USER_ID, photo.getUser_id());
		startActivity(intent);
	}

}
