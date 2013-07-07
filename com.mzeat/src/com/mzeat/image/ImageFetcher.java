/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mzeat.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mzeat.util.BuildConfig;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL. ImageResizer的一个简单子类，从一个URL取得和调整图片的大小。从ImageResizer继承。
 */
public class ImageFetcher extends ImageResizer {
	private static final String TAG = "ImageFetcher";
	private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
	public static final String HTTP_CACHE_DIR = "http";

	/**
	 * ImageFetcher构造方法
	 * Initialize providing a target image width and height for the processing
	 * images.给处理的图片提供目标图片的宽度和高度的初始化
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageFetcher(Context context, int imageWidth, int imageHeight) {
		super(context, imageWidth, imageHeight);
		init(context);
	}

	/**
	 * Initialize providing a single target image size (used for both width and
	 * height);
	 * 给单一目标图片尺寸提供初始化
	 * @param context
	 * @param imageSize
	 */
	public ImageFetcher(Context context, int imageSize) {
		super(context, imageSize);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		checkConnection(context);
	}

	/**
	 * Simple network connection check. 
	 * 简单的网络连接检查
	 * 
	 * @param context
	 */
	private void checkConnection(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			//Toast.makeText(context, "No network connection found.",
			//		Toast.LENGTH_LONG).show();
			Log.e(TAG, "checkConnection - no connection found");
		}
	}

	/**
	 * The main process method, which will be called by the ImageWorker in the
	 * AsyncTask background thread. 
	 * 在AsyncTask的后台线程调用ImageWorker的主要处理方法。
	 * 
	 * @param data
	 *            The data to load the bitmap, in this case, a regular http URL
	 * @return The downloaded and resized bitmap
	 */
	private Bitmap processBitmap(String data) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "processBitmap - " + data);
		}

		// Download a bitmap, write it to a file
		// 下载位图
		final File f = downloadBitmap(mContext, data);

		if (f != null) {
			// Return a sampled down version
			return decodeSampledBitmapFromFile(f.toString(), mImageWidth,
					mImageHeight);
		}

		return null;
	}

	@Override
	protected Bitmap processBitmap(Object data) {
		return processBitmap(String.valueOf(data));
	}

	/**
	 * Download a bitmap from a URL, write it to a disk and return the File
	 * pointer. This implementation uses a simple disk cache.
	 * 通过URL下载位图，把它写入内存并返回文件的指针。这个实现使用简单的内存缓存。
	 * @param context
	 *            The context to use
	 * @param urlString
	 *            The URL to fetch
	 * @return A File pointing to the fetched bitmap
	 */
	public static File downloadBitmap(Context context, String urlString) {
		final File cacheDir = DiskLruCache.getDiskCacheDir(context,
				HTTP_CACHE_DIR);

		final DiskLruCache cache = DiskLruCache.openCache(context, cacheDir,
				HTTP_CACHE_SIZE);

		final File cacheFile = new File(cache.createFilePath(urlString));

		if (cache.containsKey(urlString)) {
			if (BuildConfig.DEBUG) {
				Log.d(TAG, "downloadBitmap - found in http cache - "
						+ urlString);
			}
			return cacheFile;
		}

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "downloadBitmap - downloading - " + urlString);
		}

		Utils.disableConnectionReuseIfNecessary();
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;

		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			final InputStream in = new BufferedInputStream(
					urlConnection.getInputStream(), Utils.IO_BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(cacheFile),
					Utils.IO_BUFFER_SIZE);

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}

			return cacheFile;

		} catch (final IOException e) {
			Log.e(TAG, "Error in downloadBitmap - " + e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (out != null) {
				try {
					out.close();
				} catch (final IOException e) {
					Log.e(TAG, "Error in downloadBitmap - " + e);
				}
			}
		}

		return null;
	}

	/**
	 * Write a Bitmap to local file.
	 * 
	 * @param bitmap
	 *            bitmap to write
	 * @param fileName
	 *            local file name
	 * @param quality
	 *            0-100
	 * @return A File pointing to the fetched bitmap
	 */
	public static File bitmapToFile(Bitmap bitmap, String fileName, int quality) {
		File file = new File(fileName);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Sorry, the file can not be created");
			return null;
		} catch (IOException e) {
			Log.e(TAG, "IOException occurred when save upload file");
			return null;
		}
		return file;
	}
}
