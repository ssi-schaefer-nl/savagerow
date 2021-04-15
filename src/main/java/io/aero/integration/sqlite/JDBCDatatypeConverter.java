package io.aero.integration.sqlite;

import java.math.BigDecimal;
import java.sql.*;

public class JDBCDatatypeConverter {
    public static Object convertStringToType(String data, int typeCode) {
        switch (typeCode) {
            case Types.VARCHAR:
            case Types.LONGVARCHAR: return data;
            case Types.NUMERIC: return new BigDecimal(data);
            case Types.BIT: return Boolean.parseBoolean(data);
            case Types.TINYINT: return Byte.parseByte(data);
            case Types.SMALLINT: return Short.parseShort(data);
            case Types.INTEGER: return Integer.parseInt(data);
            case Types.BIGINT: return Long.parseLong(data);
            case Types.REAL: return Float.parseFloat(data);
            case Types.DOUBLE: return Double.parseDouble(data);
            case Types.VARBINARY:
            case Types.LONGVARBINARY: return data.getBytes();
            case Types.DATE: return Date.valueOf(data);
            case Types.TIME: return Time.valueOf(data);
            case Types.TIMESTAMP: return Timestamp.valueOf(data);
            default: return data;
        }
    }

    public static String convertTypeToStringName(int typeCode) {
        switch (typeCode) {
            case Types.VARCHAR:
            case Types.LONGVARCHAR: return "string";
            case Types.BIT: return "boolean";
            case Types.NUMERIC:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.REAL:
            case Types.DOUBLE: return "number";
            case Types.VARBINARY:
            case Types.LONGVARBINARY: return "bytes";
            case Types.DATE: return "date";
            case Types.TIME: return "time";
            case Types.TIMESTAMP: return "timestamp";
            default: return "string";
        }
    }
}
