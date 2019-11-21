package com.salescube.healthcare.demo;

import android.app.Notification;

import com.salescube.healthcare.demo.data.model.ActivityLog;
import com.salescube.healthcare.demo.data.model.Agent;
import com.salescube.healthcare.demo.data.model.Area;
import com.salescube.healthcare.demo.data.model.AreaAgent;
import com.salescube.healthcare.demo.data.model.AttendanceReport;
import com.salescube.healthcare.demo.data.model.CompetitorInfo;
import com.salescube.healthcare.demo.data.model.Complaint;
import com.salescube.healthcare.demo.data.model.ComplaintType;
import com.salescube.healthcare.demo.data.model.Dashboard;
import com.salescube.healthcare.demo.data.model.DayWiseAchievementReport;
import com.salescube.healthcare.demo.data.model.Employee;
import com.salescube.healthcare.demo.data.model.Holiday;
import com.salescube.healthcare.demo.data.model.Installationlog;
import com.salescube.healthcare.demo.data.model.Leave;
import com.salescube.healthcare.demo.data.model.Locality;
import com.salescube.healthcare.demo.data.model.LocationLog;
import com.salescube.healthcare.demo.data.model.MyPlace;
import com.salescube.healthcare.demo.data.model.NoOrder;
import com.salescube.healthcare.demo.data.model.Note;
import com.salescube.healthcare.demo.data.model.NotificationModel;
import com.salescube.healthcare.demo.data.model.OtherWork;
import com.salescube.healthcare.demo.data.model.OtherWorkReason;
import com.salescube.healthcare.demo.data.model.POP;
import com.salescube.healthcare.demo.data.model.POPShop;
import com.salescube.healthcare.demo.data.model.Product;
import com.salescube.healthcare.demo.data.model.ProductRate;
import com.salescube.healthcare.demo.data.model.ProductWiseReport;
import com.salescube.healthcare.demo.data.model.Route;
import com.salescube.healthcare.demo.data.model.SOOtherWork;
import com.salescube.healthcare.demo.data.model.SS;
import com.salescube.healthcare.demo.data.model.SalesOrder;
import com.salescube.healthcare.demo.data.model.SalesOrderPrevious;
import com.salescube.healthcare.demo.data.model.SalesReturn;
import com.salescube.healthcare.demo.data.model.Shop;
import com.salescube.healthcare.demo.data.model.ShopStock;
import com.salescube.healthcare.demo.data.model.ShopType;
import com.salescube.healthcare.demo.data.model.ShopWiseOrder;
import com.salescube.healthcare.demo.data.model.SoAttendance;
import com.salescube.healthcare.demo.data.model.SysDate;
import com.salescube.healthcare.demo.data.model.Target;
import com.salescube.healthcare.demo.data.model.TourPlan;
import com.salescube.healthcare.demo.view.SoAnalytics;
import com.salescube.healthcare.demo.view.TourPlanResponse;
import com.salescube.healthcare.demo.view.vProductWiseSaleQty;
import com.salescube.healthcare.demo.view.vSOMonthAttendenceReport;
import com.salescube.healthcare.demo.view.vSOMonthPerformance;
import com.salescube.healthcare.demo.view.vSOMonthSalesProductQtyReport;
import com.salescube.healthcare.demo.view.vSOTodayShopOrder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @GET("Shop/getStock")
    Call<ShopStock[]> getShopStock(@Query("soId") int soId, @Query("appShopId") String appShopId, @Query("date") String date);

    @POST("Shop/updateStock")
    Call<Void> postShopStock(@Body List<ShopStock> dateList);

    @POST("User/UpdateFcmToken")
    Call<Void> postFcmToken(@Query("soId") int soId, @Query("token") String fcmToken);


    @POST("InstallationLog/log")
    Call<Void> postInstallationLog(@Body Installationlog log);

    @GET("AnalyticData/getSoAnalytics")
    Call<SoAnalytics> getAnalyticsData(@Query("soID") int soId);

    @GET("ManagerReport/getSOAttendance")
    Call<List<SoAttendance>> getAttendence(@Query("mgrId") int mgrId);

    @GET("ManagerReport/getSOShopOrder")
    Call<vSOTodayShopOrder> getSOShopOrder(@Query("mgrId") int i);

    @GET("Report/getProductReport")
    Call<List<ProductWiseReport>> getProductReport(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("Report/getMonthProductWiseAchievementReport")
    Call<List<ProductWiseReport>> getMonthProductWiseAchievementReport(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("Report/getTodayProductWiseAchievementReport")
    Call<List<ProductWiseReport>> getTodayProductWiseAchievementReport(@Query("soId") int i);

    @GET("Report/getMonthDayWiseAchievementReport")
    Call<List<DayWiseAchievementReport>> getMonthDayWiseAchievementReport(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("Report/getMonthShopWiseAchievementReport")
    Call<List<ShopWiseOrder>> getMonthShopWiseAchievementReport(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("Report/getDayWiseAchievementReport")
    Call<List<DayWiseAchievementReport>> getDayWiseAchievementReport(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate") String toDate);

    @GET("ManagerReport/getSOOtherWorkReport")
    Call<List<SOOtherWork>> getSOOtherWorkReport(@Query("soId") int soId);

    @GET("ManagerReport/getSOTodayProductSalesQTY")
    Call<vProductWiseSaleQty> getSOTodayProductSalesQTY(@Query("soId") int soId);

    @GET("ManagerReport/getMonthAttendenceReport")
    Call<vSOMonthAttendenceReport> getSOMonthAttendenceReport(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate")String toDate);

    @GET("ManagerReport/getSOMonthProductSalesQTY")
    Call<vSOMonthSalesProductQtyReport> getSOMonthProductSalesQTY(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate")String toDate);

    @GET("ManagerReport/getSOMonthPerformance")
    Call<vSOMonthPerformance> getSOMonthPerformance(@Query("soId") int i, @Query("fromDate") String fromDate, @Query("toDate")String toDate);

    @GET("Order/getTodayOrderSO")
    Call<SalesOrder[]> getTodayOrderSO(@Query("soId") int soId);

    @GET("Shop/getAll")
    Call<Shop []> getShops(@Query("soId") int soId);

    @GET("Product/getProductsBySo")
    Call<Product[]> getProductsbySo(@Query("soId") int soId);

    @GET("Product/getPriceList")
    Call<ProductRate[]> getPriceList(@Query("soId") int soId);

    @GET("Target/getTarget")
    Call<Target[]> getTargetList(@Query("soId") int soId, @Query("orderDate") String fromDate);

    @GET("Note/getNotes")
    Call<Note[]> getNotes(@Query("soId") int soId);

    @GET("Order/getLastOrdersBySo")
    Call< List<SalesOrderPrevious>> getLastOrdersBySo(@Query("soId") int soId);

    @GET("POP/getPOPListBySo")
    Call<POP[]> getPOPBySO(@Query("soId") int soId);

    @GET("ShopType/GetAllShopType")
    Call<ShopType[]> getShopType(@Query("soId") int soId);


    @GET("OtherWork/getWorkReasonList")
    Call<OtherWorkReason[]> getOtherWorkReason(@Query("soId") int soId);

    @GET("SS/getSS")
    Call<SS[]> getSS(@Query("mgrId") int soId);

    @GET("complaint/getComplaintType")
    Call<ComplaintType[]> getComplaintType(@Query("soId") int soId);

    @GET("Agent/getAllAgents")
    Call<Agent[]> getallList(@Query("soId") int soId);

    @GET("Area/getAreaList")
    Call<Area[]> getAreas(@Query("SoId") int soId);

    @GET("Route/getRoutes")
    Call<Route[]> getRoute(@Query("soId") int soId);

    @GET("Locality/getLocalality")
    Call<Locality[]> getLocality(@Query("soId") int soId);

    @GET("AreaAgent/getAreaAgent")
    Call<AreaAgent[]> getRLAreaAgent(@Query("soId") int soId);


    @GET("Employee/getSoByEmployee")
    Call<Employee[]> getSOByManagerId(@Query("mgrId") int soId);


    //Upload Attedence Mark

    @POST("Attendance/SaveAttendance")
    Call<Void> saveDayInDayOut(@Body SysDate dateList);

    @POST("Attendance/MarkAttendance")
    Call<Void> getAttendenceMark(@Body List<SysDate> dateList);

    //Upload SHop Update
    @POST("Shop/update")
    Call<Void> getShopUpdate(@Body List<Shop> dateList);

    //Upload order
    @POST("Order/postOrders")
    Call<Void> postOrders(@Body List<SalesOrder> dateList);

    //Upload order
    @POST("Order/postColdCalls")
    Call<Void> postColdCalls(@Body List<NoOrder> dateList);

    //Upload leave
    @POST("Leave/apply")
    Call<Void> apply(@Body List<Leave> dateList);

    //Upload OtherWork
    @POST("OtherWork/otherWork")
    Call<Void> postOtherWork(@Body List<OtherWork> dateList);

    //Upload ActivationLog
    @POST("ActivityLog/log")
    Call<Void> postActivityLog(@Body List<ActivityLog> dateList);

    //Upload MyPlace
    @POST("MyPlace/saveMyPlace")
    Call<Void> postMyPlace(@Body List<MyPlace> dateList);

    //Upload Location Log
    @POST("Location/log")
    Call<Void> postLocationLog(@Body List<LocationLog> dateList);

    //Upload SalesReturn
    @POST("Order/postSalesReturn")
    Call<Void> postSalesReturn(@Body List<SalesReturn> dateList);

    //Upload POP
    @POST("POP/PostPOP")
    Call<Void> postPOP(@Body POPShop dateList);

    @Multipart
    @POST("POP/PostPOPWithImage")
    Call<Void> postPOPWithImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody, @Part("POPShop") POPShop dateList);

    //Upload CompetitorInfo
    @POST("CompetitorInfo/PostInfo")
    Call<Void> postInfo(@Body CompetitorInfo dateList);

    @Multipart
    @POST("CompetitorInfo/PostInfoWithImage")
    Call<Void> postCompetitorWithImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody, @Part("CompetitorInfo") CompetitorInfo dateList);

    //Upload Complaint
    @POST("complaint/PostInfo")
    Call<Void> postComplaint(@Body Complaint dateList);

    @Multipart
    @POST("complaint/PostInfoWithImage")
    Call<Void> postComplaintWithImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody, @Part("Complaint") Complaint dateList);

    @GET("TourPlan/GetAll")
    Call<List<TourPlan>> getTourPlan(@Query("soId") int soId);

    @GET("TourPlan/GetMonthlyHolidays")
    Call<List<Holiday>> getMonthlyHolidays(@Query("firstdate") String firstDate);

    //Upload tourPlan
    @POST("TourPlan/PostTourPlan")
    Call<TourPlanResponse> postTourPlan(@Body List<TourPlan> tourPlan);


    // to get Notification from max id

    @GET("Notification/GetAll")
    Call<NotificationModel []> getNotificationList(@Query("soId") int soId, @Query("maxId") int maxId);


    @GET("Report/getDashboard")
    Call<Dashboard> getDashboard(@Query("soId") int soId);


}
