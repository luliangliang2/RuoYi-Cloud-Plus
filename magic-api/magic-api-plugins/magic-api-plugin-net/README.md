# magic-api-plugin-net 使用说明

`magic-api-plugin-net` 为 Magic API 提供 TCP、UDP、WebSocket 网络能力。当前支持：

- TCP 客户端、TCP 服务端
- UDP 客户端、UDP 服务端
- 单路径 WebSocket 服务端、WebSocket 客户端
- WebSocket Hub：一个端口注册多个 path 执行器
- WebSocket 握手阶段集成 RuoYi/Sa-Token 鉴权
- 基于 session 参数的通用规则推送

## 1. WebSocket Hub 推荐用法

WebSocket Hub 适合一个端口承载多个业务通道。

```js
var hub = net.websocketHub(19001, true);

hub.route('/ws/location')
  .onConnect((ctx) => {
    println('location connected: ' + ctx.alias);
  })
  .onMessage((ctx, msg) => {
    println('location message: ' + msg);
  })
  .onDisconnect((ctx) => {
    println('location disconnected: ' + ctx.alias);
  });

hub.route('/ws/alarm')
  .onMessage((ctx, msg) => {
    println('alarm message: ' + msg);
  });
```

客户端连接：

```text
ws://127.0.0.1:19001/ws/location?Authorization=Bearer <token>&clientid=<clientid>&alias=device-001&bizType=vehicle&bizId=1001
```

说明：

- `Authorization`：RuoYi 登录 token。
- `clientid`：RuoYi 客户端 ID。
- `alias`：连接别名。未传时默认使用 RuoYi `userId`，再没有则生成 `client_xxx`。
- 其他 query 参数都会进入 session，可用于后续规则推送。

## 2. Hub 上下文

`onConnect`、`onMessage`、`onDisconnect` 中的 `ctx` 包含：

```js
ctx.path        // 当前 WebSocket path
ctx.clientId    // socket 原始连接 ID
ctx.alias       // 会话别名
ctx.tenantId    // RuoYi 租户 ID
ctx.userId      // RuoYi 用户 ID
ctx.username    // RuoYi 用户名
ctx.clientid    // RuoYi clientid
ctx.params      // 连接 query 参数 Map
ctx.headers     // 握手 header Map
ctx.channel     // Netty Channel
```

获取自定义参数：

```js
var bizId = ctx.params.bizId;
var groupId = ctx.params.groupId;
```

## 3. 通用规则推送

插件不绑定具体业务。连接时携带什么参数，推送时就可以用同名 key 匹配。

按 path + 参数推送：

```js
hub.push('/ws/location', {
  bizType: 'vehicle',
  bizId: '1001'
}, {
  type: 'location',
  data: {
    lng: 116.39,
    lat: 39.90
  }
});
```

按租户 + 参数推送：

```js
hub.push('/ws/alarm', {
  tenantId: '000000',
  groupId: 'A'
}, {
  type: 'alarm',
  data: {
    level: 'high'
  }
});
```

按别名推送：

```js
hub.pushByAlias('/ws/location', 'device-001', {
  type: 'notice',
  data: 'hello'
});
```

按单个参数推送：

```js
hub.pushByParam('/ws/location', 'bizId', '1001', 'hello');
```

按租户 + 单个参数推送：

```js
hub.pushByParam('/ws/location', '000000', 'bizId', '1001', 'hello');
```

广播某个 path：

```js
hub.broadcast('/ws/location', {
  type: 'broadcast',
  data: {}
});
```

租户内广播：

```js
hub.broadcast('/ws/location', '000000', {
  type: 'tenant_broadcast',
  data: {}
});
```

## 4. 自定义注册别名

默认注册规则：

1. 优先使用 query 参数 `alias`
2. 其次使用 RuoYi token 中的 `userId`
3. 最后生成 `client_xxx`

可以通过 `onRegister` 自定义：

```js
hub.route('/ws/device')
  .onRegister((ctx, data) => {
    return ctx.params.deviceCode;
  })
  .onConnect((ctx) => {
    println('device connected: ' + ctx.alias);
  });
```

`onRegister` 返回 `null` 或空字符串时会拒绝连接。

## 5. RuoYi 鉴权

创建 Hub 时第二个参数控制是否启用 RuoYi 鉴权：

```js
var hub = net.websocketHub(19001, true);
```

浏览器原生 WebSocket 无法直接设置 `Authorization` header，建议放在 query 中：

```text
ws://127.0.0.1:19001/ws/device?Authorization=Bearer <token>&clientid=<clientid>
```

服务端会读取：

- header：`Authorization`、`authorization`、`token`
- query：`Authorization`、`authorization`、`token`
- clientid：header/query 中的 `clientid`、`ClientID`、`clientId`

鉴权失败返回 `401`，path 不存在返回 `404`。

## 6. 单路径 WebSocket

如果只需要一个 path，可以使用单路径 WebSocket Server：

```js
var server = net.createWebSocketServer('wsServer', 19002, '/websocket', true);

net.wsServer.onConnect((clientId, alias) => {
  println('connected: ' + clientId + ', alias=' + alias);
});

net.wsServer.onMessage((clientId, msg) => {
  println('message: ' + msg);
});

net.wsServer.sendTo('1', 'hello');
net.wsServer.broadcast('broadcast message');
```

客户端：

```text
ws://127.0.0.1:19002/websocket?Authorization=Bearer <token>&clientid=<clientid>
```

## 7. WebSocket 客户端

```js
var client = net.createWebSocketClient('wsClient', 'ws://127.0.0.1:19002/websocket', {
  Authorization: 'Bearer <token>',
  clientid: '<clientid>'
});

net.onConnected(() => {
  net.send('wsClient', 'hello');
});

net.onMessage((clientId, msg) => {
  println('server message: ' + msg);
});
```

## 8. TCP 服务端

```js
var server = net.createServer('tcpServer', 'tcp', 18001);

net.tcpServer.onConnect((clientId, alias) => {
  println('tcp connected: ' + clientId);
});

net.tcpServer.onMessage((clientId, msg) => {
  println('tcp message: ' + msg);
});

net.tcpServer.broadcast('hello tcp clients');
```

## 9. TCP 客户端

```js
var client = net.createClient('tcpClient', 'tcp', '127.0.0.1', 18001);

net.send('tcpClient', 'hello server');
```

## 10. UDP

UDP 服务端：

```js
var server = net.createServer('udpServer', 'udp', 18002);

net.onMessage((clientId, msg) => {
  println('udp message: ' + msg);
});
```

UDP 客户端：

```js
var client = net.udpClient();
net.sendUdp(client, '127.0.0.1', 18002, 'hello udp');
```

## 11. 数据源配置

控制台支持以下类型：

```text
tcp-client
tcp-server
udp-client
udp-server
websocket-client
websocket-server
websocket-hub
```

`websocket-server` 示例：

```json
{
  "path": "/websocket",
  "ruoyiAuth": true
}
```

`websocket-hub` 示例：

```json
{
  "ruoyiAuth": true
}
```

注意：`websocket-hub` 的 path 执行器通常在 Magic 脚本中通过 `hub.route(path)` 注册。

## 12. 重复创建与端口重启

重复创建相同配置或复用同一服务端端口时，插件会先同步关闭旧端口，再启动新服务，避免端口占用。

适用范围：

- `tcp-server`
- `udp-server`
- `websocket-server`
- `websocket-hub`

脚本中重复创建同名 `websocket-server` 或 `websocket-hub` 时，也会先关闭旧实例再重启。

## 13. 常见问题

### WebSocket 一直 Connecting

检查：

- 客户端 path 是否已通过 `hub.route(path)` 注册。
- token 是否有效。
- `clientid` 是否与 token 匹配。
- URL 中 query token 是否正确编码。

### 返回 401

表示 RuoYi 鉴权失败。确认连接中包含：

```text
Authorization=Bearer <token>
clientid=<clientid>
```

### 返回 404

表示 path 未注册。确认 Magic 脚本中已执行：

```js
hub.route('/your/path')
```

### 推送不到连接

确认连接参数和推送规则 key 完全一致：

```text
连接：?bizId=1001&groupId=A
推送：{ bizId: '1001', groupId: 'A' }
```
