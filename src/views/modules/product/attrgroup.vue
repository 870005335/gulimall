<template>
  <el-row :gutter="20">
    <el-col :span="6"><category @tree-node-click="treeNodeClick"></category></el-col>
    <el-col :span="18"><div class="mod-config">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">查询</el-button>
          <el-button type="success" @click="getAllDataList()">查询全部</el-button>
          <el-button
              type="primary"
              @click="addOrUpdateHandle(attrGroupId)"
          >新增</el-button>
          <el-button
              type="danger"
              @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0"
          >批量删除</el-button>
        </el-form-item>
      </el-form>
      <el-table
          :data="dataList"
          border
          v-loading="dataListLoading"
          @selection-change="selectionChangeHandle"
          style="width: 100%;"
      >
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="attrGroupId" header-align="center" align="center" label="分组id"></el-table-column>
        <el-table-column prop="attrGroupName" header-align="center" align="center" label="组名"></el-table-column>
        <el-table-column prop="sort" header-align="center" align="center" label="排序"></el-table-column>
        <el-table-column prop="description" header-align="center" align="center" label="描述"></el-table-column>
        <el-table-column prop="icon" header-align="center" align="center" label="组图标"></el-table-column>
        <el-table-column prop="categoryId" header-align="center" align="center" label="所属分类id"></el-table-column>
        <el-table-column
            fixed="right"
            header-align="center"
            align="center"
            width="150"
            label="操作"
        >
          <template v-slot="{row}">
            <el-button type="text" size="small" @click="relationHandle(row.attrGroupId)">关联</el-button>
            <el-button
                type="text"
                size="small"
                @click="addOrUpdateHandle(row.attrGroupId)"
            >修改</el-button>
            <el-button type="text" size="small" @click="deleteHandle(row.attrGroupId)">删除</el-button>
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
      <attr-group-add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></attr-group-add-or-update>
    </div></el-col>
  </el-row>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import Category from "@/views/modules/common/category";
import AttrGroupAddOrUpdate from "@/views/modules/product/attrgroup-add-or-update";

export default {
  data () {
    // 这里存放数据
    return {
      attrGroupId: 0,
      catId: 0,
      dataForm: {},
      dataListLoading: false,
      dataListSelections: [],
      dataList: [],
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      },
      addOrUpdateVisible: false
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {Category, AttrGroupAddOrUpdate},
  props: {},
  // 方法集合
  methods: {
    treeNodeClick(data) {
      this.catId = data.catId;
      //重新查询
      this.getDataList();
    },
    getAllDataList() {
      this.catId = 0;
      this.dataForm.key="";
      this.getDataList();
    },
    getDataList() {
      this.dataListLoading = true;
      this.$http.get(
          `product/attr/group/list/${this.catId}`,
          {params: {
              page: this.pagination.pageIndex,
              limit: this.pagination.pageSize,
              key: this.dataForm.key
            }
          }).then(({data: res}) => {
            if (res.code === 0) {
              this.dataList = res.page.list;
              this.pagination.totalPage = res.page.totalCount;
            } else {
              this.$message.error(res.msg);
            }
            this.dataListLoading = false;
      }).catch(() => this.$message.error("系统异常，查询属性分组列表异常"));
    },
    addOrUpdateHandle(attrGroupId) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(attrGroupId)
      })
    },
    deleteHandle(attrGroupId) {
      const ids = attrGroupId
          ? [attrGroupId]
          : this.dataListSelections.map(item => item.attrGroupId);
      this.$confirm(
          `确定对[id=${ids.join(",")}]属性分组进行[${attrGroupId ? "删除" : "批量删除"}]操作?`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
      ).then(() => {
        this.$http.post(
            "product/attr/group/delete",
            {data: ids}
        ).then(({data: res}) => {
          res.code === 0?this.$message.success("操作成功") : this.$message.error(res.msg);
        })
      }).catch(() => {})
    },
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    relationHandle() {

    },
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    }
  },
  // 计算属性 类似于 data 概念
  computed: {},
  // 监控 data 中的数据变化
  watch: {},
  //过滤器
  filters: {},
  // 生命周期 - 创建之前
  beforeCreate (){
  },
  // 生命周期 - 创建完成（可以访问当前this 实例）
  created () {
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
    this.getDataList();
  },
  //离开的时候触发
  deactivated() {
  },
}
</script>

<style scoped>
</style>
