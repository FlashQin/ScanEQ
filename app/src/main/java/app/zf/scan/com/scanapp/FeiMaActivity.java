package app.zf.scan.com.scanapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.gyf.barlibrary.ImmersionBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.GaiMaLittleAdapter;
import app.zf.scan.com.BaseHelper.BaseActivity;
import app.zf.scan.com.BaseHelper.CreatJson;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.views.MyDialog;
import app.zf.scan.com.views.ZQListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

@RuntimePermissions
public class FeiMaActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    @InjectView(R.id.editText_feima)
    EditText editTextFeima;
    @InjectView(R.id.txt_one_feima)
    TextView txtOneFeima;
    @InjectView(R.id.txt_two_feima)
    TextView txtTwoFeima;
    @InjectView(R.id.txt_three_feima)
    TextView txtThreeFeima;
    @InjectView(R.id.btn_sure_feima)
    Button btnSureFeima;
    @InjectView(R.id.listview_feima_ac)
    ZQListView listView;
    @InjectView(R.id.img_back_feima)
    ImageView back;
    private Boolean isScan = false;
    private String scanOneStar, scanOneEnd, scanTwoStart, scanTwoEnd, scanThreeCode;
    private final int HTTP_SUCC_GET_PICI = 3;
    private int which = 1, whichOne = 1;
    private final int HTTP_SUCC_SAVE = 4;
    private int SCAN_ER_WEI_MA = 5;
    private int SCAN_ONE_CODE = 6;
    private int GAIMA_TIJIAO = 9;
    private int SCAN_THREE_CODE = 7;
    private GaiMaLittleAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            if (msg.what == SCAN_ONE_CODE) {
                String jStringname = new String(msg.obj.toString());
                dialog.hide();
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        isScan = true;
                        Map<String, Object> mapd = new HashMap<String, Object>();
                        Map<String, Object> mapdd = new HashMap<String, Object>();
                        mapd = (Map<String, Object>) map.get("d");
                        String codeGenerateRecordId = mapd.get("codeGenerateRecordId").toString();
                        String fileName = mapd.get("fileName").toString();
                        mapdd.put("codeGenerateRecordId", codeGenerateRecordId);
                        mapdd.put("startPage", scanOneStar);
                        mapdd.put("endPage", scanOneEnd);
                        UserInfo.listData.add(mapdd);
                        UserInfo.listviewData.add(mapd);
                        adapter = new GaiMaLittleAdapter(UserInfo.listviewData, FeiMaActivity.this, true,handler);
                        listView.setAdapter(adapter);
                        setHeight(adapter, listView);
                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {
                    showToast("异常:" + e);
                }
            }
            if (msg.what == SCAN_THREE_CODE) {
                String jStringname = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        isScan = true;
                        Map<String, Object> mapd = new HashMap<String, Object>();
                        Map<String, Object> mapdd = new HashMap<String, Object>();
                        mapd = (Map<String, Object>) map.get("d");
                        String codeGenerateRecordId = mapd.get("codeGenerateRecordId").toString();
                        String fileName = mapd.get("fileName").toString();
                        mapdd.put("codeGenerateRecordId", codeGenerateRecordId);
                        mapdd.put("startCode", mapd.get("startCode").toString());
                        mapdd.put("endCode", mapd.get("endCode").toString());
                        mapd.put("startPage",mapd.get("currentPage"));
                        mapd.put("endPage",mapd.get("currentPage"));
                        UserInfo.listData.add(mapdd);
                        UserInfo.listviewData.add(mapd);
                        adapter = new GaiMaLittleAdapter(UserInfo.listviewData, FeiMaActivity.this, false,handler);
                        listView.setAdapter(adapter);
                        setHeight(adapter, listView);
                        dialog.hide();
                    } else {
                        showToast(m);
                        dialog.hide();
                    }

                } catch (Exception e) {
                    showToast("异常:" + e);
                    dialog.hide();
                }
            }
            if (msg.what == GAIMA_TIJIAO) {
                String jStringname = new String(msg.obj.toString());
                dialog.hide();
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        showToast("废码成功");
                        finish();
                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {
                    showToast("异常:" + e);
                }
            }
            if (msg.what==8){
                int jString =Integer.parseInt( new String(msg.obj.toString()));
                for (int i = 0; i <UserInfo.listviewData.size() ; i++) {
                    if (i==jString){
                        UserInfo.listviewData.remove(i);
                        UserInfo.listData.remove(i);
                        setHeight(adapter, listView);
                        adapter.notifyDataSetChanged();
                    }


            }
        }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feima);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
        UserInfo.listData.clear();
        UserInfo.listviewData.clear();
    }

    @OnClick({R.id.txt_one_feima, R.id.txt_two_feima, R.id.txt_three_feima, R.id.btn_sure_feima, R.id.img_back_feima})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_one_feima:
                which = 1;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    FeiMaActivityPermissionsDispatcher.requstWithCheck(FeiMaActivity.this);

                } else {
                    Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCAN_ER_WEI_MA);

                }
                break;
            case R.id.txt_two_feima:
                which = 2;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    FeiMaActivityPermissionsDispatcher.requstWithCheck(FeiMaActivity.this);

                } else {
                    Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCAN_ER_WEI_MA);

                }
                break;
            case R.id.txt_three_feima:
                which = 3;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    FeiMaActivityPermissionsDispatcher.requstWithCheck(FeiMaActivity.this);

                } else {
                    Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCAN_ER_WEI_MA);

                }
                break;
            case R.id.btn_sure_feima:
                if (!editTextFeima.getText().toString().isEmpty()) {
                    if (UserInfo.listData.size() > 0) {
                        btnSure();
                    } else {
                        showToast("请选择扫码方式");
                    }
                } else {
                    showToast("请填写说明信息");
                }

                break;
            case R.id.img_back_feima:
                finish();
                break;
        }
    }

    public void btnSure() {
        dialog = MyDialog.showDialog(FeiMaActivity.this);
        dialog.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyExplain", editTextFeima.getText().toString());

        json = CreatJson.mapCreateJsonFei(map, UserInfo.listData);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.feima_tijiao), handler, GAIMA_TIJIAO, json, UserInfo.utoken);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {

            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {

                String result = (bundle.getString(CodeUtils.RESULT_STRING)).replace("http://cac.top/168/00/", "");
                // Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_SHORT).show();
                if (which == 1) {
                    if (UserInfo.whichone == 1) {
                        UserInfo.whichone = 2;
                        //scanOneStar = result;
                        scanOneStar = result;
                        mAlertView = new AlertView("扫描成功", result, null, new String[]{"重新扫描", "扫描下一个"},
                                null, FeiMaActivity.this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener( this);
                        mAlertView.show();


                    } else {
                        UserInfo.whichone = 1;
                        dialog = MyDialog.showDialog(FeiMaActivity.this);
                        dialog.show();
                        scanOneEnd = result;
                        map.put("endCode", scanOneEnd);
                        map.put("startCode", scanOneStar);
                        json = ToJson.mapToJson(map);
                        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_get_tongye), handler, SCAN_ONE_CODE, json, UserInfo.utoken);
                    }

                }
                if (which == 2) {
                    if (UserInfo.whichone == 1) {
                        UserInfo.whichone = 2;
                        //scanOneStar = result;
                        scanOneStar = result;
                        mAlertView = new AlertView("扫描成功", result, null, new String[]{"重新扫描", "扫描下一个"},
                                null, FeiMaActivity.this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener((OnDismissListener) this);
                        mAlertView.show();


                    } else {
                        UserInfo.whichone = 1;
                        dialog = MyDialog.showDialog(FeiMaActivity.this);
                        dialog.show();
                        scanOneEnd = result;
                        map.put("endCode", scanOneEnd);
                        map.put("startCode", scanOneStar);
                        json = ToJson.mapToJson(map);
                        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_get_butongye), handler, SCAN_ONE_CODE, json, UserInfo.utoken);
                    }
                }
                if (which == 3) {
                    dialog = MyDialog.showDialog(FeiMaActivity.this);
                    dialog.show();
                    //scanOneEnd = result;
                    scanOneStar = result;
                    map.put("code", scanOneStar);
                    // map.put("startCode", scanOneStar);
                    json = ToJson.mapToJson(map);
                    new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_get_zhengye), handler, SCAN_THREE_CODE, json, UserInfo.utoken);
                }


            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                showToast("解析二维码失败");
                //txtOneMain.setText("解析二维码失败");
                //  txttai.setText("查询失败");
                // txtTwoMain.setText("无");


            }
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void requst() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FeiMaActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void shuoming(final PermissionRequest request) {
        ShowSystemQuanxian("使用此功能需要打开相机权限", request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void jujue() {
        Toast.makeText(FeiMaActivity.this, "权限未授予，功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void notask() {
        setPermission(FeiMaActivity.this, "当前应用缺少相机权限,请去设置界面打开");
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (which == 1) {
            if (position == 1) {
                Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
            if (position == 0) {
                UserInfo.whichone = 1;
                which = 1;
                Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
        }
        if (which == 2) {
            if (position == 1) {
                Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
            if (position == 0) {
                UserInfo.whichone = 2;
                which = 2;
                Intent intent = new Intent(FeiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
        }
    }

    @Override
    public void onDismiss(Object o) {

    }
}
