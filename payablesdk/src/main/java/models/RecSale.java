package models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Dell on 12/9/2015.
 */
public class RecSale extends APIData {

    public static final int STATUS_OPEN = 1 ;
    public static final int STATUS_CLOSE = 2 ;
    // public static final int STATUS_VOID = 3 ;

    public static final int STATUS_OPEN_VOID = 11 ;
    public static final int STATUS_CLOSE_VOID = 12 ;

    public static final int CARD_VISA = 1 ;
    public static final int CARD_AMEX = 2 ;
    public static final int CARD_MASTER = 3 ;

    public static final int SIGNATURE_NOT_AVAILABLE = 1 ;
    public static final int SIGNATURE_AVAILABLE = 2 ;


    public static final Creator<RecSale> CREATOR = new Creator<RecSale>() {
        public RecSale createFromParcel(Parcel in) {
            return new RecSale(in);
        }

        public RecSale[] newArray(int size) {
            return new RecSale[size];
        }
    };


    private int id  = 0;

    @SerializedName("txId")
    @Expose
    private long txId  = 0;

    private long associateTxId = -1 ;

    @SerializedName("cardHolder")
    @Expose
    private String cardHolder ;

    @SerializedName("ccLast4")
    @Expose
    private String ccLast4 ;

    @SerializedName("amount")
    @Expose
    private double amount = 0;

    @SerializedName("cardType")
    @Expose
    private int cardType ;

    private long ts = -1;

    @SerializedName("time")
    @Expose
    private Date time ;


    @SerializedName("status")
    @Expose
    private int status ;

    @SerializedName("approvalCode")
    @Expose
    private int approvalCode ;

    @SerializedName("voidTs")
    @Expose
    private Date voidTs ;

    @SerializedName("sigFlag")
    @Expose
    private int sigFlag ;

    public RecSale(){

    }

    public RecSale(Parcel in){
        id = in.readInt() ;
        txId = in.readLong() ;
        associateTxId = in.readLong() ;
        cardHolder = in.readString() ;
        ccLast4 = in.readString() ;
        amount = in.readDouble() ;
        cardType = in.readInt() ;

        ts = in.readLong() ;

        if(ts> 0){
            time = new Date(ts) ;
        }else{
            time = null ;
        }

        status = in.readInt() ;
        approvalCode = in.readInt() ;

        long l = in.readLong() ;

        if(l> 0){
            voidTs = new Date(l) ;
        }else{
            voidTs = null ;
        }

        sigFlag = in.readInt() ;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeLong(txId) ;
        out.writeLong(associateTxId) ;
        out.writeString(cardHolder) ;
        out.writeString(ccLast4) ;
        out.writeDouble(amount) ;
        out.writeInt(cardType) ;

        if(time == null){
            out.writeLong(-1) ;
        }else{
            out.writeLong(time.getTime()) ;
        }

        out.writeInt(status) ;
        out.writeInt(approvalCode) ;

        if(voidTs == null){
            out.writeLong(-1) ;
        }else{
            out.writeLong(voidTs.getTime()) ;
        }

        out.writeInt(sigFlag) ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTxId() {
        return txId;
    }

    public void setTxId(long txId) {
        this.txId = txId;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCcLast4() {
        return ccLast4;
    }

    public void setCcLast4(String ccLast4) {
        this.ccLast4 = ccLast4;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAssociateTxId() {
        return associateTxId;
    }

    public void setAssociateTxId(long associateTxId) {
        this.associateTxId = associateTxId;
    }

    public int getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(int approvalCode) {
        this.approvalCode = approvalCode;
    }

    public Date getVoidTs() {
        return voidTs;
    }

    public void setVoidTs(Date voidTs) {
        this.voidTs = voidTs;
    }

    public int getSigFlag() {
        return sigFlag;
    }

    public void setSigFlag(int sigFlag) {
        this.sigFlag = sigFlag;
    }

}

