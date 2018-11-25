package models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TxSettlementReq extends APIData{
	
	public static final Creator<TxSettlementReq> CREATOR = new Creator<TxSettlementReq>() {
		public TxSettlementReq createFromParcel(Parcel in) {
			return new TxSettlementReq(in);
		}

		public TxSettlementReq[] newArray(int size) {
			return new TxSettlementReq[size];
		}
	};

	
	
	@SerializedName("stn")
	@Expose
	private int valSTN ;
	
	@SerializedName("batchNumber")
	@Expose
	private int batchNumber ;

	public TxSettlementReq(){
		
	}
	
	public TxSettlementReq(Parcel in){
		valSTN = in.readInt() ;
		batchNumber = in.readInt() ;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(valSTN) ;
		out.writeInt(batchNumber) ;
		
	}
	
	public int getValSTN() {
		return valSTN;
	}

	public void setValSTN(int valSTN) {
		this.valSTN = valSTN;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}

	
	
	

}
