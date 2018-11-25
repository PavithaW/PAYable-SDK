package com.cba.androidpayablesdk;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;

import common.PayableCallBack_RegisterCardReader;

public class RegisterActivity extends AppCompatActivity implements PayableCallBack_RegisterCardReader {

    private BluetoothAdapter mBluetoothAdapter;
    Payable payable;

    TextView txtStatus;

    protected static final String TAG = "JEYLOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtStatus=(TextView)findViewById(R.id.txtStatus);

        payable = new Payable(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            if (mBluetoothAdapter.isEnabled()) {

                try {
                   // payable.registerDevice(RegisterActivity.this);
                    payable.registerReader(this);
                } catch (PayableException e) {
                    e.printStackTrace();
                }

            } else {
                showDisabled();
            }
        }


    }


    private void showDisabled() {
        txtStatus.setText("Bluetooth is turned off.Please turn it on");
        txtStatus.setTextColor(Color.RED);

    }

    private void showUnsupported() {
        txtStatus.setText("Bluetooth is unsupported by this device");
        txtStatus.setTextColor(Color.RED);
    }

    @Override
    public void onSuccessRegister(String res) {
        txtStatus.setText(res);
    }

    @Override
    public void onFailureRegister(String res) {
        txtStatus.setText(res);

    }


    @Override
    public void onBluetoothTurnedOn(String res) {
        txtStatus.setText(res);
    }

    @Override
    public void onBluetoothTurnedOff(String res) {
        txtStatus.setText(res);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
