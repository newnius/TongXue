package com.tongxue.client.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.Group.Create.KindActivity;
import com.tongxue.client.R;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/17.
 */
public class GroupMemActivity extends BaseActivity {
    @Bind(R.id.memIds)             ListView memIds;
    @Bind(R.id.actionbar_back)     ImageView back;

    public List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_mem);
        ButterKnife.bind(this);

        list= (List<String>)getIntent().getSerializableExtra("memIds");
        memIds.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));

        memIds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = list.get(position);
                Intent intent = new Intent(GroupMemActivity.this, GroupMemInfoActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                overridePendingTransition(R.anim.common_right_in, R.anim.common_left_out);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

    }
}
