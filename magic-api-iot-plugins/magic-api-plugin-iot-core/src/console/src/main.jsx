import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Alert, Badge, Card, Col, ConfigProvider, Empty, Layout, Progress, Row, Space, Statistic, Table, Tag, Typography } from 'antd';
import { ApiOutlined, CheckCircleOutlined, ClusterOutlined, CloudServerOutlined, DashboardOutlined, DatabaseOutlined, ReloadOutlined, ThunderboltOutlined } from '@ant-design/icons';
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

function App() {
  const [status, setStatus] = useState(fallback);
  const [components, setComponents] = useState([]);
  const [error, setError] = useState('');
  const [updatedAt, setUpdatedAt] = useState();

  const load = useCallback(async () => {
    try {
      const [nextStatus, nextComponents] = await Promise.all([
        getJson('/api/iot/gateway/status'),
        getJson('/api/iot/gateway/components')
      ]);
      setStatus(nextStatus);
      setComponents(nextComponents.components || []);
      setError('');
      setUpdatedAt(new Date());
    } catch (err) {
      setError(err.message || 'Gateway API unavailable');
    }
  }, []);

  useEffect(() => { load(); const timer = setInterval(load, 10000); return () => clearInterval(timer); }, [load]);

  const activeCount = components.filter(item => item.status === 'UP').length;
  const groups = useMemo(() => new Set(components.map(item => item.module)).size, [components]);
  const columns = [
    { title: '组件', dataIndex: 'name', key: 'name', render: value => <Text strong>{value}</Text> },
    { title: '模块', dataIndex: 'module', key: 'module', render: value => <Tag color="blue">{value}</Tag> },
    { title: '实现', dataIndex: 'implementation', key: 'implementation', ellipsis: true },
    { title: '状态', dataIndex: 'status', key: 'status', render: value => <Badge status={value === 'UP' ? 'success' : 'error'} text={value} /> }
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
          <Col xs={24} sm={12} lg={6}><Card bordered={false}><Statistic title="已就绪" value={activeCount} prefix={<ClusterOutlined />} suffix={` / ${components.length}`}/><Progress percent={components.length ? Math.round(activeCount / components.length * 100) : 0} showInfo={false} strokeColor="#0ea5e9"/></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false}><Statistic title="模块分组" value={groups} prefix={<DatabaseOutlined />} suffix="组"/></Card></Col>
        </Row>
        <Row gutter={[16, 16]}>
          <Col xs={24} lg={16}><Card title={<Space><ApiOutlined />组件清单</Space>} extra={<Tag color="green">{activeCount} UP</Tag>} bordered={false}><Table rowKey="id" columns={columns} dataSource={components} pagination={{ pageSize: 8 }} locale={{ emptyText: <Empty description="暂无组件探针数据" /> }} scroll={{ x: 680 }}/></Card></Col>
          <Col xs={24} lg={8}><Card title={<Space><CloudServerOutlined />运行时摘要</Space>} bordered={false}><div className="summary-list"><div><span>设备注册</span><b>{status.deviceRegistry}</b></div><div><span>会话仓库</span><b>{status.sessionRepository}</b></div><div><span>消息总线</span><b>{status.messageBus}</b></div><div><span>采集模式</span><Tag color="blue">Bean Probe</Tag></div></div></Card><Card className="hint-card" bordered={false}><Text type="secondary">页面每 10 秒从当前启动实例重新探测组件。生产环境可将探针替换为 Actuator、Prometheus 或集群聚合接口。</Text></Card></Col>
        </Row>
      </Content>
    </Layout>
  </Layout>;
}

createRoot(document.getElementById('root')).render(<ConfigProvider theme={{ token: { colorPrimary: '#0f766e', borderRadius: 8, colorBgLayout: '#f4f7f8' } }}><App /></ConfigProvider>);

