package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.workflow.exception.WorkflowTaskExecutionException;
import nl.ssischaefer.savaragerow.workflow.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.task.WorkflowTaskInput;
import nl.ssischaefer.savaragerow.workflow.trigger.AbstractTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workflow {
    private final Logger logger = LoggerFactory.getLogger("Workflow");

    private String workflowId;
    private AbstractTrigger trigger;
    private Map<Long, AbstractWorkflowTask> tasks;

    public Workflow() {
        tasks = new HashMap<>();
    }


    public void execute(WorkflowTaskInput taskinput) throws JobExecutionException {
        logger.info(String.format("Workflow %s has been triggered and starts executing at task %d", workflowId, trigger.getTask()));
        var input = taskinput;
        var taskId = trigger.getTask();

        while (taskId != null) {
            var task = tasks.get(taskId);
            if (task == null) break;
            logger.info(String.format("(%s) Executing task: %d", workflowId, taskId));
            input = task.execute(input);
            taskId = task.getNext();
        }
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    public void setTrigger(AbstractTrigger trigger) {
        this.trigger = trigger;
    }

    public void setTasks(List<AbstractWorkflowTask> tasks) {
        tasks.forEach(t -> this.tasks.put(t.getId(), t));
    }

    public AbstractTrigger getTrigger() {
        return trigger;
    }

    public Map<Long, AbstractWorkflowTask> getTasks() {
        return tasks;
    }

    public void setTasks(Map<Long, AbstractWorkflowTask> tasks) {
        this.tasks = tasks;
    }

    public Map<String, String> getInput(Long taskId) {
        return new HashMap<>();
    }

}
