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
import android.widget.TextView;

public class LoginFragment extends Fragment implements Constants {
	private View view;
	private EditText usernameText;
	private EditText passwordText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_login, container, false);
		
		//Check if user is logged in
		try {
			checkLogin();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}
	
	//login form
	public void initLogin(){
		Button submit = (Button)view.findViewById(R.id.login_button);
		usernameText = (EditText)view.findViewById(R.id.username);
		passwordText = (EditText)view.findViewById(R.id.password);
		
		usernameText.setVisibility(View.VISIBLE);
		passwordText.setVisibility(View.VISIBLE);
		submit.setVisibility(View.VISIBLE);
		
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String username = usernameText.getText().toString();
				String password = passwordText.getText().toString();
				try{
					submitCreds(username, password);
				}catch(JSONException joe){
					joe.printStackTrace();
				}
			}
		});
	}
	
	//login submission
	public void submitCreds(String username, String password) throws JSONException {
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		
		ApiClient.post(ROOT_PATH + "login", params, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONObject response) {
				TextView responseText = (TextView)view.findViewById(R.id.login_text);
				try{
					if(!response.getBoolean("error")){
						responseText.setText("logged in as " + response.getString("username"));
						returnToStart();
						return;
					}else{
						responseText.setText("Error.  Try retyping your credentials");
					}
				}catch(JSONException joe){
					joe.printStackTrace();
				}
			}
		});
	}
	
	//Check if user is already logged in.  If so, go to back to StartFragment
	public void checkLogin() throws JSONException {
		ApiClient.get(ROOT_PATH + "check", null, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONObject response) {
				try{
					if(!response.getBoolean("error")){
						returnToStart(); //user is logged in, got to list
					}else{
						initLogin(); //user is not logged in, show login form
					}
				}catch(JSONException joe){
					joe.printStackTrace();
				}
				return;
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
