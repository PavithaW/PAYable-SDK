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

/**
 * Created by Dell on 12/9/2015.
 */
public class OpenTxAdapter extends BaseAdapter {
    ArrayList<PayableTX> itemList;

    public Activity context;
    public LayoutInflater inflater;


    ArrayList<PayableTX> AllData = new ArrayList<PayableTX>();


    public OpenTxAdapter(Activity context, ArrayList<PayableTX> itemList) {
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

        TextView txtName, txtAmount;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.open_listdata, null);
            holder = new ViewHolder();

            holder.txtName = (TextView) convertView.findViewById(R.id.txtTitle);

            holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        PayableTX bean = (PayableTX) itemList.get(pos);

        holder.txtName.setText(bean.getCardHolder());

      //  holder.txtName.setText(String.valueOf(bean.getTxId()));

        holder.txtAmount.setText(String.valueOf(bean.getAmount()));


        return convertView;
    }


}

