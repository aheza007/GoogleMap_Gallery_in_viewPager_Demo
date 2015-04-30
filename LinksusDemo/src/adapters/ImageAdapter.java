package adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dev155.linksusdemo.GlobalObject;
import com.dev155.linksusdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ImageAdapter extends BaseAdapter {
	List<File> mList;
	LayoutInflater mInflater;
	Context mContext;
	SparseBooleanArray mSparseBooleanArray;
	ImageLoader myimageLoader;

	public ImageAdapter(Context context, List<File> imageList) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mSparseBooleanArray = new SparseBooleanArray();
		mList = new ArrayList<File>();
		this.mList = imageList;
		myimageLoader=GlobalObject.mImageLoader;
		// imageloader.displayImage("file://, imageView)
	}

	public ArrayList<File> getCheckedItems() {
		ArrayList<File> mTempArry = new ArrayList<File>();

		for (int i = 0; i < mList.size(); i++) {
			if (mSparseBooleanArray.get(i)) {
				mTempArry.add(mList.get(i));
			}
		}

		return mTempArry;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.photoitem, null);
			convertView.setTag(R.id.imageView1, convertView.findViewById(R.id.imageView1));
		}

		CheckBox mCheckBox = (CheckBox) convertView
				.findViewById(R.id.checkBox1);
//		final ImageView imageView = (ImageView) convertView
//				.findViewById(R.id.imageView1);
		final ImageView imageView=(ImageView) convertView.getTag(R.id.imageView1);
		myimageLoader.displayImage("file://" + mList.get(position),
				imageView, GlobalObject.options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(Bitmap loadedImage) {
						Animation anim = AnimationUtils.loadAnimation(mContext,
								R.anim.fade_in);
						imageView.setAnimation(anim);
						anim.start();
					}
				});

		mCheckBox.setTag(position);
		mCheckBox.setChecked(mSparseBooleanArray.get(position));
		mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);

		return convertView;
	}

	OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
		}
	};

}
