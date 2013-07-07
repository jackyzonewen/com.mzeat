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
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.Shopping;
import com.mzeat.ui.widget.CustomDialog;
import com.mzeat.util.CountStartNum;
import com.mzeat.util.StringUtils;

public class MyCartAdapter extends
		GenerateListViewWithImagesBaseAdapter<PrivilegeItem> {
	private BitmapManager 				bmpManager;
	private Context context;

	public MyCartAdapter(Context context) {
		super(context);
		this.context = context;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
	}

	int[] state = { R.drawable.state_notbegin, R.drawable.state_begin,
			R.drawable.state_end };
	public String numCount;
	public String moneyCount;
	@Override
	protected View createView(int position) {
		// TODO Auto-generated method stub

		LayoutInflater inflater = getLayoutInflater();
		View convertView = inflater.inflate(R.layout.listitem_cart, null);
		ViewHolder holder = new ViewHolder();
		holder.img_cart = (ImageView) convertView.findViewById(R.id.img_cart);
		holder.cart_title = (TextView) convertView
				.findViewById(R.id.cart_title);
		holder.nowprice = (TextView) convertView
				.findViewById(R.id.tv_singleprice);
		holder.num = (TextView) convertView.findViewById(R.id.cart_num);
		holder.btn_delete = (ImageButton) convertView
				.findViewById(R.id.img_delete);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	protected void fillDataToView(View convertView, int position) {
		// TODO Auto-generated method stub
		final PrivilegeItem item = getItem(position);
		if (null == item)
			return;
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (!StringUtils.isEmpty(item.getImage())){
			//loadImage(position, holder.img_cart);
			bmpManager.loadBitmap(item.getImage(),holder.img_cart, BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_image));
	
		}
		holder.cart_title.setText(item.getTitle());
		holder.num.setText(item.getNum());
		holder.nowprice.setText(item.getCur_price());
		final int pos = position;
		holder.btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder ad = new AlertDialog.Builder(context);
				ad.setTitle("删除");
				ad.setMessage("确认删除该商品?");
				ad.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								// TODO Auto-generated method stub
								mDatas.remove(pos);
								// setData(data);
								MycartDb mDb = new MycartDb(context);
								mDb.delete(
										item.getGoods_id(),MzeatApplication.getInstance().getpPreferencesConfig().getString("email", ""));
								notifyDataSetChanged();
								sendBrocast();

							}
						});
				ad.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								// 不退出不用执行任何操作
							}
						});
				ad.show();// 显示对话框

			}
		});
		
		holder.num.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MzeatApplication.getInstance().getpPreferencesConfig().setInt("position", pos);
				showNumChange();
			}
		});

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
		ImageView img_cart;
		TextView cart_title;
		TextView nowprice;
		TextView num;
		ImageButton btn_delete;

	}
	public void setNumCount() {
		int num = 0;
		if (mDatas.size() != 0) {
			for (int i = 0; i < mDatas.size(); i++) {
				num += Integer.valueOf(mDatas.get(i).getNum());
			}
			//this.numCount = String.valueOf(num);
		}
		this.numCount = String.valueOf(num);
	}

	public String getNumCount() {

		return numCount;
	}

	public void setMoneyCount() {
		double  num = 0;
		if (mDatas.size() != 0) {
			for (int i = 0; i < mDatas.size(); i++) {
				num += Double.valueOf(mDatas.get(i).getCount());
			}
			//this.moneyCount = String.valueOf(num);
		}
		this.moneyCount = String.valueOf(num);
	}

	public String getMoneyCount() {

		return moneyCount;
	}

	public void sendBrocast() {
		Intent intent = new Intent();
		setNumCount();
		setMoneyCount();
		intent.putExtra("num", getNumCount());
		intent.putExtra("money", getMoneyCount());
		intent.setAction("android.intent.action.setCount");// action与接收器相同
		context.sendBroadcast(intent);
	}
	
	private void showNumChange() {
		final Dialog dialog = new CustomDialog(context);
		dialog.setContentView(R.layout.numchange);
		dialog.setTitle("请商品数量");
		dialog.show();
		final int position = MzeatApplication.getInstance().getpPreferencesConfig().getInt("position", 0);

		final EditText et_num = (EditText) dialog.findViewById(R.id.choisenum);
		et_num.setText(String.valueOf(mDatas.get(position).getNum()));
		CharSequence text = et_num.getText();
		// Debug.asserts(text instanceof Spannable);
		if (text instanceof Spannable) {
			Spannable spanText = (Spannable) text;
			Selection.setSelection(spanText, text.length());
		}
		Button add = (Button) dialog.findViewById(R.id.addnum);

		Button cut = (Button) dialog.findViewById(R.id.cutnum);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int num = Integer.valueOf(et_num.getText().toString());
				et_num.setText(String.valueOf(++num));
				CharSequence text = et_num.getText();
				// Debug.asserts(text instanceof Spannable);
				if (text instanceof Spannable) {
					Spannable spanText = (Spannable) text;
					Selection.setSelection(spanText, text.length());
				}
			}
		});

		cut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int num = Integer.valueOf(et_num.getText().toString());
				if (num <= 1) {
					et_num.setText("1");
					CharSequence text = et_num.getText();
					// Debug.asserts(text instanceof Spannable);
					if (text instanceof Spannable) {
						Spannable spanText = (Spannable) text;
						Selection.setSelection(spanText, text.length());
					}
				} else {
					et_num.setText(String.valueOf(--num));
					CharSequence text = et_num.getText();
					// Debug.asserts(text instanceof Spannable);
					if (text instanceof Spannable) {
						Spannable spanText = (Spannable) text;
						Selection.setSelection(spanText, text.length());
					}
				}
			}
		});

		Button yes = (Button) dialog.findViewById(R.id.btn_yes);
		Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		yes.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (et_num.getText().toString().equals("")) {
					Toast.makeText(context, "商品数量不能为空", Toast.LENGTH_SHORT)
							.show();
					mDatas.get(position).setNum("1");
					//sendBrocast();
				} else {
					if (Integer.valueOf(et_num.getText().toString()) == 0) {
						Toast.makeText(context, "商品数量不能小于1", Toast.LENGTH_SHORT)
								.show();
						mDatas.get(position).setNum("1");
						//sendBrocast();
					} else {
						int num = Integer.valueOf(et_num.getText().toString());
						mDatas.get(position).setNum(String.valueOf(num));
						
						
						
					}
				}
				sendBrocast();
				MycartDb mycartDb = new MycartDb(context);
				mycartDb.updateCart(getItem(position));
				mycartDb.closeDB();
				dialog.dismiss();
				notifyDataSetChanged();
			}
		});
		cancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
}
