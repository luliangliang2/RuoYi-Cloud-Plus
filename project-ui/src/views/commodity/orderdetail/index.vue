<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单id" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品id" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入商品id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品图片" prop="productPic">
        <el-input
          v-model="queryParams.productPic"
          placeholder="请输入商品图片"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input
          v-model="queryParams.productName"
          placeholder="请输入商品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品价格" prop="productPrice">
        <el-input
          v-model="queryParams.productPrice"
          placeholder="请输入商品价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品数量" prop="productQuantity">
        <el-input
          v-model="queryParams.productQuantity"
          placeholder="请输入商品数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商品类型id" prop="productCategoryId">
        <el-input
          v-model="queryParams.productCategoryId"
          placeholder="请输入商品类型id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单总金额" prop="totalPrice">
        <el-input
          v-model="queryParams.totalPrice"
          placeholder="请输入订单总金额"
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
          v-hasPermi="['commodity:orderdetail:add']"
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
          v-hasPermi="['commodity:orderdetail:edit']"
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
          v-hasPermi="['commodity:orderdetail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['commodity:orderdetail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderdetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" v-if="true"/>
      <el-table-column label="订单id" align="center" prop="orderId" />
      <el-table-column label="订单编号" align="center" prop="orderNum" />
      <el-table-column label="商品id" align="center" prop="productId" />
      <el-table-column label="商品图片" align="center" prop="productPic" />
      <el-table-column label="商品名称" align="center" prop="productName" />
      <el-table-column label="商品价格" align="center" prop="productPrice" />
      <el-table-column label="商品数量" align="center" prop="productQuantity" />
      <el-table-column label="商品类型id" align="center" prop="productCategoryId" />
      <el-table-column label="订单总金额" align="center" prop="totalPrice" />
      <el-table-column label="逻辑删除;0未删除1已删除" align="center" prop="deleted" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['commodity:orderdetail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['commodity:orderdetail:remove']"
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

    <!-- 添加或修改订单详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单id" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单id" />
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="商品id" prop="productId">
          <el-input v-model="form.productId" placeholder="请输入商品id" />
        </el-form-item>
        <el-form-item label="商品图片" prop="productPic">
          <el-input v-model="form.productPic" placeholder="请输入商品图片" />
        </el-form-item>
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品价格" prop="productPrice">
          <el-input v-model="form.productPrice" placeholder="请输入商品价格" />
        </el-form-item>
        <el-form-item label="商品数量" prop="productQuantity">
          <el-input v-model="form.productQuantity" placeholder="请输入商品数量" />
        </el-form-item>
        <el-form-item label="商品类型id" prop="productCategoryId">
          <el-input v-model="form.productCategoryId" placeholder="请输入商品类型id" />
        </el-form-item>
        <el-form-item label="订单总金额" prop="totalPrice">
          <el-input v-model="form.totalPrice" placeholder="请输入订单总金额" />
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
import { listOrderdetail, getOrderdetail, delOrderdetail, addOrderdetail, updateOrderdetail } from "@/api/commodity/orderdetail";

export default {
  name: "Orderdetail",
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
      // 订单详情表格数据
      orderdetailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: undefined,
        orderNum: undefined,
        productId: undefined,
        productPic: undefined,
        productName: undefined,
        productPrice: undefined,
        productQuantity: undefined,
        productCategoryId: undefined,
        totalPrice: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "id不能为空", trigger: "blur" }
        ],
        orderId: [
          { required: true, message: "订单id不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "订单编号不能为空", trigger: "blur" }
        ],
        productId: [
          { required: true, message: "商品id不能为空", trigger: "blur" }
        ],
        productPic: [
          { required: true, message: "商品图片不能为空", trigger: "blur" }
        ],
        productName: [
          { required: true, message: "商品名称不能为空", trigger: "blur" }
        ],
        productPrice: [
          { required: true, message: "商品价格不能为空", trigger: "blur" }
        ],
        productQuantity: [
          { required: true, message: "商品数量不能为空", trigger: "blur" }
        ],
        productCategoryId: [
          { required: true, message: "商品类型id不能为空", trigger: "blur" }
        ],
        totalPrice: [
          { required: true, message: "订单总金额不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "逻辑删除;0未删除1已删除不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单详情列表 */
    getList() {
      this.loading = true;
      listOrderdetail(this.queryParams).then(response => {
        this.orderdetailList = response.rows;
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
        orderId: undefined,
        orderNum: undefined,
        productId: undefined,
        productPic: undefined,
        productName: undefined,
        productPrice: undefined,
        productQuantity: undefined,
        productCategoryId: undefined,
        totalPrice: undefined,
        deleted: undefined,
        createTime: undefined,
        updateTime: undefined
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
      this.title = "添加订单详情";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getOrderdetail(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改订单详情";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateOrderdetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOrderdetail(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除订单详情编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delOrderdetail(ids);
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
      this.download('commodity/orderdetail/export', {
        ...this.queryParams
      }, `orderdetail_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
