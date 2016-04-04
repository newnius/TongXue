package com.tongxue.connector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.connector.Objs.TXObject;

import java.util.List;

/**
 *
 * Created by newnius on 16-3-16.
 */
public class Article {
    /*
    private int articleID;
    private String title;
    private String content;
    private long time;
    private String author;
    private int category;
    private String cover;
    private int views;
    private int ups;
    */

    /*
    private int commentID;
    private int articleID;
    private String content;
    private String author;
    private long time;
    */

    private Article(){}

    public static Msg searchArticle(TXObject article) {
        String con = new Gson().toJson(new Msg(RequestCode.SEARCH_ARTICLE, article));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            String obj = new Gson().toJson(msg.getObj());
            List<TXObject> articles = new Gson().fromJson(obj, new TypeToken<List<TXObject>>() {}.getType());
            msg.setObj(articles);
        }
        return msg;
    }

    public static Msg postArticle(TXObject article) {
        if (!article.hasKey("title"))
            return new Msg(ErrorCode.TITLE_IS_EMPTY);
        if (!article.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.POST_ARTICLE, article));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg updateArticle(TXObject article) {
        if (!article.hasKey("articleID"))
            return new Msg(ErrorCode.ARTICLE_NOT_EXIST);
        if (!article.hasKey("title"))
            return new Msg(ErrorCode.TITLE_IS_EMPTY);
        if (!article.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.UPDATE_ARTICLE, article));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg deleteArticle(TXObject article) {
        if (!article.hasKey("articleID"))
            return new Msg(ErrorCode.ARTICLE_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.DELETE_ARTICLE, article));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg searchArticleComments(TXObject article) {
        if(!article.hasKey("articleID"))
            return new Msg(ErrorCode.ARTICLE_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.SEARCH_ARTICLE_COMMENT, article));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            String obj = new Gson().toJson(msg.getObj());
            List<TXObject> comments = new Gson().fromJson(obj, new TypeToken<List<TXObject>>() {}.getType());
            msg.setObj(comments);
        }
        return msg;
    }

    public static Msg commentAtArticle(TXObject comment) {
        if(!comment.hasKey("articleID"))
            return new Msg(ErrorCode.ARTICLE_NOT_EXIST);
        if (!comment.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.COMMENT_AT_ARTICLE, comment));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg updateArticleComment(TXObject comment) {
        if (!comment.hasKey("commentID"))
            return new Msg(ErrorCode.COMMENT_NOT_EXIST);
        if (!comment.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.UPDATE_ARTICLE_COMMENT, comment));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg deleteArticleComment(TXObject comment) {
        if (!comment.hasKey("commentID"))
            return new Msg(ErrorCode.COMMENT_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.DELETE_ARTICLE_COMMENT, comment));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }


}
