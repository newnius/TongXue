package com.tongxue.client.Utils;


import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * Created by chaosi on 2015/10/9.
 */
public class HttpUtils {

    public static FinalHttp finalHttp;
    public static String preUrl = "http://127.0.0.2/Learn/laravel/public/";

    public static void get(String url, AjaxCallBack callBack){
        url = preUrl + url;
        if(finalHttp == null){
            finalHttp = new FinalHttp();
            finalHttp.addHeader("Accept-Charset", "UTF-8");
            finalHttp.configCharset("UTF-8");
        }
        finalHttp.get(url, callBack);
    }

    public static void get(String url, AjaxParams params, AjaxCallBack callBack){
        url = preUrl + url;
        if(finalHttp == null){
            finalHttp = new FinalHttp();
            finalHttp.addHeader("Accept-Charset", "UTF-8");
            finalHttp.configCharset("UTF-8");
        }
        finalHttp.get(url, params, callBack);
    }

    public static void post(String url, AjaxParams params, AjaxCallBack callBack){
        url = preUrl + url;
        if(finalHttp == null){
            finalHttp = new FinalHttp();
            finalHttp.addHeader("Accept-Charset", "UTF-8");
            finalHttp.configCharset("UTF-8");
        }
        finalHttp.post(url, params, callBack);
    }


}
