package com.grobo.iotmanager.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.grobo.iotmanager.R;
import com.grobo.iotmanager.nettools.utils.ARPInfo;
import com.grobo.iotmanager.nettools.portscanning.PortScan;
import com.grobo.iotmanager.nettools.subnet.Device;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

	private Context context;

	public HomeListAdapter(Context context) {
		this.context = context;
	}

	private List<Device> deviceList;

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_ip_list, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		if (deviceList != null) {
			Device current = deviceList.get(position);
			if (current != null) {
				holder.ip.setText(current.toString());

				String mac = ARPInfo.getMACFromIPAddress(current.ip);
				holder.hostName.setText(mac);

				try {
					PortScan.onAddress(current.ip).setPortsAll().setMethodTCP().doScan(new PortScan.PortListener() {
						@Override
						public void onResult(int portNo, boolean open) {
							if (open) {
								((AppCompatActivity) context).runOnUiThread(() -> holder.ports.append(portNo + ", "));
							}
						}

						@Override
						public void onFinished(ArrayList<Integer> openPorts) {
						}
					});
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

			}
		}
	}

	@Override
	public int getItemCount() {
		if (deviceList != null) return deviceList.size();
		else return 0;
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		CardView cardRoot;

		TextView ip;
		TextView hostName;
		TextView ports;

		ViewHolder(@NonNull View itemView) {
			super(itemView);
			cardRoot = itemView.findViewById(R.id.card_root);

			ip = itemView.findViewById(R.id.ip);
			hostName = itemView.findViewById(R.id.host_name);
			ports = itemView.findViewById(R.id.ports);
		}
	}

	public void setDeviceList(List<Device> data) {
		deviceList = data;
		notifyDataSetChanged();
	}

	public void addItem(Device data) {
		if (deviceList == null) deviceList = new ArrayList<>();
		int l = deviceList.size();
		deviceList.add(l, data);
		notifyItemInserted(l);
	}

}