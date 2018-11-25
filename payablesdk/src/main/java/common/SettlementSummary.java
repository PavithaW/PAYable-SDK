package common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import models.Merchant;
import models.SimpleReq;
import models.TxSettlementSummaryEle;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 12/31/2015.
 */
public class SettlementSummary {

    private PayableCallBack payableCallBack;

    public SettlementSummary(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_settlementsummary(Activity context, String md5, SimpleReq simpleReq) {

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;


        RestClient.PayableAPI service = RestClient.getPayableAPI(context);

        Merchant m = UserConfig.getUser(context) ;

        Call<List<TxSettlementSummaryEle>> call = service.proxy_settlementsummary(md5,Config.VER_SIG, Config.UserAgent, m.getId(), m.getAuth1(),
                m.getAuth2(), deviceId,
                simId, simpleReq);
        call.enqueue(new Callback<List<TxSettlementSummaryEle>>() {
            @Override
            public void onResponse(Response<List<TxSettlementSummaryEle>> response, Retrofit retrofit) {


                Payable.reset(1427482071469L);

                if (response.isSuccess()) {
                    try {
                        ArrayList<TxSettlementSummaryEle> result = (ArrayList<TxSettlementSummaryEle>) response.body();

                        String json = new Gson().toJson(result);

                        Type type = new TypeToken<ArrayList<TxSettlementSummaryEle>>() {
                        }.getType();
                        ArrayList<TxSettlementSummaryEle> settlelist = new Gson().fromJson(json, type);

                        payableCallBack.onSuccessSettlementSummary(settlelist);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureSettlementSummary(new PayableException(Integer.parseInt(error_code), error_message));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Payable.reset(1427482071469L);

                payableCallBack.onFailureSales(new PayableException(PayableException.TIMEOUT_ERROR, "Issue with network connectivity"));

                // t.printStackTrace();
            }
        });
    }
}
