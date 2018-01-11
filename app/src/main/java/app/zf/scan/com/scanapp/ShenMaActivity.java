package app.zf.scan.com.scanapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.BaseHelper.BaseActivity;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class ShenMaActivity extends BaseActivity {
    @InjectView(R.id.txt_time_addshenma)
    TextView txtTimeAddshenma;
    @InjectView(R.id.rel_choose_time)
    RelativeLayout relChooseTime;

    @InjectView(R.id.edt_name_sp_shenma)
    TextView edtNameSpShenma;
    @InjectView(R.id.txt_name_pc_shenma)
    TextView txtNamePcShenma;
    @InjectView(R.id.txt_type_shenma)
    TextView txtTypeShenma;
    @InjectView(R.id.edt_nums_shenma)
    EditText edtNumsShenma;
    @InjectView(R.id.edt_shuoming_shenma)
    EditText edtShuomingShenma;
    @InjectView(R.id.btn_save_smac)
    Button btnSaveSmac;
    @InjectView(R.id.rel_choose_sp_sm)
    RelativeLayout relChooseSpSm;
    @InjectView(R.id.rel_choose_pc_sm)
    RelativeLayout relChoosePcSm;
    @InjectView(R.id.rel_choose_type_sm)
    RelativeLayout relChooseTypeSm;
    @InjectView(R.id.img_back_smac)
    ImageView imgBackSmac;

    private String[] name, nameid, piciName, piciId, Type;
    String year, mouth, day;

    private String NameId, PiciId, Shenmatype;
    private int Index;
    private Boolean isChoose = false;
    private final int HTTP_SUCC_GET_NAME = 2;
    private final int HTTP_SUCC_GET_PICI = 3;
    private final int HTTP_SUCC_SAVE = 4;
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
                        showToast("添加成功");
                        finish();
                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {

                }
            }
            if (msg.what == HTTP_SUCC_GET_NAME) {
                String jStringname = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
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

                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {

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

                }
            }
            if (msg.what == HTTP_SUCC_SAVE) {
                String jStringname = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jStringname);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        showToast("添加申码成功");
                        finish();

                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {
                    showToast("异常:" + e);
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenma);
        ButterKnife.inject(this);
        gettimee();
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan


    }

    /**
     * 获取今天系统日期
     */
    @SuppressWarnings("deprecation")
    public void gettimee() {

        @SuppressWarnings("deprecation")
        Time time = new Time();
        time.setToNow();
        year = time.year + "";
        mouth = time.month + 1 + "";
        day = time.monthDay + "";
        // String timenow = year + "-" + mouth + "-" + day;// 系统时间
        String initEndDateTime = time.format("%Y年%m月%d日 %H:%M");

    }

    public void postadd() {
        map.put("productCode", NameId);
        map.put("batchCode", PiciId);
        map.put("codeType", "1");
        map.put("applyDate", txtTimeAddshenma.getText().toString());
        map.put("num", edtNumsShenma.getText().toString());
        map.put("applyExplain", edtShuomingShenma.getText().toString());
        json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString
                (R.string.add_shenma), handler, HTTP_SUCC_SAVE, json, UserInfo.utoken);
    }

    public void getName() {
        new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.get_shenma_name), handler, HTTP_SUCC_GET_NAME);
    }

    public void getPici() {
        NameId = nameid[Index];

        new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.get_shenma_pici) + NameId, handler, HTTP_SUCC_GET_PICI);
    }

    public void showNameDoor(String[] strName, final int type) {
        //boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        SinglePicker<String> picker = new SinglePicker<>(this,
                false ? strName : strName);
        picker.setCanLoop(true);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setGravity(Gravity.CENTER);
        int with = (int) ((wh - (5 * UserInfo.Dp2px(ShenMaActivity.this, 10))));
        picker.setWidth(with - 170);
        picker.setTopLineColor(0xFF999999);
        picker.setTopLineHeight(1);
        picker.setTitleText(false ? "" : "");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(17);
        picker.setCancelTextColor(0xFF999999);
        picker.setCancelTextSize(17);
        picker.setSubmitTextColor(0xFF999999);
        picker.setSubmitTextSize(17);
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
                    edtNameSpShenma.setText(item);
                    isChoose = true;
                    NameId = nameid[index];
                }
                if (type == 2) {
                    txtNamePcShenma.setText(item);
                    PiciId = piciId[index];
                }
                if (type == 3) {
                    txtTypeShenma.setText(item);
                    Shenmatype = item;
                }

            }
        });
        picker.show();
    }

    @OnClick({R.id.img_back_smac, R.id.btn_save_smac, R.id.rel_choose_time, R.id.rel_choose_sp_sm, R.id.rel_choose_pc_sm, R.id.rel_choose_type_sm})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.img_back_smac:
                finish();
                break;
            case R.id.btn_save_smac:
                if (NameId != null && PiciId != null && Shenmatype != null && UserInfo.ShenmaTime.equals("true") && !edtShuomingShenma.getText().toString().isEmpty()) {
                    postadd();
                } else {
                    showToast("请完善相关信息");
                }
                break;
            case R.id.rel_choose_time:
                //dialogUtil.dateTimePicKDialog(txtTimeAddshenma);
                new DatePickerDialog(ShenMaActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtTimeAddshenma.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                        UserInfo.ShenmaTime = "true";

                    }
                }, Integer.parseInt(year), Integer.parseInt(mouth), Integer.parseInt(day)).show();

                break;
            case R.id.rel_choose_sp_sm:
                getName();
                break;
            case R.id.rel_choose_pc_sm:

                if (isChoose == true) {
                    getPici();
                } else {
                    showToast("请先选择商品名称");
                }

                break;
            case R.id.rel_choose_type_sm:
                Type = new String[2];
                Type[0] = "溯源";
                Type[1] = "防伪";
                showNameDoor(Type, 3);
                break;
        }
    }


}
