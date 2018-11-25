package com.cba.payablesdk;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.NoSuchAlgorithmException;

import common.PayableCallBack;
import models.Credentials;
import models.Merchant;
import models.TxSaleRes;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import util.SDKUtil;
import webapis.*;
import webapis.Config;

/**
 * Created by Dell on 2/13/2017.
 */

public class LoginApi {

    private static final String TAG = "JEYLOGS";
    private PayableCallBack payableCallBack;
    private Context context ;

    public LoginApi(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_login(Context cx ,Credentials c) {
        context = cx ;

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId();
        String simId = p.getSimSerialNumber();

        //Log.i(TAG,"device id : "+ deviceId);
        //Log.i(TAG,"sim id : "+ simId);

        SigninReq req = new SigninReq();
        req.setUserName(c.getUserName());
        req.setPwd(c.getPwd());
        req.setDeveloperKey(c.getDeveloperKey());
        req.setDeveloperToken(c.getDeveloperToken());
        req.setDeviceId(deviceId);
        req.setSimId(simId);

        String strMd5 = "";

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        try {
            String xxx = gson.toJson(req);
            strMd5 = SDKUtil.generateMD5(xxx);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<Merchant> call = service.proxy_signin(strMd5, webapis.Config.VER_SIG, Config.UserAgent, req);

        call.enqueue(new Callback<Merchant>() {
            @Override
            public void onResponse(Response<Merchant> response, Retrofit retrofit) {
                Payable.reset(1427482071469L);

                if (response.isSuccess()){

                    Merchant m = response.body() ;
                    //Log.i(TAG , "Json string: " +  m.getJson()) ;

                    UserConfig.setUser(context , m);
                    UserConfig.setState(context , UserConfig.STATUS_LOGIN);
                    payableCallBack.onSucessAuthenticate();

                }else{

                    try {

                        //Log.d(TAG, "response.toString()  : " + response.raw().toString());

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        //Log.d(TAG, "mpos-expCode: " + error_code);
                        //Log.d(TAG, "mpos-expMsg: " + error_message);

                        payableCallBack.onFailureAuthenticate(new PayableException(Integer.parseInt(error_code), error_message));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }

            public void onFailure(Throwable t) {
                Payable.reset(1427482071469L);
                payableCallBack.onFailureAuthenticate(new PayableException(PayableException.TIMEOUT_ERROR, "Issue with network connectivity"));
            }
        });


    }


}
