<template>
  <div>
    <el-dialog
        :title="this.dataForm.attrId===0?'新增':'编辑'"
        :close-on-click-modal="false"
        :visible.sync="visible"
        @closed="dialogClose">
        <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="120px">
          <el-form-item label="属性名" prop="attrName">
            <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
          </el-form-item>
          <el-form-item label="属性类型" prop="attrType">
            <el-select v-model="dataForm.attrType" placeholder="请选择属性类型" disabled>
              <el-option :value="1" label="规格参数"></el-option>
              <el-option :value="0" label="销售属性"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="值类型" prop="valueType">
            <el-select v-model="dataForm.valueType" placeholder="请选择值类型" clearable>
              <el-option :value="0" label="只能单个值"></el-option>
              <el-option :value="1" label="允许多个值"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="可选值" prop="valueSelect">
            <el-select
                v-model="dataForm.valueSelect"
                filterable
                multiple
                allow-create
                placeholder="请输入内容">
            </el-select>
          </el-form-item>
          <el-form-item label="属性图标" prop="icon">
            <single-upload v-model="dataForm.icon"></single-upload>
          </el-form-item>
          <el-form-item label="所属分类" prop="categoryId">
            <category-cascader :category-path.sync="categoryPath"></category-cascader>
          </el-form-item>
          <el-form-item label="所属分组" prop="attrGroupId" v-if="attrType===1">
            <el-select v-model="dataForm.attrGroupId" placeholder="请选择">
              <el-option
                v-for="item in attrGroups"
                :key="item.attrGroupId"
                :label="item.attrGroupName"
                :value="item.attrGroupId"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="所属分组组内排序" prop="sort" v-if="attrType===1 && dataForm.attrGroupId">
            <el-input-number
                v-model="dataForm.sort"
                controls-position="right"
                :min="0">
            </el-input-number>
          </el-form-item>
          <el-form-item label="可检索" prop="searchType" v-if="attrType === 1">
            <el-switch
                v-model="dataForm.searchType"
                active-color="#13ce66"
                inactive-color="#ff4949"
                :active-value="1"
                :inactive-value="0"
            ></el-switch>
          </el-form-item>
          <el-form-item label="快速展示" prop="showDesc" v-if="attrType === 1">
            <el-switch
                v-model="dataForm.showDesc"
                active-color="#13ce66"
                inactive-color="#ff4949"
                :active-value="1"
                :inactive-value="0"
            ></el-switch>
          </el-form-item>
          <el-form-item label="启用状态" prop="enable">
            <el-switch
                v-model="dataForm.enable"
                active-color="#13ce66"
                inactive-color="#ff4949"
                :active-value="1"
                :inactive-value="0"
            ></el-switch>
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
import SingleUpload from "@/components/upload/singleUpload";
import CategoryCascader from "@/views/modules/common/category-cascader";

export default {
  data () {
    // 这里存放数据
    return {
      title: "",
      visible: false,
      categoryPath: [],
      attrGroups: [],
      dataForm: {
        attrId: 0,
        attrName: "",
        attrType: 1,
        valueType: 1,
        valueSelect: "",
        icon: "",
        categoryId: "",
        attrGroupId: "",
        searchType: "",
        showDesc: "",
        sort:0
      },
      dataRule: {
        attrName: [
          { required: true, message: "属性名不能为空", trigger: "blur" }
        ],
        valueType: [
          {
            required: true,
            message: "值类型不能为空",
            trigger: "blur"
          }
        ],
        attrType: [
          {
            required: true,
            message: "属性类型不能为空",
            trigger: "blur"
          }
        ],
        categoryId: [
          {
            required: true,
            message: "需要选择正确的三级分类数据",
            trigger: "blur"
          }
        ]
      }
    }
  },
  // import 引入的组件需要注入到对象中才能使用
  components: {SingleUpload, CategoryCascader},
  props: {
    attrType: {
      type: Number,
      default: 1
    }
  },
  // 方法集合
  methods: {
    dialogClose() {
      this.categoryPath=[];
    },
    init(attrId) {
      this.visible=true;
      this.dataForm.attrId=attrId;
      this.dataForm.attrType=this.attrType;
      this.$nextTick(() => {
        this.$refs.dataForm.resetFields();
        if (this.dataForm.attrId !== 0) {
          this.$http.get(
              `product/attr/info/${this.dataForm.attrId}`
          ).then(({data: res}) => {
            if (res.code === 0) {
              this.dataForm.attrName = res.attr.attrName;
              this.dataForm.searchType = res.attr.searchType;
              this.dataForm.valueType = res.attr.valueType;
              this.dataForm.icon = res.attr.icon;
              this.dataForm.valueSelect = res.attr.valueSelect.split(";");
              this.dataForm.attrType = res.attr.attrType;
              this.dataForm.enable = res.attr.enable;
              this.dataForm.categoryId = res.attr.categoryId;
              this.dataForm.showDesc = res.attr.showDesc;
              this.categoryPath = res.attr.categoryPath;
              this.$nextTick(() => {
                this.dataForm.attrGroupId = res.attr.attrGroupId;
              })
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
          let data = this.dataForm;
          data.valueSelect=this.dataForm.valueSelect.join(";");
          this.$http.post(
              `product/attr/${this.dataForm.attrId===0?"save":"update"}`,
              data
          ).then(({data: res}) => {
            if (res.code===0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible=false;
                  this.$emit("refreshDataList")
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
  watch: {
    categoryPath(path) {
      //监听到路径变化需要查出这个三级分类的分组信息
      this.attrGroups=[];
      this.dataForm.attrGroupId="";
      this.dataForm.categoryId=path[path.length-1];
      if (this.attrType === 1 && path && path.length===3) {
        this.$http.get(
            `product/attr/group/list/${this.dataForm.categoryId}`,
            {params: {
                page: 1,
                limit: 10000
              }}
        ).then(({data: res}) => {
          if (res.code === 0) {
            this.attrGroups=res.page.list;
          } else {
            this.$message.error(res.msg);
          }
        }).catch(() => {})
      }
    }
  },
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
