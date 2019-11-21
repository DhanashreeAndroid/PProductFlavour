package com.salescube.healthcare.demo.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.model.Note;
import com.salescube.healthcare.demo.func.DateFunc;
import com.salescube.healthcare.demo.view.vNote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 12/11/2016.
 */

public class NoteRepo {

    public static String createTable() {
        String qry = "";

        qry = "CREATE TABLE " + Note.TABLE + "("
                + Note.COL_ID + " INT,"
                + Note.COL_SO_ID + " INT ,"
                + Note.COL_TR_DATE + " DATE ,"
                + Note.COL_NOTE_TYPE + " TEXT ,"
                + Note.COL_NOTE_CONTENT + " TEXT "
                + ")";

        return qry;
    }


    private void insert(Note target, SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put(Note.COL_SO_ID, target.getSoId());
        values.put(Note.COL_TR_DATE, DateFunc.getDateStr(target.getTrDate()));
        values.put(Note.COL_NOTE_TYPE, target.getNoteType());
        values.put(Note.COL_NOTE_CONTENT, target.getNoteContent());

        try {
            db.insert(Note.TABLE, null, values);
        } catch (Exception ex) {
            throw ex;
        }

    }

    private void insert(Note[] products, SQLiteDatabase db) {

        for (Note productRate : products) {
            insert(productRate, db);
        }
    }

    public void  deleteAll(int soId){

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        db.execSQL("DELETE FROM " + Note.TABLE + " WHERE so_id=" + soId + "") ;
        DatabaseManager.getInstance().closeDatabase();
    }

    public void insert(Note[] products) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        insert(products, db);
        DatabaseManager.getInstance().closeDatabase();
    }

    public List<vNote> getTodayNotes(int soId, Date todayDate) {

        Cursor c;
        String sql;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        sql = "";
        sql += " SELECT * ";
        sql += " FROM " + Note.TABLE + "";
        sql += " WHERE so_id =" + soId + "";
        sql += " AND   tr_date = '" + DateFunc.getDateStr(todayDate) + "'";

        try {
            c = db.rawQuery(sql, null);
        } catch (Exception e) {

            return null;
        }

        return fillObject(c);
    }


    private List<vNote> fillObject(Cursor c) {

        int ix_so_id = c.getColumnIndexOrThrow(Note.COL_SO_ID);
        int ix_tr_date = c.getColumnIndexOrThrow(Note.COL_TR_DATE);
        int ix_note_type = c.getColumnIndexOrThrow(Note.COL_NOTE_TYPE);
        int ix_note_content = c.getColumnIndexOrThrow(Note.COL_NOTE_CONTENT);

        vNote note = new vNote();
        List<vNote> notes = new ArrayList<>();

        while (c.moveToNext()) {

            note = new vNote();
            note.setSoId(c.getInt(ix_so_id));
            note.setTrDate(DateFunc.getDate(c.getString(ix_tr_date)));
            note.setNoteType(c.getString(ix_note_type));
            note.setNoteContent(c.getString(ix_note_content));
            notes.add(note);
        }

        return notes;
    }

}
