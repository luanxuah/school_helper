package com.luanxu.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by xieshuai 2017/1/8 0008.
 */
public class AESUtils {

    private static final String AES = "AES";
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final String PARAMETERS_PASS = "luanxu";//参数密钥

    public static void main(String[] args) {
        String encrypt = encryptParameters("120706049");
        System.out.println(encrypt);
        System.out.println(System.currentTimeMillis());
        String decrypt = decryptParameters(encrypt);
        System.out.println(decrypt);
    }

    /**
     * 加密传输的参数生成秘文
     *
     * @param clearText 明文
     * @return 密文
     */
    public static String encryptParameters(String clearText) {
        return encrypt(clearText, PARAMETERS_PASS);
    }

    /**
     * 解密传输的参数生成明文
     *
     * @param cipherText 密文
     * @return 明文
     */
    public static String decryptParameters(String cipherText) {
        return decrypt(cipherText, PARAMETERS_PASS);
    }

    /**
     * AES加密
     *
     * @param content  需要加密的内容
     * @param password 密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String content, String password) {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);// 提供AES密钥算法的（对称）密钥生成器
            SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
            secureRandom.setSeed(password.getBytes());
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();// 生成一个密钥
            Cipher cipher = Cipher.getInstance(AES);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);// 初始化 加密模式
            byte[] result = cipher.doFinal(byteContent);
            return CommonUtils.parseByte2HexStr(result); // 加密 后的16进制
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidKeyException | UnsupportedEncodingException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     *
     * @param content  解密的String
     * @param password 密钥
     * @return 明文
     */
    public static String decrypt(String content, String password) {
        return decrypt(CommonUtils.parseHexStr2Byte(content), password);
    }

    /**
     * AES
     *
     * @param content  解密的byte[]
     * @param password 密钥
     * @return 明文
     */
    public static String decrypt(byte[] content, String password) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);// 提供AES密钥算法的（对称）密钥生成器
            SecureRandom secureRandom = SecureRandom.getInstance(SHA1PRNG);
            secureRandom.setSeed(password.getBytes());
            keyGenerator.init(128, secureRandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);
            Cipher cipher = Cipher.getInstance(AES);// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化 解密模式
            byte[] result = cipher.doFinal(content);// 解密为16进制
            return new String(result);// 将16进制转化为字符串 解密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

}
