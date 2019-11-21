package com.salescube.healthcare.demo.view;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductSecondary implements Parcelable {

    private String productName;
    private double targetValue;
    private double achValue;
    private int percent;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double getAchValue() {
        return achValue;
    }

    public void setAchValue(double achValue) {
        this.achValue = achValue;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public ProductSecondary() {


    }

    public ProductSecondary(Parcel in) {
        this.productName = in.readString();
        this.targetValue = in.readDouble();
        this.achValue = in.readDouble();
        this.percent = in.readInt();
    }

    public static final Creator CREATOR = new Creator() {

        public ProductSecondary createFromParcel(Parcel in) {
            return new ProductSecondary(in);
        }

        public ProductSecondary[] newArray(int size) {
            return new ProductSecondary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.productName);
        parcel.writeDouble(this.targetValue);
        parcel.writeDouble(this.achValue);
        parcel.writeInt(this.percent);
    }

}
