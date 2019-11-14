package com.wizarpos.pay.app;

import java.io.File;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ImageLoadApp extends Application {
	public static String fileDir = Environment.getExternalStorageDirectory()
			+ File.separator + "pay2" + File.separator;
	public static DisplayImageOptions options;

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader();
	}

	private void initImageLoader() {
		mkdir();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).memoryCacheExtraOptions(480, 800)
				.diskCacheExtraOptions(480, 800, null).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
				.diskCache(new UnlimitedDiscCache(new File(fileDir)))
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(1000)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(this)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getOptions() {
		if (options == null) {
			options = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
			// .showImageForEmptyUri(R.drawable.ic_empty) // resource or
			// drawable
			// .showImageOnFail(R.drawable.ic_error) // resource or drawable
			// .resetViewBeforeLoading(false) // default
			// .delayBeforeLoading(1000)
					.cacheInMemory(true) // default
					.cacheOnDisk(true) // default
					.considerExifParams(true) // default
					.build();
		}
		return options;
	}

	private void mkdir() {
		File file = new File(fileDir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

}
