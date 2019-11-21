package com.salescube.healthcare.demo.view;

/**
 * Created by user on 23/05/2017.
 */

public class vLookup {

    private String valueText;
    private String displayText;

    public String getValueText() {
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public vLookup() {
    }

    public vLookup(String _valueText, String _displayText) {
        this.valueText = _valueText;
        this.displayText = _displayText;
    }

    @Override
    public String toString() {
        return this.displayText;
    }
}
