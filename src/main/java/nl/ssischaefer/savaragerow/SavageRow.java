package nl.ssischaefer.savaragerow;

import nl.ssischaefer.savaragerow.api.controller.RowController;
import nl.ssischaefer.savaragerow.api.controller.SchemaController;
import nl.ssischaefer.savaragerow.api.controller.WorkflowController;
import nl.ssischaefer.savaragerow.common.event.TableEvent;
import nl.ssischaefer.savaragerow.common.event.TableEventProducer;
import nl.ssischaefer.savaragerow.data.DynamicRepositoryImpl;
import nl.ssischaefer.savaragerow.data.ManagementService;
import nl.ssischaefer.savaragerow.data.OperationsService;
import nl.ssischaefer.savaragerow.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.workflow.mapper.CrudTaskSchemaMapper;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowMapper;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowTaskSchemaMapper;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowTriggerSchemaMapper;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableEventObserver;
import nl.ssischaefer.savaragerow.workflow.persistence.WorkflowRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static spark.Spark.*;

public class SavageRow {
    private static final Logger logger = LoggerFactory.getLogger("Main");
    private static final String API_PREFIX = "/api/v1";

    public static void main(String[] args) {
        logger.info("Starting up SavageFlow");
        configureServer();
        logger.info("Instantiating Objects");
        var executor = Executors.newFixedThreadPool(1);
        var eventQueue = new LinkedBlockingQueue<TableEvent>();
        var eventObserver = new TableEventObserver(eventQueue);
        executor.submit(eventObserver);

        var workflowMapper = buildWorkflowMapper(eventQueue);

        var workflowRepository = new WorkflowRepositoryImpl(WorkspaceConfiguration.getWorkflowPath());

        var workflowService = new WorkflowService(workflowRepository, eventObserver, workflowMapper);
        var managementService = new ManagementService();

        var eventProducer = new TableEventProducer(eventQueue);
        var operationsService = new OperationsService(eventProducer, new DynamicRepositoryImpl());

        var schemaController = new SchemaController(managementService);
        var workflowController = new WorkflowController(workflowService);
        var rowController = new RowController(operationsService);

        schemaController.setup(API_PREFIX);
        workflowController.setup(API_PREFIX);
        rowController.setup(API_PREFIX);

        setupExceptions();
        workflowService.startAll();
    }

    private static void configureServer() {
        logger.info("Configuring server");
        port(Configuration.parseOrDefaultInteger("PORT", 9010));
        staticFiles.location("/public");
    }

    private static WorkflowMapper buildWorkflowMapper(LinkedBlockingQueue<TableEvent> eventQueue) {
        var ctskm = new CrudTaskSchemaMapper(new DynamicRepositoryImpl(), new TableEventProducer(eventQueue));
        var tksm = new WorkflowTaskSchemaMapper(ctskm);
        var trsm = new WorkflowTriggerSchemaMapper(new DynamicRepositoryImpl());
        return new WorkflowMapper(tksm, trsm);
    }

    private static void setupExceptions() {
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
