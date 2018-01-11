package app.zf.scan.com.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.GaiMaLittleAdapter;
import app.zf.scan.com.BaseHelper.BaseActivity;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.views.ZQListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class GaiMaInfoActivity extends BaseActivity {
    @InjectView(R.id.txt_name_gaimainfo)
    TextView txtNameGaimainfo;
    @InjectView(R.id.txt_pname_gaimainfo)
    TextView txtPnameGaimainfo;
    @InjectView(R.id.txt_shuoming_gaimainfo)
    TextView txtShuomingGaimainfo;
    @InjectView(R.id.listview_gaimainfo)
    ListView listviewGaimainfo;
    @InjectView(R.id.img_back_gaimainfo)
    ImageView imgBackGaimainfo;
    private GaiMaLittleAdapter adapter;

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
                        Map<String, Object> mapthis = new HashMap<String, Object>();
                        mapthis = (Map<String, Object>) map.get("d");
                        listdadta = (List<Map<String, Object>>) mapthis.get("changeCodeApplyNumList");
                        adapter = new GaiMaLittleAdapter(listdadta, GaiMaInfoActivity.this,false,handler);
                        listviewGaimainfo.setAdapter(adapter);
                        setHeight(adapter, listviewGaimainfo);
                    } else {
                        showToast(m);
                    }

                } catch (Exception e) {
                    showToast("异常:"+e);
                }
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaima_info);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
        init();
    }

    public void init() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String name = extras.getString("name");
        String id = extras.getString("id");
        String pcode = extras.getString("pcode");
        String applyExplain = extras.getString("applyExplain");

        txtNameGaimainfo.setText(name);
        txtPnameGaimainfo.setText(pcode);
        txtShuomingGaimainfo.setText(applyExplain);

        new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_info_get) + id, handler, HTTP_SUCC);

    }

    @OnClick(R.id.img_back_gaimainfo)
    public void onViewClicked() {
        finish();
    }
}
