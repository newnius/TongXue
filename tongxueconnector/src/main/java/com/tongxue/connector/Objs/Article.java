package com.tongxue.connector.Objs;

public class Article {
	private int articleID;
	private String title;
	private String content;
	private long time;
	private String author;
	private int category;
	private String cover;
	private int views;
	private int ups;

	public Article(int articleID){
		this.articleID = articleID;
	}

	public Article(int articleID, String title, String content, long time, String author, int category, String cover,
			int views, int ups) {
		this.articleID = articleID;
		this.title = title;
		this.content = content;
		this.time = time;
		this.author = author;
		this.category = category;
		this.cover = cover;
		this.views = views;
		this.ups = ups;
	}

	public Article(String title, String content, long time, String author, int category, String cover) {
		this.title = title;
		this.content = content;
		this.time = time;
		this.author = author;
		this.category = category;
		this.cover = cover;
	}

	public int getArticleID() {
		return articleID;
	}

	public void setArticleID(int articleID) {
		this.articleID = articleID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getUps() {
		return ups;
	}

	public void setUps(int ups) {
		this.ups = ups;
	}
	
	
	

}
