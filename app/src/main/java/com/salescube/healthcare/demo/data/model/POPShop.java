package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 20/10/2016.
 */

public class POPShop {

    public  static  final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_pop_shop";

    public final static String COL_ID = "id";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_TR_DATE = "tr_date";
    public final static String COL_APP_SHOP_ID = "app_shop_id";
    public final static String COL_POP_ID = "pop_id";
    public final static String COL_POP_QTY = "pop_qty";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_IMAGE_NAME = "image_name";
    public final static String COL_IMAGE_PATH = "image_path";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private int soId;
    private Date trDate;
    private String appShopId;
    private int popId;
    private int popQty;
    private int agentId;
    private String imageName;
    private String imagePath;
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

    public Date getTrDate() {
        return trDate;
    }

    public void setTrDate(Date trDate) {
        this.trDate = trDate;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public int getPopId() {
        return popId;
    }

    public void setPopId(int popId) {
        this.popId = popId;
    }

    public int getPopQty() {
        return popQty;
    }

    public void setPopQty(int popQty) {
        this.popQty = popQty;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
