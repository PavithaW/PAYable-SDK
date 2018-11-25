package common;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;
import com.facebook.stetho.inspector.helper.IntegerFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import models.Merchant;
import models.SimpleAck;
import models.SimpleReq;
import models.ValueBean;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 12/7/2015.
 */
public class AddEcho {

    private static final String LOG_TAG = "JEYLOGS";

    private PayableCallBack payableCallBack;

    public AddEcho() {
    }

    public AddEcho(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_addecho(Context context,String md5, int value) {

        SimpleReq req = new SimpleReq() ;
        req.setValue(value);

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
       // Call<String> call = service.proxy_echo(md5, Config.UserAgent,1, new ValueBean(value));
        Call<SimpleAck> call = service.proxy_echo(md5,  Config.VER_SIG,
                Config.UserAgent,
                m.getId(),
                m.getAuth1(),
                m.getAuth2(),
                deviceId,
                simId,req);
        call.enqueue(new Callback<SimpleAck>() {
            @Override
            public void onResponse(Response<SimpleAck> response, Retrofit retrofit) {

                Payable.reset(1427482071469L);

                if (response.isSuccess()) {

                    // request successful (status code 200, 201)
                   // int code = 0;
                   // String result = response.body().toString();

                    SimpleAck res = response.body() ;



                    payableCallBack.onSuccessEcho(res.getStatus());
                } else {
                    String error_code = response.headers().get("mpos-expCode");
                    String error_message = response.headers().get("mpos-expMsg");

                    payableCallBack.onFailureEcho(new PayableException(Integer.parseInt(error_code), error_message));
                }

            }

            @Override
            public void onFailure(Throwable t) {
                //Log.i(LOG_TAG , "inside onFailure.") ;
                //Log.i(LOG_TAG , "Err : " + t.toString()) ;
               Payable.reset(1427482071469L);
                payableCallBack.onFailureEcho(new PayableException(PayableException.TIMEOUT_ERROR, "Issue with network connectivity"));

            }
        });
    }
}
