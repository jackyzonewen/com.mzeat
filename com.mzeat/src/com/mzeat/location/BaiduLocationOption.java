package com.mzeat.location;

import android.content.Context;

import com.baidu.location.LocationClientOption;
import com.mzeat.util.CheckNetworkConnection;

public class BaiduLocationOption {

	public static LocationClientOption getOption(Context context) {

		LocationClientOption option = new LocationClientOption();
		//option.setOpenGps(CheckNetworkConnection.checkGPS(context));// 打开gps
		/**
		 * 返回国测局经纬度坐标系 coor=gcj02 返回百度墨卡托坐标系 coor=bd09 返回百度经纬度坐标系 coor=bd09ll
		 */

		option.setCoorType("bd09ll"); // 设置坐标类型,如果不指定，默认返回百度坐标系
		option.setServiceName("com.mzeat");

		// 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
		option.setPoiExtraInfo(false);

		// 设置是否要返回地址信息，默认为无地址信息。
		// String 值为 all时，表示返回地址信息。其他值都表示不返回地址信息。

		option.setAddrType("all");

		/**
		 * 说明：
		 * 
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
		 * )后，每隔设定的时间，定位SDK就会进行一次定位
		 * 。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，返回上一次定位的结果；
		 * 如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 * 当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。每调用一次requestLocation(
		 * )，定位SDK会发起一次定位。请求定位与监听结果一一对应。
		 * 设定了定时定位后，可以热切换成一次定位，需要重新设置时间间隔小于1000（ms）
		 * 即可。locationClient对象stop后，将不再进行定位
		 * 。如果设定了定时定位模式后，多次调用requestLocation（），则是每隔一段时间进行一次定位
		 * ，同时额外的定位请求也会进行定位，但频率不会超过1秒一次。
		 */
		option.setScanSpan(3000);

		/**
		 * 设置定位方式的优先级。目前定位SDK的定位方式有两类：一是使用GPS进行定位。优点是定位准确，精度在几十米，缺点是第一次定位速度较慢，
		 * 甚至需要2、3分钟。二是使用网络定位。优点是定位速度快，服务端只需8ms，考虑到网速的话，一般客户端3秒左右即可定位，缺点是没有gps准确
		 * ，精度在几十到几百米。为了方便用户，我们提供了有两个整型的项：LocationClientOption.GpsFirst 以及
		 * LocationClientOption.NetWorkFirst：
		 * 
		 * GpsFirst：当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。
		 * 如果gps不可用，再发起网络请求，进行定位。
		 * NetWorkFirst：即时有gps，而且可用，也仍旧会发起网络请求。这个选项适合对精确坐标不是特别敏感，但是希望得到位置描述的用户。
		 */
		if (CheckNetworkConnection.checkGPS(context)) {
			option.setPriority(LocationClientOption.GpsFirst);// 不设置，默认是gps优先

		} else {
			option.setPriority(LocationClientOption.NetWorkFirst);// 设置网络优先

		}

		option.setPoiNumber(3);// 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。
		option.disableCache(true);// true表示禁用缓存定位，false表示启用缓存定位。
		return option;

	}
}
