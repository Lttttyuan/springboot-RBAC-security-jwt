<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-popconfirm
          title="确定删除吗？"
          @confirm="deleteBatch"
      >
        <template #reference>
          <el-button type="danger" >批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0;width: 20%;display: flex">
      <el-input v-model="search" placeholder="请输入书籍名称进行查询" clearable/>
      <el-button style="margin-left: 5px" type="primary" @click="findPage">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: auto" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="id" label="id" sortable width="80"/>
      <el-table-column prop="name" label="书籍名称"/>

      <el-table-column label="封面">
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.cover"
              :zoom-rate="1.2"
              :preview-src-list="[scope.row.cover]"
              :initial-index="4"
              preview-teleported="true"
              fit="cover"
          />
        </template>
      </el-table-column>

      <el-table-column prop="price" label="价格"/>
      <el-table-column prop="author" label="年龄"/>
      <el-table-column prop="createTime" label="出版时间"/>

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
    <el-dialog v-model="dialogVisible" :lock-scroll="false" :append-to-body="true" title="编辑" width="30%">
      <el-form :model="form">
        <el-form-item label="书籍名称：" :label-width="formLabelWidth">
          <el-input v-model="form.name" style="width: 80%"/>
        </el-form-item>
        <el-form-item label="价格：" :label-width="formLabelWidth">
          <el-input v-model="form.price" style="width: 80%"/>
        </el-form-item>
        <el-form-item label="作者：" :label-width="formLabelWidth">
          <el-input v-model="form.author" style="width: 80%" placeholder="默认为：author"/>
        </el-form-item>

        <el-form-item label="出版时间：" :label-width="formLabelWidth">
          <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" type="date" clearable/>
        </el-form-item>

        <el-form-item label="封面：" :label-width="formLabelWidth">
          <el-upload limit="1" :action="filesUploadUrl" ref="upload" :on-success="fileUploadSuccess">
            <el-button type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">
                只能上传jpg/png文件,且不能超过500kb.
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>


      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </span>
      </template>

      <template>
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
  name: "Book",
  components: {},
  data() {
    return {
      user: {},
      form: {},
      formLabelWidth: 120,
      dialogVisible: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      tableData: [],
      filesUploadUrl: "http://" + window.server.filesUploadUrl + ":8088/files/upload",
      ids: [] //当前选框选中的值
    }
  },
  //加载页面
  created() {
    this.findPage()

    let userStr = sessionStorage.getItem("userInfo") || "{}"
    this.user = JSON.parse(userStr)

    // 请求服务器，确认当前登录用户的合法信息
    request.get("/user/findId/" + this.user.id).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
  },
  methods: {
    fileUploadSuccess(res){
      this.form.cover = res.data
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据！")
        return
      }
      request.post("/book/deleteBatch", this.ids).then(res => {
        if (res.code === '0') {
          this.$message.success("批量删除成功")
          this.findPage()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => v.id)   // [{id,name}, {id,name}] => [id,id]
    },

    //分页查询
    findPage() {
      request.get("/book/findPage", {
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
    //新增
    add() {
      this.dialogVisible = true;
      //清空表单域
      this.form = {}

      this.$nextTick(() => {
        this.$refs.upload.clearFiles() //清除历史文件列表
      })
    },
    //新增
    save() {
      if (this.form.id) { //更新
        request.put("/book/updata", this.form).then(res => {
          console.log(res);

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.errorInfo(res)
          }
        })
      } else { //新增
        request.post("/book/save", this.form).then(res => {
          console.log(res);

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "success",
              message: "更新失败"
            })
          }
        })
      }
      this.dialogVisible = false //关闭弹窗
      this.findPage() //刷新表格数据
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true

      this.$nextTick(() => {
        this.$refs.upload.clearFiles() //清除历史文件列表
      })
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
      request.delete("/book/delete/" + id).then(res => {
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