package app.zf.scan.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.scanapp.ShenMaInfoActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/14.
 */

public class HomeAllAdapter extends BaseAdapter {
    private List<Map<String, Object>> listdata = new ArrayList<>();
    private Context mcontext;

    public HomeAllAdapter(List<Map<String, Object>> list, Context contenx) {
        this.listdata = list;
        this.mcontext = contenx;

    }
    public void setDataChanged(List<Map<String, Object>> list) {
        this.listdata = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_listview_homeone, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtNameHomeone.setText(listdata.get(position).get("productInfo").toString());
        if (listdata.get(position).get("codeType").toString().equals("1")){
            holder.txtTypeHomeone.setText("溯源");
        }else {
            holder.txtTypeHomeone.setText("防伪");
        }

        holder.txtNumsHomeone.setText(listdata.get(position).get("num").toString());
        holder.txtScodeHomeone.setText(listdata.get(position).get("productCode").toString());
        holder.txtPcodeHomeone.setText(listdata.get(position).get("batchCode").toString());
        String date=listdata.get(position).get("applyDate").toString().substring(0,10);
        holder.txtTimeHomeone.setText(date);
        String has=listdata.get(position).get("hasBoxCode").toString();
        if (has.equals("true")){
            holder.txtYouheHomeone.setText("有盒码");
        }else {
            holder.txtYouheHomeone.setText("无盒码");
        }
        int stat= Integer.parseInt( listdata.get(position).get("applyStatus").toString());
        if (stat==0){
            holder.imgone.setBackgroundResource(R.mipmap.ic_toexamine);
        }
        if (stat==1){
            holder.imgone.setBackgroundResource(R.mipmap.image_adopt);
        }
        if (stat==2){
            holder.imgone.setBackgroundResource(R.mipmap.ic_notthrough);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ShenMaInfoActivity.class);
                intent.putExtra("name", listdata.get(position).get("productInfo").toString());
                intent.putExtra("type", listdata.get(position).get("codeType").toString());
                intent.putExtra("nums", listdata.get(position).get("num").toString());
                intent.putExtra("pcode", listdata.get(position).get("batchInfo").toString());
                intent.putExtra("time", listdata.get(position).get("applyDate").toString());
                intent.putExtra("applyExplain", listdata.get(position).get("applyExplain").toString());
                mcontext.startActivity(intent);

            }
        });
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.txt_one_nothing)
        TextView txtOneNothing;
        @InjectView(R.id.txt_name_homeone)
        TextView txtNameHomeone;
        @InjectView(R.id._homeone)
        ImageView imgone;
        @InjectView(R.id.txt_type_homeone)
        TextView txtTypeHomeone;
        @InjectView(R.id.txt_nums_homeone)
        TextView txtNumsHomeone;
        @InjectView(R.id.txt_youhe_homeone)
        TextView txtYouheHomeone;
        @InjectView(R.id.txt_scode_homeone)
        TextView txtScodeHomeone;
        @InjectView(R.id.txt_pcode_homeone)
        TextView txtPcodeHomeone;
        @InjectView(R.id.txt_time_homeone)
        TextView txtTimeHomeone;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
