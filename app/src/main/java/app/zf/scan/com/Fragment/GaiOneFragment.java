package app.zf.scan.com.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.GaiMaAllAdapter;
import app.zf.scan.com.Adapter.HomeAllAdapter;
import app.zf.scan.com.BaseHelper.BaseFragment;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.views.MyDialog;
import app.zf.scan.com.views.XListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class GaiOneFragment extends BaseFragment implements XListView.IXListViewListener {
    @InjectView(R.id.xlistview_gaione)
    XListView xlistviewGaione;
    GaiMaAllAdapter adapter;
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
                            if (PageIndexAdd>=recommond){
                                xlistviewGaione.setLoadALL(recommond);
                            }
                            listdadta = (List<Map<String, Object>>) mapdata.get("content");
                            adapter = new GaiMaAllAdapter(listdadta, getActivity());
                            xlistviewGaione.setAdapter(adapter);
                            xlistviewGaione.setVisibility(View.VISIBLE);
                            //xlistviewGaione.stopLoadMore();
                            xlistviewGaione.stopRefresh();
                            dialog.hide();
                        }else {

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
                            tmpList=   (List<Map<String, Object>>) mapdata.get("content");
                            int other=tmpList.size();
                            int old=listdadta.size();
                            listdadta.addAll(tmpList);
                            int neww=listdadta.size();
                            adapter.setDataChanged(listdadta);
                            //adapter=new HomeAllAdapter(listdadta,getActivity());
                            // xlistviewHomeone.setAdapter(adapter);
                            //  xlistviewHomeone.setVisibility(View.VISIBLE);
                            xlistviewGaione.stopLoadMore();
                            xlistviewGaione.stopRefresh();
                            dialog.hide();
                        }


                    } else {
                        // showToast(m);
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "异常:" + e, Toast.LENGTH_SHORT).show();
                }}
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gai_one, container, false);
        ButterKnife.inject(this, view);
        dialog = MyDialog.showDialog(getActivity());
        dialog.show();
        xlistviewGaione.setPullRefreshEnable(true);
        xlistviewGaione.setPullLoadEnable(true);
        xlistviewGaione.setXListViewListener(this);
        getdata(HTTP_SUCC,1,PageIndexAdd);

        return view;

    }

    public void getdata(int x,int PageIndex,int PageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("applyStatus", "");
        map.put("pageNo", PageIndex);
        map.put("pageSize", PageSize);
        String json = ToJson.mapToJson(map);
        new HttpUtils().DoPostJson(myOKHttp, getString(R.string.url_root) + getString(R.string.gaima_get_all),
                handler, HTTP_SUCC, json, UserInfo.utoken);
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
                    xlistviewGaione.setLoadALL(recommond);
                }
            }, 800);
        } else {
            getdata(HTTP_LISTVIEW_LOAD_MORE_SUCC,PageIndex+1,PageIndexAdd);
        }
    }
}