package common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;

import models.Merchant;
import models.TxSettlementReq;
import models.TxSettlementResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 3/2/2016.
 */
public class Settle {

    private PayableCallBack payableCallBack;

    public Settle() {
    }

    public Settle(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_settle(Activity context, String md5, TxSettlementReq txSettlementReq) {


        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);

        Merchant m = UserConfig.getUser(context) ;

        Call<TxSettlementResponse> call = service.proxy_settlement(md5, Config.VER_SIG,Config.UserAgent, m.getId(), m.getAuth1(),
                m.getAuth2(),deviceId, simId, txSettlementReq);
        call.enqueue(new Callback<TxSettlementResponse>() {
            @Override
            public void onResponse(Response<TxSettlementResponse> response, Retrofit retrofit) {

                Payable.reset(1427482071469L);

                if (response.isSuccess()) {
                    TxSettlementResponse txSettlementResponse = response.body();

                    payableCallBack.onSuccessSettle(txSettlementResponse);
                } else {

                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureSettle(new PayableException(Integer.parseInt(error_code), error_message));

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
