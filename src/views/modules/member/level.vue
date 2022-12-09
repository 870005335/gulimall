<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button
            type="primary"
            @click="addOrUpdateHandle(id)"
        >新增</el-button>
        <el-button
            type="danger"
            @click="deleteHandle(id)"
            :disabled="dataListSelections.length <= 0"
        >批量删除</el-button>
      </el-form-item>
      <el-table
          :data="dataList"
          border
          v-loading="dataListLoading"
          @selection-change="selectionChangeHandle"
          style="width: 100%;"
      >
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="id" header-align="center" align="center" label="id"></el-table-column>
        <el-table-column prop="name" header-align="center" align="center" label="等级名称"></el-table-column>
        <el-table-column prop="growthPoint" header-align="center" align="center" label="所需成长值"></el-table-column>
        <el-table-column prop="defaultStatus" header-align="center" align="center" label="默认等级">
          <template v-slot="{row}">
            <i class="el-icon-success" v-if="row.defaultStatus===1"></i>
            <i class="el-icon-error" v-else></i>
          </template>
        </el-table-column>
        <el-table-column prop="freeFreightPoint" header-align="center" align="center" label="免运费标准"></el-table-column>
        <el-table-column
            prop="commentGrowthPoint"
            header-align="center"
            align="center"
            label="每次评价获取的成长值"
        ></el-table-column>
        <el-table-column header-align="center" align="center" label="特权">
          <el-table-column
              prop="privilegeFreeFreight"
              header-align="center"
              align="center"
              label="免邮特权"
          >
            <template v-slot="{row}">
              <i class="el-icon-success" v-if="row.privilegeFreeFreight===1"></i>
              <i class="el-icon-error" v-else></i>
            </template>
          </el-table-column>
          <el-table-column
              prop="privilegeMemberPrice"
              header-align="center"
              align="center"
              label="会员价格特权"
          >
            <template v-slot="{row}">
              <i class="el-icon-success" v-if="row.privilegeMemberPrice===1"></i>
              <i class="el-icon-error" v-else></i>
            </template>
          </el-table-column>
          <el-table-column
              prop="privilegeBirthday"
              header-align="center"
              align="center"
              label="生日特权"
          >
            <template v-slot="{row}">
              <i class="el-icon-success" v-if="row.privilegeBirthday===1"></i>
              <i class="el-icon-error" v-else></i>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="note" header-align="center" align="center" label="备注"></el-table-column>
        <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
            <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
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
    </el-form>
    <level-add-or-update ref="addOrUpdate" @refreshDataList="getDataList"></level-add-or-update>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import LevelAddOrUpdate from "@/views/modules/member/level-add-or-update";

export default {
  data () {
    // 这里存放数据
    return {
      id: 0,
      dataForm: {
        key: ""
      },
      dataList: [],
      dataListSelections: [],
      dataListLoading: false,
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {LevelAddOrUpdate},
  props: {},
  // 方法集合
  methods: {
    getDataList() {
      this.dataListLoading=true;
      this.$http.get(
          "/member/member/level/list",
          {params: {
              key: this.dataForm.key,
              page: this.pagination.pageIndex,
              limit: this.pagination.pageSize
            }
          }
      ).then(({data: res}) => {
        if (res.code===0) {
          this.dataList=res.page.list;
          this.pagination.totalPage=res.page.totalCount;
        } else {
          this.$message.error(res.msg);
        }
        this.dataListLoading=false;
      }).catch(() => {});
    },
    addOrUpdateHandle(id) {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    deleteHandle(id) {
      const ids=id!==0?[id]:this.dataListSelections.map(item => item.id);
      this.$confirm(
          `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
      ).then(() => {
        this.$http.post(
            "member/member/level/delete",
            ids
        ).then(({data: res}) => {
          if (res.code===0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              }
            })
          } else {
            this.$message.error(res.msg);
          }
        })

      }).catch(() => {})
    },
    selectionChangeHandle(val) {
      this.dataListSelections=val;
    },
    sizeChangeHandle(val) {
      this.pagination.pageIndex=1;
      this.pagination.pageSize=val;
      this.dataForm.key="";
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.dataForm.key="";
      this.pagination.pageIndex=val;
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
