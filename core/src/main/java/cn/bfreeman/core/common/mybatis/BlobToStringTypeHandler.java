package cn.bfreeman.core.common.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author xiang.rao created on 11/15/17 9:34 AM
 * @version $Id$
 */
public class BlobToStringTypeHandler extends BaseTypeHandler<String> {


    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    String parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis;

        try {
            if (Objects.isNull(parameter)) {
                return;
            }
            bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));
            ps.setBinaryStream(i, bis, parameter.length());
        } catch (java.io.IOException e) {
            throw new RuntimeException("Blob encoding failure!", e);
        }
    }


    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        return decodeValue(returnValue);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        return decodeValue(returnValue);
    }

    private String decodeValue(byte[] returnValue) {
        try {
            if (Objects.isNull(returnValue)) {
                return StringUtils.EMPTY;
            }
            String retString = new String(returnValue, DEFAULT_CHARSET);
            return retString;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Blob decoding Error!", e);
        }
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int columnIndex)
            throws SQLException {
        // TODO Auto-generated method stub
        Blob blob = resultSet.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        return decodeValue(returnValue);
    }
}

