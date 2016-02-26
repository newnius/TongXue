package com.tongxue.connector.Objs;

public class Chat {
	private int cid = 0;
	private int type = 0;
	private int topicID = 0;
	private int groupID = 0;
	private String username = null;
	private String content = null;
	private long time = 0;

	public Chat(int cid, int type, int topicID, int groupID, String username, String content, long time) {
		this.cid = cid;
		this.type = type;
		this.topicID = topicID;
		this.groupID = groupID;
		this.username = username;
		this.content = content;
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTopicID() {
		return topicID;
	}

	public int getGroupID() {
		return groupID;
	}

	public String getUsername() {
		return username;
	}

	public long getTime() {
		return time;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getCid() {
		return cid;
	}

}
