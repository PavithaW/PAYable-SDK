package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class SettleActivity extends AppCompatActivity implements PayableCallBack{

    Button btnSettle;
    TextView txtStatus,txtTotalAmount,txtTotalSales;

    Payable payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settle);

        InitViews();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnSettle: {

                    txtStatus.setText("Sent Request, Awaiting Reply.....");
                    //    payable.AddSignature(getApplicationContext(), Long.parseLong(edtSalesId.getText().toString()), edtSig.getText().toString());

                    try {
                        payable.settleSales(SettleActivity.this,1);
                    } catch (PayableException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
    };


    private void InitViews()
    {
        payable = new Payable(this);

        btnSettle =(Button)findViewById(R.id.btnSettle);
        btnSettle.setOnClickListener(onClickListener);

        txtStatus =(TextView)findViewById(R.id.txtStatus);

        txtTotalAmount =(TextView)findViewById(R.id.txtTotalAmount);

        txtTotalSales =(TextView)findViewById(R.id.txtTotalSales);

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

    }

    @Override
    public void onFailureSettlementSummary(PayableException e) {

    }

    @Override
    public void onSuccessSettle(TxSettlementResponse res) {
        txtStatus.setText("Reply Received");
        TxSettlementResponse txSettlementResponse = res;

        txtTotalAmount.setText(String.valueOf(txSettlementResponse.getTotalAmount()));
        txtTotalSales.setText(String.valueOf(txSettlementResponse.getTotalSales()));

    }

    @Override
    public void onFailureSettle(PayableException e) {
        txtStatus.setText("Reply Received");
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
