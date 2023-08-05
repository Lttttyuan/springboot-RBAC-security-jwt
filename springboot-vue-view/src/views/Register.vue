<template>
  <div class="layout">
    <el-tabs type="border-card">
      <el-tab-pane label="注册">
        <el-form ref="from" :model="form" label-position="right" label-width="100px" style="max-width: 460px"
                 class="loginForm" :rules="rules" :hide-required-asterisk="true">
          <el-form-item label="用户名：" prop="username" :prefix-icon="Avatar">
            <el-input v-model="form.username"/>
          </el-form-item>
          <el-form-item label="密码：" prop="password" :prefix-icon="Lock">
            <el-input type="password" v-model="form.password" show-password/>
          </el-form-item>
          <el-form-item label="确认密码：" prop="password" :prefix-icon="Lock">
            <el-input type="password" v-model="form.confirm" show-password/>
          </el-form-item>

          <!--          验证码功能-->
          <el-form-item label="验证码：">
            <div style="display: flex">
              <el-input v-model="form.validCode" style="width: 50%" placeholder="请输入验证码："/>
              <ValidCode @input="createValidcode"/>
            </div>
          </el-form-item>

          <div style="display:flex;">
            <el-button type="primary" style="width: 50%" @click="register">注册</el-button>
            <el-button type="primary" style="width: 50%" @click="this.$router.push('/login')">取消</el-button>
          </div>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>


<script>
import request from "@/utils/request";
import ValidCode from "@/components/ValidCode";
// 一个用户图像+密码锁图标
import { Avatar, Lock } from "@element-plus/icons-vue";
import router from "@/router";


export default {
  name: "Register",
  components: {ValidCode},
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ]
      },
      validCode:''
    }
  },
  setup(){
    return {
      Avatar,
      Lock
    }
  },
  methods: {
    //接收验证码组件提交的四位验证码
    createValidcode(data) {
      this.validCode = data
    },
    register() {
      this.$refs.from.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请输入用户名和密码!"
          })
          return
        }
        // operation code
        if (!this.form.validCode){
          this.$message.error("请输入验证码!")
          return;
        }
        if (this.form.validCode.toLowerCase() !== this.validCode.toLowerCase()){
          this.$message.error("验证码错误！")
          return;
        }
      this.form.role = '普通用户'
      this.$refs.from.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请输入用户名和密码!"
          })
          return
        }
        request.post("/user/register", this.form).then(res => {
          console.log(res);
          if (this.form.password != this.form.confirm) {
            this.$message({
              type: "error",
              message: "两次输入的密码不相同！"
            })
            return
          }

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "注册成功"
            })
            router.push("/login")
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
            this.form = {} //刷新表单
          }
        })
      })
      })
    }
  }
}
</script>

<style scoped>
.layout {
  position: absolute;
  left: calc(50% - 200px);
  top: 20%;
  width: 400px;
}

.loginForm {
  text-align: center;
}
</style>