package wf.design.core.linear;

import wf.design.core.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class LinearWorkflow<T> {

    List<Task<T>> taskList;

    public T addTask(Task<T> currentTask) {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        this.taskList.add(currentTask);
        return (T) this;
    }

    public void run(Task<T> currentTask) {
        currentTask.execute((T) this);
    }

    public void serialRun() {
        taskList.forEach(task -> task.execute((T) this));
    }
}