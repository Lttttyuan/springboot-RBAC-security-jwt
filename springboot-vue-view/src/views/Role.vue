<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%">
      <el-table-column prop="rid" label="id" sortable width="80"></el-table-column>
      <el-table-column prop="roleName" label="名称"></el-table-column>
      <el-table-column prop="roleComment" label="备注"></el-table-column>
      <el-table-column label="权限菜单">
        <template #default="scope">
          <el-form-item>
            <el-select clearable v-model="scope.row.permissions" multiple placeholder="请选择" style="width: 80%">
              <el-option v-for="item in permissions" :key="item.pid" :label="item.permissionComment" :value="item.pid"></el-option>
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" type="primary" @click="changeRolePermission(scope.row)">保存权限菜单</el-button>
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.rid)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="提示" v-model="dialogVisible" width="30%" :lock-scroll="false" :append-to-body="true">
      <el-form :model="form" label-width="120px">
        <el-form-item label="名称">
          <el-input v-model="form.roleName" style="width: 80%"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.roleComment" style="width: 80%"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </template>
    </el-dialog>

  </div>
</template>

<script>

import request from "@/utils/request";

export default {
  name: 'Role',
  components: {},
  data() {
    return {
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      permissions: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    changeRolePermission(row){
      request.put("/role/changePermission",row).then((res) =>{
        if (res.code === '0'){
          console.log(row.roleName)
          this.$message.success("更新权限成功")
          //当前登录的用户id如果等于当前操作行的角色id，那么就需要重新登陆
          let user = JSON.parse(localStorage.getItem("userInfo"))
          console.log(user.role)
          // 注意：!=-1即为为真，可以找到得情况
          if (user.role.search(row.roleName) != -1){
            this.$router.push('/login')
          }
        }
      })
    },
    load() {
      this.loading = true
      request.get("/role/findPage", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
        console.log(this.tableData)
      })

      request.get("/permission/all/").then(res => {
        this.permissions = res.data
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    save() {
      if (this.form.pid) {  // 更新
        request.put("/role/updata", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      } else {  // 新增
        let userStr = localStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr)
        this.form.author = user.nickName

        request.post("/role/save", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }

    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(rid) {
      console.log(rid)
      request.delete("/role/delete/" + rid).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
