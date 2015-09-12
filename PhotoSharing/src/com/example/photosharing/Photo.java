package com.example.photosharing;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable, Constants {
	private int id;
	private int user_id;
	private String path;
	private String caption;
	
	public void setId(int id) {
		this.id = id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getId() {
		return id;
	}
	public int getUser_id() {
		return user_id;
	}
	public String getPath() {
		return path;
	}
	public String getCaption() {
		return caption;
	}
	
	
	
	
	public static Photo getFromJson(JSONObject jsonObject){
		Photo photo = new Photo();
		try{
			photo.id = jsonObject.getInt(ID_KEY);
			photo.user_id = jsonObject.getInt(USER_ID_KEY);
			photo.path = jsonObject.getString(PATH_KEY);
			photo.caption = jsonObject.getString(CAPTION_KEY);
		}catch(JSONException joe){
			joe.printStackTrace();
			return null;
		}		
		return photo;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// TODO Auto-generated method stub
		parcel.writeInt(id);
		parcel.writeInt(user_id);
		parcel.writeString(path);
		parcel.writeString(caption);
	}
	
	public static final Parcelable.Creator<Photo> CREATOR = new Creator<Photo>(){
		public Photo createFromParcel(Parcel source){
			Photo mPhoto = new Photo();
			mPhoto.id = source.readInt();
			mPhoto.user_id = source.readInt();
			mPhoto.caption = source.readString();
			mPhoto.path = source.readString();
			
			return mPhoto;
		}
		public Photo[] newArray(int size){
			return new Photo[size];
		}
	};
}
