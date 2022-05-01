package com.xz.helpful.utils;

import java.util.ArrayList;
import java.util.List;


public class ConvertUtil {
    /**
     * Object 转 List<T>对象
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
