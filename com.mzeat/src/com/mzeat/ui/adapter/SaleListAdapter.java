package com.mzeat.ui.adapter;

import com.mzeat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mzeat.model.Invite;
import com.mzeat.model.MyOrderItem;
import com.mzeat.model.Sale;

public class SaleListAdapter extends
		GenerateListViewWithImagesBaseAdapter<Sale> {

	public SaleListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_sale, null);
		ViewHolder holder = new ViewHolder();
		holder.title_invite = (TextView) convertView
				.findViewById(R.id.title_invite);

		holder.tv_create_time = (TextView) convertView
				.findViewById(R.id.tv_create_time);

		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		Sale item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.title_invite.setText(item.getTitle());

		holder.tv_create_time.setText(item.getCreate_time());

	}

	private class ViewHolder {
		TextView title_invite;
		TextView tv_create_time;

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
