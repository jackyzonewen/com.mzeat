package com.mzeat.image;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mzeat.ApiClient;
import com.mzeat.AppException;
import com.mzeat.util.FileUtils;
import com.mzeat.util.ImageUtils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 异步线程加载图片工具类
 * 使用说明：
 * BitmapManager bmpManager;
 * bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.loading));
 * bmpManager.loadBitmap(imageURL, imageView);
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-6-25
 */
public class BmpManager {  
	  
    private static HashMap<String, SoftReference<Bitmap>> cache;  
    private static ExecutorService pool;  
    private static Map<ImageView, String> imageViews;  
    private Bitmap defaultBmp;  
    
    
	DisplayMetrics dm;
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	float minScaleR;// 最小缩放比例
	static final float MAX_SCALE = 4f;// 最大缩放比例
    static {  
        cache = new HashMap<String, SoftReference<Bitmap>>();  
        pool = Executors.newFixedThreadPool(5);  //固定线程池
        imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }  
    public BmpManager(DisplayMetrics dm,Bitmap def){
    	this.defaultBmp = def;
    	this.dm = dm;
    	
    }
    static final int ZOOM = 2;// 缩放
	int mode = ZOOM;
    /**
     * 限制最大最小缩放比例，自动居中
     */
    private void CheckView(ImageView imgView,Bitmap bitmap) {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR) {
                matrix.setScale(minScaleR, minScaleR);
            }
            if (p[0] > MAX_SCALE) {
                matrix.set(savedMatrix);
            }
        }
        center(imgView,bitmap);
    }

    /**
     * 最小缩放比例，最大为100%
     */
    private void minZoom(Bitmap bitmap) {

        if(bitmap.getWidth() >= dm.widthPixels)
        	minScaleR = ((float) dm.widthPixels) / bitmap.getWidth();
    	else
    		minScaleR = 1.0f;
        
        if (minScaleR < 1.0) {
            matrix.postScale(minScaleR, minScaleR);
        }
    }
    
    private void center(ImageView imgView,Bitmap bitmap) {
        center(true, true,imgView, bitmap);
    }

    /**
     * 横向、纵向居中
     */
    protected void center(boolean horizontal, boolean vertical,ImageView imgView,Bitmap bitmap) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = imgView.getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
    }
	
	
    public BmpManager(Bitmap def) {
    	this.defaultBmp = def;
    }
    
    /**
     * 设置默认图片
     * @param bmp
     */
    public void setDefaultBmp(Bitmap bmp) {  
    	defaultBmp = bmp;  
    }   
  
    /**
     * 加载图片
     * @param url
     * @param imageView
     */
    public void loadBitmap(String url, ImageView imageView) {  
    	loadBitmap(url, imageView, this.defaultBmp, 0, 0);
    }
	
    /**
     * 加载图片-可设置加载失败后显示的默认图片
     * @param url
     * @param imageView
     * @param defaultBmp
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp) {  
    	loadBitmap(url, imageView, defaultBmp, 0, 0);
    }
    
    /**
     * 加载图片-可指定显示图片的高宽
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void loadBitmap(String url, ImageView imageView, Bitmap defaultBmp, int width, int height) {  
        imageViews.put(imageView, url);  
        bitmap = getBitmapFromCache(url);  
   
        if (bitmap != null) {  
			//显示缓存图片
            imageView.setImageBitmap(bitmap);
            minZoom(bitmap);// 计算最小缩放比
			CheckView(imageView,bitmap);// 设置图像居中
			imageView.setImageMatrix(matrix);
        } else {  
        	//加载SD卡中的图片缓存
        	String filename = FileUtils.getFileName(url);
        	String filepath = imageView.getContext().getFilesDir() + File.separator + filename;
    		File file = new File(filepath);
    		if(file.exists()){
				//显示SD卡中的图片缓存
    			bitmap = ImageUtils.getBitmap(imageView.getContext(), filename);
    			 imageView.setImageBitmap(bitmap);
    	            minZoom(bitmap);// 计算最小缩放比
    				CheckView(imageView,bitmap);// 设置图像居中
    				imageView.setImageMatrix(matrix);
        	}else{
				//线程加载网络图片
        		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(, height);
        		
        		imageView.setLayoutParams(params);
        		imageView.setImageBitmap(defaultBmp);
        		
        		queueJob(url, imageView, width, height);
        	}
        }  
    }  
  
    /**
     * 从缓存中获取图片
     * @param url
     */
    public Bitmap getBitmapFromCache(String url) {  
    	 bitmap = null;
        if (cache.containsKey(url)) {  
            bitmap = cache.get(url).get();  
        }  
        return bitmap;  
    }  
    
    /**
     * 从网络中加载图片
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void queueJob(final String url, final ImageView imageView, final int width, final int height) {  
        /* Create handler in UI thread. */  
        final Handler handler = new Handler() {  
            public void handleMessage(Message msg) {  
                String tag = imageViews.get(imageView);  
                if (tag != null && tag.equals(url)) {  
                    if (msg.obj != null) {  
                    	bitmap = (Bitmap) msg.obj;
                    	 imageView.setImageBitmap(bitmap);
                         minZoom(bitmap);// 计算最小缩放比
             			CheckView(imageView,bitmap);// 设置图像居中
             			imageView.setImageMatrix(matrix);
                        try {
                        	//向SD卡中写入图片缓存
							ImageUtils.saveImage(imageView.getContext(), FileUtils.getFileName(url), (Bitmap) msg.obj);
						} catch (IOException e) {
							e.printStackTrace();
						}
                    } 
                }  
            }  
        };  
  
        pool.execute(new Runnable() {   
            public void run() {  
                Message message = Message.obtain();  
                message.obj = downloadBitmap(url, width, height);  
                handler.sendMessage(message);  
            }  
        });  
    } 
    Bitmap bitmap;
    /**
     * 下载图片-可指定显示图片的高宽
     * @param url
     * @param width
     * @param height
     */
    private Bitmap downloadBitmap(String url, int width, int height) {   
       bitmap = null;
        try {
			//http加载图片
			bitmap = ApiClient.getNetBitmap(url);
			if(width > 0 && height > 0) {
				//指定显示图片的高宽
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			} 
			//放入缓存
			cache.put(url, new SoftReference<Bitmap>(bitmap));
		} catch (AppException e) {
			e.printStackTrace();
		}
        return bitmap;  
    }  
}