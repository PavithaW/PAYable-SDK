package common;

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

import models.BatchlistRes;
import models.Merchant;
import models.OpenTxHistoryReq;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 3/29/2016.
 */
public class BatchList {

    private PayableCallBack payableCallBack;

    public BatchList() {
    }

    public BatchList(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_batchlist(Context context,String md5, OpenTxHistoryReq openTxHistoryReq)
    {

       // Payable.reset(1427482071469L);

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<List<BatchlistRes>> call = service.proxy_batchlist(md5, Config.VER_SIG, Config.UserAgent, m.getId(),
                m.getAuth1(),
                m.getAuth2(),deviceId, simId, openTxHistoryReq);
        call.enqueue(new Callback<List<BatchlistRes>>() {
            @Override
            public void onResponse(Response<List<BatchlistRes>> response, Retrofit retrofit) {

                Payable.reset(1427482071469L);

                if (response.isSuccess()) {

                    try {

                        ArrayList<BatchlistRes> result = (ArrayList<BatchlistRes>) response.body();

                        String json = new Gson().toJson(result);

                        Type type = new TypeToken<ArrayList<BatchlistRes>>() {
                        }.getType();
                        ArrayList<BatchlistRes> inpList = new Gson().fromJson(json, type);

                        payableCallBack.onSuccessBatchList(inpList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureBatchList(new PayableException(Integer.parseInt(error_code),error_message));

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
