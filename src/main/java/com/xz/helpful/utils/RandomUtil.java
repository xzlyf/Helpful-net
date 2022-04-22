package com.xz.helpful.utils;

import java.util.Random;

/**
 * @Author: xz
 * @Date: 2022/4/22
 */
public class RandomUtil {
    public static String getRandomNum(int len) {
        int[] arry = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        StringBuilder buf = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            buf.append(arry[random.nextInt(arry.length)]);
        }
        return buf.toString();
    }

}
