package com.shiyi.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @ClassName BoundSql
 * @Description
 * @Author FengL
 * @Date 2021/07/14 9:37
 * @Version V1.0
 */
@Data
@Builder
public class BoundSql {

    //解析过后的SQL  已经用? 替换了参数
    private String sqlText;

    //参数名
    private List<String> paramsList;
}
