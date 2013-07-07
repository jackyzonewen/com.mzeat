package com.mzeat.ui.adapter;

import com.mzeat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mzeat.model.Invite;
import com.mzeat.model.MyOrderItem;

public class InviteListAdapter extends
		GenerateListViewWithImagesBaseAdapter<Invite> {

	public InviteListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_invite, null);
		ViewHolder holder = new ViewHolder();
		holder.title_invite = (TextView) convertView
				.findViewById(R.id.title_invite);
		holder.tv_post = (TextView) convertView.findViewById(R.id.tv_post);
		holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
		holder.tv_sex = (TextView) convertView.findViewById(R.id.tv_sex);
		holder.tv_degree = (TextView) convertView.findViewById(R.id.tv_degree);
		holder.tv_treatment = (TextView) convertView
				.findViewById(R.id.tv_treatment);
		holder.tv_claim = (TextView) convertView.findViewById(R.id.tv_claim);
		holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
		holder.tv_contact = (TextView) convertView
				.findViewById(R.id.tv_contact);
		holder.tv_address_invite = (TextView) convertView
				.findViewById(R.id.tv_address_invite);
		holder.tv_create_time = (TextView) convertView
				.findViewById(R.id.tv_create_time);

		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		Invite item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.title_invite.setText(item.getBusiness());
		holder.tv_post.setText(item.getPost());

		holder.tv_number.setText(item.getNumber());

		holder.tv_sex.setText(item.getSex());
		holder.tv_degree.setText(item.getDegree());
		holder.tv_treatment.setText(item.getTreatment());

		holder.tv_claim.setText(item.getClaim());
		holder.tv_phone.setText(item.getPhone());
		holder.tv_contact.setText(item.getContact());
		holder.tv_address_invite.setText(item.getAddress());
		holder.tv_create_time.setText(item.getCreate_time());

	}

	private class ViewHolder {
		TextView title_invite;
		TextView tv_post;
		TextView tv_number;
		TextView tv_sex;
		TextView tv_degree;
		TextView tv_treatment;
		TextView tv_claim;
		TextView tv_phone;
		TextView tv_contact;
		TextView tv_address_invite;
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
