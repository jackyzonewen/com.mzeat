package com.mzeat.ui.adapter;import java.util.List;import com.mzeat.R;import com.mzeat.image.ImageCache;import com.mzeat.image.ImageCache.ImageCacheParams;import com.mzeat.image.ImageFetcher;import com.mzeat.image.ImageResizer;import com.mzeat.image.ImageWorker.ImageWorkerAdapter;import com.mzeat.ui.fragment.ImagePagerFragment;import android.content.Context;import android.support.v4.app.Fragment;import android.support.v4.app.FragmentManager;import android.support.v4.app.FragmentPagerAdapter;import android.util.Log;/** * <p> * Description: * </p> *  * @version 1.0 * @author hongjinqun * @date 2012-12-23 */public class ImageFragmentAdapter extends FragmentPagerAdapter {	private List<String> imageUrls;	private ImageResizer mImageWorker;	private ImageCache mImageCache;	private Context mContext;	/**	 * ImageFragmentAdapter构造方法	 * 	 * @param context	 * @param fm	 * @param imageUrls	 */	public ImageFragmentAdapter(Context context, FragmentManager fm,			List<String> imageUrls) {		super(fm);		mContext = context;		this.imageUrls = imageUrls;		initImageLoader();	}	/**	 * 设置ImageUrlList	 * 	 * @param imageUrls	 */	public void setImageUrlList(List<String> imageUrls) {		if (this.imageUrls != null) {			this.imageUrls.clear();			this.imageUrls = imageUrls;			notifyDataSetChanged();		}	}	/**	 * 初始化ImageLoader	 */	private void initImageLoader() {		mImageWorker = new ImageFetcher(mContext, (int) mContext.getResources()				.getDimension(R.dimen.poster_width), (int) mContext				.getResources().getDimension(R.dimen.poster_height));		mImageWorker.setAdapter(imageThumbWorkerUrlsAdapter);		mImageWorker.setLoadingImage(R.drawable.empty_image);		ImageCacheParams cacheParams = new ImageCacheParams(				ImageCache.IMAGE_DIR);				mImageCache = new ImageCache(mContext, cacheParams);		mImageWorker.setImageCache(mImageCache);	}	@Override	public Fragment getItem(int position) {		Log.e("ImageFragmentAdapter", "position :" + position);		String imageUrl = imageUrls.get(position);		//= MobileStoreAppliction.getInstance().getService()		//		.formartImageDownloadUrl(imageUrls.get(position));		ImagePagerFragment instance = ImagePagerFragment.newInstance(				mImageWorker, imageUrl);		instance.setImagePoision(position);		return instance;	}	/**	 * 取得总数	 */	@Override	public int getCount() {		return imageUrls.size();	}	public ImageWorkerAdapter imageThumbWorkerUrlsAdapter = new ImageWorkerAdapter() {		@Override		public Object getItem(int index) {			String imageUrl = imageUrls.get(index) ;			//= MobileStoreAppliction.getInstance().getService()				//	.formartImageDownloadUrl(imageUrls.get(index));			return imageUrl;		}		@Override		public int getSize() {			return imageUrls.size();		}	};}