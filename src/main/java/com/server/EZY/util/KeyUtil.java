package com.server.EZY.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class KeyUtil {

    protected int size;

    public String getKey(int size) {
        this.size = size;
        return getAuthCode();
    }

    //Generate Authentication Code for RandomNumbers
    private String getAuthCode() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while(buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        return buffer.toString();
    }
}