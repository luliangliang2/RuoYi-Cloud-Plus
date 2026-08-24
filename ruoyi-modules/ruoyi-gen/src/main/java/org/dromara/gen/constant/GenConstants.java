package org.dromara.gen.constant;

/**
 * 代码生成通用常量
 *
 * @author Lion Li
 */
public interface GenConstants {
    /**
     * 单表（增删改查）
     */
    String TPL_CRUD = "crud";

    /**
     * 树表（增删改查）
     */
    String TPL_TREE = "tree";

    /**
     * 树编码字段
     */
    String TREE_CODE = "treeCode";

    /**
     * 树父编码字段
     */
    String TREE_PARENT_CODE = "treeParentCode";

    /**
     * 树名称字段
     */
    String TREE_NAME = "treeName";

    /**
     * 上级菜单ID字段
     */
    String PARENT_MENU_ID = "parentMenuId";

    /**
     * 上级菜单名称字段
     */
    String PARENT_MENU_NAME = "parentMenuName";

    /**
<<<<<<< HEAD
=======
     * 是否启用导出能力
     */
    String ENABLE_EXPORT = "enableExport";

    /**
     * 是否启用状态切换能力
     */
    String ENABLE_STATUS = "enableStatus";

    /**
     * 状态字段
     */
    String STATUS_FIELD = "statusField";

    /**
     * 是否启用组合唯一校验
     */
    String ENABLE_UNIQUE = "enableUnique";

    /**
     * 组合唯一字段
     */
    String UNIQUE_FIELDS = "uniqueFields";

    /**
     * 是否启用排序调整能力
     */
    String ENABLE_SORT = "enableSort";

    /**
     * 排序字段
     */
    String SORT_FIELD = "sortField";

    /**
     * 默认前端模板类型，对应模板根目录下的 vue 目录。
     */
    String FRONTEND_TYPE_VUE = "vue";

    /**
     * React 前端模板类型，对应模板根目录下的 react 目录。
     */
    String FRONTEND_TYPE_REACT = "react";

    /**
     * 树根节点值
     */
    String TREE_ROOT_VALUE = "treeRootValue";

    /**
     * 树祖级字段
     */
    String TREE_ANCESTORS = "treeAncestors";

    /**
     * 树排序字段
     */
    String TREE_ORDER_FIELD = "treeOrderField";

    /**
>>>>>>> future/3.X
     * 数据库字符串类型
     */
    String[] COLUMNTYPE_STR = {"char", "varchar", "enum", "set", "nchar", "nvarchar", "varchar2", "nvarchar2",
        "long varchar", "long nvarchar", "uniqueidentifier", "uuid", "json", "jsonb", "xml"};

    /**
     * 数据库文本类型
     */
    String[] COLUMNTYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext", "binary", "varbinary", "blob",
        "ntext", "image", "bytea", "clob", "nclob", "long", "long raw", "raw"};

    /**
     * 数据库时间类型
     */
    String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp", "year", "interval",
        "smalldatetime", "datetime2", "datetimeoffset", "timestamptz", "timestamp with time zone",
        "timestamp without time zone", "time with time zone", "time without time zone"};

    /**
     * 数据库数字类型
     */
    String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "int2", "int4", "int8", "number", "integer",
        "bit", "boolean", "bool", "bigint", "float", "float4", "float8", "double", "decimal", "dec", "fixed",
        "numeric", "real", "double precision", "binary_float", "binary_double", "smallserial", "serial",
        "serial2", "serial4", "serial8", "bigserial", "money", "smallmoney"};

    /**
     * BO对象 不需要添加字段
     */
<<<<<<< HEAD
    String[] COLUMNNAME_NOT_ADD = {"create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "version", "tenant_id"};
=======
    String[] COLUMNNAME_NOT_ADD = {"del_flag", "version"};
>>>>>>> future/3.X

    /**
     * BO对象 不需要编辑字段
     */
<<<<<<< HEAD
    String[] COLUMNNAME_NOT_EDIT = {"create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "version", "tenant_id"};
=======
    String[] COLUMNNAME_NOT_EDIT = {"del_flag", "version"};
>>>>>>> future/3.X

    /**
     * VO对象 不需要返回字段
     */
    String[] COLUMNNAME_NOT_LIST = {"create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "version", "tenant_id"};

    /**
     * BO对象 不需要查询字段
     */
<<<<<<< HEAD
    String[] COLUMNNAME_NOT_QUERY = {"id", "create_dept", "create_by", "create_time", "del_flag", "update_by",
        "update_time", "remark", "version", "tenant_id"};
=======
    String[] COLUMNNAME_NOT_QUERY = {"id", "del_flag", "remark", "version"};
>>>>>>> future/3.X

    /**
     * Entity基类字段
     */
    String[] BASE_ENTITY = {"createDept", "createBy", "createTime", "updateBy", "updateTime", "tenantId"};

    /**
     * 文本框
     */
    String HTML_INPUT = "input";

    /**
     * 文本域
     */
    String HTML_TEXTAREA = "textarea";

    /**
     * 下拉框
     */
    String HTML_SELECT = "select";

    /**
     * 单选框
     */
    String HTML_RADIO = "radio";

    /**
     * 复选框
     */
    String HTML_CHECKBOX = "checkbox";

    /**
     * 日期控件
     */
    String HTML_DATETIME = "datetime";

    /**
     * 图片上传控件
     */
    String HTML_IMAGE_UPLOAD = "imageUpload";

    /**
     * 文件上传控件
     */
    String HTML_FILE_UPLOAD = "fileUpload";

    /**
     * 富文本控件
     */
    String HTML_EDITOR = "editor";

    /**
     * 字符串类型
     */
    String TYPE_STRING = "String";

    /**
     * 整型
     */
    String TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    String TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    String TYPE_DOUBLE = "Double";

    /**
     * 单精度浮点型
     */
    String TYPE_FLOAT = "Float";

    /**
     * 高精度计算类型
     */
    String TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型
     */
    String TYPE_DATE = "Date";

    /**
     * 模糊查询
     */
    String QUERY_LIKE = "LIKE";

    /**
     * 相等查询
     */
    String QUERY_EQ = "EQ";

    /**
     * 需要
     */
    String REQUIRE = "1";
}
