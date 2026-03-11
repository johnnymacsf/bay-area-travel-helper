package com.hotelapp.backend.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class PasswordUtil {
    private static final SecureRandom random = new SecureRandom();

    public static String generateSalt(){
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return encodeHex(salt, 32);
    }

    public static String encodeHex(byte[] bytes, int length){
        BigInteger bigInteger = new BigInteger(1, bytes);
        return String.format("%0" + length + "X", bigInteger);
    }

    public static String hashPassword(String password, String salt){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update((salt+password).getBytes());
            return encodeHex(messageDigest.digest(), 64);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean isValid(String password){
        return password.length() >= 8 && password.matches(".*\\d.*") && password.matches(".*[^a-zA-Z0-9].*");
    }
}
