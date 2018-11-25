package dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cba.androidpayablesdk.R;


/**
 * Created by Dell on 12/7/2015.
 */
public class Result_Dialog {

    static Dialog dialog;
    TextView txtResult;


    public static void ResultDialog(Activity activity, String strReply) {

        try {

            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_result);
            dialog.setCanceledOnTouchOutside(false);

            final TextView txtResult = (TextView) dialog.findViewById(R.id.txtResult);
            txtResult.setText(strReply);

            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Display display = activity.getWindowManager().getDefaultDisplay();

            WindowManager.LayoutParams lp;

            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
//            lp.width = (int) (display.getWidth() * 0.9);
//            lp.height = (int) (display.getHeight() * 0.9);
//
//            dialog.getWindow().setAttributes(lp);

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void ResultDialog(Activity activity, String title, String strReply) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(strReply);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
}
