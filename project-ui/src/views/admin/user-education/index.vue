<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户序号" prop="userInfoId">
        <el-input
          v-model="queryParams.userInfoId"
          placeholder="请输入用户序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学校名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入学校名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入学时间" prop="admissionTime">
        <el-date-picker clearable
          v-model="queryParams.admissionTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择入学时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="毕业时间" prop="graduationTime">
        <el-date-picker clearable
          v-model="queryParams.graduationTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择毕业时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="专业" prop="major">
        <el-input
          v-model="queryParams.major"
          placeholder="请输入专业"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文化程度" prop="geadeLevel">
        <el-select v-model="queryParams.geadeLevel" placeholder="请选择文化程度" clearable>
          <el-option
            v-for="dict in dict.type.grade_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="说明介绍" prop="introduction">
        <el-input
          v-model="queryParams.introduction"
          placeholder="请输入说明介绍"
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
          v-hasPermi="['admin:user-education:add']"
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
          v-hasPermi="['admin:user-education:edit']"
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
          v-hasPermi="['admin:user-education:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['admin:user-education:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userEducationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" v-if="true"/>
      <el-table-column label="用户序号" align="center" prop="userInfoId" />
      <el-table-column label="学校名称" align="center" prop="name" />
      <el-table-column label="入学时间" align="center" prop="admissionTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.admissionTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="毕业时间" align="center" prop="graduationTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.graduationTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="专业" align="center" prop="major" />
      <el-table-column label="文化程度" align="center" prop="geadeLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.grade_level" :value="scope.row.geadeLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="说明介绍" align="center" prop="introduction" />
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
            v-hasPermi="['admin:user-education:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['admin:user-education:remove']"
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

    <!-- 添加或修改学历对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户序号" prop="userInfoId">
          <el-input v-model="form.userInfoId" placeholder="请输入用户序号" />
        </el-form-item>
        <el-form-item label="学校名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入学校名称" />
        </el-form-item>
        <el-form-item label="入学时间" prop="admissionTime">
          <el-date-picker clearable
            v-model="form.admissionTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择入学时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduationTime">
          <el-date-picker clearable
            v-model="form.graduationTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择毕业时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="form.major" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item label="文化程度" prop="geadeLevel">
          <el-select v-model="form.geadeLevel" placeholder="请选择文化程度">
            <el-option
              v-for="dict in dict.type.grade_level"
              :key="dict.value"
              :label="dict.label"
:value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="说明介绍" prop="introduction">
          <el-input v-model="form.introduction" placeholder="请输入说明介绍" />
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
import { listUserEducation, getUserEducation, delUserEducation, addUserEducation, updateUserEducation } from "@/api/admin/user-education";

export default {
  name: "UserEducation",
  dicts: ['grade_level', 'deleted'],
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
      // 学历表格数据
      userEducationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userInfoId: undefined,
        name: undefined,
        admissionTime: undefined,
        graduationTime: undefined,
        major: undefined,
        geadeLevel: undefined,
        introduction: undefined,
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
        userInfoId: [
          { required: true, message: "用户序号不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "学校名称不能为空", trigger: "blur" }
        ],
        admissionTime: [
          { required: true, message: "入学时间不能为空", trigger: "blur" }
        ],
        graduationTime: [
          { required: true, message: "毕业时间不能为空", trigger: "blur" }
        ],
        major: [
          { required: true, message: "专业不能为空", trigger: "blur" }
        ],
        geadeLevel: [
          { required: true, message: "文化程度不能为空", trigger: "change" }
        ],
        introduction: [
          { required: true, message: "说明介绍不能为空", trigger: "blur" }
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
    /** 查询学历列表 */
    getList() {
      this.loading = true;
      listUserEducation(this.queryParams).then(response => {
        this.userEducationList = response.rows;
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
        userInfoId: undefined,
        name: undefined,
        admissionTime: undefined,
        graduationTime: undefined,
        major: undefined,
        geadeLevel: undefined,
        introduction: undefined,
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
      this.title = "添加学历";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getUserEducation(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改学历";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateUserEducation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addUserEducation(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除学历编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delUserEducation(ids);
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
      this.download('admin/user-education/export', {
        ...this.queryParams
      }, `user-education_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
