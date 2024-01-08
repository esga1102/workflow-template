package wf.design.core.tree;

import lombok.Getter;
import lombok.Setter;
import wf.design.core.Task;

@Getter
@Setter
public class TreeNodeTask<T> {
    TreeNodeTask<T> left;
    TreeNodeTask<T> right;
    Task<T> task;

    public TreeNodeTask(Task<T> coreTask) {
        left = null;
        right = null;
        task = coreTask;
    }

    public static <T> TreeNodeTask<T> of(Task<T> task){
        return new TreeNodeTask<>(task);
    }
}