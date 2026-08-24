package org.dromara.gen.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;

/**
 * 代码生成业务字段表 gen_table_column
 *
 * @author Lion Li
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("gen_table_column")
public class GenTableColumn extends BaseEntity {

    /**
     * 编号
     */
    @TableId(value = "column_id")
    private Long columnId;

    /**
     * 归属表编号
     */
    private Long tableId;

    /**
     * 列名称
     */
    private String columnName;

    /**
     * 列描述
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String columnComment;

    /**
     * 列类型
     */
    private String columnType;

    /**
     * JAVA类型
     */
    private String javaType;

    /**
     * JAVA字段名
     */
    @NotBlank(message = "Java属性不能为空")
    private String javaField;

    /**
     * 是否主键（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isPk;

    /**
     * 是否自增（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isIncrement;

    /**
     * 是否必填（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isRequired;

    /**
     * 是否为插入字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isInsert;

    /**
     * 是否编辑字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isEdit;

    /**
     * 是否列表字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isList;

    /**
     * 是否查询字段（1是）
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String isQuery;

    /**
     * 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围）
     */
    private String queryType;

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    private String htmlType;

    /**
     * 字典类型
     */
    @TableField(updateStrategy = FieldStrategy.ALWAYS, jdbcType = JdbcType.VARCHAR)
    private String dictType;

    /**
     * 排序
     */
    private Integer sort;

    public String getCapJavaField() {
        return StringUtils.capitalize(javaField);
    }

<<<<<<< HEAD
=======
    /**
     * 获取适合界面展示的字段注释，去除括号内的枚举说明。
     *
     * @return 字段展示名称
     */
    public String getColumnLabel() {
        int index = StringUtils.indexOf(this.columnComment, "（");
        if (index != StringUtils.INDEX_NOT_FOUND) {
            return StringUtils.substring(this.columnComment, 0, index);
        }
        return this.columnComment;
    }

    /**
     * 获取 TypeScript 字段类型。
     *
     * @return TypeScript 类型
     */
    public String getTsType() {
        if (StringUtils.containsAny(this.javaField, "id", "Id")) {
            return "string | number";
        }
        if (StringUtils.equalsAny(this.javaType,
            GenConstants.TYPE_LONG, GenConstants.TYPE_INTEGER, GenConstants.TYPE_DOUBLE,
            GenConstants.TYPE_FLOAT, GenConstants.TYPE_BIGDECIMAL)) {
            return "number";
        }
        if (StringUtils.equals(this.javaType, GenConstants.TYPE_BOOLEAN)) {
            return "boolean";
        }
        return "string";
    }

    /**
     * 获取开关启用值的前端字面量。
     *
     * @return 前端字面量
     */
    public String getSwitchActiveValue() {
        if (StringUtils.equals(this.javaType, GenConstants.TYPE_BOOLEAN)) {
            return "true";
        }
        if (isNumberType()) {
            return "0";
        }
        return "'0'";
    }

    /**
     * 获取开关停用值的前端字面量。
     *
     * @return 前端字面量
     */
    public String getSwitchInactiveValue() {
        if (StringUtils.equals(this.javaType, GenConstants.TYPE_BOOLEAN)) {
            return "false";
        }
        if (isNumberType()) {
            return "1";
        }
        return "'1'";
    }

    /**
     * 判断当前字段是否为数值类型。
     *
     * @return 数值类型返回 {@code true}
     */
    public boolean isNumberType() {
        return StringUtils.equalsAny(this.javaType,
            GenConstants.TYPE_INTEGER, GenConstants.TYPE_LONG, GenConstants.TYPE_DOUBLE,
            GenConstants.TYPE_FLOAT, GenConstants.TYPE_BIGDECIMAL);
    }

    /**
     * 是否为日期范围查询字段。
     *
     * @return 日期范围查询字段返回 {@code true}
     */
    public boolean isDateRangeQuery() {
        return isQuery()
            && StringUtils.equals(this.htmlType, GenConstants.HTML_DATETIME)
            && StringUtils.equals(this.queryType, GenConstants.QUERY_BETWEEN);
    }

    /**
     * 判断当前列是否为主键列。
     *
     * @return 主键列返回 {@code true}
     */
>>>>>>> future/3.X
    public boolean isPk() {
        return isPk(this.isPk);
    }

    public boolean isPk(String isPk) {
        return isPk != null && StringUtils.equals("1", isPk);
    }

    public boolean isIncrement() {
        return isIncrement(this.isIncrement);
    }

    public boolean isIncrement(String isIncrement) {
        return isIncrement != null && StringUtils.equals("1", isIncrement);
    }

    public boolean isRequired() {
        return isRequired(this.isRequired);
    }

    public boolean isRequired(String isRequired) {
        return isRequired != null && StringUtils.equals("1", isRequired);
    }

    public boolean isInsert() {
        return isInsert(this.isInsert);
    }

    public boolean isInsert(String isInsert) {
        return isInsert != null && StringUtils.equals("1", isInsert);
    }

    public boolean isEdit() {
        return isEdit(this.isEdit);
    }

    public boolean isEdit(String isEdit) {
        return isEdit != null && StringUtils.equals("1", isEdit);
    }

    public boolean isList() {
        return isList(this.isList);
    }

    public boolean isList(String isList) {
        return isList != null && StringUtils.equals("1", isList);
    }

    public boolean isQuery() {
        return isQuery(this.isQuery);
    }

    public boolean isQuery(String isQuery) {
        return isQuery != null && StringUtils.equals("1", isQuery);
    }

    public boolean isSuperColumn() {
        return isSuperColumn(this.javaField);
    }

    public static boolean isSuperColumn(String javaField) {
        return StringUtils.equalsAnyIgnoreCase(javaField,
            // BaseEntity
            "createBy", "createTime", "updateBy", "updateTime",
            // TreeEntity
            "parentName", "parentId");
    }

    public boolean isUsableColumn() {
        return isUsableColumn(javaField);
    }

    public static boolean isUsableColumn(String javaField) {
        // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
        return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum", "remark");
    }

    public String readConverterExp() {
        String remarks = StringUtils.substringBetween(this.columnComment, "（", "）");
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(remarks)) {
            for (String value : remarks.split(" ")) {
                if (StringUtils.isNotEmpty(value)) {
                    Object startStr = value.subSequence(0, 1);
                    String endStr = value.substring(1);
                    sb.append(StringUtils.EMPTY).append(startStr).append("=").append(endStr).append(StringUtils.SEPARATOR);
                }
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return this.columnComment;
        }
    }
}
