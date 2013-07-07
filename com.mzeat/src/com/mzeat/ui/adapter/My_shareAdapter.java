package com.mzeat.ui.adapter;

import com.mzeat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mzeat.model.MyOrderItem;
import com.mzeat.model.My_share;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.util.SmileyParser;

public class My_shareAdapter extends
		GenerateListViewWithImagesBaseAdapter<My_share> {
			SmileyParser parser ;
	public My_shareAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SmileyParser.init(context);	
		 parser = SmileyParser.getInstance();
	}

	
	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_my_share, null);
		ViewHolder holder = new ViewHolder();
		holder.content = (TextView) convertView.findViewById(R.id.content);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		My_share item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		holder.content.setText(parser.addSmileySpans(item.getContent()));

	}

	private class ViewHolder {
	
		TextView content;

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
