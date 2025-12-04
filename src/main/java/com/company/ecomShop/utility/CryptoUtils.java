package com.company.ecomShop.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoUtils {
    /**
     * Cifra una stringa applicando XOR con il carattere 'a'
     * (equivalente alla funzione Cripta di Delphi).
     *
     * @param plainText testo in chiaro da criptare
     * @return stringa criptata
     */
    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            char c = plainText.charAt(i);
            char encryptedChar = (char) (c ^ 'a'); // XOR con 'a' (come in Delphi)
            result.append(encryptedChar);
        }

        return result.toString();
    }
}