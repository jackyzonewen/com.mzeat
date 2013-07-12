package com.mzeat.ui.adapter;

import com.mzeat.MzeatApplication;
import com.mzeat.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Selection;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mzeat.db.MycartDb;
import com.mzeat.image.BitmapManager;
import com.mzeat.model.ConfirmOrderItem;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.Shopping;
import com.mzeat.ui.widget.CustomDialog;
import com.mzeat.util.CountStartNum;
import com.mzeat.util.StringUtils;

public class ConfirmOrderAdapter extends
		GenerateListViewWithImagesBaseAdapter<ConfirmOrderItem> {
	private BitmapManager 				bmpManager;
	private Context context;

	public ConfirmOrderAdapter(Context context) {
		super(context);
		this.context = context;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
	}

	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_confirmorder, null);
		ViewHolder holder = new ViewHolder();
		holder.img_cart = (ImageView) convertView.findViewById(R.id.img_cart);
		holder.cart_title = (TextView) convertView
				.findViewById(R.id.cart_title);
		holder.tv_singleprice = (TextView) convertView
				.findViewById(R.id.tv_singleprice);
		holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
		holder.tv_count = (TextView) convertView
				.findViewById(R.id.tv_count);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		final ConfirmOrderItem item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (!StringUtils.isEmpty(item.getUrl())){
			//loadImage(position, holder.img_cart);
			bmpManager.loadBitmap(item.getUrl(),holder.img_cart, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
	
		}
		holder.cart_title.setText(item.getProduct());
		holder.tv_num.setText(item.getNum());
		holder.tv_singleprice.setText("￥"+item.getPrice());
		holder.tv_count.setText("￥"+item.getCount());

	}

	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub
		ConfirmOrderItem item = getItem(index);
		String imageUrl = item.getUrl();
		return item == null ? null : imageUrl;
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
		ImageView img_cart;
		TextView cart_title;
		TextView tv_singleprice;
		TextView tv_num;
		TextView tv_count;
	}

}
