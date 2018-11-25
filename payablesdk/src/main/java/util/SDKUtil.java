package util;

import android.content.Context;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Dell on 12/1/2015.
 */
public class SDKUtil {

    private static SecureRandom random ;

    static {
        random = new SecureRandom() ;
    }

    public static long generateLongToken(){
        return new BigInteger(64,random).longValue();
    }

    public static String generateMD5(String s) throws NoSuchAlgorithmException {

        MessageDigest digest = null;

        digest = MessageDigest.getInstance("MD5");

        digest.reset();
        byte[] data = digest.digest(s.getBytes());
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));


    }


}
