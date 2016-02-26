package com.tongxue.client.Qa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tongxue.client.Base.BaseActivity;
import com.tongxue.client.R;
import com.tongxue.client.View.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chaosi on 2015/10/8.
 */
public class QaEditBActivity extends BaseActivity {
    @Bind(R.id.back)      ImageView back;
    @Bind(R.id.next)      TextView next;
    @Bind(R.id.RelaTv)    TextView relaTv;
    @Bind(R.id.brief)     EditText briefEt;
    @Bind(R.id.listView)  NoScrollListView listView;
    public String brief;
    public List<Map<String, Object>> list;
    public SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_edit_b);
        ButterKnife.bind(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThisActivity();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brief = briefEt.getText().toString().trim();
                if(brief.equals("")){
                    toast("您还没有描述您的问题呢");
                }else{
                    Intent intent = new Intent(QaEditBActivity.this, QaEditDActivity.class);
                    intent.putExtra("brief", brief);
                    startActivity(intent);
                    overridePendingTransition(R.anim.common_right_in, R.anim.empty);
                }

            }
        });

        briefEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                    relaTv.setVisibility(View.GONE);
                    list.clear();
                    adapter.notifyDataSetChanged();
                }else{
                    relaTv.setVisibility(View.VISIBLE);
                    getData(new Random().nextInt(5));
                }
            }
        });

        list = new ArrayList<>();
        adapter = new SimpleAdapter(this, list, R.layout.item_list_qa_rec, new String[]{"askQue", "asker", "askTime"}, new int[]{R.id.askQue, R.id.asker, R.id.askTime});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    public void getData(int num){
        list.clear();
        for(int i=0; i< num; i++){
            Map map = new HashMap();
            map.put("askQue", "这是推荐的问题"+i);
            map.put("asker", "提问者：Super pan");
            map.put("askTime", "2015年10月12日");
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }
}
