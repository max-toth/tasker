package com.tasker.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * @author mtolstyh
 * @since 19.02.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    private String number;
    private String task;
    private String title;
    private Date date;
    private Date end;
    private Boolean status; //0 complete | 1 progress

    public Task() {
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void resolve() {
        this.setStatus(false);
        this.setEnd(new Date(System.currentTimeMillis()));
    }
}
