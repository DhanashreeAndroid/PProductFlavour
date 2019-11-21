package com.salescube.healthcare.demo.data.model;

import java.util.Date;

/**
 * Created by user on 12/11/2016.
 */

public class Note {

    public  static  final String TAG = Shop.class.getSimpleName();
    public static final String TABLE = "trn_note";

    public static final String COL_ID = "id";
    public static final String COL_SO_ID = "so_id";
    public static final String COL_TR_DATE = "tr_date";
    public static final String COL_NOTE_TYPE = "note_type";
    public static final String COL_NOTE_CONTENT = "note_content";

    private int id;
    private int soId;
    private Date trDate;
    private String noteType;
    private String noteContent;


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

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
}
