package app.zf.scan.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Map;

import app.zf.scan.com.BaseHelper.BaseFragment;
import app.zf.scan.com.BaseHelper.GetNetResult;
import app.zf.scan.com.BaseHelper.UserInfo;
import app.zf.scan.com.HttpUtils.HttpUtils;
import app.zf.scan.com.scanapp.ChangePassActivity;
import app.zf.scan.com.scanapp.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/6.
 */

public class MineFragment extends BaseFragment implements OnItemClickListener, OnDismissListener {

    @InjectView(R.id.img_inmg)
    LinearLayout imgInmg;
    @InjectView(R.id.rel_changepaaword)
    RelativeLayout relChangepaaword;

    @InjectView(R.id.rel_tuichu_mine)
    RelativeLayout relTuichuMine;
    protected ImmersionBar mImmersionBar;
    @InjectView(R.id.txt_nickname)
    TextView txtNickname;
    @InjectView(R.id.txt_factorname)
    TextView txtFactorname;
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


                        UserInfo.cleanLoginInfo(getActivity());
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        System.exit(0);
                        UserInfo.isexit = true;
//                        Intent intent=new Intent(getActivity(),LoginActivity.class);
//                        getActivity().startActivity(intent);
                    } else {
                        // showToast("旧密码错误");
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

        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.inject(this, view);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
        Map<String,Object>map=new HashMap<String, Object>();
        map=UserInfo.getLoginInfo(getActivity());
        String ss=map.get("nickName").toString();
        if (!map.get("nickName").toString().equals("")){
            txtNickname.setText(map.get("nickName").toString());
            UserInfo.nickNam=map.get("nickName").toString();
            UserInfo.factoryName=map.get("factoryName").toString();
        }

        txtFactorname.setText(map.get("factoryName").toString());
        mAlertView = new AlertView("退出", "你确定退出登录", "取消", new String[]{"确定"},
                null, getActivity(), AlertView.Style.Alert, this).setCancelable(true).setOnDismissListener(this);


        return view;
    }


    public static MineFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @OnClick({R.id.rel_changepaaword, R.id.rel_tuichu_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_changepaaword:
                Intent intent = new Intent(getActivity(), ChangePassActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rel_tuichu_mine:
                mAlertView.show();
                break;
        }
    }


    @Override
    public void onItemClick(Object o, int position) {
        if (position == 0) {
            new HttpUtils().DoGet(myOKHttp, getString(R.string.url_root) + getString(R.string.tuichu), handler, HTTP_SUCC);
        }


    }

    @Override
    public void onDismiss(Object o) {
        // new HttpUtils().DoGet(myOKHttp,getString(R.string.url_root)+getString(R.string.tuichu),handler,HTTP_SUCC);
    }
}
