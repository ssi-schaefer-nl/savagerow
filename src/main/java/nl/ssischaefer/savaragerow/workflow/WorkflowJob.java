package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.workflow.task.WorkflowTaskInput;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.HashMap;

public class WorkflowJob implements Job {
    private Workflow workflow;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        var data = new HashMap<String, String>();
        var jobData = jobExecutionContext.getMergedJobDataMap();
        jobData.keySet().stream().filter(k -> !k.equalsIgnoreCase("workflow")).forEach(k -> data.put(k, jobData.getString(k)));
        var input = new WorkflowTaskInput(data);
        workflow.execute(input);
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }
}
