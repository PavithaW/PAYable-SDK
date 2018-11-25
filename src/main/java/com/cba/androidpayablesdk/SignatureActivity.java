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
import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class SignatureActivity extends AppCompatActivity implements PayableCallBack {

    EditText edtSalesId, edtSig;
    Button btnSubmit;
    TextView txtStatus;

    Payable payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        InitViews();
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnSubmit: {

                  /*  txtStatus.setText("Sent Request, Awaiting Reply.....");
                    //    payable.AddSignature(getApplicationContext(), Long.parseLong(edtSalesId.getText().toString()), edtSig.getText().toString());

                    try {
                        payable.AddSignature(SignatureActivity.this, Long.parseLong(edtSalesId.getText().toString()), "/9j/4AAQSkZJRgABAQAASABIAAD/4QBMRXhpZgAATU0AKgAAAAgoooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAoo\nooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAoo\nooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA//9k=");
                    } catch (PayableException e) {
                        e.printStackTrace();
                    }*/


                    break;
                }
            }
        }
    };


    private void InitViews() {
        payable = new Payable(this);

        txtStatus = (TextView) findViewById(R.id.txtStatus);


        edtSalesId = (EditText) findViewById(R.id.edtSalesId);
        edtSig = (EditText) findViewById(R.id.edtSig);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(onClickListener);
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

    }

    @Override
    public void onFailureVoid(PayableException e) {

    }

    @Override
    public void onSuccessSignature() {
        txtStatus.setText("Reply Received");
        Toast.makeText(getApplicationContext(), "Signature is Added Successfully", Toast.LENGTH_LONG).show();

        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onFailureSignature(PayableException e) {
        txtStatus.setText("Reply Received");
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
