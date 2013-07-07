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
import com.mzeat.model.Change;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.Shopping;
import com.mzeat.util.CountStartNum;
import com.mzeat.util.StringUtils;

public class ChangeListAdapter extends
		GenerateListViewWithImagesBaseAdapter<Change> {
	private BitmapManager 				bmpManager;
	private Context context;
	public ChangeListAdapter(Context context) {
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
		View convertView = inflater.inflate(R.layout.listitem_change, null);
		ViewHolder holder = new ViewHolder();
		holder.img_change = (ImageView) convertView
				.findViewById(R.id.img_change);
		holder.tv_jf = (TextView) convertView
				.findViewById(R.id.tv_jf);
		holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
		
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		Change item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (!StringUtils.isEmpty(item.getImage())){
			//loadImage(position, holder.img_privilege);
			bmpManager.loadBitmap(item.getImage(),holder.img_change, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));

			}
		holder.tv_jf.setText(item.getScore());
		holder.tv_title.setText(item.getTitle());


	}

	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub
		return  null ;
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
		ImageView img_change;
		TextView tv_jf;
		TextView tv_title;


	}

}
