package wf.design.core.tree;

import org.springframework.stereotype.Component;
import wf.design.core.context.ContextModel;

@Component
public class TreeWorkflow<T extends ContextModel> {

    public T runOneTreeTask(TreeNodeTask<T> parentTask, T context) {
        parentTask.task.execute(context);
        if (context.isRightTree && parentTask.getRight() != null) {
            runOneTreeTask(parentTask.getRight(), context);
        } else if (!context.isRightTree && parentTask.getLeft() != null) {
            runOneTreeTask(parentTask.getLeft(), context);
        }
        return context;
    }
}
