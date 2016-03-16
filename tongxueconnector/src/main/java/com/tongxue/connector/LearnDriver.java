package com.tongxue.connector;

import com.google.gson.Gson;


/**
 *
 * @author Newnius
 */
public class LearnDriver implements  Runnable{

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

    @Override
    public void run() {
        //Server.changeIp("114.215.146.189");


        //System.out.println(Server.register("newnius", "123456", "newnius1@jlu.edu.cn"));
/*        Msg msg = Server.login("newnius", "123456");
        if(msg.getCode() == 11200){
            String tmp = new Gson().toJson(msg.getObj());
            User u = new Gson().fromJson(tmp, User.class);
            new Thread(new Receiver(u)).start();
        }
        System.out.println(msg.getCode());*/

        //System.out.println(Server.applyGroup(1).getCode());

        //System.out.println(Server.createGroup(new Group(0, "鳄鱼", 4, "introduction", "no icon", 0, 0)));
        //System.out.println(Server.searchGroupByName("鳄鱼").getCode());
        //System.out.println(Server.searchGroupByNameVague("鳄鱼").getCode());
        //System.out.println(Server.searchGroupByCategory(1).getCode());
        //System.out.println(Server.searchGroupByUser(null).getCode());
        //
        //System.out.println(Server.getGroupChat(1).getCode());

        //System.out.println(Server.searchGroupById(8).getCode());
        //System.out.println(Server.sendGroupChat(new Chat(0,0,0,1,null,"dasdsa message ",0)).getCode());
        //System.out.println(Server.sendGroupChat(new Chat(0,0,0,1,null,"dasdsa message ",0)).getCode());
        //System.out.println(Server.getGroupChatAfterChat(new Chat(0,0,0,3,null,null,1437546025594l-1)).getCode());
        //System.out.println(Server.postArticle(new Article( "title","内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容",0,null,0,null)));
        //System.out.println(Server.getArticleByUser(null));
        //System.out.println(Server.getAllArticleAfterArticle(null));
        //System.out.println(Server.getHottestArticleAfterArticle(null));
        //System.out.println(Server.askQuestion(new Question("标题", "description", null, null)).getCode());
        //System.out.println(Server.getQuestionByID(1).getCode());
        //System.out.println(Server.getAllQuestionsBefore(null).getCode());
        //System.out.println(Server.getUnsolvedQuestionsBefore(null).getCode());
        //System.out.println(Server.getAllQuestionsByAuthorBefore(null).getCode());
/*System.out.println(Server.answerQuestion(new Answer(1, "ask google")));
        System.out.println(Server.answerQuestion(new Answer(1, "i dont't know")));
        System.out.println(Server.answerQuestion(new Answer(2, "answer 2")));
        System.out.println(Server.getAnswersByQuestionID(1));*/

/*        System.out.println(Server.makeComment(new Comment(1,"as")).getCode());
        System.out.println(Server.makeComment(new Comment(1,"comment 2")).getCode());
        System.out.println(Server.makeComment(new Comment(2,"comment of id 2")).getCode());
        System.out.println(Server.getCommentsByArticleID(1));*/

    }
}
