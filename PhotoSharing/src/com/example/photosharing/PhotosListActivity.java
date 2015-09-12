package com.example.photosharing;

import android.support.v4.app.Fragment;

public class PhotosListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new PhotosListFragment();
	}
}
