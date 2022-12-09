<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6">
        <category ref="category" @tree-node-click="treeNodeClick"></category>
      </el-col>
      <el-col :span="18">
        <div class="mod-config">
          <el-form :inline="true" :model="dataForm">
            <el-form-item>
              <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="getDataList">查询</el-button>
              <el-button type="success" @click="getAllDataList">查询全部</el-button>
              <el-button type="primary" @click="addOrUpdateHandle(attrId)">新增</el-button>
              <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length === 0">批量删除</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="dataList"
                    border
                    v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle"
                    style="width: 100%;">
            <el-table-column header-align="center" align="center" type="selection" width="50"></el-table-column>
            <el-table-column header-align="center" align="center" prop="attrId" label="id"></el-table-column>
            <el-table-column header-align="center" align="center" prop="attrName" label="属性名"></el-table-column>
            <el-table-column
                header-align="center"
                align="center"
                prop="searchType"
                label="可检索"
                v-if="attrType === 1"
            >
            <template v-slot="{row}">
              <i class="el-icon-success" v-if="row.searchType === 1"></i>
              <i class="el-icon-error" v-else></i>
            </template>
            </el-table-column>
            <el-table-column header-align="center" align="center" prop="valueType" label="值类型">
              <template v-slot="{row}">
                <el-tag type="success" v-if="row.valueType === 0">单选</el-tag>
                <el-tag v-else>多选</el-tag>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="center" prop="icon" label="图标"></el-table-column>
            <el-table-column header-align="center" align="center" prop="valueSelect" label="可选值">
              <template v-slot="{row}">
                <el-tooltip placement="top">
                  <div slot="content">
                    <span v-for="(i,index) in row.valueSelect.split(';')" :key="index">{{i}}<br/></span>
                  </div>
                  <el-tag>{{row.valueSelect.split(";")[0]+" ..."}}</el-tag>
                </el-tooltip>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="center" prop="enable" label="启用状态">
              <template v-slot="{row}">
                <el-switch
                    v-model="row.enable"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    :active-value="1"
                    :inactive-value="0"
                >
                </el-switch>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="center" prop="categoryName" label="所属分类"></el-table-column>
            <el-table-column
                v-if="attrType === 1"
                prop="attrGroupName"
                header-align="center"
                align="center"
                label="所属分组"
            ></el-table-column>
            <el-table-column v-if="attrType === 1" prop="showDesc" header-align="center" align="center" label="快速展示">
              <template v-slot="{row}">
                <i class="el-icon-success" v-if="row.showDesc === 1"></i>
                <i class="el-icon-error" v-else></i>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="center" fixed="right" width="150px" label="操作">
              <template v-slot="{row}">
                <el-button type="text" size="small" @click="addOrUpdateHandle(row.attrId)">修改</el-button>
                <el-button type="text" size="small" @click="deleteHandle(row.attrId)">删除</el-button>
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

          <attr-add-or-update ref="addOrUpdate" :attr-type="attrType" @refreshDataList="getAllDataList"></attr-add-or-update>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import Category from "@/views/modules/common/category";
import AttrAddOrUpdate from "@/views/modules/product/attr-add-or-update";

export default {
  data () {
    // 这里存放数据
    return {
      attrId: 0,
      catId: 0,
      dataForm: {
        key: ""
      },
      dataListLoading: false,
      dataList: [],
      dataListSelections: [],
      attrIds:[],
      pagination:{
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {Category, AttrAddOrUpdate},
  props: {
    attrType: {
      type: Number,
      default: 1
    }
  },
  // 方法集合
  methods: {
    treeNodeClick(data) {
      this.catId = data.catId;
      this.dataForm.key="";
      this.getDataList();
    },
    getAllDataList() {
      this.catId = 0;
      this.dataForm.key="";
      this.getDataList();
      this.$refs.category.getMenus();
    },
    getDataList() {
      this.dataListLoading=true;
      let type=this.attrType === 1? "base":"sale";
      this.$http.get(
          `product/attr/${type}/list/${this.catId}`,
          {params: {
              page: this.pagination.pageIndex,
              limit: this.pagination.pageSize,
              key: this.dataForm.key
            }}
      ).then(({data: res}) => {
        if (res.code === 0) {
          this.dataList = res.page.list;
          this.pagination.totalPage = res.page.totalCount;
        } else {
          this.$message.error(res.msg);
        }
        this.dataListLoading=false;
      }).catch(() => {});
    },
    sizeChangeHandle(val) {
      this.pagination.pageIndex=1;
      this.pagination.pageSize=val;
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.pagination.pageIndex=val;
      this.getDataList();
    },
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    addOrUpdateHandle(attrId) {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(attrId)
      })
    },
    deleteHandle(attrId) {
      const ids = attrId? [attrId]:this.dataListSelections.map(item => item.attrId);
      this.$confirm(
          `确定对[id=${ids.join(",")}]属性进行[${attrId ?"删除" : "批量删除"}]操作?`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
        this.$http.post(
            "product/attr/delete",
            ids
        ).then(({data: res}) => {
          if (res.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getAllDataList();
              }
            });
          } else {
            this.$message.error(res.msg);
          }
        })
      }).catch(() => {});
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
