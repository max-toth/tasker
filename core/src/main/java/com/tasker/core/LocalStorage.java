package com.tasker.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public class LocalStorage {
    private static List<Task> taskList = new ArrayList<>();

    public static List<Task> getTaskList() {
        return taskList;
    }

    public static void put(Task task) {
        taskList.add(task);
    }

    public static void sync() {

    }
}
