package com.luxg.course.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import com.lidroid.xutils.BitmapUtils;
import com.luxg.course.widget.basewise.Constant;

/**
 * Created by lingcheng on 15/10/26.
 */
public class ImageUtil {
	private static ImageUtil ourInstance = new ImageUtil();
	private BitmapUtils bitmapUtils ;
	public static ImageUtil getInstance() {
		return ourInstance;
	}

	public void displayImage(Context context, ImageView image, String url) {
		bitmapUtils = new BitmapUtils(context);
		if (context instanceof FragmentActivity) {
			Activity a = (Activity) context;
			if (a.isFinishing())
				return;
		}
		if (!TextUtils.isEmpty(url)) {
			if (!url.startsWith("http://")){
				url = Constant.URL + url;
			}
			bitmapUtils.display(image,url);
		}
	}



}
