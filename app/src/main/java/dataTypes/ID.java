package dataTypes;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ID implements Serializable {

    //Code zu großen Teilen von https://www.baeldung.com/sha-256-hashing-java

    /**
     * creates a random hash String which serves as an id and returns that String
     * @return id
     */
    public static String getID() {
        Random r = new Random();
        long random1 = r.nextLong();
        long random2 = r.nextLong();
        long random3 = r.nextLong();
        String originalString = "" + random1 + "" + random2 +"" + random3;
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        byte[] encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    /**
     * takes in a byte array and transforms it into a hex String
     * @param hash byte array to turn to hex
     * @return hex String
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
