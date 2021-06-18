package nl.ssischaefer.savaragerow.v1.sqlite.service;

import nl.ssischaefer.savaragerow.v1.sqlite.utils.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v1.service.WorkspaceService;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SQLiteWorkspaceService implements WorkspaceService {
    private final File workspaceDirectory;
    private static final String workspaceProperties = "workspace.properties";
    private String currentDatabase;

    public SQLiteWorkspaceService() throws Exception {
        Properties properties = getWorkspaceProperties();
        if (properties == null || properties.getProperty("workspace") == null) {
            throw new Exception("Workspace configuration missing!");
        }

        workspaceDirectory = new File(properties.getProperty("workspace"));
    }

    private Properties getWorkspaceProperties() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream input = loader.getResourceAsStream(workspaceProperties)) {
            Properties props = new Properties();
            props.load(input);
            return props;
        } catch (IOException ex) {
            return null;
        }
    }

    public void setDatabase(String database) throws Exception {
        File[] files = workspaceDirectory.listFiles();
        if (files == null) {
            return;
        }
        File databaseDir = Arrays.stream(files)
                .filter(File::isDirectory)
                .filter(d -> d.getName().equals(database))
                .findFirst()
                .orElseThrow(() -> new Exception("Directory does not exist"));

        String url = getConnectionUrl(databaseDir);

        currentDatabase = database;
        SQLiteDataSource.connect(url);

    }


    private String getConnectionUrl(File dbDir) throws Exception {
        File[] files = dbDir.listFiles();
        if (files == null) {
            return null;
        }

        String url = Arrays.stream(files)
                .filter(f -> f.getName().contains(".db"))
                .findFirst()
                .map(File::getAbsolutePath)
                .orElseThrow(() -> new Exception("No .db file found"));

        return "jdbc:sqlite:"+url;
    }


    public List<String> listDatabases() {
        if (workspaceDirectory == null) {
            return Collections.emptyList();
        }
        File[] files = workspaceDirectory.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files).filter(File::isDirectory).map(File::getName).collect(Collectors.toList());
    }

    public boolean databaseExists(String database) {
        return listDatabases().contains(database);
    }

    @Override
    public String getCurrentDatabase() {
        return currentDatabase;
    }

    @Override
    public void setDatabaseIfNotSet(String database) throws Exception {
            String curDb = getCurrentDatabase();
            if(curDb == null || !curDb.equals(database)) {
                setDatabase(database);
            }
    }
}
