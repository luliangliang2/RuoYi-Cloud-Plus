<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="发送消息用户" prop="sendMessageId">
        <el-input
          v-model="queryParams.sendMessageId"
          placeholder="请输入发送消息用户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否由我发送" prop="sendFromMeStatus">
        <el-select v-model="queryParams.sendFromMeStatus" placeholder="请选择是否由我发送" clearable>
          <el-option
            v-for="dict in dict.type.yes_or_no"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="搜索值" prop="searchValue">
        <el-input
          v-model="queryParams.searchValue"
          placeholder="请输入搜索值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="逻辑删除" prop="deleted">
        <el-select v-model="queryParams.deleted" placeholder="请选择逻辑删除" clearable>
          <el-option
            v-for="dict in dict.type.deleted"
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
          v-hasPermi="['admin:user-communication-message:add']"
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
          v-hasPermi="['admin:user-communication-message:edit']"
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
          v-hasPermi="['admin:user-communication-message:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:user-communication-message:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userCommunicationMessageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" v-if="true"/>
      <el-table-column label="发送消息用户" align="center" prop="sendMessageId" />
      <el-table-column label="消息内容" align="center" prop="messageContent" />
      <el-table-column label="是否由我发送" align="center" prop="sendFromMeStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.yes_or_no" :value="scope.row.sendFromMeStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="逻辑删除" align="center" prop="deleted">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.deleted" :value="scope.row.deleted"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['admin:user-communication-message:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:user-communication-message:remove']"
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

    <!-- 添加或修改沟通消息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="发送消息用户" prop="sendMessageId">
          <el-input v-model="form.sendMessageId" placeholder="请输入发送消息用户" />
        </el-form-item>
        <el-form-item label="消息内容">
          <editor v-model="form.messageContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="是否由我发送">
          <el-radio-group v-model="form.sendFromMeStatus">
            <el-radio
              v-for="dict in dict.type.yes_or_no"
              :key="dict.value"
:label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="搜索值" prop="searchValue">
          <el-input v-model="form.searchValue" placeholder="请输入搜索值" />
        </el-form-item>
        <el-form-item label="逻辑删除">
          <el-radio-group v-model="form.deleted">
            <el-radio
              v-for="dict in dict.type.deleted"
              :key="dict.value"
:label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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
import { listUserCommunicationMessage, getUserCommunicationMessage, delUserCommunicationMessage, addUserCommunicationMessage, updateUserCommunicationMessage } from "@/api/admin/user-communication-message";

export default {
  name: "UserCommunicationMessage",
  dicts: ['yes_or_no', 'deleted'],
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
      // 沟通消息表格数据
      userCommunicationMessageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sendMessageId: undefined,
        messageContent: undefined,
        sendFromMeStatus: undefined,
        searchValue: undefined,
        deleted: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "ID不能为空", trigger: "blur" }
        ],
        sendMessageId: [
          { required: true, message: "发送消息用户不能为空", trigger: "blur" }
        ],
        messageContent: [
          { required: true, message: "消息内容不能为空", trigger: "blur" }
        ],
        sendFromMeStatus: [
          { required: true, message: "是否由我发送不能为空", trigger: "blur" }
        ],
        searchValue: [
          { required: true, message: "搜索值不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "逻辑删除不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建者不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        updateBy: [
          { required: true, message: "更新者不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询沟通消息列表 */
    getList() {
      this.loading = true;
      listUserCommunicationMessage(this.queryParams).then(response => {
        this.userCommunicationMessageList = response.rows;
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
        sendMessageId: undefined,
        messageContent: undefined,
        sendFromMeStatus: 0,
        searchValue: undefined,
        deleted: 0,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined
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
      this.title = "添加沟通消息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getUserCommunicationMessage(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改沟通消息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateUserCommunicationMessage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserCommunicationMessage(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除沟通消息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delUserCommunicationMessage(ids);
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
      this.download('admin/user-communication-message/export', {
        ...this.queryParams
      }, `user-communication-message_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
