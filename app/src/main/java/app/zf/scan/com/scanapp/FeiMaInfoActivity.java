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
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class FeiMaInfoActivity extends BaseActivity {
    @InjectView(R.id.txt_type_feimainfo)
    TextView txtTypeFeimainfo;
    @InjectView(R.id.txt_shuomong_feimainfo)
    TextView txtShuomongFeimainfo;
    @InjectView(R.id.listview_feimainfo)
    ListView listviewFeimainfo;
    @InjectView(R.id.img_back_feiinfo)
    ImageView back;
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
                        listdadta = (List<Map<String, Object>>) mapthis.get("destroyCodeApplyNumList");
                        adapter = new GaiMaLittleAdapter(listdadta, FeiMaInfoActivity.this,false,handler);
                        listviewFeimainfo.setAdapter(adapter);
                        setHeight(adapter, listviewFeimainfo);
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
        setContentView(R.layout.activity_feima_info);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
        init();
    }

    public void init() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");
        String applyExplain = extras.getString("applyExplain");

        txtShuomongFeimainfo.setText(applyExplain);
        new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.feima_get_info) + id, handler, HTTP_SUCC);

    }

    @OnClick(R.id.img_back_feiinfo)
    public void onViewClicked() {
        finish();
    }
}
