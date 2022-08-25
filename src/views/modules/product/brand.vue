<template>
  <div class="mod-config">
    <el-form :inline="true" v-model="formData" label-position="left" label-width="140px">
      <el-form-item>
        <el-input v-model="formData.key" placeholder="请输入查询参数" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addOrUpdateHandle(brandId)">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" :disabled="this.dataListSelections.length === 0" @click="handleDelete(dataListSelections)">批量删除</el-button>
      </el-form-item>
    </el-form>
    <el-table
        @selection-change="selectionChangeHandle"
        :data="dataList"
        border
        v-loading="dataListLoading"
        stripe
        style="width: 100%">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <el-table-column prop="brandId" header-align="center" align="center" label="品牌id"></el-table-column>
      <el-table-column prop="name" header-align="center" align="center" label="品牌名称"></el-table-column>
      <el-table-column prop="logo" header-align="center" align="center" label="品牌logo地址">
        <template v-slot="{row}">
          <img :src="row.logo" style="width: 100px; height: 80px"  :alt="row.name"/>
        </template>
      </el-table-column>
      <el-table-column prop="descript" header-align="center" align="center" label="介绍"></el-table-column>
      <el-table-column prop="showStatus" header-align="center" align="center" label="显示状态">
        <template v-slot="{row}">
          <el-switch
              v-model="row.showStatus"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
              @change="updateShowStatusHandle(row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="firstLetter" header-align="center" align="center" label="检索首字母"></el-table-column>
      <el-table-column prop="sort" header-align="center" align="center" label="排序"></el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="250" label="操作">
        <template v-slot="{row}">
          <el-button type="text" size="small" @click="addOrUpdateHandle(row.brandId)">修改</el-button>
          <el-button type="text" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pagination.pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        :total="pagination.totalPage"
        layout="total, sizes, prev, pager, next, jumper"
    ></el-pagination>
    <brand-add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></brand-add-or-update>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import brandAddOrUpdate from './brand-add-or-update'

export default {
  data () {
    // 这里存放数据
    return {
      addOrUpdateVisible: false,
      dataListLoading: false,
      dataList: [],
      pagination:{
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      },
      dataListSelections:[],
      formData: {},
      brandId: 0
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {brandAddOrUpdate},
  props: {},
  // 方法集合
  methods: {
    getDataList() {
      this.dataListLoading = true;
      this.$http.get(
          "product/brand/list",
          {params: {
              key: this.formData.key,
              page: this.pagination.pageIndex,
              limit: this.pagination.pageSize
            }}
      ).then(({data: res}) => {
        if (res.code === 0) {
          this.dataList = res.page.list;
          this.pagination.totalPage = res.page.totalPage;
        } else {
          this.$message.error(res.msg);
        }
        this.dataListLoading = false;
      }).catch(() => this.$message.error("系统异常，查询品牌列表失败"))
    },
    handleDelete(data) {
      const ids = Array.isArray(data)? data.map(item => item.brandId) : [data.brandId];
      this.$confirm(`确定对[id=${ids.join(",")}]进行[${Array.isArray(data) ? "批量删除" : "删除"}]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http.delete(
            "product/brand/delete",
            {data: ids}
        ).then(({data : res}) => {
          res.code === 0? this.$message.success("操作成功") : this.$message.error(res.msg);
          // 刷新页面数据
          this.getDataList();
        }).catch(() => this.$message.error("系统异常，操作失败"))
      }).catch(() => {});
    },
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    updateShowStatusHandle(data) {
      this.$http.post(
          "/product/brand/update",
          {brandId : data.brandId, showStatus: data.showStatus}
      ).then(({data : res}) => {
        res.code === 0? this.$message.success("操作成功") : this.$message.error(res.msg);
        // 刷新页面数据
        this.getDataList();
      }).catch(() => this.$message.error("系统异常，操作失败"))
    },
    addOrUpdateHandle(brandId) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(brandId);
      })
    }
  },
  // 计算属性 类似于 data 概念
  computed: {},
  // 监控 data 中的数据变化
  watch: {},
  //过滤器
  filters: {},
  // 生命周期 - 创建之前
  beforeCreate () {
  },
  // 生命周期 - 创建完成（可以访问当前this 实例）
  created () {
    this.getDataList();
  },
  // 生命周期 - 挂载之前
  beforeMount () {
  },
  // 生命周期 - 挂载完成（可以访问 DOM 元素）
  mounted () {
  },
  // 生命周期 - 更新之前
  beforeUpdate () {
  },
  // 生命周期 - 更新之后
  updated () {
  },
  // 生命周期 - 销毁之前
  beforeDestroy () {
  },
  // 生命周期 - 销毁完成
  destroyed () {
  },
  // 如果页面有 keep-alive 缓存功能,这个函数会触发
  //进入的时候触发
  activated () {
  },
  //离开的时候触发
  deactivated() {
  },
}
</script>

<style scoped>
</style>
