package com.tongxue.connector.Objs;

public class Comment {
	private int commentID;
	private int articleID;
	private String content;
	private String author;
	private long time;

	public Comment(int articleID, String content) {
		this.articleID = articleID;
		this.content = content;
	}

	public Comment(int articleID, String content, String author, long time) {
		super();
		this.articleID = articleID;
		this.content = content;
		this.author = author;
		this.time = time;
	}

	public Comment(int commentID, int articleID, String content, String author, long time) {
		super();
		this.commentID = commentID;
		this.articleID = articleID;
		this.content = content;
		this.author = author;
		this.time = time;
	}
	
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public int getArticleID() {
		return articleID;
	}
	public void setArticleID(int articleID) {
		this.articleID = articleID;
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

	
	
	
}
