package com.salescube.healthcare.demo.data.model;

import java.io.Serializable;

public class MyMenu implements Serializable {

    private int menuId;
    private String menuName;
    private String iconPath;
    private String menuType;
    private int screenNo;
    private int rowNo;
    private int columnNo;
    private boolean visiable;
    private boolean enable;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public int getScreenNo() {
        return screenNo;
    }

    public void setScreenNo(int screenNo) {
        this.screenNo = screenNo;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(int columnNo) {
        this.columnNo = columnNo;
    }

    public boolean isVisiable() {
        return visiable;
    }

    public void setVisiable(boolean visiable) {
        this.visiable = visiable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
