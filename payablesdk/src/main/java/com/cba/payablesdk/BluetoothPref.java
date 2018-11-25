package com.cba.payablesdk;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class BluetoothPref {

	protected static final String TAG = "JEYLOGS";
	
	private static final String PREF_NAME = "BLUETOOTH_PREF";

	public static final String KEY_DEVICE_NAME = "device_name";

	public static final String KEY_DEVICE_ADDRESS = "device_address";

	public static void setDeviceReaderStatus(Context c, String devicename, String deviceadd) {
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString(KEY_DEVICE_NAME, devicename);
		editor.putString(KEY_DEVICE_ADDRESS, deviceadd);
		editor.commit();
	}

	public static String getDeviceName(Context c) {
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getString(KEY_DEVICE_NAME, null);
	}

	public static String getDeviceAddress(Context c) {
		SharedPreferences pref = c.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return pref.getString(KEY_DEVICE_ADDRESS, null);

		//return "8C:DE:52:83:B7:96" ;
		//return "1C:87:76:0D:E7:7B" ;
	}
	
	public static String generateValidDeviceName(Context context){

		//return "MPOS5100900006" ;
		//return "MPOS6022300020" ;

		String deviceId = UserConfig.getReaderId(context) ;
		
		String strValidName = "MPOS" + deviceId.substring(deviceId.length() - 10) ;
		
		//Log.i(TAG, "Generated device name: " + strValidName) ;
		
		return strValidName ;
	}
	
	public static boolean isRegiesterRequired(Context context){
		
		//Log.i(TAG, "inside isRegiesterRequired.");
		
		SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strDeviceName = pref.getString(KEY_DEVICE_NAME, null);

		//Log.i(TAG, "strDeviceName : " + strDeviceName);
		
		if(strDeviceName == null || strDeviceName.trim().length() < 5){
			return true ;
		}
		
		String strAddress =  pref.getString(KEY_DEVICE_ADDRESS, null);

		//Log.i(TAG, "strAddress : " + strAddress);
		
		if(strAddress == null || strAddress.trim().length() < 5){
			return true ;
		}
		
		//Log.i(TAG, "before fetching reader id.");
		
		String deviceId = com.cba.payablesdk.UserConfig.getReaderId(context) ;
		
		//Log.i(TAG, "After fetching reader id.");
		
		if(deviceId == null){
			//Log.i(TAG, "deviceId is null");
			return true ;
		}else{
			//Log.i(TAG, "deviceId is not null");
		}
		
		String strValidName = "MPOS" + deviceId.substring(deviceId.length() - 10) ;
		
		//Log.i(TAG, "Valid device name: " + strValidName) ;
		
		if(! strDeviceName.equalsIgnoreCase(strValidName)){
			return true ;
		}
		
		
		
		return false ;
	}

}
