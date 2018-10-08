package com.czapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.utils.DistanceUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Stack;

/***
 * 定位滤波demo，实际定位场景中，可能会存在很多的位置抖动，此示例展示了一种对定位结果进行的平滑优化处理
 * 实际测试下，该平滑策略在市区步行场景下，有明显平滑效果，有效减少了部分抖动，开放算法逻辑，希望能够对开发者提供帮助
 * 注意：该示例场景仅用于对定位结果优化处理的演示，里边相关的策略或算法并不一定适用于您的使用场景，请注意！！！
 * 
 * @author baidu
 * 
 */
public class LocationFilter extends Activity implements OnGetGeoCoderResultListener, OnGetShareUrlResultListener {
	private MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private Button share;
	private LocationService locService;
	private boolean screen = false;
	private boolean flag = false;
	private GeoCoder mGeoCoder = null;
	private ShareUrlSearch mShareUrlSearch = null;
	private boolean isHangUp = false;//用来判断挂断电话
	private boolean isLink = false;
	private Handler handler = new Handler();
	private Stack<String> contactsList = new Stack<String>();
	private String currentAddr;
	private String sendMessage;
	private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationfilter);
		mMapView = (MapView) findViewById(R.id.bmapView);
		share = (Button) findViewById(R.id.clear);
		/*reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d("click","before");
				//
				Log.d("click","after");
			}
		});*/
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
		locService = ((LocationApplication) getApplication()).locationService;
		LocationClientOption mOption = locService.getDefaultLocationClientOption();
		mOption.setCoorType("bd09ll");
		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch.setOnGetShareUrlResultListener(this);
		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(this);
		Intent intent = getIntent();
		flag = intent.getBooleanExtra("flag",false);
		Log.d("LocationFilter","flag:"+flag);
		if(flag==true)
		{
			mOption.setScanSpan(0);
			readFile();
		}
		intent.removeExtra("flag");
		locService.setLocationOption(mOption);
		locService.registerListener(listener);
		locService.start();
		screen = intent.getBooleanExtra("screen",false);
		TelephonyManager telManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
		Log.d("LocationFilter","onCreate开始");
		/*share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LatLng point = new LatLng(sharelocation.getLatitude(), sharelocation.getLongitude());
				mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
			}
		});*/
	}

	private void readFile()
	{
		FileInputStream in = null;
		BufferedReader reader = null;
		try {
			in = openFileInput("contactperson");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(" ");
				contactsList.push(temp[1]);
			}
			//Toast.makeText(LocationFilter.this,"readFile："+contactsList.size(),Toast.LENGTH_SHORT).show();
			Log.d("LocationFilter","ReadFile");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean getCallLogState(String phoneNumber) {

		ContentResolver cr = getContentResolver();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(LocationFilter.this,new String[]{Manifest.permission.READ_CALL_LOG},8);
		}
		final Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI,
				new String[]{CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DURATION},
				CallLog.Calls.NUMBER + "=? and " + CallLog.Calls.TYPE + "= ?",
				new String[]{phoneNumber, "2"}, null);

		if(cursor.moveToNext()){
			int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
			int durationIndex1 = cursor.getColumnIndex(CallLog.Calls.NUMBER);
			long durationTime = cursor.getLong(durationIndex);
			String number = cursor.getString(durationIndex1);
			if(durationTime > 0){
				isLink = true;

				//Toast.makeText(this,"持续时间"+durationTime,Toast.LENGTH_SHORT).show();
			} else {
				isLink = false;
				Log.d("LocationFilter","getCalllog的contactlist:"+contactsList.size());
				//Toast.makeText(this,"未接听",Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			//Toast.makeText(getActivity(),"没有记录",Toast.LENGTH_SHORT).show();
		}
		return isLink;
	}

	public class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
				case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
					if (isHangUp) {//若isHangUp为true说明是由OFFHOOK状态触发得IDLE状态，所以为挂断
						isHangUp = false;
						//Toast.makeText(LocationFilter.this,"挂断了size为："+contactsList.size(),Toast.LENGTH_SHORT).show();
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								/**
								 *要执行的操作
								 */
								if(!contactsList.empty())
								{
									getCallLogState(contactsList.peek());
									if(isLink==false)
									{
										contactsList.pop();
										call();
										//smsOpera();
									}
									else
									{
										Log.d("LocationFilter","接通了");
										flag = false;
										contactsList.clear();
										//readFile();
									}
								}

							}
						}, 1500);
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
					isHangUp = true;
					break;
				default:
					break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	private void call() {
		try {
			Log.d("LocationFilter","call的contactlist:"+contactsList.size());
			if(!contactsList.empty())
			{
				smsOpera();
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:"+contactsList.peek()));
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			else
			{
				flag = false;
				Toast.makeText(LocationFilter.this,"没有其他紧急联系人",Toast.LENGTH_SHORT).show();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	private void smsOpera() {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(contactsList.peek(), "adfada", sendMessage, null, null);
		//Toast.makeText(LocationFilter.this,"发短信",Toast.LENGTH_SHORT).show();
		Log.d("LocationFilter","发短信");
	}

	/***
	 * 定位结果回调，在此方法中处理定位结果
	 */
	BDAbstractLocationListener listener = new BDAbstractLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location != null && (location.getLocType() == 161 || location.getLocType() == 66)) {
				Message locMsg = locHander.obtainMessage();
				Bundle locData;
				locData = Algorithm(location);
				if (locData != null) {
					locData.putParcelable("loc", location);
					locMsg.setData(locData);
					locHander.sendMessage(locMsg);
				}
			}
		}
	};

	/***
	 * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
	 * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
	 * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
	 *
	 * @param location
	 * @return Bundle
	 */
	private Bundle Algorithm(BDLocation location) {
		Bundle locData = new Bundle();
		double curSpeed = 0;
		if (locationList.isEmpty() || locationList.size() < 2) {
			LocationEntity temp = new LocationEntity();
			temp.location = location;
			temp.time = System.currentTimeMillis();
			locData.putInt("iscalculate", 0);
			locationList.add(temp);
		} else {
			if (locationList.size() > 5)
				locationList.removeFirst();
			double score = 0;
			for (int i = 0; i < locationList.size(); ++i) {
				LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
						locationList.get(i).location.getLongitude());
				LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
				double distance = DistanceUtil.getDistance(lastPoint, curPoint);
				curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
				score += curSpeed * Utils.EARTH_WEIGHT[i];
			}
			if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
				location.setLongitude(
						(locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
								/ 2);
				location.setLatitude(
						(locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
								/ 2);
				locData.putInt("iscalculate", 1);
			} else {
				locData.putInt("iscalculate", 0);
			}
			LocationEntity newLocation = new LocationEntity();
			newLocation.location = location;
			newLocation.time = System.currentTimeMillis();
			locationList.add(newLocation);

		}
		return locData;
	}

	/***
	 * 接收定位结果消息，并显示在地图上
	 */
	private Handler locHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				BDLocation location = msg.getData().getParcelable("loc");
				int iscal = msg.getData().getInt("iscalculate");
				if (location != null) {
					LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
					// 构建Marker图标
					BitmapDescriptor bitmap = null;
					if (iscal == 0) {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark); // 非推算结果
					} else {
						bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark); // 推算结果
					}

					// 构建MarkerOption，用于在地图上添加Marker
					OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
					// 在地图上添加Marker，并显示
					if (mBaiduMap != null)
					{
						mBaiduMap.clear();
					}
					mBaiduMap.addOverlay(option);
					mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
					Log.d("point",""+point);
					mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
				}
                /*if(screen)
                {
                    mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                        public void onSnapshotReady(Bitmap snapshot) {
                            File file = new File("/mnt/sdcard/test.png");
                            FileOutputStream out;
                            try {
                                out = new FileOutputStream(file);
                                if (snapshot.compress(
                                        Bitmap.CompressFormat.PNG, 100, out)) {
                                    out.flush();
                                    out.close();
                                }
                                Toast.makeText(LocationFilter.this,
                                        "屏幕截图成功，图片存在: " + file.toString(),
                                        Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Toast.makeText(LocationFilter.this, "正在截取屏幕图片...",
                            Toast.LENGTH_SHORT).show();
                }*/
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		flag = false;
		Log.d("LocationFilter","onDestroy开始");
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		locService.unregisterListener(listener);
		locService.stop();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.d("LocationFilter","onResume开始contact的size："+contactsList.size());
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		/*share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mBaiduMap != null)
				{
					mBaiduMap.clear();
				}

			}
		});*/
	}

	@Override
	protected void onPause() {
		super.onPause();
		flag = false;
		Log.d("LocationFilter","onPause开始contact的size："+contactsList.size());
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();

	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("LocationFilter","onStart开始contact的size："+contactsList.size());
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("LocationFilter","onReStart开始contact的size："+contactsList.size());
	}

	@Override
	protected void onStop() {
		super.onStop();
		flag = false;
		Log.d("LocationFilter","onStop开始contact的size："+contactsList.size());
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
		//currentAddr = reverseGeoCodeResult.getAddress();
		if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(LocationFilter.this, "抱歉，未找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		//mBaiduMap.clear();

		mShareUrlSearch
				.requestLocationShareUrl(new LocationShareURLOption()
						.location(reverseGeoCodeResult.getLocation()).snippet("测试分享点")
						.name(reverseGeoCodeResult.getAddress()));
         currentAddr = reverseGeoCodeResult.getAddress();

	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult shareUrlResult) {
		sendMessage = currentAddr+shareUrlResult.getUrl();
		if(flag==true)
		{
			Log.d("LocationFilter","定位后分享"+currentAddr+sendMessage);
			call();
		}
		//smsOpera();
		// 分享短串结果
		/*Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友通过百度地图SDK与您分享一个位置: " + currentAddr
				+ " -- " + shareUrlResult.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将短串分享到"));*/
	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult shareUrlResult) {

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
	}

	@Override
	public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {

	}

	/**
	 * 封装定位结果和时间的实体类
	 * 
	 * @author baidu
	 *
	 */
	class LocationEntity {
		BDLocation location;
		long time;
	}
}
