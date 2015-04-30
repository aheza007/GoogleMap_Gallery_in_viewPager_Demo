package com.dev155.linksusdemo;

import adapters.ImageAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class GalleryActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_gallery);
		if(arg0==null){
			FragmentTransaction fragTrans=getSupportFragmentManager().beginTransaction();
			fragTrans.add(R.id.container_new_design_main,new FragmentGallery(),Integer.toString(R.id.fragment_gallery));
		//	fragTrans.addToBackStack(null);
			fragTrans.commit();
		}
	}
	class FragmentGallery extends Fragment{
		ImageAdapter mImageAdapter;
		public FragmentGallery(){
			
		}
		@Override
		public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
			View rootView=inflater.inflate(R.layout.grid_gallery,container, false);
			mImageAdapter=new ImageAdapter(getActivity(), GlobalObject.selectedFiles);
			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
			gridview.setAdapter(mImageAdapter);
			return rootView;
		}
	}
}
