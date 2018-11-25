package com.cba.androidpayablesdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterBatchList_Activity extends AppCompatActivity {

    EditText edtPageId,edtPageSize;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_batch_list);

        InitViews();
    }

    private View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId())
            {
                case R.id.btnSubmit:
                {
                    Intent in =new Intent(getApplicationContext(),BatchList_Activity.class);
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
