package com.salescube.healthcare.demo.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.salescube.healthcare.demo.R;
import com.salescube.healthcare.demo.data.DatabaseManager;
import com.salescube.healthcare.demo.data.DbHelper;

import org.acra.ACRA;
import org.acra.BuildConfig;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraHttpSender;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.DialogConfigurationBuilder;
import org.acra.config.ToastConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;
import org.acra.config.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@AcraCore(
        buildConfigClass = BuildConfig.class,
        reportFormat = StringFormat.JSON
)
@AcraHttpSender(
        uri = "https://collector.tracepot.com/720ac08a",
        httpMethod = HttpSender.Method.POST
)




public class App extends Application {

    private static Context context;
    private static DbHelper dbHelper;

    private final AtomicInteger refCount = new AtomicInteger();
    private ScriptGroup.Binding binding;

    private boolean mBound;

    @Override
    public void onCreate() {
        super.onCreate();

       /* Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {

                handleUncaughtException(thread, throwable);
                Logger.log(Logger.DEBUG,"PSS","APP CRASH",throwable);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        });*/

        Logger.init("SMARTSALES")                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo(); //default AndroidLogAdapter

        context = this.getApplicationContext();
        dbHelper = new DbHelper(context);
        DatabaseManager.initializeInstance(dbHelper);

//        if (!isServiceIsRunning(UpdateService.class)){
//            Intent intent = new Intent(this, UpdateService.class);
//            startService(intent);
//        }
//
//        if (!isServiceIsRunning(LocationService.class)){
//            Intent intent = new Intent(this, LocationService.class);
//            startService(intent);
//        }

        // bind to service
        //Intent intent = new Intent(getApplicationContext(), MyService.class);
        //getApplicationContext().bindService(intent, mConnection,Context.BIND_AUTO_CREATE);
        //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setResIcon(R.drawable.icon_login);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setResTheme(R.style.AppTheme_NoActionBar);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setNegativeButtonText("");
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setResTitle(R.string.acra_title);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setResText(R.string.acra_toast_text);
        builder.getPluginConfigurationBuilder(DialogConfigurationBuilder.class).setEnabled(true);
        ACRA.init(this, builder);

    }

    public ScriptGroup.Binding acquireBinding() {
        refCount.incrementAndGet();
        return binding;
    }

    public void releaseBinding() {
        if (refCount.get() == 0 || refCount.decrementAndGet() == 0) {
            // Unbind from service
            if (mBound) {
                //unbindService(mConnection);
                mBound = false;
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            MyService.LocalBinder binder = (MyService.LocalBinder) service;
//            mService = binder.getService();
//            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void handleUncaughtException(Thread thread, Throwable e) {

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
        //
//            }
//        });

//        String cause = e.getCause().toString();
//
//        Intent intent = new Intent();
//        intent.setAction("com.dom.SEND_LOG");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("cause", cause);
//
//        startActivity(intent);

//        System.exit(1);

    }

    public static Context getContext() {
        return context;
    }

    private boolean isServiceIsRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(service.service.getClassName())){
                return  true;
            }
        }
        return false;
    }

}

