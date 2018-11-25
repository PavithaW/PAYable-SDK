package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity implements PayableCallBack {

    Button btnOpenTx, btnCloseTx, btnSales, btnVoid, btnSignature, btnSettleSummary, btnSettle;
    ListView lstMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitViews();


    }

    private void InitViews() {
        lstMain = (ListView) findViewById(R.id.lstMain);

       /* btnOpenTx =(Button)findViewById(R.id.btnOpenTx);
        btnOpenTx.setOnClickListener(onClickListener);

        btnCloseTx=(Button)findViewById(R.id.btnCloseTx);
        btnCloseTx.setOnClickListener(onClickListener);

        btnSales=(Button)findViewById(R.id.btnSales);
        btnSales.setOnClickListener(onClickListener);

        btnVoid=(Button)findViewById(R.id.btnVoid);
        btnVoid.setOnClickListener(onClickListener);

        btnSignature=(Button)findViewById(R.id.btnSignature);
        btnSignature.setOnClickListener(onClickListener);

        btnSettleSummary=(Button)findViewById(R.id.btnSettleSummary);
        btnSettleSummary.setOnClickListener(onClickListener);

        btnSettle=(Button)findViewById(R.id.btnSettle);
        btnSettle.setOnClickListener(onClickListener);
*/
        // Defined Array values to show in ListView
       /* String[] values = new String[]{"Open Transactions",
                "Close Transactions",
                "Sales",
                "Void",
                "Signature",
                "Settle Summary",
                "Settle",
                "Batch List",
                "Test",
                "Register Reader"

        };
*/
        String[] values = new String[]{"Open Transactions",
                "Close Transactions",
                "Sales",
                "Void",
                "Logout",
                "Settle Summary",
                "Settle",
                "Batch List",
                "Test",
                "Register Reader",
                "Sign in"

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        lstMain.setAdapter(adapter);


        lstMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: {
                        Intent in = new Intent(MainActivity.this, EnterOpenTxDetails_Activity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 1: {
                        Intent in = new Intent(MainActivity.this, EnterCloseTxDetails_Activity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 2: {
                        Intent in = new Intent(MainActivity.this, EnterSwipeSales_Activity.class);
                        startActivity(in);
                      //  finish();
                        break;
                    }

                    case 3: {
                        Intent in = new Intent(MainActivity.this, VoidActivity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 4: {
                        Intent in = new Intent(MainActivity.this, Logout.class);
                        startActivity(in);
                        //finish();
                        break;
                    }


                    case 5: {
                        Intent in = new Intent(MainActivity.this, SettlementSummary_Activity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 6: {
                        Intent in = new Intent(MainActivity.this, SettleActivity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 7: {
                        Intent in = new Intent(MainActivity.this, EnterBatchList_Activity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 8: {


                        break;
                    }

                    case 9:
                    {
                        Intent in = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(in);
                       // finish();
                        break;
                    }

                    case 10:
                    {
                        Intent in = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(in);
                        break;
                    }

                }
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
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    /* private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btnOpenTx:
                {
                    Intent in =new Intent(MainActivity.this,EnterOpenTxDetails_Activity.class);
                    startActivity(in);
                    finish();
                    break;
                }

                case R.id.btnCloseTx:
                {
                    Intent in =new Intent(MainActivity.this,EnterCloseTxDetails_Activity.class);
                    startActivity(in);
                    finish();
                    break;
                }

                case R.id.btnSales:
                {
                    Intent in =new Intent(MainActivity.this,EnterSwipeSales_Activity.class);
                    startActivity(in);
                    finish();
                    break;
                }

                case R.id.btnVoid:
                {
                    Intent in =new Intent(MainActivity.this,VoidActivity.class);
                    startActivity(in);
                    finish();
                    break;
                }

                case R.id.btnSignature:
                {
                    Intent in =new Intent(MainActivity.this,SignatureActivity.class);
                    startActivity(in);
                    finish();
                    break;
                }


                case R.id.btnSettleSummary:
                {
                    Intent in =new Intent(MainActivity.this,SettlementSummary_Activity.class);
                    startActivity(in);
                    finish();
                    break;
                }

                case R.id.btnSettle:
                {
                    Intent in =new Intent(MainActivity.this, SettleActivity.class);
                    startActivity(in);
                    finish();
                    break;
                }



            }

        }
    };*/
}
