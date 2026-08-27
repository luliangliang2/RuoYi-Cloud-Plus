import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Alert, Badge, Button, Card, Col, ConfigProvider, Descriptions, Empty, Input, Layout, Popconfirm, Progress, Row, Space, Statistic, Table, Tag, Tooltip, Typography } from 'antd';
import { ApiOutlined, CheckCircleOutlined, ClusterOutlined, CloudServerOutlined, DashboardOutlined, DatabaseOutlined, DeleteOutlined, EditOutlined, FileTextOutlined, HeartOutlined, PlusOutlined, ReloadOutlined, SaveOutlined, SettingOutlined, ThunderboltOutlined } from '@ant-design/icons';
import './style.css';

const { Header, Content, Sider } = Layout;
const { Text, Title } = Typography;

const fallback = {
  status: 'UNKNOWN',
  components: { deviceRegistry: 'Unknown', sessionRepository: 'Unknown', messageBus: 'Unknown' }
};

async function getJson(url) {
  const response = await fetch(url);
  if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
  return response.json();
}

function transportTarget(snapshot) {
  if (!snapshot) return '';
  return snapshot.bindAddress?.includes('://')
    ? snapshot.bindAddress
    : `${snapshot.bindAddress}:${snapshot.port}`;
}

function shortTypeName(type) {
  if (!type) return '-';
  const normalized = type.split('$')[0];
  return normalized.slice(normalized.lastIndexOf('.') + 1);
}

function App() {
  const [status, setStatus] = useState(fallback);
  const [components, setComponents] = useState([]);
  const [providers, setProviders] = useState([]);
  const [runtime, setRuntime] = useState({ protocol: { protocolIds: [] }, transports: [] });
  const [view, setView] = useState('dashboard');
  const [spi, setSpi] = useState({ services: [], handshakeProviders: [], authenticators: [] });
  const [external, setExternal] = useState({ enabled: false, plugins: [], errors: {} });
  const [configuration, setConfiguration] = useState([]);
  const [configurationMeta, setConfigurationMeta] = useState({ provider: 'UNKNOWN', details: {} });
  const [memory, setMemory] = useState({ values: [], parsed: {}, parserErrors: {} });
  const [editingKey, setEditingKey] = useState(null);
  const [editingValue, setEditingValue] = useState('');
  const [newKey, setNewKey] = useState('');
  const [newValue, setNewValue] = useState('');
  const [configurationError, setConfigurationError] = useState('');
  const [configurationNotice, setConfigurationNotice] = useState('');
  const [error, setError] = useState('');
  const [updatedAt, setUpdatedAt] = useState();

  const load = useCallback(async () => {
    try {
      const [nextStatus, nextComponents, nextProviders, nextRuntime] = await Promise.all([
        getJson('/api/iot/gateway/status'),
        getJson('/api/iot/gateway/components'),
        getJson('/api/iot/gateway/providers'),
        getJson('/api/iot/gateway/runtime')
      ]);
      setStatus(nextStatus);
      setComponents(nextComponents.components || []);
      setProviders(nextProviders.providers || []);
      setRuntime(nextRuntime);
      setError('');
      setUpdatedAt(new Date());
    } catch (err) {
      setError(err.message || 'Gateway API unavailable');
    }
  }, []);

  useEffect(() => { load(); const timer = setInterval(load, 10000); return () => clearInterval(timer); }, [load]);

  const loadConfiguration = useCallback(async () => {
    try {
      const [list, meta, nextMemory] = await Promise.all([
        getJson('/api/iot/gateway/configuration'),
        getJson('/api/iot/gateway/configuration/meta'),
        getJson('/api/iot/gateway/configuration/memory')
      ]);
      setConfiguration(list.values || []);
      setConfigurationMeta(meta);
      setMemory(nextMemory);
      setConfigurationError('');
    } catch (err) {
      setConfigurationError(err.message || 'Configuration API unavailable');
    }
  }, []);

  const loadSpi = useCallback(async () => {
    try {
      const [nextSpi, nextExternal] = await Promise.all([
        getJson('/api/iot/gateway/spi'),
        getJson('/api/iot/gateway/plugins/external')
      ]);
      setSpi(nextSpi);
      setExternal(nextExternal);
    } catch (err) { setError(err.message || 'SPI API unavailable'); }
  }, []);

  useEffect(() => { if (view === 'configuration') loadConfiguration(); }, [view, loadConfiguration]);
  useEffect(() => { if (view === 'plugins') loadSpi(); }, [view, loadSpi]);

  const saveConfiguration = async (key, value, revision) => {
    try {
      const response = revision
        ? await fetch('/api/iot/gateway/configuration/cas', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ key, value, expectedRevision: revision }) })
        : await fetch('/api/iot/gateway/configuration', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ key, value }) });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      const result = await response.json();
      if (revision && !result.applied) throw new Error('配置已被其他节点修改，请刷新后重试');
      setEditingKey(null);
      setConfigurationNotice(`已保存 ${key}，其他网关节点将通过 watch 同步`);
      await loadConfiguration();
    } catch (err) { setConfigurationError(err.message || '保存配置失败'); }
  };

  const deleteConfiguration = async item => {
    try {
      const response = await fetch(`/api/iot/gateway/configuration?key=${encodeURIComponent(item.key)}&expectedRevision=${encodeURIComponent(item.revision)}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      const result = await response.json();
      if (!result.applied) throw new Error('配置已被其他节点修改，请刷新后重试');
      setConfigurationNotice(`已删除 ${item.key}`);
      await loadConfiguration();
    } catch (err) { setConfigurationError(err.message || '删除配置失败'); }
  };

  const activeCount = components.filter(item => item.status === 'UP').length;
  const healthyProviderCount = providers.filter(item => item.health?.status === 'UP').length;
  const groups = useMemo(() => new Set(components.map(item => item.module)).size, [components]);
  const transport = runtime.transports?.find(item => item.transportId === 'tcp') || runtime.transports?.[0];
  const columns = [
    { title: '组件', dataIndex: 'name', key: 'name', render: value => <Text strong>{value}</Text> },
    { title: '模块', dataIndex: 'module', key: 'module', render: value => <Tag color="blue">{value}</Tag> },
    { title: '实现', dataIndex: 'implementation', key: 'implementation', ellipsis: true },
    { title: '状态', dataIndex: 'status', key: 'status', render: value => {
      const badge = value === 'UP' ? 'success' : value === 'DEGRADED' ? 'warning' : value === 'DOWN' ? 'error' : value === 'REGISTERED' ? 'processing' : 'default';
      return <Badge status={badge} text={value} />;
    } }
  ];
  const healthBadge = value => {
    const status = value === 'UP' ? 'success' : value === 'DEGRADED' ? 'warning' : value === 'UNKNOWN' ? 'default' : 'error';
    return <Badge status={status} text={value || 'UNKNOWN'} />;
  };
  const providerColumns = [
    { title: '职责', dataIndex: 'providerType', key: 'providerType', render: value => <Tag color="geekblue">{value}</Tag> },
    { title: 'Provider', dataIndex: 'providerId', key: 'providerId', render: value => <Text strong>{value}</Text> },
    { title: '健康状态', key: 'health', render: (_, item) => healthBadge(item.health?.status) },
    { title: '延迟', key: 'latency', render: (_, item) => item.health?.details?.latencyMs == null ? '-' : `${item.health.details.latencyMs} ms` },
    { title: '探测结果', key: 'message', render: (_, item) => <Text type={item.health?.status === 'DOWN' ? 'danger' : 'secondary'}>{item.health?.message || 'Local provider ready'}</Text> },
    { title: '目标', key: 'target', ellipsis: true, render: (_, item) => {
      const details = item.health?.details || {};
      return details.topic || details.service || details.dataId || details.connectString || details.endpoints?.join(', ')
        || details.rootPath || details.rootPrefix
        || details.nameServer || (details.snapshot ? transportTarget(details.snapshot) : details.command) || 'local';
    } }
  ];

  const configColumns = [
    { title: '配置键', dataIndex: 'key', key: 'key', width: 280, render: value => <Text strong>{value}</Text> },
    { title: '值', dataIndex: 'value', key: 'value', render: (value, item) => editingKey === item.key
      ? <Input.TextArea autoSize={{ minRows: 1, maxRows: 5 }} value={editingValue} onChange={event => setEditingValue(event.target.value)} />
      : <Text className="config-value">{value}</Text> },
    { title: 'Revision', dataIndex: 'revision', key: 'revision', width: 125, render: value => <Tag>{value}</Tag> },
    { title: '操作', key: 'actions', width: 155, render: (_, item) => editingKey === item.key
      ? <Space><Button size="small" type="primary" icon={<SaveOutlined />} onClick={() => saveConfiguration(item.key, editingValue, item.revision)}>保存</Button><Button size="small" onClick={() => setEditingKey(null)}>取消</Button></Space>
      : <Space><Button size="small" icon={<EditOutlined />} onClick={() => { setEditingKey(item.key); setEditingValue(item.value); }}>编辑</Button><Popconfirm title="确认删除此配置？" onConfirm={() => deleteConfiguration(item)}><Button size="small" danger icon={<DeleteOutlined />}>删除</Button></Popconfirm></Space> }
  ];

  const configurationMetaItems = configurationMeta.provider === 'nacos'
    ? [{ key: 'serverAddr', label: '服务地址' }, { key: 'namespace', label: 'Namespace' }, { key: 'dataId', label: 'Data ID' }, { key: 'group', label: 'Group' }]
    : configurationMeta.provider === 'zookeeper'
      ? [{ key: 'connectString', label: '连接串' }, { key: 'rootPath', label: 'Root Path' }]
      : [{ key: 'endpoints', label: 'Endpoints' }, { key: 'rootPrefix', label: 'Root Prefix' }];

  const configurationView = <Content className="content">
    <div className="page-heading"><div><Text className="eyebrow">CONFIGURATION CENTER</Text><Title level={2}>配置管理</Title><Text type="secondary">通过当前激活的 {configurationMeta.provider} 配置中心管理网关运行配置</Text></div><Space><Badge status="processing" text={`${configurationMeta.provider} ACTIVE`} /><Button icon={<ReloadOutlined />} onClick={loadConfiguration}>刷新</Button></Space></div>
    {configurationError && <Alert type="error" showIcon message="配置操作失败" description={configurationError} closable onClose={() => setConfigurationError('')} />}
    {configurationNotice && <Alert className="config-notice" type="success" showIcon message={configurationNotice} closable onClose={() => setConfigurationNotice('')} />}
    <Card className="configuration-meta" title={<Space><SettingOutlined />当前配置中心</Space>} bordered={false}>
      <Descriptions size="small" column={{ xs: 1, sm: 2, lg: 4 }} items={configurationMetaItems.map(item => ({ key: item.key, label: item.label, children: Array.isArray(configurationMeta.details?.[item.key]) ? configurationMeta.details[item.key].join(', ') : configurationMeta.details?.[item.key] || '-' }))} />
    </Card>
    <Row gutter={[16, 16]} className="configuration-layout">
      <Col xs={24} lg={16}><Card title={<Space><FileTextOutlined />配置条目</Space>} extra={<Tag color="blue">{configuration.length} 条</Tag>} bordered={false}>
        <Table rowKey="key" columns={configColumns} dataSource={configuration} pagination={{ pageSize: 10 }} locale={{ emptyText: <Empty description="当前配置中心没有配置条目" /> }} scroll={{ x: 760 }} />
      </Card></Col>
      <Col xs={24} lg={8}><Card title={<Space><PlusOutlined />新增配置</Space>} bordered={false}>
        <Input className="config-input" prefix="Key" placeholder="例如 providers/kafka" value={newKey} onChange={event => setNewKey(event.target.value)} />
        <Input.TextArea className="config-input" rows={7} placeholder="配置值，支持 JSON 或普通文本" value={newValue} onChange={event => setNewValue(event.target.value)} />
        <Button type="primary" block icon={<SaveOutlined />} disabled={!newKey.trim()} onClick={async () => { await saveConfiguration(newKey.trim(), newValue, null); setNewKey(''); setNewValue(''); }}>写入配置中心</Button>
        <div className="configuration-sync-note"><Tag color="green">同步</Tag><Text type="secondary">写入共享配置中心后，其他节点通过 watch 更新本地内存镜像。</Text></div>
      </Card><Card className="memory-card" title="本节点内存镜像" bordered={false}><div className="summary-list"><div><span>镜像条目</span><b>{memory.values?.length || 0}</b></div><div><span>解析器</span><b>{Object.keys(memory.parsed || {}).join(', ') || '-'}</b></div><div><span>解析错误</span><b>{Object.keys(memory.parserErrors || {}).length}</b></div></div></Card></Col>
    </Row>
  </Content>;

  const spiColumns = [
    { title: '服务类型', dataIndex: 'serviceType', key: 'serviceType', width: 220, render: value => <Tooltip title={value}><span className="spi-type-label">{shortTypeName(value)}</span></Tooltip> },
    { title: 'Service ID', dataIndex: 'serviceId', key: 'serviceId', render: value => <Text strong>{value}</Text> },
    { title: '插件', dataIndex: 'pluginId', key: 'pluginId' },
    { title: '来源', dataIndex: 'source', key: 'source', ellipsis: true },
    { title: '调用', key: 'calls', render: (_, item) => `${item.successes || 0} / ${item.invocations || 0}` },
    { title: '错误', dataIndex: 'lastError', key: 'lastError', ellipsis: true }
  ];
  const pluginView = <Content className="content">
    <div className="page-heading"><div><Text className="eyebrow">PLUGIN RUNTIME</Text><Title level={2}>SPI 与外部插件</Title><Text type="secondary">查看插件发现、ServiceLoader 注册、调用统计与加载错误</Text></div><Button icon={<ReloadOutlined />} onClick={loadSpi}>刷新</Button></div>
    <Row gutter={[16, 16]} className="metrics">
      <Col xs={24} sm={8}><Card bordered={false}><Statistic title="已注册 SPI 服务" value={spi.services?.length || 0} prefix={<ApiOutlined />} /></Card></Col>
      <Col xs={24} sm={8}><Card bordered={false}><Statistic title="握手 Provider" value={spi.handshakeProviders?.length || 0} prefix={<SettingOutlined />} /></Card></Col>
      <Col xs={24} sm={8}><Card bordered={false}><Statistic title="外部插件" value={external.plugins?.length || 0} prefix={<CloudServerOutlined />} /></Card></Col>
    </Row>
    {Object.keys(external.errors || {}).length > 0 && <Alert type="error" showIcon message="外部插件发现错误" description={Object.entries(external.errors).map(([jar, reason]) => <div key={jar}>{jar}: {reason}</div>)} />}
    <div className="spi-sections">
      <Card title="SPI 服务注册表" bordered={false}><Table rowKey={item => `${item.serviceType}:${item.serviceId}`} columns={spiColumns} dataSource={spi.services || []} pagination={false} locale={{ emptyText: <Empty description="暂无 SPI 服务" /> }} scroll={{ x: 900 }} /></Card>
      <Card title="握手与认证 Provider" bordered={false}>
        <Descriptions size="small" column={{ xs: 1, sm: 2 }} items={[
          { key: 'handshake', label: 'Handshake', children: (spi.handshakeProviders || []).map(item => `${item.id} [${(item.protocols || []).join(', ')}]`).join('; ') || '-' },
          { key: 'auth', label: 'Authenticator', children: (spi.authenticators || []).map(item => item.id).join(', ') || '-' }
        ]} />
      </Card>
    </div>
  </Content>;

  return <Layout className="app-shell">
    <Sider width={244} className="app-sider">
      <div className="brand"><div className="brand-mark"><DashboardOutlined /></div><div><b>IoT Gateway</b><span>Control Center</span></div></div>
      <div className={`nav-item ${view === 'dashboard' ? 'active' : ''}`} onClick={() => setView('dashboard')}><DashboardOutlined /> 运行监控</div>
      <div className="nav-item"><ApiOutlined /> 设备接入</div>
      <div className="nav-item"><ClusterOutlined /> 节点集群</div>
      <div className={`nav-item ${view === 'configuration' ? 'active' : ''}`} onClick={() => setView('configuration')}><SettingOutlined /> 配置管理</div>
      <div className={`nav-item ${view === 'plugins' ? 'active' : ''}`} onClick={() => setView('plugins')}><ThunderboltOutlined /> SPI 插件</div>
    </Sider>
    <Layout>
      <Header className="topbar"><Space><CloudServerOutlined /><Text strong>网关运行监控</Text><Tag color="cyan">DEV</Tag></Space><Space><Text type="secondary">自动刷新 10s</Text><ReloadOutlined onClick={load} className="clickable"/></Space></Header>
      {view === 'configuration' ? configurationView : view === 'plugins' ? pluginView : <Content className="content">
        <div className="page-heading"><div><Text className="eyebrow">MAGIC API IOT PLUGINS</Text><Title level={2}>Gateway Overview</Title><Text type="secondary">观察当前 Spring 容器中的 IoT 插件、连接能力与运行健康度</Text></div><Space><Badge status={status.status === 'UP' ? 'success' : 'error'} text={status.status === 'UP' ? '网关在线' : '连接异常'} /><Text type="secondary">{updatedAt ? `更新于 ${updatedAt.toLocaleTimeString()}` : '等待数据'}</Text></Space></div>
        {error && <Alert type="warning" showIcon message="无法读取网关状态" description={error} closable onClose={() => setError('')} />}
        <Row gutter={[16, 16]} className="metrics">
          <Col xs={24} sm={12} lg={6}><Card bordered={false}><Statistic title="网关状态" value={status.status === 'UP' ? 'ONLINE' : status.status} prefix={<CheckCircleOutlined />} valueStyle={{ color: status.status === 'UP' ? '#16a34a' : '#dc2626' }}/></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false}><Statistic title="IoT 组件" value={components.length} prefix={<ThunderboltOutlined />} suffix="个"/></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false}><Statistic title="Provider 健康" value={healthyProviderCount} prefix={<HeartOutlined />} suffix={` / ${providers.length}`}/><Progress percent={providers.length ? Math.round(healthyProviderCount / providers.length * 100) : 0} showInfo={false} strokeColor={healthyProviderCount === providers.length ? '#16a34a' : '#dc2626'}/></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false}><Statistic title="TCP 活跃连接" value={transport?.activeConnections || 0} prefix={<DatabaseOutlined />} suffix="条"/></Card></Col>
        </Row>
        <Card className="provider-health" title={<Space><HeartOutlined />Provider 健康探测</Space>} extra={<Tag color={healthyProviderCount === providers.length ? 'green' : 'red'}>{healthyProviderCount} / {providers.length} UP</Tag>} bordered={false}>
          <Table rowKey={item => `${item.providerType}:${item.providerId}`} columns={providerColumns} dataSource={providers} pagination={false} locale={{ emptyText: <Empty description="当前没有启用 Provider 探针" /> }} scroll={{ x: 840 }}/>
        </Card>
        <Row gutter={[16, 16]}>
          <Col xs={24} lg={16}><Card title={<Space><ApiOutlined />组件清单</Space>} extra={<Tag color="green">{activeCount} UP</Tag>} bordered={false}><Table rowKey="id" columns={columns} dataSource={components} pagination={{ pageSize: 8 }} locale={{ emptyText: <Empty description="暂无组件探针数据" /> }} scroll={{ x: 680 }}/></Card></Col>
          <Col xs={24} lg={8}><Card title={<Space><CloudServerOutlined />运行时摘要</Space>} bordered={false}><div className="summary-list"><div><span>设备注册</span><b>{status.deviceRegistry}</b></div><div><span>会话仓库</span><b>{status.sessionRepository}</b></div><div><span>消息总线</span><b>{status.messageBus}</b></div><div><span>协议</span><b>{runtime.protocol?.protocolIds?.join(', ') || '-'}</b></div><div><span>TCP 监听</span><b>{transportTarget(transport) || '-'}</b></div><div><span>接收/发布</span><b>{runtime.protocol?.receivedFrames || 0} / {runtime.protocol?.publishedMessages || 0}</b></div><div><span>流量</span><b>{transport?.receivedBytes || 0} B in / {transport?.sentBytes || 0} B out</b></div><div><span>健康入口</span><Tag color="blue">Actuator</Tag></div></div></Card><Card className="hint-card" bordered={false}><Text type="secondary">Provider 探针结果缓存 10 秒，与 Actuator 共享。Raw TCP 使用换行符分帧，当前连接身份为临时 channel ID。</Text></Card></Col>
        </Row>
      </Content>}
    </Layout>
  </Layout>;
}

createRoot(document.getElementById('root')).render(<ConfigProvider theme={{ token: { colorPrimary: '#0f766e', borderRadius: 8, colorBgLayout: '#f4f7f8' } }}><App /></ConfigProvider>);
