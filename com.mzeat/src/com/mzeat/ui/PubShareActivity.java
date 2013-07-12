package com.mzeat.ui;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



import com.mzeat.AppConfig;
import com.mzeat.MzeatApplication;
import com.mzeat.R;
import com.mzeat.UIHelper;
import com.mzeat.api.MzeatService;
import com.mzeat.model.CommentReturn;
import com.mzeat.model.PubShare;
import com.mzeat.task.GenericTask;
import com.mzeat.task.TaskAdapter;
import com.mzeat.task.TaskParams;
import com.mzeat.task.TaskResult;
import com.mzeat.ui.adapter.FaceGridViewAdapter;
import com.mzeat.util.FileUtils;
import com.mzeat.util.ImageUtils;
import com.mzeat.util.MediaUtils;
import com.mzeat.util.ShowToast;
import com.mzeat.util.SmileyParser;
import com.mzeat.util.SmileyParser.Smileys;
import com.mzeat.util.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PubShareActivity extends BaseActivity {

	ImageButton btn_photo;
	ImageButton btn_title;
	ImageButton btn_face;
	ImageButton btn_pubshare;
	
	LinearLayout ll_img;
	EditText et_title;
	EditText et_content;
	private boolean isTitle = false;
	
	
	private String content;
	private String title ="";
	
	private LoadDataTask mLoadDataTask;
	
	private InputMethodManager imm;
	private Dialog builder; 
	  private String[] mSmileyTexts;
	  public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pubshare);
		//软键盘管理类
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		 mSmileyTexts = getResources().getStringArray(DEFAULT_SMILEY_TEXTS);
		initView();
		setViewData();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_photo = (ImageButton) findViewById(R.id.btn_photo);
		btn_photo.setOnClickListener(pickClickListener);
		
		ll_img = (LinearLayout) findViewById(R.id.ll_img);
		
		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_title = (ImageButton) findViewById(R.id.btn_title);
		btn_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isTitle) {
					et_title.setVisibility(View.VISIBLE);
					et_title.setFocusable(true);
					et_title.setFocusableInTouchMode(true);
					isTitle=true;
				}else {
					et_title.setVisibility(View.GONE);
					isTitle = false;
					et_title.setText("");
				}
			}
		});
		
		btn_pubshare  = (ImageButton) findViewById(R.id.btn_pubshare);
		btn_pubshare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				// TODO Auto-generated method stub
				if (checkInput()) {
					loaddata();
				}
			}
		});
		
		btn_face  = (ImageButton) findViewById(R.id.btn_face);
		btn_face.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				createExpressionDialog();
			}
		});
	}
	
	/**
	 * 创建一个表情选择对话框
	 */
	private void createExpressionDialog() {
		builder = new Dialog(PubShareActivity.this);
		GridView gridView = createGridView();
		builder.setContentView(gridView);
		builder.setTitle("默认表情");
		builder.show();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (et_content.isFocused()) {
					et_content.append(mSmileyTexts[arg2]);
				}else if (et_title.isFocused()) {
					et_title.append(mSmileyTexts[arg2]);
				}
				
				builder.dismiss();
			}
		});
	}
	
	/**
	 * 生成一个表情对话框中的gridview
	 * @return
	 */
	SmileyParser smileyParser;
	ArrayList<Integer> listItems = new ArrayList<Integer>();
	private GridView createGridView() {
		final GridView view = new GridView(this);
	
		
		
		FaceGridViewAdapter mAdapter = new FaceGridViewAdapter(this);
		//SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.team_layout_single_expression_cell, new String[]{"image"}, new int[]{R.id.image});
		view.setAdapter(mAdapter);
		view.setNumColumns(6);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(3);
		view.setVerticalSpacing(3);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		view.setPadding(5, 5, 5, 5);
		return view;
	}

	
	private void setViewData() {
		// TODO Auto-generated method stub
		
	}
	private ArrayList<File> imgfiles;
	private boolean checkInput(){
		
		content = et_content.getText().toString().trim();
		if (content.equals("")) {
				ShowToast.showMessage(this, "请输入分享内容！");

			return false;

		} 
		
		Iterator<Entry<Integer, File>> iter = map.entrySet().iterator(); 
		imgfiles = new ArrayList<File>();
		while (iter.hasNext()) { 
			
		    Entry<Integer, File> entry = iter.next(); 
		    Object key = entry.getKey(); 
		    File val = entry.getValue(); 
		    imgfiles.add( val);
		} 
		Log.e("files",String.valueOf(files.size()));
		if (imgfiles.size() == 0) {
			ShowToast.showMessage(this, "请选择分享照片！");
		
			return false;

		}
		Log.e("files.size()", String.valueOf(files.size()));
		return true;
		
	}
	ProgressDialog pg;

	private TaskAdapter mTaskListener = new TaskAdapter() {

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		public void onPreExecute(GenericTask task) {
			// TODO 任务开始执行，可提供进度条展现
			pg = ProgressDialog.show(PubShareActivity.this,
					getString(R.string.dialog_tips),
					getString(R.string.loading), true, true, cancelListener);

		}

		public void onPostExecute(GenericTask task, TaskResult result) {
			pg.dismiss();
			pg = null;
			// TODO 判断TaskReult的返回值是否ok
			if (result == TaskResult.OK) {
				ShowToast.showMessage(PubShareActivity.this,pubShare.getInfo());
				setResult(1);
				finish();
				mLoadDataTask = null;

			} else if (result == TaskResult.FAILED) {
				mLoadDataTask = null;
				ShowToast.showMessage(PubShareActivity.this,pubShare.getInfo());

			} else if (result == TaskResult.IO_ERROR) {
				mLoadDataTask = null;
				ShowToast.showError(PubShareActivity.this);
			}

		}

		public void onProgressUpdate(GenericTask task, Object param) {
			// TODO 如果是下载，可在此显示下载进度

		}

		public void onCancelled(GenericTask task) {
			// TODO 后台任务被取消的事件回调，适当情况下可以提示用户，如“下载已取消”

		}
	};
	DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			clearTask();
		}

	};

	private void clearTask() {
		// TODO Auto-generated method stub
		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING) {
			mLoadDataTask.cancel(true);
			mLoadDataTask = null;
		}

	}

	private PubShare pubShare = new PubShare();

	private class LoadDataTask extends GenericTask {

		@Override
		protected TaskResult _doInBackground(TaskParams... params) {
			// TODO Auto-generated method stub
			title = et_title.getText().toString().trim();
			pubShare = MzeatApplication.getInstance().getService()
					.getPubShare(content, title, imgfiles);
			int code = Integer.valueOf(pubShare.getOpen());
			if (code == MzeatService.RESULT_OK) {

				return TaskResult.OK;
			} else if (code == MzeatService.RESULT_FAILE) {
				return TaskResult.FAILED;
			} else {
				return TaskResult.IO_ERROR;
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

	}

	private void loaddata() {

		/**
		 * 重要！！需要判断当前任务是否正在运行，否则重复执行会出错，典型的场景就是用户点击登录按钮多次
		 */

		if (null != mLoadDataTask
				&& mLoadDataTask.getStatus() == GenericTask.Status.RUNNING)
			return;

		mLoadDataTask = new LoadDataTask();
		mLoadDataTask.setListener(mTaskListener);
		try {
			mLoadDataTask.execute();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mLoadDataTask) {
				mLoadDataTask.cancel(true);
				mLoadDataTask.setListener(null);
				mLoadDataTask = null;
				return true;
			}

		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
		}

		return true;
	}
	private View.OnClickListener pickClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			//隐藏表情
			//hideFace();		
			
			CharSequence[] items = {
					PubShareActivity.this.getString(R.string.img_from_album),
					PubShareActivity.this.getString(R.string.img_from_camera)
			};
			imageChooseItem(items);
		}
	};
	
	
	/**
	 * 操作选择
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items )
	{
		AlertDialog imageDialog = new AlertDialog.Builder(this).setTitle(R.string.ui_insert_image).setIcon(android.R.drawable.btn_star).setItems(items,
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int item)
				{
					//手机选图
					if( item == 0 )
					{
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
						intent.addCategory(Intent.CATEGORY_OPENABLE); 
						intent.setType("image/*"); 
						startActivityForResult(Intent.createChooser(intent, "选择图片"),ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD); 
					}
					//拍照
					else if( item == 1 )
					{	
						String savePath = "";
						//判断是否挂载了SD卡
						String storageState = Environment.getExternalStorageState();		
						if(storageState.equals(Environment.MEDIA_MOUNTED)){
							savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MZEAT/Camera/";//存放照片的文件夹
							File savedir = new File(savePath);
							if (!savedir.exists()) {
								savedir.mkdirs();
							}
						}
						
						//没有挂载SD卡，无法保存文件
						if(StringUtils.isEmpty(savePath)){
							UIHelper.ToastMessage(PubShareActivity.this, "无法保存照片，请检查SD卡是否挂载");
							return;
						}

						String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
						String fileName = "mzeat_" + timeStamp + ".jpg";//照片命名
						File out = new File(savePath, fileName);
						Uri uri = Uri.fromFile(out);
						
						theLarge = savePath + fileName;//该照片的绝对路径
						
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
					}   
				}}).create();
		
		 imageDialog.show();
	}
	private String theLarge;
	private String theThumbnail;
	private File imgFile;
	private ArrayList<File> files = new ArrayList<File>();
	private String tempTweetImageKey = AppConfig.TEMP_TWEET_IMAGE;
	Map<Integer, File> map = new HashMap<Integer, File>();
	private int tag = 0;
	@Override 
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{ 
    	if(resultCode != RESULT_OK) return;
		
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what == 1 && msg.obj != null){
					//Log.e("isok"," isok");
					Bitmap bitmap = (Bitmap)msg.obj;
					final ImageView img =	(ImageView) LayoutInflater
					.from(PubShareActivity.this).inflate(
							R.layout.img_pubshare, null);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200,200);
					//ImageView img = new ImageView(PubShareActivity.this);
					//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(60,45);
					//img.setLayoutParams(params);
					img.setLayoutParams(params);
					img.setImageBitmap(bitmap);
					img.setTag(tag);
					//bitmap.recycle();
					//bitmap= null;
					
					ll_img.addView(img);
					//files.add(imgFile);
					map.put(tag, imgFile);
					tag = tag + 1;
					img.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int which = (Integer) v.getTag();
							ll_img.removeView(img);
							map.remove(v.getTag());
							Log.e("which",String.valueOf(map.size()));
							
						}
					});

				}
			}
		};
		
		new Thread(){
			public void run() 
			{
				Bitmap bitmap = null;
				
		        if(requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) 
		        {         	
		        	if(data == null)  return;
		        	
		        	Uri thisUri = data.getData();
		        	String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(thisUri);
		        	
		        	//如果是标准Uri
		        	if(StringUtils.isEmpty(thePath))
		        	{
		        		theLarge = ImageUtils.getAbsoluteImagePath(PubShareActivity.this,thisUri);
		        	}
		        	else
		        	{
		        		theLarge = thePath;
		        	}
		        	
		        	String attFormat = FileUtils.getFileFormat(theLarge);
		        	if(!"photo".equals(MediaUtils.getContentType(attFormat)))
		        	{
		        		Toast.makeText(PubShareActivity.this, getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
		        		return;
		        	}
		        	
		        	//获取图片缩略图 只有Android2.1以上版本支持
		    		if(MzeatApplication.isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)){
		    			String imgName = FileUtils.getFileName(theLarge);
		    			//bitmap = ThumbnailUtils.createVideoThumbnail(imgName, MediaStore.Images.Thumbnails.MICRO_KIND);
		    					bitmap =ImageUtils.loadImgThumbnail(PubShareActivity.this, imgName, MediaStore.Images.Thumbnails.MICRO_KIND);
		    			//bitmap.recycle();
		    		}
		        	
		        	if(bitmap == null && !StringUtils.isEmpty(theLarge))
		        	{
		        		bitmap = ImageUtils.loadImgThumbnail(theLarge, 500, 500);
		        		//bitmap.recycle();
		        	}
		        }
		        //拍摄图片
		        else if(requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA)
		        {	
		        	if(bitmap == null && !StringUtils.isEmpty(theLarge))
		        	{
		        		
		        		bitmap = ImageUtils.loadImgThumbnail(theLarge, 500, 500);
		        		//String imgName = FileUtils.getFileName(theLarge);
		    			//bitmap = ImageUtils.loadImgThumbnail(PubShareActivity.this, imgName, MediaStore.Images.Thumbnails.MICRO_KIND);

		        	
		        	}
		        }
		        
				if(bitmap!=null)
				{
					//存放照片的文件夹
					String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MZEAT/Camera/";
					File savedir = new File(savePath);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}
					
					String largeFileName = FileUtils.getFileName(theLarge);
					String largeFilePath = savePath + largeFileName;
					//判断是否已存在缩略图
					if(largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) 
					{
						theThumbnail = largeFilePath;
						imgFile = new File(theThumbnail);
					} 
					else 
					{
						//生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if(new File(theThumbnail).exists())
						{
							imgFile = new File(theThumbnail);
						}
						else
						{
							try {
								//压缩上传的图片
								ImageUtils.createImageThumbnail(PubShareActivity.this, theLarge, theThumbnail, 800, 60);
								imgFile = new File(theThumbnail);
							} catch (IOException e) {
								e.printStackTrace();
							}	
						}						
					}					
					//保存动弹临时图片
					MzeatApplication.getInstance().setProperty(tempTweetImageKey, theThumbnail);
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}				
			};
		}.start();
    }
}
