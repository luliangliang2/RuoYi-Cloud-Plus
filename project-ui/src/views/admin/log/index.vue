<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="交易流水" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入交易流水"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动金额" prop="beforeMoney">
        <el-input
          v-model="queryParams.beforeMoney"
          placeholder="请输入变动金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变动金额" prop="afterMoney">
        <el-input
          v-model="queryParams.afterMoney"
          placeholder="请输入变动金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目标id" prop="targetId">
        <el-input
          v-model="queryParams.targetId"
          placeholder="请输入目标id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="deleted">
        <el-input
          v-model="queryParams.deleted"
          placeholder="请输入启用状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="搜索值" prop="searchValue">
        <el-input
          v-model="queryParams.searchValue"
          placeholder="请输入搜索值"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['admin:log:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['admin:log:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['admin:log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="自增ID" align="center" prop="id" v-if="true"/>
      <el-table-column label="交易流水" align="center" prop="number" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="变动金额" align="center" prop="beforeMoney" />
      <el-table-column label="变动金额" align="center" prop="afterMoney" />
      <el-table-column label="业务类型" align="center" prop="targetType" />
      <el-table-column label="目标id" align="center" prop="targetId" />
      <el-table-column label="处理状态" align="center" prop="status" />
      <el-table-column label="处理结果" align="center" prop="resultType" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="启用状态" align="center" prop="deleted" />
      <el-table-column label="搜索值" align="center" prop="searchValue" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:log:remove']"
          >删除</el-button>
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

    <!-- 添加或修改钱包变动日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="交易流水" prop="number">
          <el-input v-model="form.number" placeholder="请输入交易流水" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="变动金额" prop="beforeMoney">
          <el-input v-model="form.beforeMoney" placeholder="请输入变动金额" />
        </el-form-item>
        <el-form-item label="变动金额" prop="afterMoney">
          <el-input v-model="form.afterMoney" placeholder="请输入变动金额" />
        </el-form-item>
        <el-form-item label="目标id" prop="targetId">
          <el-input v-model="form.targetId" placeholder="请输入目标id" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="启用状态" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入启用状态" />
        </el-form-item>
        <el-form-item label="搜索值" prop="searchValue">
          <el-input v-model="form.searchValue" placeholder="请输入搜索值" />
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
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/admin/log";

export default {
  name: "Log",
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
      // 钱包变动日志表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        number: undefined,
        userId: undefined,
        beforeMoney: undefined,
        afterMoney: undefined,
        targetType: undefined,
        targetId: undefined,
        status: undefined,
        resultType: undefined,
        remark: undefined,
        deleted: undefined,
        searchValue: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "自增ID不能为空", trigger: "blur" }
        ],
        number: [
          { required: true, message: "交易流水不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        beforeMoney: [
          { required: true, message: "变动金额不能为空", trigger: "blur" }
        ],
        afterMoney: [
          { required: true, message: "变动金额不能为空", trigger: "blur" }
        ],
        targetType: [
          { required: true, message: "业务类型不能为空", trigger: "change" }
        ],
        targetId: [
          { required: true, message: "目标id不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "处理状态不能为空", trigger: "blur" }
        ],
        resultType: [
          { required: true, message: "处理结果不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "备注不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "启用状态不能为空", trigger: "blur" }
        ],
        searchValue: [
          { required: true, message: "搜索值不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询钱包变动日志列表 */
    getList() {
      this.loading = true;
      listLog(this.queryParams).then(response => {
        this.logList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        number: undefined,
        userId: undefined,
        beforeMoney: undefined,
        afterMoney: undefined,
        targetType: undefined,
        targetId: undefined,
        status: 0,
        resultType: undefined,
        remark: undefined,
        createTime: undefined,
        updateTime: undefined,
        deleted: undefined,
        createBy: undefined,
        updateBy: undefined,
        searchValue: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加钱包变动日志";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getLog(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改钱包变动日志";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addLog(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除钱包变动日志编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delLog(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('admin/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
