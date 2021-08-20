package com.shiyi.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName MappedStatement
 * @Description
 * @Author FengL
 * @Date 2021/07/02 16:56
 * @Version V1.0
 */
@Data
@Builder
public class MappedStatement {

    private String id;
    private String resultType;
    private String paramterType;
    private String sql;
    private String sqlType;
}
