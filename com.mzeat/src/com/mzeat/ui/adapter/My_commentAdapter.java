package com.mzeat.ui.adapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mzeat.R;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mzeat.model.MyOrderItem;
import com.mzeat.model.My_share;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.util.SmileyParser;

public class My_commentAdapter extends
		GenerateListViewWithImagesBaseAdapter<U_commentlist_item> {
			SmileyParser parser ;
	public My_commentAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SmileyParser.init(context);	
		 parser = SmileyParser.getInstance();
	}

	
	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_my_comment, null);
		ViewHolder holder = new ViewHolder();
		holder.content = (TextView) convertView.findViewById(R.id.content);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		U_commentlist_item item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		
		String content = item.getScontent();
		String str = (String) content.replace("/", "");
		Pattern pattern = Pattern.compile( "@([\\w\\u4e00-\\u9fa5]+):", Pattern.CASE_INSENSITIVE); 
		Matcher matcher = pattern.matcher(str); 
		String name = "";
		if (matcher.find()) { 
		name = matcher.group(1);
		System.out.println(name);
		} 
		
		String[] contentArray = str.split(name);
		
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
		//holder.content.setText(item.getScontent());

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
