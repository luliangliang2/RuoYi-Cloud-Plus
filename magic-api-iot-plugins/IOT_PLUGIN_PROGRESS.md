# IoT Plugin 长期建设进度

> 本文档是 IoT 插件体系的长期进度基线。每次完成代码、目录、配置或测试改造后更新本文档。
> 详细待办仍维护在 [IOT_PLUGIN_TODO.md](IOT_PLUGIN_TODO.md)。

## 当前基线

- 更新时间：2026-08-25
- 当前阶段：第 5 步 Provider 拆分与实现已完成，准备进入第 6 步扩展点标准化
- 总体进度：约 50%
- 构建状态：插件 reactor `mvn clean install` 已通过（本次更新）
- 测试工程：`ruoyi-iot-plugin-test` `mvn package` 已通过（本次更新）
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

### Provider

- [x] `magic-api-plugin-device-registry-memory`
- [x] `magic-api-plugin-device-session-memory`
- [x] `magic-api-plugin-message-bus-memory`
- [x] Memory Provider 必须通过 `iot.providers.*.type=memory` 显式启用
- [x] `prod` 和 `production` profile 禁止使用 Memory Provider
- [x] 从 core 移除空的 registry、session、message-bus 模块

### 分布式 Provider（进行中）

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
- [ ] 使用可用 Pulsar broker 执行真实集成测试
- [ ] 使用可用 RocketMQ broker 执行真实集成测试
- [x] 增加 Pulsar/RocketMQ 可选外部服务集成测试入口
- [x] 增加可选 Redis/Kafka 外部服务集成测试
- [ ] 增加 Docker 可用时的 Testcontainers 集成测试

### 监控和测试工程

- [x] 测试应用接入插件 Starter
- [x] `/api/iot/gateway/status` 返回插件和能力数量
- [x] `/api/iot/gateway/components` 返回插件版本、能力、依赖、状态和健康信息
- [x] 5177 监控页面完成基础展示和间距优化

## 进行中

- [ ] 将设备模型和 SPI 从 `iot-core` 逐步迁移到 `plugin-api`
- [ ] 标准化 Transport、Protocol、Codec、Storage 和 Rule Action 扩展点

## 下一阶段顺序

1. 标准化 Transport、Protocol Detector、Frame Decoder、Message Decoder 和 Command Encoder。
2. 标准化 Storage Writer、Rule Action 和 Provider Health 扩展点。
3. 将稳定的模型和 SPI 从 `iot-core` 迁移到 `plugin-api`。
4. 增加 Actuator 健康检查和 Micrometer 指标。
5. 增加 Docker 可用时的 Testcontainers 回归环境。
6. 再进入外部 JAR 加载和插件生命周期管理。

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
- RocketMQ NameServer `10.211.55.4:9876` 可达，但 Broker 广播 `127.0.0.1:10911`，远程客户端无法回连。
- Pulsar 容器未发布宿主机 `6650/8080`，从开发机连接被拒绝。
- 当前机器 Docker 不可用，Redis/Kafka Testcontainers 尚未实际运行。
- Redis 集成测试已连接 `10.211.55.4:6379` 并通过，测试结束后清理精确测试键。
- Kafka 集成测试已连接 `10.211.55.4:9092` 并通过，使用随机 topic 和 consumer group。
- Kafka 失败消费验证了 3 次实际调用后进入 `.DLT`。
- Provider 配置改为按契约限制类型，拒绝 `device-registry=kafka` 等错误组合。
- 外部集成测试默认关闭，通过 `-Diot.integration.enabled=true` 显式启用。
