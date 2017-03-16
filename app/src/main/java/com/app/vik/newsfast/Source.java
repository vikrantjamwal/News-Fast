package com.app.vik.newsfast;

public class Source {

    private String mId;
    private String mName;

    public Source(String id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
