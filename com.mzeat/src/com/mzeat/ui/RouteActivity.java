package com.mzeat.ui;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mzeat.MzeatApplication;
import com.mzeat.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class RouteActivity extends BaseActivity {

	MapView mMapView = null; // 地图View
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用

	private String st_Latitude;
	private String st_Longitude;
	private String en_Latitude;
	private String en_Longitude;
	GeoPoint en_gp;
	GeoPoint st_gp;

	private ImageButton btn_back;

	private boolean search_result = false;
	
	MKDrivingRouteResult res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		res = (MKDrivingRouteResult) intent.getSerializableExtra("res");
		st_Latitude = intent.getStringExtra("st_Latitude");
		st_Longitude = intent.getStringExtra("st_Longitude");
		 st_Latitude = MzeatApplication.getInstance().lat;
		 st_Longitude = MzeatApplication.getInstance().lon;
		en_Latitude = intent.getStringExtra("en_Latitude");
		en_Longitude = intent.getStringExtra("en_Longitude");
		double st_geoLatitude = Double.valueOf(st_Latitude) * 1E6;
		double st_geoLongitude = Double.valueOf(st_Longitude) * 1E6;
		st_gp = new GeoPoint((int) st_geoLatitude, (int) st_geoLongitude);
		double en_geoLatitude = Double.valueOf(en_Latitude) * 1E6;
		double en_geoLongitude = Double.valueOf(en_Longitude) * 1E6;
		en_gp = new GeoPoint((int) en_geoLatitude, (int) en_geoLongitude);
		setContentView(R.layout.activity_route);
		findViewById(R.id.cb_near).setVisibility(View.GONE);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mSearch = new MKSearch();
		
		search_result = mSearch.init(
				MzeatApplication.getInstance().mBMapManager,
				new MKSearchListener() {

					@Override
					public void onGetPoiDetailSearchResult(int type, int error) {
					}

					public void onGetDrivingRouteResult(
							MKDrivingRouteResult res, int error) {
						// 错误号可参考MKEvent中的定义
						if (error != 0 || res == null) {
							// Log.e("error", String.valueOf(error));
							Toast.makeText(RouteActivity.this, "抱歉，未找到结果",
									Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							Log.e("error code ", String.valueOf(error));
							RouteOverlay routeOverlay = new RouteOverlay(
									RouteActivity.this, mMapView);
							// 此处仅展示一个方案作为示例
							routeOverlay.setData(res.getPlan(0).getRoute(0));

							mMapView.getOverlays().clear();
							mMapView.getOverlays().add(routeOverlay);
							mMapView.refresh();

							// mMapView.getController().enableClick(true);
							// mMapView.getController().setCenter(en_gp);
							// mMapView.getController().setZoom(12);
							// mMapView.setBuiltInZoomControls(true);
							// mMapView.setDoubleClickZooming(true);

							// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
							mMapView.getController().zoomToSpan(
									routeOverlay.getLatSpanE6(),
									routeOverlay.getLonSpanE6());
							mMapView.getController().animateTo(res.getEnd().pt);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					

					}

					public void onGetTransitRouteResult(
							MKTransitRouteResult res, int error) {

					}

					public void onGetWalkingRouteResult(
							MKWalkingRouteResult res, int error) {

					}

					public void onGetAddrResult(MKAddrInfo res, int error) {
					}

					public void onGetPoiResult(MKPoiResult res, int arg1,
							int arg2) {
					}

					public void onGetBusDetailResult(MKBusLineResult result,
							int iError) {
					}

					@Override
					public void onGetSuggestionResult(MKSuggestionResult res,
							int arg1) {
					}

				});
				
		Log.e("init ", String.valueOf(search_result));
		initMapView();
		showMapView();
		

		// showMapView();

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Log.e("FINISH", "FINISH");
				//mMapView.destroy();
				finish();

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//mMapView.destroy();
		finish();
	}

	private void showMapView() {
		// TODO Auto-generated method stub
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();

		stNode.pt = st_gp;
		// Log.e("en_gp.getLatitudeE6()",
		// String.valueOf(st_gp.getLongitudeE6()));
		MKPlanNode enNode = new MKPlanNode();

		enNode.pt = en_gp;
		// Log.e("en_gp.getLatitudeE6()",
		// String.valueOf(en_gp.getLongitudeE6()));
		// 实际使用中请对起点终点城市进行正确的设定
		mSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
		int result = mSearch.drivingSearch("梅州", stNode, "梅州", enNode);
		while (result == -1 ) {
			result = mSearch.drivingSearch("梅州", stNode, "梅州", enNode);
			
		}
		Log.e(" result", String.valueOf(result));
	}

	private void initMapView() {
		mMapView.getController().enableClick(true);
		mMapView.getController().setCenter(en_gp);

		mMapView.getController().setZoom(12);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setDoubleClickZooming(true);
		
		/**
		
		RouteOverlay routeOverlay = new RouteOverlay(
				RouteActivity.this, mMapView);
		// 此处仅展示一个方案作为示例
		routeOverlay.setData(res.getPlan(0).getRoute(0));

		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(routeOverlay);
		mMapView.refresh();

		 mMapView.getController().enableClick(true);
		// mMapView.getController().setCenter(en_gp);
		 mMapView.getController().setZoom(12);
		 mMapView.setBuiltInZoomControls(true);
		 mMapView.setDoubleClickZooming(true);

		// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
		mMapView.getController().zoomToSpan(
				routeOverlay.getLatSpanE6(),
				routeOverlay.getLonSpanE6());
		mMapView.getController().animateTo(res.getEnd().pt);**/

	}

	@Override
	protected void onPause() {
		// mMapView.setVisibility(View.INVISIBLE);
		// mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// mMapView.setVisibility(View.VISIBLE);
		// mMapView.onResume();
		super.onResume();
		if (MzeatApplication.getInstance().mBMapManager == null) {
			MzeatApplication.getInstance().mBMapManager = new BMapManager(this);
			MzeatApplication.getInstance().mBMapManager.init(
					MzeatApplication.strKey,
					new MzeatApplication.MyGeneralListener());
			Log.e("mBMapManager", "mBMapManager");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		// mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		// Log.e("mMapView.destroy();", "mMapView.destroy();");
		// if (MzeatApplication.getInstance().mBMapManager != null) {
		// MzeatApplication.getInstance().mBMapManager.destroy();
		// MzeatApplication.getInstance().mBMapManager = null;
		// }
		super.onDestroy();
	}
}
