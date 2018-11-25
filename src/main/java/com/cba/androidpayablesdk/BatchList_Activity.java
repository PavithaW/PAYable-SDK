package com.cba.androidpayablesdk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;

import java.util.ArrayList;

import adapters.BatchAdapter;
import common.PayableCallBack;
import dialogs.Result_Dialog;
import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class BatchList_Activity extends AppCompatActivity implements PayableCallBack {

    ListView lstBatch;
    TextView txtStatus;
    ArrayList<BatchlistRes> batchlistRes = new ArrayList<BatchlistRes>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list_);

        InitViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Payable payable = new Payable(this);
            try {
                payable.batchList(BatchList_Activity.this, extras.getInt("PageNumber"), extras.getInt("PageSize"));
            } catch (PayableException e) {
                Result_Dialog.ResultDialog(BatchList_Activity.this, e.toString());
            }

        }

    }


    private void InitViews() {
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText("Sent Request, Awaiting Reply.....");

        lstBatch = (ListView) findViewById(R.id.lstBatch);
        lstBatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

//                String cardHolder = payableTXArrayList.get(position).getCardHolder();
//                String amount = String.valueOf(payableTXArrayList.get(position).getAmount());
//                String ccLast4 = payableTXArrayList.get(position).getCcLast4();
//                String datetime = String.valueOf(payableTXArrayList.get(position).getTime());
//                String approvalcode = String.valueOf(payableTXArrayList.get(position).getApprovalCode());
//
//                Intent in = new Intent(getApplicationContext(), OpenTxDetails_Activity.class);
//                in.putExtra("cardHolder",cardHolder);
//                in.putExtra("amount",amount);
//                in.putExtra("ccLast4",ccLast4);
//                in.putExtra("datetime",datetime);
//                in.putExtra("approvalcode",approvalcode);
//                startActivity(in);


            }
        });
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
        txtStatus.setText("Reply Received");
        batchlistRes = res;

        BatchAdapter batchAdapter = new BatchAdapter(BatchList_Activity.this, batchlistRes);
        lstBatch.setAdapter(batchAdapter);

    }

    @Override
    public void onFailureBatchList(PayableException e) {

        Result_Dialog.ResultDialog(BatchList_Activity.this, e.toString());
    }

    @Override
    public void onSucessAuthenticate() {

    }

    @Override
    public void onFailureAuthenticate(PayableException e) {

    }
}
