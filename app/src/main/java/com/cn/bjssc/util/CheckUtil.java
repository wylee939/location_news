package com.cn.bjssc.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CheckUtil {
	private static CheckUtil util;
	private static Context ctx;
	private SharedPreferences USER;
	private TelephonyManager tm;
    //判断文件是否存在的TAG
    private boolean FILE_TAG = false;
    private CheckFileSizeListener fileSizeListener;
	private CheckUtil(){
		
	}
	
	public static CheckUtil GetUtil(){
		if(util==null){
			util=new CheckUtil();
		}
		return util;
	}
	

	//网络是否连接
	public static boolean isNetworkAvailable(Context context) {
		boolean flag = false;
		ConnectivityManager systemService = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (systemService.getActiveNetworkInfo() != null) {
			flag = systemService.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}


	// 是否打开GPS
	public boolean isOpenGPS() {
		LocationManager locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}
	
	public static void setNet(final Context mContext) {
		// TODO Auto-generated method stub
		Builder builder = new Builder(mContext,
				AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		builder.setTitle("网络提示信息");
		builder.setMessage("数据连接不可用，如果继续，请先设置！");
		builder.setPositiveButton("去设置", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				/*Intent intent = null;
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(
							android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}*/
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                mContext.startActivity(intent);

			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, "没有网络将无法获取数据！", Toast.LENGTH_SHORT).show();
			}
		});
		builder.create();
		builder.show();	
		}



    public static int daysBetween(String smdate, String bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;

        try {
            cal.setTime(sdf.parse(smdate));
            time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            time2 = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between_days = (time1 - time2) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));

    }


         public interface CheckFileSizeListener{
          void CheckFileFinish();
    }
}
