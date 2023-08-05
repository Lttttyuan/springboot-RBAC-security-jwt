<template>
  <div class="layout">
    <el-tabs type="border-card">
      <el-tab-pane label="登录">
        <el-form ref="from" :model="form" label-position="right" label-width="85px" style="max-width: 460px"
                 :rules="rules" :hide-required-asterisk="true">
          <el-form-item label="用户名：" prop="username">
            <el-input :prefix-icon="Avatar" v-model="form.username"/>
          </el-form-item>
          <el-form-item label="密码：" prop="password">
            <el-input :prefix-icon="Lock" type="password" v-model="form.password" show-password/>
          </el-form-item>

          <!--          验证码功能-->
          <el-form-item label="验证码：">
            <div style="display: flex">
              <el-input v-model="form.validCode" style="width: 50%" placeholder="请输入验证码："/>
              <ValidCode @input="createValidcode"/>
            </div>
          </el-form-item>
        </el-form>
        <div style="margin-left: 60px;display: flex">
          <el-button type="primary" style="width: 40%" @click="login">登录</el-button>
          <el-button type="primary" style="width: 40%" @click="this.$router.push('/register')">前往注册</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>


import {Avatar, Lock} from "@element-plus/icons-vue";
import request from "@/utils/request";
import router from "@/router/index";
import ValidCode from "@/components/ValidCode";
import {activeRouter} from "@/router/permission";

export default {
  name: "Login",
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
  //图标
  setup() {
    return {
      Avatar,
      Lock
    }
  },
  created() {
    sessionStorage.removeItem("userInfo")
  },
  methods: {
    //接收验证码组件提交的四位验证码
    createValidcode(data){
      this.validCode = data
    },
    login() {
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

        request.post("/user/login", this.form).then(res => {
          console.log(res);
          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "登录成功"
            })
            sessionStorage.setItem("userInfo",JSON.stringify(res.data)) //缓存用户信息

            const permissions = res.data.permissions;
            //初始化路由信息
            activeRouter(permissions)

            console.log(router.getRoutes())
            router.push("/home") //登录成功后进行页面跳转
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
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