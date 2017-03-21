package com.app.vik.newsfast.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {

    private NewsContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.app.vik.newsfast";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NEWS);

        public final static String TABLE_NAME = "news";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NEWS_TITLE = "title";
        public final static String COLUMN_NEWS_DESCRIPTION = "description";
        public final static String COLUMN_NEWS_URL = "url";
        public final static String COLUMN_NEWS_IMAGE_URL = "img";

    }

}
