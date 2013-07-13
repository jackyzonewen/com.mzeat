package com.mzeat.ui;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mzeat.R;
import com.mzeat.alipay.AlixId;
import com.mzeat.alipay.BaseHelper;
import com.mzeat.alipay.MobileSecurePayHelper;
import com.mzeat.alipay.MobileSecurePayer;
import com.mzeat.alipay.ResultChecker;
import com.mzeat.alipay.Rsa;
import com.mzeat.db.UserDb;
import com.mzeat.model.ConfirmOrderItem;
import com.mzeat.model.User;
import com.mzeat.ui.adapter.ConfirmOrderAdapter;
import com.mzeat.ui.widget.MyListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OrderConfirmActivity extends BaseActivity {
	static String TAG = "OrderConfirmActivity";

	
	private TextView tv_title;
	
	private MyListView lv_cart;
	private EditText et_note;
	private RadioGroup rg_paytype;
	private RadioButton rd_zhifubao;
	private TextView tv_all_product_count;
	private TextView tv_should_pay;
	private TextView tv_jifen_change;
	private EditText et_edit_mobile;
	private ImageButton btn_contact;
	private ArrayList<ConfirmOrderItem> orderItems = new ArrayList<ConfirmOrderItem>();
	private String total_count;
	
	
	private ConfirmOrderAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmorder);
		
		Intent intent = getIntent();
		orderItems = (ArrayList<ConfirmOrderItem>) intent
				.getSerializableExtra(ShopCartActivity.SER_KEY);
		total_count = intent.getStringExtra("total_count");
		mAdapter = new ConfirmOrderAdapter(this);
		mAdapter.setDataList(orderItems);
		initView();
		setViewData();
	}
	
	private void initView(){
		
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		lv_cart =  (MyListView) findViewById(R.id.lv_cart);
		
		et_note = (EditText) findViewById(R.id.et_note);
		
		rg_paytype = (RadioGroup) findViewById(R.id.rg_paytype);
		rg_paytype.setOnCheckedChangeListener(checkedChangeListener);
		rd_zhifubao = (RadioButton) findViewById(R.id.rd_zhifubao);
		rd_zhifubao.setChecked(true);
		
		tv_all_product_count = (TextView) findViewById(R.id.tv_all_product_count);
		tv_should_pay = (TextView) findViewById(R.id.tv_should_pay);
		tv_jifen_change = (TextView) findViewById(R.id.tv_jifen_change);
		
		et_edit_mobile = (EditText) findViewById(R.id.et_edit_mobile);
		
		btn_contact = (ImageButton) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject object = new JSONObject();
				JSONArray ja = new JSONArray();

				for (int i = 0; i < orderItems.size(); i++) {
					ja.put(orderItems.get(i).getJSONObject());
				}
				try {
					object.put("item", ja);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				
				try {
					object.put("total_count",total_count);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					object.put("order_note",et_note.getText().toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					object.put("mobile",et_edit_mobile.getText().toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					object.put("pay_type",pay_type);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Log.e("jsonarray", object.toString());
			}
		});
		
		
	
	}
	
	private void setViewData(){
		
		tv_title.setText("提交订单");
		lv_cart.setAdapter(mAdapter);
		
		tv_all_product_count.setText("￥"+total_count);
		tv_should_pay.setText("￥"+total_count);
		
		UserDb userDb = new UserDb(this);
		User user = userDb.getUser();
		if (user.getMobile() != null) {
			et_edit_mobile.setText(user.getMobile());
		}
		
		
	}
	private String pay_type = "20"; 
	
	
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.rd_zhifubao:
				pay_type = "20";
				break;
			case R.id.rd_leftmoney:
				pay_type = "15";
				break;
			default:
				break;
			}
		}
		
	};
	
	private String getSubject(){
		for (int i = 0; i < orderItems.size(); i++) {
			subject = subject + orderItems.get(i).getProduct();
		}
		
		return subject;
	}
	
	private String getBody(){
		for (int i = 0; i < orderItems.size(); i++) {
			body = body + orderItems.get(i).getDescribe();
		}
		
		return body;
		
	}
	
	private String getPrice(){
		
		price = total_count;
		return price;
		
	}
	
	private String PARTNER = "";
	private String SELLER = "";
	private String OutTradeNo = "";
	private String subject= "";
	private String body = "";
	private String price = "";
	private String RSA_PRIVATE = "";
	private String RSA_ALIPAY_PUBLIC;
	
	private ProgressDialog mProgress = null;
	String getOrderInfo(String PARTNER,String SELLER, String OutTradeNo) {
		String strOrderInfo = "partner=" + "\"" + PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + OutTradeNo + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + getSubject()
				+ "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + getBody() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\""
				+ getPrice() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ "http://www.mzeat.com" + "\"";
		
		return strOrderInfo;
	}



	//
	//
	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 * @return
	 */
	String sign(String signType, String content,String RSA_PRIVATE) {
		return Rsa.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * get the char set we use. 获取字符集
	 * 
	 * @return
	 */
	String getCharset() {
		String charset = "charset=" + "\"" + "utf-8" + "\"";
		return charset;
	}

	/**
	 * the onItemClick for the list view of the products. 商品列表商品被点击事件
	 */
	public void payTaobao(){
		//
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// check some info.
		// 检测配置信息
		if (!checkInfo()) {
			BaseHelper
					.showDialog(
							OrderConfirmActivity.this,
							"提示",
							"缺少partner或者seller。",
							R.drawable.infoicon);
			return;
		}

		// start pay for this order.
		// 根据订单信息开始进行支付
		try {
			// prepare the order info.
			// 准备订单信息
			String orderInfo = getOrderInfo(PARTNER,SELLER,OutTradeNo);
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType,orderInfo,RSA_PRIVATE);
			Log.v("sign:", strsign);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			Log.v("orderInfo:", info);
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(this, null, "正在支付", false,
						true);
			} else
				;
		} catch (Exception ex) {
			Toast.makeText(OrderConfirmActivity.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}



	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean checkInfo() {
		String partner = PARTNER;
		String seller = SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}

	//
	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				Log.e(TAG, strRet);	// strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					BaseHelper.log(TAG, strRet);

					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);
						
						//先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign(RSA_ALIPAY_PUBLIC);
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									OrderConfirmActivity.this,
									"提示",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// 验签成功。验签成功后再判断交易状态码
							if(tradeStatus.equals("9000"))//判断交易状态码，只有9000表示交易成功
								BaseHelper.showDialog(OrderConfirmActivity.this, "提示","支付成功。交易状态码："+tradeStatus, R.drawable.infoicon);
							else
							BaseHelper.showDialog(OrderConfirmActivity.this, "提示", "支付失败。交易状态码:"
									+ tradeStatus, R.drawable.infoicon);
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(OrderConfirmActivity.this, "提示", strRet,
								R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	//
	//
	/**
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	public static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	//
	// close the progress bar
	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");

		try {
			mProgress.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
