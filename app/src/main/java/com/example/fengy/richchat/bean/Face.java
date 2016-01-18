package com.example.fengy.richchat.bean;

/**
 * 自定义表情
 */
public class Face {

    private String Name; // 表情选择后，显示的名字
    private String Id; // 与表情文件名对应

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
