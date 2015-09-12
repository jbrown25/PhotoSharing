package com.example.photosharing;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadFragment extends Fragment implements Constants {
	private View view;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private static final String TOKEN = "lW6uihjZr37o651zQx1IMcbzexPpeadPtTdlxe9R";
	private static final int FROM_DEVICE = 1;
	private static final int FROM_CAMERA = 2;
	
	private Uri uploadUri = null;
	private Button submitButton;
	private Button cancelButton;
	private ImageView previewImage;
	private EditText captionEdit;
	private TextView promptText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.activity_upload, container, false);
		
		initPromptViews();
		
		initFromFileButton();
		initFromCameraButton();
		
		initSubmitButton();
		initCancelButton();
		return view;
	}
	
	//set views for prompting the user on image selection
	public void initPromptViews(){
		previewImage = (ImageView)view.findViewById(R.id.preview_image);
		previewImage.setVisibility(View.GONE);
		
		promptText = (TextView)view.findViewById(R.id.submit_prompt);
		promptText.setVisibility(View.GONE);
		
		captionEdit = (EditText)view.findViewById(R.id.caption);
		captionEdit.setVisibility(View.GONE);
	}
	
	//button for getting picture from phone
	public void initFromFileButton(){
		Button fileButton = (Button)view.findViewById(R.id.from_file);
		fileButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, FROM_DEVICE);
			}
		});
	}
	
	//button for getting picture from camera
	public void initFromCameraButton(){
		Button cameraButton = (Button)view.findViewById(R.id.from_camera);
		cameraButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, FROM_CAMERA);
			}
		});
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null) {
			Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
			return;
		}
		if(requestCode == FROM_CAMERA || requestCode == FROM_DEVICE){
			uploadUri = data.getData();
			createPromptDialog();
		}
	}
	
	//make prompt visible
	public void createPromptDialog(){
		promptText.setVisibility(View.VISIBLE);
		submitButton.setVisibility(View.VISIBLE);
		cancelButton.setVisibility(View.VISIBLE);
		previewImage.setVisibility(View.VISIBLE);
		captionEdit.setVisibility(View.VISIBLE);
		imageLoader.displayImage(uploadUri.toString(), previewImage);
	}
	
	//yes, submit
	public void initSubmitButton(){
		submitButton = (Button)view.findViewById(R.id.submit_yes);
		submitButton.setVisibility(View.GONE);
		submitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				uploadImage(true);
			}
		});
	}
	
	//no, cancel
	public void initCancelButton(){
		cancelButton = (Button)view.findViewById(R.id.submit_no);
		cancelButton.setVisibility(View.GONE);
		cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				uploadImage(false);
			}
		});
	}
	
	//get id of prompt form, send send uri
	public void uploadImage(boolean notCancel){
		String caption = captionEdit.getText().toString();
		promptText.setVisibility(View.GONE);
		submitButton.setVisibility(View.GONE);
		cancelButton.setVisibility(View.GONE);
		previewImage.setVisibility(View.GONE);
		captionEdit.setText(null);
		captionEdit.setVisibility(View.GONE);
		if(notCancel){
			processUri(caption);
		}else{
			uploadUri = null;
		}
	}
	
	//get full path to image including file extension
	public void processUri(String caption){
		if(uploadUri != null){
			
			//I basically copied and pasted this from StackOverflow.
			/*****************/
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(uploadUri,
			        filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			/******************/
			 
			uploadFromLocalPath(picturePath, caption);
		}
	}
	
	//upload the image
	public void uploadFromLocalPath(String localPath, String caption){
		RequestParams params = new RequestParams();
		File picFile = new File(localPath);
		try{
			params.put(PHOTO_KEY, picFile);
			params.put(CAPTION_KEY, caption);
			params.put(TOKEN_KEY, TOKEN);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		ApiClient.post(ROOT_PATH + "api/v1/myphotos", params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				try{
					if(!response.getBoolean(ERROR_KEY)){
						String uploadMessage = response.getString(MESSAGE_KEY);
						Toast.makeText(getActivity(), uploadMessage, Toast.LENGTH_SHORT).show();
					}
				}catch(JSONException joe){
					joe.printStackTrace();
				}
				uploadUri = null;
			}
		});
	}
}
