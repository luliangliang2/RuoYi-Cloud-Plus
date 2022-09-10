<template>
  <div class="app-container">
    <!-- 操作工作栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-plus" size="mini" v-hasPermi="['database:doc:export']" @click="handleExportHtml">导出 HTML</el-button>
        <el-button type="warning" plain icon="el-icon-plus" size="mini" v-hasPermi="['database:doc:export']" @click="handleExportWord">导出 Word</el-button>
        <el-button type="warning" plain icon="el-icon-plus" size="mini" v-hasPermi="['database:doc:export']" @click="handleExportMarkdown">导出 Markdown</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-s-operation"
          size="mini"
          @click="handleDatabaseConfig"
          v-hasPermi="['database:config:list']"
        >数据库配置管理</el-button>
      </el-col>
    </el-row>

    <!-- 展示文档 -->
    <div :style="'height:'+ height">
      <i-frame :src="src" />
    </div>
  </div>
</template>
<script>
import { exportHtml, exportWord, exportMarkdown} from "@/api/database/doc";
import iFrame from "@/components/iFrame";

export default {
  name: "DatabaseDoc",
  components: { iFrame },
  data() {
    return {
      height: document.documentElement.clientHeight - 94.5 + "px;",
      src: undefined,
    };
  },
  created() {
    // 打开加载层
    this.openLoading()
    // 加载 Html，进行预览
    exportHtml().then(response => {
      // 关闭加载层
      this.$modal.closeLoading();
      let blob = new Blob([response], {type : 'text/html'});
      this.src = window.URL.createObjectURL(blob);
    })
  },
  methods: {
    /** 处理导出 HTML */
    handleExportHtml() {
      exportHtml().then(response => {
        this.$download.html(response, '数据库文档.html');
      })
    },
    /** 处理导出 Word */
    handleExportWord() {
      exportWord().then(response => {
        this.$download.word(response, '数据库文档.doc');
      })
    },
    /** 处理导出 Markdown */
    handleExportMarkdown() {
      exportMarkdown().then(response => {
        this.$download.markdown(response, '数据库文档.md');
      })
    },
    /** 数据库配置列表查询 */
    handleDatabaseConfig() {
      this.$router.push({ path: '/tool/database-config'})
    },
    /** 打开数据库文档加载层 */
    openLoading() {
      this.$modal.loading("正在努力加载数据库文档，请稍候！");
    }
  }
};
</script>
