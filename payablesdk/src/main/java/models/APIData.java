package models;

import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Dell on 12/8/2015.
 */
public abstract class APIData implements Parcelable {

    public String getJson() {
        final GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        final Gson gson = builder.create();
        return gson.toJson(this);
    }

    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
