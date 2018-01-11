package app.zf.scan.com.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.zf.scan.com.scanapp.R;
import app.zf.scan.com.views.ZQView;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/14.
 */

public class GaiMaLittleAdapter extends BaseAdapter {
    private List<Map<String, Object>> listdata = new ArrayList<>();
    private Context mcontext;
    private Boolean Type;
    private Handler mhandler;

    public GaiMaLittleAdapter(List<Map<String, Object>> list, Context contenx, Boolean type,Handler handler) {
        this.listdata = list;
        this.mcontext = contenx;
        this.Type = type;
        this.mhandler=handler;

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
        ViewHolder holder ;
        ZQView slideView = (ZQView) convertView;
        if (slideView == null) {

            View itemView = LayoutInflater.from(mcontext).inflate(R.layout.item_listview_gaima_ac, null);
            slideView = new ZQView(mcontext);
            slideView.setContentView(itemView);
            holder = new ViewHolder(slideView);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }

        holder.txtPiciCodeItemGaima.setText(listdata.get(position).get("fileName").toString());
        if (Type == true) {
            if (listdata.get(position).get("startPage").toString().equals(listdata.get(position).get("endPage").toString())) {
                holder.txtPageItemGaima.setText(listdata.get(position).get("startPage").toString());
            } else {
                holder.txtPageItemGaima.setText(listdata.get(position).get("startPage").toString() + "-" + listdata.get(position).get("endPage").toString());
            }
        } else {
            holder.txtPageItemGaima.setText(listdata.get(position).get("startPage").toString());
        }

        holder.txtNumsItemGaima.setText(listdata.get(position).get("num").toString());
        holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = Message.obtain();
                message.what = 8;
                message.obj = String.valueOf(position);
                synchronized (mhandler) {
                    mhandler.sendMessage(message);
                }
            }
        });

        return slideView;
    }


    private static class ViewHolder {
        public  TextView txtPiciCodeItemGaima;
        public     TextView txtPageItemGaima;
        public      TextView txtNumsItemGaima;
        public ViewGroup deleteHolder;
         ViewHolder(View view) {
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
             txtPiciCodeItemGaima= (TextView) view.findViewById(R.id.txt_pici_code_item_gaima);
             txtPageItemGaima= (TextView) view.findViewById(R.id.txt_page_item_gaima);
             txtNumsItemGaima= (TextView) view.findViewById(R.id.txt_nums_item_gaima);
        }
    }
}
