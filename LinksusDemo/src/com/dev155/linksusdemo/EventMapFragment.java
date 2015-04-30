package com.dev155.linksusdemo;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressWarnings({ "unused", "deprecation" })
public class EventMapFragment extends Fragment {
	// implements
	// LocationSource,com.google.android.gms.location.LocationListener
	MapView mMapView;
	private static View view;
	private GoogleMap googleMap;
	private double latitude, longitude;
	FragmentActivity activity;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	Location location = null;
	static Location lastLocation = null;
	// Location newLocation=null;
	String json_string = "";

	// private FollowMeLocationSource followMeLocationSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;
		// (RelativeLayout)
		view = (RelativeLayout) inflater.inflate(R.layout.event_map_fragment,
				container, false);
		activity = getActivity();
		// mMapView = (MapView) view.findViewById(R.id.mapView);
		// mMapView.onCreate(savedInstanceState);
		// mMapView.onResume();// needed to get the map to display immediately
		// googleMap = mMapView.getMap();
		// latitude = 35.0000;
		// longitude = -98.0000;
		// followMeLocationSource=new FollowMeLocationSource();
		setupMap();
		return view;
	}

	@Override
	public void onResume() {
		// followMeLocationSource.getBestAvailableProvider();
		super.onResume();
	}

	private void setupMap() {
		// TODO Auto-generated method stub
		if (googleMap == null) {
			// googleMap =
			// ((SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.events_map)).getMap();
			int sa = LinksMainActivity.fragmentManager.getFragments().size();

			Log.d("ann", "" + sa);
			googleMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.events_map)).getMap();
			if (googleMap != null)
				setMapThen();
		}
	}

	JSONObject allEventsArroundMe;

	private void setMapThen() {
		// TODO Auto-generated method stub
		// latitude=googleMap.getMyLocation().getLatitude();
		// longitude=googleMap.getMyLocation().getLongitude();

		googleMap.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(getActivity().LOCATION_SERVICE);
		// Creating a criteria object to retrieve provider
		Criteria criteria = new Criteria();
		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting Current Location
		location = locationManager.getLastKnownLocation(provider);
		if (location != null && locationManager.isProviderEnabled(provider)) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		this.getActivity().runOnUiThread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {

				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT >= 9) {
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
							.permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
				try {
					if(location!=null){
						HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,
								TIMEOUT_MILLISEC);
						HttpConnectionParams.setSoTimeout(httpParams,
								TIMEOUT_MILLISEC);
						HttpClient client = new DefaultHttpClient(httpParams);
						String uri = "https://api.eventful.com/json/events/search?app_key=xkHk8NQpn2WqHvKj&within=25&page_size=500&units=mi&location="
								+ location.getLatitude()
								+ ','
								+ location.getLongitude();
						;
						// HttpPost postRequest=new
						// HttpPost("http://api.eventful.com/json/events/search?app_key=xkHk8NQpn2WqHvKj&within=50&page_size=1000&units=mi&location=35.6500,-97.4667");
						HttpGet getRequest = new HttpGet(uri);
						HttpResponse response = client.execute(getRequest);
						StatusLine statusLine = response.getStatusLine();
						int statusCode = statusLine.getStatusCode();
						// json_string = "";
						if (statusCode == 200) {
							json_string = EntityUtils.toString(response
									.getEntity());
							allEventsArroundMe = new JSONObject(json_string);
							JSONObject items = allEventsArroundMe
									.getJSONObject("events");
							JSONArray arrayOfAllEvents = items
									.getJSONArray("event");

							for (int i = 0; i < arrayOfAllEvents.length(); i++) {
								JSONObject event = arrayOfAllEvents
										.getJSONObject(i);
								googleMap.addMarker(new MarkerOptions()
										.position(
												new LatLng(
														event.getDouble("latitude"),
														event.getDouble("longitude")))
										.title(event.getString("title"))
										.snippet(event.getString("venue_name")));
							}
							getActivity()
									.setProgressBarIndeterminateVisibility(
											false);
							Log.i("sasda", "Dsds");
							googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
							googleMap.moveCamera(CameraUpdateFactory
									.newLatLngZoom(new LatLng(latitude,
											longitude), 14.0f));
							googleMap.animateCamera(
									CameraUpdateFactory.zoomTo(12), 2000, null);

						} else {
							Log.e(LinksMainActivity.class.toString(),
									"Failedet JSON object");
						}

					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (googleMap != null)
			setMapThen();

		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.events_map)).getMap(); // getMap is
																	// deprecated
			// Check if we were successful in obtaining the map.
			if (googleMap != null)
				setMapThen();
		}
	}

	/****
	 * The mapfragment's id must be removed from the FragmentManager or else if
	 * the same it is passed on the next time then app will crash
	 ****/
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (googleMap != null) {
			{
				try {
					FragmentTransaction trs = ((FragmentActivity) getActivity())
							.getSupportFragmentManager().beginTransaction();
					 Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.events_map));
//				SupportMapFragment map=((SupportMapFragment) getChildFragmentManager()
//						.findFragmentById(R.id.events_map));
					trs.remove(fragment).commitAllowingStateLoss();
					googleMap = null;
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
		
	}

	// @Override
	// public void onLocationChanged(Location arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	/*
	 * Our custom LocationSource. We register this class to receive location
	 * updates from the Location Manager and for that reason we need to also
	 * implement the LocationListener interface.
	 */
	private class FollowMeLocationSource implements LocationSource,
			LocationListener {

		private OnLocationChangedListener mListener;
		private LocationManager locationManager;
		private final Criteria criteria = new Criteria();
		private String bestAvailableProvider;
		/*
		 * Updates are restricted to one every 10 seconds, and only when
		 * movement of more than 10 meters has been detected.
		 */
		private final int minTime = 10000; // minimum time interval between
											// location updates, in milliseconds
		private final int minDistance = 100; // minimum distance between
												// location updates, in meters
		Location currentLocation;

		private FollowMeLocationSource() {
			// Get reference to Location Manager
			locationManager = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);

			// Specify Location Provider criteria
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			criteria.setAltitudeRequired(true);
			criteria.setBearingRequired(true);
			criteria.setSpeedRequired(true);
			criteria.setCostAllowed(true);
		}

		private void getBestAvailableProvider() {
			/*
			 * The preffered way of specifying the location provider (e.g. GPS,
			 * NETWORK) to use is to ask the Location Manager for the one that
			 * best satisfies our criteria. By passing the 'true' boolean we ask
			 * for the best available (enabled) provider.
			 */
			bestAvailableProvider = locationManager.getBestProvider(criteria,
					true);
			currentLocation = locationManager
					.getLastKnownLocation(bestAvailableProvider);
		}

		/*
		 * Activates this provider. This provider will notify the supplied
		 * listener periodically, until you call deactivate(). This method is
		 * automatically invoked by enabling my-location layer.
		 */
		@Override
		public void activate(OnLocationChangedListener listener) {
			// We need to keep a reference to my-location layer's listener so we
			// can push forward
			// location updates to it when we receive them from Location
			// Manager.
			mListener = listener;

			// Request location updates from Location Manager
			if (bestAvailableProvider != null) {
				locationManager.requestLocationUpdates(bestAvailableProvider,
						minTime, minDistance, this);
			} else {
				// (Display a message/dialog) No Location Providers currently
				// available.
			}
		}

		/*
		 * Deactivates this provider. This method is automatically invoked by
		 * disabling my-location layer.
		 */
		@Override
		public void deactivate() {
			// Remove location updates from Location Manager
			locationManager.removeUpdates(this);

			mListener = null;
		}

		@Override
		public void onLocationChanged(Location location) {
			/*
			 * Push location updates to the registered listener.. (this ensures
			 * that my-location layer will set the blue dot at the new/received
			 * location)
			 */
			if (mListener != null) {
				mListener.onLocationChanged(location);
			}
			currentLocation = location;
			/*
			 * ..and Animate camera to center on that location ! (the reason for
			 * we created this custom Location Source !)
			 */
			googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(
					location.getLatitude(), location.getLongitude())));
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public Location getCurrentLocation() {
			return currentLocation;
		}
	}

	// @Override
	// public void onLocationChanged(Location arg0) {
	// // TODO Auto-generated method stub
	// newLocation=arg0;
	// }
	//
	// @Override
	// public void activate(OnLocationChangedListener arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void deactivate() {
	// // TODO Auto-generated method stub
	//
	// }

}
