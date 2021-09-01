package nl.ssischaefer.savaragerow.workflow.util;

import nl.ssischaefer.savaragerow.common.schema.AbstractWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.common.schema.WorkflowTriggerSchema;
import nl.ssischaefer.savaragerow.workflow.exceptions.NoInitialTaskException;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaskLinker {
    public static List<AbstractWorkflowTask> linkTasks(List<AbstractWorkflowTask> tasks, List<AbstractWorkflowTaskSchema> tasksSchema) {
        Map<Long, Long> linkingMap = new HashMap<>();
        for (var t: tasksSchema) {
            if(t.getNext() != null) {
                linkingMap.put(t.getId(), t.getNext());
            }
        }

        for (var t : tasks) {
            var targetId = linkingMap.get(t.getId());
            if(targetId == null) continue;
            tasks.stream().filter(x -> x.getId().equals(targetId)).findFirst().ifPresent(t::setNext);
        }
        return tasks;
    }
}
