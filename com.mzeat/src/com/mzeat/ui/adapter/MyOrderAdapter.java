package com.mzeat.ui.adapter;

import com.mzeat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mzeat.model.MyOrderItem;

public class MyOrderAdapter extends
		GenerateListViewWithImagesBaseAdapter<MyOrderItem> {

	public MyOrderAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_myorder, null);
		ViewHolder holder = new ViewHolder();
		holder.ordername = (TextView) convertView.findViewById(R.id.ordertitle);
		holder.ordernum = (TextView) convertView.findViewById(R.id.tv_ordernum);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		MyOrderItem item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		

		holder.ordername.setText(item.getOrderGoods().get(0).getName());
		holder.ordernum.setText(item.getSn());

	}

	

	

	private class ViewHolder {
		TextView ordername;
		TextView ordernum;

	}





	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected int getItemImageWidth() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	protected int getItemImageHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
