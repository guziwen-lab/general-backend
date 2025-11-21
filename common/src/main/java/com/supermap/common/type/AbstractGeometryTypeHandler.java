package com.supermap.common.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.postgresql.util.PGobject;
import org.springframework.util.ObjectUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将WKB转换为JTS对应的Geometry几何类型，或反向转换
 */
public class AbstractGeometryTypeHandler<T extends Geometry> extends BaseTypeHandler<Geometry> {

    private static final GeometryFactory geometryFactory;

    static {
        // 经度保留6为小数
        PrecisionModel precisionModel = new PrecisionModel(1000000);
        geometryFactory = new GeometryFactory(precisionModel, 4490);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Geometry geometry, JdbcType jdbcType) throws SQLException {
        PGobject object = new PGobject();
        object.setType("geometry");

        WKBWriter wkbWriter;
        if (geometry.getSRID() != 0) {
            wkbWriter = new WKBWriter(2, true);
        } else {
            wkbWriter = new WKBWriter();
        }

        byte[] bytes = wkbWriter.write(geometry);
        object.setValue(WKBWriter.toHex(bytes));
        preparedStatement.setObject(i, object);
    }

    @Override
    public Geometry getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Object object = resultSet.getObject(s);
        return getGeometry(object);
    }

    @Override
    public Geometry getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Object object = resultSet.getObject(i);
        return getGeometry(object);
    }

    @Override
    public Geometry getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Object object = callableStatement.getObject(i);
        return getGeometry(object);
    }

    private Geometry getGeometry(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            return null;
        }
        String source = object.toString();
        byte[] bytes = WKBReader.hexToBytes(source);
        WKBReader reader = new WKBReader(geometryFactory);
        try {
            return reader.read(bytes);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
