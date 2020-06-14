package com.grobo.iotmanager.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grobo.iotmanager.databinding.FragmentHomeBinding;
import com.grobo.iotmanager.nettools.portscanning.PortScan;
import com.grobo.iotmanager.nettools.subnet.Device;
import com.grobo.iotmanager.nettools.subnet.SubnetDevices;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

	private HomeViewModel homeViewModel;
	private FragmentHomeBinding binding;
	private Context context;
	WifiManager wifiManager;

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		this.context = context;
	}

	private static final int PERMISSIONS_REQUEST_LOC = 102;
	private static final int PERMISSIONS_REQUEST_WIFI = 103;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		final RecyclerView recyclerView = binding.recyclerView;
		recyclerView.setLayoutManager(new LinearLayoutManager(context));

		final HomeListAdapter adapter = new HomeListAdapter(context);
		recyclerView.setAdapter(adapter);

		SubnetDevices.fromLocalAddress().setNoThreads(100).setTimeOutMillis(500).findDevices(new SubnetDevices.OnSubnetDeviceFound() {
			@Override
			public void onDeviceFound(final Device device) {
				((AppCompatActivity) context).runOnUiThread(() -> adapter.addItem(device));
			}

			@Override
			public void onFinished(ArrayList<Device> devicesFound) {
				((AppCompatActivity) context).runOnUiThread(() -> {
					Toast.makeText(context, "Scan finished!", Toast.LENGTH_SHORT).show();
				});
			}
		});

		checkPermission();

		try {
			PortScan.onAddress("192.168.43.78").setPortsAll().setTimeOutMillis(200).setNoThreads(100).setMethodTCP().doScan(new PortScan.PortListener() {
				@Override
				public void onResult(int portNo, boolean open) {
					if (open) Log.e("iotmanager port", "open: " + portNo);

				}

				@Override
				public void onFinished(ArrayList<Integer> openPorts) {
				}
			});
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	private void checkPermission() {
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOC);
		} else if (ContextCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.CHANGE_WIFI_STATE}, PERMISSIONS_REQUEST_WIFI);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST_LOC) {
			if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
				Toast.makeText(context, "Please grant location permission to allow GPS tracking", Toast.LENGTH_LONG).show();
			checkPermission();
		} else if (requestCode == PERMISSIONS_REQUEST_WIFI) {
			if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED)
				Toast.makeText(context, "Please grant wifi permission", Toast.LENGTH_LONG).show();
			checkPermission();
		}
	}

}