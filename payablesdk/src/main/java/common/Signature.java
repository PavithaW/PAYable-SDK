package common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import models.Merchant;
import models.ModelSignature;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 3/1/2016.
 */
public class Signature {

    private PayableCallBack payableCallBack;


    public Signature() {
    }

    public Signature(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_signature(Activity context, String md5, ModelSignature modelSignature) {


        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<Object> call = service.proxy_sig(md5, Config.VER_SIG, Config.UserAgent,m.getId(), m.getAuth1(),
                m.getAuth2(), deviceId, simId, modelSignature);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {

                Payable.reset(1427482071469L);

                if (response.isSuccess()) {

                    try {
                        Gson gson = new Gson();
                        String hhh = gson.toJson(response.body());
                        JSONObject jsonObject = new JSONObject(hhh);
                        String status = jsonObject.getString("status");
                        if (status.equals("1.0")) {
                            payableCallBack.onSuccessSignature();
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureSignature(new PayableException(Integer.parseInt(error_code), error_message));

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
