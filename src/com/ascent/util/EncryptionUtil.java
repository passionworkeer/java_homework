package com.ascent.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类，实现SHA-256密码加密和验证功能
 * @author ascent
 * @version 1.0
 */
public class EncryptionUtil {
    
    /**
     * 使用SHA-256算法对密码进行加密
     * @param password 原始密码
     * @return 加密后的密码（十六进制字符串）
     */
    public static String encryptPassword(String password) {
        try {
            // 获取SHA-256算法实例
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // 将密码转换为字节数组并更新摘要
            byte[] hashBytes = digest.digest(password.getBytes());
            
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // 如果加密失败，返回原始密码（不推荐，但避免系统崩溃）
        }
    }
    
    /**
     * 验证密码是否正确
     * @param inputPassword 用户输入的原始密码
     * @param storedPassword 存储的加密密码
     * @return 密码是否匹配
     */
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        // 对输入密码进行加密，然后与存储的加密密码比较
        String encryptedInput = encryptPassword(inputPassword);
        return encryptedInput.equals(storedPassword);
    }
}