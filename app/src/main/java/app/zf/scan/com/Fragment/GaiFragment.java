package app.zf.scan.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import app.zf.scan.com.Adapter.GaiMaFragmentAdapter;
import app.zf.scan.com.Adapter.HomeFragmentAdapter;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.scanapp.GaiMaActivity;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.scanapp.SerchActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/8.
 */

public class GaiFragment extends Fragment {
    @InjectView(R.id.txt_name_title_gaima)
    TextView txtNameTitleGaima;
    @InjectView(R.id.img_left_gaima)
    ImageView imgLeftGaima;
    @InjectView(R.id.img_right_gaima)
    ImageView imgRightGaima;
    @InjectView(R.id.tab_gaima)
    XTabLayout tabGaima;
    @InjectView(R.id.vp_gaima)
    ViewPager vpGaima;
    List<Fragment> fragments = new ArrayList<>();
    protected ImmersionBar mImmersionBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gaima, container, false);
        ButterKnife.inject(this, view);
        //ImmersionBar.with(this).statusBarColor(R.color.blue).init();

        initData();

        return view;
    }
    public void initData() {


        fragments.add(new GaiOneFragment());
        fragments.add(new GaiTwoFragment());
        fragments.add(new GaiThreeFragment());
        fragments.add(new GaiFourFragment());

        List<String> titless = new ArrayList<>();
        titless.add("全部");
        titless.add("待审核");
        titless.add("已通过");
        titless.add("未通过");

        //创建一个viewpager的adapter
        GaiMaFragmentAdapter adapter = new GaiMaFragmentAdapter(getChildFragmentManager(), fragments,titless);
        vpGaima.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tabGaima.setupWithViewPager(vpGaima);
    }

    public static GaiFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        GaiFragment fragment = new GaiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.img_left_gaima, R.id.img_right_gaima})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left_gaima:
                UserInfo.whichSerch=2;
                Intent intent1=new Intent(getActivity(), SerchActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.img_right_gaima:
                Intent intent=new Intent(getActivity(), GaiMaActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
