package ru.study.crypto;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 10/14/13
 * Time: 4:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface iCipher {
    public byte[] encrypt(byte[] raw);
    public byte[] decrypt(byte[] crypted);
    public byte[] getKey();
}
