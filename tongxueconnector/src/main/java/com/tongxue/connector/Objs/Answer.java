package com.tongxue.connector.Objs;

public class Answer {
	private int answerID;
	private int questionID;
	private String content;
	private String author;
	private long time;
	private boolean helpful = false;
	private int ups=0;
	private int downs=0;
	
	
	public Answer(int questionID) {
		super();
		this.questionID = questionID;
	}


	public Answer(int questionID, String content) {
		super();
		this.questionID = questionID;
		this.content = content;
	}
	
	
	public Answer(int answerID, int questionID, String content, String author, long time) {
		super();
		this.answerID = answerID;
		this.questionID = questionID;
		this.content = content;
		this.author = author;
		this.time = time;
	}


	public Answer(int answerID, int questionID, String content, String author, long time, boolean helpful, int ups,
			int downs) {
		super();
		this.answerID = answerID;
		this.questionID = questionID;
		this.content = content;
		this.author = author;
		this.time = time;
		this.helpful = helpful;
		this.ups = ups;
		this.downs = downs;
	}


	public int getAnswerID() {
		return answerID;
	}
	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isUseful() {
		return helpful;
	}
	public void setUseful(boolean useful) {
		this.helpful = useful;
	}
	public int getUps() {
		return ups;
	}
	public void setUps(int ups) {
		this.ups = ups;
	}
	public int getDowns() {
		return downs;
	}
	public void setDowns(int downs) {
		this.downs = downs;
	}
	
	
	

}
