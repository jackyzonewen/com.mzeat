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
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.Shopping;
import com.mzeat.util.CountStartNum;
import com.mzeat.util.StringUtils;

public class PrivilegeAdapter extends
		GenerateListViewWithImagesBaseAdapter<PrivilegeItem> {
	private BitmapManager 				bmpManager;
	private Context context;
	public PrivilegeAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));

	}

	int[] state = { R.drawable.state_notbegin, R.drawable.state_begin,
			R.drawable.state_end };

	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_privilege, null);
		ViewHolder holder = new ViewHolder();
		holder.img_privilege = (ImageView) convertView
				.findViewById(R.id.img_privilege);
		holder.img_state = (ImageView) convertView.findViewById(R.id.state);
		holder.privilege_title = (TextView) convertView
				.findViewById(R.id.privilege_title);
		holder.nowprice = (TextView) convertView.findViewById(R.id.tv_nowprice);
		holder.buyed = (TextView) convertView.findViewById(R.id.tv_buyed);
		holder.lefttime = (TextView) convertView.findViewById(R.id.tv_lefttime);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		PrivilegeItem item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (!StringUtils.isEmpty(item.getImage())){
			//loadImage(position, holder.img_privilege);
			bmpManager.loadBitmap(item.getImage(),holder.img_privilege, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));

			}
		holder.privilege_title.setText(item.getTitle());
		holder.buyed.setText(item.getBuy_count());
		int time_state = Integer.valueOf(item.getTime_status());
		switch (time_state) {
		case 0:
			holder.lefttime.setText(item.getStart_times() + "开始");

			break;
		case 1:
			holder.lefttime.setText(item.getLess_time());
			break;
		case 2:
			holder.lefttime.setText(item.getLess_time());
			break;
		default:
			break;
		}
		//holder.lefttime.setText(item.getLess_time());
		holder.nowprice.setText(item.getCur_price_format());
		int privilege_state = Integer.valueOf(item.getTime_status());
		switch (privilege_state) {
		case 0:
			holder.img_state.setBackgroundResource(state[0]);
			break;
		case 1:
			holder.img_state.setBackgroundResource(state[1]);
			break;
		case 2:
			holder.img_state.setBackgroundResource(state[2]);
			break;
		default:
			break;
		}

	}

	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub
		PrivilegeItem item = getItem(index);
		String imageUrl = item.getImage();
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
		ImageView img_privilege;
		TextView privilege_title;
		TextView nowprice;
		TextView buyed;
		TextView lefttime;
		ImageView img_state;

	}

}
