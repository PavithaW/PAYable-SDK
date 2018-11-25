package com.cba.androidpayablesdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableEnvironment;
import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import common.PayableCallBack;
import dialogs.Result_Dialog;
import models.BatchlistRes;
import models.Credentials;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class SignInActivity extends AppCompatActivity implements PayableCallBack {

    protected static final String TAG = "JEYLOGS";

    EditText edtUsername, edtPassword;
    Button btnSignIn;

    Payable payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initViews();

        payable = new Payable(this);
    }

    private void initViews() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btnSignIn: {
                    Credentials c = new Credentials() ;
                    c.setUserName(edtUsername.getText().toString());
                    c.setPwd(edtPassword.getText().toString());
                    c.setDeveloperKey("895CE8EA3075F0ED7BAE164C3CC23DF6C4F7DE704161E7643E2CB123FF06CFD5D8A7C67CFE7E38AACE1A22CE83F1F55E738D25F464D79F6546C09C40CEFDF3A2");
                    c.setDeveloperToken("BDAA59153102007076AC201A6C8A1A4D1E1863D2");
                     c.setEnvironment(PayableEnvironment.SAND_BOX);

                    try {
                        payable.authenticate(SignInActivity.this,c);
                    } catch (PayableException e) {
                        //Log.i(TAG , "exception e:" + e.toString());
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void onSuccessEcho(int res) { }

    @Override
    public void onFailureEcho(PayableException e) { }

    @Override
    public void onSuccessOpenTx(ArrayList<PayableTX> res) { }

    @Override
    public void onFailureOpenTx(PayableException e) { }

    @Override
    public void onSuccessCloseTx(ArrayList<PayableTX> res) { }

    @Override
    public void onFailureCloseTx(PayableException e) { }

    @Override
    public void onSuccessSales(TxSaleRes txSaleRes) { }

    @Override
    public void onFailureSales(PayableException e) { }

    @Override
    public void onSuccessVoid(TxVoidRes txVoidRes) { }

    @Override
    public void onFailureVoid(PayableException e) { }

    @Override
    public void onSuccessSignature() { }

    @Override
    public void onFailureSignature(PayableException e) { }

    @Override
    public void onSuccessSettlementSummary(ArrayList<TxSettlementSummaryEle> res) { }

    @Override
    public void onFailureSettlementSummary(PayableException e) { }

    @Override
    public void onSuccessSettle(TxSettlementResponse res) { }

    @Override
    public void onFailureSettle(PayableException e) { }

    @Override
    public void onSuccessBatchList(ArrayList<BatchlistRes> res) { }

    @Override
    public void onFailureBatchList(PayableException e) { }

    @Override
    public void onSucessAuthenticate() {
        Intent in =new Intent(SignInActivity.this,MainActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    public void onFailureAuthenticate(PayableException e) {
        Result_Dialog.ResultDialog(SignInActivity.this, e.toString());
    }
}
