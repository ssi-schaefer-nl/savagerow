package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.workflow.exception.NoAppropriateTriggerException;
import nl.ssischaefer.savaragerow.workflow.trigger.ScheduledTrigger;
import nl.ssischaefer.savaragerow.workflow.trigger.TableEventTrigger;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowService {
    private final Logger logger = LoggerFactory.getLogger("WorkflowService");

    private final WorkflowScheduler workflowScheduler;
    private final TableEventConsumer tableEventConsumer;

    public WorkflowService(WorkflowScheduler workflowScheduler, TableEventConsumer tableEventConsumer) {
        this.workflowScheduler = workflowScheduler;
        this.tableEventConsumer = tableEventConsumer;
    }

    public void stopWorkflow(String workflowId) {
        try {
            workflowScheduler.deleteJob(workflowId);
            tableEventConsumer.removeTrigger(workflowId);
            workflowScheduler.removeTrigger(workflowId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public boolean startWorkflow(Workflow workflow) {
        if(workflowInvalid(workflow)) return false;
        try {
            workflowScheduler.addJob(workflow);
            setupTrigger(workflow);
        } catch (SchedulerException | NoAppropriateTriggerException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return true;
    }

    private boolean workflowInvalid(Workflow workflow) {
        var triggerPresent = workflow.getTrigger() != null;
        var tasksPresent = workflow.getTasks() != null && !workflow.getTasks().isEmpty();
        var triggerTaskPresent = triggerPresent && workflow.getTrigger().getTask() != null;

        if(!(triggerTaskPresent && tasksPresent && triggerPresent))  {
            var workflowState = String.format("trigger present: %s, tasks present: %s, trigger task present: %s)", triggerPresent, tasksPresent, triggerTaskPresent);
            logger.info(String.format("Could not start workflow %s (%s)", workflow.getWorkflowId(), workflowState));
            System.out.println(workflow.getTrigger().getTask());
            return true;
        }
        return false;
    }

    private void setupTrigger(Workflow workflow) throws NoAppropriateTriggerException {
        var trigger = workflow.getTrigger();
        logger.debug("Setting up trigger for type %s", trigger.getClass().getSimpleName());

        if(trigger instanceof TableEventTrigger)
            tableEventConsumer.addTrigger((TableEventTrigger) trigger);
        else if(trigger instanceof ScheduledTrigger)
            workflowScheduler.addWorkflowSchedule((ScheduledTrigger) trigger);

        else throw new NoAppropriateTriggerException();
    }
}
