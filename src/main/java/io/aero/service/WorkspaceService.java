package io.aero.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkspaceService {
    public List<String> listDatabases();
    public void setDatabase(String database) throws Exception;
    public boolean databaseExists(String database);
    public String getCurrentDatabase();
}
