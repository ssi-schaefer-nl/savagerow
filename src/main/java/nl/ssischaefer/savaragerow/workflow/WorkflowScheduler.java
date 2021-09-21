package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.workflow.trigger.ScheduledTrigger;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class WorkflowScheduler {
    private final Logger logger = LoggerFactory.getLogger("WorkflowScheduler");

    private final Scheduler scheduler;

    public WorkflowScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }

    public void addJob(Workflow workflow) throws SchedulerException {
        var jobKey = JobKey.jobKey(workflow.getWorkflowId());
        if(scheduler.checkExists(jobKey)) return;
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("workflow", workflow);
        JobDetail job = JobBuilder.newJob(WorkflowJob.class).withIdentity(jobKey).usingJobData(dataMap).storeDurably().build();
        scheduler.addJob(job, false);

        logger.info(String.format("Added job to scheduler for workflow for workflow %s", workflow.getWorkflowId()));
    }

    public void deleteJob(String id) throws SchedulerException {
        var jobKey = JobKey.jobKey(id);
        scheduler.deleteJob(jobKey);
        logger.info(String.format("Deleted job from scheduler for workflow for workflow %s", id));

    }

    public void triggerWorkflow(String id, Map<String, String> data) throws SchedulerException {
        logger.info(String.format("Triggering workflow %s with %d key-value pairs", id, data.size()));
        var jobKey = JobKey.jobKey(id);
        var jobDataMap = new JobDataMap(data);
        scheduler.triggerJob(jobKey, jobDataMap);
    }

    public void addWorkflowSchedule(ScheduledTrigger trigger) {
        logger.info(String.format("Added schedule for job of workflow %s", trigger.getWorkflow()));
        //todo implement
        return;
    }

    public void removeTrigger(String workflowId) {
        logger.info(String.format("Removed schedule for job for workflow %s", workflowId));
        //todo implement
        return;
    }
}
