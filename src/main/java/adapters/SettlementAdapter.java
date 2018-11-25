package adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cba.androidpayablesdk.R;

import java.util.ArrayList;

import models.PayableTX;
import models.TxSettlementSummaryEle;

/**
 * Created by Dell on 3/2/2016.
 */
public class SettlementAdapter extends BaseAdapter {
    ArrayList<TxSettlementSummaryEle> itemList;

    public Activity context;
    public LayoutInflater inflater;


    ArrayList<PayableTX> AllData = new ArrayList<PayableTX>();


    public SettlementAdapter(Activity context, ArrayList<TxSettlementSummaryEle> itemList) {
        // TODO Auto-generated constructor stub

        super();
        this.context = context;
        this.itemList = itemList;
        AllData = (ArrayList<PayableTX>) itemList.clone();


    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    private class ViewHolder {

        TextView txtType, txtCount, txtAmount;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lstdata_settle, null);
            holder = new ViewHolder();

            holder.txtType = (TextView) convertView.findViewById(R.id.txtType);

            holder.txtCount = (TextView) convertView.findViewById(R.id.txtCount);

            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        TxSettlementSummaryEle bean = (TxSettlementSummaryEle) itemList.get(pos);

        if(bean.getType()==1)
        {
            holder.txtType.setText("Visa");
        }

       else if(bean.getType()==2)
        {
            holder.txtType.setText("Amex");
        }

       else if(bean.getType()==3)
        {
            holder.txtType.setText("Master");
        }
        else
        {
            // Do Nothing
        }

        holder.txtCount.setText(String.valueOf(bean.getCount()));

        holder.txtAmount.setText(String.valueOf(bean.getAmount()));


        return convertView;
    }


}


