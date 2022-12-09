<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-form :inline="true" :model="dataForm">
          <el-form-item>
            <el-input
                placeholder="请输入查询参数"
                style="width:160px"
                v-model="dataForm.key"
                clearable></el-input>
          </el-form-item>
          <el-form-item label="分类">
            <category-cascader :category-path.sync="categoryPath"></category-cascader>
          </el-form-item>
          <el-form-item label="品牌">
            <brand-select
                ref="brandSelect"
                @change="handleBrandSelectChange"
                :category-id="dataForm.categoryId"></brand-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select style="width:160px" v-model="dataForm.publishStatus" clearable>
              <el-option label="上架" :value="1"></el-option>
              <el-option label="下架" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="getDataList">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div class="mod-config">
          <el-table :data="dataList"
                    border
                    v-loading="dataListLoading"
                    @selection-change="selectionChangeHandle"
                    style="width: 100%;">
            <el-table-column header-align="center" align="center" type="selection" width="50"></el-table-column>
            <el-table-column header-align="center" align="center" label="id" prop="id"></el-table-column>
            <el-table-column header-align="center" align="center" label="名称" prop="spuName"></el-table-column>
            <el-table-column header-align="center" align="center" label="描述" prop="spuDescription"></el-table-column>
            <el-table-column header-align="center" align="center" label="所属分类" prop="categoryName"></el-table-column>
            <el-table-column header-align="center" align="center" label="所属品牌" prop="brandName"></el-table-column>
            <el-table-column header-align="center" align="center" label="重量" prop="weight"></el-table-column>
            <el-table-column header-align="center" align="center" label="上架状态" prop="publishStatus">
              <template v-slot="{row}">
                <el-tag v-if="row.publishStatus === 1">已上架</el-tag>
                <el-tag v-if="row.publishStatus === 0">已下架</el-tag>
              </template>
            </el-table-column>
            <el-table-column header-align="center" align="center" label="创建时间" prop="createTime"></el-table-column>
            <el-table-column header-align="center" align="center" label="修改时间" prop="updateTime"></el-table-column>
            <el-table-column
                header-align="center"
                align="center" label="操作"
                width="150">
              <template v-slot="{row}">
                <el-button
                    v-if="row.publishStatus === 0"
                    type="text"
                    size="small"
                    @click="productUp(row.id)"
                >上架</el-button>
                <el-button type="text" size="small" @click="attrUpdateShow(row)">规格</el-button>
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
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import CategoryCascader from "@/views/modules/common/category-cascader";
import BrandSelect from "@/views/modules/common/brand-select";

export default {
  data () {
    // 这里存放数据
    return {
      dataForm: {
        key: "",
        categoryId: 0,
        brandId: 0,
        publishStatus: ""
      },
      categoryPath: [],
      dataList: [],
      dataListLoading: false,
      dataListSelections: [],
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      },
      catPathSub: null,
      brandIdSub: null
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {CategoryCascader, BrandSelect},
  props: {},
  // 方法集合
  methods: {
    handleBrandSelectChange(val) {
      this.dataForm.brandId=val;
    },
    selectionChangeHandle(val) {
      this.dataListSelections=val;
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
    getDataList() {
      this.dataListLoading=true;
      let params = {};
      Object.assign(params, this.dataForm, {
        page: this.pagination.pageIndex,
        limit: this.pagination.pageSize
      });
      this.$http.get(
          "/product/spu/info/list",
          {params: params}
      ).then(({data: res}) => {
        if (res.code===0) {
          this.dataList=res.page.list;
          this.pagination.totalPage=res.page.totolCount;
        } else {
          this.$message.error(res.msg);
        }
        this.dataListLoading=false;
      }).catch(() => {});
    },
    productUp(spuId) {
      this.$http.post(
          `/product/spu/info/up/${spuId}`
      ).then(({data: res}) => {
        if (res.code===0) {
          this.$message({
            message: "操作成功",
            type: "success",
            duration: 1500,
            onClose: () => {
              this.getDataList();
            }
          });
        } else {
          this.$message.error(res.msg);
        }
      }).catch(() => {});
    },
    attrUpdateShow(data) {
      this.$router.push({
        path: "/product-attr-update",
        query: { spuId: data.id, categoryId: data.categoryId }
      });
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
    this.catPathSub = this.PubSub.subscribe("catPath", (msg, val) => {
      this.dataForm.catelogId = val[val.length-1];
    });
    this.brandIdSub = this.PubSub.subscribe("brandId", (msg, val) => {
      this.dataForm.brandId = val;
    });
  },
  // 生命周期 - 更新之前
  beforeUpdate () {
  },
  // 生命周期 - 更新之后
  updated () {
  },
  // 生命周期 - 销毁之前
  beforeDestroy () {
    this.PubSub.unsubscribe(this.catPathSub);
    this.PubSub.unsubscribe(this.brandIdSub);
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
