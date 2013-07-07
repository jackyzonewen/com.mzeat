package com.mzeat.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

public class LocalImageFetcher extends ImageResizer {
	public LocalImageFetcher(Context context, int imageSize) {
		super(context, imageSize);
	}

	public LocalImageFetcher(Context context, int imageWidth, int imageHeight) {
		super(context, imageWidth, imageHeight);
	}

	@Override
	protected Bitmap processBitmap(Object path) {
		return ImageResizer.decodeSampledBitmapFromFile((String) path,
				mImageWidth, mImageHeight);
	}
}
