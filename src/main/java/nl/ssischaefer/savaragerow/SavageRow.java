package nl.ssischaefer.savaragerow;

import nl.ssischaefer.savaragerow.api.endpoints.RowApiEndpoint;
import nl.ssischaefer.savaragerow.api.endpoints.SchemaApiEndpoint;
import nl.ssischaefer.savaragerow.api.endpoints.WorkflowApiEndpoint;
import nl.ssischaefer.savaragerow.shared.event.TableEvent;
import nl.ssischaefer.savaragerow.storage.StorageManagementController;
import nl.ssischaefer.savaragerow.storage.StorageOperationsController;
import nl.ssischaefer.savaragerow.storage.StorageServiceImpl;
import nl.ssischaefer.savaragerow.storage.TableEventProducerImpl;
import nl.ssischaefer.savaragerow.workflow.*;
import nl.ssischaefer.savaragerow.workflow.mapper.*;
import nl.ssischaefer.savaragerow.workflow.persistence.WorkflowRepositoryImpl;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static spark.Spark.*;

public class SavageRow {
    private static final Logger logger = LoggerFactory.getLogger("Main");

    private static ExecutorService executor;
    private static BlockingQueue<TableEvent> tableEventQueue;

    private static WorkflowController workflowController;
    private static StorageManagementController storageManagementController;
    private static StorageOperationsController storageOperationsController;

    public static void main(String[] args) throws SchedulerException {
        setup();
        setupControllers();
        setupApiEndpoints();
        setupExceptions();
        workflowController.startAll();
    }

    private static void setupApiEndpoints() {
        String apiPrefix = "/api/v1";

        var rowApiEndpoint = new RowApiEndpoint(storageOperationsController);
        var workflowApiEndpoint = new WorkflowApiEndpoint(workflowController);
        var schemaApiEndpoint = new SchemaApiEndpoint(storageManagementController);

        rowApiEndpoint.setup(apiPrefix);
        workflowApiEndpoint.setup(apiPrefix);
        schemaApiEndpoint.setup(apiPrefix);
    }

    private static void setupControllers() throws SchedulerException {
        workflowController = setupWorkflowController();
        storageManagementController = setupStorageManagementController();
        storageOperationsController = setupStorageOperationsController();
    }

    private static void setup() {
        logger.info("Starting up SavageFlow");
        configureServer();
        tableEventQueue = new LinkedBlockingQueue<>();
        executor = Executors.newFixedThreadPool(1);
    }

    private static StorageOperationsController setupStorageOperationsController() {
        var tableEventProducer = new TableEventProducerImpl(tableEventQueue);
        return new StorageOperationsController(tableEventProducer, new StorageServiceImpl());
    }

    private static StorageManagementController setupStorageManagementController() {
        return new StorageManagementController();
    }

    private static WorkflowController setupWorkflowController() throws SchedulerException {
        SchedulerFactory sf = new StdSchedulerFactory();
        var workflowScheduler = new WorkflowScheduler(sf.getScheduler());
        workflowScheduler.start();
        var tableEventConsumer = new TableEventConsumer(tableEventQueue, workflowScheduler);
        var workflowRepository = new WorkflowRepositoryImpl(WorkspaceConfiguration.getWorkflowPath());
        var workflowMapper = createWorkflowMapper();
        var workflowService = new WorkflowService(workflowScheduler, tableEventConsumer);
        var workflowController = new WorkflowControllerImpl(workflowService, workflowMapper, workflowRepository);

        executor.submit(tableEventConsumer);

        return workflowController;
    }

    private static void configureServer() {
        port(Configuration.parseOrDefaultInteger("PORT", 9010));
        staticFiles.location("/public");
    }

    private static WorkflowMapper createWorkflowMapper() {
        var storageAdapter = new StorageAdapterImpl(new StorageServiceImpl());

        var ctskm = new CrudTaskSchemaMapper(storageAdapter, new LocalTableEventProducer(tableEventQueue));
        var tksm = new WorkflowTaskMapper(ctskm, new DecisionTaskSchemaMapper());
        var trsm = new WorkflowTriggerMapper(storageAdapter);
        return new WorkflowMapper(tksm, trsm);
    }

    private static void setupExceptions() {
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }
}
