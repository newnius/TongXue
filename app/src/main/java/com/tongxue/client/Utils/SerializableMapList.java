package com.tongxue.client.Utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by chaosi on 2015/7/20.
 */
public class SerializableMapList implements Serializable{
    private List<Map<String, Object>> mapList;

    public List<Map<String, Object>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<String, Object>> mapList) {
        this.mapList = mapList;
    }
}
