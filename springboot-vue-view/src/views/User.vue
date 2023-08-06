<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0;width: 20%;display: flex">
      <el-input v-model="search" placeholder="请输入用户名进行查询" clearable/>
      <el-button style="margin-left: 5px" type="primary" @click="findPage">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: auto">
      <el-table-column prop="id" label="id" sortable width="80"/>
      <el-table-column prop="username" label="用户名"/>
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="nickName" label="昵称"/>
      <el-table-column prop="age" label="年龄"/>
      <el-table-column prop="sex" label="性别"/>
      <el-table-column prop="address" label="地址"/>
      <el-table-column prop="role" label="角色"/>


      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>

          <!--          二次确认弹出消息框-->
          <el-popconfirm
              width="220"
              confirm-button-text="确认"
              cancel-button-text="取消"
              :icon="InfoFilled"
              icon-color="#626AEF"
              title="确认要删除吗？"
              @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--    分页-->
    <div class="demo-pagination-block" style="margin: 8px 0">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20]"
          :small="true"
          :disabled="false"
          :background="true"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </div>

    <!--新增弹窗-->
    <!--    lock-scroll	是否在 Dialog 出现时将 body 滚动锁定-->
    <!--    append-to-body	Dialog 自身是否插入至 body 元素上。 嵌套的 Dialog 必须指定该属性并赋值为 true-->
    <el-dialog v-model="dialogVisible" :lock-scroll="false" :append-to-body="true" title="新增" width="30%">
      <el-form :model="form">
        <el-form-item label="用户名：" :label-width="formLabelWidth">
          <el-input v-model="form.username" style="width: 80%"/>
        </el-form-item>
        <el-form-item label="密码：" :label-width="formLabelWidth">
          <el-input type="password" v-model="form.password" style="width: 80%" show-password/>
        </el-form-item>
        <el-form-item label="昵称：" :label-width="formLabelWidth">
          <el-input v-model="form.nickName" style="width: 80%"/>
        </el-form-item>
        <el-form-item label="年龄：" :label-width="formLabelWidth">
          <el-input v-model="form.age" style="width: 80%"/>
        </el-form-item>

        <el-form-item label="性别：" :label-width="formLabelWidth">
          <el-radio-group v-model="form.sex">
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="地址：" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.address" style="width: 80%"/>
        </el-form-item>

        <el-form-item label="权限管理：" :label-width="formLabelWidth">
          <el-checkbox-group v-model="userRole" style="margin-left: 20px">
            <el-checkbox :label="role.roleName" v-for="role in roleAll" :key="role.rid"/>
          </el-checkbox-group>
        </el-form-item>

      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>


import request from "@/utils/request";

export default {
  name: "User",
  data() {
    return {
      form: {},
      formLabelWidth: 120,
      dialogVisible: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      tableData: [],
      roleAll: [],
      userRole:[]
    }
  },
  //加载页面
  created() {
    this.findPage()
    this.findRoleAll()
  },
  methods: {
    //分页查询
    findPage() {
      request.get("/user/findPage", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    //查询数据库中所有的角色
    findRoleAll() {
      request.get("/role/findPage").then((res) => {
        console.log(res.data)
        this.roleAll = res.data.records
        console.log(this.roleAll)
      })
    },

    //新增
    add() {
      this.dialogVisible = true;
      //清空表单域
      this.form = {}
    },

    //设置用户角色
    changeRole(){
      console.log(this.userRole)
      //将userRole数组转为字符串
      this.form.role = this.userRole.join(',')
      console.log(this.form)
      if (this.form.id) {
        request.put("/userRole/updata/changeUserRole", this.form).then((res) => {
          if (res.code == '0') {
            this.$message.success("角色权限设置成功")
          } else {
            this.$message.error("角色权限设置失败")
          }
        })
      }else {
        request.post("/userRole/save/changeUserRole", this.form).then((res) => {
          if (res.code == '0') {
            this.$message.success("角色权限设置成功")
          } else {
            this.$message.error("角色权限设置失败")
          }
        })
      }
    },
    //确认按钮
    save() {
      this.form.role = this.userRole.join(',')
      if (this.form.id) { //更新
        request.put("/user/updata", this.form).then(res => {
          console.log(res);

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
            this.changeRole()
          } else {
            this.errorInfo(res)
          }
        })
      } else { //新增
        request.post("/user/save", this.form).then(res => {
          console.log(res);

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
            this.changeRole()
          } else {
            this.$message({
              type: "success",
              message: "新增失败"
            })
          }
        })
      }
      this.dialogVisible = false //关闭弹窗
      this.findPage() //刷新表格数据

      //如果当前修改的id为当前登录的用户的id则跳转到登录界面
      let user = JSON.parse(sessionStorage.getItem("userInfo"))
      if (this.form.id == user.id){
        this.$router.push("/login")
      }
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true

      //将role字符串转为数组
      this.userRole = this.form.role.split(',')
    },
    //报错信息弹窗
    errorInfo(res) {
      this.$message({
        type: "error",
        message: res.msg
      })
    },
    //删除
    handleDelete(id) {
      console.log(id)
      request.delete("/user/delete/" + id).then(res => {
        if (res.code == '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.errorInfo(res)
        }
        this.findPage()  //删除之后重新刷新表格数据
      })
    },
    handleSizeChange(pageSize) { //改变当前每页条数触发
      this.pageSize = pageSize
      this.findPage()
    },
    handleCurrentChange(pageNum) { //改变当前页码触发
      this.currentPage = pageNum
      this.findPage()
    }
  }
}

</script>

<style scoped>

</style>