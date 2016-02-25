package com.tasker.core;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public interface ITaskerCore {
    void create(Task task);
    void sync(Task task);
    void resolve(Task task);
}
