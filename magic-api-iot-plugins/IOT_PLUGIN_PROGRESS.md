# IoT Plugin 长期建设进度

> 本文档是 IoT 插件体系的长期进度基线。每次完成代码、目录、配置或测试改造后更新本文档。
> 详细待办仍维护在 [IOT_PLUGIN_TODO.md](IOT_PLUGIN_TODO.md)。

## 当前基线

- 更新时间：2026-08-26
- 当前阶段：etcd 三节点真实业务验证完成，进入 Provider Micrometer 指标建设
- 总体进度：约 80%
- 构建状态：etcd 定向集成测试 6 个 reactor 模块全部成功，测试工程 `clean package` 已通过
- 测试工程：已通过 `9226` 临时实例验证 etcd 配置中心管理 API 和 Actuator 健康链路
- 业务边界：IoT 插件不引入租户、住户等业务概念

## 当前目录规范

```text
magic-api-iot-plugins/
├── platform/   插件平台基础设施
├── core/       网关核心模型、SPI 和核心能力
├── features/   可选功能模块
├── adapters/   协议及外部系统适配器
└── providers/  可替换的基础设施实现
```

新模块必须归入上述目录之一，不直接放在工程根目录。目录代表交付分类，artifactId 保持稳定并用于模块依赖。

## 已完成能力

### 平台基础设施

- [x] `magic-api-plugin-api`：无 Spring 依赖的插件契约
- [x] `IotPlugin` 生命周期接口
- [x] `PluginDescriptor` 插件描述模型
- [x] `PluginContext` 受控插件上下文
- [x] `PluginState`、`PluginHealth` 运行状态模型
- [x] `magic-api-plugin-runtime`：插件描述发现和注册
- [x] `PluginRegistry` 插件注册中心
- [x] `CapabilityRegistry` 能力注册中心
- [x] `iot-plugin.json` 描述文件读取
- [x] 插件 ID、版本、必需依赖和重复 ID 校验
- [x] `magic-api-plugin-spring-boot-starter`：Spring Boot 自动装配边界

### 网关核心

- [x] `magic-api-plugin-iot-core` 保留设备模型和 SPI
- [x] 状态、消息、会话、命令、协议等基础模型
- [x] 设备注册、设备会话、消息总线作为 Provider 扩展点
- [x] core 不再默认创建内存实现
- [x] 协议处理拆分为 Detector、Frame Decoder、Message Decoder、Command Encoder 流水线
- [x] `TransportProvider` 统一连接生命周期、收发和断连边界
- [x] 协议流水线按 `protocolId` 组装，缺失阶段和重复阶段启动失败
- [x] 删除旧 `ProtocolAdapter`、兼容桥和 `ProtocolRegistry`
- [x] `ProtocolIngressRuntime` 将 Transport 帧经协议流水线转换后发布到统一 Message Bus
- [x] Raw 协议适配器已自动装配，当前使用连接级临时设备身份

### 扩展 Provider 契约

- [x] `StorageWriterProvider`：按稳定 Provider ID 路由，支持单条和批量写入
- [x] `RuleActionProvider`：规则通过稳定 Action ID 解析动作，不再内嵌 `Consumer`
- [x] `ProviderHealthIndicator`：无 Spring 依赖的统一 Provider 健康契约
- [x] `ProviderHealthCatalog`：Spring 侧统一收集和排序健康快照
- [x] Raw 协议适配器已迁移到四阶段 SPI
- [x] 删除无实现的 `magic-api-plugin-protocol-sample` 模块

### Provider

- [x] `magic-api-plugin-device-registry-memory`
- [x] `magic-api-plugin-device-session-memory`
- [x] `magic-api-plugin-message-bus-memory`
- [x] Memory Provider 必须通过 `iot.providers.*.type=memory` 显式启用
- [x] `prod` 和 `production` profile 禁止使用 Memory Provider
- [x] 从 core 移除空的 registry、session、message-bus 模块
- [x] `magic-api-plugin-transport-tcp`：Netty TCP 监听、换行分帧、连接管理、收发和资源关闭
- [x] TCP Transport 支持独立 LINE 和 MODBUS_TCP 监听器，按 MBAP length 字段处理 TCP 粘包/拆包
- [x] `magic-api-plugin-transport-mqtt`：内嵌 mica-mqtt broker、Topic 映射、QoS 元数据、注册中心鉴权和上下行
- [x] `magic-api-plugin-protocol-modbus-tcp`：DigitalPetri PDU 编解码、8 类功能码、异常响应和事务关联

### 分布式 Provider

- [x] `NodeRegistry` 统一网关节点注册、心跳、发现、摘除和健康契约
- [x] `magic-api-plugin-iot-cluster` 统一节点生命周期，集群启用但 Provider 缺失时启动失败
- [x] Nacos Provider 使用临时 Naming 实例注册网关节点
- [x] ZooKeeper Provider 使用临时 ZNode 注册网关节点
- [x] etcd Provider 使用 Lease 绑定节点 Key，并在心跳时续租
- [x] Nacos、ZooKeeper、etcd Provider 启动参数校验和组件描述
- [x] Nacos `10.211.55.3:8848` 真实注册、发现、心跳和健康探测通过
- [x] ZooKeeper 嵌入式临时节点集成测试通过
- [x] etcd 三节点真实集群完成 Lease 注册、跨客户端发现、心跳、摘除和关闭撤销验证
- [x] `ConfigurationCenter` 独立 SPI，支持 get、前缀 list、put、watch、revision、CAS 和带 revision 删除
- [x] Nacos 配置 Provider：单 JSON 文档、原生 MD5-CAS、前缀过滤和文档差异 watch
- [x] ZooKeeper 配置 Provider：持久 ZNode、version CAS、CuratorCache watch
- [x] etcd 配置 Provider：前缀 Key、modRevision 事务 CAS、原生 prefix watch
- [x] 配置中心 Provider 类型/Bean 缺失和冲突校验，生产环境禁止静默缺失
- [x] Nacos Provider 内存测试桩验证原生 MD5-CAS 和 watch 差异事件
- [x] ZooKeeper 嵌入式集成测试验证 put/list/watch/CAS/delete
- [x] Nacos `10.211.55.3:8848` 真实配置读写、过期 CAS 拒绝、有效 CAS 和带 revision 删除验证
- [x] etcd 三节点真实集群完成 put/get/list/watch、过期 CAS、有效 CAS 和带 revision 删除验证
- [x] 新增 `ConfigurationRuntime`：启动全量装载、远端 watch 增量同步、本地内存快照和解析器通知
- [x] 新增 `ConfigurationParser` 命名空间解析 SPI，规则引擎支持 `rules/` JSON 配置热替换
- [x] 测试应用新增 `/api/iot/gateway/configuration/memory`，可查看本节点镜像和解析结果
- [x] 5177 新增配置管理页面，按当前激活的 Nacos/ZooKeeper/etcd 展示不同定位信息并支持 CRUD/CAS

- [x] Redis Device Registry Provider 模块和自动配置
- [x] Redis Device Session Provider 模块和节点索引
- [x] Kafka Message Bus Provider 发布和动态消费适配器
- [x] Kafka 手动 ACK、可配置重试、`.DLT` 死信和并发背压
- [x] RocketMQ Message Bus Provider 发布、动态消费、原生重试和 DLQ
- [x] Pulsar Provider 发布、Shared 动态消费、ACK、negative ACK、重投和 DLQ
- [x] Provider type 空值、非法值和生产 Memory 校验
- [x] 缺失核心 Provider 配置时生产环境启动失败
- [x] 已配置 Provider 缺少真实实现 Bean 时启动失败
- [x] 同一核心 SPI 存在多个 Provider Bean 时启动失败
- [x] `magic-api-plugin-testkit` Registry、Session、MessageBus 统一行为契约
- [x] 三个 Memory Provider 通过统一契约测试
- [x] Redis Registry 在 `10.211.55.4:6379` 通过跨实例集成测试
- [x] Redis Session 在 `10.211.55.4:6379` 通过跨实例和节点索引集成测试
- [x] Kafka 在 `10.211.55.4:9092` 通过真实发布、动态消费和手动 ACK 测试
- [x] Kafka 在真实 broker 上通过重试次数和 `.DLT` 死信测试
- [x] Provider 类型按 Registry、Session、MessageBus 分类限制
- [x] Provider 配置规则单元测试
- [ ] 增加 Kafka broker 集成测试
- [x] Pulsar 在 `10.211.55.4:6650` 通过发布、Shared 消费、negative ACK 和重投测试
- [x] RocketMQ 在 `10.211.55.4:9876` 通过发布、消费失败、Broker 重投和成功消费测试
- [x] 增加 Pulsar/RocketMQ 可选外部服务集成测试入口
- [x] 增加可选 Redis/Kafka 外部服务集成测试
- [ ] 增加 Docker 可用时的 Testcontainers 集成测试
- [x] Redis Registry/Session 使用 `PING` 实现真实健康探测
- [x] Kafka/Pulsar 使用配置 Topic 的分区元数据实现真实健康探测
- [x] RocketMQ 使用业务 Topic 和默认 Topic 路由实现 `UP`、`DEGRADED`、`DOWN` 探测
- [x] 新增条件化 `magic-api-plugin-redis-support`，仅在 Redis Registry/Session 启用时创建连接工厂

### 监控和测试工程

- [x] 测试应用接入插件 Starter
- [x] `/api/iot/gateway/status` 返回插件和能力数量
- [x] `/api/iot/gateway/components` 返回插件版本、能力、依赖、状态和健康信息
- [x] 5177 监控页面完成基础展示和间距优化
- [x] `/api/iot/gateway/providers` 返回 Provider 类型、ID、状态、消息、延迟和探测详情
- [x] Actuator 暴露 `iotProviders` 聚合健康项，并采用最差状态作为聚合结果
- [x] 5177 页面展示 Provider 健康率和健康明细表格
- [x] 健康探针支持异步超时、10 秒缓存和并发探测去重
- [x] `/api/iot/gateway/runtime` 返回协议、Transport、连接、流量和错误快照
- [x] `/api/iot/gateway/cluster` 返回本节点、注册中心类型、心跳错误和活跃节点列表
- [x] `/api/iot/gateway/configuration` 提供配置 list/get/put/CAS/delete 测试接口
- [x] 5177 页面展示 TCP 监听、活跃连接、协议 ID、收发流量和发布计数
- [x] 现有运行快照和 Provider 健康表自动展示 MQTT 与 Modbus TCP Transport

## 进行中

- [ ] 将设备模型和 SPI 从 `iot-core` 逐步迁移到 `plugin-api`
- [ ] 为 Provider 健康和插件生命周期接入 Micrometer 指标
- [ ] 为设备注册、会话、Transport、Protocol Provider 增加配置解析器并接入运行时替换

## 下一阶段顺序

1. 增加 Micrometer Provider 延迟、状态和插件生命周期指标。
2. 为 Provider、Transport、Protocol 增加配置解析器并定义热更新边界。
3. 将稳定的模型和 SPI 从 `iot-core` 迁移到 `plugin-api`。
4. 增加 Docker 可用时的 Testcontainers 回归环境。
5. 再进入外部 JAR 加载和插件生命周期管理。

## 长期路线

### API 稳定化

- 插件 API 保持无 Spring、Netty、Kafka、Redis、Nacos 和 Magic API 依赖。
- 通过 API 版本和语义化版本管理兼容性。
- 为描述文件建立 JSON Schema。
- 插件只通过 `PluginContext` 获取受控服务。

### 分布式网关

- Session 路由状态使用 Redis 等共享存储。
- 节点心跳、节点摘除和设备连接归属需要可恢复。
- Message Bus 负责跨节点事件传播，不使用本地内存作为生产总线。
- Device Registry、Shadow 和命令结果需要明确一致性策略。

### 可运维性

- 插件状态可观测：发现、校验、初始化、启动、运行、降级、失败、停止。
- 监控页面展示版本、能力、依赖、配置状态、健康状态和最近错误。
- 插件配置按插件 ID 隔离，敏感值使用密钥引用。
- 所有 Provider 必须提供健康检查和资源关闭行为。

### 动态插件

- 第一阶段只支持启动期发现和启停配置。
- 第二阶段支持外部 JAR 发现和独立 ClassLoader。
- 第三阶段再支持协议插件热加载、连接排空、升级和回滚。
- 有状态插件在卸载前必须完成命令排空和设备连接迁移。

## 每次更新规则

每次完成一项插件能力后，必须更新以下内容：

- 更新“当前基线”的更新时间和当前阶段。
- 将已完成事项从“进行中”移动到“已完成能力”。
- 记录新增模块、接口、配置和兼容性变化。
- 记录执行过的构建、单元测试和集成测试命令及结果。
- 记录未完成事项、已知风险和下一步。
- 如果目录、artifactId、配置键或 API 发生变化，必须同步更新 README 和 TODO。

## 更新记录

### 2026-08-26

- 5177 监控页新增“配置管理”视图，自动识别当前激活配置中心。
- Nacos 展示 serverAddr、namespace、dataId、group；ZooKeeper 展示 connectString、rootPath；etcd 展示 endpoints、rootPrefix。
- 配置编辑使用 revision CAS，新增使用 put，删除使用 revision 保护删除；页面同时展示本节点内存镜像、解析器和解析错误。
- 前端 `npm run build` 与测试应用 `mvn package -DskipTests` 通过。

- 新增节点本地 `ConfigurationRuntime`，三种配置中心均通过现有 Provider 的 watch 同步到内存，不做无事务的 Nacos/ZooKeeper/etcd 三写。
- 配置管理 API 改为读写运行时镜像；新增 `/api/iot/gateway/configuration/memory` 查看本地同步数据和解析快照。
- 新增 `ConfigurationParser` SPI 与规则引擎 JSON 解析器，规则配置变更后自动替换内存规则集合。
- 测试覆盖两个运行时订阅者对共享配置中心外部变更的同步收敛。

- etcd 三节点集群 `10.211.55.4:2379/22379/32379` 健康检查、成员一致性和 Leader 状态验证通过。
- 新增 etcd 节点注册中心外部集成测试，验证 Lease 注册、跨客户端发现、心跳更新、显式摘除和关闭时 Lease 撤销。
- 新增 etcd 配置中心外部集成测试，验证三端点复制、前缀 watch、过期 CAS 拒绝、有效 CAS 和 revision 保护删除。
- 测试应用修正 jetcd 与 RuoYi/RocketMQ 依赖管理造成的 gRPC 版本冲突，统一 jetcd gRPC 组件为 `1.70.0`。
- `9226` 真实应用实例中 `configuration-center:etcd`、Provider API 和 Actuator 均为 `UP`；HTTP 管理接口完整业务链路通过，临时键已删除。
- etcd 定向集成命令通过：节点注册 2 项、配置中心 1 项测试成功，相关 6 个 reactor 模块全部成功。

- 为避免与 Magic API 自带集群插件冲突，将模块由 `magic-api-plugin-cluster` 重命名为 `magic-api-plugin-iot-cluster`，运行时插件 ID 同步改为 `iot-gateway-cluster`；`iot.cluster.*` 配置键和 Java 包保持兼容。
- 新增独立 `magic-api-plugin-configuration-center` SPI，统一 get、前缀 list、put、watch、opaque revision、CAS 和带 revision 删除。
- 新增 Nacos、ZooKeeper、etcd 三种配置中心 Provider，可通过 `iot.providers.configuration-center.type` 独立于节点注册中心单选切换。
- Nacos 使用单 JSON 文档和原生 MD5-CAS；ZooKeeper 使用 ZNode version；etcd 使用 modRevision 事务。
- 配置中心纳入核心 Provider 配置/Bean 缺失与冲突校验、健康探测、Actuator、Provider API 和 5177 组件发现。
- 测试应用新增配置 list/get/put/CAS/delete 管理接口；Nacos 契约测试与 ZooKeeper 嵌入式集成测试通过。
- Nacos `10.211.55.3:8848` 真实创建、前缀查询、过期 CAS 拒绝、有效 CAS 和删除通过，临时测试键已清理。
- 测试应用运行于 `9218`，配置中心 Provider 与 Actuator 均为 `UP`；5177 数据源可识别 Nacos 为启用、ZooKeeper/etcd 为未启用。
- 40 模块 `mvn clean test`、`mvn install -DskipTests` 和测试应用 `mvn package -DskipTests` 通过；etcd 真实验证状态单独记录，不以编译通过替代。

- 新增 `magic-api-plugin-iot-cluster`，统一网关节点启动注册、周期心跳、节点发现和优雅摘除。
- 新增 Nacos、ZooKeeper、etcd 三种 `NodeRegistry` Provider，可通过 `iot.providers.node-registry.type` 单选切换。
- Nacos 使用临时 Naming 实例，ZooKeeper 使用临时 ZNode，etcd 使用 Lease 绑定节点 Key。
- 集群启用但节点注册 Provider 缺失时直接启动失败；三种 Provider 均增加地址、路径、超时和凭证参数校验。
- 测试应用新增 `/api/iot/gateway/cluster`，Provider API、Actuator 和 5177 组件发现链路可感知三种注册中心。
- `ruoyi-iot-plugin-test-9218` 已在 `10.211.55.3:8848` 的 `iot-gateway-nodes` 服务完成真实注册和心跳，健康状态为 `UP`。
- 36 模块 `mvn clean test`、`mvn install -DskipTests` 及测试应用 `mvn package -DskipTests` 通过。
- 本阶段只实现节点注册中心；配置中心的 watch、revision、CAS 等能力留在独立 SPI 的下一阶段。

- 新增 `magic-api-plugin-transport-mqtt-client`，网关可作为 MQTT 3.1.1 客户端连接 EMQX 等外部 Broker。
- 支持 EMQX `$share` 共享订阅、节点唯一 Client ID、自动重连和恢复订阅、Topic 设备身份映射与 Registry 启用状态校验。
- 外部 MQTT 上行消息直接转换为统一 `DeviceMessage`；下行命令通过 `devices/{productId}/{deviceId}/commands` 回发。
- 新 Provider 接入统一 Transport 快照、Provider 健康探测、Actuator 和 5177 监控发现链路，测试工程默认关闭并提供完整环境变量配置。
- 当前设备会话为收到消息后建立的虚拟路由会话；EMQX 权威上下线事件适配仍待实现。
- 外部 MQTT 双向集成测试通过；32 模块 `mvn test` 和测试应用 `mvn package` 通过。
- 测试应用已真实连接 `10.211.55.4:1883`，`/api/iot/gateway/runtime`、Provider API 和 Actuator 均报告 `mqtt-client` 为 `UP`，临时 9225 实例验证后已停止。
- 测试应用将外部 MQTT Client 默认设为启用，使 5177 Provider 健康表同时展示内置 `mqtt` Broker 与外部 `mqtt-client`；仍可通过 `IOT_MQTT_CLIENT_ENABLED=false` 显式关闭。
- 修正 5177 对 URI 类型 Transport 目标地址重复追加端口的问题，并让 TCP 摘要按 `transportId=tcp` 选择监听实例；页面验证 `mqtt-client` 为 `UP`、Provider 健康为 `7/7`。

- 新增 `magic-api-plugin-transport-mqtt`，使用 mica-mqtt 2.6.6 提供嵌入式 MQTT 3/5 broker 能力。
- MQTT Topic 按 `devices/{productId}/{deviceId}/...` 映射属性、事件、命令回复、心跳和固件消息，保留 QoS、retained、duplicate 元数据。
- MQTT 可通过 Device Registry 校验 `productId/deviceId` 和设备密钥；测试工程匿名模式只用于本地开发。
- `prod` 和 `production` profile 禁止 MQTT 匿名启动，避免生产配置遗漏后开放未鉴权 broker。
- 新增 `magic-api-plugin-protocol-modbus-tcp`，使用 DigitalPetri Modbus 2.1.6 编解码 PDU。
- Modbus TCP 支持 `0x01/02/03/04/05/06/0F/10`，记录 MBAP transactionId、unitId、异常码和 commandId 关联。
- TCP Transport 新增 MBAP length-field 分帧，拆分写入 12 字节请求仍只产生一个完整协议帧。
- 临时端口端到端验证：MQTT `18884` 和 Modbus TCP `15022` 各接收 1 条消息并发布到统一 Memory Message Bus，三个 Transport 健康状态均为 `UP`。
- MQTT Paho 真实客户端测试、Modbus 编解码测试和 TCP Socket 二进制分帧测试通过。
- 插件 reactor 31 模块全量 `mvn test` 通过，测试工程 `mvn package` 通过。

- 多协议接入进入可运行阶段：新增 Netty Raw-over-TCP 链路，默认监听 `19000`，使用换行符分帧。
- 新增 `ProtocolIngressRuntime`，完成 TCP -> 协议检测 -> 拆包/解码 -> Kafka Message Bus 的入口链路。
- `nc` 向临时端口 `19001` 发送 `temperature=23.5` 验证成功：1 帧、16 字节、1 条统一消息发布、0 错误。
- 新增 TCP Socket 测试和协议入口发布测试，监控 API/5177 增加协议与 Transport 运行快照。
- 当前 TCP 身份为 `tcp-raw/<channelId>` 临时身份，不代表设备已鉴权；下一步必须实现鉴权握手。
- `ruoyi-iot-plugin-test` 补齐 Redis、Kafka、Pulsar、RocketMQ 已验证环境地址，默认启用 Redis Registry、Redis Session 和 Kafka Message Bus。
- 消息总线仍遵守单 Provider 约束，可通过 `IOT_MESSAGE_BUS_PROVIDER=kafka|pulsar|rocketmq|memory` 切换；其他地址配置只作为候选环境，不会同时装配多个 MessageBus Bean。
- 补齐 Redis Registry、Redis Session、Kafka Message Bus 的插件描述；组件接口和监控页可区分启用 Provider 的健康状态与未启用 Provider 的 `INACTIVE` 状态。
- RocketMQ Spring 自动配置收敛到 `message-bus=rocketmq` 条件内，未选中 RocketMQ 时不再提前创建 Producer。
- Redis、Kafka、Pulsar、RocketMQ Provider 已实现真实只读健康探测。
- 新增异步超时、TTL 缓存和并发去重的 `ProbeProviderHealthIndicator`。
- 新增 `ProviderHealthCatalog` 和 Actuator `iotProviders` 聚合健康项。
- 测试应用新增 `/api/iot/gateway/providers`，5177 页面新增 Provider 健康率和明细表格。
- 新增条件化 `magic-api-plugin-redis-support`，Memory 模式不再创建默认 localhost Redis 健康项。
- Redis `10.211.55.4:6379`、Kafka `10.211.55.4:9092`、Pulsar `10.211.55.4:6650` 探测结果为 `UP`。
- RocketMQ `10.211.55.4:9876` 可达，但业务 Topic 尚无路由，因此探测结果为 `DEGRADED`。
- 插件 reactor 28 模块 `mvn test`、`mvn install`，测试应用 `mvn package` 和前端 `npm run build` 均通过。
- Memory 和 Redis + Kafka 两种启动模式已在 9220 验证；Actuator 均为 `UP`，Memory 模式无 Redis 健康项。
- 5177 页面已通过真实 API 数据做桌面浏览器验收；移动视口覆盖未作用于持久浏览器窗口，响应式截图仍待后续补充。
- 第 6 步采用直接收敛策略，不保留旧协议、存储和规则动作兼容层。
- 新增协议四阶段流水线和 `TransportProvider`，增加完整性、优先级和缺失阶段测试。
- 删除 `ProtocolAdapter`、`LegacyProtocolAdapterBridge`、`ProtocolRegistry`。
- Raw 协议适配器直接实现 Detector、Frame Decoder、Message Decoder、Command Encoder。
- 删除空的 `magic-api-plugin-protocol-sample` reactor 模块，模块总数从 28 调整为 27。
- 删除 `StorageWriter`，新增 `StorageWriterProvider` 及批量路由和重复 ID 校验。
- 删除内嵌 `Consumer` 的 `DeviceRule`，新增 `RuleDefinition` 和 `RuleActionProvider`。
- 新增 `ProviderHealthIndicator` 和 `ProviderHealthCatalog` 健康发现边界。
- Storage、Rule 和 Protocol 新增测试，全量 `mvn test` 通过。

### 2026-08-25

- 建立 platform、core、features、adapters、providers 五类目录规范。
- 建立插件 API、Runtime 和 Spring Boot Starter 三层平台结构。
- 增加插件描述、生命周期、健康状态、插件注册和能力注册模型。
- 将 registry、session、message-bus 的 Memory 实现拆到 `providers/`。
- 从 `core/` 移除不再承担职责的三个空模块。
- 增加显式 Memory Provider 配置和生产环境禁用校验。
- 插件 reactor 构建通过。
- `ruoyi-iot-plugin-test` 构建通过。

- 新增 Redis Device Registry Provider，使用 Redis Hash/Value 存储设备和凭证摘要。
- 新增 Redis Device Session Provider，维护 device、session、gateway node 三类索引。
- 新增 Kafka Message Bus Provider 发布适配器。
- 新增 Pulsar、RocketMQ Provider 模块边界，客户端消费适配尚未实现。
- 增加 Provider 类型合法性、生产环境缺失配置和 Memory 禁用校验。
- 新增 `magic-api-plugin-testkit` 初版 Provider/插件契约断言工具。
- 再次验证插件 reactor 和测试工程构建通过。
- 增加容器完成初始化后的 Provider Bean 数量校验，禁止缺失和冲突实现。
- 增加 Registry、Session、MessageBus 三类统一 Provider 行为契约。
- Memory Registry、Session、MessageBus Provider 契约测试全部通过。
- 完整 reactor `mvn test` 通过，共 28 个模块构建成功。
- Kafka Provider 增加按 subscriber 独立 consumer group 的动态消费容器。
- Kafka Provider 增加手动 ACK、重试延迟、DLT 和 `max-pending` 背压限制。
- RocketMQ Provider 增加基于 `RocketMQTemplate` 的真实发布实现和自动配置。
- RocketMQ Provider 增加 `DefaultMQPushConsumer` 动态订阅、原生重试和 `%DLQ%group`。
- Pulsar Provider 增加 Producer、Shared Consumer、ACK、negative ACK、重投延迟和 DeadLetterPolicy。
- Pulsar/RocketMQ 客户端代码已通过 clean test，但当前没有对应 broker，尚未做真实服务验证。
- RocketMQ Broker 广播地址已修正，`9876/10909/10911/10912` 均可访问。
- RocketMQ 随机测试 Topic 先预热创建，再验证首次失败、原生重投和第二次成功消费。
- Pulsar `10.211.55.4:6650` 已开放并通过真实 Provider 集成测试。
- Pulsar 测试验证第一次消费失败后 negative ACK，随后重投并成功 ACK。
- Pulsar 宿主机 `8880` 当前无服务监听；消息协议测试不依赖管理 HTTP 端口。
- 当前机器 Docker 不可用，Redis/Kafka Testcontainers 尚未实际运行。
- Redis 集成测试已连接 `10.211.55.4:6379` 并通过，测试结束后清理精确测试键。
- Kafka 集成测试已连接 `10.211.55.4:9092` 并通过，使用随机 topic 和 consumer group。
- Kafka 失败消费验证了 3 次实际调用后进入 `.DLT`。
- Provider 配置改为按契约限制类型，拒绝 `device-registry=kafka` 等错误组合。
- 外部集成测试默认关闭，通过 `-Diot.integration.enabled=true` 显式启用。
