package com.tongxue.connector;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tongxue.connector.Objs.Answer;
import com.tongxue.connector.Objs.Article;
import com.tongxue.connector.Objs.Chat;
import com.tongxue.connector.Objs.Comment;
import com.tongxue.connector.Objs.Group;
import com.tongxue.connector.Objs.Question;
import com.tongxue.connector.Objs.User;

/**
 *
 * @author Newnius
 */
public class Server {

	private static Communicate comm;

	private Server() {

	}

	public static Msg login(String username, String password) {
		if (comm == null ) {
			comm = new Communicate();
		}
		try {
			User user = new User(username, password, null);
			String con = new Gson().toJson(new Msg(11, user));
			String res = comm.send(con);
			Msg msg;
			if (res == null) {
				msg = new Msg(10000, null);
			} else {
				System.out.println(res);
                msg = new Gson().fromJson(res, Msg.class);
                user = new Gson().fromJson(msg.getObj().toString(), User.class);
                new Thread(new Receiver(user)).start();

            }
			return msg;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Msg register(String username, String password, String email) {
		if (comm == null) {
			comm = new Communicate();
		}
		try {
			User user = new User(username, password, email);
			String con = new Gson().toJson(new Msg(12, user));
			String res = comm.send(con);
			Msg msg;
			if (res == null) {
				msg = new Msg(10000, null);
			} else {
				System.out.println(res);
				msg = new Gson().fromJson(res, Msg.class);
			}
			return msg;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public static Msg searchGroupByName(String name) {
		if (comm == null) {
			comm = new Communicate();
		}
		Group group = new Group(0, name, 0, null, null, 0, 0);
		String con = new Gson().toJson(new Msg(21, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Group> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Group>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg searchGroupByNameVague(String name) {
		if (comm == null) {
			comm = new Communicate();
		}
		Group group = new Group(0, name, 0, null, null, 0, 0);
		String con = new Gson().toJson(new Msg(31, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Group> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Group>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg searchGroupByCategory(int category) {
		if (comm == null) {
			comm = new Communicate();
		}
		Group group = new Group(0, null, category, null, null, 0, 0);
		String con = new Gson().toJson(new Msg(23, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Group> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Group>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg searchGroupByUser(User user) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(24, user));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Group> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Group>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg createGroup(Group group) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(22, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg applyGroup(int groupID) {
		if (comm == null) {
			comm = new Communicate();
		}
		Group group = new Group(groupID, null, 0, null, null, 0, 0);
		String con = new Gson().toJson(new Msg(25, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg sendGroupChat(Chat chat) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(26, chat));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg getGroupChat(int groupID) {
		if (comm == null) {
			comm = new Communicate();
		}
		Group group = new Group(groupID, null, 0, null, null, 0, 0);
		String con = new Gson().toJson(new Msg(27, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Chat> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Chat>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg searchGroupById(int groupID) {
		if (comm == null) {
			comm = new Communicate();
		}
		Group group = new Group(groupID, null, 0, null, null, 0, 0);
		String con = new Gson().toJson(new Msg(28, group));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			group = new Gson().fromJson(new Gson().toJson(msg.getObj()), Group.class);
			msg.setObj(group);
		}
		return msg;
	}

	public static Msg getGroupChatAfterChat(Chat chat) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(29, chat));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Chat> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Chat>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}



	public static Msg sendBoardAction(String action) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = action;
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg postArticle(Article article){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(70, article));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg updateArticle(User user, Article article) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(71, article));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg getArticleById(int articleID) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(72, new Article(articleID)));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			Article article = new Gson().fromJson(new Gson().toJson(msg.getObj()), Article.class);
			msg.setObj(article);
		}
		return msg;
	}


	public static Msg getArticleByUser(User user) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(73, user));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Article> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Article>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg getAllArticleAfterArticle(Article article) {

		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(74, article));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Article> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Article>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}


	public static Msg getHottestArticleAfterArticle(Article article) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(75, article));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Article> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Article>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg deleteArticle(User user, int articleID) {
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(76, new Article(articleID)));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg askQuestion(Question question){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(90, question));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg getAllQuestionsBefore(Question question){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(91, question));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Question> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Question>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg getUnsolvedQuestionsBefore(Question question){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(92, question));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Question> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Question>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg getAllQuestionsByAuthorBefore(Question question){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(93, question));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Question> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Question>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg updateQuestion(Question question){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(94, question));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg deleteQuestion(Question question){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(95, question));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg getQuestionByID(int questionID){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(96, new Question(questionID)));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			Question question = new Gson().fromJson(new Gson().toJson(msg.getObj()), Question.class);
			msg.setObj(question);
		}
		return msg;
	}

	public static Msg getAnswersByQuestionID(int questionID){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(97, new Question(questionID)));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
			List<Answer> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Answer>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}

	public static Msg answerQuestion(Answer answer){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(98, answer));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);
		}
		return msg;
	}

	public static Msg makeComment(Comment comment){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(78, comment));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);

		}
		return msg;
	}

	public static Msg getCommentsByArticleID(int articleID){
		if (comm == null) {
			comm = new Communicate();
		}
		String con = new Gson().toJson(new Msg(79, new Article(articleID)));
		String res = comm.send(con);
		Msg msg;
		if (res == null) {
			msg = new Msg(10000, null);
		} else {
			System.out.println(res);
			msg = new Gson().fromJson(res, Msg.class);

			List<Comment> list = new Gson().fromJson(new Gson().toJson(msg.getObj()), new TypeToken<List<Comment>>() {
			}.getType());
			msg.setObj(list);
		}
		return msg;
	}





}
