package com.grobo.iotmanager.devices;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity
public class SavedDevice {

	@PrimaryKey(autoGenerate = true)
	private int id;
	private int authType;

	private String mac;
	private String name;

	private String localIp;
	private int port;
	private String publicUrl;

	private String rootPath;
	private String socketPath;



}
