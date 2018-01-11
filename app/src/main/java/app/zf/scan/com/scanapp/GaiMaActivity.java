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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
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
public class GaiMaActivity extends BaseActivity implements OnItemClickListener, OnDismissListener {
    @InjectView(R.id.rel_choose_name_gm)
    RelativeLayout relChooseNameGm;
    @InjectView(R.id.rel_choose_pc_gm)
    RelativeLayout relChoosePcGm;
    @InjectView(R.id._edtshuoming_gm)
    EditText EdtshuomingGm;
    @InjectView(R.id.txt_scanone_gm)
    TextView txtScanoneGm;
    @InjectView(R.id.txt_scantwo_gm)
    TextView txtScantwoGm;
    @InjectView(R.id.txt_scanthree_gm)
    TextView txtScanthreeGm;
    @InjectView(R.id.btn_sure_gm)
    Button btnSureGm;
    @InjectView(R.id.txt_name_gaima)
    TextView txtNameGaima;
    @InjectView(R.id.txt_pici_gaima)
    TextView txtPiciGaima;
    @InjectView(R.id.listview_gaima_ac)
    ZQListView listviwe;
    private GaiMaLittleAdapter adapter;
    private Boolean isChoose = false, isScan = false;
    private int which = 1, whichOne = 1;
    private String[] name, nameid, piciName, piciId, Type;
    private final int HTTP_SUCC_GET_PICI = 3;
    private final int HTTP_SUCC_SAVE = 4;
    private int SCAN_ER_WEI_MA = 5;
    private int SCAN_ONE_CODE = 6;
    private int SCAN_THREE_CODE = 7;

    private int GAIMA_TIJIAO = 9;
    private String NameId, PiciId, Shenmatype, scanOneStar, scanOneEnd, scanTwoStart, scanTwoEnd, scanThreeCode;
    private int Index,point=1;
    private android.hardware.Camera mCamera;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HTTP_SUCC) {

                String jString = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jString);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        listdadta = (List<Map<String, Object>>) map.get("d");
                        name = new String[listdadta.size()];
                        nameid = new String[listdadta.size()];
                        for (int i = 0; i < listdadta.size(); i++) {
                            name[i] = listdadta.get(i).get("productName").toString();
                            nameid[i] = listdadta.get(i).get("id").toString();
                        }
                        showNameDoor(name, 1);
                    }
                } catch (Exception e) {
                    showToast("异常:"+e);
                }
            }
            if (msg.what == HTTP_SUCC_GET_PICI) {
                String jStringname = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        listdadta = (List<Map<String, Object>>) map.get("d");
                        piciName = new String[listdadta.size()];
                        piciId = new String[listdadta.size()];
                        for (int i = 0; i < listdadta.size(); i++) {
                            piciName[i] = listdadta.get(i).get("batchName").toString();
                            piciId[i] = listdadta.get(i).get("id").toString();
                        }
                        showNameDoor(piciName, 2);

                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {
                    showToast("异常:"+e);
                }
            }
            if (msg.what == SCAN_ONE_CODE) {
                String jStringname = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        point++;
                        isScan = true;
                        Map<String, Object> mapd = new HashMap<String, Object>();
                        Map<String, Object> mapdd = new HashMap<String, Object>();
                        mapd = (Map<String, Object>) map.get("d");
                        String codeGenerateRecordId = mapd.get("codeGenerateRecordId").toString();
                        String fileName = mapd.get("fileName").toString();
                        mapdd.put("codeGenerateRecordId", codeGenerateRecordId);
                        mapdd.put("startCode", scanOneStar);
                        mapdd.put("endCode", scanOneEnd);
                        mapd.put("id",point);
                        UserInfo.listData.add(mapdd);

                        UserInfo.listviewData.add(mapd);
                        adapter = new GaiMaLittleAdapter(UserInfo.listviewData, GaiMaActivity.this,true,handler);
                        listviwe.setAdapter(adapter);
                        setHeight(adapter, listviwe);
                        dialog.hide();
                    } else {
                        showToast(m);
                        dialog.hide();
                    }

                } catch (Exception e) {
                    showToast("异常:"+e);
                    dialog.hide();
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
                        point++;
                        isScan = true;
                        Map<String, Object> mapd = new HashMap<String, Object>();
                        Map<String, Object> mapdd = new HashMap<String, Object>();
                        mapd = (Map<String, Object>) map.get("d");
                        String codeGenerateRecordId = mapd.get("codeGenerateRecordId").toString();
                        String fileName = mapd.get("fileName").toString();
                        mapdd.put("codeGenerateRecordId", codeGenerateRecordId);
                        mapdd.put("startCode", mapd.get("startCode").toString());
                        mapdd.put("endCode",  mapd.get("endCode").toString());
                        mapd.put("startPage",mapd.get("currentPage"));
                        mapd.put("endPage",mapd.get("currentPage"));
                        mapd.put("id",point);
                        UserInfo.listData.add(mapdd);
                        UserInfo.listviewData.add(mapd);
                        adapter = new GaiMaLittleAdapter(UserInfo.listviewData, GaiMaActivity.this,false,handler);
                        listviwe.setAdapter(adapter);
                        setHeight(adapter, listviwe);
                        dialog.hide();
                    } else {
                        showToast(m);
                        dialog.hide();
                    }

                } catch (Exception e) {
                    showToast("异常:"+e);
                    dialog.hide();
                }
            }
            if (msg.what == GAIMA_TIJIAO) {
                String jStringname = new String(msg.obj.toString());
                dialog.show();
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        showToast("改码成功");
                        finish();
                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {
                    showToast("异常:"+e);
                }
            }
            if (msg.what==8){
                int jString =Integer.parseInt( new String(msg.obj.toString()));
                for (int i = 0; i <UserInfo.listviewData.size() ; i++) {
                    if (i==jString){
                        UserInfo.listviewData.remove(i);
                        UserInfo.listData.remove(i);
                        setHeight(adapter, listviwe);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaima);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
        UserInfo.listData.clear();
        UserInfo.listviewData.clear();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void getName() {
        new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.get_shenma_name), handler, HTTP_SUCC);
    }

    public void getPici() {
        NameId = nameid[Index];

        new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.get_shenma_pici) + NameId, handler, HTTP_SUCC_GET_PICI);
    }

    @OnClick({R.id.rel_choose_name_gm, R.id.rel_choose_pc_gm, R.id.txt_scanone_gm, R.id.txt_scantwo_gm, R.id.txt_scanthree_gm, R.id.btn_sure_gm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_choose_name_gm:
                getName();
                break;
            case R.id.rel_choose_pc_gm:
                if (isChoose == true) {
                    getPici();
                } else {
                    showToast("请先选择商品名称");
                }
                break;
            case R.id.txt_scanone_gm:
                which = 1;
                UserInfo.whichone = 1;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    GaiMaActivityPermissionsDispatcher.requstPermissionWithCheck(GaiMaActivity.this);

                } else {
                    Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCAN_ER_WEI_MA);


                }
                break;
            case R.id.txt_scantwo_gm:
                UserInfo.whichone = 1;
                which = 2;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    GaiMaActivityPermissionsDispatcher.requstPermissionWithCheck(GaiMaActivity.this);

                } else {
                    Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCAN_ER_WEI_MA);

                }
                break;
            case R.id.txt_scanthree_gm:
                which = 3;
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    GaiMaActivityPermissionsDispatcher.requstPermissionWithCheck(GaiMaActivity.this);

                } else {
                    Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, SCAN_ER_WEI_MA);
                }
                break;
            case R.id.btn_sure_gm:
                if (NameId != null && PiciId != null && !EdtshuomingGm.getText().toString().isEmpty()) {
                    if (UserInfo.listData.size() > 0) {
                        btnSure();
                    } else {
                        showToast("请选择号段");
                    }
                } else {
                    showToast("请完善相关信息");
                }

                break;
        }
    }

    public void btnSure() {
        dialog = MyDialog.showDialog(GaiMaActivity.this);
        dialog.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applyExplain", EdtshuomingGm.getText().toString());
        map.put("batchCode", PiciId);
        // map.put("changeCodeApplyNums", listData);
        map.put("productCode", NameId);
        //json = ToJson.mapToJson(map);
        json = CreatJson.mapCreateJsonGai(map, UserInfo.listData);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_tijiao), handler, GAIMA_TIJIAO, json, UserInfo.utoken);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null != data) {
            //  txttai.setText("查询中...");
            // txtOneMain.setText("");
            // txtTwoMain.setText("");
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
                                null, GaiMaActivity.this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
                        mAlertView.show();


                    } else {
                        dialog = MyDialog.showDialog(GaiMaActivity.this);
                        dialog.show();
                        UserInfo.whichone = 1;
                        //scanOneEnd = result;
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
                                null, GaiMaActivity.this, AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);
                        mAlertView.show();


                    } else {
                        dialog = MyDialog.showDialog(GaiMaActivity.this);
                        dialog.show();
                        UserInfo.whichone = 1;
                        //scanOneEnd = result;
                        scanOneEnd = result;
                        map.put("endCode", scanOneEnd);
                        map.put("startCode", scanOneStar);
                        json = ToJson.mapToJson(map);
                        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_get_butongye), handler, SCAN_ONE_CODE, json, UserInfo.utoken);
                    }
                }
                if (which == 3) {
                    dialog = MyDialog.showDialog(GaiMaActivity.this);
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

    public void showNameDoor(String[] strName, final int type) {
        //boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        SinglePicker<String> picker = new SinglePicker<>(this,
                false ? strName : strName);
        picker.setCanLoop(true);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setGravity(Gravity.CENTER);
        int with = (int) ((wh - (5 * UserInfo.Dp2px(GaiMaActivity.this, 10))));
        picker.setWidth(with - 170);
        picker.setTopLineColor(0xFF999999);
        picker.setTopLineHeight(1);
        picker.setTitleText(false ? "请选择" : "请选择");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFF999999);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFF999999);
        picker.setSubmitTextSize(13);
        picker.setSelectedTextColor(0xFF33B5E5);
        picker.setUnSelectedTextColor(0xFF999999);
        LineConfig config = new LineConfig();
        config.setColor(0xFF999999);//线颜色
        config.setAlpha(140);//线透明度
        config.setRatio((float) (1.0 / 8.0));//线比率
        picker.setLineConfig(config);
        picker.setItemWidth(500);
        picker.setBackgroundColor(0xFFEEEEEE);
        //picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(2);
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                //showToast("index=" + index + ", item=" + item);
                Index = index;
                if (type == 1) {
                    txtNameGaima.setText(item);
                    isChoose = true;
                    NameId = nameid[index];
                }
                if (type == 2) {
                    txtPiciGaima.setText(item);
                    PiciId = piciId[index];
                }


            }
        });
        picker.show();
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void requstPermission() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GaiMaActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void dilgoPermission(final PermissionRequest request) {
        ShowSystemQuanxian("使用此功能需要打开相机权限", request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void jujuePermission() {
        Toast.makeText(this, "权限未授予，功能无法使用", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void notaskPermission() {
        setPermission(GaiMaActivity.this, "当前应用缺少相机权限,请去设置界面打开");
    }

    @Override
    public void onItemClick(Object o, int position) {
        if (which == 1) {
            if (position == 1) {
                Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
            if (position == 0) {
                UserInfo.whichone = 1;
                which = 1;
                Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
        }if (which==2){
            if (position == 1) {
                Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
            if (position == 0) {
                UserInfo.whichone = 2;
                which = 2;
                Intent intent = new Intent(GaiMaActivity.this, CaptureActivity.class);
                startActivityForResult(intent, SCAN_ER_WEI_MA);
            }
        }
    }






    @Override
    public void onDismiss(Object o) {

    }
}
