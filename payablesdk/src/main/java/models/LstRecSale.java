package models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Dell on 12/9/2015.
 */
public class LstRecSale extends APIList<RecSale> {

    @Override
    protected ClassLoader onGetClassLoader() {
        return RecSale.class.getClassLoader();
    }

    @Override
    public void setList(String json) {
        Gson gson = new GsonBuilder().create();

        try{
            JSONArray arr = new JSONArray(json);

            if (arr == null || arr.length() == 0) {
                return;
            }

            listObj = new ArrayList<RecSale>();

            for (int i = 0; i < arr.length(); i++) {
                String strRes = arr.getJSONObject(i).toString() ;
                RecSale resData = gson.fromJson(strRes, RecSale.class);
                listObj.add(resData);

            }

        }catch (JSONException e) {
            //Log.i("Error is", "Exp:" + e.toString());
        }


    }

}

