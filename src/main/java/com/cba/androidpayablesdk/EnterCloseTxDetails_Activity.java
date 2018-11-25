package com.cba.androidpayablesdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class EnterCloseTxDetails_Activity extends AppCompatActivity {

    private static final String TAG = "JEYLOGS";

    EditText edtSearch, edtStartDate, edtEndDate, edtPageId, edtPageSize;
    CheckBox chkMaster, chkVisa, chkAmex;
    Button btnSubmit;

    int Master_status=0, Visa_status=0, Amex_Status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_close_tx_details);

        InitViews();
    }

    private void InitViews()
    {
        edtSearch =(EditText)findViewById(R.id.edtSearch);

        edtStartDate =(EditText)findViewById(R.id.edtStartDate);

        edtEndDate =(EditText)findViewById(R.id.edtEndDate);

        edtPageId =(EditText)findViewById(R.id.edtPageId);

        edtPageSize =(EditText)findViewById(R.id.edtPageSize);

        chkMaster =(CheckBox)findViewById(R.id.chkMaster);

        chkVisa =(CheckBox)findViewById(R.id.chkVisa);

        chkAmex =(CheckBox)findViewById(R.id.chkAmex);

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(onClickListener);

        chkMaster.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Master_status=1;
                } else {
                    Master_status=0;
                }
            }
        });

        chkVisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Visa_status=1;
                } else {
                    Visa_status=0;
                }
            }
        });

        chkAmex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Amex_Status=1;
                } else {
                    Amex_Status=0;
                }
            }
        });
    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch(v.getId())
            {
                case R.id.btnSubmit:
                {
                    //Log.i(TAG, "Visa_status : " + Visa_status) ;
                    //Log.i(TAG, "Master_status : " + Master_status) ;
                    //Log.i(TAG, "Amex_status : " + Amex_Status) ;

                    Intent in =new Intent (getApplicationContext(),CloseTxListActivity_2.class);
                    in.putExtra("Searchterm",edtSearch.getText().toString());
                    in.putExtra("PageId",edtPageId.getText().toString());
                    in.putExtra("PageSize",edtPageSize.getText().toString());
                    in.putExtra("StartDate",edtStartDate.getText().toString());
                    in.putExtra("EndDate",edtEndDate.getText().toString());
                    in.putExtra("MasterStatus",Master_status);
                    in.putExtra("VisaStatus",Visa_status);
                    in.putExtra("AmexStatus",Amex_Status);
                    startActivity(in);
                }
            }
        }
    };
}
