package com.salescube.healthcare.demo;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewParent;
import android.widget.EditText;

public abstract class XTextWatcher implements TextWatcher {

    private EditText editText;

    public XTextWatcher() {

    }

    public XTextWatcher(EditText editText) {
        this.editText = editText;
    }

    public EditText getEditTextView() {
        return this.editText;
    }

    public ViewParent getParentView() {
        return this.editText.getParent();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

