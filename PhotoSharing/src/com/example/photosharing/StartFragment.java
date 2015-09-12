package com.example.photosharing;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class StartFragment extends Fragment implements Constants {
	private View view;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_start, container, false);
		initLoginButton();
		initRegisterButton();
		return view;
	}
	
	
	
	public void initLoginButton(){
		Button loginButton = (Button)view.findViewById(R.id.login);
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivityForResult(intent, 0);
			}			
		});
	}
	
	public void initRegisterButton(){
		Button registerButton = (Button)view.findViewById(R.id.register);
		registerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RegisterActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null) return;
		if(data.getBooleanExtra(LOGIN_DATA, false)){
			Intent intent = new Intent(getActivity(), PhotosListActivity.class);
			startActivity(intent);
		}
	}
}
