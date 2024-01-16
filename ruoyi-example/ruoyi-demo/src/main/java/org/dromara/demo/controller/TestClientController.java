package org.dromara.demo.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.demo.client.RuiYiPlusClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * http客户端演示用例
 *
 * @author 21001
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class TestClientController {

    private final RuiYiPlusClient ruiYiPlusClient;

    /**
     * 获取验证码
     *
     * @return 验证码
     */
    @GetMapping("/code")
    public String testClient() {
        return ruiYiPlusClient.getCode();
    }

}
