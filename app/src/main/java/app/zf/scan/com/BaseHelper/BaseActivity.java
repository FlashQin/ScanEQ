package app.zf.scan.com.BaseHelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.gyf.barlibrary.ImmersionBar;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.scanapp.MainActivity;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.views.MyDialog;
import permissions.dispatcher.PermissionRequest;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/11/22.
 */

public abstract class BaseActivity extends AppCompatActivity{
    protected int PageIndex = 1, PageSize = 10;
    protected int recommond = 0;

    protected float wh;//屏幕宽度

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
    protected ImmersionBar mImmersionBar;
    private InputMethodManager imm;
    protected MyOkHttp myOKHttp = MyAppcation.getInstance().getMyOkHttp();
    protected List<Map<String, Object>> listdadta = new ArrayList<Map<String, Object>>();
    protected Map<String, String> map = new HashMap<>();
    private Toast mToast;
    protected  String json="";
    protected AlertView mAlertView;
    protected SharedPreferences sp;
    protected MyDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        wh = display.getWidth();
        UserInfo.wh=wh;
        myOKHttp = new MyOkHttp();
        if (isImmersionBarEnabled()){
            initImmersionBar();
        }

    }
    protected boolean isImmersionBarEnabled() {
        return true;
    }
    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁
    }
    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }
    /**
     * 被拒绝并且不再提醒,提示用户去设置界面重新打开权限
     */
    protected    void setPermission(Context context,String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                startActivity(intent);
            }
        });
        builder.show();
    }

    protected void setHeight(Adapter adapter, ListView listviwe){
        int height = 0;
        int count = adapter.getCount();
        for(int i=0;i<count;i++){
            View temp = adapter.getView(i,null,listviwe);
            temp.measure(0,0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listviwe.getLayoutParams();
        layoutParams.width = LinearLayout.LayoutParams.FILL_PARENT;
        layoutParams.height = height;
        listviwe.setLayoutParams(layoutParams);
    }
    protected void showToast(final String message) {
        if (!TextUtils.isEmpty(message)) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (mToast == null) {
                        mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(message);
                    }
                    mToast.show();
                }
            });

        }
    }
    /**
     * 告知用户具体需要权限的原因
     * @param messageResId
     * @param request
     */
    protected void ShowSystemQuanxian(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();//请求权限

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
