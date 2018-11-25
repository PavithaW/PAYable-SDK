package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cba.payablesdk.Payable;
import com.cba.payablesdk.PayableException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import adapters.OpenTxAdapter;
import common.PayableCallBack;
import dialogs.Result_Dialog;
import models.BatchlistRes;
import models.PayableTX;
import models.TxSaleRes;
import models.TxSettlementResponse;
import models.TxSettlementSummaryEle;
import models.TxVoidRes;

public class CloseTxListActivity extends AppCompatActivity implements PayableCallBack {

    ListView lstOpentx;
    TextView txtStatus;

    ArrayList<PayableTX> payableTXArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_tx_list);

        InitViews();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = formatter.parse(extras.getString("StartDate"));
                endDate = formatter.parse(extras.getString("EndDate"));
                String newDateString = formatter.format(startDate);

                System.out.println(newDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Payable payable = new Payable(this);
            try {
                payable.getCloseTxs(CloseTxListActivity.this, Integer.parseInt(extras.getString("PageId")), Integer.parseInt(extras.getString("PageSize")),
                        extras.getInt("MasterStatus"), extras.getInt("VisaStatus"), startDate, endDate, extras.getString("Searchterm"));
            } catch (PayableException e) {
                Result_Dialog.ResultDialog(CloseTxListActivity.this, e.toString());
            }
        }
    }

    private void InitViews() {
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText("Sent Request, Awaiting Reply.....");

        lstOpentx = (ListView) findViewById(R.id.lstOpentx);
        lstOpentx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String cardHolder = payableTXArrayList.get(position).getCardHolder();
                String amount = String.valueOf(payableTXArrayList.get(position).getAmount());
                String ccLast4 = payableTXArrayList.get(position).getCcLast4();
                String datetime = String.valueOf(payableTXArrayList.get(position).getTime());
                //String approvalcode = String.valueOf(payableTXArrayList.get(position).getApprovalCode());

                String approvalcode = payableTXArrayList.get(position).getApprovalCode();

                Intent in = new Intent(getApplicationContext(), OpenTxDetails_Activity.class);
                in.putExtra("cardHolder", cardHolder);
                in.putExtra("amount", amount);
                in.putExtra("ccLast4", ccLast4);
                in.putExtra("datetime", datetime);
                in.putExtra("approvalcode", approvalcode);
                startActivity(in);


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
        txtStatus.setText("Reply Received");
        payableTXArrayList = res;

        OpenTxAdapter openTxAdapter = new OpenTxAdapter(CloseTxListActivity.this, res);
        lstOpentx.setAdapter(openTxAdapter);
    }

    @Override
    public void onFailureCloseTx(PayableException e) {
        txtStatus.setText("Reply Received");

        Result_Dialog.ResultDialog(CloseTxListActivity.this, e.toString());
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(in);
        finish();
    }
}
