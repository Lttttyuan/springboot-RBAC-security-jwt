<template>
  <div style="margin: 20px">
    <el-form :model="form">
      <el-form-item label="标题：" :label-width="formLabelWidth">
        <el-input v-model="form.title" style="width: 60%"/>
      </el-form-item>
      <div>
        <el-button type="primary" @click="saveEditor">确认</el-button>

        <el-button type="primary" @click="$router.push('/news')">取消</el-button>
      </div>

    </el-form>

    <div style="border: 1px solid #ccc; margin-top: 10px">
      <Toolbar
          :editor="editorRef"
          :defaultConfig="toolbarConfig"
          :mode="mode"
          style="border-bottom: 1px solid #ccc"
      />
      <Editor
          :defaultConfig="editorConfig"
          :mode="mode"
          v-model="valueHtml"
          style="height: 400px; overflow-y: hidden"
          @onCreated="handleCreated"
          @onChange="handleChange"
          @onDestroyed="handleDestroyed"
          @onFocus="handleFocus"
          @onBlur="handleBlur"
          @customAlert="customAlert"
          @customPaste="customPaste"
      />
    </div>
    <!--    <div style="margin-top: 10px">-->
    <!--      <textarea-->
    <!--          v-model="valueHtml"-->
    <!--          readonly-->
    <!--          style="width: 100%; height: 200px; outline: none"-->
    <!--      ></textarea>-->
    <!--    </div>-->
  </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css';
import {onBeforeUnmount, ref, shallowRef, watch} from 'vue';
import {Editor, Toolbar} from '@wangeditor/editor-for-vue';
import request from "@/utils/request";
import router from "@/router";

export default {
  name: "MyEdtior",
  components: {Editor, Toolbar},
  props: ['forms'],
  data() {
    return {
      form: {}
    }
  },
  created() {
    this.dataInfo()
  },
  watch:{
    forms(newValue,oldValue){
      console.log(newValue)
      console.log(oldValue)
      // this.$nextTick(()=>{
      //   if (oldValue != newValue){
      //     this.dataInfo()
      //   }
      // })
      if (oldValue != newValue){
        this.dataInfo()
      }
    }
  },
  methods: {
    saveEditor() {
      if (this.valueHtml == "<p><br></p>") {
        this.$message.error("请输入文本内容!")
        return
      }
      this.form.content = this.valueHtml

      if (this.form.id) { //更新
        this.form.author = this.forms.author
        this.form.time = this.forms.time
        request.put("/news/updata", this.form).then(res => {
          console.log(res);

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message.error("更新失败")
          }
        })
        router.push("/news")
      } else { //新增
        this.form.author = JSON.parse(sessionStorage.getItem("userInfo")).username //获取登录时的用户名
        request.post("/news/save", this.form).then(res => {
          console.log(res);

          if (res.code == '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "success",
              message: "新增失败"
            })
          }
        })
      }
      router.push("/news")
    },
    dataInfo(){
      if (this.forms != null) {
        this.form.title = this.forms.title
        this.form.id = this.forms.id
        this.valueHtml = this.forms.content
      }
    }
    },
  setup(props) {
    // watch(
    //     () => props.forms,
    //     (newVal, oldVal) => {
    //       console.log('监听基本类型数据forms')
    //       console.log('new', newVal)
    //       console.log('old', oldVal)
    //     });

    // 编辑器实例，必须用 shallowRef，重要！
    const editorRef = shallowRef();

    // 内容 HTML
    const valueHtml = ref("");

    // 模拟 ajax 异步获取内容
    // onMounted(() => {
    //   setTimeout(() => {
    //     valueHtml.value = '';
    //   }, 1500);
    // });

    const toolbarConfig = {};
    const editorConfig = {placeholder: '请输入内容...'};

    // 组件销毁时，也及时销毁编辑器，重要！
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    // 编辑器回调函数
    const handleCreated = (editor) => {
      console.log('created', editor);
      editorRef.value = editor; // 记录 editor 实例，重要！
    };
    // const handleChange = (editor) => {
    //   console.log('change:', editor.getHtml());
    // };
    const handleDestroyed = (editor) => {
      console.log('destroyed', editor);
    };
    const handleFocus = (editor) => {
      console.log('focus', editor);
    };
    const handleBlur = (editor) => {
      console.log('blur', editor);
    };
    const customAlert = (info, type) => {
      alert(`【自定义提示】${type} - ${info}`);
    };
    const customPaste = (editor, event, callback) => {
      console.log('ClipboardEvent 粘贴事件对象', event);

      // 自定义插入内容
      editor.insertText('xxx');

      // 返回值（注意，vue 事件的返回值，不能用 return）
      callback(false); // 返回 false ，阻止默认粘贴行为
      // callback(true) // 返回 true ，继续默认的粘贴行为
    };


    return {
      editorRef,
      mode: 'default',
      valueHtml,
      toolbarConfig,
      editorConfig,
      handleCreated,
      // handleChange,
      handleDestroyed,
      handleFocus,
      handleBlur,
      customAlert,
      customPaste
    };
  },
};
</script>