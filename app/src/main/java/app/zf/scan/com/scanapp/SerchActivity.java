package app.zf.scan.com.scanapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.GaiMaAllAdapter;
import app.zf.scan.com.Adapter.HomeAllAdapter;
import app.zf.scan.com.BaseHelper.BaseActivity;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.views.MyDialog;
import app.zf.scan.com.views.XListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class SerchActivity extends BaseActivity implements XListView.IXListViewListener {
    @InjectView(R.id.img_back_serch)
    ImageView imgBackSerch;
    @InjectView(R.id.img_serch_serch)
    ImageView imgSerchSerch;
    @InjectView(R.id.edt_serch)
    EditText edtSerch;
    @InjectView(R.id.listview_serch)
    XListView listviewSerch;
    private HomeAllAdapter Hadapter;
    private GaiMaAllAdapter Gadapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HTTP_SUCC) {
                String jString = new String(msg.obj.toString());
                dialog.hide();
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jString);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        Map<String, Object> mapdata = new HashMap<String, Object>();
                        mapdata = (Map<String, Object>) map.get("d");
                        listdadta = (List<Map<String, Object>>) mapdata.get("content");
                        recommond = Integer.parseInt(mapdata.get("totalElements").toString());
                        if (recommond!=0) {
                            if (PageSize >= recommond) {
                                listviewSerch.setLoadALL(recommond);
                            }
                            if (listdadta.size() > 0) {
                                if (UserInfo.whichSerch == 1) {
                                    Hadapter = new HomeAllAdapter(listdadta, SerchActivity.this);
                                    listviewSerch.setAdapter(Hadapter);
                                } else {
                                    Gadapter = new GaiMaAllAdapter(listdadta, SerchActivity.this);
                                    listviewSerch.setAdapter(Gadapter);
                                }
                            }
                        }else {
                            showToast("没有该关键字的数据");
                            listviewSerch.setLoadALL(0);
                        }

                    }


                } catch (Exception e) {

                }
            }
            if (msg.what == HTTP_LISTVIEW_LOAD_MORE_SUCC) {

                String jString = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jString);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    PageIndex++;
                    if (c.equals("0")) {
                        Map<String, Object> mapdata = new HashMap<String, Object>();
                        mapdata = (Map<String, Object>) map.get("d");
                        if (mapdata.size() > 0) {
                            List<Map<String, Object>> tmpList = new ArrayList<>();
                            tmpList=   (List<Map<String, Object>>) mapdata.get("content");
                            listdadta.addAll(tmpList);
                            if (UserInfo.whichSerch==1){
                                Hadapter.setDataChanged(listdadta);
                                listviewSerch.stopLoadMore();
                                listviewSerch.stopRefresh();
                            }else {
                                Gadapter.setDataChanged(listdadta);
                                listviewSerch.stopLoadMore();
                                listviewSerch.stopRefresh();
                            }


                        }


                    } else {
                        // showToast(m);
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
        setContentView(R.layout.activity_serch);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan
        listviewSerch.setPullRefreshEnable(false);
        listviewSerch.setPullLoadEnable(true);
        listviewSerch.setXListViewListener(this);

    }


    @OnClick({R.id.img_back_serch, R.id.img_serch_serch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back_serch:
                finish();
                break;
            case R.id.img_serch_serch:
                if (!edtSerch.getText().toString().isEmpty()) {
                    dialog = MyDialog.showDialog(SerchActivity.this);
                    dialog.show();
                    if (UserInfo.whichSerch == 1) {
                        getShenma(HTTP_SUCC, 1, 20);
                    } else {
                        getGaima(HTTP_SUCC, 1, 20);
                    }

                } else {
                    showToast("请输入关键字");
                }

                break;
        }
    }

    public void getGaima(int x, int PageIndex, int PageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("applyStatus", "");
        map.put("pageNo", PageIndex);
        map.put("pageSize", PageSize);
        map.put("productInfo", edtSerch.getText().toString());
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_get_all),
                handler, x, json, UserInfo.utoken);
    }

    public void getShenma(int x, int PageIndex, int PageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("applyStatus", "");
        map.put("pageNo", PageIndex);
        map.put("pageSize", PageSize);
        map.put("productInfo", edtSerch.getText().toString());
        String token = UserInfo.utoken;
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.homepage_get_shenma),
                handler, x, json, UserInfo.utoken);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        if (PageIndex * 20 >= recommond) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    listviewSerch.setLoadALL(recommond);
                }
            }, 800);
        } else {
            if (UserInfo.whichSerch == 1) {
                getShenma(HTTP_LISTVIEW_LOAD_MORE_SUCC, PageIndex + 1, 20);
            } else {
                getGaima(HTTP_LISTVIEW_LOAD_MORE_SUCC, PageIndex + 1, 20);
            }

        }
    }
}
