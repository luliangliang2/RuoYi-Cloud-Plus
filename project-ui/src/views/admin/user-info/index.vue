<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="昵称" prop="nick">
        <el-input
          v-model="queryParams.nick"
          placeholder="请输入昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入手机号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="影响力" prop="effectCount">
        <el-input
          v-model="queryParams.effectCount"
          placeholder="请输入影响力"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="访客数量" prop="visitorCount">
        <el-input
          v-model="queryParams.visitorCount"
          placeholder="请输入访客数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司" prop="company">
        <el-input
          v-model="queryParams.company"
          placeholder="请输入公司"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职位" prop="position">
        <el-input
          v-model="queryParams.position"
          placeholder="请输入职位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="自我介绍" prop="selfIntroduction">
        <el-input
          v-model="queryParams.selfIntroduction"
          placeholder="请输入自我介绍"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职业方向" prop="careerDirection">
        <el-input
          v-model="queryParams.careerDirection"
          placeholder="请输入职业方向"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在位置" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入所在位置"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="家乡" prop="hometown">
        <el-input
          v-model="queryParams.hometown"
          placeholder="请输入家乡"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="星座" prop="constellation">
        <el-select v-model="queryParams.constellation" placeholder="请选择星座" clearable>
          <el-option
            v-for="dict in dict.type.constellation"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
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
      <el-form-item label="启用状态" prop="deleted">
        <el-select v-model="queryParams.deleted" placeholder="请选择启用状态" clearable>
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
          v-hasPermi="['admin:user-info:add']"
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
          v-hasPermi="['admin:user-info:edit']"
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
          v-hasPermi="['admin:user-info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:user-info:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" v-if="true"/>
      <el-table-column label="姓名" align="center" prop="name" />
      <el-table-column label="昵称" align="center" prop="nick" />
      <el-table-column label="手机号码" align="center" prop="phone" />
      <el-table-column label="影响力" align="center" prop="effectCount" />
      <el-table-column label="访客数量" align="center" prop="visitorCount" />
      <el-table-column label="公司" align="center" prop="company" />
      <el-table-column label="职位" align="center" prop="position" />
      <el-table-column label="自我介绍" align="center" prop="selfIntroduction" />
      <el-table-column label="职业方向" align="center" prop="careerDirection" />
      <el-table-column label="所在位置" align="center" prop="location" />
      <el-table-column label="家乡" align="center" prop="hometown" />
      <el-table-column label="星座" align="center" prop="constellation">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.constellation" :value="scope.row.constellation"/>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="搜索值" align="center" prop="searchValue" />
      <el-table-column label="启用状态" align="center" prop="deleted">
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
            v-hasPermi="['admin:user-info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:user-info:remove']"
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

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nick">
          <el-input v-model="form.nick" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="头像">
          <image-upload v-model="form.avatar"/>
        </el-form-item>
        <el-form-item label="影响力" prop="effectCount">
          <el-input v-model="form.effectCount" placeholder="请输入影响力" />
        </el-form-item>
        <el-form-item label="访客数量" prop="visitorCount">
          <el-input v-model="form.visitorCount" placeholder="请输入访客数量" />
        </el-form-item>
        <el-form-item label="公司" prop="company">
          <el-input v-model="form.company" placeholder="请输入公司" />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position" placeholder="请输入职位" />
        </el-form-item>
        <el-form-item label="自我介绍" prop="selfIntroduction">
          <el-input v-model="form.selfIntroduction" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="职业方向" prop="careerDirection">
          <el-input v-model="form.careerDirection" placeholder="请输入职业方向" />
        </el-form-item>
        <el-form-item label="所在位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入所在位置" />
        </el-form-item>
        <el-form-item label="家乡" prop="hometown">
          <el-input v-model="form.hometown" placeholder="请输入家乡" />
        </el-form-item>
        <el-form-item label="星座" prop="constellation">
          <el-select v-model="form.constellation" placeholder="请选择星座">
            <el-option
              v-for="dict in dict.type.constellation"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="搜索值" prop="searchValue">
          <el-input v-model="form.searchValue" placeholder="请输入搜索值" />
        </el-form-item>
        <el-form-item label="启用状态" prop="deleted">
          <el-select v-model="form.deleted" placeholder="请选择启用状态">
            <el-option
              v-for="dict in dict.type.deleted"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
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
import { listUserInfo, getUserInfo, delUserInfo, addUserInfo, updateUserInfo } from "@/api/admin/user-info";

export default {
  name: "UserInfo",
  dicts: ['deleted', 'constellation'],
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
      // 用户信息表格数据
      userInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        nick: undefined,
        phone: undefined,
        avatar: undefined,
        effectCount: undefined,
        visitorCount: undefined,
        company: undefined,
        position: undefined,
        selfIntroduction: undefined,
        careerDirection: undefined,
        location: undefined,
        hometown: undefined,
        constellation: undefined,
        email: undefined,
        searchValue: undefined,
        deleted: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "序号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        nick: [
          { required: true, message: "昵称不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "手机号码不能为空", trigger: "blur" }
        ],
        avatar: [
          { required: true, message: "头像不能为空", trigger: "blur" }
        ],
        effectCount: [
          { required: true, message: "影响力不能为空", trigger: "blur" }
        ],
        visitorCount: [
          { required: true, message: "访客数量不能为空", trigger: "blur" }
        ],
        company: [
          { required: true, message: "公司不能为空", trigger: "blur" }
        ],
        position: [
          { required: true, message: "职位不能为空", trigger: "blur" }
        ],
        selfIntroduction: [
          { required: true, message: "自我介绍不能为空", trigger: "blur" }
        ],
        careerDirection: [
          { required: true, message: "职业方向不能为空", trigger: "blur" }
        ],
        location: [
          { required: true, message: "所在位置不能为空", trigger: "blur" }
        ],
        hometown: [
          { required: true, message: "家乡不能为空", trigger: "blur" }
        ],
        constellation: [
          { required: true, message: "星座不能为空", trigger: "change" }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" }
        ],
        searchValue: [
          { required: true, message: "搜索值不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "启用状态不能为空", trigger: "change" }
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
    /** 查询用户信息列表 */
    getList() {
      this.loading = true;
      listUserInfo(this.queryParams).then(response => {
        this.userInfoList = response.rows;
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
        name: undefined,
        nick: undefined,
        phone: undefined,
        avatar: undefined,
        effectCount: undefined,
        visitorCount: undefined,
        company: undefined,
        position: undefined,
        selfIntroduction: undefined,
        careerDirection: undefined,
        location: undefined,
        hometown: undefined,
        constellation: undefined,
        email: undefined,
        searchValue: undefined,
        deleted: undefined,
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
      this.title = "添加用户信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getUserInfo(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改用户信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateUserInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delUserInfo(ids);
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
      this.download('admin/user-info/export', {
        ...this.queryParams
      }, `user-info_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
