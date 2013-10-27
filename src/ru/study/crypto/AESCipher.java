package ru.study.crypto;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 10/14/13
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AESCipher implements iCipher {
    private byte[] key;

    public AESCipher() {
        key = new byte[32];
        new Random().nextBytes(key);
    }

    @Override
    public byte[] encrypt(byte[] raw) {
        byte[] res = AES.encrypt(raw, key);
        System.out.println("encrypt("+Arrays.toString(raw) + ") -> " + Arrays.toString(res));
        return res;
    }

    @Override
    public byte[] decrypt(byte[] crypted) {
        byte[] dec = AES.decrypt(crypted, key);
        System.out.println("decrypt("+Arrays.toString(crypted) + ") -> " + Arrays.toString(dec));
        return dec;
    }

    @Override
    public String getKeyString() {
        return "AES Key: "+DatatypeConverter.printHexBinary(key);
    }
}
