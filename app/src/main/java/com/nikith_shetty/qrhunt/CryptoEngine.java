package com.nikith_shetty.qrhunt;

import android.util.Log;

/**
 * Created by Nikith_Shetty on 16/01/2017.
 */

final class CryptoEngine {

    private final static String TAG = "CyptoEngine";
    private final static String ruler = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ~!@#$%&*()/\\\"?<>,':;+-_=^.";

    private CryptoEngine(){
    }

    static String encrypt(int key, String message) {
        //encryption algorithm
        Log.e(TAG, "Encryption started");
        String cipherText = "";
        String keyStr = "" + key;

        //convert to message into integer array
        int messageInt[] = new int[message.length()];
        for( int i=0; i<message.length(); i++){
            messageInt[i] = ruler.indexOf(message.charAt(i));
        }

        //convert key into integer array
        int keyInt[] = new int[message.length()];
        Log.e(TAG, "Length: " + message.length());
        for (int i = 0; i < message.length(); i++) {
            keyInt[i] = keyStr.charAt(i%keyStr.length()) - '0';
        }

        //modular clac for converting into cipherText
        int[] encryptedInt = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            encryptedInt[i] = (messageInt[i] + keyInt[i])%ruler.length();
        }

        //conversion into cipherText
        for (int i = 0; i < message.length(); i++) {
            cipherText = cipherText + ruler.charAt(encryptedInt[i]);
        }

        Log.e(TAG, "ruler.len : " + ruler.length());
        for (int i = 0; i < message.length(); i++) {
            Log.e(TAG, "Message array: " + message.charAt(i) + " @ " +
                    ruler.indexOf(message.charAt(i)) + " + " + keyInt[i] + " - " +
                    encryptedInt[i] + " @ " + cipherText.charAt(i));
        }

        Log.e(TAG, "Encryption finished: " + cipherText);
        return cipherText;
    }

    static String decrypt(int key, String cipherText) {
        //decryption algorithm
        Log.e(TAG, "Decryption started");
        String plainText = "";
        String keyStr = "" + key;

        //convert to message into integer array
        int messageInt[] = new int[cipherText.length()];
        for( int i=0; i<cipherText.length(); i++){
            messageInt[i] = ruler.indexOf(cipherText.charAt(i));
        }

        //convert key into integer array
        int keyInt[] = new int[cipherText.length()];
        for (int i = 0; i < cipherText.length(); i++) {
            keyInt[i] = keyStr.charAt(i%keyStr.length()) - '0';
        }

        //modular clac for converting into cipherText
        int[] encryptedInt = new int[cipherText.length()];
        for (int i = 0; i < cipherText.length(); i++) {
            encryptedInt[i] = (messageInt[i] - keyInt[i])%ruler.length();
            if(encryptedInt[i]<0)
                encryptedInt[i] = encryptedInt[i] + ruler.length();
        }

        //conversion into plainText
        for (int i = 0; i < cipherText.length(); i++) {
            plainText = plainText + ruler.charAt(encryptedInt[i]);
        }

        Log.e(TAG, "ruler.len : " + ruler.length());
        for (int i = 0; i < cipherText.length(); i++) {
            Log.e(TAG, "Message array: " + cipherText.charAt(i) + " @ " +
                    ruler.indexOf(cipherText.charAt(i)) + " - " + keyInt[i] + " - " +
                    encryptedInt[i] + " @ " + plainText.charAt(i));
        }

        Log.e(TAG, "Decryption finished: " + plainText);
        return plainText;
    }
}
