package com.tasker.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasker.core.LocalStorage;
import com.tasker.core.Task;

import java.io.*;
import java.util.List;

import static com.tasker.core.cli.ICommandLine.*;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
public class FileUtil {

    private File dir = new File(homedrive + homepath + tasker_dir);
    private File file = new File(homedrive + homepath + tasker_dir + data_file);

    private ObjectMapper objectMapper = new ObjectMapper();

    public File getDir() {
        return dir;
    }

    public File getFile() {
        return file;
    }

    public void saveFile(File file) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(objectMapper.writeValueAsString(LocalStorage.getTaskList()));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line, content = "";
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            LocalStorage.getTaskList().addAll(objectMapper.readValue(content, new TypeReference<List<Task>>() {}));
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createFile(File file) {
        createFile(file, "");
    }

    public void createFile(File file, String content) {
        Writer writer;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        saveFile(file);
    }

    public void readFile() {
        readFile(file);
    }
}
