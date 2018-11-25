package com.cba.payablesdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import models.Merchant;
import models.TxDSSaleReq;
import models.TxDsEmvReq;
import models.TxSaleRes;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import util.SDKUtil;
import webapis.*;

/**
 * Created by Dell on 1/6/2017.
 */

class EMVSalesTx {

    protected static final String TAG = "JEYLOGS";

    private PayableEMVCallBack callBack ;

    public EMVSalesTx(PayableEMVCallBack cb){
        callBack = cb ;
    }

    public void proxy_emvsales(Context context, TxDsEmvReq req) throws NoSuchAlgorithmException {

        String strMd5 = SDKUtil.generateMD5(req.getJson()) ;
        RestClient.PayableAPI service = RestClient.getPayableAPI(context);

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        Call<TxSaleRes> call = service.proxy_emvsale(strMd5, webapis.Config.VER_SIG, webapis.Config.UserAgent,m.getId(), m.getAuth1(),
                m.getAuth2(), deviceId, simId, req);

        call.enqueue(new Callback<TxSaleRes>(){
            public void onResponse(Response<TxSaleRes> response, Retrofit retrofit){
                Payable.reset(1427482071469L);


                //Log.d(TAG, "inside oon success: " );

                if (response.isSuccess()) {
                    TxSaleRes txSaleRes = response.body();
                    callBack.onSuccessSales(txSaleRes);

                }else{

                    //response.toString() ;
                    //Log.d(TAG, "response.toString() : " + response.raw().toString());
                    String error_code = response.headers().get("mpos-expCode");
                    String error_message = response.headers().get("mpos-expMsg");

                    //Log.d(TAG, "mpos-expCode: " + error_code);
                    //Log.d(TAG, "mpos-expMsg: " + error_message);

                    PayableException ex = new PayableException(Integer.parseInt(error_code), error_message) ;

                    String strIsStatus = response.headers().get("emv-is-status") ;

                    if(strIsStatus != null && strIsStatus.equals("1")){
                        String strIssuerScript = response.headers().get("emv-is") ;

                        if(strIssuerScript != null){
                            ex.setIsState(1);
                            ex.setIsMessage(strIssuerScript);
                        }
                    }

                    callBack.onFailureSales(ex);
                }


            }

            public void onFailure(Throwable t){
                Payable.reset(1427482071469L);
                //Log.d(TAG, "inside onFailure : " + t.toString() );

               // PayableException ex = new PayableException(PayableException.TIMEOUT_ERROR, "Issue with network connectivity") ;

                callBack.onFailureSales(new PayableException(PayableException.TIMEOUT_ERROR, "Issue with network connectivity"));

            }

        }) ;

    }
}
