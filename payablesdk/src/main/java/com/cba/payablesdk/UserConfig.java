package com.cba.payablesdk;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import models.Merchant;

public class UserConfig {

	private final static String STRPREF = "PREFMPOsdkSR2";
	
	private final static String KEY_APP_STATE = "mposappstate";
	private final static String KEY_USER_ID = "mpos_sdk_userId";
	private final static String KEY_USER_LOGIN_NAME = "mpos_sdk_username";
	private final static String KEY_USER_AUTH1 = "mpos_user2_sdk_auth1";
	private final static String KEY_USER_AUTH2 = "mpos_user2_sdk_auth2";
	private final static String KEY_USER_READER_ID = "mpos_user_readerid";
	private final static String KEY_USER_PWD_FLAG = "mpos_user_pwdflag";
	private final static String KEY_USER_READER_TYPE = "mpos_user_reader_type";
	private final static String KEY_USER_MAX_AMOUNT = "mpos_user_max_amount";
	private final static String KEY_USER_TETMINAL_ID = "mpos_user_terminal_id";
	private final static String KEY_USER_AQ_ID = "mpos_user_aq_id";
	private final static String KEY_USER_BUSINESS_NAME = "mpos_user_business_name";
	private final static String KEY_USER_BUSINESS_ADDRESS = "mpos_user_business_address";
	
	private final static String KEY_STN = "mpos_stn_val" ;
	private final static String KEY_INVOICE = "mpos_invoice_val_int" ;
	private final static String KEY_BATCH = "mpos_batch_val" ;
	private final static String KEY_READER = "mpos_crreader_val" ;
	
	public final static int STATUS_LOGOUT = 0 ;
	public final static int STATUS_LOGIN = 1 ;
	
	public final static int READER_DEVICE1 = 1 ;
	public final static int READER_DS_AUDIO = 2 ;
	public final static int READER_DS_BT = 3 ;
	
	public static final float DEFAULT_MAX_AMOUNT = 50000 ;
	
	
	public static void setState(Context c, int state){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(KEY_APP_STATE, state) ;
		editor.commit() ;
	}
	
	public static int getState(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		return pref.getInt(KEY_APP_STATE, 0) ;
	}

	public static boolean isLogedIn(Context c){
		if(getState(c) == STATUS_LOGIN){
			return true;
		}
		return false ;
	}
	
	public static int getActiveReader(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		return pref.getInt(KEY_READER, READER_DEVICE1) ;
	}
	
	public static String getReaderId(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		return pref.getString(KEY_USER_READER_ID, null) ;
	}
	
	public static void setActiveReader(Context c , int state){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putInt(KEY_READER, state) ;
		editor.commit() ;
	}

	public static void setLogout(Context c){
		setState(c,STATUS_LOGOUT);
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putLong(KEY_USER_AUTH1,0);
		editor.putLong(KEY_USER_AUTH2, 0);
		editor.commit();

	}
	
	public static void setUser(Context c , Merchant m){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		
		//Log.i("JEYLOGS", "Storing user id:" + m.getId());
		editor.putLong(KEY_USER_ID, m.getId());
		editor.putString(KEY_USER_LOGIN_NAME, m.getUserName()) ;
		editor.putLong(KEY_USER_AUTH1, m.getAuth1());
		editor.putLong(KEY_USER_AUTH2, m.getAuth2());
		editor.putString(KEY_USER_READER_ID, m.getCardReaderId());
		//KEY_USER_PWD_FLAG
		editor.putInt(KEY_USER_PWD_FLAG, m.getPwdFlag() ) ;
		
		editor.putInt(KEY_USER_READER_TYPE , m.getReaderType()) ;
		editor.putFloat(KEY_USER_MAX_AMOUNT , m.getMax_amount()) ;
		editor.putString(KEY_USER_TETMINAL_ID , m.getTerminalId()) ;
		editor.putString(KEY_USER_AQ_ID , m.getCardAqId()) ;
		
		//Log.i("JEYLOGS", "Business name: " + m.getBusinessName());
		
		editor.putString(KEY_USER_BUSINESS_NAME  , m.getBusinessName()) ;
		editor.putString(KEY_USER_BUSINESS_ADDRESS , m.getAddress()) ;
		
		editor.commit();
		
	}
	
	public static float getMaxTxValue(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		return pref.getFloat(KEY_USER_MAX_AMOUNT, DEFAULT_MAX_AMOUNT) ;
	}
	
	public static int getPwdFlag(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		return pref.getInt(KEY_USER_PWD_FLAG, Merchant.PWD_FLAG_APP) ;
	}
	
	public static Merchant getProfile(Context c){
		
		Merchant m = new Merchant() ;
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		
		m.setUserName(pref.getString(KEY_USER_LOGIN_NAME, "")) ;
		m.setTerminalId(pref.getString(KEY_USER_TETMINAL_ID, ""));
		m.setCardAqId(pref.getString(KEY_USER_AQ_ID, ""));
		
		m.setMax_amount(pref.getFloat(KEY_USER_MAX_AMOUNT, 0));
		m.setReaderType(pref.getInt(KEY_USER_READER_TYPE , 0));
		
		m.setBusinessName(pref.getString(KEY_USER_BUSINESS_NAME, ""));
		m.setAddress(pref.getString(KEY_USER_BUSINESS_ADDRESS, ""));
		
		return m ;
		
	}
	
	public static Merchant getUser(Context c){
		Merchant m = new Merchant() ;
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		
		m.setId(pref.getLong(KEY_USER_ID, -1)) ;
		m.setUserName(pref.getString(KEY_USER_LOGIN_NAME, "")) ;
		m.setAuth1(pref.getLong(KEY_USER_AUTH1, 0));
		m.setAuth2(pref.getLong(KEY_USER_AUTH2, 0));
		
		return m ;
	}
	

	
	public static int generateINVOICE(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		int val = pref.getInt(KEY_INVOICE, 0) + 1 ;
		
		if(val > 999999999){
			val = 1 ;
		}
		
		Editor editor = pref.edit();
		editor.putInt(KEY_INVOICE, val) ;
		editor.commit();
		
		return val ;
	}
	
	public static int generateBatchId(Context c){
		SharedPreferences pref = c.getSharedPreferences(STRPREF,
				Context.MODE_PRIVATE);
		int val = pref.getInt(KEY_BATCH, 0) + 1 ;
		
		if(val > 999999){
			val = 1 ;
		}
		
		Editor editor = pref.edit();
		editor.putInt(KEY_BATCH, val) ;
		editor.commit() ;
		
		return val ;
	}
	
	
	
}
