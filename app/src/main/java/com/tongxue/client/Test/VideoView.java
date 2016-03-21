package com.tongxue.client.Test;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;

import com.tongxue.client.R;

import butterknife.Bind;

public class VideoView extends AppCompatActivity {
    @Bind(R.id.bt_video)
    Button bt_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

                /* 设置横屏 */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        bt_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo("http://v1.mukewang.com/be7bbb43-514c-45a4-8d0d-7896d6287416/L.mp4");
            }
        });

        playVideo("http://v1.mukewang.com/be7bbb43-514c-45a4-8d0d-7896d6287416/L.mp4");
    }


    public void playVideo(String url){
        try {
            /* 设置播放视频时候不需要的部分 *//* 以下代码需要写在setContentView();之前 */

            /* 去掉title
            * requestFeature() must be called before adding content
            * */
            //requestWindowFeature(Window.FEATURE_NO_TITLE);


            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            /* 设置屏幕常亮 *//* flag：标记 ； */
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            android.widget.VideoView videoView = (android.widget.VideoView) findViewById(R.id.video_view);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            videoView.setMediaController(mc);
            // videoView.setVideoPath("file:///my.mp4");
            videoView.setVideoURI(Uri.parse(url));
            videoView.requestFocus();
            videoView.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


}
