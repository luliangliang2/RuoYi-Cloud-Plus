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
      <el-form-item label="支付方式" prop="fromId">
        <el-input
          v-model="queryParams.fromId"
          placeholder="请输入支付方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接收方" prop="toId">
        <el-input
          v-model="queryParams.toId"
          placeholder="请输入接收方"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易金额" prop="money">
        <el-input
          v-model="queryParams.money"
          placeholder="请输入交易金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="备注信息" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注信息"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择交易时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="收款时间" prop="fetchTime">
        <el-date-picker clearable
          v-model="queryParams.fetchTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择收款时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="对账时间" prop="checkTime">
        <el-date-picker clearable
          v-model="queryParams.checkTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择对账时间">
        </el-date-picker>
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
          v-hasPermi="['admin:record:add']"
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
          v-hasPermi="['admin:record:edit']"
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
          v-hasPermi="['admin:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="自增ID" align="center" prop="id" v-if="true"/>
      <el-table-column label="交易流水" align="center" prop="number" />
      <el-table-column label="支付方式" align="center" prop="fromId" />
      <el-table-column label="接收方" align="center" prop="toId" />
      <el-table-column label="交易类型" align="center" prop="type" />
      <el-table-column label="交易金额" align="center" prop="money" />
      <el-table-column label="支付方式" align="center" prop="payType" />
      <el-table-column label="备注信息" align="center" prop="remark" />
      <el-table-column label="支付状态" align="center" prop="payStatus" />
      <el-table-column label="交易时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="收款状态" align="center" prop="fetchStatus" />
      <el-table-column label="收款时间" align="center" prop="fetchTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.fetchTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="对账状态" align="center" prop="checkStatus" />
      <el-table-column label="对账时间" align="center" prop="checkTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.checkTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" align="center" prop="deleted" />
      <el-table-column label="搜索值" align="center" prop="searchValue" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:record:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:record:remove']"
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

    <!-- 添加或修改钱包交易记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="交易流水" prop="number">
          <el-input v-model="form.number" placeholder="请输入交易流水" />
        </el-form-item>
        <el-form-item label="支付方式" prop="fromId">
          <el-input v-model="form.fromId" placeholder="请输入支付方式" />
        </el-form-item>
        <el-form-item label="接收方" prop="toId">
          <el-input v-model="form.toId" placeholder="请输入接收方" />
        </el-form-item>
        <el-form-item label="交易金额" prop="money">
          <el-input v-model="form.money" placeholder="请输入交易金额" />
        </el-form-item>
        <el-form-item label="备注信息" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注信息" />
        </el-form-item>
        <el-form-item label="交易时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择交易时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="收款时间" prop="fetchTime">
          <el-date-picker clearable
            v-model="form.fetchTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择收款时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="对账时间" prop="checkTime">
          <el-date-picker clearable
            v-model="form.checkTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择对账时间">
          </el-date-picker>
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
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/admin/record";

export default {
  name: "Record",
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
      // 钱包交易记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        number: undefined,
        fromId: undefined,
        toId: undefined,
        type: undefined,
        money: undefined,
        payType: undefined,
        remark: undefined,
        payStatus: undefined,
        payTime: undefined,
        fetchStatus: undefined,
        fetchTime: undefined,
        checkStatus: undefined,
        checkTime: undefined,
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
        fromId: [
          { required: true, message: "支付方式不能为空", trigger: "blur" }
        ],
        toId: [
          { required: true, message: "接收方不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "交易类型不能为空", trigger: "change" }
        ],
        money: [
          { required: true, message: "交易金额不能为空", trigger: "blur" }
        ],
        payType: [
          { required: true, message: "支付方式不能为空", trigger: "change" }
        ],
        remark: [
          { required: true, message: "备注信息不能为空", trigger: "blur" }
        ],
        payStatus: [
          { required: true, message: "支付状态不能为空", trigger: "blur" }
        ],
        payTime: [
          { required: true, message: "交易时间不能为空", trigger: "blur" }
        ],
        fetchStatus: [
          { required: true, message: "收款状态不能为空", trigger: "blur" }
        ],
        fetchTime: [
          { required: true, message: "收款时间不能为空", trigger: "blur" }
        ],
        checkStatus: [
          { required: true, message: "对账状态不能为空", trigger: "blur" }
        ],
        checkTime: [
          { required: true, message: "对账时间不能为空", trigger: "blur" }
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
    /** 查询钱包交易记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
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
        fromId: undefined,
        toId: undefined,
        type: undefined,
        money: undefined,
        payType: undefined,
        remark: undefined,
        payStatus: 0,
        payTime: undefined,
        fetchStatus: 0,
        fetchTime: undefined,
        checkStatus: 0,
        checkTime: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined,
        deleted: undefined,
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
      this.title = "添加钱包交易记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getRecord(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改钱包交易记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRecord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除钱包交易记录编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delRecord(ids);
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
      this.download('admin/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
