package com.cba.androidpayablesdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Logout extends AppCompatActivity implements PayableCallBack {

    Button btnLogout;
    Payable payable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        InitViews() ;
        payable = new Payable(this);
    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btnLogout:
                {
                    performLogout() ;
                }
            }

        }
    };

    private void performLogout() {
        //Log.i("JEYLOGS" , "inside performLogout") ;
        try {
            payable.logout(this);
        } catch (PayableException e) {
            //Log.i("JEYLOGS" , "inside exception") ;
            //Log.i("JEYLOGS" , "Ex : " + e.toString()) ;
        }
    }

    private void InitViews()
    {
       // edtAmount =(EditText)findViewById(R.id.edtAmount);

        btnLogout=(Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(onClickListener);
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
}
