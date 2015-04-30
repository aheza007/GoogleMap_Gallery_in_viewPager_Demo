package com.dev155.linksusdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class GalleryPhotos extends Fragment {

	private ArrayList<String> imageUrls;
	private DisplayImageOptions options;
	// private ImageAdapter imageAdapter;
	ImageLoader imageLoader;
	private DirectoryAdapter mDirectoryAdapter;

	// List<File> selectedItems;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.directory_in_gallery,
				container, false);
		imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext())
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(1500000)
				// 1.5 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.enableLogging() // Not necessary in common
				.build();
		imageLoader.init(config);
		final String[] columns = { MediaStore.Images.Media.DATA,
				MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File secondPath = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

		@SuppressWarnings("deprecation")
		Cursor imagecursor = getActivity().managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");
		this.imageUrls = new ArrayList<String>();
		final HashMap<String, List<File>> allFolderImage = new HashMap<String, List<File>>();
		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(imagecursor.getString(dataColumnIndex));
			// files.add(new File(imagecursor.getString(dataColumnIndex)));

			File file = new File(imagecursor.getString(dataColumnIndex));
			String key = file.getParentFile().getName();
			if (allFolderImage.containsKey(key)) {
				allFolderImage.get(key).add(file);
			} else {
				List<File> items = new ArrayList<File>();
				items.add(file);
				allFolderImage.put(key, items);
			}
			System.out.print("====> ARRAY path =>" + imageUrls.get(i));
		}
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_action_user)
				.showImageForEmptyUri(R.drawable.ic_action_user)
				.cacheInMemory().cacheOnDisc().build();

		GridView gridview = (GridView) rootview
				.findViewById(R.id.gridview_gallery);
		mDirectoryAdapter = new DirectoryAdapter(getActivity(), allFolderImage);
		gridview.setAdapter(mDirectoryAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// List<File> selectedItems=new ArrayList<File>();
				// selectedItems=mDirectoryAdapter.getSelectedItem(position);
				GlobalObject.mImageLoader = imageLoader;
				GlobalObject.selectedFiles = mDirectoryAdapter
						.getSelectedItem(position);
				GlobalObject.options = options;
				Intent openGAllery = new Intent(getActivity(),
						GalleryActivity.class);
				getActivity().startActivity(openGAllery);

				// FragmentTransaction
				// fragTrans=getActivity().getSupportFragmentManager().beginTransaction();
				// fragTrans.add(R.id.container_new_design_main,new
				// FragmentGallery(),Integer.toString(R.id.fragment_gallery));
				// fragTrans.addToBackStack(null);
				// fragTrans.commit();
			}
		});
		return rootview;
	}

	// private void open_directory(View rootview) {
	//
	// imageAdapter = new ImageAdapter(getActivity(), imageUrls);
	// GridView gridview = (GridView) rootview.findViewById(R.id.gridview);
	// gridview.setAdapter(imageAdapter);
	// }

	public void listf(String directoryName, ArrayList<File> files,
			ArrayList<File> fileDiretory) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				if (file.listFiles(new ImageFileFilter()).length > 0)
					fileDiretory.add(file);
				listf(file.getAbsolutePath(), files, fileDiretory);
			}
		}

	}

	@Override
	public void onStop() {
		imageLoader.stop();
		super.onStop();
	}

	@Override
	public void onPause() {
		imageLoader.stop();
		super.onPause();
	}

	// public void btnChoosePhotosClick(View v) {
	//
	// ArrayList<File> selectedItems = imageAdapter.getCheckedItems();
	// Toast.makeText(getActivity(),
	// "Total photos selected: " + selectedItems.size(),
	// Toast.LENGTH_SHORT).show();
	// // Log.d(MultiPhotoSelectActivity.class.getSimpleName(),
	// // "Selected Items: " + selectedItems.toString());
	// }

	class DirectoryAdapter extends BaseAdapter {
		Context mContext;
		LayoutInflater mInflater;
		HashMap<String, List<File>> mDirectory;
		List<String> keys;

		public DirectoryAdapter(Context pContext,
				HashMap<String, List<File>> pDirectory) {
			mContext = pContext;
			mInflater = LayoutInflater.from(mContext);
			mDirectory = new HashMap<String, List<File>>();
			this.mDirectory = pDirectory;
			keys = new ArrayList<String>();
			for (String key : mDirectory.keySet()) {
				keys.add(key);
			}
		}

		@Override
		public int getCount() {
			return keys.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<File> getSelectedItem(int position) {

			return mDirectory.get(keys.get(position));
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.gallery_directory_item, null);
				convertView.setTag(R.id.recent_added_in_directory, convertView
						.findViewById(R.id.recent_added_in_directory));
				convertView.setTag(R.id.textView_directory_name,
						convertView.findViewById(R.id.textView_directory_name));
				convertView
						.setTag(R.id.number_of_media_in_directory,
								convertView
										.findViewById(R.id.number_of_media_in_directory));
			}
			// final ImageView imageView_recent = (ImageView) convertView
			// .findViewById(R.id.recent_added_in_directory);
			// TextView directoryName = (TextView) convertView
			// .findViewById(R.id.textView_directory_name);
			// TextView numberOfMedia = (TextView) convertView
			// .findViewById(R.id.number_of_media_in_directory);
			final ImageView imageView_recent = (ImageView) convertView
					.getTag(R.id.recent_added_in_directory);
			TextView directoryName = (TextView) convertView
					.getTag(R.id.textView_directory_name);
			TextView numberOfMedia = (TextView) convertView
					.getTag(R.id.number_of_media_in_directory);
			String key = keys.get(position);
			directoryName.setText(key);
			// convertView.setTag(mDirectory.get(key));
			// List<File> items=new ArrayList<File>();
			// items=mDirectory.containsKey(key)?mDirectory.get(key):null;
			// int count=mDirectory.get(key).size();
			numberOfMedia.setText("" + mDirectory.get(key).size());
			imageLoader.displayImage("file://" + mDirectory.get(key).get(0),
					imageView_recent, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(Bitmap loadedImage) {
							Animation anim = AnimationUtils.loadAnimation(
									mContext, R.anim.fade_in);
							imageView_recent.setAnimation(anim);
							anim.start();
						}
					});
			return convertView;
		}
	}

}
