package com.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * kafka dto
 *
 * @author fuhongxing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaTopicDto implements Serializable {

    /**
     * topic名称
     */
    @NotBlank(message = "topic名称不能为空")
    private String topicName;

    /**
     * 分区数量
     */
    @NotBlank(message = "分区数量不能为空")
    private Integer partitionNum;

    /**
     * 红包编码
     */
    private Integer replicaNum = 1;

}