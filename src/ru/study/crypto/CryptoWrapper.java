package ru.study.crypto;


import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 10/14/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class CryptoWrapper {
    private iCipher cipher;

    public CryptoWrapper(iCipher cipher) {
        this.cipher = cipher;
    }

    //@return hex string
    public byte[] encrypt(String s, boolean isHex) {
        return cipher.encrypt(isHex ? toBytes(s) : s.getBytes());
    }

    //@return hex string
    public String encrypt(File f) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        byte[] b = new byte[(int) raf.length()];
        raf.read(b);
        return toHex(cipher.encrypt(b));
    }

    public byte[] decrypt(String s, boolean isHex) {
        return cipher.decrypt(isHex ? toBytes(s) : s.getBytes());
    }

    public byte[] decrypt(File f, boolean isHex) throws IOException {
        byte[] b = readBytesFromFile(f);
        System.out.println("Readed bytes: " + Arrays.toString(b));
        return isHex ? cipher.decrypt(toBytes(new String(b))) : cipher.decrypt(b);
    }

    public String toHex(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public byte[] toBytes(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public String getKey() {
        return toHex(cipher.getKey());
    }

    private byte[] readBytesFromFile(File f) throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(f), "UTF8"));

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString().getBytes();
    }
}
