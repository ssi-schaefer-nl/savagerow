package nl.ssischaefer.savaragerow.v1.sqlite.utils;

import java.math.BigDecimal;
import java.sql.*;

public class SQLiteDatatypeConverter {
    public static Object convertStringToType(String data, int typeCode) throws Exception {
        try {
            switch (typeCode) {
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    return data;
                case Types.NUMERIC:
                    return new BigDecimal(data);
                case Types.BIT:
                    return Boolean.parseBoolean(data);
                case Types.TINYINT:
                    return Byte.parseByte(data);
                case Types.SMALLINT:
                    return Short.parseShort(data);
                case Types.INTEGER:
                    return Integer.parseInt(data);
                case Types.BIGINT:
                    return Long.parseLong(data);
                case Types.REAL:
                    return Float.parseFloat(data);
                case Types.DOUBLE:
                    return Double.parseDouble(data);
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                    return data.getBytes();
                case Types.DATE:
                    return Date.valueOf(data);
                case Types.TIME:
                    return Time.valueOf(data);
                case Types.TIMESTAMP:
                    return Timestamp.valueOf(data);
                case Types.FLOAT:
                    return Float.parseFloat(data);
                default:
                    return data;
            }
        } catch (Exception e) {
            throw new Exception("Value must be of type " + convertTypeToStringName(typeCode));
        }
    }

    public static String convertTypeToStringName(int typeCode) {
        switch (typeCode) {
            case Types.NUMERIC:
                return "numeric";
            case Types.BIT:
                return "bit";
            case Types.TINYINT:
                return "tinyint";
            case Types.SMALLINT:
                return "smallint";
            case Types.INTEGER:
                return "integer";
            case Types.BIGINT:
                return "bigint";
            case Types.REAL:
                return "real";
            case Types.DOUBLE:
                return "double";
            case Types.DATE:
                return "date";
            case Types.TIME:
                return "time";
            case Types.TIMESTAMP:
                return "timestamp";
            case Types.FLOAT:
                return "float";
            default:
                return "text";
        }
    }

    public static String convertToSQLTypeName(String datatype) {
        switch (datatype) {
            case "text":
                return "TEXT";
            case "number":
                return "REAL";
            default:
                return "TEXT";
        }
    }
}
