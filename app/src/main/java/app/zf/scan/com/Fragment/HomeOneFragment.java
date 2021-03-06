package app.zf.scan.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.HomeAllAdapter;
import app.zf.scan.com.BaseHelper.BaseFragment;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.MyAppcation;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.scanapp.LoginActivity;
import app.zf.scan.com.scanapp.MainActivity;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.views.MyDialog;
import app.zf.scan.com.views.XListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.content.Context.MODE_PRIVATE;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/12.
 */

public class HomeOneFragment extends BaseFragment implements XListView.IXListViewListener {
    @InjectView(R.id.xlistview_homeone)
    XListView xlistviewHomeone;
    private int mType = 0;

    public void setType(int mType) {
        this.mType = mType;
    }

    protected MyOkHttp myOKHttp = MyAppcation.getInstance().getMyOkHttp();
    List<Map<String, Object>> listdadta = new ArrayList<Map<String, Object>>();
    protected final int HTTP_SUCC = 1;
    private HomeAllAdapter adapter;
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
                        Map<String, Object> mapdata = new HashMap<String, Object>();
                        mapdata = (Map<String, Object>) map.get("d");
                        recommond = Integer.parseInt(mapdata.get("totalElements").toString());
                        if (PageIndexAdd >= recommond) {
                            xlistviewHomeone.setLoadALL(recommond);
                        }
                        if (mapdata.size() > 0) {
                            listdadta = (List<Map<String, Object>>) mapdata.get("content");
                            adapter = new HomeAllAdapter(listdadta, getActivity());
                            xlistviewHomeone.setAdapter(adapter);
                            xlistviewHomeone.setVisibility(View.VISIBLE);
                            xlistviewHomeone.stopLoadMore();
                            xlistviewHomeone.stopRefresh();
                            dialog.hide();
                        }


                    }
                    if (c.equals("1004")) {
                        // Toast.makeText(getActivity(),"NeedLogin",Toast.LENGTH_SHORT).show();
                        login(handler);
                    }

                } catch (Exception e) {
                    //Toast.makeText(getActivity(), "异常:" + e, Toast.LENGTH_SHORT).show();
                }
            }
            if (msg.what == HTTP_LISTVIEW_SUCC) {
                String jString = new String(msg.obj.toString());
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map = GetNetResult.GetJosn(jString);
                    String c = map.get("c").toString();
                    String m = map.get("m").toString();
                    if (c.equals("0")) {
                        // Toast.makeText(getActivity(),"LoginSCUU",Toast.LENGTH_SHORT).show();

                        getdata(HTTP_SUCC, PageIndex, PageIndexAdd);
                    } else {
                        dialog.hide();
                        UserInfo.cleanLoginInfo(getActivity());
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        getActivity(). startActivity(intent);
                        Toast.makeText(getActivity(), "此账号密码已变更,重新登录", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    dialog.hide();
                    Toast.makeText(getActivity(), "异常:" + e, Toast.LENGTH_SHORT).show();


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
                            tmpList = (List<Map<String, Object>>) mapdata.get("content");
                            listdadta.addAll(tmpList);
                            adapter.setDataChanged(listdadta);
                            xlistviewHomeone.stopLoadMore();
                            xlistviewHomeone.stopRefresh();
                            dialog.hide();
                        }


                    } else {
                        // showToast(m);
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "异常:" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_one, container, false);
        ButterKnife.inject(this, view);
        dialog = MyDialog.showDialog(getActivity());
        dialog.show();
        xlistviewHomeone.setPullRefreshEnable(true);
        xlistviewHomeone.setPullLoadEnable(true);
        xlistviewHomeone.setXListViewListener(this);
        getdata(HTTP_SUCC, 1, PageIndexAdd);
        return view;


    }

    public void login(Handler handler) {

        sp = getActivity().getSharedPreferences("USER_INFO", MODE_PRIVATE);
        String phone = sp.getString("account", null);
        String utoken = sp.getString("utoken", null);
        String password = sp.getString("password", null);
        map.put("userName", phone);
        map.put("password", password);
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.login), handler, HTTP_LISTVIEW_SUCC, json, "");
    }

    public void getdata(int x, int PageIndex, int PageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("applyStatus", "");
        //map.put("factoryCode","0");
        map.put("pageNo", PageIndex);
        map.put("pageSize", PageSize);
        //map.put("productInfo","0");
        String token = UserInfo.utoken;
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.homepage_get_shenma),
                handler, x, json, UserInfo.utoken);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onRefresh() {
        getdata(HTTP_SUCC, 1, PageIndexAdd);
    }

    @Override
    public void onLoadMore() {
        if (PageIndex * PageIndexAdd >= recommond) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    xlistviewHomeone.setLoadALL(recommond);
                }
            }, 800);
        } else {
            getdata(HTTP_LISTVIEW_LOAD_MORE_SUCC, PageIndex + 1, PageIndexAdd);
        }
    }
}
