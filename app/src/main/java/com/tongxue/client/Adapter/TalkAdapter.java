package com.tongxue.client.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import com.tongxue.client.R;
import com.tongxue.connector.Objs.TXObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/8/11.
 */
public class TalkAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<TXObject> list;

    public TalkAdapter(Context c, List<TXObject> list) {
        mInflater=LayoutInflater.from(c);
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_list_talk, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Log.i("position", position+"");
            holder.tv_name.setText( list.get(position).get("discussName"));
            holder.tv_kind.setText(list.get(position).get("category"));
            holder.tv_intro.setText(list.get(position).get("introduction"));
            holder.tv_num.setText(list.get(position).get("status"));
            //holder.image.setImageDrawable(R.drawable.head38);

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder{
        //@Bind(R.id.image)  ImageView image;
        @Bind(R.id.name)  TextView tv_name;
        @Bind(R.id.tv_kind)  TextView tv_kind;
        @Bind(R.id.tv_intro)  TextView tv_intro;
        @Bind(R.id.tv_num)  TextView tv_num;

        public ViewHolder(View mView){
            ButterKnife.bind(this, mView);
        }
    }

}