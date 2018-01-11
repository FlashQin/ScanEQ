package app.zf.scan.com.Fragment;


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
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.views.XListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/12.
 */

public class HomeTwoFragment extends BaseFragment implements XListView.IXListViewListener {
    protected MyOkHttp myOKHttp = MyAppcation.getInstance().getMyOkHttp();
    List<Map<String, Object>> listdadta = new ArrayList<Map<String, Object>>();
    protected final int HTTP_SUCC = 1;
    @InjectView(R.id.xlistview_hometwo)
    XListView xlistviewHometwo;
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
                        recommond= Integer.parseInt(mapdata.get("totalElements").toString());
                        if (mapdata.size() > 0) {

                            listdadta = (List<Map<String, Object>>) mapdata.get("content");
                            adapter = new HomeAllAdapter(listdadta, getActivity());
                            xlistviewHometwo.setAdapter(adapter);
                            xlistviewHometwo.stopLoadMore();
                            xlistviewHometwo.stopRefresh();
                            if (PageIndexAdd>=recommond){
                                xlistviewHometwo.setLoadALL(recommond);
                            }
                        }


                    } else {
                        // showToast(m);
                    }

                } catch (Exception e) {
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
                            int other = tmpList.size();
                            int old = listdadta.size();
                            listdadta.addAll(tmpList);
                            int neww = listdadta.size();
                            adapter.setDataChanged(listdadta);
                            //adapter=new HomeAllAdapter(listdadta,getActivity());
                            // xlistviewHomeone.setAdapter(adapter);
                            //  xlistviewHomeone.setVisibility(View.VISIBLE);
                            xlistviewHometwo.stopLoadMore();
                            xlistviewHometwo.stopRefresh();
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
        View view = inflater.inflate(R.layout.fragment_home_two, container, false);
        ButterKnife.inject(this, view);
        xlistviewHometwo.setPullRefreshEnable(true);
        xlistviewHometwo.setPullLoadEnable(true);
        xlistviewHometwo.setXListViewListener(this);
        getdata(HTTP_SUCC,1,PageIndexAdd);
        return view;
    }
    public void getdata(int x,int PageIndex,int PageSize) {

        Map<String, Object> map = new HashMap<>();
        map.put("applyStatus", "0");
        //map.put("factoryCode","0");
        map.put("pageNo", PageIndex);
        map.put("pageSize", PageSize);
        //map.put("productInfo","0");
        String token=UserInfo.utoken;
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
        getdata(HTTP_SUCC,1,PageIndexAdd);
    }

    @Override
    public void onLoadMore() {
        if (PageIndex * PageIndexAdd >= recommond) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    xlistviewHometwo.setLoadALL(recommond);
                }
            }, 800);
        } else {
            getdata(HTTP_LISTVIEW_LOAD_MORE_SUCC,PageIndex+1,PageIndexAdd);
        }
    }
}
