package org.dromara.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件类型枚举
 *
 * @author Feng
 */
@Getter
@AllArgsConstructor
public class MailType {

    /**
     * 表示业务级别的枚举。
     * <p>
     * 该枚举包含了系统、租户和部门等不同的业务级别。
     * </p>
     */
    @Getter
    public enum BusinessLevel {

        /**
         * 业务级别 - 系统
         */
        SYSTEM("0"),

        /**
         * 业务级别 - 租户
         */
        TENANT("1"),

        /**
         * 业务级别 - 部门
         */
        DEPARTMENT("2");

        private final String value;

        /**
         * BusinessLevel 枚举的构造方法。
         *
         * @param value 枚举常量的关联值。
         */
        BusinessLevel(String value) {
            this.value = value;
        }

        /**
         * 获取业务级别的值。
         *
         * @return 业务级别的值。
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * 表示消息类型的枚举。
     * <p>
     * 该枚举包含了单个和多个两种不同的消息类型。
     * </p>
     */
    @Getter
    public enum MessageType {

        /**
         * 消息类型 - 单个
         */
        SINGLE("0"),

        /**
         * 消息类型 - 多个
         */
        MULTIPLE("1");

        private final String value;

        /**
         * MessageType 枚举的构造方法。
         *
         * @param value 枚举常量的关联值。
         */
        MessageType(String value) {
            this.value = value;
        }

        /**
         * 获取消息类型的值。
         *
         * @return 消息类型的值。
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * 表示邮件类型的枚举。
     * <p>
     * 该枚举包含了文本和HTML两种不同的邮件类型。
     * </p>
     */
    @Getter
    public enum EmailType {

        /**
         * 邮件类型 - 文本
         */
        TEXT("0"),

        /**
         * 邮件类型 - HTML
         */
        HTML("1");

        private final String value;

        /**
         * EmailType 枚举的构造方法。
         *
         * @param value 枚举常量的关联值。
         */
        EmailType(String value) {
            this.value = value;
        }

        /**
         * 获取邮件类型的值。
         *
         * @return 邮件类型的值。
         */
        public String getValue() {
            return value;
        }
    }

    /**
     * 表示邮件发送状态的枚举。
     * <p>
     * 该枚举包含了成功和失败两种不同的邮件发送状态。
     * </p>
     */
    @Getter
    public enum EmailStatus {

        /**
         * 邮件发送状态 - 成功
         */
        SUCCESS("0"),

        /**
         * 邮件发送状态 - 失败
         */
        FAILURE("1");

        private final String value;

        /**
         * EmailStatus 枚举的构造方法。
         *
         * @param value 枚举常量的关联值。
         */
        EmailStatus(String value) {
            this.value = value;
        }

        /**
         * 获取邮件发送状态的值。
         *
         * @return 邮件发送状态的值。
         */
        public String getValue() {
            return value;
        }
    }


}
