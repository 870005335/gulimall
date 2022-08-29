<template>
  <div>
  <el-dialog
      :title="dataForm.attrGroupId === 0? '新增' : '编辑'"
      :close-on-click-modal="false"
      :visible.sync="visible" @close="handleClose">
    <el-form
        :model="dataForm"
        ref="dataForm"
        :rules="dataRule"
        @keyup.enter.native="dataFormSubmit"
        label-position="left"
        label-width="100px">
      <el-form-item label="组名" prop="attrGroupName">
        <el-input v-model="dataForm.attrGroupName" placeholder="组名"></el-input>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="dataForm.description" placeholder="描述"></el-input>
      </el-form-item>
      <el-form-item label="组图标" prop="icon">
        <single-upload v-model="dataForm.logo"></single-upload>
      </el-form-item>
      <el-form-item label="所属分类" prop="categoryId">
        <category-cascader :categoryPath.sync="categoryPath"></category-cascader>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit">确定</el-button>
    </span>
  </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import CategoryCascader from "@/views/modules/common/category-cascader";
import SingleUpload from "@/components/upload/singleUpload";

export default {
  data () {
    // 这里存放数据
    return {
      visible: false,
      dataForm: {
        attrGroupName: "",
        sort: 0,
        description: "",
        icon: "",
        categoryId: 0
      },
      categories: [],
      categoryPath: [],
      dataRule: {
        attrGroupName: [{required: true, message: "组名不能为空", trigger: "blur"}],
        sort: [{
          validator: (rule, value, callback) => {
            if (!Number.isInteger(value) || value < 0) {
              callback(new Error("排序必须是一个大于等于0的整数"));
            } else {
              callback();
            }
          },
          trigger: "blur"
        }],
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {CategoryCascader, SingleUpload},
  props: {},
  // 方法集合
  methods: {
    handleClose() {
      this.categoryPath = [];
    },
    init(attrGroupId) {
      this.dataForm.attrGroupId = attrGroupId;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields();
        if (this.dataForm.attrGroupId !== 0) {
          this.$http.get(
              `product/attr/group/info/${this.dataForm.attrGroupId}`
          ).then(({data: res}) => {
            if (res.code === 0) {
              this.dataForm = res.attrGroup;
              this.categoryPath = res.attrGroup.categoryPath;
            } else {
              this.$message.error(res.msg);
            }
          }).catch(() => {});
        }
      })
    },
    dataFormSubmit() {
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          this.$http.post(
              `product/attr/group/${this.dataForm.attrGroupId === 0?"save" : "update"}`,
              {
                attrGroupId: this.dataForm.attrGroupId,
                attrGroupName: this.dataForm.attrGroupName,
                sort: this.dataForm.sort,
                description: this.dataForm.description,
                icon: this.dataForm.icon,
                categoryId: this.categoryPath[this.categoryPath.length-1]
              }
          ).then(({data : res}) => {
            if (res.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                }
              })
            } else {
              this.$message.error(res.msg);
            }
          }).catch(() => {})
        }
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
