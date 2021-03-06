package com.cba.androidpayablesdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import adapters.OpenTxAdapter;
import common.PayableCallBack;
import dialogs.Result_Dialog;
import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class OpenTxListActivity_2 extends AppCompatActivity implements PayableCallBack {

    private EditText txtStatus;
    private ListView lstOpentx;
    ArrayList<PayableTX> payableTXArrayList = new ArrayList<>();

    protected static final String TAG = "JEYLOGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_tx_list_2);

        InitViews();

        Bundle extras =getIntent().getExtras();

        if(extras!=null)
        {
            Payable payable = new Payable(this);
            try {
                payable.getOpenTxs(OpenTxListActivity_2.this,extras.getInt("PageNumber"), extras.getInt("PageSize"));
            } catch (PayableException e) {
                Result_Dialog.ResultDialog(OpenTxListActivity_2.this, e.toString());
            }

        }

    }

    private void InitViews() {
        txtStatus = (EditText) findViewById(R.id.txtStatus);
        txtStatus.setText("Sent Request, Awaiting Reply.....");

        lstOpentx = (ListView) findViewById(R.id.lstOpentx);
        lstOpentx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String cardHolder = payableTXArrayList.get(position).getCardHolder();
                String amount = String.valueOf(payableTXArrayList.get(position).getAmount());
                String ccLast4 = payableTXArrayList.get(position).getCcLast4();
                String datetime = String.valueOf(payableTXArrayList.get(position).getTime());
                String approvalcode = String.valueOf(payableTXArrayList.get(position).getApprovalCode());

                String strTxId = payableTXArrayList.get(position).getTxId() ;
                //Log.i(TAG , "tx id :" + strTxId) ;

                Intent in = new Intent(getApplicationContext(), OpenTxDetails_Activity.class);
                in.putExtra("cardHolder",cardHolder);
                in.putExtra("amount",amount);
                in.putExtra("ccLast4",ccLast4);
                in.putExtra("datetime",datetime);
                in.putExtra("approvalcode",approvalcode);
                startActivity(in);

            }
        });
    }

    @Override
    public void onSuccessEcho(int res) { }

    @Override
    public void onFailureEcho(PayableException e) { }

    @Override
    public void onSuccessOpenTx(ArrayList<PayableTX> res) {
        txtStatus.setText("Reply Received");
        payableTXArrayList=res;

        OpenTxAdapter openTxAdapter = new OpenTxAdapter(OpenTxListActivity_2.this, res);
        lstOpentx.setAdapter(openTxAdapter);
    }

    @Override
    public void onFailureOpenTx(PayableException e) {
        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        txtStatus.setText( e.toString());
    }

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
    public void onSucessAuthenticate() { }

    @Override
    public void onFailureAuthenticate(PayableException e) { }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }
}
