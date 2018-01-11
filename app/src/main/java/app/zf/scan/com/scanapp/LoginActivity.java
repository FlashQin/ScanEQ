package app.zf.scan.com.scanapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.HashMap;
import java.util.Map;

import app.zf.scan.com.BaseHelper.BaseActivity;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.views.MyDialog;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/12.
 */

public class LoginActivity extends BaseActivity {
    @InjectView(R.id.edt_accout_login)
    EditText edtAccoutLogin;
    @InjectView(R.id.edt_pass_login)
    EditText edtPassLogin;
    @InjectView(R.id.img_show_login)
    ImageView Login;
    @InjectView(R.id.btn_login_login)
    Button btnLoginLogin;
    private int type = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                            String active = mapdata.get("active").toString();
                            String createDate = mapdata.get("createDate").toString();
                            String id = mapdata.get("id").toString();
                            String nickName = mapdata.get("nickName").toString();
                            String token = mapdata.get("token").toString();
                            String factoryName = mapdata.get("factoryName").toString();
                            UserInfo.utoken = token;
                            UserInfo.account = edtAccoutLogin.getText().toString();
                            UserInfo.saveLoginInfo(LoginActivity.this, token, edtAccoutLogin.getText().toString(), edtPassLogin.getText().toString(), nickName, factoryName);

                            UserInfo.isneed = false;
                            UserInfo.isexit = false;
                            edtPassLogin.setText("");
                            edtAccoutLogin.setText("");
                            dialog.hide();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            showToast(m);
                            dialog.hide();
                        }

                    } catch (Exception e) {
                        showToast("异常:"+e);
                        dialog.hide();

                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ImmersionBar.with(this).transparentBar().init();
        ButterKnife.inject(LoginActivity.this);
        edtPassLogin.setText("");
        edtAccoutLogin.setText("");
        sp = getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String phone = sp.getString("account", "");
        String utoken = sp.getString("utoken", "");
        String password = sp.getString("password", "");
        if (!phone.equals("")) {
            UserInfo.utoken = utoken;
            UserInfo.account = phone;
            UserInfo.password = password;

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
            return false;

        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.img_show_login, R.id.btn_login_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_show_login:
                if (!edtPassLogin.getText().toString().isEmpty()) {
                    if (type == 1) {
                        type = 2;
                        edtPassLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);// show
                    } else {
                        type = 1;
                        edtPassLogin.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);// hide
                    }

                }
                break;
            case R.id.btn_login_login:
                if (!edtAccoutLogin.getText().toString().isEmpty() || !edtPassLogin.getText().toString().isEmpty()) {
                    dialog = MyDialog.showDialog(LoginActivity.this);
                    dialog.show();
                    Map<String, String> map = new HashMap<>();
                    map.put("userName", edtAccoutLogin.getText().toString());
                    map.put("password", edtPassLogin.getText().toString());
                    String json = ToJson.mapToJson(map);
                    new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.login), handler, HTTP_SUCC, json, "");
                } else {
                    showToast("账号或密码不能为空");
                }

                break;
        }
    }
}
