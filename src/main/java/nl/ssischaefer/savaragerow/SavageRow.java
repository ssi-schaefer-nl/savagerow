package nl.ssischaefer.savaragerow;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.api.controller.*;
import nl.ssischaefer.savaragerow.common.event.TableEvent;
import nl.ssischaefer.savaragerow.common.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.data.management.ManagementService;
import nl.ssischaefer.savaragerow.data.operations.DynamicRepository;
import nl.ssischaefer.savaragerow.data.operations.OperationsService;
import nl.ssischaefer.savaragerow.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.workflow.mapper.CrudTaskSchemaMapper;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowMapper;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowTaskSchemaMapper;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowTriggerSchemaMapper;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableEventObserver;
import nl.ssischaefer.savaragerow.workflow.persistence.WorkflowRepositoryImpl;
import nl.ssischaefer.savaragerow.common.event.TableEventProducer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static spark.Spark.*;

public class SavageRow {
    private static final String API_PREFIX = "/api/v1";

    public static void main(String[] args) {
        port(Configuration.parseOrDefaultInteger("PORT", 9010));
        staticFiles.location("/public");
        var executor = Executors.newFixedThreadPool(1);

        var eventQueue = new LinkedBlockingQueue<TableEvent>();
        var workflowMapper = buildWorkflowMapper(eventQueue);

        var workflowRepository = new WorkflowRepositoryImpl(WorkspaceConfiguration.getWorkflowPath());

        var eventObserver = new TableEventObserver(eventQueue);
        executor.submit(eventObserver);
        var workflowService = new WorkflowService(workflowRepository, eventObserver, workflowMapper);
        var managementService = new ManagementService();

        var eventProducer = new TableEventProducer(eventQueue);
        var operationsService = new OperationsService(eventProducer, new DynamicRepository());

        var schemaController = new SchemaController(managementService);
        var workflowController = new WorkflowController(workflowService);
        var rowController = new RowController(operationsService);
        try {
            var schema = new ObjectMapper().readValue(Paths.get("/tmp", "input.json").toFile(), WorkflowSchema.class);
            workflowService.add(schema);
            schema = new ObjectMapper().readValue(Paths.get("/tmp", "input2.json").toFile(), WorkflowSchema.class);
            workflowService.add(schema);
        } catch (IOException e) {
            e.printStackTrace();
        }

        schemaController.setup(API_PREFIX);
        workflowController.setup(API_PREFIX);
        rowController.setup(API_PREFIX);

        setupExceptions();
    }

    private static WorkflowMapper buildWorkflowMapper(LinkedBlockingQueue<TableEvent> eventQueue) {
        var ctskm = new CrudTaskSchemaMapper(new DynamicRepository(), new TableEventProducer(eventQueue));
        var tksm = new WorkflowTaskSchemaMapper(ctskm);
        var trsm = new WorkflowTriggerSchemaMapper();
        return new WorkflowMapper(tksm, trsm);
    }

    private static void setupExceptions() {
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
