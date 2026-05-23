var MagicApiStartup = function(vue) {
  "use strict";
  return function(opt) {
    var request = opt.request;
    var i18n = opt.i18n;

    var localZhCN = {
      apiStartup: {
        name: "修改开机执行接口",
        title: "开机接口",
        placeholder: { name: "请输入名称", key: "请输入Key" },
        selected: "已选接口",
        empty: "暂无可选接口",
        enabled: "启用",
        apiList: "接口列表",
        description: "说明"
      }
    };
    var localEn = {
      apiStartup: {
        name: "Edit Startup APIs",
        title: "Startup APIs",
        placeholder: { name: "Name", key: "Key" },
        selected: "Selected APIs",
        empty: "No available API",
        enabled: "Enabled",
        apiList: "API List",
        description: "Description"
      }
    };
    i18n.add("zh-cn", localZhCN);
    i18n.add("en", localEn);

    var MagicApiStartupInfo = {
      name: "MagicApiStartupInfo",
      props: {
        info: Object
      },
      setup: function(props) {
        var format = vue.inject("i18n.format");
        var apiOptions = vue.ref([]);
        var loading = vue.ref(false);
        var loaded = vue.ref(false);

        var ensureInfo = function() {
          if (!props.info.apiIds) props.info.apiIds = [];
          if (props.info.enabled === void 0) props.info.enabled = true;
        };

        var normalizeTree = function(nodes, items, prefix) {
          (nodes || []).forEach(function(treeNode) {
            if (!treeNode) return;
            var node = treeNode.node || treeNode;
            var currentLabel = prefix ? prefix + "/" + (node.name || "") : (node.name || "");
            if (node.method && node.id) {
              items.push({ id: node.id, label: currentLabel, method: node.method });
            }
            if (treeNode.children && treeNode.children.length) {
              normalizeTree(treeNode.children, items, currentLabel);
            }
          });
        };

        var loadApis = function() {
          if (loading.value) return;
          loading.value = true;
          request.sendPost("/resource", {}).success(function(res) {
            var result = [];
            var payload = res && res.data ? res.data : res;
            var apiRoot = payload && payload.api;
            if (apiRoot && apiRoot.children) {
              normalizeTree(apiRoot.children, result, "");
            }
            apiOptions.value = result;
            loaded.value = true;
          }).end(function() {
            loading.value = false;
          });
        };

        var toggleApi = function(id, checked) {
          ensureInfo();
          var list = Array.isArray(props.info.apiIds) ? props.info.apiIds.slice() : [];
          var index = list.indexOf(id);
          if (checked && index === -1) list.push(id);
          else if (!checked && index > -1) list.splice(index, 1);
          props.info.apiIds = list;
        };

        var selectedApiLabels = vue.computed(function() {
          ensureInfo();
          return apiOptions.value.filter(function(item) {
            return props.info.apiIds.indexOf(item.id) > -1;
          }).map(function(item) {
            return "[" + item.method + "] " + item.label;
          }).join(", ");
        });

        vue.watch(function() { return props.info.name; }, function(val) {
          if ((!props.info.key || props.info.key === "") && val) {
            props.info.key = val.replace(/\s+/g, "-").toLowerCase();
          }
        }, { immediate: true });

        vue.onMounted(function() {
          ensureInfo();
          loadApis();
        });

        return function() {
          var children = [];
          
          children.push(vue.h("form", { class: "magic-api-startup-form" }, [
            // 名称
            vue.h("div", { class: "magic-form-row" }, [
              vue.h("label", null, "名称"),
              vue.h("input", {
                class: "magic-api-startup-input",
                value: props.info.name || "",
                onInput: function(event) { props.info.name = event.target.value; }
              })
            ]),
            // Key
            vue.h("div", { class: "magic-form-row" }, [
              vue.h("label", null, "Key"),
              vue.h("input", {
                class: "magic-api-startup-input",
                value: props.info.key || "",
                onInput: function(event) { props.info.key = event.target.value; }
              })
            ]),
            // 启用
            vue.h("div", { class: "magic-form-row" }, [
              vue.h("label", null, format("apiStartup.enabled")),
              vue.h("div", { class: "magic-api-startup-control-wrap" }, [
                vue.h("input", {
                  type: "checkbox",
                  class: "magic-api-startup-checkbox-main",
                  checked: props.info.enabled !== false,
                  onChange: function(event) { props.info.enabled = !!event.target.checked; }
                })
              ])
            ]),
            // 接口列表 (带 4 行滚动限制)
            vue.h("div", { class: "magic-form-row align-top" }, [
              vue.h("label", null, format("apiStartup.apiList")),
              vue.h("div", { class: "magic-api-startup-list-container" }, [
                apiOptions.value.length ? apiOptions.value.map(function(item) {
                  var checked = Array.isArray(props.info.apiIds) && props.info.apiIds.indexOf(item.id) > -1;
                  return vue.h("div", { class: "magic-api-startup-item-line", key: item.id }, [
                    vue.h("input", {
                      type: "checkbox",
                      checked: checked,
                      onChange: function(event) { toggleApi(item.id, !!event.target.checked); }
                    }),
                    vue.h("span", { class: "magic-api-startup-line-method" }, "[" + item.method + "]"),
                    vue.h("span", { class: "magic-api-startup-line-label" }, item.label)
                  ]);
                }) : [vue.h("div", { class: "magic-api-startup-empty" }, format("apiStartup.empty"))]
              ])
            ]),
            // 已选接口
            vue.h("div", { class: "magic-form-row align-top" }, [
              vue.h("label", null, format("apiStartup.selected")),
              vue.h("textarea", {
                class: "magic-api-startup-textarea magic-api-startup-textarea-readonly",
                readonly: true,
                value: selectedApiLabels.value || ""
              })
            ]),
            // 说明
            vue.h("div", { class: "magic-form-row align-top" }, [
              vue.h("label", null, format("apiStartup.description")),
              vue.h("textarea", {
                class: "magic-api-startup-textarea",
                value: props.info.description || "",
                onInput: function(event) { props.info.description = event.target.value; }
              })
            ])
          ]));

          if (!loaded.value && !loading.value) loadApis();

          var style = vue.h("style", null, "\n    .magic-api-startup-form { padding: 15px; }\n    .magic-form-row {\n      display: flex;\n      margin-bottom: 15px;\n      align-items: center;\n    }\n    .magic-form-row.align-top {\n      align-items: flex-start;\n    }\n    .magic-form-row label {\n      width: 100px;\n      flex-shrink: 0;\n      color: #333;\n      text-align: right;\n      margin-right: 15px;\n      font-size: 14px;\n    }\n    .magic-api-startup-input,\n    .magic-api-startup-list-container,\n    .magic-api-startup-textarea,\n    .magic-api-startup-control-wrap {\n      flex: 1;\n      min-width: 0;\n    }\n    .magic-api-startup-input {\n      padding: 6px 12px;\n      border: 1px solid #dcdfe6;\n      border-radius: 4px;\n    }\n    .magic-api-startup-checkbox-main {\n      width: 18px;\n      height: 18px;\n      cursor: pointer;\n    }\n    \n    /* 接口列表容器：限制高度，超过 4 行显示滚动条 */\n    .magic-api-startup-list-container {\n      border: 1px solid #ebeef5;\n      background: #fff;\n      border-radius: 4px;\n      max-height: 150px; /* 约 4 行高度 */\n      overflow-y: auto;\n    }\n    .magic-api-startup-item-line {\n      display: flex;\n      align-items: center;\n      padding: 8px 12px;\n      border-bottom: 1px solid #f0f0f0;\n    }\n    .magic-api-startup-item-line:last-child { border-bottom: none; }\n    .magic-api-startup-item-line input[type='checkbox'] {\n      margin-right: 12px;\n      cursor: pointer;\n    }\n    .magic-api-startup-line-method {\n      color: #409eff;\n      font-weight: bold;\n      font-size: 13px;\n      width: 60px;\n      flex-shrink: 0;\n    }\n    .magic-api-startup-line-label {\n      font-size: 13px;\n      color: #606266;\n      flex: 1;\n      white-space: nowrap;\n      overflow: hidden;\n      text-overflow: ellipsis;\n    }\n    \n    .magic-api-startup-textarea {\n      height: 90px;\n      padding: 8px 12px;\n      border: 1px solid #dcdfe6;\n      border-radius: 4px;\n      box-sizing: border-box;\n      resize: vertical;\n      font-family: inherit;\n    }\n    .magic-api-startup-textarea-readonly {\n      background-color: #f5f7fa;\n      color: #909399;\n    }\n    .magic-api-startup-empty { color: #999; padding: 20px; text-align: center; }\n  ");

          return vue.h("div", { class: "magic-api-startup-info" }, [style, children]);
        };
      }
    };

    return {
      datasources: [{
        type: "api-startup",
        title: "启动接口",
        name: i18n.format("apiStartup.name"),
        icon: "api",
        service: { requireScript: false, injectResources: function() {} },
        component: MagicApiStartupInfo
      }]
    };
  };
}(Vue);