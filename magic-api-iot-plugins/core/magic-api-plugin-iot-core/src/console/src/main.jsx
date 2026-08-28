import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { createRoot } from 'react-dom/client';
import Editor from '@monaco-editor/react';
import { Alert, Badge, Button, Card, Col, ConfigProvider, Descriptions, Divider, Empty, Input, Layout, List, Modal, Popconfirm, Progress, Row, Select, Space, Statistic, Switch, Table, Tag, Tooltip, Typography } from 'antd';
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

function durationToMs(duration) {
  if (typeof duration === 'number') return duration;
  if (duration && typeof duration === 'object') {
    if (typeof duration.seconds === 'number') return duration.seconds * 1000 + Math.floor((duration.nanos || 0) / 1000000);
    if (typeof duration.millis === 'number') return duration.millis;
  }
  if (typeof duration === 'string') {
    const match = duration.match(/^PT(?:(\d+(?:\.\d+)?)H)?(?:(\d+(?:\.\d+)?)M)?(?:(\d+(?:\.\d+)?)S)?$/i);
    if (match) return Math.round((Number(match[1] || 0) * 3600 + Number(match[2] || 0) * 60 + Number(match[3] || 0)) * 1000);
    const millis = Number(duration);
    if (Number.isFinite(millis)) return millis;
  }
  return 2000;
}

const SCRIPT_LANGUAGE_OPTIONS = [
  { value: 'javascript', label: 'JavaScript', engine: 'graalvm' },
  { value: 'python', label: 'Python', engine: null },
  { value: 'java', label: 'Java', engine: null },
  { value: 'groovy', label: 'Groovy', engine: 'groovy' },
  { value: 'aviator', label: 'Aviator', engine: 'aviator' }
];

function defaultLanguageForEngine(engineId) {
  return SCRIPT_LANGUAGE_OPTIONS.find(item => item.engine === engineId)?.value || 'javascript';
}

function configureScriptEditor(editor, monaco) {
  const moduleSuggestions = [
    { label: "import iot", detail: "IoT facade module", insertText: "import { iot } from 'iot';", kind: monaco.languages.CompletionItemKind.Module },
    { label: "import iot.device", detail: "设备上下文与认证动作", insertText: "import { device } from 'iot.device';", kind: monaco.languages.CompletionItemKind.Module },
    { label: "import iot.route", detail: "设备路由动作", insertText: "import { route } from 'iot.route';", kind: monaco.languages.CompletionItemKind.Module },
    { label: "import iot.message", detail: "消息投递动作", insertText: "import { message } from 'iot.message';", kind: monaco.languages.CompletionItemKind.Module },
    { label: "import iot.command", detail: "设备命令动作", insertText: "import { command } from 'iot.command';", kind: monaco.languages.CompletionItemKind.Module }
  ];
  const symbols = [
    { label: "input", detail: "当前事件输入", insertText: "input", kind: monaco.languages.CompletionItemKind.Variable },
    { label: "attributes", detail: "执行上下文属性", insertText: "attributes", kind: monaco.languages.CompletionItemKind.Variable },
    { label: "route.bind", detail: "生成设备路由动作", insertText: "route.bind", kind: monaco.languages.CompletionItemKind.Method },
    { label: "device.authenticate", detail: "生成设备认证动作", insertText: "device.authenticate", kind: monaco.languages.CompletionItemKind.Method },
    { label: "message.publish", detail: "生成消息投递动作", insertText: "message.publish", kind: monaco.languages.CompletionItemKind.Method },
    { label: "command.send", detail: "生成设备命令动作", insertText: "command.send", kind: monaco.languages.CompletionItemKind.Method },
    { label: "actions", detail: "标准动作计划数组", insertText: "actions", kind: monaco.languages.CompletionItemKind.Property }
  ];
  const actionSnippets = [
    { label: "route.bind action", detail: "标准动作 DSL", insertText: "{ actionId: 'route.bind', parameters: { productId: input.productId, deviceId: input.deviceId } }", kind: monaco.languages.CompletionItemKind.Snippet },
    { label: "device.authenticate action", detail: "标准动作 DSL", insertText: "{ actionId: 'device.authenticate', parameters: { productId: input.productId, deviceId: input.deviceId } }", kind: monaco.languages.CompletionItemKind.Snippet }
  ];
  const provider = monaco.languages.registerCompletionItemProvider(['javascript', 'typescript', 'java', 'python', 'groovy', 'plaintext'], {
    triggerCharacters: ['.', '/', "'", '"'],
    provideCompletionItems(model, position) {
      const line = model.getLineContent(position.lineNumber).slice(0, position.column - 1);
      const suggestions = line.includes('import') || line.endsWith("'") || line.endsWith('"') ? moduleSuggestions : [...symbols, ...actionSnippets];
      return { suggestions: suggestions.map(item => ({ ...item, range: undefined })) };
    }
  });
  editor.onDidDispose(() => provider.dispose());
}

function App() {
  const [status, setStatus] = useState(fallback);
  const [components, setComponents] = useState([]);
  const [providers, setProviders] = useState([]);
  const [runtime, setRuntime] = useState({ protocol: { protocolIds: [] }, transports: [] });
  const [view, setView] = useState('dashboard');
  const [scripts, setScripts] = useState({ scripts: [], engines: [] });
  const [selectedScript, setSelectedScript] = useState(null);
  const [scriptForm, setScriptForm] = useState({ scriptId: '', name: '', engineId: 'aviator', language: 'aviator', source: 'true', permissions: '', triggers: 'MESSAGE', timeoutMs: 2000 });
  const [scriptDebugInput, setScriptDebugInput] = useState('{}');
  const [scriptResult, setScriptResult] = useState(null);
  const [scriptMessage, setScriptMessage] = useState('');
  const [scriptError, setScriptError] = useState('');
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
  const [debugRequest, setDebugRequest] = useState({ protocolId: 'raw', transportId: 'tcp', remoteAddress: '127.0.0.1:10001', handshakeProviderId: 'vendor-pipe-v1', encoding: 'text', payload: 'HELLO|robot|agv-001|secret' });
  const [debugResult, setDebugResult] = useState(null);
  const [debugging, setDebugging] = useState(false);
  const [pluginValidation, setPluginValidation] = useState(null);
  const [devicePage, setDevicePage] = useState({ items: [], total: 0, page: 1, pageSize: 20 });
  const [deviceFilters, setDeviceFilters] = useState({ productId: '', keyword: '' });
  const [deviceForm, setDeviceForm] = useState({ productId: 'robot', deviceId: '', protocolId: 'raw', enabled: true, capabilities: '{}', credentialType: 'secret', credential: '', generateCredential: true });
  const [deviceError, setDeviceError] = useState('');
  const [deviceNotice, setDeviceNotice] = useState('');
  const [credentialDevice, setCredentialDevice] = useState(null);
  const [credentialTypes, setCredentialTypes] = useState([]);
  const [credentialForm, setCredentialForm] = useState({ type: 'secret', value: '' });
  const [generatedCredential, setGeneratedCredential] = useState('');
  const [editingDevice, setEditingDevice] = useState(null);
  const [dashboardSections, setDashboardSections] = useState(() => {
    try {
      const saved = JSON.parse(localStorage.getItem('iot-dashboard-sections') || '{}');
      return { providers: saved.providers !== false, components: saved.components !== false, summary: saved.summary !== false };
    } catch { return { providers: true, components: true, summary: true }; }
  });
  const [activeDashboardSection, setActiveDashboardSection] = useState('components');
  const dashboardRefs = useRef({});
  const [spiSections, setSpiSections] = useState(() => {
    try {
      const saved = JSON.parse(localStorage.getItem('iot-spi-sections') || '{}');
      return { services: saved.services !== false, providers: saved.providers !== false, external: saved.external !== false, debug: saved.debug !== false };
    } catch { return { services: true, providers: true, external: true, debug: true }; }
  });
  const [activeSpiSection, setActiveSpiSection] = useState('services');
  const spiRefs = useRef({});

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

  useEffect(() => {
    let disposed = false;
    let timer;
    const refresh = async () => {
      if (disposed) return;
      await load();
      if (!disposed) timer = setTimeout(refresh, 10000);
    };
    refresh();
    return () => { disposed = true; clearTimeout(timer); };
  }, [load]);

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

  const rescanExternalPlugins = async () => {
    try {
      const response = await fetch('/api/iot/gateway/plugins/external/rescan', { method: 'POST' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadSpi();
    } catch (err) { setError(err.message || '插件扫描失败'); }
  };

  const reloadExternalPlugin = async pluginId => {
    try {
      const response = await fetch(`/api/iot/gateway/plugins/external/${encodeURIComponent(pluginId)}/reload`, { method: 'POST' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadSpi();
    } catch (err) { setError(err.message || '插件重新加载失败'); }
  };

  const disableExternalPlugin = async pluginId => {
    try {
      const response = await fetch(`/api/iot/gateway/plugins/external/${encodeURIComponent(pluginId)}/disable`, { method: 'POST' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadSpi();
    } catch (err) { setError(err.message || '插件禁用失败'); }
  };

  const enableExternalPlugin = async jar => {
    try {
      const response = await fetch('/api/iot/gateway/plugins/external/enable', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ jar }) });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadSpi();
    } catch (err) { setError(err.message || '插件启用失败'); }
  };

  const promptEnableExternalPlugin = () => {
    const jar = window.prompt('请输入要启用的插件 JAR 绝对路径');
    if (jar?.trim()) enableExternalPlugin(jar.trim());
  };

  const validateExternalPlugin = async () => {
    const jar = window.prompt('请输入要验证的插件 JAR 绝对路径');
    if (!jar?.trim()) return;
    try {
      const response = await fetch('/api/iot/gateway/plugins/external/validate', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ jar: jar.trim() }) });
      const result = await response.json();
      if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`);
      setPluginValidation(result);
    } catch (err) { setError(err.message || '插件验证失败'); }
  };

  const upgradeExternalPlugin = async pluginId => {
    const jar = window.prompt('请输入新版本 JAR 的绝对路径');
    if (!jar?.trim()) return;
    try {
      const response = await fetch(`/api/iot/gateway/plugins/external/${encodeURIComponent(pluginId)}/upgrade`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ jar: jar.trim() }) });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadSpi();
    } catch (err) { setError(err.message || '插件升级失败'); }
  };

  const rollbackExternalPlugin = async pluginId => {
    try {
      const response = await fetch(`/api/iot/gateway/plugins/external/${encodeURIComponent(pluginId)}/rollback`, { method: 'POST' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadSpi();
    } catch (err) { setError(err.message || '插件回滚失败'); }
  };

  const debugHandshake = async () => {
    setDebugging(true);
    try {
      const response = await fetch('/api/iot/gateway/spi/handshake/debug', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(debugRequest) });
      const result = await response.json();
      if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`);
      setDebugResult(result);
    } catch (err) { setDebugResult({ error: err.message || '握手调试失败' }); }
    finally { setDebugging(false); }
  };

  useEffect(() => { if (view === 'configuration') loadConfiguration(); }, [view, loadConfiguration]);
  useEffect(() => { if (view === 'plugins') loadSpi(); }, [view, loadSpi]);

  const updateDashboardSections = next => {
    setDashboardSections(next);
    localStorage.setItem('iot-dashboard-sections', JSON.stringify(next));
  };

  const focusDashboardSection = section => {
    if (!dashboardSections[section]) return;
    setActiveDashboardSection(section);
    dashboardRefs.current[section]?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };

  const updateSpiSections = next => {
    setSpiSections(next);
    localStorage.setItem('iot-spi-sections', JSON.stringify(next));
  };

  const focusSpiSection = section => {
    if (!spiSections[section]) return;
    setActiveSpiSection(section);
    spiRefs.current[section]?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };

  const loadScripts = useCallback(async () => {
    try { const result = await getJson('/api/iot/gateway/scripts'); setScripts(result); setScriptError(''); }
    catch (err) { setScriptError(err.message || '脚本 API 不可用'); }
  }, []);
  useEffect(() => { if (view === 'scripts') loadScripts(); }, [view, loadScripts]);

  const chooseScript = async script => {
    try {
      const item = await getJson(`/api/iot/gateway/scripts/${encodeURIComponent(script.scriptId)}`);
      setSelectedScript(item);
      setScriptForm({ scriptId: item.scriptId, name: item.name, engineId: item.engineId, language: item.language, source: item.source, permissions: [...(item.permissions || [])].join(','), triggers: [...(item.triggers || [])].join(','), timeoutMs: durationToMs(item.timeout) });
      setScriptResult(null);
    } catch (err) { setScriptError(err.message || '脚本读取失败'); }
  };
  const newScript = () => { setSelectedScript(null); setScriptForm({ scriptId: `route-${Date.now()}`, name: '新脚本', engineId: 'aviator', language: 'aviator', source: 'true', permissions: '', triggers: 'MESSAGE', timeoutMs: 2000 }); setScriptResult(null); };
  const saveScript = async () => {
    try {
      const response = await fetch(`/api/iot/gateway/scripts/${encodeURIComponent(scriptForm.scriptId)}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ ...scriptForm, permissions: scriptForm.permissions.split(',').map(item => item.trim()).filter(Boolean), triggers: scriptForm.triggers.split(',').map(item => item.trim()).filter(Boolean), timeoutMs: Number(scriptForm.timeoutMs) || 2000, metadata: {} }) });
      const result = await response.json(); if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`);
      setSelectedScript(result.script); setScriptMessage(result.validation.valid ? '脚本已保存，语法校验通过' : '脚本已保存，但校验未通过'); await loadScripts(); await chooseScript(result.script);
    } catch (err) { setScriptError(err.message || '脚本保存失败'); }
  };
  const scriptAction = async (action, body) => {
    if (!selectedScript) return;
    try { const response = await fetch(`/api/iot/gateway/scripts/${encodeURIComponent(selectedScript.scriptId)}${action}`, { method: action === '/enabled' ? 'PUT' : 'POST', headers: { 'Content-Type': 'application/json' }, body: body ? JSON.stringify(body) : undefined }); const result = response.status === 204 ? null : await response.json(); if (!response.ok) throw new Error(result?.message || `${response.status} ${response.statusText}`); if (action !== '/validate') setSelectedScript(result || selectedScript); setScriptMessage(action === '/validate' ? (result.valid ? '脚本校验通过' : '脚本校验未通过') : '操作已完成'); await loadScripts(); if (action !== '/validate') await chooseScript(selectedScript); }
    catch (err) { setScriptError(err.message || '脚本操作失败'); }
  };
  const debugScript = async () => { try { const input = JSON.parse(scriptDebugInput || '{}'); const response = await fetch(`/api/iot/gateway/scripts/${encodeURIComponent(selectedScript.scriptId)}/debug`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ traceId: `console-${Date.now()}`, trigger: 'MANUAL', input, attributes: {} }) }); const result = await response.json(); if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`); setScriptResult(result); } catch (err) { setScriptResult({ status: 'ERROR', reason: err.message || '调试失败' }); } };
  const deleteScript = async () => { if (!selectedScript) return; try { const response = await fetch(`/api/iot/gateway/scripts/${encodeURIComponent(selectedScript.scriptId)}`, { method: 'DELETE' }); if (!response.ok) throw new Error(`${response.status} ${response.statusText}`); setSelectedScript(null); setScriptMessage('脚本已删除'); await loadScripts(); newScript(); } catch (err) { setScriptError(err.message || '脚本删除失败'); } };

  const loadDevices = useCallback(async (page = 1, pageSize = devicePage.pageSize) => {
    try {
      const query = new URLSearchParams({ productId: deviceFilters.productId, keyword: deviceFilters.keyword, page: String(page), pageSize: String(pageSize) });
      setDevicePage(await getJson(`/api/iot/gateway/devices?${query}`));
      setDeviceError('');
    } catch (err) { setDeviceError(err.message || '设备注册中心不可用'); }
  }, [deviceFilters, devicePage.pageSize]);

  useEffect(() => { if (view === 'devices') loadDevices(); }, [view, loadDevices]);

  const registerDevice = async () => {
    try {
      let capabilities;
      try { capabilities = JSON.parse(deviceForm.capabilities || '{}'); }
      catch { throw new Error('能力定义必须是 JSON 对象'); }
      const editing = editingDevice;
      const url = editing
        ? `/api/iot/gateway/devices/${encodeURIComponent(editing.productId)}/${encodeURIComponent(editing.deviceId)}`
        : '/api/iot/gateway/devices';
      const response = await fetch(url, { method: editing ? 'PUT' : 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ ...deviceForm, capabilities }) });
      const result = await response.json();
      if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`);
      setGeneratedCredential(result.generatedCredential || '');
      setDeviceNotice(editing ? `设备 ${editing.productId}/${editing.deviceId} 已更新` : `设备 ${result.device.identity.productId}/${result.device.identity.deviceId} 已登记`);
      setEditingDevice(null);
      setDeviceForm({ ...deviceForm, deviceId: '', credential: '' });
      await loadDevices();
    } catch (err) { setDeviceError(err.message || '设备登记失败'); }
  };

  const editDevice = item => {
    setEditingDevice(item.identity);
    setDeviceForm({ productId: item.identity.productId, deviceId: item.identity.deviceId, protocolId: item.protocolId, enabled: item.enabled, capabilities: JSON.stringify(item.capabilities || {}, null, 2), credentialType: 'secret', credential: '', generateCredential: false });
  };

  const deleteDevice = async item => {
    try {
      const { productId, deviceId } = item.identity;
      const response = await fetch(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      if (editingDevice?.productId === productId && editingDevice?.deviceId === deviceId) setEditingDevice(null);
      setDeviceNotice(`设备 ${productId}/${deviceId} 已删除`);
      await loadDevices(1, devicePage.pageSize);
    } catch (err) { setDeviceError(err.message || '设备删除失败'); }
  };

  const changeDeviceStatus = async item => {
    try {
      const { productId, deviceId } = item.identity;
      const response = await fetch(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/status`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ enabled: !item.enabled }) });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadDevices(devicePage.page, devicePage.pageSize);
    } catch (err) { setDeviceError(err.message || '设备状态更新失败'); }
  };

  const openCredentials = async item => {
    try {
      const { productId, deviceId } = item.identity;
      const result = await getJson(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/credentials`);
      setCredentialDevice(item);
      setCredentialTypes(result.credentialTypes || []);
      setCredentialForm({ type: 'secret', value: '' });
      setGeneratedCredential('');
    } catch (err) { setDeviceError(err.message || '凭证信息读取失败'); }
  };

  const saveCredential = async generate => {
    if (!credentialDevice) return;
    try {
      const { productId, deviceId } = credentialDevice.identity;
      const response = await fetch(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/credentials`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ type: credentialForm.type, value: credentialForm.value, generate }) });
      const result = await response.json();
      if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`);
      setCredentialTypes(result.credentialTypes || []);
      setGeneratedCredential(result.generatedCredential || '');
      setCredentialForm({ ...credentialForm, value: '' });
      await loadDevices(devicePage.page, devicePage.pageSize);
      const refreshed = await getJson(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/credentials`);
      setCredentialTypes(refreshed.credentialTypes || []);
    } catch (err) { setDeviceError(err.message || '凭证设置失败'); }
  };

  const verifyCredential = async () => {
    if (!credentialDevice) return;
    try {
      const { productId, deviceId } = credentialDevice.identity;
      const response = await fetch(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/credentials/verify`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(credentialForm) });
      const result = await response.json();
      if (!response.ok) throw new Error(result.message || `${response.status} ${response.statusText}`);
      setDeviceNotice(result.verified ? '凭证验证通过' : '凭证验证失败');
    } catch (err) { setDeviceError(err.message || '凭证验证失败'); }
  };

  const deleteCredential = async type => {
    if (!credentialDevice) return;
    try {
      const { productId, deviceId } = credentialDevice.identity;
      const response = await fetch(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/credentials/${encodeURIComponent(type)}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
      await loadDevices(devicePage.page, devicePage.pageSize);
      const refreshed = await getJson(`/api/iot/gateway/devices/${encodeURIComponent(productId)}/${encodeURIComponent(deviceId)}/credentials`);
      setCredentialTypes(refreshed.credentialTypes || []);
    } catch (err) { setDeviceError(err.message || '凭证删除失败'); }
  };

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

  const deviceColumns = [
    { title: '设备', key: 'identity', render: (_, item) => <div><Text strong>{item.identity.deviceId}</Text><div><Text type="secondary">{item.identity.productId}</Text></div></div> },
    { title: '协议', dataIndex: 'protocolId', key: 'protocolId', render: value => <Tag color="blue">{value}</Tag> },
    { title: '状态', dataIndex: 'enabled', key: 'enabled', render: value => <Badge status={value ? 'success' : 'default'} text={value ? '启用' : '停用'} /> },
    { title: '能力', dataIndex: 'capabilities', key: 'capabilities', render: value => Object.keys(value || {}).length ? Object.keys(value).map(key => <Tag key={key}>{key}</Tag>) : '-' },
    { title: '版本', dataIndex: 'version', key: 'version', width: 80 },
    { title: '操作', key: 'actions', width: 300, render: (_, item) => <Space wrap><Button size="small" onClick={() => changeDeviceStatus(item)}>{item.enabled ? '停用' : '启用'}</Button><Button size="small" icon={<EditOutlined />} onClick={() => editDevice(item)}>编辑</Button><Button size="small" icon={<SettingOutlined />} onClick={() => openCredentials(item)}>凭证</Button><Popconfirm title="确认删除该设备及其凭证？" description="删除后不可恢复" onConfirm={() => deleteDevice(item)}><Button size="small" danger icon={<DeleteOutlined />}>删除</Button></Popconfirm></Space> }
  ];

  const devicesView = <Content className="content">
    <div className="page-heading"><div><Text className="eyebrow">DEVICE ONBOARDING</Text><Title level={2}>设备接入</Title><Text type="secondary">登记设备身份、控制启停状态并管理认证凭证</Text></div><Button icon={<ReloadOutlined />} onClick={() => loadDevices(devicePage.page, devicePage.pageSize)}>刷新</Button></div>
    {deviceError && <Alert className="device-alert" type="error" showIcon message="设备操作失败" description={deviceError} closable onClose={() => setDeviceError('')} />}
    {deviceNotice && <Alert className="device-alert" type="success" showIcon message={deviceNotice} closable onClose={() => setDeviceNotice('')} />}
    {generatedCredential && <Alert className="device-alert" type="warning" showIcon message="新凭证仅显示一次" description={<Text copyable code>{generatedCredential}</Text>} closable onClose={() => setGeneratedCredential('')} />}
    <Row gutter={[16, 16]}>
      <Col xs={24} lg={17}>
        <Card title={<Space><ApiOutlined />设备注册清单</Space>} extra={<Tag color="blue">{devicePage.total || 0} 台</Tag>} bordered={false}>
          <Space className="device-filters" wrap><Input placeholder="产品 ID" value={deviceFilters.productId} onChange={event => setDeviceFilters({ ...deviceFilters, productId: event.target.value })} /><Input.Search placeholder="设备 ID" value={deviceFilters.keyword} onChange={event => setDeviceFilters({ ...deviceFilters, keyword: event.target.value })} onSearch={() => loadDevices(1, devicePage.pageSize)} enterButton="查询" /></Space>
          <Table rowKey={item => item.identity.productId + '/' + item.identity.deviceId} columns={deviceColumns} dataSource={devicePage.items || []} pagination={{ current: devicePage.page, pageSize: devicePage.pageSize, total: devicePage.total, showSizeChanger: true, onChange: loadDevices }} locale={{ emptyText: <Empty description="暂无登记设备" /> }} scroll={{ x: 760 }} />
        </Card>
      </Col>
      <Col xs={24} lg={7}>
        <Card title={<Space>{editingDevice ? <EditOutlined /> : <PlusOutlined />}{editingDevice ? '编辑设备' : '登记设备'}</Space>} bordered={false}>
          <div className="device-form">
            <Input addonBefore="产品" disabled={Boolean(editingDevice)} value={deviceForm.productId} onChange={event => setDeviceForm({ ...deviceForm, productId: event.target.value })} />
            <Input addonBefore="设备" disabled={Boolean(editingDevice)} placeholder="例如 agv-001" value={deviceForm.deviceId} onChange={event => setDeviceForm({ ...deviceForm, deviceId: event.target.value })} />
            <Select value={deviceForm.protocolId} onChange={value => setDeviceForm({ ...deviceForm, protocolId: value })} options={[{ value: 'raw', label: 'Raw TCP' }, { value: 'mqtt', label: 'MQTT' }, { value: 'modbus-tcp', label: 'Modbus TCP' }]} />
            <Input.TextArea rows={4} value={deviceForm.capabilities} onChange={event => setDeviceForm({ ...deviceForm, capabilities: event.target.value })} placeholder='能力 JSON，例如 {"lift":"supported"}' />
            <Input addonBefore="凭证类型" value={deviceForm.credentialType} onChange={event => setDeviceForm({ ...deviceForm, credentialType: event.target.value })} />
            {!deviceForm.generateCredential && <Input.Password addonBefore="凭证" value={deviceForm.credential} onChange={event => setDeviceForm({ ...deviceForm, credential: event.target.value })} />}
            <div className="device-switch-row"><Text>自动生成凭证</Text><Switch checked={deviceForm.generateCredential} onChange={checked => setDeviceForm({ ...deviceForm, generateCredential: checked })} /></div>
            <div className="device-switch-row"><Text>登记后启用</Text><Switch checked={deviceForm.enabled} onChange={checked => setDeviceForm({ ...deviceForm, enabled: checked })} /></div>
            <Space direction="vertical" style={{ width: '100%' }}><Button type="primary" block icon={<SaveOutlined />} disabled={!deviceForm.productId.trim() || !deviceForm.deviceId.trim()} onClick={registerDevice}>{editingDevice ? '保存修改' : '登记设备'}</Button>{editingDevice && <Button block onClick={() => { setEditingDevice(null); setDeviceForm({ ...deviceForm, deviceId: '', credential: '' }); }}>取消编辑</Button>}</Space>
          </div>
        </Card>
      </Col>
    </Row>
    <Modal title={credentialDevice ? `凭证管理：${credentialDevice.identity.productId}/${credentialDevice.identity.deviceId}` : '凭证管理'} open={Boolean(credentialDevice)} onCancel={() => setCredentialDevice(null)} footer={null} destroyOnHidden>
      <div className="credential-panel">
        <div><Text type="secondary">已配置类型</Text><div className="credential-tags">{credentialTypes.length ? credentialTypes.map(type => <Tag key={type} closable onClose={event => { event.preventDefault(); deleteCredential(type); }}>{type}</Tag>) : <Text type="secondary">暂无凭证</Text>}</div></div>
        <Input addonBefore="类型" value={credentialForm.type} onChange={event => setCredentialForm({ ...credentialForm, type: event.target.value })} />
        <Input.Password addonBefore="凭证" value={credentialForm.value} onChange={event => setCredentialForm({ ...credentialForm, value: event.target.value })} />
        <Space wrap><Button type="primary" onClick={() => saveCredential(false)} disabled={!credentialForm.type.trim() || !credentialForm.value}>设置凭证</Button><Button onClick={() => saveCredential(true)} disabled={!credentialForm.type.trim()}>自动轮换</Button><Button icon={<CheckCircleOutlined />} onClick={verifyCredential} disabled={!credentialForm.value}>验证</Button></Space>
        {generatedCredential && <Alert type="warning" showIcon message="自动生成凭证仅显示一次" description={<Text copyable code>{generatedCredential}</Text>} />}
      </div>
    </Modal>
  </Content>;

  const scriptsView = <Content className="content">
    <div className="page-heading"><div><Text className="eyebrow">SCRIPT ORCHESTRATION</Text><Title level={2}>脚本编排</Title><Text type="secondary">用脚本组合握手、认证、路由和消息处理 SPI</Text></div><Space><Button icon={<PlusOutlined />} onClick={newScript}>新建脚本</Button><Button icon={<ReloadOutlined />} onClick={loadScripts}>刷新</Button></Space></div>
    {scriptError && <Alert className="device-alert" type="error" showIcon message="脚本操作失败" description={scriptError} closable onClose={() => setScriptError('')} />}
    {scriptMessage && <Alert className="device-alert" type="success" showIcon message={scriptMessage} closable onClose={() => setScriptMessage('')} />}
    <Row gutter={[16, 16]} className="script-layout">
      <Col xs={24} lg={6}><Card title="脚本目录" bordered={false} className="script-list-card"><List dataSource={scripts.scripts || []} renderItem={item => <div className={`script-list-item ${selectedScript?.scriptId === item.scriptId ? 'active' : ''}`} onClick={() => chooseScript(item)}><div><Text strong>{item.name}</Text><Text type="secondary" ellipsis>{item.scriptId}</Text></div><Space><Tag>{item.engineId}</Tag><Badge status={item.enabled ? 'success' : 'default'} /></Space></div>} locale={{ emptyText: '暂无脚本' }} /></Card></Col>
      <Col xs={24} lg={18}><Card bordered={false} className="script-editor-card">
        <div className="script-toolbar"><Space wrap><Input addonBefore="ID" value={scriptForm.scriptId} disabled={Boolean(selectedScript)} onChange={event => setScriptForm({ ...scriptForm, scriptId: event.target.value })} /><Input addonBefore="名称" value={scriptForm.name} onChange={event => setScriptForm({ ...scriptForm, name: event.target.value })} /><Select aria-label="执行引擎" value={scriptForm.engineId} onChange={value => setScriptForm({ ...scriptForm, engineId: value, language: defaultLanguageForEngine(value) })} options={(scripts.engines || []).map(item => ({ value: item.id, label: `${item.id} · ${(item.languages || []).join(', ')}` }))} /><Select aria-label="代码语言" value={scriptForm.language} onChange={value => setScriptForm({ ...scriptForm, language: value })} options={SCRIPT_LANGUAGE_OPTIONS.map(item => ({ value: item.value, label: item.label }))} /></Space></div>
        <div className="script-meta-row"><Input addonBefore="触发器" value={scriptForm.triggers} onChange={event => setScriptForm({ ...scriptForm, triggers: event.target.value })} placeholder="HANDSHAKE, AUTHENTICATE, ROUTE, MESSAGE" /><Input addonBefore="权限" value={scriptForm.permissions} onChange={event => setScriptForm({ ...scriptForm, permissions: event.target.value })} placeholder="device.read, route.bind" /><Input addonBefore="超时(ms)" type="number" value={scriptForm.timeoutMs} onChange={event => setScriptForm({ ...scriptForm, timeoutMs: event.target.value })} /></div>
        <div className="script-editor-surface"><Editor height="420px" language={scriptForm.language === 'aviator' ? 'plaintext' : scriptForm.language} theme="vs-dark" value={scriptForm.source} onChange={value => setScriptForm({ ...scriptForm, source: value || '' })} onMount={configureScriptEditor} options={{ minimap: { enabled: false }, fontSize: 14, wordWrap: 'on', automaticLayout: true, suggestOnTriggerCharacters: true }} /></div>
        <div className="script-actions"><Space wrap><Button type="primary" icon={<SaveOutlined />} onClick={saveScript}>保存草稿</Button><Button onClick={() => scriptAction('/validate')}>校验</Button><Button onClick={() => scriptAction('/publish')}>发布</Button><Button onClick={() => scriptAction('/enabled', { enabled: !selectedScript?.enabled })} disabled={!selectedScript}>{selectedScript?.enabled ? '停用' : '启用'}</Button><Popconfirm title="确认删除脚本？" onConfirm={deleteScript}><Button danger icon={<DeleteOutlined />} disabled={!selectedScript}>删除</Button></Popconfirm></Space></div>
        <Divider />
        <Row gutter={[16, 16]}><Col xs={24} lg={11}><Input.TextArea rows={7} value={scriptDebugInput} onChange={event => setScriptDebugInput(event.target.value)} placeholder="Dry-run 输入 JSON" /><Button className="script-debug-button" type="primary" icon={<ThunderboltOutlined />} disabled={!selectedScript} onClick={debugScript}>Dry-run 调试</Button></Col><Col xs={24} lg={13}>{scriptResult ? <pre className="script-result">{JSON.stringify(scriptResult, null, 2)}</pre> : <Empty description="执行结果将在这里显示" />}</Col></Row>
      </Card></Col>
    </Row>
  </Content>;

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
  const externalPluginColumns = [
    { title: '插件', dataIndex: 'pluginId', key: 'pluginId', render: value => <Text strong>{value}</Text> },
    { title: '版本', dataIndex: 'version', key: 'version' },
    { title: '状态', dataIndex: 'state', key: 'state', render: value => <Badge status={value === 'RUNNING' ? 'success' : value === 'DEGRADED' ? 'warning' : 'error'} text={value} /> },
    { title: 'SPI 服务', dataIndex: 'services', key: 'services', render: value => value?.length || 0 },
    { title: 'JAR', dataIndex: 'jar', key: 'jar', ellipsis: true },
    { title: '操作', key: 'actions', width: 280, render: (_, item) => <Space wrap>
      <Popconfirm title="确认禁用此插件？" onConfirm={() => disableExternalPlugin(item.pluginId)}><Button size="small" danger icon={<DeleteOutlined />}>禁用</Button></Popconfirm>
      <Button size="small" icon={<ReloadOutlined />} onClick={() => reloadExternalPlugin(item.pluginId)} disabled={item.state === 'STOPPING'}>重载</Button>
      <Button size="small" icon={<SaveOutlined />} onClick={() => upgradeExternalPlugin(item.pluginId)}>升级</Button>
      <Popconfirm title="使用上一版本回滚？" onConfirm={() => rollbackExternalPlugin(item.pluginId)}><Button size="small">回滚</Button></Popconfirm>
    </Space> }
  ];
  const pluginView = <Content className="content">
    <div className="page-heading"><div><Text className="eyebrow">PLUGIN RUNTIME</Text><Title level={2}>SPI 与外部插件</Title><Text type="secondary">查看插件发现、ServiceLoader 注册、调用统计与加载错误</Text></div><Space wrap><Button icon={<CheckCircleOutlined />} onClick={validateExternalPlugin}>验证 JAR</Button><Button icon={<PlusOutlined />} onClick={promptEnableExternalPlugin}>启用插件</Button><Button icon={<ReloadOutlined />} onClick={rescanExternalPlugins}>扫描并更新</Button><Button icon={<ReloadOutlined />} onClick={loadSpi}>刷新状态</Button></Space></div>
    <Card className="dashboard-controls" bordered={false} size="small">
      <Space wrap>
        <Text strong>显示内容</Text>
        <Switch checked={spiSections.services} onChange={checked => updateSpiSections({ ...spiSections, services: checked })} /><Text type="secondary">SPI 服务</Text>
        <Switch checked={spiSections.providers} onChange={checked => updateSpiSections({ ...spiSections, providers: checked })} /><Text type="secondary">握手认证</Text>
        <Switch checked={spiSections.external} onChange={checked => updateSpiSections({ ...spiSections, external: checked })} /><Text type="secondary">外部插件</Text>
        <Switch checked={spiSections.debug} onChange={checked => updateSpiSections({ ...spiSections, debug: checked })} /><Text type="secondary">调试验证</Text>
      </Space>
    </Card>
    <Row gutter={[16, 16]} className="metrics">
      <Col xs={24} sm={8}><Card bordered={false} className={`metric-link ${!spiSections.services ? 'metric-disabled' : ''}`} onClick={() => focusSpiSection('services')}><Statistic title="已注册 SPI 服务" value={spi.services?.length || 0} prefix={<ApiOutlined />} /><Text type="secondary">查看服务注册表</Text></Card></Col>
      <Col xs={24} sm={8}><Card bordered={false} className={`metric-link ${!spiSections.providers ? 'metric-disabled' : ''}`} onClick={() => focusSpiSection('providers')}><Statistic title="握手 Provider" value={spi.handshakeProviders?.length || 0} prefix={<SettingOutlined />} /><Text type="secondary">查看握手与认证</Text></Card></Col>
      <Col xs={24} sm={8}><Card bordered={false} className={`metric-link ${!spiSections.external ? 'metric-disabled' : ''}`} onClick={() => focusSpiSection('external')}><Statistic title="外部插件" value={external.plugins?.length || 0} prefix={<CloudServerOutlined />} /><Text type="secondary">查看插件状态</Text></Card></Col>
    </Row>
    {Object.keys(external.errors || {}).length > 0 && <Alert type="error" showIcon message="外部插件发现错误" description={Object.entries(external.errors).map(([jar, reason]) => <Space key={jar} direction="vertical"><Text>{jar}: {reason}</Text><Button size="small" icon={<PlusOutlined />} onClick={() => enableExternalPlugin(jar)}>尝试启用</Button></Space>)} />}
    {pluginValidation && <Alert className="plugin-validation" type={pluginValidation.dependenciesSatisfied && !pluginValidation.duplicatePluginId ? 'success' : 'warning'} showIcon message={`${pluginValidation.pluginId} JAR 验证${pluginValidation.dependenciesSatisfied && !pluginValidation.duplicatePluginId ? '通过' : '需处理'}`} description={<Space direction="vertical"><Text>版本：{pluginValidation.version}，SPI 服务：{pluginValidation.services?.length || 0} 个</Text>{pluginValidation.missingDependencies?.length > 0 && <Text type="danger">缺少依赖：{pluginValidation.missingDependencies.join(', ')}</Text>}{pluginValidation.duplicatePluginId && <Text type="danger">插件 ID 已存在</Text>}</Space>} closable onClose={() => setPluginValidation(null)} />}
    <div className="spi-sections">
      {spiSections.services && <Card ref={node => { spiRefs.current.services = node; }} className={`dashboard-section ${activeSpiSection === 'services' ? 'dashboard-section-active' : ''}`} title="SPI 服务注册表" bordered={false}><Table rowKey={item => `${item.serviceType}:${item.serviceId}`} columns={spiColumns} dataSource={spi.services || []} pagination={false} locale={{ emptyText: <Empty description="暂无 SPI 服务" /> }} scroll={{ x: 900 }} /></Card>}
      {spiSections.providers && <Card ref={node => { spiRefs.current.providers = node; }} className={`dashboard-section ${activeSpiSection === 'providers' ? 'dashboard-section-active' : ''}`} title="握手与认证 Provider" bordered={false}>
        <Descriptions size="small" column={{ xs: 1, sm: 2 }} items={[
          { key: 'handshake', label: 'Handshake', children: (spi.handshakeProviders || []).map(item => `${item.id} [${(item.protocols || []).join(', ')}]`).join('; ') || '-' },
          { key: 'auth', label: 'Authenticator', children: (spi.authenticators || []).map(item => item.id).join(', ') || '-' }
        ]} />
      </Card>}
      {spiSections.external && <Card ref={node => { spiRefs.current.external = node; }} className={`dashboard-section ${activeSpiSection === 'external' ? 'dashboard-section-active' : ''}`} title="外部插件" bordered={false} extra={<Tag color={external.enabled ? 'green' : 'default'}>{external.enabled ? '已启用' : '未启用'}</Tag>}>
        <Table rowKey="pluginId" columns={externalPluginColumns} dataSource={external.plugins || []} pagination={false} locale={{ emptyText: <Empty description="暂无外部插件" /> }} scroll={{ x: 900 }} />
      </Card>}
      {spiSections.debug && <Card ref={node => { spiRefs.current.debug = node; }} className={`dashboard-section ${activeSpiSection === 'debug' ? 'dashboard-section-active' : ''}`} title="握手 SPI 调试验证" bordered={false}>
        <Row gutter={[12, 12]}>
          {['protocolId', 'transportId', 'remoteAddress', 'handshakeProviderId', 'encoding'].map(key => <Col xs={24} sm={12} lg={key === 'handshakeProviderId' ? 8 : 4} key={key}><Input addonBefore={key} value={debugRequest[key]} onChange={event => setDebugRequest({ ...debugRequest, [key]: event.target.value })} /></Col>)}
          <Col xs={24}><Input.TextArea rows={3} addonBefore="payload" value={debugRequest.payload} onChange={event => setDebugRequest({ ...debugRequest, payload: event.target.value })} /></Col>
          <Col xs={24}><Space><Button type="primary" icon={<ThunderboltOutlined />} loading={debugging} onClick={debugHandshake}>执行握手验证</Button>{debugResult && <Button onClick={() => setDebugResult(null)}>清除结果</Button>}</Space></Col>
          {debugResult && <Col xs={24}><pre className="spi-debug-result">{JSON.stringify(debugResult, null, 2)}</pre></Col>}
        </Row>
      </Card>}
    </div>
  </Content>;

  return <Layout className="app-shell">
    <Sider width={244} className="app-sider">
      <div className="brand"><div className="brand-mark"><DashboardOutlined /></div><div><b>IoT Gateway</b><span>Control Center</span></div></div>
      <div className={`nav-item ${view === 'dashboard' ? 'active' : ''}`} onClick={() => setView('dashboard')}><DashboardOutlined /> 运行监控</div>
      <div className={`nav-item ${view === 'devices' ? 'active' : ''}`} onClick={() => setView('devices')}><ApiOutlined /> 设备接入</div>
      <div className="nav-item"><ClusterOutlined /> 节点集群</div>
      <div className={`nav-item ${view === 'configuration' ? 'active' : ''}`} onClick={() => setView('configuration')}><SettingOutlined /> 配置管理</div>
      <div className={`nav-item ${view === 'scripts' ? 'active' : ''}`} onClick={() => setView('scripts')}><FileTextOutlined /> 脚本编排</div>
      <div className={`nav-item ${view === 'plugins' ? 'active' : ''}`} onClick={() => setView('plugins')}><ThunderboltOutlined /> SPI 插件</div>
    </Sider>
    <Layout>
      <Header className="topbar"><Space><CloudServerOutlined /><Text strong>网关运行监控</Text><Tag color="cyan">DEV</Tag></Space><Space><Text type="secondary">自动刷新 10s</Text><ReloadOutlined onClick={load} className="clickable"/></Space></Header>
      {view === 'devices' ? devicesView : view === 'configuration' ? configurationView : view === 'scripts' ? scriptsView : view === 'plugins' ? pluginView : <Content className="content">
        <div className="page-heading"><div><Text className="eyebrow">MAGIC API IOT PLUGINS</Text><Title level={2}>Gateway Overview</Title><Text type="secondary">观察当前 Spring 容器中的 IoT 插件、连接能力与运行健康度</Text></div><Space wrap><Badge status={status.status === 'UP' ? 'success' : 'error'} text={status.status === 'UP' ? '网关在线' : '连接异常'} /><Text type="secondary">{updatedAt ? `更新于 ${updatedAt.toLocaleTimeString()}` : '等待数据'}</Text></Space></div>
        {error && <Alert type="warning" showIcon message="无法读取网关状态" description={error} closable onClose={() => setError('')} />}
        <Card className="dashboard-controls" bordered={false} size="small">
          <Space wrap>
            <Text strong>显示内容</Text>
            <Switch checked={dashboardSections.providers} onChange={checked => updateDashboardSections({ ...dashboardSections, providers: checked })} />
            <Text type="secondary">Provider 健康</Text>
            <Switch checked={dashboardSections.components} onChange={checked => updateDashboardSections({ ...dashboardSections, components: checked })} />
            <Text type="secondary">组件清单</Text>
            <Switch checked={dashboardSections.summary} onChange={checked => updateDashboardSections({ ...dashboardSections, summary: checked })} />
            <Text type="secondary">运行摘要</Text>
          </Space>
        </Card>
        <Row gutter={[16, 16]} className="metrics">
          <Col xs={24} sm={12} lg={6}><Card bordered={false} className={`metric-link ${!dashboardSections.summary ? 'metric-disabled' : ''}`} onClick={() => focusDashboardSection('summary')}><Statistic title="网关状态" value={status.status === 'UP' ? 'ONLINE' : status.status} prefix={<CheckCircleOutlined />} valueStyle={{ color: status.status === 'UP' ? '#16a34a' : '#dc2626' }}/><Text type="secondary">查看运行摘要</Text></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false} className={`metric-link ${!dashboardSections.components ? 'metric-disabled' : ''}`} onClick={() => focusDashboardSection('components')}><Statistic title="IoT 组件" value={components.length} prefix={<ThunderboltOutlined />} suffix="个"/><Text type="secondary">查看组件清单</Text></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false} className={`metric-link ${!dashboardSections.providers ? 'metric-disabled' : ''}`} onClick={() => focusDashboardSection('providers')}><Statistic title="Provider 健康" value={healthyProviderCount} prefix={<HeartOutlined />} suffix={` / ${providers.length}`}/><Progress percent={providers.length ? Math.round(healthyProviderCount / providers.length * 100) : 0} showInfo={false} strokeColor={healthyProviderCount === providers.length ? '#16a34a' : '#dc2626'}/><Text type="secondary">查看健康探测</Text></Card></Col>
          <Col xs={24} sm={12} lg={6}><Card bordered={false} className={`metric-link ${!dashboardSections.summary ? 'metric-disabled' : ''}`} onClick={() => focusDashboardSection('summary')}><Statistic title="TCP 活跃连接" value={transport?.activeConnections || 0} prefix={<DatabaseOutlined />} suffix="条"/><Text type="secondary">查看运行摘要</Text></Card></Col>
        </Row>
        {dashboardSections.providers && <Card ref={node => { dashboardRefs.current.providers = node; }} className={`provider-health dashboard-section ${activeDashboardSection === 'providers' ? 'dashboard-section-active' : ''}`} title={<Space><HeartOutlined />Provider 健康探测</Space>} extra={<Tag color={healthyProviderCount === providers.length ? 'green' : 'red'}>{healthyProviderCount} / {providers.length} UP</Tag>} bordered={false}>
          <Table rowKey={item => `${item.providerType}:${item.providerId}`} columns={providerColumns} dataSource={providers} pagination={false} locale={{ emptyText: <Empty description="当前没有启用 Provider 探针" /> }} scroll={{ x: 840 }}/>
        </Card>}
        <Row gutter={[16, 16]}>
          {dashboardSections.components && <Col xs={24} lg={16}><Card ref={node => { dashboardRefs.current.components = node; }} className={`dashboard-section ${activeDashboardSection === 'components' ? 'dashboard-section-active' : ''}`} title={<Space><ApiOutlined />组件清单</Space>} extra={<Tag color="green">{activeCount} UP</Tag>} bordered={false}><Table rowKey="id" columns={columns} dataSource={components} pagination={{ pageSize: 8 }} locale={{ emptyText: <Empty description="暂无组件探针数据" /> }} scroll={{ x: 680 }}/></Card></Col>}
          {dashboardSections.summary && <Col xs={24} lg={dashboardSections.components ? 8 : 24}><div ref={node => { dashboardRefs.current.summary = node; }} className={`dashboard-section ${activeDashboardSection === 'summary' ? 'dashboard-section-active' : ''}`}><Card title={<Space><CloudServerOutlined />运行时摘要</Space>} bordered={false}><div className="summary-list"><div><span>设备注册</span><b>{status.deviceRegistry}</b></div><div><span>会话仓库</span><b>{status.sessionRepository}</b></div><div><span>消息总线</span><b>{status.messageBus}</b></div><div><span>协议</span><b>{runtime.protocol?.protocolIds?.join(', ') || '-'}</b></div><div><span>TCP 监听</span><b>{transportTarget(transport) || '-'}</b></div><div><span>接收/发布</span><b>{runtime.protocol?.receivedFrames || 0} / {runtime.protocol?.publishedMessages || 0}</b></div><div><span>流量</span><b>{transport?.receivedBytes || 0} B in / {transport?.sentBytes || 0} B out</b></div><div><span>健康入口</span><Tag color="blue">Actuator</Tag></div></div></Card><Card className="hint-card" bordered={false}><Text type="secondary">Provider 探针结果缓存 10 秒，与 Actuator 共享。Raw TCP 使用换行符分帧，当前连接身份为临时 channel ID。</Text></Card></div></Col>}
        </Row>
      </Content>}
    </Layout>
  </Layout>;
}

createRoot(document.getElementById('root')).render(<ConfigProvider theme={{ token: { colorPrimary: '#0f766e', borderRadius: 8, colorBgLayout: '#f4f7f8' } }}><App /></ConfigProvider>);
