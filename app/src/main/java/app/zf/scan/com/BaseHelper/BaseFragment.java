package app.zf.scan.com.BaseHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.bigkoo.alertview.AlertView;
import com.gyf.barlibrary.ImmersionBar;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.views.MyDialog;

import static android.content.Context.MODE_PRIVATE;


/**
 * ( )Created by ${Ethan_Zeng} on 2017/11/6.
 */

public class BaseFragment extends Fragment {
    protected int PageIndex = 1, PageSize = 10;
    protected int recommond = 0;
    protected int PageIndexAdd = 10;
    protected SharedPreferences sp;

    /*
       * 屏幕的宽度、高度、密度
       */
    protected int mScreenWidth;
    protected int mScreenHeight;
    protected float mDensity;
    /**
     * activity栈管理
     */

    protected AlertDialog progressDialog = null;
    protected boolean isCancel = false;
    protected Context context;
    protected final int SDK_PAY_FLAG = 9;
    /**
     * 网络请求成功标识
     */
    protected final int HTTP_SUCC = 1;
    /**
     * 发送图片
     */
    protected final int SEND_PIC_SUCC = 5;
    /**
     * 网络请求成功标识
     */
    protected final int GET_HTTP_SUCC = 0x4;

    /**
     * listview网络请求成功标识
     */
    protected final int HTTP_LISTVIEW_SUCC = 0x2;
    /**
     * listview网络请求加载更多成功标识
     */
    protected final int HTTP_LISTVIEW_LOAD_MORE_SUCC = 0x3;
    protected Activity mActivity;
    protected View mRootView;

    protected MyOkHttp myOKHttp = MyAppcation.getInstance().getMyOkHttp();
    protected List<Map<String, Object>> listdadta = new ArrayList<Map<String, Object>>();
    protected Map<String, String> map = new HashMap<>();
    protected AlertView mAlertView;
    protected MyDialog dialog;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }
    protected    void login(Handler handler){

        sp = context.getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String phone = sp.getString("account", null);
        String utoken = sp.getString("utoken", null);
        String password = sp.getString("password", null);
        map.put("userName",phone);
        map.put("password", password);
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.login), handler, HTTP_LISTVIEW_SUCC, json, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


}
