package com.tasker.core.cli;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public interface ICommandLine {
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";

    String title_opt = "title";
    String create_opt = "create";
    String resolve_opt = "resolve";
    String sync_opt = "sync";
    String list_opt = "list";
    String help_opt = "help";
    String export_opt = "export";
    String filter_opt = "filter";


    String homedrive = System.getenv().get("HOMEDRIVE");
    String homepath = System.getenv().get("HOMEPATH");

    String tasker_dir = "/.Tasker";
    String data_file = "/data.tsk";

    String home = homedrive + homepath + tasker_dir;

    String task_arg_name = "TASK";
    String head_arg_name = "HEAD";

    String progress_state = "progress";
    String complete_state = "complete";
    String date_prefix = " -- ";

}
