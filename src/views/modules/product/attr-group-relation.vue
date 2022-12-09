<template>
  <div>
  <el-dialog
      title="关联属性"
      :close-on-click-modal="false"
      :visible.sync="visible">
    <el-dialog
        :close-on-click-modal="false"
        :visible.sync="innerVisible"
        width="40%"
        title="选择属性"
        append-to-body>
      <div>
        <el-form
            :inline="true"
            v-model="dataForm"
            @keyup.enter.native="getAttrDataList()">
          <el-form-item>
            <el-input v-model="dataForm.key" placeholder="参数名" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="getAttrDataList()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
            :data="dataList"
            style="width: 100%;"
            border
            v-loading="dataListLoading"
            @selection-change="innerSelectionChangeHandle">
          <el-table-column type="selection" header-align="center" align="center"></el-table-column>
          <el-table-column header-align="center" align="center" prop="attrId" label="属性Id"></el-table-column>
          <el-table-column header-align="center" align="center" prop="attrName" label="属性名"></el-table-column>
          <el-table-column header-align="center" align="center" prop="icon" label="属性图标"></el-table-column>
          <el-table-column
              header-align="center"
              align="center"
              prop="valueSelect"
              label="可选值列表">
            <template v-slot="{row}">
              <el-tooltip>
                <div slot="content">
                  <span v-for="(item, index) in row.valueSelect.split(';')" :key="index">
                    {{item}}
                  </span>
                </div>
                <el-tag>{{row.valueSelect.split(";")[0]+" ..."}}</el-tag>
              </el-tooltip>
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
      <div slot="footer" class="dialog-foot">
        <el-button @click="innerVisible=false">取消</el-button>
        <el-button type="primary"
                   @click="submitAddRelation"
                   :disabled="innerDataListSelections.length === 0">确定
        </el-button>
      </div>
    </el-dialog>
    <el-row>
      <el-col :span="24">
        <el-button type="primary" @click="addRelation">新建关联</el-button>
        <el-button
            type="danger"
            @click="handleRelationRemove()"
            :disabled="relationAttrsSelections.length === 0">
          批量移除
        </el-button>
        <el-table :data="relationAttrs" @selection-change="selectionChangeHandle" style="width: 100%">
          <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
          <el-table-column prop="attrId" header-align="center" align="center" label="#"></el-table-column>
          <el-table-column prop="attrName" label="属性名" header-align="center" align="center"></el-table-column>
          <el-table-column
              header-align="center"
              align="center"
              prop="valueSelect"
              label="可选值">
            <template v-slot="{row}">
              <el-tooltip>
                <div slot="content">
                    <span v-for="(i,index) in row.valueSelect.split(';')" :key="index">
                      {{i}}
                      <br />
                    </span>
                </div>
                <el-tag>{{row.valueSelect.split(";")[0]+" ..."}}</el-tag>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column label="操作" fixed="right" header-align="center" align="center">
            <template v-slot="{row}">
              <el-button type="text" size="small" @click="handleRelationRemove(row.attrId)">移除 </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';

export default {
  data () {
    // 这里存放数据
    return {
      attrGroupId: 0,
      visible: false,
      innerVisible: false,
      relationAttrs: [],
      relationAttrsSelections:[],
      dataForm: {},
      dataListLoading: false,
      dataList:[],
      pagination: {
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0
      },
      innerDataListSelections: []
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  // 方法集合
  methods: {
    getRelationAttrs() {
      this.$http.get(
          `product/attr/group/attr/relation/${this.attrGroupId}`
      ).then(({data: res}) => {
        if (res.code === 0) {
          this.relationAttrs = res.relationAttrs;
        } else {
          this.$message.error(res.msg);
        }
      }).catch(() => {});
    },
    init(attrGroupId) {
      this.attrGroupId = attrGroupId;
      this.visible=true;
      this.getRelationAttrs();
    },
    selectionChangeHandle(val) {
      this.relationAttrsSelections = val;
    },
    handleRelationRemove(attrId) {
      const attrIds = attrId?[attrId]:this.relationAttrsSelections.map(item => item.attrId);
      this.$confirm(
          `确定对[id=${attrIds.join(",")}]属性进行关联[${attrId ?"移除" : "批量移除"}]操作?`,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
          this.$http.post(
              "/product/attr/group/attr/relation/delete",
              {attrGroupId: this.attrGroupId, attrIds: attrIds}
          ).then(({data: res}) => {
            res.code===0?this.$message.success("操作成功"):this.$message.error(res.msg);
            this.getRelationAttrs();
          });
      }).catch(() => {});
    },
    addRelation() {
      this.getAttrDataList();
      this.innerVisible=true;
    },
    getAttrDataList() {
      this.dataListLoading=true;
      this.$http.get(
          `/product/attr/group/noAttr/relation/${this.attrGroupId}`,
          {
            params: {
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
    innerSelectionChangeHandle(val) {
      this.innerDataListSelections=val;
    },
    sizeChangeHandle(val) {
      this.pagination.pageIndex=1;
      this.pagination.pageSize=val;
      this.getAttrDataList();
    },
    currentChangeHandle(val) {
      this.pagination.pageIndex=val;
      this.getAttrDataList();
    },
    submitAddRelation() {
      const attrIds = this.innerDataListSelections.map(item => item.attrId);
      this.$http.post(
          "product/attr/group/attr/relation/save",
          {attrGroupId: this.attrGroupId, attrIds: attrIds}
      ).then(({data: res}) => {
        if (res.code===0) {
          this.$message.success("操作成功");
          this.innerVisible=false;
          this.getRelationAttrs();
        } else {
          this.$message.error(res.msg);
        }
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
