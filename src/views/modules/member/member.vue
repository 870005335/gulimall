<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getDataList()">查询</el-button>
        <el-button type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
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
      <el-table-column prop="id" header-align="center" align="center" label="id"></el-table-column>
      <el-table-column prop="levelId" header-align="center" align="center" label="会员等级"></el-table-column>
      <el-table-column prop="username" header-align="center" align="center" label="用户名"></el-table-column>
      <el-table-column prop="nickname" header-align="center" align="center" label="昵称"></el-table-column>
      <el-table-column prop="mobile" header-align="center" align="center" label="手机号码"></el-table-column>
      <el-table-column prop="email" header-align="center" align="center" label="邮箱"></el-table-column>
      <el-table-column prop="header" header-align="center" align="center" label="头像"></el-table-column>
      <el-table-column prop="gender" header-align="center" align="center" label="性别"></el-table-column>
      <el-table-column prop="birth" header-align="center" align="center" label="生日"></el-table-column>
      <el-table-column prop="city" header-align="center" align="center" label="所在城市"></el-table-column>
      <el-table-column prop="job" header-align="center" align="center" label="职业"></el-table-column>
      <el-table-column prop="sign" header-align="center" align="center" label="个性签名"></el-table-column>
      <el-table-column prop="sourceType" header-align="center" align="center" label="用户来源"></el-table-column>
      <el-table-column prop="integration" header-align="center" align="center" label="积分"></el-table-column>
      <el-table-column prop="growth" header-align="center" align="center" label="成长值"></el-table-column>
      <el-table-column prop="status" header-align="center" align="center" label="启用状态">
        <template v-slot="{row}">
          <el-switch
              v-model="row.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" header-align="center" align="center" label="注册时间"></el-table-column>
      <el-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template v-slot="{row}">
          <el-button type="text" size="small">送券</el-button>
          <el-button type="text" size="small">查订单</el-button>
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
    <member-add-or-update ref="addOrUpdate"></member-add-or-update>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import MemberAddOrUpdate from "@/views/modules/member/member-add-or-update";

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
  components: {MemberAddOrUpdate},
  props: {},
  // 方法集合
  methods: {
    getDataList() {
      this.dataListLoading=true;
      this.$http.get(
          "/member/member/list",
          {params: {
              page: this.pagination.pageIndex,
              limit: this.pagination.pageSize,
              key: this.dataForm.key
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
    selectionChangeHandle(val) {
      this.dataListSelections=val;
    },
    sizeChangeHandle(val) {
      this.dataForm.key="";
      this.pagination.pageIndex=1;
      this.pagination.pageSize=val;
      this.getDataList();
    },
    currentChangeHandle(val) {
      this.dataForm.key="";
      this.pagination.pageIndex=val;
      this.getDataList();
    },
    addOrUpdateHandle() {
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(this.id);
      });
    },
    deleteHandle() {

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
