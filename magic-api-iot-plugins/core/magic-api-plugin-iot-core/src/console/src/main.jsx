import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Alert, Badge, Card, Col, ConfigProvider, Empty, Layout, Progress, Row, Space, Statistic, Table, Tag, Typography } from 'antd';
import { ApiOutlined, CheckCircleOutlined, ClusterOutlined, CloudServerOutlined, DashboardOutlined, DatabaseOutlined, HeartOutlined, ReloadOutlined, ThunderboltOutlined } from '@ant-design/icons';
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

function App() {
  const [status, setStatus] = useState(fallback);
  const [components, setComponents] = useState([]);
  const [providers, setProviders] = useState([]);
  const [runtime, setRuntime] = useState({ protocol: { protocolIds: [] }, transports: [] });
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

  return <Layout className="app-shell">
    <Sider width={244} className="app-sider">
      <div className="brand"><div className="brand-mark"><DashboardOutlined /></div><div><b>IoT Gateway</b><span>Control Center</span></div></div>
      <div className="nav-item active"><DashboardOutlined /> 运行监控</div>
      <div className="nav-item"><ApiOutlined /> 设备接入</div>
      <div className="nav-item"><ClusterOutlined /> 节点集群</div>
    </Sider>
    <Layout>
      <Header className="topbar"><Space><CloudServerOutlined /><Text strong>网关运行监控</Text><Tag color="cyan">DEV</Tag></Space><Space><Text type="secondary">自动刷新 10s</Text><ReloadOutlined onClick={load} className="clickable"/></Space></Header>
      <Content className="content">
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
      </Content>
    </Layout>
  </Layout>;
}

createRoot(document.getElementById('root')).render(<ConfigProvider theme={{ token: { colorPrimary: '#0f766e', borderRadius: 8, colorBgLayout: '#f4f7f8' } }}><App /></ConfigProvider>);
