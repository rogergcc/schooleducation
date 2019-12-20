package com.appsnipp.schooleducation.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Layout;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentActivity;

import com.appsnipp.schooleducation.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.text.format.Time.TIMEZONE_UTC;

public class Utils {

//    public static void initializeSSLContext(Context mContext){
//        try {
//            SSLContext.getInstance("TLSv1.2");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For software3000 columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {

        removeBadge(bottomNavigationView, itemId);

        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.profile_view, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.notificationsBadge);
        text.setText(value);
        itemView.addView(badge);

//        BottomNavigationMenuView mbottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
//        View view = mbottomNavigationMenuView.getChildAt(4);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) view;
//        View cart_badge = LayoutInflater.from(this).inflate(R.layout.profile_view,mbottomNavigationMenuView, false);
//        ((TextView) cart_badge.findViewById(R.id.notificationsBadge)).setText("5");
//
//        itemView.addView(cart_badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

    /**
     * Json to Pojo to parse the response into our model class.
     *
     * @param jsonString
     * @param pojoClass
     * @return
     */
    public static Object jsonToPojo(String jsonString, Class<?> pojoClass) {
        return new Gson().fromJson(jsonString, pojoClass);
    }

    /**
     * This method checks if the Network available on the device or not.
     *
     * @param context
     * @return true if network available, false otherwise
     */
    public static Boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        try {
            final ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                connected = true;
            } else if (netInfo != null && netInfo.isConnected()
                    && cm.getActiveNetworkInfo().isAvailable()) {
                connected = true;
            } else if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url
                            .openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        connected = true;
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (cm != null) {
                final NetworkInfo[] netInfoAll = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfoAll) {
                    System.out.println("get network type :::" + ni.getTypeName());
                    if ((ni.getTypeName().equalsIgnoreCase("WIFI") || ni
                            .getTypeName().equalsIgnoreCase("MOBILE"))
                            && ni.isConnected() && ni.isAvailable()) {
                        connected = true;
                        if (connected) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

//    otro metodo
//            para saber si esta en Red
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * Check app is running in background
     *
     * @param context
     * @return
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName()))
                            isInBackground = false;
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName()))
                isInBackground = false;
        }
        return isInBackground;
    }

    /**
     * check app is installed on the device or not
     *
     * @param uri
     * @return
     */
    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean status;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            status = true;
        } catch (PackageManager.NameNotFoundException e) {
            status = false;
        }
        return status;
    }

    /**
     * check app is installed on the device or not
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }


    /**
     * Json to Pojo
     *
     * @param jsonString
     * @param pojoClass
     * @return
     */
    public static Object jsonToPojo(String jsonString, Type pojoClass) {
        return new Gson().fromJson(jsonString, pojoClass);
    }

    /**
     * Pojo to Json
     *
     * @param pojoObject
     * @return
     */
    public static String pojoToJson(Object pojoObject) {
        return new Gson().toJson(pojoObject);
    }

    /**
     * Json to Pojo with Deserializer
     *
     * @param jsonString
     * @param pojoClass
     * @param jsonDeserializer
     * @param pojoType
     * @return
     */
    public static Object jsonToPojoWithDeserializer(String jsonString, Class<?> pojoClass,
                                                    JsonDeserializer<?> jsonDeserializer, Type pojoType) {
        return new GsonBuilder().registerTypeAdapter(pojoType, jsonDeserializer).create()
                .fromJson(jsonString, pojoClass);
    }

    /**
     * Get screen width
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(FragmentActivity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * Get fragment tag while using with pager
     *
     * @param viewId
     * @param id
     * @return
     */
    public static String getFragmentTagFromViewPager(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    /**
     * Hide keyboard
     */
    public static void hideKeyboard(Context context, View view) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Encrypt data with base64
     *
     * @param normalString
     * @return
     */
    public static String encryptBase64(String normalString) {
        return Base64.encodeToString(normalString.getBytes(), Base64.NO_WRAP | Base64.URL_SAFE);
    }

    /**
     * Decrypt data with base64
     *
     * @param encodedString
     * @return
     */
    public static String decryptBase64(String encodedString) {
        return new String(Base64.decode(encodedString, Base64.NO_WRAP | Base64.URL_SAFE));
    }

    /**
     * Get distance between two lat-long
     *
     * @param sLatitude
     * @param sLongitude
     * @param dLatitude
     * @param dLongitude
     * @return distance in meter
     */
    public static double getDistance(double sLatitude, double sLongitude, double dLatitude, double dLongitude) {
        Location locationA = new Location("point A");
        locationA.setLatitude(sLatitude);
        locationA.setLongitude(sLongitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(dLatitude);
        locationB.setLongitude(dLongitude);
        return locationA.distanceTo(locationB) / 1000;//in km
    }

    /**
     * This method to get screen size.
     *
     * @param context
     * @return window manager instance which contains width and height of the
     * screen
     */
    public static DisplayMetrics getScreenSize(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    public final static SimpleDateFormat formate_year_month_day_hour_minute_second = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat formate_week_month_day_year = new SimpleDateFormat("EEEE, MMM dd, yyyy");
    public final static SimpleDateFormat formate_week = new SimpleDateFormat("EEEE");
    public final static SimpleDateFormat formate_hour_minute = new SimpleDateFormat("HH:mm");
    public final static SimpleDateFormat formate_month_year = new SimpleDateFormat("MM/yy");
    public final static SimpleDateFormat formate_hour_minute_second = new SimpleDateFormat("HH:mm:ss");
    public final static SimpleDateFormat formate_hour_minute_ampm = new SimpleDateFormat("hh:mm a");
    public final static SimpleDateFormat formate_year_month_day = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat formate_month_day_year = new SimpleDateFormat("MM.dd.yyyy");
    public final static SimpleDateFormat formate_month_day_year1 = new SimpleDateFormat("MMM dd, yyyy");

    public static String get_string_from_date_object(Date fulldateobj, SimpleDateFormat formate) {

        String month_day_str = "";
        try {
            month_day_str = formate.format(fulldateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return month_day_str;
    }

    /**
     * format value to 6 decimal point
     *
     * @param value
     * @return
     */
    public static String formateToSixDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#.000000");
        return df.format(value);
    }

    /**
     * format value to 6 decimal point
     *
     * @param value
     * @return
     */
    public static String formateToThreeDecimal(double value) {
        DecimalFormat df = new DecimalFormat("#.000");
        return df.format(value);
    }


    /**
     * get date in UTC time format
     *
     * @param date
     * @return
     */
    public static String current_datetime_To_UTC(Date date) {
        SimpleDateFormat actualdate_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actualdate_format.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        String Utc_Time = actualdate_format.format(date);
        return Utc_Time;
    }

    /**
     * change date time format
     *
     * @param from_str
     * @param from_formate
     * @param to_formate
     * @return
     */
    public static String date_formate_to_other_date_formate(String from_str, SimpleDateFormat from_formate, SimpleDateFormat to_formate) {

        Date date_from = null;
        try {
            date_from = from_formate.parse(from_str);
        } catch (Exception e) {

            e.printStackTrace();
        }

        String to_str = null;
        try {
            to_str = to_formate.format(date_from);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return to_str;

    }


    /**
     * chnage datetime format
     *
     * @param from_str
     * @param from_formate
     * @return
     */
    public static Date date_formate_to_date_obj(String from_str, SimpleDateFormat from_formate) {

        Date date_from = null;
        try {
            date_from = from_formate.parse(from_str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date_from;

    }

    /**
     * make textView ellipsis
     *
     * @param textview
     * @return
     */
    public static boolean Textview_Ellipsis(TextView textview) {

        Layout layout = textview.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0) {
                int ellipsisCount = layout.getEllipsisCount(lines - 1);
                if (ellipsisCount > 0) {
                    return true;
                }
            }
        }

        return false;

    }


    /**
     * if you want to know which requirement was not met
     */

    public static boolean ValidationForPassword(String pass) {
//        if (pass.length() < 8 || pass.length() >15 ){
        if (pass.length() < 6) {
//            System.out.println("pass too short or too long");
            return false;
        }
//        if (username.length() < 3 || username.length() >15 ){
//            System.out.println("username too short or too long");
//            return false;
//        }
        if (!pass.matches(".*\\d.*")) {
//            System.out.println("no digits found");
            return false;
        }
        if (!pass.matches(".*[A-Z].*")) {
//            System.out.println("no uppercase letters found");
            return false;
        }
        if (!pass.matches(".*[a-z].*")) {
//            System.out.println("no lowercase letters found");
            return false;
        }
//        if (!pass.matches(".*[!@#$%^&*+=?-].*")) {
//            System.out.println("no special chars found");
//            return false;
//        }
//        if (containsPartOf(pass,username)) {
//            System.out.println("pass contains substring of username");
//            return false;
//        }
//        if (containsPartOf(pass,email)) {
//            System.out.println("pass contains substring of email");
//            return false;
//        }
        return true;
    }

    /**
     * format decimal value
     *
     * @param value
     * @return
     */
    public static String formatDecimal(double value) {
        try {
            DecimalFormat format = new DecimalFormat();
            format.setDecimalSeparatorAlwaysShown(false);
            format.setGroupingUsed(false);
            format.setMaximumFractionDigits(2);
            return format.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * format decimal value
     *
     * @param value
     * @return
     */
    public static String formatDecimal(double value, boolean isForNutrition) {
        try {
            DecimalFormat format = new DecimalFormat();
            format.setGroupingUsed(false);
            format.setMaximumFractionDigits(2);
            if (isForNutrition) {
                String valueString = "" + value;
                String lastDegits = valueString.substring(valueString.length() - 2, valueString.length());
                // check last 2 digits if 15.00 make 15 and if 15.5 make 15.50
                if (lastDegits.equals(".0") || lastDegits.equals("00")) {
                    format.setDecimalSeparatorAlwaysShown(false);
                } else {
                    format.setDecimalSeparatorAlwaysShown(true);
                    format.setMinimumFractionDigits(2);
                }
            } else {
                format.setDecimalSeparatorAlwaysShown(false);
            }
            return format.format(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
