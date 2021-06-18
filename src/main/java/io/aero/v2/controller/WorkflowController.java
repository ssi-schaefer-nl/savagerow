package io.aero.v2.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.aero.v2.dto.WorkflowOverviewDTO;
import io.aero.v2.model.Workflow;
import io.aero.v2.model.WorkflowType;
import io.aero.v2.model.WorkflowsManager;
import io.aero.v2.query.GetTablesQuery;
import io.aero.v2.util.RequestParams;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;
import java.util.stream.Collectors;

public class WorkflowController {

    private WorkflowController() {
    }

    public static final Route getSummary = (Request request, Response response) -> {
        List<String> tables = new GetTablesQuery().execute().getResult();
        WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
        List<WorkflowOverviewDTO> summary = tables.stream().map(table -> {
            List<Workflow> deleteWorkflows = workflows.getByTable(WorkflowType.DELETE, table);
            List<Workflow> updateWorkflows = workflows.getByTable(WorkflowType.UPDATE, table);
            List<Workflow> insertWorkflows = workflows.getByTable(WorkflowType.INSERT, table);

            return new WorkflowOverviewDTO()
                    .setTable(table)
                    .setDelete((int) deleteWorkflows.stream().filter(Workflow::isActive).count(), deleteWorkflows.size())
                    .setUpdate((int) updateWorkflows.stream().filter(Workflow::isActive).count(), updateWorkflows.size())
                    .setInsert((int) insertWorkflows.stream().filter(Workflow::isActive).count(), insertWorkflows.size());
        }).collect(Collectors.toList());
        return new ObjectMapper().writeValueAsString(summary);
    };

    public static final Route getAllWorkflows = (Request request, Response response) -> {
        WorkflowsManager workflowManager = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
        return new ObjectMapper().writeValueAsString(workflowManager.get());
    };

    public static final Route getTableWorkflows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));

        List<Workflow> tableWorkflows =  WorkflowsManager.getWorkflowsFromCurrentWorkspace().getByTable(type, table);
        return new ObjectMapper().writeValueAsString(tableWorkflows);
    };

    public static final Route addWorkflow = (Request request, Response response) -> {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Workflow workflow = objectMapper.readValue(request.body(), Workflow.class);

        WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
        workflows.add(type, workflow);
        WorkflowsManager.save(workflows);
        return "";
    };

    public static final Route deleteWorkflow = (Request request, Response response) -> {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        String name = request.params(RequestParams.Parameter.WorklfowName);
        String table = request.params(RequestParams.Parameter.Table);

        WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
        workflows.delete(type, table, name);

        WorkflowsManager.save(workflows);
        return "";
    };

    public static final Route setActive = (Request request, Response response) -> {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        String name = request.params(RequestParams.Parameter.WorklfowName);
        String table = request.params(RequestParams.Parameter.Table);
        boolean active = Boolean.parseBoolean(request.params(RequestParams.Parameter.WorkflowActive));

        WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
        workflows.setActive(type, table, name, active);

        WorkflowsManager.save(workflows);
        return "";
    };

}
