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
import java.util.List;

import models.Merchant;
import models.OpenTxHistoryReq;
import models.PayableTX;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 12/8/2015.
 */
public class OpenPayableTX {

    private static final String LOG_TAG = "JEYLOGS";
    private PayableCallBack payableCallBack;

    public OpenPayableTX() {

    }

    public OpenPayableTX(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_opentx(Activity context, String md5, int pageId, int pageSize) {
        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<List<PayableTX>> call = service.proxy_opentransaction(
                md5,
                Config.VER_SIG,
                Config.UserAgent,
                m.getId(),
                m.getAuth1(),
                m.getAuth2(),
                deviceId,
                simId,
                new OpenTxHistoryReq(pageId, pageSize));


        call.enqueue(new Callback<List<PayableTX>>() {
            @Override
            public void onResponse(Response<List<PayableTX>> response, Retrofit retrofit) {


                Payable.reset(1427482071469L);


                if (response.isSuccess()) {

                    try {

                        //Log.i(LOG_TAG, response.body().toString()) ;

                        ArrayList<PayableTX> result = (ArrayList<PayableTX>) response.body();

                        String json = new Gson().toJson(result);

                        Type type = new TypeToken<ArrayList<PayableTX>>() {
                        }.getType();
                        ArrayList<PayableTX> inpList = new Gson().fromJson(json, type);

                        payableCallBack.onSuccessOpenTx(inpList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureOpenTx(new PayableException(Integer.parseInt(error_code), error_message));

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
