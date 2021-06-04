package io.aero.v2.util;

public class Path {
    private static String ROOT = "/api/v1/:database";

    public static class Database {
        private static final String ROOT_DB = ROOT + "/database";
        private static final String ROOT_DB_TABLE = ROOT_DB + "/:table";
        private static final String ROOT_DB_TABLE_ROW = ROOT_DB_TABLE + "/:row";
        private static final String ROOT_DB_TABLE_SCHEMA = ROOT_DB_TABLE + "/schema";
        private static final String ROOT_DB_TABLE_COLUMN = ROOT_DB_TABLE + "/column";
        private static final String ROOT_DB_TABLE_COLUMN_NAME = ROOT_DB_TABLE_COLUMN + "/:column";

        public static final String SCHEMA = ROOT_DB_TABLE_SCHEMA;
        public static final String ROWS = ROOT_DB_TABLE;
        public static final String ROWS_ID = ROOT_DB_TABLE_ROW;
        public static final String DATABASES = ROOT;
        public static final String TABLES = ROOT_DB;
        public static final String DATABASE = ROOT;
        public static final String COLUMN = ROOT_DB_TABLE_COLUMN;
        public static final String COLUMN_NAME = ROOT_DB_TABLE_COLUMN_NAME;
    }

    public static class Workflow {
        private static final String ROOT_WORKFLOW = ROOT + "/workflow";
        private static final String ROOT_WORKFLOW_TABLE = ROOT_WORKFLOW + "/:table";
        private static final String ROOT_WORKFLOW_TYPE = ROOT_WORKFLOW + "/:type";
        private static final String ROOT_WORKFLOW_TABLE_TYPE = ROOT_WORKFLOW_TABLE + "/:type";

        public static final String DB_SUMMARY = ROOT_WORKFLOW;
        public static final String TYPE = ROOT_WORKFLOW_TYPE;
        public static final String TABLE_TYPE = ROOT_WORKFLOW_TABLE_TYPE;
    }

    public static class Wildcard {
        public static final String ALL = ROOT + "/*";
    }
}
