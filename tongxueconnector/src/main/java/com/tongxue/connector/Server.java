package com.tongxue.connector;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongxue.connector.Objs.TXObject;
import com.tongxue.connector.course.Course;
import com.tongxue.connector.video.IMOOCCourseGetTask;

/**
 * @author Newnius
 */
 public class Server {
    public static final Msg errorMsg = new Msg(ErrorCode.TXOBJECT_IS_NULL);

    public static Msg login(TXObject user) {
        try {
            if (user == null)
                return errorMsg;
            return User.login(user);
        }catch(Exception ex){
            ex.printStackTrace();
            return errorMsg;
        }
    }

    public static Msg register(TXObject user) {
        try {
            if (user == null)
                return errorMsg;
            return User.register(user);
        }catch(Exception ex){
            ex.printStackTrace();
            return errorMsg;
        }
    }


    public static Msg createGroup(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.createGroup(group);
    }


    public static Msg searchGroup(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.searchGroup(group);
    }

    public static Msg updateGroup(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.updateGroup(group);
    }

    public static Msg dismissGroup(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.dismissGroup(group);
    }


    public static Msg searchGroupByUser(TXObject user) {
        if(user==null)
            return errorMsg;
        return Group.searchGroupByUser(user);
    }

    public static Msg applyGroup(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.applyGroup(group);
    }

    public static Msg quitGroup(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.quitGroup(group);
    }

    public static Msg sendGroupMessage(TXObject message) {
        if(message==null)
            return errorMsg;
        return Group.sendGroupMessage(message);
    }

    public static Msg getGroupMessage(TXObject group) {
        if(group==null)
            return errorMsg;
        return Group.getGroupMessage(group);
    }


    public static Msg sendBoardAction(String action) {
        return errorMsg;
    }

    public static Msg postArticle(TXObject article) {
        if(article==null)
            return errorMsg;
        return Article.postArticle(article);
    }

    public static Msg updateArticle(TXObject article) {
        if(article==null)
            return errorMsg;
        return Article.updateArticle(article);
    }


    public static Msg searchArticle(TXObject article) {
        if(article==null)
            return errorMsg;
        return Article.searchArticle(article);
    }


    public static Msg getHottestArticle(TXObject article) {
        return errorMsg;
    }

    public static Msg deleteArticle(TXObject article) {
        if(article==null)
            return errorMsg;
        return Article.deleteArticle(article);
    }

    public static Msg askQuestion(TXObject question) {
        if(question==null)
            return errorMsg;
        return Question.askQuestion(question);
    }

    public static Msg searchQuestion(TXObject question) {
        try {
            if (question == null)
                return errorMsg;
            return Question.searchQuestion(question);
        }catch(Exception ex){
            ex.printStackTrace();
            return errorMsg;
        }
    }

    public static Msg updateQuestion(TXObject question) {
        if(question==null)
            return errorMsg;
        return Question.updateQuestion(question);
    }

    public static Msg deleteQuestion(TXObject question) {
        if(question==null)
            return errorMsg;
        return Question.deleteQuestion(question);
    }

    public static Msg getQuestionAnswer(TXObject question) {
        try {
            if (question == null)
                return errorMsg;
            return Question.searchQuestionAnswer(question);
        }catch(Exception ex){
            ex.printStackTrace();
            return errorMsg;
        }
    }

    public static Msg answerQuestion(TXObject answer) {
        if(answer==null)
            return errorMsg;
        return Question.answerQuestion(answer);
    }

    public static Msg makeComment(TXObject comment) {
        if(comment==null)
            return errorMsg;
        return Article.commentAtArticle(comment);
    }

    public static Msg getCommentsByArticle(TXObject article) {
        if(article==null)
            return errorMsg;
        return Article.searchArticleComments(article);
    }

    public static List<Course> getCourses() {
        new IMOOCCourseGetTask().getList();
        return null;
    }

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

            List<Course> courses = new ArrayList<>();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            int versionId = root.path("version_id").asInt();
            String description = root.path("description").asText();
            String downloadUrl = root.path("download_url").asText();

            Msg msg = new Msg(versionId, downloadUrl);
            msg.setMsg(description);

            Log.i("update", new Gson().toJson(msg));
            return msg;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


    }


}
