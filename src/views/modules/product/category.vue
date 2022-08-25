<template>
  <div>
    <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
    <el-button v-if="draggable" type="primary" @click="batchSave">批量保存</el-button>
    <el-button type="danger" @click="batchDelete">批量删除</el-button>
  <el-tree :data="data"
           node-key="catId"
           show-checkbox
           :default-expanded-keys="expandedKey"
           :expand-on-click-node="false"
           :props="defaultProps"
           :draggable="draggable"
           :allow-drop="allowDrop"
           @node-drop="handleDrop"
           ref="menuTree">
    <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
              type="text"
              size="mini"
              v-if="node.level <=2"
              @click="() => append(data)">
            新增
          </el-button>
          <el-button
              type="text"
              size="mini"
              @click="() => edit(data)">
            编辑
          </el-button>
          <el-button
              type="text"
              size="mini"
              v-if="node.childNodes.length === 0"
              @click="() => remove(node, data)">
            删除
          </el-button>
        </span>
      </span>
  </el-tree>

  <el-dialog
      :title="dialog.type === 'add'? '新增分类' : '编辑分类'"
      :visible.sync="dialog.addOrEditVisible"
      :close-on-click-modal="false">
    <el-form :model="dialog.category" label-position="left" label-width="auto">
      <el-form-item label="分类名称">
        <el-input style="width: 50%" v-model="dialog.category.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="图标">
        <el-input style="width: 50%" v-model="dialog.category.icon" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="计量单位">
        <el-input style="width: 50%" v-model="dialog.category.productUnit" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialog.category.addOrEditVisible = false">取 消</el-button>
      <el-button type="primary" @click="submitData">确 定</el-button>
    </div>
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
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      expandedKey: [],
      maxLevel: 0,
      draggable: false,
      pCid: [],
      updateNodes: [],
      // dialog相关数据
      dialog : {
        category: {
          name: "",
          icon: "",
          showStatus: 1,
          sort: 0,
          productUnit: "",
          parentCid: 0
        },
        addOrEditVisible: false,
        type: ""
      },

    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  // 方法集合
  methods: {
    getMenus() {
      this.$http.get(
          '/product/category/list/tree',
          {params: {}}
      ).then(({ data: res }) => {
        if (res.code === 0) {
          this.data = res.trees;
        } else {
          this.$message.error(res.msg);
        }
      }).catch(() => this.$message.error("系统错误"))
    },
    batchSave() {
      this.$http.post(
          "/product/category/update/sort",
          this.updateNodes
      ).then(({ data: res }) => {
        if (res.code === 0) {
          this.$message({
            message: "菜单顺序等修改成功",
            type: "success"
          });
          //刷新出新的菜单
          this.getMenus();
          //设置需要默认展开的菜单
          this.expandedKey = this.pCid;
          this.updateNodes = [];
          this.maxLevel = 0;
          // this.pCid = 0;
        } else {
          this.$message.error("操作失败")
        }
      });
    },
    batchDelete() {
      let catIds = [];
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      for (let i = 0; i < checkedNodes.length; i++) {
        catIds.push(checkedNodes[i].catId);
      }
      this.$confirm(`是否批量删除【${catIds}】菜单?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http.post(
            "/product/category/delete",
            catIds
        ).then(({data: res}) => {
          res.code === 0? this.$message.success("操作成功") : this.$message.error(res.msg);
          //刷新出新的菜单
          this.getMenus();
          //设置需要默认展开的菜单
          this.expandedKey = this.pCid;
          this.updateNodes = [];
          this.maxLevel = 0;
        }).catch(() => this.$message.error("系统错误，操作失败"));
      }).catch(() => {});
    },
    append(data) {
      this.dialog.addOrEditVisible = true;
      this.dialog.type = "add";
      this.dialog.category.parentCid = data.catId;
      this.dialog.category.catLevel = data.catLevel * 1 + 1;
      this.dialog.category.catId = null;
      this.dialog.category.name = "";
      this.dialog.category.icon = "";
      this.dialog.category.productUnit = "";
      this.dialog.category.sort = 0;
      this.dialog.category.showStatus = 1;
    },
    edit(data) {
      this.dialog.addOrEditVisible = true;
      this.dialog.type = "edit";
      // 发送Http请求获取当前分类节点最新信息
      this.$http.get(
          `/product/category/info/${data.catId}`
      ).then(({data: res}) => {
        if (res.code === 0) {
          this.dialog.category = res.category;
        } else {
          this.$message.error(res.msg);
        }
      }).catch(() => this.$message.error("系统异常，操作失败"))
    },
    remove(node, data) {
      const ids = [data.catId];
      this.$confirm(`是否删除【${data.name}】菜单`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http.delete(
            '/product/category/delete',
            {'data': ids}
        ).then(({data : res}) => {
          res.code === 0 ? this.$message.success("操作成功") : this.$message.error(res.msg);
          // 刷新调用三级分类接口
          this.getMenus();
        }).catch(() => this.$message.error("系统错误，操作失败"));
      }).catch(() => {});
    },
    submitData() {
      if (this.dialog.type === "add") {
        this.addCategory();
      } else {
        this.editCategory();
      }
    },
    addCategory() {
      this.$http.post(
          "/product/category/save",
          this.dialog.category
      ).then(({data: res}) => {
        res.code === 0 ? this.$message.success("操作成功") : this.$message.error(res.msg);
        // 关闭对话框
        this.dialog.addOrEditVisible = false;
        // 刷新三级分类菜单
        this.getMenus();
      }).catch(() => {
        this.$message.error("系统错误，操作失败");
        this.dialog.addOrEditVisible = false;
      })
      // 展开新增节点的父节点
      this.expandedKey = [this.dialog.category.parentCid];
    },
    editCategory() {
      this.$http.post(
          "/product/category/update",
          this.dialog.category
      ).then(({data: res}) => {
        res.code === 0 ? this.$message.success("操作成功") : this.$message.error(res.msg);
        // 关闭对话框
        this.dialog.addOrEditVisible = false;
        // 刷新三级分类菜单
        this.getMenus();
      }).catch(() => {
        this.$message.error("系统错误，操作失败");
        this.dialog.addOrEditVisible = false;
      })

      // 展开新增节点的父节点
      this.expandedKey = [this.dialog.category.parentCid];
    },
    handleDrop(draggingNode, dropNode, dropType, ev) {
      //1、当前节点最新的父节点id
      let pCid = 0;
      let siblings = null;
      if (dropType === "before" || dropType === "after") {
        pCid =
            dropNode.parent.data.catId === undefined
                ? 0
                : dropNode.parent.data.catId;
        siblings = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
      }
      this.pCid.push(pCid);

      //2、当前拖拽节点的最新顺序，
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId === draggingNode.data.catId) {
          //如果遍历的是当前正在拖拽的节点
          let catLevel = draggingNode.level;
          if (siblings[i].level !== draggingNode.level) {
            //当前节点的层级发生变化
            catLevel = siblings[i].level;
            //修改他子节点的层级
            this.updateChildNodeLevel(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: catLevel
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }
    },
    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          const cNode = node.childNodes[i].data;
          this.updateNodes.push({
            catId: cNode.catId,
            catLevel: node.childNodes[i].level
          });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
    allowDrop(draggingNode, dropNode, type) {
      // 1、被拖动的当前节点以及所在的父节点总层数不能大于3
      this.countNodeLevel(draggingNode);
      //当前正在拖动的节点+父节点所在的深度不大于3即可
      let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
      //   this.maxLevel
      if (type === "inner") {
        return deep + dropNode.level <= 3;
      } else {
        return deep + dropNode.parent.level <= 3;
      }
    },
    countNodeLevel(node) {
      //找到所有子节点，求出最大深度
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      }
    },
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
    this.getMenus();
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
