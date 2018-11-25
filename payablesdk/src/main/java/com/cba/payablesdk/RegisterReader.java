package com.cba.payablesdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import common.PayableCallBack_RegisterCardReader;

/**
 * Created by Dell on 1/18/2017.
 */



public class RegisterReader {

    protected static final String TAG = "JEYLOGS";

    private PayableCallBack_RegisterCardReader payableCallBack_cardreader;
    private BluetoothAdapter mBtAdapter;
    private Context context ;

    private boolean boolDeviceFound = false, boolScanFinished = false;

    private String reader_name = "";

    public RegisterReader(Context c ,PayableCallBack_RegisterCardReader cb){
        payableCallBack_cardreader = cb ;
        context = c ;

    }


    public void scan_device() throws PayableException{

        //Log.i(TAG , "inside scan_device.") ;

        reader_name = BluetoothPref.generateValidDeviceName(context) ;

        if(reader_name == null){
            throw new PayableException(PayableException.NO_READER_ASSIGNED, "There are no reader assigned with this profile.");
        }

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null){
            throw new PayableException(PayableException.BLUETOOTH_NOT_SUPPORTED, "No bluetooth supported.");
        }

        if (! mBtAdapter.isEnabled()){
            throw new PayableException(PayableException.BLUETOOTH_OFF, "Bluetooth is turnedoff.");
        }

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(mReceiver, filter);

        mBtAdapter.startDiscovery();


    }


    private void _releaseResource(){
        context.unregisterReceiver(mReceiver) ;
        Payable.reset(1427482071469L);
    }



    public  final BroadcastReceiver mReceiver = new BroadcastReceiver(){
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    // showToast("Enabled");
                    boolDeviceFound = false ;
                    payableCallBack_cardreader.onBluetoothTurnedOn("Scanning Devices, Please wait...");
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                boolScanFinished = true;

                if (!boolDeviceFound) {

                    payableCallBack_cardreader.onFailureRegister("Reader not found.Please turn on your reader");

                }else{
                    payableCallBack_cardreader.onSuccessRegister(reader_name);
                }

                _releaseResource() ;

            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String devicename_main = device.getName();

                if (reader_name.equals(devicename_main)){

                    BluetoothPref.setDeviceReaderStatus(context,devicename_main,device.getAddress());

                    boolDeviceFound = true;
                }



            }

        }

    };
}
