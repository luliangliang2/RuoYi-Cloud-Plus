package org.dromara.workflow.api.domain.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Attachment模型
 * 与{@link org.flowable.engine.task.Attachment}属性一致
 *
 * @Author ZETA
 * @Date 2024/5/29
 */
public class AttachmentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String type;
    private String taskId;
    private String processInstanceId;
    private String url;
    private String userId;
    private Date time;
    private String contentId;
}
