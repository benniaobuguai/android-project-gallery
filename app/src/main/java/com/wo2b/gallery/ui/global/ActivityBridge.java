package com.wo2b.gallery.ui.global;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.model.image.PhotoInfoSet;
import com.wo2b.gallery.ui.image.ImageViewerActivity;

/**
 * Activity间通讯的辅助类
 * 
 * @author 笨鸟不乖
 * 
 */
public class ActivityBridge
{

	/**
	 * 
	 * @param activity
	 * @param photoInfoSet
	 * @param position
	 * @param cacheDir
	 */
	public static void gotoImageViewerActivity(Activity activity, PhotoInfoSet photoInfoSet, int position,
			String cacheDir)
	{
		Bundle bundle = new Bundle();
		bundle.putSerializable(GIntent.EXTRA_IMAGE_SET, photoInfoSet);
		bundle.putInt(GIntent.EXTRA_POSITION, position);
		bundle.putString(GIntent.EXTRA_DIRECTORY, cacheDir);

		Intent intent = new Intent(activity, ImageViewerActivity.class);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

}
