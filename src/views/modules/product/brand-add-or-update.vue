<template>
  <div>
    <el-dialog
        :title="dataForm.brandId === 0 ? '新增品牌' : '修改品牌'"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false">
      <el-form :rules="dataRule" :model="dataForm" ref="dataForm" label-position="left" label-width="120px">
        <el-form-item label="品牌名" prop="name">
          <el-input v-model="dataForm.name" placeholder="品牌名"></el-input>
        </el-form-item>
        <el-form-item label="品牌logo地址" prop="logo">
<!--           <el-input v-model="dataForm.logo" placeholder="品牌logo地址"></el-input>-->
          <single-upload v-model="dataForm.logo"></single-upload>
        </el-form-item>
        <el-form-item label="介绍" prop="descript">
          <el-input v-model="dataForm.descript" placeholder="介绍"></el-input>
        </el-form-item>
        <el-form-item label="显示状态" prop="showStatus">
          <el-switch
              v-model="dataForm.showStatus"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item label="检索首字母" prop="firstLetter">
        <el-input v-model="dataForm.firstLetter" placeholder="检索首字母"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model.number="dataForm.sort" placeholder="排序"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dataFormSubmit">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import singleUpload from "@/components/upload/singleUpload";

export default {
  data () {
    // 这里存放数据
    return {
      dialogVisible: false,
      dataForm: {
        name: "",
        logo: "",
        descript: "",
        firstLetter: "",
        sort: ""
      },
      dataRule: {
        name: [{required: true, message: "品牌名不能为空", trigger: "blur"}],
        // logo: [{required: true, message: "品牌logo不能为空", trigger: "blur"}],
        firstLetter: [{
          required: true,
          validator: (rule, value, callback) => {
            if (value === "") {
              callback(new Error("品牌首字母必须填写"));
            } else if (!/^[a-zA-Z]$/.test(value)) {
              callback(new Error("品牌首字母必须在a-z或A-Z之间"));
            } else {
              callback();
            }
          },
          trigger: "blur"
        }],
        sort: [{
          validator: (rule, value, callback) => {
            if (!Number.isInteger(value) || value < 0) {
              callback(new Error("排序必须是一个大于等于0的整数"));
            } else {
              callback();
            }
          },
          trigger: "blur"
        }]
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {singleUpload},
  props: {},
  // 方法集合
  methods: {
    init(brandId) {
      this.dataForm.brandId = brandId;
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.brandId) {
          this.$http.get(
              `product/brand/info/${this.dataForm.brandId}`
          ).then(({data: res}) => {
            if (res.code === 0) {
              this.dataForm.name = res.brand.name;
              this.dataForm.logo = res.brand.logo;
              this.dataForm.descript = res.brand.descript;
              this.dataForm.showStatus = res.brand.showStatus;
              this.dataForm.firstLetter = res.brand.firstLetter;
              this.dataForm.sort = res.brand.sort;
            } else {
              this.$message.error(res.msg)
            }
          }).catch(() => this.$message.error("系统异常,查询品牌信息失败"));
        }
      })
    },
    dataFormSubmit() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          this.$http.post(
              `product/brand/${this.dataForm.brandId === 0? "save" : "update"}`,
              {
                brandId: this.dataForm.brandId,
                name: this.dataForm.name,
                logo: this.dataForm.logo,
                descript: this.dataForm.descript,
                showStatus: this.dataForm.showStatus,
                firstLetter: this.dataForm.firstLetter,
                sort: this.dataForm.sort
              }
          ).then(({data: res}) => {
            if (res.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.dialogVisible = false;
                  this.$emit("refreshDataList");
                }
              })
            } else {
              this.$message.error(res.msg);
            }
          }).catch(() => this.$message.error("系统异常，操作失败"))
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
