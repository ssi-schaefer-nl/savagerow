package nl.ssischaefer.savaragerow;

import java.nio.file.Paths;

import static nl.ssischaefer.savaragerow.Configuration.parseOrDefault;

public class WorkspaceConfiguration {
    private static final String ENV_WORKSPACE = "WORKSPACE";
    private static final String ENV_DATABASE = "DATABASE";
    private static final String ENV_WORKFLOW_QUEUE_CAPACITY = "WORKFLOWQUEUECAPACITY";
    private static final String ENV_WORKFLOW_WORKERS = "WORKFLOWWORKERS";

    private static final String DEFAULT_WORKSPACE = "/tmp/SavageFlow";
    private static final String DEFAULT_DATABASE = "database";
    private static final String DEFAULT_WORKFLOW_QUEUE_CAPACITY = "100";
    private static final String DEFAULT_WORKFLOW_WORKERS = "5";

    private static final String DB_EXT = ".db";
    private static final String JDBC_SQLITE_PREFIX = "jdbc:sqlite:";


    public static String getWorkspace() {
        return parseOrDefault(ENV_WORKSPACE, DEFAULT_WORKSPACE);

    }

    private static String getDatabasePath() {
        return Paths.get(getWorkspace(), parseOrDefault(ENV_DATABASE, DEFAULT_DATABASE + DB_EXT)).toString();
    }

    public static int getWorkflowQueueCapacity() {
        return Integer.parseInt(parseOrDefault(ENV_WORKFLOW_QUEUE_CAPACITY, DEFAULT_WORKFLOW_QUEUE_CAPACITY));
    }

    public static int getWorkflowWorkers() {
        return Integer.parseInt(parseOrDefault(ENV_WORKFLOW_WORKERS, DEFAULT_WORKFLOW_WORKERS));
    }

    public static String getDatabaseUrl() {
        return JDBC_SQLITE_PREFIX + getDatabasePath();
    }

    public static String getWorkflowPath() {
        return Paths.get(getWorkspace(), "workflows").toString();
    }
}