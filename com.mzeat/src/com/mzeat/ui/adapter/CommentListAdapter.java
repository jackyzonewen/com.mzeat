package com.mzeat.ui.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mzeat.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.mzeat.image.BitmapManager;

import com.mzeat.model.Comment;
import com.mzeat.model.ShareItem;
import com.mzeat.util.SmileyParser;
import com.mzeat.util.StringUtils;

public class CommentListAdapter extends
		GenerateListViewWithImagesBaseAdapter<Comment> {
	private Context context;
	private BitmapManager bmpManager;
	SmileyParser parser ;
	public CommentListAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.empty_image));
		SmileyParser.init(context);	
		 parser = SmileyParser.getInstance();
	}

	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_comment, null);
		ViewHolder holder = new ViewHolder();

		holder.username = (TextView) convertView.findViewById(R.id.username);
		holder.creat_time = (TextView) convertView.findViewById(R.id.time);
		holder.content = (TextView) convertView.findViewById(R.id.content);
		holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);


		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		Comment item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();

		holder.username.setText(item.getUser_name());
		holder.creat_time.setText(item.getTime());
		if (!StringUtils.isEmpty(item.getUser_avatar())) {
			bmpManager.loadBitmap(item.getUser_avatar(), holder.img_user,
					BitmapFactory.decodeResource(context.getResources(),
							R.drawable.empty_image));
		}
		String content = item.getContent();
		Pattern pattern = Pattern.compile( "@([\\w\\u4e00-\\u9fa5]+):", Pattern.CASE_INSENSITIVE); 
		Matcher matcher = pattern.matcher(content); 
		String name = "";
		if (matcher.find()) { 
		name = matcher.group(1);
		System.out.println(name);
		} 
		
		String[] contentArray = content.split(name);
		
		StringBuffer sb = new StringBuffer();
		sb.append(contentArray[0]);
		sb.append(name);
		sb.append(contentArray[1]);
		
		SpannableStringBuilder spannable = new SpannableStringBuilder(
				sb.toString());
		int begin = contentArray[0].length();
		int end = contentArray[0].length()+name.length();
		spannable.setSpan(new ForegroundColorSpan(Color.RED), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.content.setText(parser.addSmileySpans(spannable));

	}

	private class ViewHolder {
		TextView username;
		TextView content;
		TextView creat_time;
		ImageView img_user;

	}

	@Override
	protected String getImageUrl(int index) {
		// TODO Auto-generated method stub

		return null;
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
