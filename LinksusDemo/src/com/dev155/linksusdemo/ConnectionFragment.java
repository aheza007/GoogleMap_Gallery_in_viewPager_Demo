package com.dev155.linksusdemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ClipData.Item;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ConnectionFragment extends Fragment {

	// String[] items = new String[] { };
	ListView itemsListView = null;
	SwipeRefreshLayout swipeContainer = null;
	FragmentActivity activity = null;
	ArrayAdapter<String> adapter = null;
	ArrayList<String> items = new ArrayList<String>() {
		{
			add("Connection");
			add("Connection1");
			add("Connection2");
			add("Connection3");
			add("Connection4");
		}
	};
	List<String> itemsnn = new ArrayList<String>() {
		{
			add("Connection0");
			add("Connection1");
			add("Connection2");
			add("Connection3");
			add("Connection4");
			add("Connection5");
			add("Connection6");
			add("Connection7");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_connections,
				container, false);
		activity = getActivity();
		swipeContainer = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipeContainer);
		itemsListView = (ListView) rootView.findViewById(R.id.lvItems);
		adapter = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_list_item_1, items);
		itemsListView.setAdapter(adapter);
		itemsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = (itemsListView == null || itemsListView
						.getChildCount() == 0) ? 0 : itemsListView
						.getChildAt(0).getTop();
				swipeContainer.setEnabled(firstVisibleItem == 0
						&& topRowVerticalPosition >= 0);
			}
		});
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							
							adapter.clear();
							adapter.notifyDataSetChanged();
							for (String string : itemsnn) {
								adapter.add(string);
							}
							adapter.notifyDataSetChanged();
							swipeContainer.setRefreshing(false);
							itemsnn.add("Connection" + itemsnn.size());
							itemsnn.add("Connection" + itemsnn.size());
						} catch (Exception e) {
							Log.d("CAUSE:", e.getMessage());
							e.printStackTrace();
						}
					}
				});
			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		return rootView;
	}
}
