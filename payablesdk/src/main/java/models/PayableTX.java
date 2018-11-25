package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 12/8/2015.
 */
public class PayableTX {

    public static final int STATUS_OPEN = 1 ;
    public static final int STATUS_CLOSE = 2 ;

    public static final int STATUS_OPEN_VOID = 11 ;
    public static final int STATUS_CLOSE_VOID = 12 ;

    public static final int CARD_VISA = 1 ;
    public static final int CARD_AMEX = 2 ;
    public static final int CARD_MASTER = 3 ;

    public static final int SIGNATURE_NOT_AVAILABLE = 1 ;
    public static final int SIGNATURE_AVAILABLE = 2 ;




   // private long txId  = 0;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    private String txId  ;

    private String cardHolder ;

    private String ccLast4 ;

    private double amount = 0;

    private int cardType ;

    private Date time ;

    private int status ;

    private String approvalCode ;

    private Date voidTs ;

    private int sigFlag ;

    private List<PayableTX> items = new ArrayList<PayableTX>();



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

    public List<PayableTX> getItems() {
        return items;
    }

    public void setItems(List<PayableTX> items) {
        this.items = items;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }
}
