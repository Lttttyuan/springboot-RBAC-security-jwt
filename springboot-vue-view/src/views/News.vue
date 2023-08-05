<template>
  <div style="padding: 10px">
    <!--    搜索区域-->
    <div style="margin: 10px 0;width: 20%;display: flex">
      <el-input v-model="search" placeholder="请输入新闻标题查询" clearable/>
      <el-button style="margin-left: 5px" type="primary" @click="findPage">查询</el-button>
    </div>

    <el-table :data="tableData" border stripe style="width: auto">
      <el-table-column prop="id" label="id" sortable/>
      <el-table-column prop="title" label="标题"/>
      <el-table-column prop="author" label="作者"/>
      <el-table-column prop="time" label="时间"/>


      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="detail(scope.row)">详情</el-button>
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

    <!--    新增弹窗-->
    <!--        lock-scroll	是否在 Dialog 出现时将 body 滚动锁定-->
    <!--        append-to-body	Dialog 自身是否插入至 body 元素上。 嵌套的 Dialog 必须指定该属性并赋值为 true-->
    <el-dialog v-model="dialogVisible" :lock-scroll="false" :append-to-body="true" title="编辑" width="50%">
      <el-form :model="form">
        <el-form-item label="作者：" :label-width="formLabelWidth">
          <el-input v-model="form.author" style="width: 80%" placeholder="默认为：author"/>
        </el-form-item>

        <el-form-item label="时间：" :label-width="formLabelWidth">
          <el-date-picker v-model="form.time" value-format="YYYY-MM-DD HH:mm:ss" type="datetime" clearable/>
        </el-form-item>
      </el-form>

      <!--      引入富文本编辑器组件-->
      <!--      父组件向子组件传递数据-->
      <MyEditor :forms="form" ref="myEditor"></MyEditor>
    </el-dialog>

    <el-dialog v-model="detailsdialog" :lock-scroll="false" :append-to-body="true" title="文本详情" width="50%">

      <el-card>
        <div v-html="details.content" style="min-height: 100px"></div>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import MyEditor from "@/views/MyEditor";

export default {
  name: "News",
  components: {MyEditor},
  data() {
    return {
      form: {},
      formLabelWidth: 75,
      dialogVisible: false,
      detailsdialog: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      search: '',
      tableData: [],
      details: {}
    }
  },
  //加载页面
  created() {
    this.findPage()
  },
  methods: {
    detail(row) {
      this.details = row
      this.detailsdialog = true
    },
    //分页查询
    findPage() {
      request.get("/news/findPage", {
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

      //点击新增跳转至编辑页面
      this.$router.push('/myEditor')
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true

      //配合ref="myEditor"回显文本数据
      // this.$nextTick(() => {
      //   this.$refs.myEditor.dataInfo()
      // })
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
      request.delete("/news/delete/" + id).then(res => {
        if (res.code === '0') {
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