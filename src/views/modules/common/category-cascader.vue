<template>
  <div>
    <el-cascader
        filterable
        clearable
        placeholder="试试搜索：手机"
        v-model="paths"
        :options="categories"
        :props="setting"
    ></el-cascader>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';

export default {
  data () {
    // 这里存放数据8
    return {
      setting: {
        value: "catId",
        label: "name",
        children: "children"
      },
      categories: [],
      paths: this.categoryPath
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  //接受父组件传来的值
  props: {
    categoryPath: {
      type: Array,
      default(){
        return [];
      }
    }
  },
  // 方法集合
  methods: {
    getCategories() {
      this.$http.get(
          "product/category/list/tree"
      ).then(({data: res}) => {
        if (res.code === 0) {
          this.categories = res.trees;
        } else {
          this.$message.error(res.msg);
        }
      }).catch(() => this.$message.error("系统错误"))
    }
  },
  // 计算属性 类似于 data 概念
  computed: {},
  // 监控 data 中的数据变化
  watch: {
    categoryPath(val) {
      this.paths = this.categoryPath;
    },
    paths(v) {
      this.$emit("update:categoryPath",v);
    }
  },
  //过滤器
  filters: {},
  // 生命周期 - 创建之前
  beforeCreate (){
  },
  // 生命周期 - 创建完成（可以访问当前this 实例）
  created () {
    this.getCategories();
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
