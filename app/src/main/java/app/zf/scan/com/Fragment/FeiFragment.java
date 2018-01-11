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

import app.zf.scan.com.Adapter.FeiMaFragmentAdapter;
import app.zf.scan.com.Adapter.GaiMaFragmentAdapter;
import app.zf.scan.com.BaseHelper.BaseFragment;
import app.zf.scan.com.BaseHelper.BaseFragmentTwo;
import app.zf.scan.com.scanapp.FeiMaActivity;
import app.zf.scan.com.scanapp.GaiMaActivity;
import app.zf.scan.com.scanapp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/8.
 */

public class FeiFragment extends Fragment {
    @InjectView(R.id.txt_name_title_feima)
    TextView txtNameTitleFeima;
    @InjectView(R.id.img_left_feima)
    ImageView imgLeftFeima;
    @InjectView(R.id.img_right_feima)
    ImageView imgRightFeima;
    @InjectView(R.id.tab_feima)
    XTabLayout tabFeima;
    @InjectView(R.id.vp_feima)
    ViewPager vpFeima;
    List<Fragment> fragments = new ArrayList<>();
    protected ImmersionBar mImmersionBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feima, container, false);
        ButterKnife.inject(this, view);


        initData();

        return view;
    }





    public void initData() {


        fragments.add(new FeiOneFragment());
        fragments.add(new FeiTwoFragment());
        fragments.add(new FeiThreeFragment());
        fragments.add(new FeiFourFragment());

        List<String> titless = new ArrayList<>();
        titless.add("全部");
        titless.add("待审核");
        titless.add("已通过");
        titless.add("未通过");

        //创建一个viewpager的adapter
        FeiMaFragmentAdapter adapter = new FeiMaFragmentAdapter(getChildFragmentManager(), fragments,titless);
        vpFeima.setAdapter(adapter);
        //将TabLayout和ViewPager关联起来
        tabFeima.setupWithViewPager(vpFeima);
    }
    public static FeiFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FeiFragment fragment = new FeiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.img_left_feima, R.id.img_right_feima})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left_feima:
                break;
            case R.id.img_right_feima:
                Intent intent=new Intent(getActivity(), FeiMaActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
