package com.luanxu.utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xieshuai 2017/1/5 0005.
 */
public class MD5Utils {
    /*public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        String a = getMD5String("这就是爱");
        System.out.println(a);
        System.out.println(System.currentTimeMillis() - t1 + "ms");
        String b = getMD5String("这就是爱");
        System.out.println(b);
        System.out.println(a.equals(b));
    }*/

    public static String getMD5String(String s) {
        char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = s.getBytes();
        //获取MD5摘要算法的MessageDigest
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

}
