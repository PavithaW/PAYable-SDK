package com.cba.payablesdk;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import common.PayableCallBack;
import common.SwipeSales;
import models.TxDSSaleReq;
import models.TxDsEmvReq;
import models.TxSaleRes;
import util.SDKUtil;

/**
 * Created by Dell on 12/22/2016.
 */

public class CRReaderInf extends CRReader implements PayableEMVCallBack{

    private double m_amount ;
    private  PayableCallBack  payableCallBack ;
    private PayableCardReaderCallBack deviceCB ;
    private Context context ;

    private TxSaleRes m_salesRes ;
    private String m_merchantInvoice  ;


    public CRReaderInf(Context c , PayableCardReaderCallBack deviceCallBack , PayableCallBack cb, double amount , String invoice ){
        super(c,deviceCallBack ,cb) ;
        m_amount = amount ;
        payableCallBack = cb ;
        deviceCB = deviceCallBack ;
        context = c ;
        m_salesRes = null ;
        m_merchantInvoice = invoice ;
    }

    public void startCardReader() throws PayableException {
        super.setupCardReader();
    }


    @Override
    protected POS_TYPE getPosType() {
       return POS_TYPE.BLUETOOTH ;
    }

    @Override
    protected void statusCallBack(int status, String message) {
        //Log.i(TAG, "inside statusCallBack");
        //Log.i(TAG, "status :" + status);
        //Log.i(TAG, "message :" + message);

    }

    @Override
    protected String getAmount() {

        String str = Double.toString(m_amount * 100);
       // Log.i(TAG , "Generated amount string :" + str) ;

        BigDecimal b = new BigDecimal(Double.toString(m_amount));
        b = b.multiply(new BigDecimal("100")) ;

        return b.toBigInteger().toString() ;

       // return "200" ;
    }

    @Override
    protected void swipeCallBack(String ksn, String cardHolder, String track2, String maskedPan) {
       // Log.i(TAG, "inside swipeCallBack");
       // Log.i(TAG, "ksn : " + ksn);
       // Log.i(TAG, "cardHolder : " + cardHolder);

        m_salesRes = null ;

        TxDSSaleReq txDSSaleReq = new TxDSSaleReq();
        txDSSaleReq.setAmount(m_amount);
       // txDSSaleReq.setInvoice(133);
        txDSSaleReq.setInvoice(com.cba.payablesdk.UserConfig.generateINVOICE(context)) ;
        txDSSaleReq.setF35(track2);
        txDSSaleReq.setCardHolderName(cardHolder);
        txDSSaleReq.setClientTime(new Date());
        //txDSSaleReq.setCardReaderId("09113309115100900033");
       // txDSSaleReq.setCardReaderId("19213214416020100160");
       // txDSSaleReq.setCardReaderId("02700334916022300020");
       // txDSSaleReq.setCardReaderId("09113309115100900005");


        if(deviceId != null){
            txDSSaleReq.setCardReaderId(deviceId);
        }else {
            txDSSaleReq.setCardReaderId("none");
        }

        txDSSaleReq.setKsn(ksn);
        txDSSaleReq.setMaskedPan(maskedPan);

        //txDSSaleReq.setKeypadL4("2044");
        txDSSaleReq.setKeypadL4("1111");

        txDSSaleReq.setTsToken(System.currentTimeMillis());
        txDSSaleReq.setRndToken(SDKUtil.generateLongToken());

        if(m_merchantInvoice != null){
            txDSSaleReq.setMerchantInvoiceId(m_merchantInvoice);
        }


        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(txDSSaleReq);

        SwipeSales swipeSales = new SwipeSales(payableCallBack);

        try {
            swipeSales.proxy_swipesales(context, SDKUtil.generateMD5(strmd5), txDSSaleReq);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void emvCallBack(String data) {
        //Log.i(TAG, "inside emvCallBack");
        //Log.i(TAG, "data : " + data);

        m_salesRes = null ;

        TxDsEmvReq req = new TxDsEmvReq() ;
        req.setAmount(m_amount);
        req.setClientTime(new Date());
        req.setTsToken(System.currentTimeMillis());
        req.setRndToken(SDKUtil.generateLongToken());
       // req.setInvoice(133);
        req.setInvoice(com.cba.payablesdk.UserConfig.generateINVOICE(context)) ;
       // req.setCardReaderId("02700334916022300020");
       // req.setCardReaderId("19213214416020100160");
      //  req.setCardReaderId("09113309115100900005");
       // req.setCardReaderId("09113309115100900033");

        if(deviceId != null){
            req.setCardReaderId(deviceId);
        }else {
            req.setCardReaderId("none");
        }

        req.setIcData(data);

        if(m_merchantInvoice != null){
            req.setMerchantInvoiceId(m_merchantInvoice);
        }

        EMVSalesTx api = new EMVSalesTx(this);
        try {
            api.proxy_emvsales(context,req);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void iccErrCallBack(String data) {

    }

    @Override
    protected void iccIssuerScriptSuccessCallBack(String data) {

    }

    @Override
    protected void iccIssuerScriptFailCallBack(String data) {

    }

    @Override
    protected void navigateToSigScreen() {

        if(m_salesRes != null){
            payableCallBack.onSuccessSales(m_salesRes);
        }

    }

    @Override
    public void onSuccessSales(TxSaleRes txSaleRes) {

        isICCOnlineTxSuccess = true ;
        isICCOnlineTxFail = false ;
        strF39ErrMsg = null ;

        if(txSaleRes.getArpc() != null){
            deviceCB.cardReaderStatus(PayableCardReaderCallBack.CHIP_UPDATE, "Updating transaction status to the card.");
            m_salesRes = txSaleRes ;
            writeIssuerScriptOnSuccess(txSaleRes.getArpc());

        }else{
            deviceCB.cardReaderStatus(PayableCardReaderCallBack.CHIP_UPDATE, "Updating transaction status to the card.");
            writeIssuerScriptOnSuccess("8A023030");
        }
    }

    @Override
    public void onFailureSales(PayableException e) {

        if(e.getErrcode() == PayableException.TIMEOUT_ERROR){

            return ;
        }

        isICCOnlineTxSuccess = false ;
        isICCOnlineTxFail = true ;
        strF39ErrMsg = e.getMessage() ;

        if(e.getIsState() == 1){
            writeIssuerScriptOnError(e.getIsMessage());
        }else{
            writeIssuerScriptOnError("8A023035") ;
        }



    }
}
