<template>
  <div>
    <el-input placeholder="输入关键字进行过滤" v-model="filterText"></el-input>
    <el-tree :data="data"
             :props="defaultProps"
             :highlight-current = "true"
             :filter-node-method="filterNode"
             @node-click="nodeClick"
             node-key="catId"
             ref="menuTree"></el-tree>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';

export default {
  data () {
    // 这里存放数据
    return {
      filterText: "",
      data: [],
      defaultProps: {
        children: "children",
        label: "name"
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  // 方法集合
  methods: {
    //树节点过滤
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
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
    nodeClick(data, node, component) {
      // 树叶节点向父组件发送事件；
      if (data.catLevel === 3) {
        this.$emit("tree-node-click", data, node, component);
      }
    }
  },
  // 计算属性 类似于 data 概念
  computed: {},
  // 监控 data 中的数据变化
  watch: {
    filterText(val) {
      this.$refs.menuTree.filter(val);
    }
  },
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
