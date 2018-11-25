package adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cba.androidpayablesdk.R;

import java.util.ArrayList;

import models.BatchlistRes;

/**
 * Created by Dell on 3/29/2016.
 */
public class BatchAdapter extends BaseAdapter {
    ArrayList<BatchlistRes> itemList;

    public Activity context;
    public LayoutInflater inflater;


    ArrayList<BatchlistRes> AllData = new ArrayList<BatchlistRes>();


    public BatchAdapter(Activity context, ArrayList<BatchlistRes> itemList) {
        // TODO Auto-generated constructor stub

        super();
        this.context = context;
        this.itemList = itemList;
        AllData = (ArrayList<BatchlistRes>) itemList.clone();


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

        TextView txtSalesTotal, txtNoSales,txtVoidTotal,txtNoVoids;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lst_batchdata, parent, false);
            holder = new ViewHolder();

            holder.txtSalesTotal = (TextView) convertView.findViewById(R.id.txtSalesTotal);

            holder.txtNoSales = (TextView) convertView.findViewById(R.id.txtNoSales);

            holder.txtVoidTotal = (TextView) convertView.findViewById(R.id.txtVoidTotal);

            holder.txtNoVoids = (TextView) convertView.findViewById(R.id.txtNoVoids);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        BatchlistRes bean = (BatchlistRes) itemList.get(pos);

        holder.txtSalesTotal.setText("Total Sales are  "+ String.valueOf(bean.getSalesTotal()));

        holder.txtNoSales.setText("No of Sales are  "+ String.valueOf(bean.getNoOfSale()));

        holder.txtVoidTotal.setText("Total Voids are  "+ String.valueOf(bean.getVoidTotal()));

        holder.txtNoVoids.setText("No of Voids are  "+ String.valueOf(bean.getNoOfVoid()));


        return convertView;
    }


}


