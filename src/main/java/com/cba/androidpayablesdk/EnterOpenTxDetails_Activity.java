package com.cba.androidpayablesdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterOpenTxDetails_Activity extends AppCompatActivity {

    EditText edtPageId,edtPageSize;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_open_tx_details);

        InitViews();
    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btnSubmit:
                {
                    Intent in =new Intent(EnterOpenTxDetails_Activity.this,OpenTxListActivity_2.class);
                    in.putExtra("PageNumber",Integer.parseInt(edtPageId.getText().toString()));
                    in.putExtra("PageSize",Integer.parseInt(edtPageSize.getText().toString()));
                    startActivity(in);
                    finish();
                    break;
                }
            }
        }
    };

    private void InitViews()
    {
        edtPageId =(EditText)findViewById(R.id.edtPageId);

        edtPageSize =(EditText)findViewById(R.id.edtPageSize);

        btnSubmit =(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(onClickListener);
    }
}
