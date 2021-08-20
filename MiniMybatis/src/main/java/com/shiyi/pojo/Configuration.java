package com.shiyi.pojo;

import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**存储sqlMapConfig的配置信息
 * @ClassName Configuration
 * @Description
 * @Author FengL
 * @Date 2021/06/17 14:45
 * @Version V1.0
 */
@Data
public class Configuration {

    private DataSource dataSource;
    /**
     * key :statementId value: mapperedStatement
     */
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();
}
