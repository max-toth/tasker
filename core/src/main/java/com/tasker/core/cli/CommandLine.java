package com.tasker.core.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasker.core.LocalStorage;
import com.tasker.core.Task;
import com.tasker.core.db.DatabaseManager;
import com.tasker.core.utils.FileUtil;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.tasker.core.cli.ICommandLine.*;
import static org.fusesource.jansi.Ansi.*;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public class CommandLine {

    public static final String export_opt = "export";
    private Options options;

    private ObjectMapper objectMapper = new ObjectMapper();
    private FileUtil fileUtil = new FileUtil();

    public CommandLine() {
        this.options = new Options();
        options.addOption(
                Option.builder("c").longOpt(create_opt)
                        .hasArgs()
                        .argName(task_arg_name).type(String.class).desc("Sentence of task short description")
                        .build()
        );
        options.addOption(
                Option.builder("t").longOpt(title_opt)
                        .hasArgs()
                        .argName(head_arg_name).type(String.class).desc("Project name, title, or any header...")
                        .build()
        );
        options.addOption(
                Option.builder("r").longOpt(resolve_opt)
                        .hasArg().argName("TASK_ID")
                        .valueSeparator(' ')
                        .type(String.class).desc("Resolve task by ID")
                        .build()
        );
        options.addOption(
                Option.builder("e").longOpt(export_opt)
                        .hasArg().argName("TYPE")
                        .valueSeparator(' ')
                        .type(String.class).desc("Export to JSON or CSF format")
                        .build()
        );
        options.addOption("s", sync_opt, false, "Sync all tasks with storage");
        options.addOption(Option.builder("l").longOpt(list_opt)
                .hasArg().argName("SIZE").optionalArg(true)
                .valueSeparator(' ')
                .type(String.class).desc("List all tasks in storage")
                .build());
        options.addOption("h", help_opt, false, "Tasker help");

        init();
    }

    public void handle(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            org.apache.commons.cli.CommandLine line = parser.parse(options, args);
            if (line.hasOption(create_opt)) {
                createTask(line);
                System.exit(0);
            }
            if (line.hasOption(title_opt)) {
                createTask(line);
                System.exit(0);
            }
            if (line.hasOption(sync_opt)) {
                syncDB();
                System.exit(0);
            }
            if (line.hasOption(list_opt)) {
                int SIZE = 0;
                String optionValue = line.getOptionValue(list_opt);
                if (optionValue != null) {
                    SIZE = Integer.parseInt(optionValue);
                }
                list(SIZE);
                System.exit(0);
            }
            if (line.hasOption(help_opt)) {
                help();
                System.exit(0);
            }
            if (line.hasOption(resolve_opt)) {
                resolve(line);
            }
            if (line.hasOption(export_opt)) {
                export(line);
                System.exit(0);
            }
            if (line.getOptions().length == 0) {
                help();
                System.exit(0);
            }
        } catch (ParseException exp) {
            System.out.println(exp.getLocalizedMessage());
            help();
            System.exit(-1);
        }
        System.exit(0);
    }

    public void syncDB() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.sync();
    }

    public void export(org.apache.commons.cli.CommandLine line) {
        String optionValue = line.getOptionValue(export_opt);
        if (StringUtils.isEmpty(optionValue) || optionValue.equals("JSON")) {
            try {
                String content = objectMapper.writeValueAsString(LocalStorage.getTaskList());
                File exportFile = new File(home + "/tasker_export_" + System.currentTimeMillis() + ".json");
                fileUtil.createFile(exportFile, content);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void list(int size) {
        AnsiConsole.systemInstall();
        List<Task> taskList = LocalStorage.getTaskList();
        int listSize = taskList.size();
        int n = 0;
        if ((size > 0) && (size < listSize)) {
            n = listSize - size;
            System.out.println("Output " + size + " of " + listSize + " tasks.");
        }
        for (int i = n; i < listSize; i++) {
            Task task = taskList.get(i);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            String start = simpleDateFormat.format(task.getDate());

            String endDate = getEndDate(task);

            System.out.println(
                    ansi().a(task.getNumber()).a("\t")
                            .fg(task.getStatus() ? Color.RED : Color.GREEN).a(task.getStatus() ? progress_state : complete_state).reset()
                            .a(date_prefix).a(start)
                            .a(date_prefix).a(endDate).a("\t")
                            .a(task.getTitle()).a(".").a(task.getTask())
            );
        }
        AnsiConsole.systemUninstall();
//        for (Task task: LocalStorage.getTaskList()) {
//            System.out.println(
//                    ANSI_RESET + task.getNumber()
//                            + "\t" + (task.getStatus() ? ANSI_RED + "progress" : ANSI_GREEN + "complete") + ANSI_RESET
//                            + " -- " + new SimpleDateFormat().format(task.getDate())
//                            + (task.getEnd() == null ? "\t" : " -- " + new SimpleDateFormat().format(task.getEnd()))
//                            + "\t" + task.getTitle() + "." + task.getTask()
//            );
//        }
    }

    public void init() {
        if (!fileUtil.getDir().exists()) {
            boolean mkdir = fileUtil.getDir().mkdir();
            if (mkdir) {
                if (!fileUtil.getFile().exists()) {
                    fileUtil.saveFile();
                }
            }
        } else {
            if (fileUtil.getFile().exists()) {
                fileUtil.readFile();
            } else {
                fileUtil.saveFile();
            }
        }
    }

    public void sync() {
        if (!fileUtil.getDir().exists()) {
            boolean mkdir = fileUtil.getDir().mkdir();
            if (mkdir) {
                fileUtil.saveFile();
            }
        } else {
            fileUtil.saveFile();
        }
    }

    private void help() {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Tasker", options, true);
    }

    private String getEndDate(Task task) {
        String endDate = "\t";
        if (task.getEnd() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Calendar calendar = simpleDateFormat.getCalendar();
            calendar.setTime(task.getDate());
            int dayStart = calendar.get(Calendar.DAY_OF_MONTH);
            int monthStart = calendar.get(Calendar.MONTH);
            int yearStart = calendar.get(Calendar.YEAR);

            calendar.setTime(task.getEnd());
            int dayEnd = calendar.get(Calendar.DAY_OF_MONTH);
            int monthEnd = calendar.get(Calendar.MONTH);
            int yearEnd = calendar.get(Calendar.YEAR);
            if (dayStart != dayEnd || monthStart != monthEnd || yearStart != yearEnd) {
                endDate = simpleDateFormat.format(task.getEnd());
            } else {
                endDate = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + "\t";
            }
        } else {
            endDate += "\t";
        }
        return endDate;
    }

    private void resolve(org.apache.commons.cli.CommandLine line) {
        String taskID = line.getOptionValue(resolve_opt);
        boolean resolved = false;
        if (StringUtils.isNotEmpty(taskID)) {
            for (Task t : LocalStorage.getTaskList()) {
                if (t.getNumber().equals(taskID)) {
                    t.resolve();
                    System.out.println("++++++ task " + t.getNumber() + " resolved");
                    resolved = true;
                    break;
                }
            }
        }
        if (!resolved) {
            System.out.println("Task with number [" + taskID + "] not found.");
        }

        sync();
    }

    private void createTask(org.apache.commons.cli.CommandLine line) {

        Task task = new Task();
        task.setStatus(true);
        task.setNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 4));
        task.setDate(new Date(System.currentTimeMillis()));

        if (line.hasOption(title_opt)) task.setTitle(line.getOptionValue(title_opt));
        if (line.hasOption(create_opt)) task.setTask(line.getOptionValue(create_opt));

        try {
            System.out.println("--->" + objectMapper.writeValueAsString(task));
            LocalStorage.put(task);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        sync();
    }
}
