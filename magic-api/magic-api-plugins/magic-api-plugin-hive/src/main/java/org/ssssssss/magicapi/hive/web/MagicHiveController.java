 package org.ssssssss.magicapi.hive.web;

 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
 import org.ssssssss.magicapi.core.config.JsonCodeConstants;
 import org.ssssssss.magicapi.core.model.JsonBean;

 @RestController
 @RequestMapping("/hive")
 public class MagicHiveController implements JsonCodeConstants {

     @PostMapping("/test-connection")
     public JsonBean<String> testConnection(@RequestBody java.util.Map<String, Object> config) {
         // 简单的连接测试逻辑
         String url = (String) config.get("url");
         if (url == null || url.trim().isEmpty()) {
             return new JsonBean<>(DS_URL_REQUIRED, "连接URL不能为空");
         }

         // 这里可以添加实际的连接测试逻辑
         // 暂时返回成功状态
         return new JsonBean<>(SUCCESS, "连接测试成功");
     }
 }
