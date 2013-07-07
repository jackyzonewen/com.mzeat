package com.mzeat.ui.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import com.mzeat.ApiClient;
import com.mzeat.AppException;
import com.mzeat.R;
import com.mzeat.UIHelper;
import com.mzeat.image.BitmapManager;
import com.mzeat.image.ImageCache;
import com.mzeat.image.ImageCache.ImageCacheParams;
import com.mzeat.image.ImageFetcher;
import com.mzeat.image.ImageResizer;
import com.mzeat.model.ShareItemImgs;
import com.mzeat.ui.ImageDialog;
import com.mzeat.ui.SharePhoto;
import com.mzeat.util.FileUtils;
import com.mzeat.util.ImageUtils;
import com.mzeat.util.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
	private Context xContext;

	protected ArrayList<String> mDatas;
	private BitmapManager bmpManager;
	private ArrayList<ShareItemImgs> imgs;
	//private Thread thread;
	//private Handler handler;
	public GridViewAdapter(Context c,  ArrayList<ShareItemImgs> imgs) {
		xContext = c;
		mDatas = new ArrayList<String>();
		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(xContext.getResources(), R.drawable.empty_image));
	this.imgs = imgs;
	
	}

	private LayoutInflater mInflater;
	//private ImageResizer mImageWorker;
	//private ImageCache mImageCache;

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.img_share, null);
			holder = new GridHolder();
			holder.cImage = (ImageView) convertView.findViewById(R.id.share);
			convertView.setTag(holder);

		} else {
			holder = (GridHolder) convertView.getTag();

		}

		if (mDatas.get(position).equals("")) {
			holder.cImage.setVisibility(View.GONE);
		} else {
			
			bmpManager.loadBitmap(mDatas.get(position),holder.cImage, BitmapFactory.decodeResource(xContext.getResources(), R.drawable.empty_image));

			final int pos = position;
			holder.cImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//UIHelper.showImageZoomDialog(v.getContext(),  mDatas.get(pos));
					ArrayList<String> url_imgs = new ArrayList<String>();
					for (int i = 0; i < imgs.size(); i++) {
						String img_url = imgs.get(i).getImg();
						url_imgs.add(img_url);
					}
					Intent intent = new Intent(xContext, SharePhoto.class);
					intent.putStringArrayListExtra("img_url", url_imgs);
					intent.putExtra("position", pos);
					xContext.startActivity(intent);
				}
			});
		}

		return convertView;
	}

	private class GridHolder {
		ImageView cImage;

	}

	public void setDataList(ArrayList<String> dataList) {
		if (null != dataList) {
			clear();
			mDatas = dataList;
		}
	}

	public void addDatas(ArrayList<String> items) {
		if (null != items)
			mDatas.addAll(items);
	}

	public void addData(String item) {
		if (null != item)
			mDatas.add(item);
	}

	public void removeAt(int position) {
		mDatas.remove(position);
	}

	public void clear() {
		mDatas.clear();
	}

}
