package common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;
import com.cba.payablesdk.UserConfig;

import models.Merchant;
import models.TxVoidReq;
import models.TxVoidRes;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import webapis.Config;
import webapis.RestClient;

/**
 * Created by Dell on 2/29/2016.
 */
public class VoidSales {

    private PayableCallBack payableCallBack;

    public VoidSales() {
    }

    public VoidSales(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public void proxy_voidsales(Activity context,String md5, TxVoidReq txVoidReq)
    {

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        String simId = p.getSimSerialNumber() ;

        Merchant m = UserConfig.getUser(context) ;

        RestClient.PayableAPI service = RestClient.getPayableAPI(context);
        Call<TxVoidRes> call =service.proxy_void(md5,Config.VER_SIG, Config.UserAgent, m.getId(), m.getAuth1(),
                m.getAuth2(), deviceId,simId, txVoidReq);
        call.enqueue(new Callback<TxVoidRes>() {
            @Override
            public void onResponse(Response<TxVoidRes> response, Retrofit retrofit) {

                Payable.reset(1427482071469L);

                if(response.isSuccess())
                {
                    TxVoidRes txVoidRes = response.body();

                    payableCallBack.onSuccessVoid(txVoidRes);
                }
                else {

                    try {

                        String error_code = response.headers().get("mpos-expCode");
                        String error_message = response.headers().get("mpos-expMsg");

                        payableCallBack.onFailureVoid(new PayableException(Integer.parseInt(error_code), error_message));

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
