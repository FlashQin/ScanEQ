package app.zf.scan.com.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.gyf.barlibrary.ImmersionBar;
import com.tsy.sdk.myokhttp.MyOkHttp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.zf.scan.com.BaseHelper.BaseActivity;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.MyAppcation;

import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.Fragment.FeiFragment;
import app.zf.scan.com.Fragment.GaiFragment;
import app.zf.scan.com.Fragment.HomeFragment;
import app.zf.scan.com.Fragment.MineFragment;
import app.zf.scan.com.HttpUtils.HttpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {



    @InjectView(R.id.layFrame)
    FrameLayout layFrame;
    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private boolean isExit = false;
    private ArrayList<Fragment> fragments;
    MyOkHttp myOKHttp = MyAppcation.getInstance().getMyOkHttp();
    private int SCAN_ER_WEI_MA = 5;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
            if (msg.what == HTTP_SUCC) {
            String jString = new String(msg.obj.toString());
                if (jString.equals("fail status=404")) {
                    showToast("服务器404");
                } else {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map = GetNetResult.GetJosn(jString);
                String c = map.get("c").toString();


                String m = map.get("m").toString();
                if (c.equals("0")) {
                    Map<String, Object> mapdata = new HashMap<String, Object>();
                    mapdata = (Map<String, Object>) map.get("d");

                    String token = mapdata.get("token").toString();
                   UserInfo.utoken=token;
                   // System.console().printf("ok");


                }else {
//                    UserInfo.cleanLoginInfo(MainActivity.this);
//                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    showToast("此账号密码已变更,重新登录");
                }

            } catch (Exception e) {

                showToast("异常:"+e);
            }
        }}
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        //ImmersionBar.with(this).transparentBar().init();
//        mAlertView = new AlertView("重新登录", "此账号密码已变更", "取消", new String[]{"确定"},
//                null, MainActivity.this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
        myOKHttp = new MyOkHttp();
        setfargmentList();

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int wh = display.getWidth();

        if (UserInfo.isneed==true){
            login(handler);
        }

       // SomeInfoData.with = getwindowwith();
        //int px = Dp2px(MainActivity.this, with);


    }
    public   void login(Handler handler){

        sp = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String phone = sp.getString("account", null);
        String utoken = sp.getString("utoken", null);
        String password = sp.getString("password", null);

        map.put("userName",phone);
        map.put("password", password);
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.login), handler, HTTP_SUCC, json, "");
    }
    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.keyboardEnable(true).init();
    }
    /***
     * First Page Fagment set
     */
    public void setfargmentList() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );
//        BadgeItem numberBadgeItem = new BadgeItem()//设置底部显示消息数量信息
//                .setBorderWidth(4)
//                .setBackgroundColor(Color.WHITE)
//                .setText("5")
//                .setHideOnSelect(true);

        bottomNavigationBar.setBarBackgroundColor(R.color.grey);

        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_application_u, "申码").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.ic_modify_u, "改码").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.ic_waste_u, "废码").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.ic_my_u, "我的").setActiveColorResource(R.color.blue))
                .setFirstSelectedPosition(0)
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, HomeFragment.newInstance("申码"));

        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {//将实例化Fragment放入数组
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("申码"));
        fragments.add(GaiFragment.newInstance("改码"));
        fragments.add(FeiFragment.newInstance("废码"));
        fragments.add(MineFragment.newInstance("我的"));

        // fragments.add(MineFragment.newInstance(",,,"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {//在实现方法里进行切换fragment
        //  点击Item时调用此方法
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //当前的fragment
                Fragment from = fm.findFragmentById(R.id.layFrame);
                //点击即将跳转的fragment
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    // 隐藏当前的fragment，显示下一个
                    ft.hide(from).show(fragment);
                } else {
                    // 隐藏当前的fragment，add下一个到Activity中
                    ft.hide(from).add(R.id.layFrame, fragment);
                    if (fragment.isHidden()) {
                        ft.show(fragment);
                        // Logger.d("被隐藏了");

                    }

                }
                ft.commitAllowingStateLoss();

                //顶部显示对应的文字
                if (position==0){
                    mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.blue).init();
                }
                if (position==1){
                    mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.blue).init();
                }
                if (position==2){
                    mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.blue).init();
                }
                if (position==3){
                    mImmersionBar.fitsSystemWindows(false).transparentStatusBar().init();
                }


            }

        }

    }

    @Override
    public void onTabUnselected(int position) { //  对没有选中的Item进行处理的方法,
        //这儿也要操作隐藏，否则Fragment会重叠
        if (fragments != null) {

            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.hide(fragment);          // 隐藏当前的fragment
                ft.commitAllowingStateLoss();

            }
        }
    }

    @Override
    public void onTabReselected(int position) {
        // 当被选中的Item 再一次被点击时调用此方法
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;

        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * the metho of exit
     */
    public void exit() {
        if (!isExit) {
            isExit = true;
            showToast("连续两次可退出");
           //Toast.makeText(context.getApplicationContext(),"连续两次可退出" , Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0, 500);
        } else {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);

        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


}


