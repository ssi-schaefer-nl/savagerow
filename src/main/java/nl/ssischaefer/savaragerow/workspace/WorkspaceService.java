package nl.ssischaefer.savaragerow.workspace;

import net.lingala.zip4j.ZipFile;
import nl.ssischaefer.savaragerow.data.common.exception.DatabaseException;
import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDataSource;
import nl.ssischaefer.savaragerow.workspace.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workspace.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static nl.ssischaefer.savaragerow.Configuration.parseOrDefault;

public class WorkspaceService {
    private static final String WORKSPACE = "WORKSPACE";
    private static final String DEFAULT_VALUE = "/tmp";
    private static final String DB_EXT = ".db";
    private static final String JDBC_SQLITE_PREFIX = "jdbc:sqlite:";

    private static String currentWorkspace;
    private static String currentDatabaseUrl;

    public static void setCurrentWorkspace(String databaseName) throws IOException, SQLException {
        String workSpace = getDatabasePath(databaseName);
        currentDatabaseUrl = JDBC_SQLITE_PREFIX + Paths.get(workSpace, databaseName+ DB_EXT).toString();
        currentWorkspace = workSpace;
    }

    public static String getCurrentWorkspace() throws WorkspaceNotSetException {
        if(currentWorkspace == null) {
            throw new WorkspaceNotSetException();
        }
        return currentWorkspace;
    }

    private static String getDatabasePath(String databaseName) throws IOException {
        String workspace = parseOrDefault(WORKSPACE, DEFAULT_VALUE);
        var path = Paths.get(workspace, databaseName);
        if(!Files.isDirectory(path)) {
            Files.createDirectories(path);
        }

        return path.toString();
    }

    public static List<String> listDatabases() {
        var workspaceDirectory = new File(parseOrDefault(WORKSPACE, DEFAULT_VALUE));
        File[] files = workspaceDirectory.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files).filter(File::isDirectory).filter(WorkspaceService::containsDbFile).map(File::getName).collect(Collectors.toList());
    }

    private static boolean containsDbFile(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            return Arrays.stream(files).map(File::getName).map(FilenameUtils::getExtension).anyMatch(e -> e.equals("db"));
        }
        return false;
    }

    public static void removeDatabase(String database) throws IOException {
        String p = getDatabasePath(database);
        var file = new File(p);
        FileUtils.deleteDirectory(file);
    }

    public static void createDatabase(String database) throws DatabaseException, IOException, SQLException {
        boolean exists = listDatabases().stream().anyMatch(d -> d.equals(database));
        if(exists) throw new DatabaseException("Database already exists");

        WorkspaceService.setCurrentWorkspace(database);
        SQLiteDataSource.getForCurrentWorkspace().close(); // Create the new database by connecting to it once
    }

    public static String getCurrentDatabaseUrl() {
        return currentDatabaseUrl;
    }

    public static ZipOutputStream export(String databaseName, OutputStream stream) throws IOException, SQLException {
        setCurrentWorkspace(databaseName);
        return ZipUtil.zipDirectory(currentWorkspace, stream);

    }

    public static void importDatabase(ZipInputStream zipInputStream) throws IOException {
        ZipUtil.unzipFile(zipInputStream, parseOrDefault(WORKSPACE, DEFAULT_VALUE));
    }
}
