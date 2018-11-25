package common;

/**
 * Created by Dell on 6/29/2016.
 */
public interface PayableCallBack_RegisterCardReader {


    // Onsuccess, Failure, Broadcast notifiation - onscanning, scanfinish,

    public void onSuccessRegister(String res);

   // public void onFailureRegister(PayableException e);

    public void onFailureRegister(String res);

    public void onBluetoothTurnedOn(String res);

    public void onBluetoothTurnedOff(String res);
}
