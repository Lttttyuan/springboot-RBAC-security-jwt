<template>
  <div>
    <el-menu
        style="min-height: calc(100vh - 50px)"
        :default-active="this.$route.path"
        router
        class="el-menu-vertical-demo"
    >
<!--      v-if="user.role == 1"-->
      <el-sub-menu index="1" >
        <template #title>
          <span>系统管理</span>
        </template>
        <el-menu-item index="user">用户管理</el-menu-item>
      </el-sub-menu>

      <el-menu-item index="book">
        <span>书籍管理</span>
      </el-menu-item>
      <el-menu-item index="news" v-if="user.role == 2">
        <span>新闻管理</span>
      </el-menu-item>
      <el-menu-item index="Category" v-if="user.role == 1">
        <span>分类管理</span>
      </el-menu-item>

    </el-menu>
  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: "Aside",
  data(){
    return{
      user:{}
    }
  },
  created() {
    let userStr = sessionStorage.getItem("userInfo") || "{}"
    this.user = JSON.parse(userStr)

    // 请求服务器，确认当前登录用户的合法信息
    request.get("/user/findId/" + this.user.id).then(res =>{
      if (res.code === '0'){
        this.user = res.data
      }
    })
  }
}
</script>

<style scoped>

</style>