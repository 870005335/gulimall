<template>
  <div>
    <el-dialog
        :title="this.dataForm.id===0 ? '新增' : '修改'"
        :close-on-click-modal="false"
        :visible.sync="visible">
      <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="120px">
        <el-form-item label="等级名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="等级名称"></el-input>
        </el-form-item>
        <el-form-item label="所需成长值" prop="growthPoint">
          <el-input-number v-model="dataForm.growthPoint" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="默认等级" prop="defaultStatus">
          <el-checkbox v-model="dataForm.defaultStatus" :true-label="1" :false-label="0"></el-checkbox>
        </el-form-item>
        <el-form-item label="免运费标准" prop="freeFreightPoint">
          <el-input-number :min="0" v-model="dataForm.freeFreightPoint"></el-input-number>
        </el-form-item>
        <el-form-item label="每次评价获取的成长值" prop="commentGrowthPoint">
          <el-input-number :min="0" v-model="dataForm.commentGrowthPoint"></el-input-number>
        </el-form-item>
        <el-form-item label="是否有免邮特权" prop="privilegeFreeFreight">
          <el-checkbox v-model="dataForm.privilegeFreeFreight" :true-label="1" :false-label="0"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否有会员价格特权" prop="privilegeMemberPrice">
          <el-checkbox v-model="dataForm.privilegeMemberPrice" :true-label="1" :false-label="0"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否有生日特权" prop="privilegeBirthday">
          <el-checkbox v-model="dataForm.privilegeBirthday" :true-label="1" :false-label="0"></el-checkbox>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="dataForm.note" placeholder="备注"></el-input>
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

export default {
  data () {
    // 这里存放数据
    return {
      visible: false,
      dataForm: {
        id: 0,
        name: "",
        growthPoint: 0,
        defaultStatus: 0,
        freeFreightPoint: "",
        commentGrowthPoint: "",
        privilegeFreeFreight: 0,
        privilegeMemberPrice: 0,
        privilegeBirthday: 0,
        note: "",
      },
      dataRule: {
        name: [
          { required: true, message: '等级名称不能为空', trigger: 'blur' }
        ],
        growthPoint: [
          { required: true, message: '等级需要的成长值不能为空', trigger: 'blur' }
        ],
        defaultStatus: [
          { required: true, message: '是否为默认等级[0->不是；1->是]不能为空', trigger: 'blur' }
        ],
        freeFreightPoint: [
          { required: true, message: '免运费标准不能为空', trigger: 'blur' }
        ],
        commentGrowthPoint: [
          { required: true, message: '每次评价获取的成长值不能为空', trigger: 'blur' }
        ],
        privilegeFreeFreight: [
          { required: true, message: '是否有免邮特权不能为空', trigger: 'blur' }
        ],
        privilegeMemberPrice: [
          { required: true, message: '是否有会员价格特权不能为空', trigger: 'blur' }
        ],
        privilegeBirthday: [
          { required: true, message: '是否有生日特权不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  // 方法集合
  methods: {
    init(id) {
      this.dataForm.id=id;
      this.visible=true;
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields();
        if (this.dataForm.id!==0) {
          this.$http.get(
              `/member/member/level/info/${this.dataForm.id}`
          ).then(({data: res}) => {
            if (res.code===0) {
              this.dataForm.name = res.memberLevel.name
              this.dataForm.growthPoint = res.memberLevel.growthPoint||0
              this.dataForm.defaultStatus = res.memberLevel.defaultStatus||0
              this.dataForm.freeFreightPoint = res.memberLevel.freeFreightPoint||0
              this.dataForm.commentGrowthPoint = res.memberLevel.commentGrowthPoint||0
              this.dataForm.privilegeFreeFreight = res.memberLevel.privilegeFreeFreight||0
              this.dataForm.privilegeMemberPrice = res.memberLevel.privilegeMemberPrice||0
              this.dataForm.privilegeBirthday = res.memberLevel.privilegeBirthday||0
              this.dataForm.note = res.memberLevel.note
            } else {
              this.$message.error(res.msg);
            }
          }).catch(() => {})
        }
      })
    },
    dataFormSubmit() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$http.post(
              `/member/member/level/${this.dataForm.id===0?"save":"update"}`,
              {
                id: this.dataForm.id,
                name: this.dataForm.name,
                growthPoint: this.dataForm.growthPoint,
                defaultStatus: this.dataForm.defaultStatus,
                freeFreightPoint: this.dataForm.freeFreightPoint,
                commentGrowthPoint: this.dataForm.commentGrowthPoint,
                privilegeFreeFreight: this.dataForm.privilegeFreeFreight,
                privilegeMemberPrice: this.dataForm.privilegeMemberPrice,
                privilegeBirthday: this.dataForm.privilegeBirthday,
                note: this.dataForm.note
              }
          ).then(({data: res}) => {
            if (res.code===0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible=false;
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(res.msg);
            }
          }).catch(() => {});
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
