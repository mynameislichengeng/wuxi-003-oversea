package com.wizarpos.pay.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wizarpos.atool.tool.Tools;

public class FileUtil {

	public static String saveBitmap(Bitmap bitmap){
		String _path = Tools.getSDPath() + File.separator + "common" + File.separator;
		File file = new File(_path);
		if (!file.exists()) {
			file.mkdirs();
		}
		String filePath = _path + System.currentTimeMillis() + ".jpg";
		com.wizarpos.atool.tool.Tools.writePng(bitmap, new File(filePath));
		try {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
		} catch (Exception e) {
		}

		return filePath;
	}

	public static String getFromAssets(Context context, String fileName){
		try {
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName) );
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line="";
			String Result="";
			while((line = bufReader.readLine()) != null)
				Result += line;
            inputReader.close();
            bufReader.close();
			return Result;
		} catch (Exception e) {
            //ignore
		}
        return null;
	}
	
	/**
	 * 读取ImageLoader保存的png
	 * @param path
	 * @return
	 */
	public static Bitmap readPng(String path) {
		File mfile = new File(path);
		if (mfile.exists()) {// 若该文件存在
			Bitmap bm = BitmapFactory.decodeFile(path);
			return bm;
		}
		return null;
	}
}
