package io.aero.v2.model;

import java.util.ArrayList;
import java.util.List;

public class Workflows {
    private List<Workflow> insert = new ArrayList<>();
    private List<Workflow> delete = new ArrayList<>();;
    private List<Workflow> update = new ArrayList<>();;

    public List<Workflow> getInsert() {
        return insert;
    }

    public Workflows setInsert(List<Workflow> insert) {
        this.insert = insert;
        return this;
    }

    public List<Workflow> getDelete() {
        return delete;
    }

    public Workflows setDelete(List<Workflow> delete) {
        this.delete = delete;
        return this;
    }

    public List<Workflow> getUpdate() {
        return update;
    }

    public Workflows setUpdate(List<Workflow> update) {
        this.update = update;
        return this;
    }
}
