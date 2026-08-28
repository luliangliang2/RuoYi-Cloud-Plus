# ruoyi-common-netty-websocket

高并发 Netty WebSocket 公共模块，和原有 `ruoyi-common-websocket` 隔离。它只提供底层连接、鉴权、会话索引、跨节点推送能力，业务逻辑由接入方动态注册。

## 启用配置

```yaml
netty-websocket:
  enabled: true
  port: 19090
  path-patterns:
    - /ws/**
  auth-enabled: true
  token-name: Authorization
  allow-query-token: true
  allow-protocol-token: true
  redis-topic: global:netty-websocket
```

## 连接方式

支持通过请求头、query 参数或 `Sec-WebSocket-Protocol` 传若依 token。

```js
const ws = new WebSocket(
  'ws://127.0.0.1:19090/ws/location?alias=device-001&bizType=vehicle&bizId=VIN001',
  ['Bearer', token],
);
```

常用连接参数：

- `alias`：连接别名，例如设备号、车辆 VIN。
- `bizType`：业务类型，例如 `vehicle`、`camera`。
- `bizId`：业务 ID，例如车辆 VIN。
- `tenantId`：关闭鉴权时可由 query 传入；开启鉴权时优先使用 token 内租户。

## Magic API 使用

`ruoyi-magic-api` 引入本模块后，会注册 `ws` 脚本模块。

```js
import ws;

ws.route('/ws/location')
  .onConnect((session) => {
    println('connect: ' + session.sessionId + ', alias=' + session.alias);
  })
  .onMessage((session, message) => {
    println('message: ' + message);
    ws.pushByPathAndAlias('/ws/location', session.alias, JSON.stringify({
      type: 'notice',
      data: 'hello'
    }));
  })
  .onDisconnect((session) => {
    println('disconnect: ' + session.sessionId);
  });

return ws.stats();
```

## 推送能力

- `pushBySession(sessionId, message)`：按单连接推送。
- `pushByPath(path, message)`：按路径推送。
- `pushByTenant(tenantId, message)`：按租户推送。
- `pushByUser(userId, message)`：按用户推送。
- `pushByAlias(alias, message)`：按别名推送。
- `pushByBiz(bizType, bizId, message)`：按业务标识推送。
- `pushByPathAndTenant(path, tenantId, message)`：按路径和租户推送。
- `pushByPathAndUser(path, userId, message)`：按路径和用户推送。
- `pushByPathAndAlias(path, alias, message)`：按路径和别名推送，适合一个端口多个业务路径时避免串发。
- `pushByPathAndBiz(path, bizType, bizId, message)`：按路径和业务标识推送。
- `broadcast(message)`：本节点广播。

跨节点推送可使用 `NettyWebSocketMessagePublisher.publish(NettyWebSocketMessageDto)` 发布到 Redis 主题，由各服务节点本地分发。
