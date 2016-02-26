package com.tongxue.client.Base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.controller.ChatManagerAdapter;
import com.avoscloud.leanchatlib.model.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;


/**
 * Created by chaosi on 2015/7/15.
 */
public class LearnApplication extends Application{

    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences= PreferenceManager.getDefaultSharedPreferences(this);

        /**LeanCloud设置**/
        AVOSCloud.useAVCloudCN();
        AVOSCloud.initialize(this,
                "eq77khmvfqdzvz7ciuxedpzvorr1xtqvxv489pjag4nkngu9",
                "bf2v9la2l4bp0z4oa5wdmsdzd2o2l538ul6fijx3pvxebj16");
        ChatManager.setDebugEnabled(true);      // tag leanchatlib
        AVOSCloud.setDebugLogEnabled(true);     // set false when release
        final ChatManager chatManager = ChatManager.getInstance();
        chatManager.init(this);
        chatManager.setChatManagerAdapter(new ChatManagerAdapter() {
            @Override
            public UserInfo getUserInfoById(String userId) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUsername(userId);
                return userInfo;
            }

            @Override
            public void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception {

            }

            //关于这个方法请见 leanchat 应用中的 ChatManagerAdapterImpl.java
            @Override
            public void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message) {
//                Toast.makeText(context, "您收到一条新消息!", Toast.LENGTH_LONG).show();
            }
        });
        initImageLoader(this);

    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
}
