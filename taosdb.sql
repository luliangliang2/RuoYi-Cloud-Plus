
-- 删除库
DROP DATABASE IF EXISTS 删除库;



-- 创建超级表：车辆告警事件
CREATE STABLE IF NOT EXISTS st_vehicle_alarm (
    ts           TIMESTAMP,      -- 告警触发时间
    alarm_code   INT,            -- 告警类型编码(如 1001=超速 1002=低电量)
    alarm_level  TINYINT,        -- 告警等级 1=提示 2=警告 3=严重
    alarm_msg    NCHAR(128),     -- 告警描述
    val          FLOAT,          -- 修改后的列名：将 value 改为 val，完美避开关键字
    handled      TINYINT         -- 是否已处理 0未处理 1已处理
) TAGS (
    vin          NCHAR(17),
    vehicle_type NCHAR(16)
);


-- 插入数据（自动创建子表）
INSERT INTO vin_alarm_LSGBC53L1ES123456 
USING st_vehicle_alarm TAGS('LSGBC53L1ES123456','PASSENGER_CAR')
VALUES (NOW, 1001, 2, '车速超过阈值', 121.5, 0);




-- 创建超级表：车辆历史状态/轨迹
CREATE STABLE IF NOT EXISTS st_vehicle_history (
    ts           TIMESTAMP,
    speed        FLOAT,          -- 车速 km/h
    rpm          INT,            -- 发动机/电机转速
    soc          TINYINT,        -- 电池SOC %
    lon          DOUBLE,         -- 经度
    lat          DOUBLE,         -- 纬度
    altitude     FLOAT,          -- 海拔 m
    mileage      BIGINT,         -- 累计里程 m
    online_status TINYINT        -- 0离线 1在线
) TAGS (
    vin          NCHAR(17),      -- 车架号(VIN) 作为子表区分标识
    plate_no     NCHAR(20),      -- 车牌号(含汉字，必须用 NCHAR)
    brand        NCHAR(32)       -- 品牌(可能含中文，必须用 NCHAR)
);

-- 插入数据（自动创建子表）
INSERT INTO vin_history_LSGBC53L1ES123456 
USING st_vehicle_history TAGS('LSGBC53L1ES123456','苏E·A12345','BYD')
VALUES (NOW, 60.5, 1200, 85, 120.58, 31.30, 15.2, 58200, 1);