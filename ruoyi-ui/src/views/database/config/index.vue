<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="数据库名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入数据库名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="数据库用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入数据库用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable size="small">
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['database:config:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['database:config:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['database:config:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['database:config:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="数据库主键" align="center" prop="dbId" v-if="true"/>
      <el-table-column label="数据库连接名称" align="center" prop="name"/>
      <el-table-column label="数据库连接地址" align="center" prop="url"/>
      <el-table-column label="数据库用户名" align="center" prop="username"/>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="0"
            inactive-value="1"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180"/>
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleTestConnectDatabase(scope.row)"
            v-hasPermi="['database:config:edit']"
          >测试连接
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['database:config:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['database:config:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据库配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="数据库连接名称" prop="name">
          <el-input v-model.trim="form.name" placeholder="请输入数据库名称"/>
        </el-form-item>
        <el-form-item label="数据库连接地址" prop="url">
          <el-input v-model.trim="form.url" type="textarea" :rows="3" placeholder="请输入数据库连接地址"/>
        </el-form-item>
        <el-form-item label="数据库用户名" prop="username">
          <el-input v-model.trim="form.username" placeholder="请输入数据库用户名"/>
        </el-form-item>
        <el-form-item label="数据库密码" prop="password">
          <el-input
            show-password
            v-model.trim="form.password"
            type="password"
            auto-complete="off"
            placeholder="请输入数据库密码"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注内容"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listConfig,
  getConfig,
  delConfig,
  addConfig,
  updateConfig,
  changeDatabaseConfigStatus,
  testDatabaseConnection
} from '@/api/database/config'

export default {
  name: 'DatabaseConfig',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据库配置表格数据
      configList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        url: undefined,
        username: undefined,
        password: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        dbId: [
          { required: true, message: '数据库主键不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '数据库名称不能为空', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '数据库链接地址不能为空', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '数据库用户名不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '数据库密码不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询数据库配置列表 */
    getList() {
      this.loading = true
      listConfig(this.queryParams).then(response => {
        this.configList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        dbId: undefined,
        name: undefined,
        url: undefined,
        username: undefined,
        password: undefined,
        status: '1',
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        remark: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dbId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加数据库配置'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      if (row.status === '0') {
        this.$modal.msgWarning('请停用后再修改')
        return
      }
      this.loading = true
      this.reset()
      const dbId = row.dbId || this.ids
      getConfig(dbId).then(response => {
        this.loading = false
        this.form = response.data
        this.open = true
        this.title = '修改数据库配置'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.dbId != null) {
            updateConfig(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addConfig(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const dbIds = row.dbId || this.ids
      this.$modal.confirm('是否确认删除数据库配置编号为"' + dbIds + '"的数据项？').then(() => {
        this.loading = true
        return delConfig(dbIds)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      }).finally(() => {
        this.loading = false
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('database/config/export', {
        ...this.queryParams
      }, `config_${new Date().getTime()}.xlsx`)
    },

    /** 数据库配置状态修改 */
    handleStatusChange(row) {
      // 启用数据库配置前，先进行数据库连接测试
      if (row.status === '0') {
        this.handleTestConnectDatabase(row).then(res => {
          if (res) {
            this.statusChange(row)
          } else {
            row.status = row.status === '0' ? '1' : '0'
            this.$modal.notifyError('请修改为正确的数据库配置')
          }
        })
      } else {
        this.statusChange(row)
      }
    },
    /** 数据库配置状态修改 */
    statusChange(row) {
      let text = row.status === '0' ? '启用' : '停用'
      this.$modal.confirm('确认要"' + text + '" "' + row.name + '"配置吗?').then(() => {
        return changeDatabaseConfigStatus(row.dbId, row.status)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === '0' ? '1' : '0'
      })
    },

    // 测试数据库连接
    handleTestConnectDatabase(row) {
      const data = {
        dbId: row.dbId,
        name: row.name,
        url: row.url,
        username: row.username,
        password: row.password
      }
      return testDatabaseConnection(data).then(response => {
        if (response.code === 200) {
          this.$modal.msgSuccess('数据库连接成功')
          return true
        }
      }).catch(() => {
        return false
      })
    }
  }
}
</script>
