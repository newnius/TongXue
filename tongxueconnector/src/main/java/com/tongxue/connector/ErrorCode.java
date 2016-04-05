package com.tongxue.connector;

/**
 *
 * Created by newnius on 16-3-16.
 */
public class ErrorCode {
    public static final int SUCCESS = 0;
    public static final int CONNECTION_FAIL = 1;
    public static final int INVALID_DATA_FORMAT = 2;
    public static final int NOT_LOGED = 3;
    public static final int UNKNOWN = 4;


    public static final int TXOBJECT_IS_NULL = 5;


    /*login*/
    public static final int USER_NOT_EXIST = 6;
    public static final int USERNAME_IS_EMPTY = 7;

    public static final int PASSWORD_IS_EMPTY = 8;
    public static final int EMAIL_IS_EMPTY = 9;
    public static final int WRONG_PASSWORD = 10;
    public static final int NOT_VERIFIED = 11;

    /* register */
    public static final int INVALID_EMAIL = 12;//duplicate
    public static final int USERNAME_OCCUPIED = 13;
    public static final int EMAIL_OCCUPIED = 14;

    /* create group */
    public static final int GROUP_NAME_IS_EMPTY = 15;
    public static final int CATEGORY_IS_EMPTY = 16;
    public static final int INCOMPLETE_INFORMATION = 17;
    public static final int MAX_GROUP_JOINED_EXCEEDED = 18;

    /* search group by category */
    public static final int NO_SUCH_CATEGORY = 19;

    public static final int ALREADY_IN_PROCESS = 20;

    /* send group chat*/
    public static final int GROUP_NOT_EXIST = 21;
    public static final int NO_ACCESS = 22;


    public static final int WHITEBOARD_NOT_EXIST = 23;
    public static final int TITLE_IS_TOO_LONG = 24;
    public static final int TITLE_IS_EMPTY = 25;

    public static final int ARTICLE_NOT_EXIST = 26;

    public static final int CONTENT_IS_TOO_LONG = 27;
    public static final int CONTENT_IS_EMPTY = 28;

    public static final int TYPE_IS_EMPTY = 29;

    public static final int QUESTION_NOT_EXIST = 30;
    public static final int ANSWER_NOT_EXIST = 31;
    public static final int COMMENT_NOT_EXIST = 32;

    public static final int USERNAME_IS_INVALID = 33;
    public static final int EMAIL_IS_INVALID = 34;

    public static final int LENGTH_NOT_MATCH = 35;

    public static final int REJECTED = 36;

    public static final int ALREADY_IN_GROUP = 37;
    public static final int MESSAGE_IS_EMPTY = 38;

    public static final int CONNECTION_CREATED = 39;
    public static final int AUTH_FAIL = 40;

    public static final int DISCUSS_NAME_IS_EMPTY = 41;
    public static final int GROUP_ID_NOT_ASSIGNED = 42;
    public static final int DISCUSS_NOT_EXIST = 43;



    public static String getMsg(int errorCode){
        switch (errorCode){
            case SUCCESS:
                return "成功";
            case CONNECTION_FAIL:
                return "连接服务器失败，请重试";
            case INVALID_DATA_FORMAT:
                return "数据格式错误";
            case NOT_LOGED:
                return "未登录或离线";
            case UNKNOWN:
                return "未知错误";
            case TXOBJECT_IS_NULL:
                return "参数为空";
            case USER_NOT_EXIST:
                return "用户不存在";
            case USERNAME_IS_EMPTY:
                return "用户名为空";
            case PASSWORD_IS_EMPTY:
                return "密码未空";
            case EMAIL_IS_EMPTY:
                return "邮箱为空";
            case WRONG_PASSWORD:
                return "密码不正确";
            case NOT_VERIFIED:
                return "尚未验证";
            case INVALID_EMAIL:
                return "无效的邮箱";
            case USERNAME_OCCUPIED:
                return "用户名已被使用";
            case EMAIL_OCCUPIED:
                return "邮箱已被使用";

            case NO_ACCESS:
                return "没有权限";


            case USERNAME_IS_INVALID:
                return "用户名长度不符合要求";
            default:
                return "出现错误 (错误码:"+errorCode+")";
        }
    }



}
