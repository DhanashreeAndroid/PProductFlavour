package com.salescube.healthcare.demo.func;

import android.text.TextUtils;

/**
 * Created by user on 10/11/2016.
 */

public class TextFunc {

    // remove blank space
    public static boolean isEmpty(CharSequence str) {

        if (str != null) {
            return TextUtils.getTrimmedLength(str) <= 0;
        }

        return true;
    }

    public static boolean isEqual(String value1, String value2){
        return value1.trim().toLowerCase().equals(value2.toLowerCase().trim());
    }
}
