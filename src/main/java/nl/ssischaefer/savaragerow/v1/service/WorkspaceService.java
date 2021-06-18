package nl.ssischaefer.savaragerow.v1.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkspaceService {
    public List<String> listDatabases();
    public void setDatabase(String database) throws Exception;
    public boolean databaseExists(String database);
    public String getCurrentDatabase();
    public void setDatabaseIfNotSet(String database) throws Exception;
}
