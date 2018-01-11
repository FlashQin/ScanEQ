package app.zf.scan.com.BaseHelper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/14.
 */

public class UserInfo {
    private static final String shareKey = "USER_INFO";
    private static final String installKey = "INSTALL_INFO";
    public static Boolean isexit = false;
    public static String ShenmaTime = "false";
    public static String nickNam;
    public static String factoryName;
    public static float wh;
    public static String utoken = "";
    public static int whichone = 1;
    public static int whichtwo = 1;
    public static List<Map<String, Object>> listData = new ArrayList<>();
    public static List<Map<String, Object>> listviewData = new ArrayList<>();
    public static String account = "";
    public static String password = "";
    public static Boolean isneed = true;
    public static int whichSerch=1;

    /***
     * px to dp
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2px(Context context, float dp) {
        final float sc = context.getResources().getDisplayMetrics().density;
        int dd = (int) (dp * sc + 0.5f);
        return dd;

    }

    /**
     * 保存用户登录信息
     *
     * @param context
     */
    public static void saveLoginInfo(Context context, String utoken, String account, String password,String nickName,String factoryName) {
        SharedPreferences share = context.getSharedPreferences(shareKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString("utoken", utoken);
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putString("nickName", nickName);
        editor.putString("factoryName", factoryName);
        editor.commit();

    }

    /**
     * 清除登录信息
     *
     * @param context
     */
    public static void cleanLoginInfo(Context context) {
        SharedPreferences share = context.getSharedPreferences(shareKey, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取用户登录信息
     *
     * @param context
     * @return
     */
    public static Map<String, Object> getLoginInfo(Context context) {
        SharedPreferences share = context.getSharedPreferences(shareKey, Context.MODE_PRIVATE);

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("account", share.getString("account", ""));
        map.put("password", share.getString("password", ""));
        map.put("utoken", share.getString("utoken", ""));
        map.put("nickName", share.getString("nickName", ""));
        map.put("factoryName", share.getString("factoryName", ""));


        return map;


    }

}
