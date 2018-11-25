package models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 12/8/2015.
 */
public class OpenTxHistoryReq extends APIData {

    @SerializedName("pageId")
    @Expose
    private int pageId;

    @SerializedName("pageSize")
    @Expose
    private int pageSize;


    public static final Creator<OpenTxHistoryReq> CREATOR =new ClassLoaderCreator<OpenTxHistoryReq>()
    {

        @Override
        public OpenTxHistoryReq createFromParcel(Parcel in) {
            return new OpenTxHistoryReq(in);
        }

        @Override
        public OpenTxHistoryReq[] newArray(int size) {
            return new OpenTxHistoryReq[size];
        }

        @Override
        public OpenTxHistoryReq createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }
    };

    public OpenTxHistoryReq() {
    }

    public OpenTxHistoryReq(int pageId, int pageSize) {
        this.pageId = pageId;
        this.pageSize = pageSize;
    }

    public OpenTxHistoryReq(Parcel in) {
        pageId = in.readInt();
        pageSize = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(pageId);
        out.writeInt(pageSize);
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
