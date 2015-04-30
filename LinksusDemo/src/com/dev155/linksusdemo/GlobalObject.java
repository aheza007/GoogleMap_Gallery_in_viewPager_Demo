
package com.dev155.linksusdemo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GlobalObject {
	public static List<File> selectedFiles=new ArrayList<File>();
	public static ImageLoader mImageLoader;
	public static DisplayImageOptions options;
}
