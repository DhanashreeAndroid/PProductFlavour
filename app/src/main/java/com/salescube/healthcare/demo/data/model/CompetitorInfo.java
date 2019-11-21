package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 04/11/2016.
 */

public class CompetitorInfo {

    public static final String TAG = Shop.class.getSimpleName();
    public final static String TABLE = "trn_competitor_info";

    public final static String COL_ID = "id";
    public final static String COL_TR_DATE = "tr_date";
    public final static String COL_SO_ID = "so_id";
    public final static String COL_AGENT_ID = "agent_id";
    public final static String COL_APP_SHOP_ID = "app_shop_id";
    public final static String COL_PRODUCT_ID = "product";
    public final static String COL_COMPETITOR_PRODUCT = "competitor_product";
    public final static String COL_GMS = "gms";
    public final static String COL_RETAILER_NET_RATE = "retailer_net_rate";
    public final static String COL_SCHEME = "scheme";
    public final static String COL_MRP = "mrp";
    public final static String COL_IMAGE_NAME = "image_name";
    public final static String COL_IMAGE_PATH = "image_path";
    public final static String COL_STOCK = "stock";
    public final static String COL_CREATED_DATE_TIME = "created_date_time";
    public final static String COL_IS_POSTED = "is_posted";

    private int id;
    private Date trDate;
    private int soId;
    private int agentId;
    private String appShopId;
    private int productId;
    private String competitorProduct;
    private String gms;
    private double retailerRate;
    private String scheme;
    private double mrp;
    private String imageName;
    private String imagePath;
    private String stock;
    private Date createdDateTime;
    private boolean isPosted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTrDate() {
        return trDate;
    }

    public void setTrDate(Date trDate) {
        this.trDate = trDate;
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getAppShopId() {
        return appShopId;
    }

    public void setAppShopId(String appShopId) {
        this.appShopId = appShopId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCompetitorProduct() {
        return competitorProduct;
    }

    public void setCompetitorProduct(String competitorProduct) {
        this.competitorProduct = competitorProduct;
    }

    public String getGms() {
        return gms;
    }

    public void setGms(String gms) {
        this.gms = gms;
    }

    public double getRetailerRate() {
        return retailerRate;
    }

    public void setRetailerRate(double retailerRate) {
        this.retailerRate = retailerRate;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
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
