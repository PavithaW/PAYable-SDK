package com.cba.payablesdk;

import util.EnumException;

/**
 * Created by Dell on 12/10/2015.
 */
public class PayableException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int INTERNAL_ERROR = 1000 ;
    public static final int TIMEOUT_ERROR = 1001 ;
    public static final int INVALID_PARAMETER = -1 ;
    public static final int BUSY_ERROR = 1003;
    public static final int NOT_LOGGED_IN = 1004;

    public static final int SWIPE_ON_ICC_EXECPTION = 30006 ;

    // Card_Errors

    public static final int READER_NOT_FOUND = 200001;

    // sdk related reader reception.
    public static final int BLUETOOTH_OFF = 40001;
    public static final int NO_REGISTERED_CARD_READER = 40002;
    public static final int CARD_TIME_OUT = 40003 ;
    public static final int READER_ERROR = 40004;
    public static final int TX_CANCELD_MANUAL = 40005;
    public static final int CARD_NO_RESPONSE = 40006;
    public static final int CARD_NOT_ICC = 40007 ;
    public static final int CARD_NONE = 40008 ;
    public static final int Transaction_Declined = 40009 ;
    public static final int READER_NOT_DETECTED = 40010 ;
    public static final int BLUETOOTH_NOT_SUPPORTED = 40011;
    public static final int NO_READER_ASSIGNED = 40012;
    public static final int MAX_VALUE_EXCEED = 40013;

    public static final int INVALID_INVOICE = 50001;
    public static final int INVALID_USER_NAME = 50002;
    public static final int INVALID_PWD = 50003;
    public static final int INVALID_DEVELOPER_KEY = 50004;
    public static final int INVALID_DEVELOPER_TOKEN = 50005;
    public static final int INVALID_TRANSACTION_ID = 50006;
    public static final int INVALID_ENVIRONMENT = 50007;

    private String strException ;
    private int errCode ;

    private EnumException enumEx ;

    private int isState = 0 ;
    private String isMessage = null ;


    public PayableException(int code ,String msg){
        strException = msg ;
        errCode = code ;

        if(errCode == 30001){
            enumEx = EnumException.NO_RECORD_FOUND ;
        }

    }

    public int getErrcode(){
        return errCode ;
    }

    public String getMessage(){
        return strException ;
    }

    public EnumException getEnumException(){
        return enumEx ;
    }

    public String toString(){

        if(strException != null)
            return strException  ;

        return super.toString() + "  Err code:" + errCode ;

    }

    public int getIsState() {
        return isState;
    }

    public void setIsState(int isState) {
        this.isState = isState;
    }

    public String getIsMessage() {
        return isMessage;
    }

    public void setIsMessage(String isMessage) {
        this.isMessage = isMessage;
    }


}

