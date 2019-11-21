package com.salescube.healthcare.demo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.data.model.Agent;
import com.salescube.healthcare.demo.data.model.AgentLocality;
import com.salescube.healthcare.demo.data.model.CompetitorInfo;
import com.salescube.healthcare.demo.data.model.Complaint;
import com.salescube.healthcare.demo.data.model.ComplaintType;
import com.salescube.healthcare.demo.data.model.Leave;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.model.MyPlace;
import com.salescube.healthcare.demo.data.model.NoOrder;
import com.salescube.healthcare.demo.data.model.Note;
import com.salescube.healthcare.demo.data.model.OtherWork;
import com.salescube.healthcare.demo.data.model.OtherWorkReason;
import com.salescube.healthcare.demo.data.model.POP;
import com.salescube.healthcare.demo.data.model.POPShop;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.ProductRate;
import com.salescube.healthcare.demo.data.model.ProductTarget;
import com.salescube.healthcare.demo.data.model.SS;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.SalesOrderPrevious;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.model.ShopType;
import com.salescube.healthcare.demo.data.model.Stock;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.model.Target;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.data.model.TourPlanDetail;
import com.salescube.healthcare.demo.data.model.User;
import com.salescube.healthcare.demo.data.repo.ActivityLogRepo;
import com.salescube.healthcare.demo.data.repo.AgentLocalityRepo;
import com.salescube.healthcare.demo.data.repo.AgentRepo;
import com.salescube.healthcare.demo.data.repo.AreaAgentRepo;
import com.salescube.healthcare.demo.data.repo.AreaRepo;
import com.salescube.healthcare.demo.data.repo.AttendanceRepo;
import com.salescube.healthcare.demo.data.repo.ColdCallRepo;
import com.salescube.healthcare.demo.data.repo.CompetitorInfoRepo;
import com.salescube.healthcare.demo.data.repo.ComplaintRepo;
import com.salescube.healthcare.demo.data.repo.ComplaintTypeRepo;
import com.salescube.healthcare.demo.data.repo.EmployeeRepo;
import com.salescube.healthcare.demo.data.repo.LeaveRepo;
import com.salescube.healthcare.demo.data.repo.LocalityRepo;
import com.salescube.healthcare.demo.data.repo.LocationLogRepo;
import com.salescube.healthcare.demo.data.repo.MyPlaceRepo;
import com.salescube.healthcare.demo.data.repo.NoteRepo;
import com.salescube.healthcare.demo.data.repo.NotificationRepo;
import com.salescube.healthcare.demo.data.repo.OtherWorkReasonRepo;
import com.salescube.healthcare.demo.data.repo.OtherWorkRepo;
import com.salescube.healthcare.demo.data.repo.POPRepo;
import com.salescube.healthcare.demo.data.repo.POPShopRepo;
import com.salescube.healthcare.demo.data.repo.ProductRateRepo;
import com.salescube.healthcare.demo.data.repo.ProductRepo;
import com.salescube.healthcare.demo.data.repo.ProductTargetRepo;
import com.salescube.healthcare.demo.data.repo.RouteRepo;
import com.salescube.healthcare.demo.data.repo.SSRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderPreviousRepo;
import com.salescube.healthcare.demo.data.repo.SalesOrderRepo;
import com.salescube.healthcare.demo.data.repo.SalesReturnRepo;
import com.salescube.healthcare.demo.data.repo.ShopRepo;
import com.salescube.healthcare.demo.data.repo.ShopTypeRepo;
import com.salescube.healthcare.demo.data.repo.ShopWiseOrderRepo;
import com.salescube.healthcare.demo.data.repo.StockRepo;
import com.salescube.healthcare.demo.data.repo.SysDateRepo;
import com.salescube.healthcare.demo.data.repo.TargetRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanDetailRepo;
import com.salescube.healthcare.demo.data.repo.TourPlanRepo;
import com.salescube.healthcare.demo.data.repo.UserRepo;
import com.salescube.healthcare.demo.data.repo.vDayreportRepo;

/**
 * Created by user on 19/09/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String TAG = DbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "OOopjc.db";

    public DbHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            createTables(db);
        } catch (Exception e) {
            Logger.log(Logger.ERROR, "DbHelper", e.getMessage(), e);
        }

    }

    private void createTables(SQLiteDatabase db) {

        // MASTER
        db.execSQL(UserRepo.createTable());
        db.execSQL(AgentLocalityRepo.createTable());
        db.execSQL(ShopRepo.createTable());
        db.execSQL(ProductRepo.createTable());
        db.execSQL(OtherWorkReasonRepo.createTable());
        db.execSQL(POPRepo.createTable());
        db.execSQL(ShopTypeRepo.createTable());
        db.execSQL(SSRepo.createTable());

        db.execSQL(StockRepo.createTable());
        db.execSQL(vDayreportRepo.createTable());



        // TRANSACTION
        db.execSQL(SysDateRepo.createTable());
        db.execSQL(SalesOrderRepo.createTable());
        db.execSQL(ProductRateRepo.createTable());
        db.execSQL(TargetRepo.createTable());
        db.execSQL(SalesOrderPreviousRepo.createTable());
        db.execSQL(ColdCallRepo.createTable());
        db.execSQL(POPShopRepo.createTable());
        db.execSQL(LocationLogRepo.createTable());
        db.execSQL(OtherWorkRepo.createTable());
        db.execSQL(LeaveRepo.createTable());
        db.execSQL(CompetitorInfoRepo.createTable());
        db.execSQL(NoteRepo.createTable());
        db.execSQL(ActivityLogRepo.createTable());
        db.execSQL(MyPlaceRepo.createTable());

        db.execSQL(ComplaintTypeRepo.createTable());
        db.execSQL(ComplaintRepo.createTable());
        db.execSQL(ProductTargetRepo.createTable());

        db.execSQL(SalesReturnRepo.createTable());

        db.execSQL(AgentRepo.createTable());
        db.execSQL(AreaRepo.createTable());
        db.execSQL(RouteRepo.createTable());
        db.execSQL(LocalityRepo.createTable());
        db.execSQL(AreaAgentRepo.createTable());


        // ASM
        db.execSQL(EmployeeRepo.createTable());
        db.execSQL(AttendanceRepo.createTable());


        db.execSQL(ShopWiseOrderRepo.createTable());


        db.execSQL(TourPlanRepo.createTable());
        db.execSQL(TourPlanDetailRepo.createTable());

       // db.execSQL(NotificationRepo.createTable());

        insertWorkReasons(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (newVersion) {
            case 2:
                version_2(db);
            case 3:
                version_3(db);
        }
    }

    private void version_3(SQLiteDatabase db){

        if(!isTableExists(TourPlan.TABLE,  db)){
            db.execSQL(TourPlanRepo.createTable());
        }else{
            insertColumn(db, TourPlan.TABLE, "is_cancelled", "INT DEFAULT 0");
        }
        if(!isTableExists(TourPlanDetail.TABLE,  db)){
            db.execSQL(TourPlanDetailRepo.createTable());
        }

    }

    public boolean isTableExists(String tableName, SQLiteDatabase mDatabase ) {

        Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }


    private  void version_2(SQLiteDatabase db) {
        insertColumn(db, TourPlan.TABLE, "is_cancelled", "INT DEFAULT 0");
    }

    private void insertColumn(SQLiteDatabase db, String tableName, String columnName, String columnType) {

        String qry = "";

        try {
            qry = "SELECT " + columnName + " FROM " + tableName + ";";
            db.rawQuery(qry, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType + ";");
        }
    }

    public boolean isColumnExists(SQLiteDatabase sqliteDatabase,
                                  String tableName,
                                  String columnToFind) {
        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(
                    "PRAGMA table_info(" + tableName + ")",
                    null
            );

            int nameColumnIndex = cursor.getColumnIndexOrThrow("name");

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameColumnIndex);

                if (name.equals(columnToFind)) {
                    return true;
                }
            }

            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void dropTables(SQLiteDatabase db) {

        // MASTER
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AgentLocality.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Shop.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Product.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OtherWorkReason.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + POP.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ShopType.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SS.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Stock.TABLE);

        // TRANSACTION
        db.execSQL("DROP TABLE IF EXISTS " + SysDate.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SalesOrder.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ProductRate.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Target.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SalesOrderPrevious.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NoOrder.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + POPShop.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LocationLog.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + OtherWork.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Leave.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CompetitorInfo.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ActivityLog.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MyPlace.TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + ComplaintType.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Complaint.TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + ProductTarget.TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + Agent.TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TourPlan.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TourPlanDetail.TABLE);

    }

    private void insertWorkReasons(SQLiteDatabase db) {

        OtherWorkReasonRepo repo = new OtherWorkReasonRepo();

        repo.insert(new OtherWorkReason(1, "Other"), db);
        repo.insert(new OtherWorkReason(2, "Training"), db);
        repo.insert(new OtherWorkReason(3, "Launching"), db);
        repo.insert(new OtherWorkReason(4, "Exhibition"), db);
        repo.insert(new OtherWorkReason(5, "Weekly Meeting"), db);
        repo.insert(new OtherWorkReason(6, "H.O.Meeting"), db);
        repo.insert(new OtherWorkReason(7, "Agent Visit"), db);
        repo.insert(new OtherWorkReason(8, "SS Visit"), db);
        repo.insert(new OtherWorkReason(9, "Joint Work with SO"), db);

    }

}
