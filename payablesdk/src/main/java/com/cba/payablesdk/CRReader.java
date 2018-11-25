package com.cba.payablesdk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.dspread.xpos.QPOSService;
import com.dspread.xpos.QPOSService.CommunicationMode;
import com.dspread.xpos.QPOSService.Display;
import com.dspread.xpos.QPOSService.DoTradeResult;
import com.dspread.xpos.QPOSService.EmvOption;
import com.dspread.xpos.QPOSService.Error;
import com.dspread.xpos.QPOSService.QPOSServiceListener;
import com.dspread.xpos.QPOSService.TransactionResult;
import com.dspread.xpos.QPOSService.TransactionType;
import com.dspread.xpos.QPOSService.UpdateInformationResult;

import common.PayableCallBack;

/*import com.mpos.util.BluetoothPref;
import com.setting.env.UserConfig;
import com.setting.env.Environment;
import com.setting.env.Environment.Advt;
import com.setting.env.Environment.Env;*/


//public abstract class CRReader extends Activity {
public abstract class CRReader {

    protected static final String TAG = "JEYLOGS";

    protected static final int QPOS_CONNECTED = 1;
    protected static final int QPOS_DISCONNECTED = 2;
    protected static final int WAITING_FOR_CARD = 3;
    protected static final int CARD_NONE = 4;
    protected static final int CARD_NOT_ICC = 5;
    protected static final int CARD_BAD_SWIPE = 6;
    protected static final int CARD_NO_RESPONSE = 7;
    protected static final int QPOS_ERROR = 8;
    protected static final int QPOS_NOT_DECTECTED = 9;
    protected static final int CARD_SWIPE_SUCCESS = 10;
    protected static final int CARD_ICC = 11;
    protected static final int CARD_ICC_ONLINE = 12;
    protected static final int SELECT_APP_FAIL = 13;

    protected static final int BT_CONNECTING = 14;

    protected static final int BT_DIS_CONNECTED = 15;

    private static final int REQUEST_ENABLE_BT = 100;

    private BluetoothAdapter mBtAdapter;
    private String blueTootchAddress = "";

    private QPOSService pos;
    private MyPosListener listener;

    private SimpleDateFormat sdf;

    private POS_TYPE posType = POS_TYPE.BLUETOOTH;

    protected String deviceId;


    private Dialog appDialog;
    private ListView appListView;

    private Context context;
    private PayableCardReaderCallBack deviceCallBack ;
    private PayableCallBack payableCallBack;


    protected boolean isFallback = false;
    private boolean isICCErrLog = false;
    private boolean isICCOnline = false;
    private boolean isWaitingForUserAction = false;
    private boolean isUpdatingIssuerScript = false;

    private boolean isReacerBlConnected = false;

    private String strIccErrLog = null;
    private String strIccBatchLog = null;
    protected String strF39ErrMsg = null;


    protected boolean isICCOnlineTxSuccess = false;
    protected boolean isICCOnlineTxFail = false;
    protected boolean isBroadcastReceiverUnRegistered = false;

    protected static enum POS_TYPE {
        BLUETOOTH, AUDIO, UART
    }

    abstract protected POS_TYPE getPosType();

    abstract protected void statusCallBack(int status, String message);

    abstract protected String getAmount();

    abstract protected void swipeCallBack(String ksn, String cardHolder,
                                          String track2, String maskedPan);

    abstract protected void emvCallBack(String data);

    abstract protected void iccErrCallBack(String data);

    abstract protected void iccIssuerScriptSuccessCallBack(String data);

    abstract protected void iccIssuerScriptFailCallBack(String data);


    abstract protected void navigateToSigScreen();


    public void dismissAppDialog() {
        if (appDialog != null) {
            appDialog.dismiss();
            appDialog = null;
        }
    }

    public CRReader(Context c , PayableCardReaderCallBack readeCb , PayableCallBack cb) {
        context = c;
        stateChangeReceiver = new BTStatusReceiver();
        deviceCallBack = readeCb ;
        payableCallBack = cb ;
    }

    protected void setupCardReader() throws PayableException {

        //Log.i(TAG, "inside setupCardReader");
        sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        deviceId = null;

        posType = getPosType();


        //Log.i(TAG, "DS base - point : 2000") ;

        _resetTxFlags();

        registerBT_BC_Receiver();

        if (posType == POS_TYPE.BLUETOOTH) {

            mBtAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBtAdapter != null) {
                // throw Payable Exception.
                if (mBtAdapter.isEnabled()) {

                    // tmp code to get device address

                   /* Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

                    if (pairedDevices == null || pairedDevices.size() == 0){
                        Log.e(TAG, "There are no paired device");
                    }

                    if (pairedDevices.size() > 0){
                        for (BluetoothDevice device : pairedDevices){
                            String strName = device.getName();
                            String strAddress = device.getAddress();

                            Log.i(TAG, "Device name :" + strName + "   Address :"
                                    + strAddress);
                        }
                    }*/

                    // end of tmp code.


                        _connectBTDevice();
                }
                else {
                    _releaseResource();
                    throw new PayableException(PayableException.BLUETOOTH_OFF, "Bluetooth is turnedoff.");
                }
            }

        }


    }


    private void registerBT_BC_Receiver() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        context.registerReceiver(stateChangeReceiver, filter);

        isBroadcastReceiverUnRegistered = false;
    }

    protected void processSignature() {
        /*if (!isBroadcastReceiverUnRegistered) {
            context.unregisterReceiver(stateChangeReceiver);
            isBroadcastReceiverUnRegistered = true;
        }*/

        _releaseResource();

        navigateToSigScreen();
    }

    private void _onTransactionCancel() {
       // showToastMessage("Transaction canceled.");
        setException(PayableException.TX_CANCELD_MANUAL,"Transaction is canceled mannualy.");
        //finish() ;
        //_releaseResource();
    }

    protected void _onTransactionDeclined() {

        String errMsg = "" ;
        if (strF39ErrMsg != null) {
           // showToastMessage(strF39ErrMsg);
            errMsg = strF39ErrMsg ;
        } else {
            //showToastMessage("Transaction Declined.");
            errMsg = "Transaction Declined." ;
        }
        //showToastMessage("Transaction Declined.") ;
        //finish() ;
       // _releaseResource();
        setException(PayableException.Transaction_Declined,errMsg);
    }

    protected void _onTransactionFallbackErr() {
        showToastMessage("Not accepted.");
        _releaseResource();
        //finish() ;
    }

    private void _onCardBlocked() {
        showToastMessage("Card Blocked.");
        _releaseResource();
        //finish() ;
    }

    protected void writeIssuerScriptOnSuccess(String script) {

        isUpdatingIssuerScript = true;

        // 00 for approval
        // 05 for a decline
        // 02 for referral

        if (!isReacerBlConnected) {
            processSignature();
            return;
        }


        if (pos != null) {
            if (script.contains("8A023030")) {
                pos.sendOnlineProcessResult(script);
            } else {
                pos.sendOnlineProcessResult("8A023030" + script);
            }

        }

    }

    protected void writeIssuerScriptOnError(String script) {

        isUpdatingIssuerScript = true;

        if (!isReacerBlConnected) {
            _onTransactionDeclined();
            return;
        }

        if (pos != null) {
            if (script.contains("8A023035")) {
                pos.sendOnlineProcessResult(script);
            } else {
                pos.sendOnlineProcessResult("8A023035" + script);
            }

        }

    }

    protected void turnoffIcc() {
        if (pos != null) {
            pos.powerOffIcc();
        }

        _releaseResource();
        //pos.lcdShowCustomDisplay(lcdModeAlign, lcdFont);

    }


    private void _connectBTDevice() throws PayableException {
        statusCallBack(BT_CONNECTING, "Please wait.Trying to connect with bluetooth reader.");

		/*deviceId = UserConfig.getReaderId(getApplicationContext()) ;
		blueTootchAddress = BluetoothPref.getDeviceAddress(getApplicationContext()) ;*/

        //context
        deviceId = com.cba.payablesdk.UserConfig.getReaderId(context);
        blueTootchAddress = BluetoothPref.getDeviceAddress(context);

        if (blueTootchAddress == null) {
           // showToastMessage("No registered card reader.");
            //finish() ;
            _releaseResource();
           // return;
            throw new PayableException(PayableException.NO_REGISTERED_CARD_READER, "No registered card reader.");
        }


        _open(CommunicationMode.BLUETOOTH_2Mode);
        posType = POS_TYPE.BLUETOOTH;

        if (pos != null) {
            sendMsg(1001);
        }

    }


    private void sendMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        mHandler.sendMessage(msg);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    sendMsg(1002);
                    break;
                case 1002:
                    // pos.connectBluetoothDevice(20, blueTootchAddress);
                    pos.connectBluetoothDevice(true, 25, blueTootchAddress);
                    // doTradeButton.setEnabled(true);
                    break;
               /* case 1004:
                    showDialog("Error", "Coudn't  find a paired reader.Please pair the reader first.", 222);
                    break;*/

                /*case 1005:
                    pos.openAudio();*/
                default:
                    break;
            }
        }
    };

    // this portion need to be eleminated for sdk.
   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {

            if (resultCode == -1) {
                _connectBTDevice();
            }

            return;
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }*/

    public void onBackPressed() {

        if (isUpdatingIssuerScript) {
            return;
        }

        if (isWaitingForUserAction) {
            turnoffIcc();
        }


        //finish() ;
        _releaseResource();
    }

	/*public void onDestroy() {
		
		if(! isBroadcastReceiverUnRegistered){
			unregisterReceiver(stateChangeReceiver);
			isBroadcastReceiverUnRegistered = true ;
		}
		
		super.onDestroy();
		_close();
		
	}*/


    // using this method to release resources instead of on destroy
    private void _releaseResource() {

        if (!isBroadcastReceiverUnRegistered) {
            //unregisterReceiver(stateChangeReceiver);
            if (context != null) {
                context.unregisterReceiver(stateChangeReceiver);
            }

            isBroadcastReceiverUnRegistered = true;
        }

        _close();


    }

    private void _open(CommunicationMode mode) {
        listener = new MyPosListener();
        pos = QPOSService.getInstance(mode);
        if (pos == null) {
            //Log.i(TAG, "CommunicationMode unknow.");
            return;
        }

        //pos.setConext(getApplicationContext());
        pos.setConext(context);
        Handler handler = new Handler(Looper.myLooper());
        pos.initListener(handler, listener);

    }

    private void _close() {
        if (pos == null) {
            return;
        }
        if (posType == POS_TYPE.AUDIO) {
            pos.closeAudio();
        } else if (posType == POS_TYPE.BLUETOOTH) {
            pos.disconnectBT();
        } else if (posType == POS_TYPE.UART) {
            pos.closeUart();
        }


    }

    public void onMSGDlgBtnClick(int callerId) {
        if (callerId == 222) {
            //finish() ;
            _releaseResource();
            return;
        }

        if (callerId == 111) {
            //finish() ;
            _releaseResource();
            return;
        }

        if (callerId == 333) {
            //finish() ;
            _releaseResource();
            return;
        }

        if (callerId == 444) {
            statusCallBack(SELECT_APP_FAIL, "Please swipe the card.");
            pos.doCheckCard(30);
            return;
        }

        if (callerId == 555) {
            //finish() ;
            _releaseResource();
            return;
        }

        //super.onMSGDlgBtnClick(callerId); removed because no super class here


    }

    private void _resetTxFlags() {
        isICCErrLog = false;
        isICCOnline = false;
        strIccErrLog = null;
        strIccBatchLog = null;
        isICCOnlineTxSuccess = false;
        isICCOnlineTxFail = false;
        strF39ErrMsg = null;
    }

    private final BTStatusReceiver stateChangeReceiver;


    class BTStatusReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            String action = intent.getAction();

            //Log.i(TAG, "inside broad cast receiver");
            //Log.i(TAG, "Action = " + action);

            //boolean isTurnedOff = false ;

            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {

                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //Log.i(TAG, "BT name : " + device.getName()) ;

                //String deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());
                String deviceName = BluetoothPref.generateValidDeviceName(c);

                //Log.i(TAG, "Binded device name : " + deviceName) ;


                if (!deviceName.equals(device.getName())) {
                    return;
                }

                if (pos != null) {
                    pos.setPosExistFlag(true);
                    //Log.i(TAG, "Is device present :"+ pos.isQposPresent()) ;
                }

            }

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {

                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //String deviceName = BluetoothPref.generateValidDeviceName(getApplicationContext());

                String deviceName = BluetoothPref.generateValidDeviceName(context);

                //	Log.i(TAG, "Binded device name : " + deviceName) ;
                //Log.i(TAG, "BT name : " + device.getName()) ;

                if (!deviceName.equals(device.getName())) {
                    return;
                }

                isReacerBlConnected = false;

                if (pos != null) {
                    pos.setPosExistFlag(false);
                    //Log.i(TAG, "Is device present :"+ pos.isQposPresent()) ;
                    pos.disconnectBT();

                    ///statusCallBack(BT_DIS_CONNECTED, "Disconnected with cardreader.");
                }

                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }

                //isICCOnlineTxFail

                if (isICCOnline && isICCOnlineTxFail) {
                    _onTransactionDeclined();
                    return;
                }

                //_onTransactionDeclined() ;
            }
        }
    }

    ;


    class MyPosListener implements QPOSServiceListener {

        @Override
        public void onBluetoothBondFailed() {
            //Log.i(TAG, "inside onBluetoothBondFailed.");
        }

        @Override
        public void onBluetoothBondTimeout() {
            //Log.i(TAG, "inside onBluetoothBondTimeout.");

        }

        @Override
        public void onBluetoothBonded() {
            //Log.i(TAG, "inside onBluetoothBonded.");

        }

        @Override
        public void onBluetoothBonding() {
            //Log.i(TAG, "inside onBluetoothBonded.");

        }

        @Override
        public void onCbcMacResult(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onConfirmAmountResult(boolean res) {
            //Log.i(TAG, "inside onConfirmAmountResult..");
            //Log.i(TAG, "Result = " + res);

        }

        @Override
        public void onDoTradeResult(DoTradeResult result,
                                    Hashtable<String, String> decodeData) {

            //Log.i(TAG, "inside onDoTradeResult." ) ;

            isWaitingForUserAction = false;
            isUpdatingIssuerScript = false;

            	//Log.i(TAG, "DecodeData : " + decodeData) ;

            if (result == DoTradeResult.NONE) {
               // statusCallBack(CARD_NONE, "No valid card detected.");
                setException(PayableException.CARD_NONE,"No valid card detected.");
                return;
            }

            if (result == DoTradeResult.NOT_ICC) {
                //statusCallBack(CARD_NOT_ICC, "No valid card detected.");
                setException(PayableException.CARD_NOT_ICC,"No valid card detected.");
                return;
            }

            if (result == DoTradeResult.BAD_SWIPE) {
                isWaitingForUserAction = true;
                deviceCallBack.cardReaderStatus(PayableCardReaderCallBack.CARD_BAD_SWIPE, "Bad swipe.Please try again.");
                //statusCallBack(CARD_BAD_SWIPE, "Bad swipe.Please try again.");
                return;
            }

            if (result == DoTradeResult.ICC) {
                //Log.i(TAG, "inside result == DoTradeResult.ICC ");

                _resetTxFlags();

                if (isFallback) {
                    showDialog("Error", "Please remove the card and swipe.", 444);
                    //pos.p
                    return;
                }

                statusCallBack(CARD_ICC, "EMV card inserted.");
                pos.doEmvApp(EmvOption.START);
                return;
            }

            if (result == DoTradeResult.MCR) {

                statusCallBack(CARD_SWIPE_SUCCESS, "Successfull Swipe.");

                String formatID = decodeData.get("formatID");

                String ksn = null;
                String cardHolder = null;
                String track2 = null;
                String maskedPan = null;

                if (formatID.equals("31") || formatID.equals("40")
                        || formatID.equals("37") || formatID.equals("17")
                        || formatID.equals("11") || formatID.equals("10")) {

                    swipeCallBack(null, null, null, null);

                } else if (formatID.equals("FF")) {
                    swipeCallBack(null, null, null, null);
                } else {
                    cardHolder = decodeData.get("cardholderName");
                    maskedPan = decodeData.get("maskedPAN");
                    track2 = decodeData.get("encTrack2");
                    ksn = decodeData.get("trackksn");

                    swipeCallBack(ksn, cardHolder, track2, maskedPan);
                }

                _releaseResource();
                return;
            }

            if (result == DoTradeResult.NO_RESPONSE) {
              //  statusCallBack(CARD_NO_RESPONSE, "No response from card.");
                setException(PayableException.CARD_NO_RESPONSE,"No response from card.");
                return;
            }

        }

        @Override
        public void onEmvICCExceptionData(String res) {
            isICCErrLog = true;
            strIccErrLog = res;
            //Log.i(TAG, "Inside onEmvICCExceptionData");
            //Log.i(TAG, "Res:" + res);

        }

        @Override
        public void onError(Error err) {

            //Log.i(TAG, "inside onError") ;
            //Log.i(TAG,"Err:" +err.toString() ) ;


            storeErrorLog("inside cr onError. err :  " + err.toString());

            if (err == Error.CMD_TIMEOUT) {

                setException(PayableException.CARD_TIME_OUT,"No card insertion/swipe");
                return;
            }

            if (err == Error.TIMEOUT) {
                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }
            }

            if (err == Error.DEVICE_BUSY) {
                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }
            }

           // statusCallBack(QPOS_ERROR, "Error with card reader.");
            setException(PayableException.READER_ERROR,"Error with card reader.");


        }

        @Override
        public void onGetCardNoResult(String arg0) {
            //Log.i(TAG, "inside onGetCardNoResult.");

        }

        @Override
        public void onGetInputAmountResult(boolean arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetPosComm(int arg0, String arg1, String arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLcdShowCustomDisplay(boolean arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPinKey_TDES_Result(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onQposIdResult(Hashtable<String, String> posIdTable) {
            //Log.i(TAG, "inside onQposIdResult." );
            deviceId = posIdTable.get("posId") == null ? "" : posIdTable
                    .get("posId");

            //Log.i(TAG, "Device id: " + deviceId);

            //pos.doCheckCard(30);

            //String strId = UserConfig.getReaderId(getApplicationContext()) ;
            String strId = com.cba.payablesdk.UserConfig.getReaderId(context);

            //Log.i(TAG, "Device id in profile: " + strId);


            if (strId.equalsIgnoreCase(deviceId)) {
                pos.setAmountIcon("Rs");
                pos.doCheckCard(30);
            } else {
                showDialog("Error", "Reader id  doesn't match.Please connect with valid  reader.", 555);
            }

        }

        @Override
        public void onQposInfoResult(Hashtable<String, String> arg0) {
            //Log.i(TAG , "inside onQposInfoResult") ;
            //Log.i(TAG , "res : " + arg0.toString()) ;

        }

        @Override
        public void onReadBusinessCardResult(boolean arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRequestBatchData(String tlv) {
            //Log.i(TAG, "inside onRequestBatchData") ;
            //Log.i(TAG, " ************* ") ;
            //Log.i(TAG, tlv) ;
            strIccBatchLog = tlv;

        }

        @Override
        public void onRequestCalculateMac(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRequestDisplay(Display arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRequestFinalConfirm() {
            //Log.i(TAG, "inside onRequestFinalConfirm.");
            pos.finalConfirm(true);

        }

        @Override
        public void onRequestIsServerConnected() {
            pos.isServerConnected(true);

        }

        @Override
        public void onRequestNoQposDetected() {
            //statusCallBack(QPOS_NOT_DECTECTED, "No cardreader detected.");
            //showDialog("Error", "No cardreader detected.", 333);

            setException(PayableException.READER_NOT_DETECTED,"No cardreader detected.");


        }

        @Override
        public void onRequestOnlineProcess(String tlv) {


            //pos.powerOffIcc() ;

            _resetTxFlags();
            isICCOnline = true;

            statusCallBack(CARD_ICC_ONLINE, "Processing online.");
            emvCallBack(tlv);

        }

        @Override
        public void onRequestQposConnected() {
            //Log.i(TAG, "inside onRequestQposConnected 2.");
			/*statusCallBack(QPOS_CONNECTED,
					"Connected with cardreader. Verifying reader .."); */

            statusCallBack(QPOS_CONNECTED,
                    "Connected with cardreader. initiating ...");

            isReacerBlConnected = true;


           // pos.getQposId();
           // pos.getQposInfo();

           // pos.setAmountIcon("Rs");
            pos.doCheckCard(30);
            //Log.i(TAG, "end of onRequestQposConnected 2.");
        }

        @Override
        public void onRequestQposDisconnected() {
            //Log.i(TAG, "inside onRequestQposDisconnected.");
            //statusCallBack(QPOS_DISCONNECTED, "Disconnected with cardreader.");
        }

        @Override
        public void onRequestSelectEmvApp(ArrayList<String> appList) {
            //Log.i(TAG, "inside onRequestSelectEmvApp");


            pos.selectEmvApp(0);

        }

        @Override
        public void onRequestSetAmount() {
            //Log.i(TAG, "inside onRequestSetAmount") ;
            TransactionType transactionType = TransactionType.GOODS;
            pos.setAmount(getAmount(), "0", "144", transactionType);
            //pos.setCardTradeMode(CardTradeMode.UNALLOWED_LOW_TRADE);
        }

        @Override
        public void onRequestSetPin() {
            //Log.i(TAG, "inside onRequestSetPin");
            //pos.sendPin("1234");
            // pos.sendPin("1111");
            pos.cancelPin();

        }

        @Override
        public void onRequestSignatureResult(byte[] arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onRequestTime() {
            // String terminalTime =
            // sdf.format(Calendar.getInstance().getTime());
            pos.sendTime(sdf.format(Calendar.getInstance().getTime()));

        }

        @Override
        public void onRequestTransactionLog(String strLog) {
            //Log.i(TAG, "inside onRequestTransactionLog");
            //Log.i(TAG, "Log :: " + strLog) ;

        }

        @Override
        public void onRequestTransactionResult(
                TransactionResult transactionResult) {
            	//Log.i(TAG, "inside onRequestTransactionResult");
            //Log.i(TAG, "Result: " + transactionResult.toString());

            storeMessageLog("inside final chip result :  " + transactionResult.toString());

            isUpdatingIssuerScript = false;

            // _onTransactionCancel

            if (transactionResult == TransactionResult.CANCEL) {
                _onTransactionCancel();
                return;
            }

            if (transactionResult == TransactionResult.APPROVED) {
                // this if condition is not necessary in sdk for now caz no need to worry about advt.
				
				/*if(Environment.getADVTStatus() == Advt.ON){
					if(strIccBatchLog != null){
						iccIssuerScriptSuccessCallBack(strIccBatchLog);
						return ;
					}
				}*/

                processSignature();
                return;
            }

            if (transactionResult == TransactionResult.DECLINED) {
                _onTransactionDeclined();
            }

           /* if (transactionResult == TransactionResult.DECLINED) {
                if (isICCErrLog) {
                    Log.i(TAG, "point 1 ");
                    if (strIccErrLog != null) {
                        // removing advt fuction for sdk for now
						*//*if(isICCOnline && Environment.getADVTStatus() == Advt.OFF){
							_onTransactionDeclined() ;
							return ;
						}*//*
                       // iccErrCallBack(strIccErrLog);
                        _onTransactionDeclined() ;
                        return;
                    }
                }

                _onTransactionDeclined() ;

                if (strIccBatchLog != null) {
                    iccIssuerScriptFailCallBack(strIccBatchLog);
                    return;
                }

                _onTransactionDeclined();
                return;
            }*/

            if (transactionResult == TransactionResult.TERMINATED) {
                if (isICCOnline && isICCOnlineTxSuccess) {
                    processSignature();
                    return;
                }
                _onTransactionDeclined();
                return;
            }

            //_onCardBlocked

            if (transactionResult == TransactionResult.CARD_BLOCKED_OR_NO_EMV_APPS) {
                _onCardBlocked();
                return;
            }

            if (transactionResult == TransactionResult.SELECT_APP_FAIL) {
                statusCallBack(SELECT_APP_FAIL, "Chip is not responding correctly.");
                showDialog("Error", "Please remove the card and click ok.", 444);
                isFallback = true;
                //pos.powerOffIcc() ;
                //pos.doCheckCard(30);
                return;
            }

            if (transactionResult == TransactionResult.FALLBACK) {
                _onTransactionFallbackErr();
                return;
            }


        }

        @Override
        public void onRequestUpdateWorkKeyResult(UpdateInformationResult arg0) {
            // TODO Auto-generated method stub
            //pos.udpateWorkKey(workKey, workKeyCheck);
            //pos.udpateWorkKey(pik, pikCheck, trk, trkCheck, mak, makCheck)

        }

        @Override
        public void onRequestWaitingUser() {
            isWaitingForUserAction = true;
            if (!isFallback) {
                //statusCallBack(WAITING_FOR_CARD, "Waiting to swipe / insert Credit /Debit card.");
                deviceCallBack.cardReaderStatus(PayableCardReaderCallBack.WAITING_FOR_CARD, "Waiting to swipe / insert Credit /Debit card.");
            } else {
                statusCallBack(SELECT_APP_FAIL, "Please swipe the card.");
            }


        }

        @Override
        public void onReturnApduResult(boolean arg0, String arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnBatchSendAPDUResult(
                LinkedHashMap<Integer, String> arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnCustomConfigResult(boolean arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnDownloadRsaPublicKey(HashMap<String, String> arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnGetPinResult(Hashtable<String, String> res) {
            //Log.i(TAG, "inside onReturnGetPinResult");
            //Log.i(TAG, "get pin result :" + res);
            // TODO Auto-generated method stub


        }

        @Override
        public void onReturnNFCApduResult(boolean arg0, String arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnPowerOffIccResult(boolean res) {
            //Log.i(TAG, "inside onReturnPowerOffIccResult.");
            //Log.i(TAG, "Res = " + res);
            //pos.doCheckCard(30);

        }

        @Override
        public void onReturnPowerOffNFCResult(boolean arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnPowerOnIccResult(boolean arg0, String arg1,
                                             String arg2, int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnPowerOnNFCResult(boolean arg0, String arg1,
                                             String arg2, int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnReversalData(String arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onReturnSetMasterKeyResult(boolean arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturnSetSleepTimeResult(boolean arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onReturniccCashBack(Hashtable<String, String> arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSetParamsResult(boolean arg0,
                                      Hashtable<String, Object> arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdateMasterKeyResult(boolean arg0,
                                            Hashtable<String, String> arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdatePosFirmwareResult(UpdateInformationResult arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onWriteBusinessCardResult(boolean arg0) {
            // TODO Auto-generated method stub

        }

    }
	
	
	
	
	
/* Callback status messages back to sdk */


    private void storeMessageLog(String str) {
        // need to see what to do later
    }

    private void storeErrorLog(String str) {
        // need to see what to do later
    }

    private void showToastMessage(String str) {
        // need to see what to do
    }

    private void showDialog(String title, String msg, int calledId) {
        // need  to see whaat to do
    }

    private void setException(int code , String message){
        PayableException ex = new PayableException(code,message);
        _releaseResource();
        payableCallBack.onFailureSales(ex);
        Payable.reset(1427482071469L);


    }
    //private void throwException


    // removed this functionality in sdk : onRequestSelectEmvApp; but need to implement it asap


    // requst client to enable bluetooth


}
