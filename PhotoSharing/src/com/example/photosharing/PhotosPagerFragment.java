package com.example.photosharing;


import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotosPagerFragment extends Fragment implements Constants {
	private View view;
	private Photo thisPhoto;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder()
    .cacheInMemory(true)
    .cacheOnDisk(true)
    .build();
	private boolean edit = false;
	
	private static final String DELETE_TEXT = "Delete this photo";
	private static final String PHOTO_DELETED = "Photo deleted.";
	
	
	public static PhotosPagerFragment newInstance(Photo photo, boolean edit){
		Bundle arguments = new Bundle();
		
		arguments.putParcelable(PARCE_PHOTO, photo);
		arguments.putBoolean(CAN_EDIT, edit);
		PhotosPagerFragment fragment = new PhotosPagerFragment();
		fragment.setArguments(arguments);
		return fragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		thisPhoto = (Photo)getArguments().getParcelable(PARCE_PHOTO);
		edit = getArguments().getBoolean(CAN_EDIT);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pager_photo_item, container, false);
		
		setImage();
		return view;
	}
	
	public void setImage(){
		String imagePath = ROOT_PATH + thisPhoto.getPath();
		ImageView imageView = (ImageView)view.findViewById(R.id.pager_image);
		imageLoader.displayImage(imagePath, imageView, options);
		
		setCaption();
	}
	
	public void setCaption(){
		TextView captionView = (TextView)view.findViewById(R.id.pager_caption);
		captionView.setText("Caption:  " + thisPhoto.getCaption());
		initDeleteButton();
	}
	
	public void initDeleteButton(){
		Button deleteButton = (Button)view.findViewById(R.id.pager_delete);
		if(edit){
			deleteButton.setVisibility(View.VISIBLE);
			deleteButton.setText(DELETE_TEXT);
		}else{
			deleteButton.setVisibility(View.GONE);
		}
		
		deleteButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				deletePhoto();
			}
		});
	}
	
	//delete entry from database (but not actual image file.  I haven't written that yet)
	public void deletePhoto(){
		String deleteId = Integer.toString(thisPhoto.getId());
		ApiClient.delete(ROOT_PATH + "api/v1/myphotos/" + deleteId, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONObject response) {
				try{
					if(!response.getBoolean("error")){
						refreshPager();
					}
				}catch(JSONException joe){
					joe.printStackTrace();
				}
			}
		});
	}
	
	//refresh pager on delete
	public void refreshPager(){
		Toast.makeText(getActivity(), PHOTO_DELETED, Toast.LENGTH_SHORT).show();
		((PhotosPagerActivity)getActivity()).setup();
	}
}
