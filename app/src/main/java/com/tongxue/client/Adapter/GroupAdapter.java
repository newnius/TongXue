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
 *
 * Created by chaosi on 2015/8/11.
 */
public class GroupAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Map<String, Object>> mAppList;

    public GroupAdapter(Context c, List<Map<String, Object>> appList) {
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
            convertView = mInflater.inflate(R.layout.item_list_group, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        holder.image.setImageDrawable((Drawable)mAppList.get(position).get("img"));
        holder.tv_name.setText((String)mAppList.get(position).get("name"));
        holder.tv_kind.setText((String)mAppList.get(position).get("kind"));
        holder.tv_intro.setText("介绍:"+mAppList.get(position).get("intro"));

        return convertView;
    }

    public class ViewHolder{
        @Bind(R.id.image)      ImageView image;
        @Bind(R.id.name)       TextView tv_name;
        @Bind(R.id.tv_kind)    TextView tv_kind;
        @Bind(R.id.tv_intro)   TextView tv_intro;

        public ViewHolder(View mView){
            ButterKnife.bind(this, mView);
        }
    }

}