package io.aero.v2.dto;

public class WorkflowOverviewDTO {
    private String table;
    private WorkflowSummary update;
    private WorkflowSummary insert;
    private WorkflowSummary delete;

    public String getTable() {
        return table;
    }

    public WorkflowOverviewDTO setTable(String table) {
        this.table = table;
        return this;
    }

    public WorkflowSummary getUpdate() {
        return update;
    }

    public WorkflowOverviewDTO setUpdate(int active, int total) {
        this.update = new WorkflowSummary(active, total);
        return this;
    }

    public WorkflowSummary getInsert() {
        return insert;
    }

    public WorkflowOverviewDTO setInsert(int active, int total) {
        this.insert = new WorkflowSummary(active, total);
        return this;
    }

    public WorkflowSummary getDelete() {
        return delete;
    }

    public WorkflowOverviewDTO setDelete(int active, int total) {
        this.delete = new WorkflowSummary(active, total);
        return this;
    }

    public static class WorkflowSummary {
        private int active;
        private int total;

        public WorkflowSummary(int active, int total) {
            this.active = active;
            this.total = total;
        }

        public int getActive() {
            return active;
        }

        public int getTotal() {
            return total;
        }
    }
}
