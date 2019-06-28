package com.dosimple.rpc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author dosimple
 */
@Data
public class HystrixErrorMessage {
    @Id
    private String id;

    private String remark;

    private String serviceMethodName;
    private String className;
    private String methodParam;

    private Date dateCreated;
}
