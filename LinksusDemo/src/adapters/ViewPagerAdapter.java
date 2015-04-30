package adapters;

import com.dev155.linksusdemo.ConnectionFragment;
import com.dev155.linksusdemo.EventMapFragment;
import com.dev155.linksusdemo.GalleryPhotos;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	String[] Titles = { "Events", "Connections", "Gallery" };

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Titles[position];
	}

	@Override
	public Fragment getItem(int position) {
		// if (position==0)
		//
		// else if (position==1)
		// return new ConnectionFragment() ;
		// else
		// return new GalleryPhotos();
		switch (position) {
			case 0:
				return new EventMapFragment();
	
			case 1:
				return new ConnectionFragment();
	
			case 2:
				return new GalleryPhotos();
	
			default:
				return null;
		}
	}

	@Override
	public int getCount() {
		return Titles.length;
	}

}
