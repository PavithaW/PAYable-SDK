package com.cba.androidpayablesdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OpenTxDetails_Activity extends AppCompatActivity {

    TextView txtCardHolderValue, txtAmountValue, txtccLast4Value, txtDateValue, txtApprovalCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_tx_details);

        InitViews();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            txtCardHolderValue.setText(extras.getString("cardHolder"));
            txtAmountValue.setText(extras.getString("amount"));
            txtccLast4Value.setText(extras.getString("ccLast4"));
            txtDateValue.setText(extras.getString("datetime"));
            txtApprovalCodeValue.setText(extras.getString("approvalcode"));

        }
    }

    private void InitViews() {
        txtCardHolderValue = (TextView) findViewById(R.id.txtCardHolderValue);
        txtAmountValue = (TextView) findViewById(R.id.txtAmountValue);
        txtccLast4Value = (TextView) findViewById(R.id.txtccLast4Value);
        txtDateValue = (TextView) findViewById(R.id.txtDateValue);
        txtApprovalCodeValue = (TextView) findViewById(R.id.txtApprovalCodeValue);


    }
}
