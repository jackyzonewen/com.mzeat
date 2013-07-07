package com.mzeat.ui.adapter;

import com.mzeat.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzeat.image.BitmapManager;
import com.mzeat.model.Shopping;
import com.mzeat.util.StringUtils;

public class ShoppingNearAdapter extends
		GenerateListViewWithImagesBaseAdapter<Shopping> {
	private BitmapManager 				bmpManager;
	private Context context;
	public ShoppingNearAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
	}

	int[] starts = { R.drawable.star0, R.drawable.star10, R.drawable.star15,
			R.drawable.star20, R.drawable.star25, R.drawable.star30,
			R.drawable.star35, R.drawable.star40, R.drawable.star45,
			R.drawable.star50 };

	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_shoppingnear, null);
		ViewHolder holder = new ViewHolder();
		holder.img_shopping = (ImageView) convertView
				.findViewById(R.id.img_shopping);
		holder.shopping_title = (TextView) convertView
				.findViewById(R.id.shopping_title);
		holder.address = (TextView) convertView.findViewById(R.id.address);
		holder.num = (TextView) convertView.findViewById(R.id.num);
		holder.distance = (TextView) convertView.findViewById(R.id.distance);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		Shopping mShopping = getItem(position);
		if (null == mShopping)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (!StringUtils.isEmpty(mShopping.getLogo())){
		//	loadImage(position, holder.img_shopping);
			bmpManager.loadBitmap(mShopping.getLogo(),holder.img_shopping, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));

		}
		holder.shopping_title.setText(mShopping.getName());
		holder.address.setText(mShopping.getApi_address());
		holder.num.setText(mShopping.getMzeatvip());
		holder.distance.setText(StringUtils.formatDistance(mShopping.getDistance()));
	}

	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub
		Shopping mShopping = getItem(index);
		String imageUrl = mShopping.getLogo();
		return mShopping == null ? null : imageUrl;
	}

	@Override
	protected int getItemImageWidth() {
		// TODO Auto-generated method stub
		return LayoutParams.WRAP_CONTENT;
	}

	@Override
	protected int getItemImageHeight() {
		// TODO Auto-generated method stub
		return LayoutParams.WRAP_CONTENT;
	}

	private class ViewHolder {
		ImageView img_shopping;
		TextView shopping_title;
		TextView address;
		TextView distance;
		TextView num;

	}

}
