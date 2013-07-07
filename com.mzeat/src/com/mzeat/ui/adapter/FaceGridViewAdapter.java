package com.mzeat.ui.adapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



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
import com.mzeat.util.SmileyParser.Smileys;
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

public class FaceGridViewAdapter extends BaseAdapter {
	private Context xContext;
	int[] faces ;
	public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;

	protected ArrayList<Map<String,Integer>> mDatas;
	public FaceGridViewAdapter(Context c) {
		xContext = c;
		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	faces = Smileys.sIconIds;
	}

	private LayoutInflater mInflater;
	//private ImageResizer mImageWorker;
	//private ImageCache mImageCache;

	@Override
	public int getCount() {
		return faces.length;
	}

	@Override
	public Object getItem(int position) {
		return faces[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.team_layout_single_expression_cell, null);
			holder = new GridHolder();
			holder.cImage = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);

		} else {
			holder = (GridHolder) convertView.getTag();

		}

		holder.cImage.setBackgroundResource(faces[position]);

		return convertView;
	}

	private class GridHolder {
		ImageView cImage;

	}

	

}
