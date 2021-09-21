package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.schema.WorkflowSchema;

import java.util.List;
import java.util.Map;

public interface WorkflowController {
    /***
     * Lists all the workflows that exist in the system.
     * @return List of workflows that exist in the system
     */
    List<WorkflowSchema> findAll();

    /***
     * Finds the workflow identified by the specified identifier.
     * @param id the identifier of the workflow that must be found.
     * @return the schema of the workflow that was found.
     */
    WorkflowSchema find(String id);

    /***
     * Create or updates the specified workflow in the system.
     * After creation/update the workflow will be evaluated to determine whether it should be
     * actived or deactivated. The workflow will be identified by the identifier defined in the workflow schema.
     * @param workflowSchema the schema of the workflow that must be stored.
     */
    void createOrUpdate(WorkflowSchema workflowSchema);

    /***
     * Deletes the specified workflow from the system.
     * @param id The unique identifier of the workflow
     */
    void delete(String id);

    /***
     * Generates a unique identifier for a workflow.
     * @return A unique identifier for a workflow.
     */
    String generateUniqueID();

    /***
     * Generates the input parameters that are available to the specified workflow task.
     * @param workflow The identifier of the workflow that contains the task
     * @param taskId The identifier of the task
     * @return A map in which each record contains the input parameter and its corresponding placeholder
     */
    Map<String, String> getTaskInput(String workflow, Long taskId);

    /***
     * Starts all workflows that are enabled and can be started
     */
    void startAll();
}
