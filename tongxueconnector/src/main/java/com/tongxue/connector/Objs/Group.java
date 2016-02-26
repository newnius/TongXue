package com.tongxue.connector.Objs;

public class Group {
	private int groupID;
	private String name;
	private int category;
	private String introduction;
	private String icon;
	private int pub;
	private long time;

	public Group(int groupID, String name, int category, String introduction, String icon, int pub, long time) {
		this.groupID = groupID;
		this.name = name;
		this.category = category;
		this.introduction = introduction;
		this.icon = icon;
		this.pub = pub;
		this.time = time;
	}

	public int getGroupID() {
		return groupID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public long getTime() {
		return time;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getPub() {
		return pub;
	}

	public void setPub(int pub) {
		this.pub = pub;
	}

}
