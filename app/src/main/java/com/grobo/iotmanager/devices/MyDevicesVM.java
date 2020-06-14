package com.grobo.iotmanager.devices;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyDevicesVM extends ViewModel {

	private MutableLiveData<String> mText;

	public MyDevicesVM() {
		mText = new MutableLiveData<>();
		mText.setValue("This is home fragment");
	}

	public LiveData<String> getText() {
		return mText;
	}
}