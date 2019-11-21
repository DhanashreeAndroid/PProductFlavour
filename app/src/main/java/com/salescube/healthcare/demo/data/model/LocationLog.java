package com.salescube.healthcare.demo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by user on 26/10/2016.
 */

public class LocationLog implements Parcelable {

    public  static  final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_location_log";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_IMEI = "imei";
    public final static String COL_APP_SHOP_ID = "app_shop_id";
    public final static String COL_TXN_DATE = "txn_date";
    public final static String COL_EVENT_TAG = "event_tag";
    public final static String COL_LATITUDE = "lattitude";
    public final static String COL_LONGITUDE = "longitude";
    public final static String COL_ADDRESS = "address";
    public final static String COL_EXTRA_INFO = "extra_info";
    public final static String COL_NETWORK = "network";
    public final static String COL_BATTERY = "battery";
    public final static String COL_DEVICE_MODEL = "device_model";
    public final static String COL_OS_VERSION = "os_version";
    public final static String COL_APP_VERSION = "app_version";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private String imei;
    private String appShopId;
    private Date txnDate;
    private String eventTag;
    private String latitude;
    private String Longitude;
    private String address;
    private String extraInfo;
    private String network;
    private String battery;
    private String deviceModel;
    private String osVersion;
    private String appVersion;
    private Date createdDateTime;
    private boolean isPosted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public Date getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Date txtDate) {
        this.txnDate = txtDate;
    }

    public String getEventTag() {
        return eventTag;
    }

    public void setEvent(String event) {
        this.eventTag = event;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public boolean isPosted() {
        return isPosted;
    }

    public void setPosted(boolean posted) {
        isPosted = posted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // USED FOR BROADCASTING
    // DO NOT CHANGE ORDER

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(soId);
        dest.writeString(imei);
        dest.writeString(appShopId);
        dest.writeString(eventTag);
    }

    public LocationLog() {

    }

    // DO NOT CHANGE ORDER
    public LocationLog(Parcel pc) {
        soId = pc.readInt();
        imei = pc.readString();
        appShopId = pc.readString();
        eventTag = pc.readString();
    }

    public static final Creator<LocationLog> CREATOR = new Creator<LocationLog>() {

        @Override
        public LocationLog createFromParcel(Parcel source) {
            return new LocationLog(source);
        }

        @Override
        public LocationLog[] newArray(int size) {
            return new LocationLog[size];
        }
    };

}
