package com.ruoyi.common.iotdb.utils;

import cn.hutool.core.date.DateUtil;
import org.apache.iotdb.rpc.IoTDBConnectionException;
import org.apache.iotdb.rpc.StatementExecutionException;
import org.apache.iotdb.session.pool.SessionDataSetWrapper;
import org.apache.iotdb.tsfile.file.metadata.enums.TSDataType;
import org.apache.iotdb.tsfile.read.common.Field;
import org.apache.iotdb.tsfile.read.common.RowRecord;

import java.util.*;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 13:39
 * @description：
 * @modified By：wt
 */
public class FormatUtils {


    public static Map<String, Object> formatResponse(SessionDataSetWrapper response) throws IoTDBConnectionException, StatementExecutionException {
        Map<String, Object> formatMap = new HashMap<>();
        List<List<Object>> tables = new ArrayList<>();
        if (response!=null){
            List<String> columnNames = response.getColumnNames();
            formatMap.put("columnNames",columnNames);
            while (response.hasNext()){
                RowRecord next = response.next();
                if (next.getTimestamp()==0) {
                    List<Object> rowList = new ArrayList<>();
                    int size = columnNames.size();
                    for (int i = 0; i < size; i++) {
                        if (!(next.getFields().get(i).getDataType() == null)) {
                            Object val = getVal(next.getFields().get(i));
                            rowList.add(val);
                        }

                    }
                    tables.add(rowList);
                }else {
                    //获取一行数据,首位为时间
                    List<Object> rowList = new ArrayList<>();
                    //第0位添加时间
                    String time = DateUtil.formatDateTime(new Date(next.getTimestamp()));
                    rowList.add(time);
                    int size = columnNames.size();
                    for (int i = 0; i < size - 1; i++) {
                        if (!(next.getFields().get(i).getDataType() == null)) {
                            Object val = getVal(next.getFields().get(i));
                            rowList.add(val);
                        }

                    }
                    tables.add(rowList);
                }
            }
            formatMap.put("dataTable",tables);
        }
        return formatMap;
    }



    private static Object getVal(Field field){
        Object value;
        TSDataType type = field.getDataType();
        switch (type) {
            case INT32:
                value = field.getIntV();
                break;
            case INT64:
                value = field.getLongV();
                break;
            case FLOAT:
                value = field.getFloatV();
                break;
            case DOUBLE:
                value = field.getDoubleV();
                break;
            case BOOLEAN:
                value = field.getBoolV();
                break;
            case VECTOR:
                value = field.getBinaryV();
                break;
            case TEXT:
            default:
                value = field.getStringValue();
        }
        return value;
    }
}
