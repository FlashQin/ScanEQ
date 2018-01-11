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

import app.zf.scan.com.scanapp.GaiMaInfoActivity;
import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.scanapp.ShenMaInfoActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/14.
 */

public class GaiMaAllAdapter extends BaseAdapter {
    private List<Map<String, Object>> listdata = new ArrayList<>();
    private Context mcontext;

    public GaiMaAllAdapter(List<Map<String, Object>> list, Context contenx) {
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

            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_listview_gaima, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtNameGaione.setText(listdata.get(position).get("productInfo").toString());
        holder.txtScodeGaione.setText(listdata.get(position).get("productCode").toString());
       // holder.txtPcodeGaione.setText(listdata.get(position).get("batchCode").toString());
        int stat= Integer.parseInt( listdata.get(position).get("applyStatus").toString());
        if (stat==0){
            holder.imgGaione.setBackgroundResource(R.mipmap.ic_toexamine);
        }
        if (stat==1){
            holder.imgGaione.setBackgroundResource(R.mipmap.image_adopt);
        }
        if (stat==2){
            holder.imgGaione.setBackgroundResource(R.mipmap.ic_notthrough);
        }
        String date=listdata.get(position).get("applyDate").toString().substring(0,10);
        holder.txttimeGaione.setText(date);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, GaiMaInfoActivity.class);
                intent.putExtra("name", listdata.get(position).get("productInfo").toString());
                intent.putExtra("id", listdata.get(position).get("id").toString());
                intent.putExtra("pcode", listdata.get(position).get("batchInfo").toString());
                intent.putExtra("applyExplain", listdata.get(position).get("applyExplain").toString());
                mcontext.startActivity(intent);

            }
        });
        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.txt_one_nothing)
        TextView txtOneNothing;
        @InjectView(R.id.txt_name_gaione)
        TextView txtNameGaione;
        @InjectView(R.id.img_gaione)
        ImageView imgGaione;
        @InjectView(R.id.txt_scode_gaione)
        TextView txtScodeGaione;
        @InjectView(R.id.txt_time_gaione)
        TextView txttimeGaione;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
