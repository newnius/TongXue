package com.avoscloud.leanchatlib.model;

/**
 * Created by lzw on 14/11/18.
 */
public enum ConversationType {
  Single(0), Group(1);
  public static final String TYPE_KEY = "type";
  public static final String ATTR_TYPE_KEY = "attr.type";
  public static final String GNAME_KEY = "GroupName";
  public static final String ATTR_GNAME_KEY="attr.GroupName";
  public static final String GKIND_KEY="kind";
  public static final String ATTR_GKIND_KEY="attr.kind";

  int value;

  ConversationType(int value) {
    this.value = value;
  }

  public static ConversationType fromInt(int i) {
    return values()[i];
  }

  public int getValue() {
    return value;
  }
}
