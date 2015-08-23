package com.michaelho.watermonitor.constants;

/**
 * Created by Administrator on 2015/8/13.
 */
public interface ServerSQLcommands {
    public static final String COMMAND_GET_EVENTS = "SELECT * FROM table_campus_events order by Id desc";
    public static final String COMMAND_GET_BULDING_LIST = "SELECT * FROM table_reg order by Id desc";
    public static final String COMMAND_GET_LAB_LIST = "SELECT * FROM table_lab_reg order by Id desc";
}
