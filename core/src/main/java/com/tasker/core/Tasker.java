package com.tasker.core;

import com.tasker.core.cli.CommandLine;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public class Tasker {
    public static void main(String[] args) {
        CommandLine manager = new CommandLine();
        manager.handle(args);
    }
}