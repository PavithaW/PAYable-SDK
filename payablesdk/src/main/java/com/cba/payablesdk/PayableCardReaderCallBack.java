package com.cba.payablesdk;

/**
 * Created by Dell on 12/23/2016.
 */

public interface PayableCardReaderCallBack {

    public static final int STATUS_READER_CONNECTED = 1001 ;
    public static final int WAITING_FOR_CARD = 1002;
    public static final int CARD_BAD_SWIPE = 1003;
    public static final int CHIP_UPDATE = 1004;

    public void cardReaderStatus(int status , String message) ;
}
