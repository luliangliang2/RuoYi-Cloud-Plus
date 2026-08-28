package org.dromara.magicapi.gis;

import org.dromara.gis.utils.GeoCoordinateUtils;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.magicapi.modules.DynamicModule;
import org.ssssssss.script.MagicScriptContext;
import org.ssssssss.script.annotation.Comment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Magic API GIS工具模块。
 *
 * <p>脚本中使用：import geo;</p>
 *
 * @author ruoyi
 */
@Component
@MagicModule("geo")
public class GeoMagicModule implements DynamicModule<GeoMagicModule> {

    @Override
    public GeoMagicModule getDynamicModule(MagicScriptContext context) {
        return this;
    }

    @Comment("WGS84转高德GCJ02")
    public Map<String, Double> wgs84ToGcj02(
        @Comment(name = "lng", value = "WGS84经度") Double lng,
        @Comment(name = "lat", value = "WGS84纬度") Double lat) {
        return toMap(GeoCoordinateUtils.wgs84ToGcj02(lng, lat));
    }

    @Comment("高德GCJ02转WGS84")
    public Map<String, Double> gcj02ToWgs84(
        @Comment(name = "lng", value = "GCJ02经度") Double lng,
        @Comment(name = "lat", value = "GCJ02纬度") Double lat) {
        return toMap(GeoCoordinateUtils.gcj02ToWgs84(lng, lat));
    }

    @Comment("高德GCJ02转百度BD09")
    public Map<String, Double> gcj02ToBd09(
        @Comment(name = "lng", value = "GCJ02经度") Double lng,
        @Comment(name = "lat", value = "GCJ02纬度") Double lat) {
        return toMap(GeoCoordinateUtils.gcj02ToBd09(lng, lat));
    }

    @Comment("百度BD09转高德GCJ02")
    public Map<String, Double> bd09ToGcj02(
        @Comment(name = "lng", value = "BD09经度") Double lng,
        @Comment(name = "lat", value = "BD09纬度") Double lat) {
        return toMap(GeoCoordinateUtils.bd09ToGcj02(lng, lat));
    }

    @Comment("WGS84转百度BD09")
    public Map<String, Double> wgs84ToBd09(
        @Comment(name = "lng", value = "WGS84经度") Double lng,
        @Comment(name = "lat", value = "WGS84纬度") Double lat) {
        return toMap(GeoCoordinateUtils.wgs84ToBd09(lng, lat));
    }

    @Comment("百度BD09转WGS84")
    public Map<String, Double> bd09ToWgs84(
        @Comment(name = "lng", value = "BD09经度") Double lng,
        @Comment(name = "lat", value = "BD09纬度") Double lat) {
        return toMap(GeoCoordinateUtils.bd09ToWgs84(lng, lat));
    }

    @Comment("GeoTools EPSG坐标系转换")
    public Map<String, Double> transformEpsg(
        @Comment(name = "sourceEpsg", value = "源坐标系，如EPSG:32650") String sourceEpsg,
        @Comment(name = "targetEpsg", value = "目标坐标系，如EPSG:4326") String targetEpsg,
        @Comment(name = "x", value = "源坐标x或经度") Double x,
        @Comment(name = "y", value = "源坐标y或纬度") Double y) {
        return toMap(GeoCoordinateUtils.transformEpsg(sourceEpsg, targetEpsg, x, y));
    }

    @Comment("机器人xyz局部坐标转WGS84")
    public Map<String, Double> robotToWgs84(
        @Comment(name = "x", value = "机器人局部坐标x，单位米") Double x,
        @Comment(name = "y", value = "机器人局部坐标y，单位米") Double y,
        @Comment(name = "originLng", value = "WGS84原点经度") Double originLng,
        @Comment(name = "originLat", value = "WGS84原点纬度") Double originLat,
        @Comment(name = "yawDeg", value = "局部坐标系相对正北顺时针旋转角度") Double yawDeg) {
        GeoCoordinateUtils.RobotCoordinateReference reference =
            new GeoCoordinateUtils.RobotCoordinateReference(originLng, originLat, 0D, yawDeg);
        return toMap(GeoCoordinateUtils.robotToWgs84(x, y, reference));
    }

    @Comment("WGS84转机器人xyz局部坐标")
    public Map<String, Double> wgs84ToRobot(
        @Comment(name = "lng", value = "WGS84经度") Double lng,
        @Comment(name = "lat", value = "WGS84纬度") Double lat,
        @Comment(name = "alt", value = "高度，单位米") Double alt,
        @Comment(name = "originLng", value = "WGS84原点经度") Double originLng,
        @Comment(name = "originLat", value = "WGS84原点纬度") Double originLat,
        @Comment(name = "originAlt", value = "原点高度") Double originAlt,
        @Comment(name = "yawDeg", value = "局部坐标系相对正北顺时针旋转角度") Double yawDeg) {
        GeoCoordinateUtils.RobotCoordinateReference reference =
            new GeoCoordinateUtils.RobotCoordinateReference(originLng, originLat, originAlt, yawDeg);
        GeoCoordinateUtils.RobotCoordinate coordinate = GeoCoordinateUtils.wgs84ToRobot(lng, lat, alt, reference);
        Map<String, Double> result = new LinkedHashMap<>(3);
        result.put("x", coordinate.x());
        result.put("y", coordinate.y());
        result.put("z", coordinate.z());
        return result;
    }

    @Comment("坐标路径JSON转换")
    public String convertPath(
        @Comment(name = "pathJson", value = "坐标路径JSON，如[[lng,lat],[lng,lat]]") String pathJson,
        @Comment(name = "from", value = "源坐标系：wgs84/gcj02/bd09") String from,
        @Comment(name = "to", value = "目标坐标系：wgs84/gcj02/bd09") String to) {
        return GeoCoordinateUtils.convertPath(pathJson, point -> convert(point.lng(), point.lat(), from, to));
    }

    private GeoCoordinateUtils.Coordinate convert(double lng, double lat, String from, String to) {
        String source = normalize(from);
        String target = normalize(to);
        if (source.equals(target)) {
            return new GeoCoordinateUtils.Coordinate(lng, lat);
        }
        if ("wgs84".equals(source) && "gcj02".equals(target)) {
            return GeoCoordinateUtils.wgs84ToGcj02(lng, lat);
        }
        if ("gcj02".equals(source) && "wgs84".equals(target)) {
            return GeoCoordinateUtils.gcj02ToWgs84(lng, lat);
        }
        if ("gcj02".equals(source) && "bd09".equals(target)) {
            return GeoCoordinateUtils.gcj02ToBd09(lng, lat);
        }
        if ("bd09".equals(source) && "gcj02".equals(target)) {
            return GeoCoordinateUtils.bd09ToGcj02(lng, lat);
        }
        if ("wgs84".equals(source) && "bd09".equals(target)) {
            return GeoCoordinateUtils.wgs84ToBd09(lng, lat);
        }
        if ("bd09".equals(source) && "wgs84".equals(target)) {
            return GeoCoordinateUtils.bd09ToWgs84(lng, lat);
        }
        throw new IllegalArgumentException("不支持的坐标系转换: " + from + " -> " + to);
    }

    private String normalize(String crs) {
        return crs == null ? "" : crs.trim().toLowerCase();
    }

    private Map<String, Double> toMap(GeoCoordinateUtils.Coordinate coordinate) {
        Map<String, Double> result = new LinkedHashMap<>(2);
        result.put("lng", coordinate.lng());
        result.put("lat", coordinate.lat());
        return result;
    }
}
