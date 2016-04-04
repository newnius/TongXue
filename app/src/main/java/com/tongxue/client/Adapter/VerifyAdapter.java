package com.tongxue.client.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tongxue.client.R;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by chaosi on 2015/7/17.
 */

public class VerifyAdapter extends BaseAdapter {

    private class buttonViewHolder {
        ImageView image;
        TextView text;
        Button button;
    }

    private class bottomHolder{
        TextView text;
        ProgressBar progressBar;
    }

    private LayoutInflater mInflater;
    private Context mContext;
    private List<Map<String, Object>> mAppList;
    private buttonViewHolder holder;
    private bottomHolder bHolder;
    private VerifyInterface verifyInterface;

    public VerifyAdapter(Context c, VerifyInterface mInterface, List<Map<String, Object>> appList) {
        mContext = c;
        verifyInterface = mInterface;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAppList = appList;
    }

    //    @Override
    public int getCount() {
        return mAppList.size()+1;
    }

    //  @Override
    public Object getItem(int position) {
        return mAppList.get(position);
    }

    //@Override
    public long getItemId(int position) {
        return position;
    }

    //  @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int mSize=mAppList.size();

        if(position==mSize){
            convertView = mInflater.inflate(R.layout.item_list_verify_bottom, null);
            bHolder = new bottomHolder();
            bHolder.text = (TextView)convertView.findViewById(R.id.tv_more);
            bHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.loading);
            convertView.setTag(bHolder);

            bHolder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bHolder.text.setVisibility(View.GONE);
                    bHolder.progressBar.setVisibility(View.VISIBLE);
                    verifyInterface.loadMore(bHolder.text, bHolder.progressBar);
                }
            });

        }else{
            convertView = mInflater.inflate(R.layout.item_list_verify, null);
            holder = new buttonViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.image);
            holder.text = (TextView)convertView.findViewById(R.id.msg);
            holder.button = (Button)convertView.findViewById(R.id.agree);
            convertView.setTag(holder);

            Map<String, Object> appInfo = mAppList.get(position);
            if (appInfo != null) {
                String groupName = (String)appInfo.get("text");
                boolean hasAgree=(boolean)appInfo.get("hasAgree");
                holder.image.setImageDrawable((Drawable)mAppList.get(position).get("img"));
                holder.text.setText(groupName);
                if(hasAgree){
                    holder.button.setText("已同意");
                    holder.button.setTextColor(mContext.getResources().getColor(R.color.no));
                    holder.button.setBackground(mContext.getResources().getDrawable(R.drawable.shape_button_search));
                }else{
                    holder.button.setText("同意");
                    holder.button.setTextColor(Color.WHITE);
                    holder.button.setBackground(mContext.getResources().getDrawable(R.drawable.group_verify_button_shape));
                }

                if(!hasAgree){
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verifyInterface.agree(position);
                        }
                    });
                }
            }
        }

        return convertView;
    }

    public interface VerifyInterface{

        public void agree(int position);
        public void loadMore(TextView textView, ProgressBar progressBar);

    }
}