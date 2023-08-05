<template>
  <div>
    <div style="width: 35%;margin: 40px;border: solid cornflowerblue">
      <el-form :model="form" :label-width="formLabelWidth" style="padding: 20px" :dataformatas="tabledatas">
        <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
          <el-input v-model="form.username"/>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth" prop="password">
          <el-input v-model="form.password"/>
        </el-form-item>

        <el-form-item label="昵称" :label-width="formLabelWidth" prop="nickName">
          <el-input v-model="form.nickName"/>
        </el-form-item>

        <el-form-item label="年龄" :label-width="formLabelWidth" prop="age">
          <el-input v-model="form.age"/>
        </el-form-item>
        <el-form-item label="性别" :label-width="formLabelWidth" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址" :label-width="formLabelWidth" prop="sex">
          <el-input v-model="form.address"/>
        </el-form-item>
        <el-form-item label="角色" :label-width="formLabelWidth" prop="sex">
          <el-input v-model="form.role" disabled/>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="update">保存</el-button>
        </div>
      </el-form>

    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  name: "Person",
  data() {
    return {
      form: {},
      formLabelWidth:60
    }
  },
  created() {
    let str = sessionStorage.getItem("userInfo") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    update() {
      request.put("/user/updata", this.form).then(res => {
        console.log(res)
        if (res.code === '0') {
          ElMessage.success("更新成功")
          sessionStorage.setItem("userInfo", JSON.stringify(this.form))
          // 触发Layout更新用户信息
          this.$emit("userInfo")
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>