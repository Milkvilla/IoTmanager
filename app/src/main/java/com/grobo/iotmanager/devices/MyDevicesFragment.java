package com.grobo.iotmanager.devices;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grobo.iotmanager.databinding.FragmentMyDevicesBinding;

public class MyDevicesFragment extends Fragment {

	public MyDevicesFragment() {
	}

	private FragmentMyDevicesBinding binding;
	private Context context;

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		this.context = context;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentMyDevicesBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}
}