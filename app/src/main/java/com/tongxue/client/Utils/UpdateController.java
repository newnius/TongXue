package com.tongxue.client.Utils;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongxue.connector.ErrorCode;
import com.tongxue.connector.Msg;
import com.tongxue.connector.Objs.TXObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by newnius on 16-3-19.
 */
public class UpdateController {
    public static Msg checkForUpdate() {
        try {
            URL url = new URL("http://tongxue.jluapp.com/api.php?action=latest-version");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();


            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String readLine;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            is.close();
            br.close();
            conn.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            String versionId = root.path("version_id").asText();
            String minVersion = root.path("min_version").asText();
            String description = root.path("description").asText();
            String downloadUrl = root.path("download_url").asText();

            Msg msg = new Msg(ErrorCode.SUCCESS);
            TXObject info = new TXObject();
            info.set("versionID", versionId);
            info.set("minVersion", minVersion);
            info.set("description", description);
            info.set("downloadUrl", downloadUrl);
            msg.setObj(info);

            Log.i("update", new Gson().toJson(msg));
            return msg;

        } catch (Exception ex) {
            Log.e("checkForUpdate", ex.getMessage());
            return new Msg(ErrorCode.CONNECTION_FAIL);
        }


    }



    //下载文件
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "tongxue.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }




    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 0;
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }
}
