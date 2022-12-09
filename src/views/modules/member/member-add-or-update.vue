<template>
  <div>
    <el-dialog
        :title="!dataForm.id ? '新增' : '修改'"
        :close-on-click-modal="false"
        :visible.sync="visible">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
        <el-form-item label="会员等级id" prop="levelId">
          <el-input v-model="dataForm.levelId" placeholder="会员等级id"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="dataForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="dataForm.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="dataForm.nickname" placeholder="昵称"></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input v-model="dataForm.mobile" placeholder="手机号码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="header">
          <el-input v-model="dataForm.header" placeholder="头像"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="dataForm.gender" placeholder="性别"></el-input>
        </el-form-item>
        <el-form-item label="生日" prop="birth">
          <el-date-picker
              v-model="dataForm.birth"
              type="date"
              placeholder="选择日期"
              format="yyyy 年 MM 月 dd 日">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="所在城市" prop="city">
          <el-input v-model="dataForm.city" placeholder="所在城市"></el-input>
        </el-form-item>
        <el-form-item label="职业" prop="job">
          <el-input v-model="dataForm.job" placeholder="职业"></el-input>
        </el-form-item>
        <el-form-item label="个性签名" prop="sign">
          <el-input v-model="dataForm.sign" placeholder="个性签名"></el-input>
        </el-form-item>
        <el-form-item label="用户来源" prop="sourceType">
          <el-input v-model="dataForm.sourceType" placeholder="用户来源"></el-input>
        </el-form-item>
        <el-form-item label="积分" prop="integration">
          <el-input v-model="dataForm.integration" placeholder="积分"></el-input>
        </el-form-item>
        <el-form-item label="成长值" prop="growth">
          <el-input v-model="dataForm.growth" placeholder="成长值"></el-input>
        </el-form-item>
        <el-form-item label="启用状态" prop="status">
          <el-switch
              v-model="dataForm.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              :active-value="1"
              :inactive-value="0"
          ></el-switch>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
    </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json 文件，图片文件等等）
// 例如：import  《组件名称》  from '《组件路径》 ';
import SingleUpload from "@/components/upload/singleUpload";


export default {
  data () {
    // 这里存放数据
    return {
      visible: false,
      dataForm: {
        id: 0,
        levelId: 0,
        username: "",
        password: "",
        nickname: "",
        mobile: "",
        email: "",
        header: "",
      },
      dataRule:[]
    }

  },
  // import 引入的组件需要注入到对象中才能使用
  components: {SingleUpload},
  props: {},
  // 方法集合
  methods: {
    init(id) {
      this.dataForm.id=id;
      this.visible=true;
      this.$refs.dataForm.resetFields();
      if (this.dataForm.id!==0) {
        this.$http.get(
            `/member/member/info/${this.dataForm.id}`
        ).then(({data: res}) => {

        }).catch(() => {});
      }
    },
    dataFormSubmit() {

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
