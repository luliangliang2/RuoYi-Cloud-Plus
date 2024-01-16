package org.dromara.demo.client;

import com.dtflys.forest.annotation.Get;

/**
 * RuiYiPlus客户端
 *
 * @author 21001
 */
public interface RuiYiPlusClient {

    @Get("http://43.138.9.96/prod-api/auth/code")
    String getCode();

}
