package com.dev155.linksusdemo;

import java.io.File;
import java.io.FilenameFilter;

public class ImageFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File file, String filename) {
		if (file.isDirectory()) {
            return true;
        }
        else if (isImageFile(file.getAbsolutePath())) {
            return true;
        }
		return false;
	}
    /**
     * Checks the file to see if it has a compatible extension.
     * check supported media format ==> http://developer.android.com/guide/appendix/media-formats.html
     *||filePath.endsWith(".gif") || filePath.endsWith(".bmp")||filePath.endsWith(".jpeg")
     */
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        {
            return true;
        }
        return false;
    }
}
