<template>
  <div>
    <el-menu
        style="min-height: calc(100vh - 50px)"
        :default-active="this.$route.path"
        router
        class="el-menu-vertical-demo"
    >

      <el-menu-item :index="menu.permissionPath" v-for="menu in user.permissions" :key="menu.pid">
        <template #title>
          <el-icon :size="20">
            <component :is="menu.icon" />
          </el-icon>
          <span>{{menu.permissionComment}}</span>
        </template>
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
    let userStr = localStorage.getItem("userInfo") || "{}"
    this.user = JSON.parse(userStr)

    // 请求服务器，确认当前登录用户的合法信息
    // request.get("/user/findId/" + this.user.id).then(res =>{
    //   if (res.code === '0'){
    //     this.user = res.data
    //   }
    // })
  }
}
</script>

<style scoped>

</style>