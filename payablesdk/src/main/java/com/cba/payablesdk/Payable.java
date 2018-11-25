package com.cba.payablesdk;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.AddEcho;
import common.BatchList;
import common.ClosePayableTX;
import common.OpenPayableTX;
import common.PayableCallBack;
import common.PayableCallBack_RegisterCardReader;
import common.RegisterDevice;
import common.Settle;
import common.SettlementSummary;
import common.Signature;
import common.VoidSales;
import models.CloseTxHistoryReq;
import models.Credentials;
import models.ModelSignature;
import models.OpenTxHistoryReq;
import models.SimpleReq;
import models.TxSettlementReq;
import models.TxVoidReq;
import util.SDKUtil;

/**
 * Created by Dell on 12/7/2015.
 */
public class Payable {

    private PayableCallBack payableCallBack;
    private PayableCallBack_RegisterCardReader payableCallBack_registerCardReader;
    static int flag = 1;

    private static long flag_busy = 1427482071469L;

    public static void reset(long value) {
        flag_busy = value;
    }


    public Payable(PayableCallBack payableCallBack) {
        this.payableCallBack = payableCallBack;
    }

    public Payable(PayableCallBack_RegisterCardReader payableCallBack_registerCardReader) {
        this.payableCallBack_registerCardReader = payableCallBack_registerCardReader;
    }

    public Payable(PayableCallBack payableCallBack,PayableCallBack_RegisterCardReader payableCallBack_registerCardReader){
        this.payableCallBack = payableCallBack;
        this.payableCallBack_registerCardReader = payableCallBack_registerCardReader;
    }

    /**
     * Adding Value to Echo Function
     *
     * @param value
     */
    public void callecho(Context context, int value) throws PayableException {

        if (flag_busy != 1427482071469L) {
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        SimpleReq req = new SimpleReq() ;
        req.setValue(value);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(req);



        AddEcho addEcho = new AddEcho(payableCallBack);

        //String strMD5 = null;



        flag_busy = 1;

        try {
            addEcho.proxy_addecho(context, SDKUtil.generateMD5(strmd5), value);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /*public void callTest2222(Activity activity) {
        flag++;
        Log.d("Payable Test Value is", String.valueOf(flag));
    }*/


    public void getOpenTxs(Activity context, int pageId, int pageSize) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }



        if (pageId < 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid page id");
        }

        if (pageSize <= 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid page size");
        }


        OpenPayableTX openPayableTX = new OpenPayableTX(payableCallBack);

        OpenTxHistoryReq openTxHistoryReq = new OpenTxHistoryReq();
        openTxHistoryReq.setPageId(pageId);
        openTxHistoryReq.setPageSize(pageSize);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(openTxHistoryReq);

        flag_busy = 1;

        try {
            //Log.i("jeylogs","before executibg proxy_opentx ") ;
            openPayableTX.proxy_opentx(context, SDKUtil.generateMD5(strmd5), pageId, pageSize);
            //Log.i("jeylogs","after executibg proxy_opentx ") ;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void getCloseTxs(Activity context, int pageId, int pageSize, int master, int visa, Date stDate,
                            Date enDate, String serchTerm) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }

        if (pageId < 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid page id");
        }

        if (pageSize <= 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid page size");
        }

        if (master == 0 && visa == 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Select At least one card type");
        }

        ClosePayableTX closePayableTX = new ClosePayableTX(payableCallBack);

        CloseTxHistoryReq closeTxHistoryReq = new CloseTxHistoryReq();
        closeTxHistoryReq.setPageId(pageId);
        closeTxHistoryReq.setPageSize(pageSize);
//        closeTxHistoryReq.setAmex(amex);
        closeTxHistoryReq.setMaster(master);
        closeTxHistoryReq.setVisa(visa);
        closeTxHistoryReq.setStDate(stDate);
        closeTxHistoryReq.setEnDate(enDate);
        closeTxHistoryReq.setSerchTerm(serchTerm);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(closeTxHistoryReq);

        flag_busy = 1;

        try {
            closePayableTX.proxy_closetx(context, SDKUtil.generateMD5(strmd5), pageId, pageSize, master, visa, 0, stDate, enDate, serchTerm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void authenticate(Activity context ,Credentials c)throws PayableException{
        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(c.getUserName() == null){
            throw new PayableException(PayableException.INVALID_USER_NAME, "Username is null");
        }

        if(c.getUserName().length() >= 100){
            throw new PayableException(PayableException.INVALID_USER_NAME, "Username is too large");
        }

        if(! c.getUserName().matches("[a-zA-Z0-9@]+")){
           // throw new PayableException(PayableException.INVALID_USER_NAME, "Username must contain only alphanumaric characters");
        }

        if(c.getPwd() == null){
            throw new PayableException(PayableException.INVALID_PWD, "Password is null");
        }

        if(c.getPwd().length() >= 100){
            throw new PayableException(PayableException.INVALID_PWD, "Password is too large");
        }

        if(! c.getPwd().matches("[a-zA-Z0-9]+")){
            throw new PayableException(PayableException.INVALID_PWD, "Password must contain only alphanumaric characters");
        }

        if(c.getDeveloperKey() == null){
            throw new PayableException(PayableException.INVALID_DEVELOPER_KEY, "Developer key is null");
        }

        if(c.getDeveloperKey().length() >= 300){
            throw new PayableException(PayableException.INVALID_DEVELOPER_KEY, "Developer key is too large");
        }

        if(! c.getDeveloperKey().matches("[a-zA-Z0-9]+")){
            throw new PayableException(PayableException.INVALID_DEVELOPER_KEY, "Developer key must contain only alphanumaric characters");
        }

        if(c.getDeveloperToken() == null){
            throw new PayableException(PayableException.INVALID_DEVELOPER_TOKEN, "Developer token is null");
        }

        if(c.getDeveloperToken().length() >= 100){
            throw new PayableException(PayableException.INVALID_DEVELOPER_TOKEN, "Developer token is too large");
        }

        if(! c.getDeveloperToken().matches("[a-zA-Z0-9]+")){
            throw new PayableException(PayableException.INVALID_DEVELOPER_TOKEN, "Developer token must contain only alphanumaric characters");
        }

        if(c.getEnvironment() != PayableEnvironment.SAND_BOX ){
            throw new PayableException(PayableException.INVALID_ENVIRONMENT, "Invalid environment");
        }

        flag_busy = 1 ;

        LoginApi api = new LoginApi(payableCallBack) ;

        api.proxy_login(context, c);


    }

    public void sale(Activity context, PayableCardReaderCallBack deviceCallBack ,double amount , String invoice) throws PayableException{

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }


        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }

        if(BluetoothPref.isRegiesterRequired(context)){
            throw new PayableException(PayableException.NO_REGISTERED_CARD_READER, "No registred card reader");
        }

        if (amount < 2.0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid Amount, Must be larger than Rs.2");
        }

        if(amount > UserConfig.getMaxTxValue(context)){
            throw new PayableException(PayableException.MAX_VALUE_EXCEED, "Transaction amuount is larger than the maximum allowed value. maximum is :" + UserConfig.getMaxTxValue(context) );
        }

        if(invoice == null){
            throw new PayableException(PayableException.INVALID_INVOICE, "Invoice is null");
        }

        if(invoice.length() > 30){
            throw new PayableException(PayableException.INVALID_INVOICE, "Invoice must be less than 30 characters");
        }

        if(! invoice.matches("[a-zA-Z0-9]+")){
            throw new PayableException(PayableException.INVALID_INVOICE, "Invoice must contain only alphanumaric characters");
        }

        flag_busy = 1;


        CRReaderInf cr = new CRReaderInf(context,deviceCallBack,payableCallBack,amount, invoice);
        try{
            cr.startCardReader();
        }catch(PayableException e){
            //Log.i("JEYLOGS" , "exception e:" + e.toString()) ;
            flag_busy = 1427482071469L ;
            throw e;
        }

    }

    public void logout(Activity context) throws PayableException{


        if (flag_busy != 1427482071469L) {
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        com.cba.payablesdk.UserConfig.setLogout(context);
    }

   /* public void SwipeSales(Activity context, double amount) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        flag_busy = 1;


        if (amount < 2.0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid Amount, Must be larger than Rs.2");
        }

        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss a");

        String modifiedDate = dateFormat.format(date);

        try {
            date = dateFormat.parse(modifiedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SwipeSales swipeSales = new SwipeSales(payableCallBack);

        TxDSSaleReq txDSSaleReq = new TxDSSaleReq();

        txDSSaleReq.setAmount(amount);
        txDSSaleReq.setInvoice(133);
        txDSSaleReq.setF35("116CA307B431DDB39794CC6862B016C57DB54EBEF687259A");
        txDSSaleReq.setCardHolderName("TEST CARD");
        txDSSaleReq.setClientTime(date);
        txDSSaleReq.setCardReaderId("09113309115100900033");
        txDSSaleReq.setKsn("00000332100300E00058");
        txDSSaleReq.setMaskedPan("541902XXXXXX0853");
        txDSSaleReq.setKeypadL4("0853");
        txDSSaleReq.setTsToken(1456310856129L);
        txDSSaleReq.setRndToken(-3246004495494100872L);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(txDSSaleReq);

        try {
            swipeSales.proxy_swipesales(context, SDKUtil.generateMD5(strmd5), txDSSaleReq);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }*/


    public void VoidSales(Activity context, String SalesId) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }


        if(SalesId == null){
            throw new PayableException(PayableException.INVALID_TRANSACTION_ID, "SalesId is null");
        }

        if(SalesId.length() > 200){
            throw new PayableException(PayableException.INVALID_TRANSACTION_ID, "SalesId must be less than 30 characters");
        }

        if(! SalesId.matches("[a-zA-Z0-9]+")){
            throw new PayableException(PayableException.INVALID_TRANSACTION_ID, "SalesId must contain only alphanumaric characters");
        }


        Date date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss a");

        String modifiedDate = dateFormat.format(date);

        try {
            date = dateFormat.parse(modifiedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TelephonyManager p = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = p.getDeviceId() ;
        //String simId = p.getSimSerialNumber() ;



        VoidSales voidSales = new VoidSales(payableCallBack);

        TxVoidReq txVoidReq = new TxVoidReq();
        txVoidReq.setClientTime(date);
        txVoidReq.setDeviceID(deviceId);
        txVoidReq.setLatitude(0.0);
        txVoidReq.setLongitude(0.0);
        txVoidReq.setRepeatFlag(0);
        //txVoidReq.setRndToken(-6254668796927894356L);
       // txVoidReq.setTsToken(1456309826952L);
        txVoidReq.setTsToken(System.currentTimeMillis());
        txVoidReq.setRndToken(SDKUtil.generateLongToken());
        txVoidReq.setTxId(SalesId);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(txVoidReq);

        flag_busy = 1;

        try {
            voidSales.proxy_voidsales(context, SDKUtil.generateMD5(strmd5), txVoidReq);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void AddSignature(Activity context, long SalesId, String sig) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }

        flag_busy = 1;

        Signature signature = new Signature(payableCallBack);

        ModelSignature modelSignature = new ModelSignature();
        modelSignature.setSaleId(SalesId);
        modelSignature.setSignature(sig);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(modelSignature);

        try {
            signature.proxy_signature(context, SDKUtil.generateMD5(strmd5), modelSignature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void batchsummary(Activity context, int value) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }



        SimpleReq simpleReq = new SimpleReq();
        simpleReq.setValue(value);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(simpleReq);

        SettlementSummary settlementSummary = new SettlementSummary(payableCallBack);

        flag_busy = 1;

        try {
            settlementSummary.proxy_settlementsummary(context, SDKUtil.generateMD5(strmd5), simpleReq);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void settleSales(Activity context, int batchNumber) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }



        TxSettlementReq txSettlementReq = new TxSettlementReq();
        txSettlementReq.setBatchNumber(batchNumber);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(txSettlementReq);

        flag_busy = 1;

        Settle settle = new Settle(payableCallBack);

        try {
            settle.proxy_settle(context, SDKUtil.generateMD5(strmd5), txSettlementReq);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }


    public void batchList(Activity context, int pageId, int pageSize) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }



        if (pageId < 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid page id");
        }

        if (pageSize <= 0) {
            throw new PayableException(PayableException.INVALID_PARAMETER, "Invalid page size");
        }

        flag_busy = 1;


        BatchList batchList = new BatchList(payableCallBack);

        OpenTxHistoryReq openTxHistoryReq = new OpenTxHistoryReq();
        openTxHistoryReq.setPageId(pageId);
        openTxHistoryReq.setPageSize(pageSize);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String strmd5 = gson.toJson(openTxHistoryReq);

        try {
            batchList.proxy_batchlist(context, SDKUtil.generateMD5(strmd5), openTxHistoryReq);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

  /*  public void registerDevice(Context context) throws PayableException {

        if (flag_busy != 1427482071469L) {
            // throw exception
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        if(! UserConfig.isLogedIn(context)){
            throw new PayableException(PayableException.NOT_LOGGED_IN, "Not logged in");
        }


        flag_busy = 1;

        RegisterDevice registerDevice = new RegisterDevice(payableCallBack_registerCardReader);

        registerDevice.scan_device(context, "MPOS5121400279");

    }*/

    public void registerReader(Context context) throws PayableException{
        if (flag_busy != 1427482071469L) {
            throw new PayableException(PayableException.BUSY_ERROR, "Already existing operation is in progress");
        }

        flag_busy = 1;

        RegisterReader registerReader = new RegisterReader(context,payableCallBack_registerCardReader) ;
        registerReader.scan_device();

    }
}
