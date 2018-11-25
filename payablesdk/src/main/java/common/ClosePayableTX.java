package common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.CloseTxHistoryReq;
import models.Merchant;
import models.PayableTX;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 12/29/2015.
 */
public class ClosePayableTX {

    private static final String TAG = "JEYLOGS";


    private PayableCallBack payableCallBack;

    public ClosePayableTX() {

    }

    public ClosePayableTX(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_closetx(Activity context, String md5, int pageId, int pageSize, int master, int visa, int amex, Date stDate, Date enDate, String serchTerm) {

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<List<PayableTX>> call = service.proxy_closetransaction(
                md5,
                Config.VER_SIG,
                Config.UserAgent,
                m.getId(),
                m.getAuth1(),
                m.getAuth2(),
                deviceId,
               simId,
                new CloseTxHistoryReq(pageId, pageSize, master, visa, amex, stDate, enDate, serchTerm));

        call.enqueue(new Callback<List<PayableTX>>() {
            @Override
            public void onResponse(Response<List<PayableTX>> response, Retrofit retrofit) {

                //Log.i(TAG , "inside onResponse") ;

                Payable.reset(1427482071469L);

                if (response.isSuccess()) {


                    try {

                        ArrayList<PayableTX> result = (ArrayList<PayableTX>) response.body();

                        String json = new Gson().toJson(result);

                        //Log.i(TAG , "json :" + json) ;

                        Type type = new TypeToken<ArrayList<PayableTX>>() {
                        }.getType();
                        ArrayList<PayableTX> outpList = new Gson().fromJson(json, type);

                        payableCallBack.onSuccessCloseTx(outpList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureCloseTx(new PayableException(Integer.parseInt(error_code), error_message));

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
