package app.zf.scan.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.tsy.sdk.myokhttp.MyOkHttp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.Adapter.HomeFragmentAdapter;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.MyAppcation;
import app.zf.scan.com.BaseHelper.ToJson;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.scanapp.LoginActivity;
import app.zf.scan.com.scanapp.MainActivity;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.scanapp.SerchActivity;
import app.zf.scan.com.scanapp.ShenMaActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/12.
 */

public class HomeFragment extends Fragment {
    @InjectView(R.id.txt_name_title_home)
    TextView txtNameTitleHome;
    @InjectView(R.id.img_serch_home)
    ImageView imgSerchHome;
    @InjectView(R.id.img_right_home)
    ImageView imgRightHome;
    @InjectView(R.id.rel_title_main)
    RelativeLayout relTitleMain;
    @InjectView(R.id.tab_essence)
    XTabLayout tab_essence;
    @InjectView(R.id.vp_essence)
    ViewPager vp_essence;
    List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        initData();

       // ImmersionBar.with(this).statusBarColor(R.color.blue).init();
        return view;
    }



    public void initData() {

        fragments.add(new HomeOneFragment());
        fragments.add(new HomeTwoFragment());
        fragments.add(new HomeThreeFragment());
        fragments.add(new HomeFourFragment());

        List<String> titless = new ArrayList<>();
        titless.add("全部");
        titless.add("待审核");
        titless.add("已通过");
        titless.add("未通过");

        //创建一个viewpager的adapter
        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getChildFragmentManager(), fragments, titless);
        vp_essence.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tab_essence.setupWithViewPager(vp_essence);
    }

    public static HomeFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.img_serch_home, R.id.img_right_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_serch_home:
                UserInfo.whichSerch=1;
                Intent intent=new Intent(getActivity(), SerchActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.img_right_home:
                Intent intents=new Intent(getActivity(), ShenMaActivity.class);
                getActivity().startActivity(intents);
                break;
        }
    }
}
