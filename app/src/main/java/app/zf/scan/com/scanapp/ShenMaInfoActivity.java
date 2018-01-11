package app.zf.scan.com.scanapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import app.zf.scan.com.BaseHelper.BaseActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/13.
 */

public class ShenMaInfoActivity extends BaseActivity {
    @InjectView(R.id.txt_name_shenma_info)
    TextView txtNameShenmaInfo;
    @InjectView(R.id.txt_pcodename_shenma_info)
    TextView txtPcodenameShenmaInfo;
    @InjectView(R.id.txt_type_shenma_info)
    TextView txtTypeShenmaInfo;
    @InjectView(R.id.txt_time_shenma_info)
    TextView txtTimeShenmaInfo;
    @InjectView(R.id.txt_nums_shenma_info)
    TextView txtNumsShenmaInfo;
    @InjectView(R.id.txt_shuoming_shenma_info)
    TextView txtShuomingShenmaInfo;
    @InjectView(R.id.img_back_shenmainfo)
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shenma_info);
        ButterKnife.inject(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).init();//zhuangtailan

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String name = extras.getString("name");
        String type = extras.getString("type");
        String pcode = extras.getString("pcode");
        String nums = extras.getString("nums");
        String time = extras.getString("time");
        String applyExplain = extras.getString("applyExplain");
        txtNameShenmaInfo.setText(name);
        txtNumsShenmaInfo.setText(nums);
        txtPcodenameShenmaInfo.setText(pcode);
        if (type.equals("1")) {
            txtTypeShenmaInfo.setText("溯源");
        } else {
            txtTypeShenmaInfo.setText("防伪");
        }

        txtTimeShenmaInfo.setText(time);
        txtShuomingShenmaInfo.setText(applyExplain);

    }

    @OnClick(R.id.img_back_shenmainfo)
    public void onViewClicked() {
        finish();
    }
}
