package models;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SimpleReq extends APIData {
	
	
	public static final Creator<SimpleReq> CREATOR = new Creator<SimpleReq>() {
		public SimpleReq createFromParcel(Parcel in) {
			return new SimpleReq(in);
		}

		public SimpleReq[] newArray(int size) {
			return new SimpleReq[size];
		}
	};
	
	
	@SerializedName("value")
	@Expose
	private int value ;
	
	
	public SimpleReq(){
		
	}

	public SimpleReq(int value) {
		this.value = value;
	}

	public SimpleReq(Parcel in){
		value = in.readInt();
	}
	
	public void writeToParcel(Parcel out, int flags){
		out.writeInt(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	

}
