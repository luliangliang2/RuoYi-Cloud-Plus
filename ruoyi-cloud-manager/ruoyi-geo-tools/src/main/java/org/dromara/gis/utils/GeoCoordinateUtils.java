package org.dromara.gis.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.referencing.CRS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * GIS坐标转换工具。
 *
 * <p>支持 WGS84、GCJ02(高德/国测局)、BD09(百度)、EPSG 坐标系与机器人局部 xyz 坐标互转。</p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GeoCoordinateUtils {

    private static final double X_PI = Math.PI * 3000.0 / 180.0;
    private static final double PI = Math.PI;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;
    private static final double EARTH_RADIUS = 6378137.0;

    public record Coordinate(double lng, double lat) {
    }

    public record RobotCoordinate(double x, double y, double z) {
    }

    /**
     * 机器人局部坐标系原点配置。
     *
     * @param originLng WGS84原点经度
     * @param originLat WGS84原点纬度
     * @param originAlt 原点高度
     * @param yawDeg 局部坐标系相对正北顺时针旋转角度，单位度
     */
    public record RobotCoordinateReference(double originLng, double originLat, double originAlt, double yawDeg) {
    }

    @FunctionalInterface
    public interface CoordinateConverter {
        Coordinate convert(Coordinate coordinate);
    }

    public static Coordinate wgs84ToGcj02(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new Coordinate(lng, lat);
        }
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
        dLng = (dLng * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
        return new Coordinate(lng + dLng, lat + dLat);
    }

    public static Coordinate gcj02ToWgs84(double lng, double lat) {
        if (outOfChina(lng, lat)) {
            return new Coordinate(lng, lat);
        }
        Coordinate gcj = wgs84ToGcj02(lng, lat);
        return new Coordinate(lng * 2 - gcj.lng(), lat * 2 - gcj.lat());
    }

    public static Coordinate gcj02ToBd09(double lng, double lat) {
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * X_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * X_PI);
        return new Coordinate(z * Math.cos(theta) + 0.0065, z * Math.sin(theta) + 0.006);
    }

    public static Coordinate bd09ToGcj02(double lng, double lat) {
        double x = lng - 0.0065;
        double y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        return new Coordinate(z * Math.cos(theta), z * Math.sin(theta));
    }

    public static Coordinate wgs84ToBd09(double lng, double lat) {
        Coordinate gcj02 = wgs84ToGcj02(lng, lat);
        return gcj02ToBd09(gcj02.lng(), gcj02.lat());
    }

    public static Coordinate bd09ToWgs84(double lng, double lat) {
        Coordinate gcj02 = bd09ToGcj02(lng, lat);
        return gcj02ToWgs84(gcj02.lng(), gcj02.lat());
    }

    /**
     * 使用 GeoTools 在任意 EPSG 坐标系之间转换。
     *
     * @param sourceEpsg 源坐标系，例如 EPSG:32650
     * @param targetEpsg 目标坐标系，例如 EPSG:4326
     * @param x 源坐标x或经度
     * @param y 源坐标y或纬度
     * @return 转换后的坐标
     */
    public static Coordinate transformEpsg(String sourceEpsg, String targetEpsg, double x, double y) {
        try {
            CoordinateReferenceSystem sourceCrs = CRS.decode(sourceEpsg, true);
            CoordinateReferenceSystem targetCrs = CRS.decode(targetEpsg, true);
            MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs, true);
            double[] source = new double[]{x, y};
            double[] target = new double[2];
            transform.transform(source, 0, target, 0, 1);
            return new Coordinate(round(target[0]), round(target[1]));
        } catch (FactoryException | TransformException e) {
            throw new ServiceException("坐标系转换失败: " + e.getMessage());
        }
    }

    public static RobotCoordinate wgs84ToRobot(double lng, double lat, double alt, RobotCoordinateReference reference) {
        double originLatRad = Math.toRadians(reference.originLat());
        double east = Math.toRadians(lng - reference.originLng()) * EARTH_RADIUS * Math.cos(originLatRad);
        double north = Math.toRadians(lat - reference.originLat()) * EARTH_RADIUS;
        double yaw = Math.toRadians(reference.yawDeg());
        double x = east * Math.cos(yaw) + north * Math.sin(yaw);
        double y = -east * Math.sin(yaw) + north * Math.cos(yaw);
        double z = alt - reference.originAlt();
        return new RobotCoordinate(round(x), round(y), round(z));
    }

    public static Coordinate robotToWgs84(double x, double y, RobotCoordinateReference reference) {
        double yaw = Math.toRadians(reference.yawDeg());
        double east = x * Math.cos(yaw) - y * Math.sin(yaw);
        double north = x * Math.sin(yaw) + y * Math.cos(yaw);
        double lat = reference.originLat() + Math.toDegrees(north / EARTH_RADIUS);
        double lng = reference.originLng()
            + Math.toDegrees(east / (EARTH_RADIUS * Math.cos(Math.toRadians(reference.originLat()))));
        return new Coordinate(round(lng), round(lat));
    }

    public static String convertPath(String pathJson, CoordinateConverter converter) {
        List<Coordinate> coordinates = parsePath(pathJson);
        JSONArray array = new JSONArray();
        for (Coordinate coordinate : coordinates) {
            Coordinate converted = converter.convert(coordinate);
            JSONArray point = new JSONArray();
            point.add(round(converted.lng()));
            point.add(round(converted.lat()));
            array.add(point);
        }
        return array.toString();
    }

    public static Coordinate convertPoint(double lng, double lat, CoordinateConverter converter) {
        Coordinate converted = converter.convert(new Coordinate(lng, lat));
        return new Coordinate(round(converted.lng()), round(converted.lat()));
    }

    public static List<Coordinate> parsePath(String pathJson) {
        if (!JSONUtil.isTypeJSONArray(pathJson)) {
            throw new ServiceException("场景范围格式不正确");
        }
        JSONArray array = JSONUtil.parseArray(pathJson);
        List<Coordinate> coordinates = new ArrayList<>(array.size());
        for (Object item : array) {
            JSONArray point = JSONUtil.parseArray(item);
            if (point.size() < 2) {
                throw new ServiceException("场景范围坐标点格式不正确");
            }
            coordinates.add(new Coordinate(point.getDouble(0), point.getDouble(1)));
        }
        return coordinates;
    }

    private static double round(double value) {
        return BigDecimal.valueOf(value).setScale(8, RoundingMode.HALF_UP).doubleValue();
    }

    private static boolean outOfChina(double lng, double lat) {
        return lng < 72.004 || lng > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
            + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y
            + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

}
