package models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Dell on 12/29/2015.
 */
public class CloseTxHistoryReq extends APIData {

    public static final Creator<CloseTxHistoryReq> CREATOR = new Creator<CloseTxHistoryReq>() {
        public CloseTxHistoryReq createFromParcel(Parcel in) {
            return new CloseTxHistoryReq(in);
        }

        public CloseTxHistoryReq[] newArray(int size) {
            return new CloseTxHistoryReq[size];
        }
    };

    @SerializedName("serchTerm")
    @Expose
    private String serchTerm ;

    @SerializedName("pageId")
    @Expose
    private int pageId ;

    @SerializedName("pageSize")
    @Expose
    private int pageSize ;

    @SerializedName("master")
    @Expose
    private int master = 0;

    @SerializedName("visa")
    @Expose
    private int visa = 0 ;

    @SerializedName("amex")
    @Expose
    private int amex = 0 ;

    @SerializedName("stDate")
    @Expose
    private Date stDate ;

    @SerializedName("enDate")
    @Expose
    private Date enDate ;

    public CloseTxHistoryReq(){

    }



    public CloseTxHistoryReq(int pageId, int pageSize, int master, int visa, int amex, Date stDate, Date enDate, String serchTerm) {
        this.pageId = pageId;
        this.pageSize = pageSize;
        this.master = master;
        this.visa = visa;
        this.amex = amex;
        this.stDate = stDate;
        this.enDate = enDate;
        this.serchTerm = serchTerm;
    }

    public CloseTxHistoryReq(Parcel in){
        serchTerm = in.readString() ;
        pageId = in.readInt() ;
        pageSize = in.readInt() ;
        master = in.readInt() ;
        visa = in.readInt() ;
        amex = in.readInt() ;

        long val1 = in.readLong() ;
        if(val1> 0){
            stDate = new Date(val1) ;
        }else{
            stDate = null ;
        }

        long val2 = in.readLong() ;
        if(val2> 0){
            enDate = new Date(val2) ;
        }else{
            enDate = null ;
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(serchTerm) ;
        out.writeInt(pageId) ;
        out.writeInt(pageSize);
        out.writeInt(master);
        out.writeInt(visa);
        out.writeInt(amex);

        if(stDate == null){
            out.writeLong(-1) ;
        }else{
            out.writeLong(stDate.getTime()) ;
        }

        if(enDate == null){
            out.writeLong(-1) ;
        }else{
            out.writeLong(enDate.getTime()) ;
        }
    }

    public String getSerchTerm() {
        return serchTerm;
    }

    public void setSerchTerm(String serchTerm) {
        this.serchTerm = serchTerm;
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

    public int getMaster() {
        return master;
    }

    public void setMaster(int master) {
        this.master = master;
    }

    public int getVisa() {
        return visa;
    }

    public void setVisa(int visa) {
        this.visa = visa;
    }

    public int getAmex() {
        return amex;
    }

    public void setAmex(int amex) {
        this.amex = amex;
    }

    public Date getStDate() {
        return stDate;
    }

    public void setStDate(Date stDate) {
        this.stDate = stDate;
    }

    public Date getEnDate() {
        return enDate;
    }

    public void setEnDate(Date enDate) {
        this.enDate = enDate;
    }




}

