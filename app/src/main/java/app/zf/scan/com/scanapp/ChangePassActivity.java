package app.zf.scan.com.scanapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.GaiMaAllAdapter;
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
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class ChangePassActivity extends BaseActivity {
    @InjectView(R.id.edt_old_pass_change)
    EditText edtOldPassChange;
    @InjectView(R.id.edt_new_pass_change)
    EditText edtNewPassChange;
    @InjectView(R.id.edt_repitnew_pass_change)
    EditText edtRepitnewPassChange;
    @InjectView(R.id.btn_sure_change)
    Button btnSureChange;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HTTP_SUCC) {
                String jString = new String(msg.obj.toString());
                dialog.hide();
                if (jString.equals("fail status=404")) {
                    showToast("服务器404");
                } else {
                    try {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map = GetNetResult.GetJosn(jString);
                        String c = map.get("c").toString();
                        String m = map.get("m").toString();
                        if (c.equals("0")) {
                            showToast("修改成功");
                            String account = (UserInfo.getLoginInfo(ChangePassActivity.this)).get("account").toString();
                            UserInfo.saveLoginInfo(ChangePassActivity.this, UserInfo.utoken, account, edtNewPassChange.getText().toString(), UserInfo.nickNam, UserInfo.factoryName);
                            finish();
                        } else {
                            showToast("旧密码错误");
                        }

                    } catch (Exception e) {
                        showToast("异常:" + e);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
    }

    @OnClick({R.id.btn_sure_change, R.id.img_back_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sure_change:
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ChangePassActivity.
                        this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//hide keybord
                if (!edtRepitnewPassChange.getText().toString().isEmpty() &&
                        !edtNewPassChange.getText().toString().isEmpty() && !edtOldPassChange.getText().toString().isEmpty()) {
                    if (edtNewPassChange.getText().toString().equals(edtRepitnewPassChange.getText().toString())) {
                        dialog = MyDialog.showDialog(ChangePassActivity.this);
                        dialog.show();
                        changepass();
                    } else {
                        showToast("密码不一致,请重新输入");
                    }
                } else {
                    showToast("密码不能为空");
                }
                break;
            case R.id.img_back_change:
                finish();
                break;
        }
    }

    public void changepass() {
        map.put("oldPassword", edtOldPassChange.getText().toString());
        map.put("password", edtNewPassChange.getText().toString());
        map.put("rePassword", edtRepitnewPassChange.getText().toString());
        json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.change_password), handler, HTTP_SUCC, json, UserInfo.utoken);
    }
}
