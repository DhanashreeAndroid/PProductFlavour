package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 13/06/2017.
 */

public class MyPlace {

    public static final String TAG = MyPlace.class.getSimpleName();
    public final static String TABLE = "trn_my_place";

    public final static String COL_ID = "id";
    public final static String COL_USER_ID = "user_id";
    public final static String COL_PLACE_TYPE = "place_type";

    public final static String COL_PLACE_ID = "agent_id";
    public final static String COL_LAT = "lat";
    public final static String COL_LNG = "lng";
    public final static String COL_ADD = "address";

    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int userId;
    private String placeType;
    private int placeId;
    private String lat;
    private String lng;
    private String add;
    private Date createdDateTime;
    private boolean isPosted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
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
}
