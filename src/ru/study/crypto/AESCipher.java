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
        return AES.encrypt(raw, key);
    }

    @Override
    public byte[] decrypt(byte[] crypted) {
        return AES.decrypt(crypted, key);
    }

    @Override
    public String getKeyString() {
        return "AES Key: " + DatatypeConverter.printHexBinary(key);
    }
}
