package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

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

public class SwipeSalesActivity extends AppCompatActivity implements PayableCallBack {


    private static final String TAG = SwipeSalesActivity.class.getSimpleName();
    double amount;
    TextView txtStatus;
    TextView txtApprovalCode, txtRRN, txtcardHolder, txtccLast4, txtstn, txtAmount, txtBatchNo, txtCardType, txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_sales);

        InitViews();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            amount = extras.getDouble("Amount");
        }

       /* Payable payable = new Payable(this);

        try {
            payable.SwipeSales(SwipeSalesActivity.this, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private void InitViews() {
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText("Sent Request, Awaiting Reply.....");

        txtAmount = (TextView) findViewById(R.id.txtAmount);

        txtApprovalCode = (TextView) findViewById(R.id.txtApprovalCode);

        txtRRN = (TextView) findViewById(R.id.txtRRN);

        txtcardHolder = (TextView) findViewById(R.id.txtcardHolder);

        txtccLast4 = (TextView) findViewById(R.id.txtccLast4);

        txtstn = (TextView) findViewById(R.id.txtstn);

        txtBatchNo = (TextView) findViewById(R.id.txtBatchNo);

        txtCardType = (TextView) findViewById(R.id.txtCardType);

        txtDate = (TextView) findViewById(R.id.txtDate);
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

        txtStatus.setText("Reply Received");

        String approvalCode = txSaleRes.getApprovalCode();
        txtApprovalCode.setText(approvalCode);

        String rrn = txSaleRes.getRrn();
        txtRRN.setText(rrn);

        String cardHolder = txSaleRes.getCardHolder();
        txtcardHolder.setText(cardHolder);

        String ccLast4 = txSaleRes.getCcLast4();
        txtccLast4.setText(ccLast4);

        int stn = txSaleRes.getStn();
        txtstn.setText(String.valueOf(stn));

        double amount = txSaleRes.getAmount();
        txtAmount.setText(String.valueOf(amount));

        int batchNo = txSaleRes.getBatchNo();
        txtBatchNo.setText(String.valueOf(batchNo));

        int cardType = txSaleRes.getCardType();
        txtCardType.setText(String.valueOf(cardType));

        String serverTime = txSaleRes.getServerTime().toString();
        txtDate.setText(serverTime);


    }

    @Override
    public void onFailureSales(PayableException e) {

        txtStatus.setText("Reply Received");
        //Log.d(TAG, "onFailureSwipeSales: " + e.getErrcode());
        Result_Dialog.ResultDialog(SwipeSalesActivity.this, e.toString());
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
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
        finish();
    }
}
