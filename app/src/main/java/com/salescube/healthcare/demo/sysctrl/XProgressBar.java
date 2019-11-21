package com.salescube.healthcare.demo.sysctrl;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.salescube.healthcare.demo.R;

public class XProgressBar extends Dialog {

    public static XProgressBar show(Context context, CharSequence title,
                                    CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public  XProgressBar show(Context context, CharSequence title,
                              CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static XProgressBar show(Context context, CharSequence title,
                                    CharSequence message, boolean indeterminate,
                                    boolean cancelable, OnCancelListener cancelListener) {
        XProgressBar dialog = new XProgressBar(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);

        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }


    public static XProgressBar show(XProgressBar dialog, Context context, CharSequence title,CharSequence message) {

        dialog.setTitle(title);
        dialog.setCancelable(true);

        dialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public XProgressBar(Context context) {
        super(context, R.style.XProgressBar);
    }
}
