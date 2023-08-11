package com.github.microservice.core.util.file;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class FileUtil {


    @SneakyThrows
    public static InputStream readStream(String fileName) {
        return new FileInputStream(new File(fileName));
    }

    @SneakyThrows
    public static InputStream readStream(File file) {
        return new FileInputStream(file);
    }

    @SneakyThrows
    public static String md5(File file) {
        @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
        return md5(fileInputStream);
    }

    @SneakyThrows
    public static String md5(InputStream inputStream) {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int read = 0;
        while ((read = inputStream.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        byte[] md5sum = digest.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        return bigInt.toString(16);
    }


}
