package common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;

import models.Merchant;
import models.TxDSSaleReq;
import models.TxSaleRes;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 2/29/2016.
 */
public class SwipeSales {

    private static final String TAG = "JEYLOGS";
    private PayableCallBack payableCallBack;

    public SwipeSales() {
    }

    public SwipeSales(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_swipesales(Context context, String md5, TxDSSaleReq txDSSaleReq) {


        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<TxSaleRes> call = service.proxy_swipesale(md5, Config.VER_SIG, Config.UserAgent,m.getId(), m.getAuth1(),
                m.getAuth2(), deviceId, simId, txDSSaleReq);
        call.enqueue(new Callback<TxSaleRes>() {
            @Override
            public void onResponse(Response<TxSaleRes> response, Retrofit retrofit) {

                Payable.reset(1427482071469L);

                if (response.isSuccess()) {
                    try {

                        TxSaleRes txSaleRes = response.body();

                        payableCallBack.onSuccessSales(txSaleRes);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    try {

                        //Log.d(TAG, "response.toString() : " + response.raw().toString());

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        //Log.d(TAG, "mpos-expCode: " + error_code);
                        //Log.d(TAG, "mpos-expMsg: " + error_message);

                        payableCallBack.onFailureSales(new PayableException(Integer.parseInt(error_code), error_message));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Payable.reset(1427482071469L);
                payableCallBack.onFailureSales(new PayableException(PayableException.TIMEOUT_ERROR, "Issue with network connectivity"));
            }
        });

    }
}
