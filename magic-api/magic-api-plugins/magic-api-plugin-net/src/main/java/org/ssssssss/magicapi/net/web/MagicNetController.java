package org.ssssssss.magicapi.net.web;

import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.JsonBean;
import org.ssssssss.magicapi.core.web.MagicController;
import org.ssssssss.magicapi.core.web.MagicExceptionHandler;
import org.ssssssss.magicapi.net.model.NetInfo;
import org.ssssssss.magicapi.net.util.NetDataSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class MagicNetController extends MagicController implements MagicExceptionHandler {

    public MagicNetController(MagicConfiguration configuration) {
        super(configuration);
    }

    @RequestMapping("/net/jdbc/test")
    @ResponseBody
    public JsonBean<String> test(@RequestBody NetInfo info) {
        try {
            NetDataSource netDataSource = new NetDataSource(info);
            if (netDataSource.validate()) {
                netDataSource.close();
                return new JsonBean<>("ok");
            } else {
                netDataSource.close();
                return new JsonBean<>("连接失败！");
            }
        } catch (Exception e) {
            return new JsonBean<>(e.getMessage());
        }
    }
}
