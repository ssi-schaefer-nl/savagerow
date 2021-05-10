package io.aero.integration.sqlite.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

public class TableMetaDataCacheEntry {
    private String name;
    private final long createdAt;
    private List<ColumnMetaData> columns;



    public TableMetaDataCacheEntry() {
        this.createdAt = (new Date().getTime())/1000;
    }

    public boolean isExpired(long cacheExpirationSeconds) {
        long currentTime = (new Date().getTime())/1000;
        return (currentTime - createdAt) >= cacheExpirationSeconds;
    }

    public String getName() {
        return name;
    }

    public TableMetaDataCacheEntry setName(String name) {
        this.name = name;
        return this;
    }

    public List<ColumnMetaData> getColumns() {
        return columns;
    }

    public TableMetaDataCacheEntry setColumns(List<ColumnMetaData> columns) {
        this.columns = columns;
        return this;
    }
}
