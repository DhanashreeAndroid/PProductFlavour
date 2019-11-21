package com.salescube.healthcare.demo.func;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.TrueTime.TrueTime;
import com.salescube.healthcare.demo.adapter.ISpinnerItemClick;
import com.salescube.healthcare.demo.adapter.SpinnerBaseAdapter;
import com.salescube.healthcare.demo.sysctrl.XProgressBar;
import com.salescube.healthcare.demo.view.vBaseSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.LOCATION_SERVICE;

//import com.pitambari.smartsales.app.App;

/**
 * Created by user on 09/09/2016.
 */

public class UtilityFunc {

    private static UtilityFunc instance = new UtilityFunc();
    private static Context context;
    public static final int GRACE_MINUTS = 5;
    static boolean isSuccess = false;
    private static XProgressBar dialog;

//    public static boolean IsConnected(){
//        String command = "ping -c 1 google.com";
//        try {
//            return (Runtime.getRuntime().exec(command).waitFor() == 0);
//        } catch (Exception ex){
//            return false;
//        }
//    }

//    public static boolean IsConnected2(){
//        String command = "ping -c 1 " + ApiManager.serverIp;
//        try {
//            return (Runtime.getRuntime().exec(command).waitFor() == 0);
//        } catch (Exception ex){
//            return false;
//        }
//    }

    public static void showDialog(Context context, String title, String message) {
        if (dialog == null) {
            dialog = new XProgressBar(context);
            dialog.show(dialog, context, title, message);
        }
    }

    public static void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean IsConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public static boolean isGPSEnabled(){
//
//        context = App.getContext();
//        LocationManager locationManager;
//        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//
//        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return isGpsEnabled;
//    }

    public static boolean isGPSEnabled(Context _context) {

        LocationManager locationManager;
        locationManager = (LocationManager) _context.getSystemService(LOCATION_SERVICE);

        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGpsEnabled;
    }

    public static boolean isGPSEnabled(boolean prompt, final Context context) {

        if (isAirplaneModeOn(context)) {

            if (prompt) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Airplane mode?");
                alert.setCancelable(false);
                alert.setMessage("Your mobile on airplane mode, please deactivate airplane mode.");

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.show();
            }

            return false;
        }

        if (UtilityFunc.isGPSEnabled(context)) {
            return true;
        }

        if (prompt) {

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("GPS required!");
            alert.setCancelable(false);
            alert.setMessage("Your GPS seems to be disabled, do you want to enable it?");

            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent locSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(locSetting);
                    //context.startActivityForResult(locSetting , 1);
                }
            });

            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "Please enable GPS, is required to proceed.", Toast.LENGTH_SHORT).show();
                }
            });

            alert.show();
        }

        return false;
    }

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;

    }


    public static GradientDrawable getRectangleBorder(int solidColor, int strokColor, int strokWidth, int radius) {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, //set a gradient direction
                new int[]{solidColor, solidColor});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setStroke(strokWidth, strokColor);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }

    public static GradientDrawable getCircularBorder(int solidColor, int strokColor, int strokWidth) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(solidColor);
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(strokWidth, strokColor);
        return gradientDrawable;
    }

    public static void setSelectorRoundedCorner(Context context, View v, int Stroke, int PrimarySolidColor,
                                                int PressedSolidColor, int PrimaryBorderColor, int PressedBOrderColor,
                                                int radius) {
        StateListDrawable states = new StateListDrawable();

        states.addState(new int[]{android.R.attr.state_pressed}, getRectangleBorder(context.getResources()
                        .getColor(PressedSolidColor), radius, Stroke,
                context.getResources().getColor(PressedBOrderColor)));
        states.addState(new int[]{}, getRectangleBorder(context.getResources().getColor(
                PrimarySolidColor), radius, Stroke,
                context.getResources().getColor(PrimaryBorderColor)));
        v.setBackgroundDrawable(states);

    }

    public static String getTourPlanId(int soId) {
        String id;
        long time = System.currentTimeMillis();
        String timeStamp = String.valueOf(time).substring(String.valueOf(time).length() - 4);
        id = String.valueOf(soId) + new SimpleDateFormat("ddMMyy").format(new Date()) + timeStamp;
        return id;
    }

    public static void showSpinnerDialog(final Context context, final TextView tvData,
                                         final ArrayList<vBaseSpinner> list, final String label,
                                         final ISpinnerItemClick listener) {

        final Dialog dialog1 = new Dialog(context, R.style.CustomDialogTheme);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.city_route_list_dialog);

        dialog1.setCanceledOnTouchOutside(false);
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources()
                .getColor(android.R.color.transparent)));
        dialog1.show();
        LinearLayout container = (LinearLayout) dialog1.findViewById(R.id.list_container);
        Button btnClose = dialog1.findViewById(R.id.btnClose);
        TextView tvLabel = dialog1.findViewById(R.id.tvLabel);
        tvLabel.setText("Select " + label);
        container.setBackgroundDrawable(UtilityFunc.getRectangleBorder(Color.WHITE,
                Color.WHITE, 1, 0));

        final android.support.v7.widget.SearchView searchView = dialog1.findViewById(R.id.searchView);
        final RecyclerView recyclerView = dialog1.findViewById(R.id.recyclerView);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        setOnFocusChangeListener(context, searchView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        final SpinnerBaseAdapter adapter = new SpinnerBaseAdapter(context, label, new ISpinnerItemClick() {
            @Override
            public void onSpinnerItemClick(Object obj, String label) {
                if (obj != null) {
                    tvData.setText(((vBaseSpinner) obj).getName());
                    tvData.setTag(((vBaseSpinner) obj).getId());
                } else {
                    tvData.setText(label);
                    tvData.setTag(-1);
                }
                dialog1.dismiss();
                tvData.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_border));
                listener.onSpinnerItemClick(obj, label);

            }
        });

        recyclerView.setAdapter(adapter);
        adapter.setData(list);

        if (list.size() > 12) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = (int) context.getResources().getDimension(R.dimen.spinner_height);
            recyclerView.setLayoutParams(params);
        }

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                tvData.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_border));
            }
        });
    }

    public static void setOnFocusChangeListener(final Context context, final View view) {

        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    view.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_border));
                } else {
                    view.setBackground(context.getResources().getDrawable(R.drawable.bg_bottom_border_selected));
                }
            }
        });
    }

    public static boolean isAllowedTourPlan(Context context, String date) {

        boolean isAllowed = false;

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_MONTH, 1);
        String minDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());
        c1.set(Calendar.DAY_OF_MONTH, c1.getActualMaximum(Calendar.DAY_OF_MONTH));
        boolean isDateLimitApplied = context.getResources().getBoolean(R.bool.isTourPlanDateLimitApplied);
        if (isDateLimitApplied) {
            c1.set(Calendar.DAY_OF_MONTH, context.getResources().getInteger(R.integer.tourPlan_allowed_upTo));
        }
        String maxDate = new SimpleDateFormat("yyyy-MM-dd").format(c1.getTime());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date min = sdf.parse(minDate);
            Date max = sdf.parse(maxDate);
            Date myDate = sdf.parse(today);
            Date tourDate = sdf.parse(date);

            //first todays date check with date limitation
            if ((myDate.compareTo(min) > 0 || myDate.compareTo(min) == 0)
                    && (myDate.compareTo(max) < 0 || myDate.compareTo(max) == 0)) {
                isAllowed = true;
            } else {
                isAllowed = false;
            }

           /* if (myDate.compareTo(min) > 0)  {
                System.out.println("Date1 is after Date2");
            } else if (myDate.compareTo(min) < 0) {
                System.out.println("Date1 is before Date2");
            } else if (myDate.compareTo(min) == 0) {
                System.out.println("Date1 is equal to Date2");
            } else {
                System.out.println("How to get here?");
            }*/
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(tourDate);
            if (!isAllowed) {
                //now compare the dates using methods on Calendar
                if (c1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    if (cal2.get(Calendar.MONTH) > c1.get(Calendar.MONTH)) {
                        // next month tourplan allowed
                        isAllowed = true;
                    } else if (cal2.get(Calendar.MONTH) < c1.get(Calendar.MONTH)) {
                        // previous month tourplan locked
                        isAllowed = false;
                    } else {
                        //all back date from today goes block
                        if (tourDate.compareTo(myDate) < 0) {
                            isAllowed = false;
                        } else {
                            isAllowed = true;
                        }
                    }
                }
            } else {
                if (c1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                    if (cal2.get(Calendar.MONTH) < c1.get(Calendar.MONTH)) {
                        // previous month tourplan locked even date is allowed
                        isAllowed = false;
                    }
                }
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return isAllowed;
    }

    public static void setOnOffState(View view, boolean enabled) {
        if (enabled) {
            view.setEnabled(true);
        } else {
            view.setEnabled(false);
        }

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public static boolean isValidDateTime(Context context) {
        if (!TrueTime.isInitialized()) {
            isSuccess = initTrueTime(context);
            if(isSuccess){
                Date trueTime = TrueTime.now();
                Date deviceTime = new Date();
                float diff = (trueTime.getTime() - deviceTime.getTime()) / 1000F;
                if (diff < 0) {
                    diff = -(diff);
                }
                if (diff > (GRACE_MINUTS * 60)) {
                    isSuccess = false;
                }else {
                    isSuccess = true;
                }
            }
        } else {
            Date trueTime = TrueTime.now();
            Date deviceTime = new Date();
            float diff = (trueTime.getTime() - deviceTime.getTime()) / 1000F;
            if (diff < 0) {
                diff = -(diff);
            }
            if (diff > (GRACE_MINUTS * 60)) {
                isSuccess = false;
            }else {
                isSuccess = true;
            }
        }
        return isSuccess;
    }


    public static String formatDate(Date date) {
        String pattern = "yyyy-MM-dd hh:mm:ss a";
        TimeZone timeZone = TimeZone.getTimeZone("GMT+05:30");
        DateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        format.setTimeZone(timeZone);
        return format.format(date);
    }

    private static boolean initTrueTime(Context context) {
        try {
            boolean get = new InitTrueTimeAsyncTask(context).execute().get();
            return get;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class InitTrueTimeAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Context context;

        InitTrueTimeAsyncTask(Context context) {
            this.context = context;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                TrueTime.build()
                        //.withSharedPreferences(SampleActivity.this)
                        .withNtpHost("time.google.com")
                        .withLoggingEnabled(false)
                        .withSharedPreferencesCache(context)
                        .withConnectionTimeout(3_1428)
                        .initialize();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Time", "something went wrong when trying to initialize TrueTime " + e.getMessage().toString(), e);
                return false;
            }
        }
    }



    public static class DatabasePullTask extends AsyncTask<Void, Void, Boolean> {
        Context context;

        public DatabasePullTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String dbName = "OOopjc.db";
            //String dbName ="roboasses.db";
            // String dbName = "student.db";
            //String dbName = "CBSE-X_V2.db";
            File dbFile = context.getApplicationContext().getDatabasePath(dbName);
            if (dbFile.exists()) {
                try {
                    File internalDbPath = new File(context.getExternalFilesDir(null), "internalDB");
                    if (!internalDbPath.exists()) {
                        internalDbPath.mkdir();
                    }
                    String destinationPath = internalDbPath.getAbsolutePath() + File.separator + dbName;
                    return copyFileFromSourceToDestn(dbFile.getAbsolutePath(), destinationPath, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public static boolean copyFileFromSourceToDestn(String sPath, String dPath, boolean isDeleteSourceDir) {
        try {
            FileInputStream fis = new FileInputStream(new File(sPath));
            File dir = new File(dPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outfile = new File(dPath);
            outfile.delete();
            if (!outfile.exists()) {
                outfile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outfile);
            byte[] block = new byte[1000];
            int i;
            while ((i = fis.read(block)) != -1) {
                fos.write(block, 0, i);
            }
            fos.close();
            if (isDeleteSourceDir) {
                deleteDir(new File(sPath));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
            return dir.delete();
        } else {
            if (dir.exists()) {
                if (dir.delete()) {
                    Log.d("SAN", "file Deleted :" + dir.getAbsolutePath());
                    return true;
                } else {
                    Log.d("SAN", "file note Deleted :" + dir.getAbsolutePath());
                    return false;
                }
            }
            return false;
        }
        // The directory is now empty so delete it

    }

    public static String getAppVersion(Context context) {
        String currentVersion = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;
        return currentVersion;
    }

    public static String getAppVersionCode(Context context) {
        String currentVersionCode = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        currentVersionCode = "" + pInfo.versionCode;
        return currentVersionCode;
    }
}
