package com.mzeat.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.R;
import com.mzeat.image.ImageCache;
import com.mzeat.image.ImageCache.ImageCacheParams;
import com.mzeat.image.ImageFetcher;
import com.mzeat.image.ImageResizer;
import com.mzeat.image.ImageWorker.ImageWorkerAdapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public abstract class GenerateListViewWithImagesBaseAdapter<T> extends
		BaseAdapter {
	protected List<T> mDatas;
	protected Context mContext;
	private ImageResizer mImageWorker;
	private ImageCache mImageCache;

	public GenerateListViewWithImagesBaseAdapter(Context context) {
		mContext = context;
		mDatas = new ArrayList<T>();
		initImageLoader();
	}

	private void initImageLoader() {
		mImageWorker = new ImageFetcher(mContext, getItemImageWidth(),
				getItemImageHeight());
		mImageWorker.setAdapter(imageThumbWorkerUrlsAdapter);
		ImageCacheParams cacheParams = new ImageCacheParams(
				ImageCache.IMAGE_DIR);
		mImageWorker.setLoadingImage(R.drawable.empty_image);
		mImageCache = new ImageCache(mContext, cacheParams);
		mImageWorker.setImageCache(mImageCache);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		if (0 <= position && position < mDatas.size())
			return mDatas.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			convertView = createView(position);
		}
		fillDataToView(convertView, position);
		return convertView;
	}

	protected LayoutInflater getLayoutInflater() {
		return (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	protected void loadImage(int index, ImageView view) {
		mImageWorker.loadImage(index, view);
	}

	public void setDataList(List<T> dataList) {
		if (null != dataList) {
			clear();
			mDatas = dataList;
		}
	}

	public void addDatas(List<T> items) {
		if (null != items)
			mDatas.addAll(items);
	}

	public void addData(T item) {
		if (null != item)
			mDatas.add(item);
	}

	public void removeAt(int position) {
		mDatas.remove(position);
	}

	public void clear() {
		mDatas.clear();
	}

	protected abstract View createView(int position);

	protected abstract void fillDataToView(View convertView, int position);

	protected abstract String getImageUrl(int index);

	protected abstract int getItemImageWidth();

	protected abstract int getItemImageHeight();

	public ImageWorkerAdapter imageThumbWorkerUrlsAdapter = new ImageWorkerAdapter() {
		@Override
		public Object getItem(int index) {
			return getImageUrl(index);
		}

		@Override
		public int getSize() {
			return mDatas.size();
		}
	};
}
