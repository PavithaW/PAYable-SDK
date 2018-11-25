package common;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cba.payablesdk.Payable;

/**
 * Created by Dell on 6/29/2016.
 */
public class RegisterDevice {

    private PayableCallBack_RegisterCardReader payableCallBack_cardreader;
    private boolean boolDeviceFound = false, boolScanFinished = false;
    private BluetoothAdapter mBluetoothAdapter;
     ProgressDialog progressDialog;

    private String reader_name = "";


    public RegisterDevice() {

    }

    public RegisterDevice(PayableCallBack_RegisterCardReader payableCallBack_cardreader) {
        this.payableCallBack_cardreader = payableCallBack_cardreader;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    public void scan_device(Context context, String value) {

        Payable.reset(1427482071469L);

        reader_name = value;

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(mReceiver, filter);

        blutoothFunctions(context);

    }


    public  final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    // showToast("Enabled");


                    payableCallBack_cardreader.onBluetoothTurnedOn("Scanning Devices, Please wait...");
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                // mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // mProgressDlg.dismiss();
                progressDialog.dismiss();
                boolScanFinished = true;


                if (!boolDeviceFound) {

               //     payableCallBack_cardreader.onFailureRegister(new PayableException(200001,"Reader not found.Please turn on your reader"));
                    // "Reader not found.Please turn on your reader"
                    payableCallBack_cardreader.onFailureRegister("Reader not found.Please turn on your reader");

                }

          //      context.unregisterReceiver(mReceiver);
                //   pgScan.setVisibility(View.GONE);

                // startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // The bluetooth device which is stored when login.

                String devicename_main = device.getName();

                if (reader_name.equals(devicename_main)) {

                    payableCallBack_cardreader.onSuccessRegister("Reader is registered with app successfully");
                    //  BluetoothPref.setDeviceReaderStatus(getApplicationContext(), devicename_main, device.getAddress());
                    boolDeviceFound = true;
                    //  tv_status.setText("Reader is registered with app successfully");

                   /* Intent in = new Intent(getApplicationContext(), Home.class);
                    startActivity(in);
                    finish();*/

                }

            }
        }
    };



    private void blutoothFunctions(Context context) {

        // If bluetooth is enabled then start the discovery
       /* tv_status.setText("Scanning Devices, Please wait...");
        tv_status.setTextColor(Color.parseColor("#4786C6"));*/

        mBluetoothAdapter.startDiscovery();

        progressDialog =new ProgressDialog(context);
        progressDialog.setMessage("Scanning Devices.Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

       /* pgScan.setVisibility(View.VISIBLE);

        btnScan.setBackgroundColor(Color.parseColor("#B2AEAD"));
        btnScan.setEnabled(false);
*/
    }


}
