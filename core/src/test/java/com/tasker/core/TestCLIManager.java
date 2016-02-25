package com.tasker.core;

import com.tasker.core.cli.CommandLine;
import org.junit.Test;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public class TestCLIManager {

    @Test
    public void testCreateTaskOptions01() {
        new CommandLine().handle(new String[]{});
    }
    @Test
    public void testCreateTaskOptions0() {
        String[] args = new String[] {"-h"};
        new CommandLine().handle(args);
    }
    @Test
    public void testCreateTaskOptionsList() {
        String[] args = new String[] {"-l"};
        new CommandLine().handle(args);
    }

    @Test
    public void testCreateTaskOptionsExport() {
        String[] args = new String[] {"-e"};
        new CommandLine().handle(args);
    }

    @Test
    public void testCreateTaskOptionsSyncDB() {
        String[] args = new String[] {"-s"};
        new CommandLine().handle(args);
    }

    @Test
    public void testCreateTaskOptionsExportJSON() {
        String[] args = new String[] {"-e", "JSON"};
        new CommandLine().handle(args);
    }

    @Test
    public void testCreateTaskOptionsResolve() {
        String[] args = new String[] {"-r", "38eadf05bfe647d983fcde9b85f59e47"};
        new CommandLine().handle(args);
    }
    @Test
    public void testCreateTaskOptions1() {
        String[] args = new String[] {"-c", "Maersk \"Just call me angel\""};
        new CommandLine().handle(args);
    }
    @Test
    public void testCreateTaskOptions2() {
        String[] args = new String[] {"-t", "Maersk"};
        new CommandLine().handle(args);
    }
    @Test
    public void testCreateTaskOptions3() {
        String[] args = new String[] {"-t", "Maersk", "-c", "Just call me angel "};
        CommandLine manager = new CommandLine();
        manager.list();
        manager.handle(args);
    }
}
