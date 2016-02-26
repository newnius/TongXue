package com.tongxue.client.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tongxue.client.Base.BaseFragment;
import com.tongxue.client.Blog.BlogEditActivity;
import com.tongxue.client.Qa.QaEditBActivity;
import com.tongxue.client.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/10/8.
 */
public class WriteFragment extends BaseFragment {
    @Bind(R.id.writeAsk)   RelativeLayout writeAsk;
    @Bind(R.id.writeBlog)  RelativeLayout writeBlog;
    @Bind(R.id.fragment_write)  RelativeLayout fragment_write;
    public View mView;
    public Context c;
    public MainActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.init();
        c = getActivity();
        mContext = (MainActivity)getActivity();
        mView = inflater.inflate(R.layout.fragment_write, container, false);
        ButterKnife.bind(this, mView);

        writeAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(c, QaEditBActivity.class));
                mContext.changeFragment();
            }
        });

        writeBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(c, BlogEditActivity.class));
                mContext.changeFragment();
            }
        });

        fragment_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.changeFragment();
            }
        });
        return mView;

    }

}
