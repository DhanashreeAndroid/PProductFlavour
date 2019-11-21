package com.salescube.healthcare.demo.func;

import android.content.Context;

import com.salescube.healthcare.demo.R;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class RetrofitException {
    private static String message;

    public static String msg(Context context, Throwable t) {
        if(t instanceof IOException){
            message = t.toString();
        }
        if (t instanceof SocketTimeoutException) {
            message = context.getString(R.string.connection_timeout);
        } else if (t instanceof ConnectException) {
            message = context.getString(R.string.no_connection);
        } else {
            message = context.getString(R.string.unknown_error);
        }
        return message;
    }
}
