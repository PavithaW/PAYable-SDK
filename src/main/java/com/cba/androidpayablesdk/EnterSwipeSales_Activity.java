package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableCardReaderCallBack;
import com.cba.payablesdk.PayableEnvironment;
import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import common.PayableCallBack;
import models.BatchlistRes;
import models.Credentials;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;
import dialogs.Result_Dialog;

public class EnterSwipeSales_Activity extends AppCompatActivity implements PayableCallBack,PayableCardReaderCallBack {

    protected static final String TAG = "JEYLOGS";

    EditText edtAmount;
    Button btnSubmit;

    Payable payable ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_swipe_sales);

        InitViews();

        payable = new Payable(this);
    }


    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btnSubmit:
                {
                   /* Intent in =new Intent (getApplicationContext(),SwipeSalesActivity.class);
                    in.putExtra("Amount",Double.parseDouble(edtAmount.getText().toString()));
                    startActivity(in);*/

                    performTx() ;


                }
            }

        }
    };

    private void performTx33(){
        //Log.i("JEYLOGS" , "inside performTx") ;

        try {
            payable.callecho(this ,300);
        } catch (PayableException e) {
            //Log.i("JEYLOGS" , "inside performTx exception") ;
            //Log.i("JEYLOGS" , "exception e:" + e.toString()) ;
        }
    }


    private void performTx2(){
        //Log.i("JEYLOGS" , "inside performTx") ;

        Credentials c = new Credentials() ;
//        c.setUserName("tst3@mail.com");
        c.setUserName("test1@cba.lk");
        c.setPwd("tst12345");
        c.setDeveloperKey("895CE8EA3075F0ED7BAE164C3CC23DF6C4F7DE704161E7643E2CB123FF06CFD5D8A7C67CFE7E38AACE1A22CE83F1F55E738D25F464D79F6546C09C40CEFDF3A2");
        c.setDeveloperToken("BDAA59153102007076AC201A6C8A1A4D1E1863D2");
        c.setEnvironment(PayableEnvironment.SAND_BOX);

        try {
            payable.authenticate(this,c);
        } catch (PayableException e) {
            //Log.i("JEYLOGS" , "inside performTx exception") ;
            //Log.i("JEYLOGS" , "exception e:" + e.toString()) ;
        }
    }

    private void performTx(){
        //Log.i("JEYLOGS" , "inside performTx") ;
       /* try {
            payable.sale(this,this,10.51,100);
        } catch (PayableException e) {
            Log.i("JEYLOGS" , "inside performTx exception") ;
            Log.i("JEYLOGS" , "exception e:" + e.toString()) ;
        }*/

        try {
            payable.sale(this,this,Integer.parseInt(edtAmount.getText().toString()),"aaa10"); // TODO: 1/17/2017
        } catch (PayableException e) {
            //Log.i("JEYLOGS" , "inside performTx exception") ;
            //Log.i("JEYLOGS" , "exception e:" + e.toString()) ;
            Result_Dialog.ResultDialog(EnterSwipeSales_Activity.this, e.toString());

        }
    }

    private void InitViews()
    {
        edtAmount =(EditText)findViewById(R.id.edtAmount);

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(onClickListener);
    }

    @Override
    public void onSuccessEcho(int res) {
        //Log.i(TAG, "inside onSuccessEcho");
         //Log.i(TAG, "status :" + res);
    }

    @Override
    public void onFailureEcho(PayableException e) {
        //Log.i(TAG, "inside onFailureEcho");
        //Log.i(TAG, "err :" + e.getMessage());
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
        //Log.i("JEYLOGS" , "inside EnterSwipeSales_Activity -> onSuccessSales ") ;
        //Log.i(TAG,"Aprrval id : " + txSaleRes.getApprovalCode());
        Result_Dialog.ResultDialog(this,"Success Transaction", txSaleRes.getApprovalCode());
    }

    @Override
    public void onFailureSales(PayableException e) {
        //Log.i("JEYLOGS" , "inside EnterSwipeSales_Activity -> onFailureSales ") ;
        //Log.i("JEYLOGS" , "Ex message: " + e.getMessage()) ;
        //Log.i("JEYLOGS" , "Ex code: " + e.getErrcode()) ;
        Result_Dialog.ResultDialog(this,"Fail Transaction", e.toString());

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
        //Log.i(TAG, "inside onSucessAuthenticate");
       // Log.i(TAG, "status :" + status);
    }

    @Override
    public void onFailureAuthenticate(PayableException e) {
        //Log.i(TAG, "inside onFailureAuthenticate");
        //Log.i(TAG, "err :" + e.getMessage());
    }

    @Override
    public void cardReaderStatus(int status, String message) {
        //Log.i(TAG, "inside cardReaderStatus");
        //Log.i(TAG, "status :" + status);
        //Log.i(TAG, "message :" + message);

    }
}
