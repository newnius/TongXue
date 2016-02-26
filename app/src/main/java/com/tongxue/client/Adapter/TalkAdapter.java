package com.tongxue.client.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import java.util.Map;
import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/8/11.
 */
public class TalkAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Map<String, Object>> mAppList;

    public TalkAdapter(Context c, List<Map<String, Object>> appList) {
        mInflater=LayoutInflater.from(c);
        mAppList=appList;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView==null){
            convertView = mInflater.inflate(R.layout.item_list_talk, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.image.setImageDrawable((Drawable)mAppList.get(position).get("img"));
        holder.tv_name.setText((String)mAppList.get(position).get("name"));
        holder.tv_kind.setText((String)mAppList.get(position).get("kind"));
        holder.tv_intro.setText("签名:"+mAppList.get(position).get("intro"));
        holder.tv_num.setText(mAppList.get(position).get("num").toString());
        return convertView;
    }

    public class ViewHolder{
        @Bind(R.id.image)  ImageView image;
        @Bind(R.id.name)  TextView tv_name;
        @Bind(R.id.tv_kind)  TextView tv_kind;
        @Bind(R.id.tv_intro)  TextView tv_intro;
        @Bind(R.id.tv_num)  TextView tv_num;

        public ViewHolder(View mView){
            ButterKnife.bind(this, mView);
        }
    }

}