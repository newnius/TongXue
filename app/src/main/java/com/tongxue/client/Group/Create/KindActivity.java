package com.tongxue.client.Group.Create;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/9/17.
 */
public class KindActivity extends BaseActivity {
    @Bind(R.id.actionbar_back)  ImageView back;
    @Bind(R.id.listView)        ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_kind);
        ButterKnife.bind(this);

        final String autho= getIntent().getStringExtra("autho");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        final String[] arr= getResources().getStringArray(R.array.groupKind);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, Arrays.asList(arr)));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(KindActivity.this, InfoActivity.class);
                intent.putExtra("autho",autho);
                intent.putExtra("kind", arr[position]);
                startActivity(intent);
                overridePendingTransition(R.anim.common_right_in, R.anim.empty);
            }
        });

    }


}
