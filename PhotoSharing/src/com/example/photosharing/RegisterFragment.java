package com.example.photosharing;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements Constants{
	private View view;
	private static final String TOAST_PROMPT = "Enter a username and password";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_register, container, false);
		initRegisterButton();
		return view;
	}
	
	public void initRegisterButton(){
		Button registerButton = (Button)view.findViewById(R.id.register_submit);
		registerButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText usernameText = (EditText)view.findViewById(R.id.register_username);
				EditText passwordText = (EditText)view.findViewById(R.id.register_password);
				String username = usernameText.getText().toString();
				String password = passwordText.getText().toString();
				if(username != "" && password != ""){
					attemptRegister(username, password);
				}else{
					Toast.makeText(getActivity(), TOAST_PROMPT, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public void attemptRegister(String username, String password){
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		ApiClient.post(ROOT_PATH + "register", params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				try{
					if(!response.getBoolean("error")){
						returnToStart();
					}else{
						Toast.makeText(getActivity(), response.getString("reason"), Toast.LENGTH_SHORT).show();
					}
				}catch(JSONException joe){
					joe.printStackTrace();
				}
			}
		});
	}
	
	public void returnToStart(){
		Intent intent = new Intent();
		intent.putExtra(LOGIN_DATA, true);
		getActivity().setResult(0, intent);
		getActivity().finish();
	}

}
