import Evaluation from "./Evaluation/Evaluation";
import Task from "./Tasks/Task";
import Trigger from "./Trigger/Trigger";

const NodeTypes = {
    'trigger': Trigger,
    'evaluation': Evaluation,
    'task': Task,
};

export default NodeTypes