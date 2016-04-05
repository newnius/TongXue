package com.tongxue.connector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.connector.Objs.TXObject;

import java.util.List;

/**
 *
 * Created by newnius on 16-3-16.
 */
public class Question {
    /*
    private int questionID;
    private String title;
    private String content;
    private String[] keywords;
    private int answerID = -1;
    private String author;
    private long time;
    private int  views;
    */
    /*
    	private int answerID;
	private int questionID;
	private String content;
	private String author;
	private long time;
	private boolean helpful = false;
	private int ups=0;
	private int downs=0;
     */

    private Question(){}

    public static Msg searchQuestion(TXObject question) {
        String con = new Gson().toJson(new Msg(RequestCode.SEARCH_QUESTION, question));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            String obj = new Gson().toJson(msg.getObj());
            List<TXObject> questions = new Gson().fromJson(obj, new TypeToken<List<TXObject>>() {}.getType());
            msg.setObj(questions);
        }
        return msg;
    }

    public static Msg askQuestion(TXObject question) {
        if (!question.hasKey("title"))
            return new Msg(ErrorCode.TITLE_IS_EMPTY);
        if (!question.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.ASK_QUESTION, question));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg updateQuestion(TXObject question) {
        if (!question.hasKey("questionID"))
            return new Msg(ErrorCode.QUESTION_NOT_EXIST);
        if (!question.hasKey("title"))
            return new Msg(ErrorCode.TITLE_IS_EMPTY);
        if (!question.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.UPDATE_QUESTION, question));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg deleteQuestion(TXObject question) {
        if (!question.hasKey("questionID"))
            return new Msg(ErrorCode.QUESTION_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.DELETE_QUESTION, question));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg searchQuestionAnswer(TXObject question) {
        if(!question.hasKey("questionID"))
            return new Msg(ErrorCode.QUESTION_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.SEARCH_QUESTION_ANSWER, question));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
            List<TXObject> answers = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<TXObject>>() {}.getType());
            msg.setObj(answers);
        }
        return msg;
    }

    public static Msg answerQuestion(TXObject answer) {
        if (!answer.hasKey("questionID"))
            return new Msg(ErrorCode.QUESTION_NOT_EXIST);
        if (!answer.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.ANSWER_QUESTION, answer));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg updateAnswer(TXObject answer) {
        if (!answer.hasKey("answerID"))
            return new Msg(ErrorCode.ANSWER_NOT_EXIST);
        if (!answer.hasKey("title"))
            return new Msg(ErrorCode.TITLE_IS_EMPTY);
        if (!answer.hasKey("content"))
            return new Msg(ErrorCode.CONTENT_IS_EMPTY);

        String con = new Gson().toJson(new Msg(RequestCode.UPDATE_QUESTION_ANSWER, answer));
        String res = Communicator.send(con);
        Msg msg;
        if (res == null) {
            msg = new Msg(ErrorCode.CONNECTION_FAIL);
        } else {
            msg = new Gson().fromJson(res, Msg.class);
        }
        return msg;
    }

    public static Msg deleteAnswer(TXObject answer) {
        if (!answer.hasKey("answerID"))
            return new Msg(ErrorCode.ANSWER_NOT_EXIST);
        String con = new Gson().toJson(new Msg(RequestCode.DELETE_QUESTION, answer));
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
