package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import adapters.SettlementAdapter;
import common.PayableCallBack;
import dialogs.Result_Dialog;
import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class SettlementSummary_Activity extends AppCompatActivity implements PayableCallBack {

    TextView txtStatus;
    ListView lstSettle;

    Payable payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_summary);

        InitViews();


    }


    private void InitViews() {
        payable = new Payable(this);

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText("Sent Request, Awaiting Reply.....");

        lstSettle = (ListView) findViewById(R.id.lstSettle);

        try {
            payable.batchsummary(SettlementSummary_Activity.this, 1);
        } catch (PayableException e) {
            e.printStackTrace();
        }
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

    }

    @Override
    public void onFailureSignature(PayableException e) {

    }

    @Override
    public void onSuccessSettlementSummary(ArrayList<TxSettlementSummaryEle> res) {

        txtStatus.setText("Reply Received");

        SettlementAdapter settlementAdapter = new SettlementAdapter(SettlementSummary_Activity.this, res);
        lstSettle.setAdapter(settlementAdapter);

    }

    @Override
    public void onFailureSettlementSummary(PayableException e) {

        txtStatus.setText("Reply Received");
        Result_Dialog.ResultDialog(SettlementSummary_Activity.this, e.toString());
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
