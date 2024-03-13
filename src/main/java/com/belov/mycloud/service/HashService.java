package com.belov.mycloud.service;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashService {

    static boolean checkHash(Path fileForChecking, String trueHash) throws IOException, NoSuchAlgorithmException {
        String newHash = calculateMD5Hash(fileForChecking);
        return newHash.equals(trueHash);
    }

    static String calculateMD5Hash(Path fileForChecking) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (
                var bis = new BufferedInputStream(new FileInputStream(fileForChecking.toFile()));
                var dis = new DigestInputStream(bis, md)
        ) {
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        }
        // bytes to hex
        var result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
