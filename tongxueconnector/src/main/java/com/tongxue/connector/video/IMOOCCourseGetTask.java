package com.tongxue.connector.video;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tongxue.connector.course.Course;
import com.tongxue.connector.course.IMOOCCourse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by newnius on 16-3-14.
 */
public class IMOOCCourseGetTask {
    private int page = 1;
    public  List<Course> getList() {
        try {
            URL url = new URL("http://www.imooc.com/course/ajaxlist?pos_id=0&lange_id=0&is_easy=0&sort=last&pagesize=20&unlearn=0&page="+ page++);
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
            JsonNode list = root.path("list");

            for (JsonNode courseNode : list) {
                int id = courseNode.path("id").asInt();
                String name = courseNode.path("name").asText();
                String pic = courseNode.path("pic").asText();
                String category = courseNode.path("cat_id").asText();
                int duration = courseNode.path("duration").asInt();
                String duration_fmt = courseNode.path("duration_fmt").asText();
                String description = courseNode.path("description").asText();
                int members = courseNode.path("numbers").asInt();
                long lastUpdate = courseNode.path("update_time").asLong();

                IMOOCCourse imoocCourse = new IMOOCCourse(id, name, pic, category, duration, duration_fmt, description, members, lastUpdate);
                courses.add(imoocCourse);
            }

            Log.i("imooc-list", new Gson().toJson(courses));
            return courses;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
