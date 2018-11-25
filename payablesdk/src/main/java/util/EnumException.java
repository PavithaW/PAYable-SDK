package util;

/**
 * Created by Dell on 12/10/2015.
 */
public enum EnumException {

    INTERNAL_ERROR(100, "internal error"),
    PROCESS_BLOCKED(100, "Couldn't complete the request"),
    INVALID_USER_AGENT(10000, "User agent is not valid"),
    INVALID_AUTHENTICATION(10001, "Invalid authentication"),

    USER_NAME_EXIST(10002, "Username already exist"),
    MOBILE_NO_EXIST(10003, "Mobile number already exist"),
    SIM_ID_EXIST(10004, "Sim Id already exist"),
    DEVICE_ID_EXIST(10005, "Device Id already exist"),
    CR_ID_EXIST(10006, "Card Reader already exist"),
    EMAIL_ID_EXIST(10007, "Email Id already exist"),

    INVALID_VERIFICATION_CODE(10008, "Invalid verification code"),
    ACCESS_DENIED(10009, "User is blocked to access the system"),
    INVALID_REQUEST(10010, "Invalid request"),
    INVALID_BANKRECORD(10011, "Invalid Bank record"),
    BANK_ACCESS_BLOCKED(10012, "Merchant bank account is denied"),

    NO_RECORD_FOUND(30001, "No record found"),

    TX_CLOSED(20001, "Transaction already closed"),
    TX_VOIDED(20002, "Transaction already voided");

    // 100000 - UNIQUE //

    private int m_code;
    private String m_description;

    EnumException(int code, String des) {
        m_code = code;
        m_description = des;
    }

    public int getCode() {
        return m_code;
    }

    public String getDescription() {
        return m_description;
    }

}
