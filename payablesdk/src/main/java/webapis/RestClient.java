package webapis;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.cba.payablesdk.R;
import com.cba.payablesdk.SigninReq;
import com.squareup.okhttp.OkHttpClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import models.BatchlistRes;
import models.CloseTxHistoryReq;
import models.Merchant;
import models.ModelSignature;
import models.OpenTxHistoryReq;
import models.PayableTX;
import models.SimpleAck;
import models.SimpleReq;
import models.TxDSSaleReq;
import models.TxDsEmvReq;
import models.TxSaleRes;
import models.TxSettlementReq;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidReq;
import models.TxVoidRes;
import models.ValueBean;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;


/**
 * Created by Dell on 12/7/2015.
 */
public class RestClient {

    protected static final String TAG = "JEYLOGS";

    private static PayableAPI payableAPI;
    public static OkHttpClient trustTestClient;

    public static PayableAPI getPayableAPI(Context context) {

        if(trustTestClient == null){
            trustTestClient = trustcert(context) ;
        }

        if (payableAPI == null) {
//            trustTestClient = trustcert(context);
//            trustTestClient.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    //TODO: Make this more restrictive
//                    return true;
//                }
//            });


//            trustTestClient.setReadTimeout(10, TimeUnit.SECONDS);
//            trustTestClient.setConnectTimeout(10, TimeUnit.SECONDS);
//            trustTestClient.interceptors().add(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//
//                    Response response = chain.proceed(chain.request());
//                    return response;
//                }
//            });

            //Log.i(TAG , "inside the getPayableAPI - 1") ;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.GLOBAL_URL)
                    .client(trustTestClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Log.i(TAG , "inside the getPayableAPI - 2") ;

            payableAPI = retrofit.create(PayableAPI.class);



        }

        return payableAPI;
    }

    public static OkHttpClient trustcert(Context context) {
        //Log.i(TAG , "inside the trustcert - 1") ;
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        try {
            KeyStore ksTrust = KeyStore.getInstance("BKS");
           // InputStream instream = context.getResources().openRawResource(R.raw.clienttruststore);

            String tm = "AAAAAQAAABRYMHpzoMtHEW38ItbIjTN0JoDkIAAABrQBAAVteWtleQAAAUydsxyeAAAAAAAFWC41MDkAAAQLMIIEBzCCAu+gAwIBAgIJAJkihChhGkKEMA0GCSqGSIb3DQEBCwUAMIGZMQswCQYDVQQGEwJMSzEQMA4GA1UECAwHV2VzdGVybjEQMA4GA1UEBwwHQ29sb21ibzEWMBQGA1UECgwNQ0JBIFNvbHV0aW9uczELMAkGA1UECwwCSVQxFzAVBgNVBAMMDjEyMy4yMzEuMTQuMjA3MSgwJgYJKoZIhvcNAQkBFhlkZXZlbG9wZXJAY2Jhc29sdXRpb25zLmxrMB4XDTE1MDQwOTEwMjExN1oXDTE2MDQwODEwMjExN1owgZkxCzAJBgNVBAYTAkxLMRAwDgYDVQQIDAdXZXN0ZXJuMRAwDgYDVQQHDAdDb2xvbWJvMRYwFAYDVQQKDA1DQkEgU29sdXRpb25zMQswCQYDVQQLDAJJVDEXMBUGA1UEAwwOMTIzLjIzMS4xNC4yMDcxKDAmBgkqhkiG9w0BCQEWGWRldmVsb3BlckBjYmFzb2x1dGlvbnMubGswggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDjxHVztmqhfzsQQ+6zBNNL+rRjp3UvRp1lhrQ0syC5rv4JPDAyXd11jI+hHIRPz82bT9np4AzHM4AKJRppOUwFjWMrs3ztFqvwEXNR4FGP9qW3yaZ2x1wznvVEegZffQ+rmcVoCmUymx3Ayv9N+yF2Gf25DY1Jh39PukBi+iXGsZJ7s/fvUOxlqo2caBbC3RJ3Ds5nsHOBz5AG84xc3MtjG5YbBCJc2f3cEOaRgSGbrkUZ5urIp4sqTetMAFRsZfeZb2eTZZRMhQ1dNziIetYyJYGjTL3V2CIK96kKjpPdTD3HoRqt9UxO73DwgYghX1xZv83TDIyCtLtRMHpuQdWLAgMBAAGjUDBOMB0GA1UdDgQWBBRM48unVTBfK9sTGOWuAJG+vxnMmjAfBgNVHSMEGDAWgBRM48unVTBfK9sTGOWuAJG+vxnMmjAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4IBAQBKxYIXvVz7OsDI2p32koUb2TayOwtY/kgjCscIfGaKC9Fqv018pto9LkSeF+Jp9tYKZdxUzVCtI7Igw7w8aVM9mwwiFasJzu3gP6Mi4ZzKBVgAnQDNQF8HGqzbzBE4nS/kx7hNpr01H/der4HkC5qsZmQajZYz1Tj2AbcbeWwTi8P3QtXT5yasGkTXWrqF+vzS/dRGDKHzMym2jnJpl+sRsW1QyJ5lo5DUEcNNlgXHzLjzYKT7R7Y5AQcXBJKUvSETIQ+WE95rukfGjPgZYIz5BZoDefTrZIEnwVmOteyixXISvt607dbQPWW5y1r2hmSs+2soNt6YCEme0BXs4ddMACX7jxJAPeloj6SkY1l6LwIYaWEr";

            InputStream instream = new ByteArrayInputStream(Base64.decode(tm.getBytes(),Base64.DEFAULT));

            String strCert = "" ;
            ksTrust.load(instream, "Pium6y7u8i9o0p".toCharArray());
            // TrustManager decides which certificate authorities to use.
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ksTrust);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {

                    //Log.i(TAG , "inside the hostnameVerifier - verify");
                    //Log.i(TAG , "Call back s : = " + s);

                    return true ;
                }
            } ;

            okHttpClient.setHostnameVerifier(hostnameVerifier);

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
            //Log.i(TAG , "inside the tm exception.");
            e.printStackTrace();
            //Log.i(TAG , e.toString());
        }
        return okHttpClient;
    }

    public static OkHttpClient trustcert2(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
        try {
            KeyStore ksTrust = KeyStore.getInstance("BKS");
            InputStream instream = context.getResources().openRawResource(R.raw.clienttruststore);
            ksTrust.load(instream, "Pium6y7u8i9o0p".toCharArray());

           // SSLSocketFactory ret = new SSLSocketFactory(ksTrust) ;

            // TrustManager decides which certificate authorities to use.
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ksTrust);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
            e.printStackTrace();
        }
        return okHttpClient;
    }


    public interface PayableAPI {

        @Headers("Content-Type:application/json")
        @POST("Echo")
        Call<SimpleAck> proxy_echo(@Header("Content-Sig") String content,
                                   @Header("Ver-Sig") String version,
                                   @Header("User-Agent") String useragent,
                                   @Header("mpos-userid") long userid,
                                   @Header("mpos-auth1") long auth1,
                                   @Header("mpos-auth2") long auth2,
                                   @Header("mpos-deviceid") String DeviceId,
                                   @Header("mpos-simid") String simId,
                                   @Body SimpleReq req);

        @Headers("Content-Type:application/json")
        @POST("OpenTxHistoryRV2")
        Call<List<PayableTX>> proxy_opentransaction(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body OpenTxHistoryReq openTxHistoryReq);

        @Headers("Content-Type:application/json")
        @POST("CloseTxHistoryRV2")
        Call<List<PayableTX>> proxy_closetransaction(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body CloseTxHistoryReq closeTxHistoryReq);


        @Headers("Content-Type:application/json")
        @POST("DsSale")
        Call<TxSaleRes> proxy_swipesale(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body TxDSSaleReq txDSSaleReq);

        @Headers("Content-Type:application/json")
        @POST("DSEmvSale")
        Call<TxSaleRes> proxy_emvsale(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body TxDsEmvReq txDsEmvReq);

        @Headers("Content-Type:application/json")
        @POST("SigninSDK")
        Call<Merchant> proxy_signin(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Body SigninReq signinReq);

        @Headers("Content-Type:application/json")
        @POST("VoidRV2")
        Call<TxVoidRes> proxy_void(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body TxVoidReq txVoidReq
        );

        @Headers("Content-Type:application/json")
        @POST("Signature")
        Call<Object> proxy_sig(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body ModelSignature modelSignature
        );


        @Headers("Content-Type:application/json")
        @POST("SettlementSummary")
        Call<List<TxSettlementSummaryEle>> proxy_settlementsummary(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body SimpleReq simpleReq);

        @Headers("Content-Type:application/json")
        @POST("Settlement")
        Call<TxSettlementResponse> proxy_settlement(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body TxSettlementReq txSettlementReq);

        @Headers("Content-Type:application/json")
        @POST("BatchList")
        Call<List<BatchlistRes>> proxy_batchlist(
                @Header("Content-Sig") String content,
                @Header("Ver-Sig") String version,
                @Header("User-Agent") String useragent,
                @Header("mpos-userid") long userid,
                @Header("mpos-auth1") long auth1,
                @Header("mpos-auth2") long auth2,
                @Header("mpos-deviceid") String DeviceId,
                @Header("mpos-simid") String simId,
                @Body OpenTxHistoryReq openTxHistoryReq);

    }


}
