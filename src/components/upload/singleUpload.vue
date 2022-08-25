<template>
<div>
<el-upload
    action="http://gulimall-liubin.oss-cn-beijing.aliyuncs.com"
    :data="dataObj"
    list-type="picture"
    :multiple="false"
    :show-file-list="showFileList"
    :file-list="fileList"
    :before-upload="beforeUpload"
    :on-remove="handleRemove"
    :on-success="handleUploadSuccess"
    :on-preview="handlePreview"
>
  <el-button size="small" type="primary">点击上传<i class="el-icon-upload el-icon--right"></i></el-button>
  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
</el-upload>
<el-dialog :visible.sync="dialogVisible">
  <img width="100%" :src="fileList[0].url" alt="">
</el-dialog>
</div>
</template>
<script>

 export default {
   name: 'SingleUpload',
   props: {
     value: String
   },
   data() {
     return {
       dataObj: {
         policy: '',
         signature: '',
         key: '',
         ossaccessKeyId: '',
         dir: '',
         host: ''
       },
       dialogVisible: false
     }
   },
   computed: {
     imageUrl() {
       return this.value
     },
     imageName() {
       if (this.value != null && this.value !== '') {
         return this.value.substr(this.value.lastIndexOf('/') + 1)
       } else {
         return null
       }
     },
     fileList() {
       return [{
         name: this.imageName,
         url: this.imageUrl
       }]
     },
     showFileList: {
       get: function() {
         return this.value !== null && this.value !== '' && this.value !== undefined
       },
       set: function(newValue) {
       }
     }
   },
   methods: {
     emitInput(val) {
       this.$emit('input', val)
     },
     handleRemove(file, fileList) {
       this.emitInput('')
     },
     handlePreview(file) {
       this.dialogVisible = true
     },
     getUUID() {
       return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
         return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
       })
     },
     beforeUpload(file) {
       const _self = this
       return new Promise((resolve, reject) => {
         // 前后端提交post异步请求获取签名信息
         this.$http.post('/thirdparty/oss/policy')
             .then(({data: res}) => {
               _self.dataObj.policy = res.data.policy
               _self.dataObj.signature = res.data.signature
               _self.dataObj.ossaccessKeyId = res.data.accessid
               _self.dataObj.key = res.data.dir + this.getUUID() + '_${filename}'
               _self.dataObj.dir = res.data.dir
               _self.dataObj.host = res.data.host
               resolve(true)
             }).catch(err => {
           reject(false)
         })
       })
     },
     handleUploadSuccess(res, file) {
       this.showFileList = true
       this.fileList.pop()
       this.fileList.push({ name: file.name, url: this.dataObj.host + '/' + this.dataObj.key.replace('${filename}', file.name) })
       this.emitInput(this.fileList[0].url)
     }
   }
 }
</script>

<style>

</style>


