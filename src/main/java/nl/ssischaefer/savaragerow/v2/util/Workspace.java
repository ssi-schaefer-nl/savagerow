package nl.ssischaefer.savaragerow.v2.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Workspace {
    private static String currentWorkspace;

    public static void setCurrentDatabase(String databaseName) throws IOException, SQLException {
        String workSpace = getDatabasePath(databaseName);
        String connUrl = workSpace + databaseName + ".db";
        currentWorkspace = workSpace;
        SQLiteDataSource.connect("jdbc:sqlite:"+connUrl);
    }

    public static String getCurrentWorkspace() {
        return currentWorkspace;
    }

    private static String getDatabasePath(String databaseName) throws IOException {
        String url = Configuration.workspace+"\\\\"+databaseName;
        Path path = Paths.get(url);
        Files.createDirectories(path);
        return url+"\\\\";
    }

    public static List<String> listDatabases() {
        File workspaceDirectory = new File(Configuration.workspace);
        File[] files = workspaceDirectory.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(files).filter(File::isDirectory).map(File::getName).collect(Collectors.toList());
    }
}
