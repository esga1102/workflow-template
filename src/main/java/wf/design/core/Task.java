package wf.design.core;

public interface Task<T> {
    String getName();
    void execute(T context);
}

