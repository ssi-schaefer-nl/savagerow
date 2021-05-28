package io.aero.v2.util;

public class Path {
    private static final String ROOT = "/api/v1/database";
    private static final String ROOT_DB = ROOT + "/:database";
    private static final String ROOT_DB_TABLE = ROOT_DB + "/:table";
    private static final String ROOT_DB_TABLE_ROW = ROOT_DB_TABLE + "/:row";
    private static final String ROOT_DB_TABLE_COLUMN =  ROOT_DB_TABLE + "/column";


    public static class Get {
        public static final String SCHEMA = ROOT_DB_TABLE_COLUMN;
        public static final String ROWS = ROOT_DB_TABLE;
        public static final String DATABASES = ROOT;
        public static final String Tables = ROOT_DB;
    }

    public static class Post {
        public static final String ROWS = ROOT_DB_TABLE;
        public static final String DATABASE = ROOT;
        public static final String COLUMN = ROOT_DB_TABLE_COLUMN;
    }

    public static class Put {
        public static final String ROWS = ROOT_DB_TABLE_ROW;
    }

    public static class Delete {
        public static final String ROWS = ROOT_DB_TABLE_ROW;
    }

    public static class Wildcard {
        public static final String DATABASE = ROOT_DB + "/*";
    }
}
