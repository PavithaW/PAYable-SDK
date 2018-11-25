package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import common.PayableCallBack;
import dialogs.Result_Dialog;
import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class VoidActivity extends AppCompatActivity implements PayableCallBack {

    EditText edtVoidNo;
    Button btnVoid;
    TextView txtStatus;

    Payable payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void);

        InitViews();


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnVoid: {
                    try {

                        txtStatus.setText("Sent Request, Awaiting Reply.....");
                        //payable.VoidSales(VoidActivity.this,edtVoidNo.getText().toString());
                        payable.VoidSales(VoidActivity.this,"4dff4ea340f0a823f15d3f4f01ab62eae0e5da579ccb851f8db9dfe84c58b2b37b89903a740e1ee172da793a6e79d560e5f7f9bd058a12a280433ed6fa46510a");
                    } catch (PayableException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    };

    private void InitViews() {
        payable = new Payable(this);

        txtStatus = (TextView) findViewById(R.id.txtStatus);


        edtVoidNo = (EditText) findViewById(R.id.edtVoidNo);

        btnVoid = (Button) findViewById(R.id.btnVoid);
        btnVoid.setOnClickListener(onClickListener);
    }

    @Override
    public void onSuccessEcho(int res) {

    }

    @Override
    public void onFailureEcho(PayableException e) {

    }

    @Override
    public void onSuccessOpenTx(ArrayList<PayableTX> res) {

    }

    @Override
    public void onFailureOpenTx(PayableException e) {

    }

    @Override
    public void onSuccessCloseTx(ArrayList<PayableTX> res) {

    }

    @Override
    public void onFailureCloseTx(PayableException e) {

    }

    @Override
    public void onSuccessSales(TxSaleRes txSaleRes) {

    }

    @Override
    public void onFailureSales(PayableException e) {

    }

    @Override
    public void onSuccessVoid(TxVoidRes txVoidRes) {
        txtStatus.setText("Reply Received");
        Toast.makeText(getApplicationContext(), "Successfully Voided", Toast.LENGTH_LONG).show();

        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onFailureVoid(PayableException e) {
        txtStatus.setText("Reply Received");
        Result_Dialog.ResultDialog(VoidActivity.this, e.toString());
    }

    @Override
    public void onSuccessSignature() {

    }

    @Override
    public void onFailureSignature(PayableException e) {

    }

    @Override
    public void onSuccessSettlementSummary(ArrayList<TxSettlementSummaryEle> res) {

    }

    @Override
    public void onFailureSettlementSummary(PayableException e) {

    }

    @Override
    public void onSuccessSettle(TxSettlementResponse res) {

    }

    @Override
    public void onFailureSettle(PayableException e) {

    }

    @Override
    public void onSuccessBatchList(ArrayList<BatchlistRes> res) {

    }

    @Override
    public void onFailureBatchList(PayableException e) {

    }

    @Override
    public void onSucessAuthenticate() {

    }

    @Override
    public void onFailureAuthenticate(PayableException e) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }
}
