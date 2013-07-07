package com.mzeat.ui.adapter;


import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.UIHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzeat.image.BitmapManager;
import com.mzeat.image.ImageCache;
import com.mzeat.image.ImageResizer;
import com.mzeat.model.ShareItem;
import com.mzeat.ui.SharePhoto;
import com.mzeat.util.SmileyParser;
import com.mzeat.util.StringUtils;

public class ShareListAdapter extends
		GenerateListViewWithImagesBaseAdapter<ShareItem> {
	private Context context;
	private BitmapManager 				bmpManager;
	SmileyParser parser ;
	public ShareListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
		SmileyParser.init(this.context);	
		 parser = SmileyParser.getInstance();
	}



	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_share, null);
		ViewHolder holder = new ViewHolder();

		holder.username = (TextView) convertView.findViewById(R.id.username);
		holder.creat_time = (TextView) convertView.findViewById(R.id.time);
		holder.content = (TextView) convertView.findViewById(R.id.content);
		holder.say = (TextView) convertView.findViewById(R.id.saycount);
		holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);

		holder.img_content = (ImageView) convertView
				.findViewById(R.id.img_content);
		 
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		final ShareItem item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.username.setText(item.getUser_name());
		holder.creat_time.setText(StringUtils.friendly_time(item
				.getCreate_time()));
		holder.say.setText("评论"+ "("+item.getReply_count()+")");
		if (!StringUtils.isEmpty(item.getUser_avatar())) {
			//loadImage(position, holder.img_user);
			bmpManager.loadBitmap(item.getUser_avatar(),holder.img_user, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
		}
		if (!item.getTitle().equals("")) {
			StringBuffer sb = new StringBuffer();
			sb.append("#");
			sb.append(item.getTitle());
			sb.append("#");
			sb.append(item.getContent());

			SpannableStringBuilder spannable = new SpannableStringBuilder(
					sb.toString());
			int begin = 0;
			spannable.setSpan(new ForegroundColorSpan(Color.RED), begin, item
					.getTitle().length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			holder.content.setText(parser.addSmileySpans(spannable));
			
		} else {
			holder.content.setText(parser.addSmileySpans(item.getContent()));
		}
		final int pos = position;
		
		
			if (!StringUtils.isEmpty(item.getImg())) {
				
				bmpManager.loadBitmap(item.getSmall_img(), holder.img_content, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
				//final String img_url =  item.getImg();
				
				holder.img_content.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, SharePhoto.class);
						intent.putExtra("img_url", item.getImg());
						intent.putExtra("fromShareList" , 1);
						context.startActivity(intent);
					}
				});

			} else {
				holder.img_content.setVisibility(View.GONE);
			}
		

	}

	private class ViewHolder {
		TextView username;
		TextView content;
		TextView say;
		TextView creat_time;
		ImageView img_user;
		ImageView img_content;
		
	}

	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub
		ShareItem item = getItem(index);
		String imageUrl = item.getUser_avatar();
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

}
