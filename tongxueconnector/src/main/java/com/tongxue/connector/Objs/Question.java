package com.tongxue.connector.Objs;

public class Question {
	private int questionID;
	private String title;
	private String description;
	private String[] keywords;
	private int answerID = -1;
	private String author;
	private long time;
	private int  views;
	
	
	public Question(int questionID) {
		super();
		this.questionID = questionID;
	}
	
	
	public Question(String title, String description, String[] keywords, String author) {
		super();
		this.title = title;
		this.description = description;
		this.keywords = keywords;
		this.author = author;
	}


	public Question(int questionID, String title, String description, String keywords, int answerID, String author,
			long time, int views) {
		this.questionID = questionID;
		this.title = title;
		this.description = description;
		this.keywords = keywords.split(";");
		this.answerID = answerID;
		this.author = author;
		this.time = time;
		this.views = views;
	}
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String[] getKeywords() {
		return keywords;
	}
	
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
	public int getAnswerID() {
		return answerID;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}

	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public int getViews() {
		return views;
	}
	
	
	
	
}
