package models;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxDsEmvReq  extends APIData {
	
	public static final Creator<TxDsEmvReq> CREATOR = new Creator<TxDsEmvReq>() {
		public TxDsEmvReq createFromParcel(Parcel in) {
			return new TxDsEmvReq(in);
		}

		public TxDsEmvReq[] newArray(int size) {
			return new TxDsEmvReq[size];
		}
	};


	@SerializedName("amount")
	@Expose
	private double amount ;
	
	@SerializedName("invoice")
	@Expose
	private int invoice ;
	
	
	@SerializedName("clientTime")
	@Expose
	private Date clientTime ;
	
	@SerializedName("crId")
	@Expose
	private String cardReaderId ;
	
	@SerializedName("tsToken")
	@Expose
	private long tsToken  = 0;
	
	@SerializedName("rndToken")
	@Expose
	private long rndToken = 0 ; 
	
	
	@SerializedName("icData")
	@Expose
	private String icData ;
	
	// added on 8th april 2016
	@SerializedName("merchantInvoiceId")
	@Expose
	private String merchantInvoiceId;
	
	public TxDsEmvReq(){
		
	}


	
	public TxDsEmvReq(Parcel in){
		amount = in.readDouble();
		invoice = in.readInt();
		
		long val1 = in.readLong();

		if (val1 > 0) {
			clientTime = new Date(val1);
		} else {
			clientTime = null;
		}

		cardReaderId = in.readString();
		tsToken = in.readLong() ;
		rndToken = in.readLong() ;
		icData = in.readString() ;
		merchantInvoiceId = in.readString() ;
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeDouble(amount) ;
		out.writeInt(invoice) ;
		
		if(clientTime != null){
			out.writeLong(clientTime.getTime()) ;
		}else{
			out.writeLong(-1) ;
		}
		
		out.writeString(cardReaderId) ;
		out.writeLong(tsToken);
		out.writeLong(rndToken) ;
		out.writeString(icData) ;
		out.writeString(merchantInvoiceId);
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getInvoice() {
		return invoice;
	}

	public void setInvoice(int invoice) {
		this.invoice = invoice;
	}


	public Date getClientTime() {
		return clientTime;
	}

	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}

	public String getCardReaderId() {
		return cardReaderId;
	}

	public void setCardReaderId(String cardReaderId) {
		this.cardReaderId = cardReaderId;
	}

	public long getTsToken() {
		return tsToken;
	}

	public void setTsToken(long tsToken) {
		this.tsToken = tsToken;
	}

	public long getRndToken() {
		return rndToken;
	}

	public void setRndToken(long rndToken) {
		this.rndToken = rndToken;
	}

	public String getIcData() {
		return icData;
	}

	public void setIcData(String icData) {
		this.icData = icData;
	}

	public String getMerchantInvoiceId() {
		return merchantInvoiceId;
	}

	public void setMerchantInvoiceId(String merchantInvoiceId) {
		this.merchantInvoiceId = merchantInvoiceId;
	}

	
	
	
}
