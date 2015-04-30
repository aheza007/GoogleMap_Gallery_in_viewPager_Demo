package com.dev155.linksusdemo;

import java.util.Stack;

import adapters.ViewPagerAdapter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class LinksMainActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener {

	public static FragmentManager fragmentManager;
	GoogleApiClient mGoogleApiClient;
	Location mLastLocation;
	// public static ProgressDialog progressD;
	public static double mLatitude;
	public static double mLongitude;
	Stack<Integer> pageHistory;
	boolean saveToHistory;
	ViewPager pager;
	int currentPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		// Request Permission to display the Progress Bar...
		this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(true);
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		fragmentManager = getSupportFragmentManager();
		// getCurrentLocation();
		// progressD= new ProgressDialog(this,AlertDialog.THEME_HOLO_LIGHT);
		// progressD.setMessage("Loading Events ...");
		// progressD.show();
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Check if first run
		pager= (ViewPager) findViewById(R.id.pager_main_activity);
		ViewPagerAdapter adapter = new ViewPagerAdapter(
				getSupportFragmentManager());
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		pageHistory = new Stack<Integer>();
		// Bind the Tabs to the ViewPager
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs_main_activity);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if(saveToHistory)
	                pageHistory.push(Integer.valueOf(arg0));
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		saveToHistory = true;
		adapter.notifyDataSetChanged();
		tabs.setViewPager(pager);
		
		Log.d("FINISHING", "YES");
	}

	@Override
	public void onBackPressed() {
		if(pageHistory.empty())
	        super.onBackPressed();
	    else {
	        saveToHistory = false;
	        pager.setCurrentItem(pageHistory.pop().intValue());
	        saveToHistory = true;
	    }
	}
	protected synchronized void getCurrentLocation() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		if (mLastLocation != null) {
			mLatitude = mLastLocation.getLatitude();
			mLongitude = mLastLocation.getLongitude();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}
}
