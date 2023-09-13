<template>
  <div style="height: 50px;line-height: 50px;border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 30px;font-weight: bold;color: deepskyblue" @click="$router.push('/user')">后台管理</div>
    <div style="flex: 1"></div>

    <!--    登录退出-->
    <div style="width: 100px">
      <el-dropdown>
        <span class="el-dropdown-link" style="margin-top: 15px;outline: none">
          {{ username }}<el-icon class="el-icon--right"><arrow-down/></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
<!--            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>-->
            <el-dropdown-item @click="exit">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>

import router from "@/router";
import request from "@/utils/request";

export default {
  name: "Header",
  data(){
    return{
      username: JSON.parse(localStorage.getItem("userInfo")).username //获取登录时的用户名
    }
  },
  methods:{
    exit(){
      request.post("/logout").then(( ) =>{
        console.log("退出登录!!!")
      })
      router.push('/login')
    }
  }
}
</script>


<style scoped>

</style>