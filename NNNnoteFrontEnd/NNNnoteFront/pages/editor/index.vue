<template>
  <div class="mavonEditor">
    <no-ssr>
      <mavon-editor
        ref="md"
        v-model="handbook"
        :toolbars="markdownOption"
        @imgAdd="addImg"
        @imgDel="delImg"
        @save="save"
      />
    </no-ssr>

    <el-dialog
      title="请选择文件夹"
      :visible.sync="saveDialog.visible"
      width="30%"
      :before-close="handleClose"
      center
    >
      <el-row style="height:calc(30vh);overflow-y:scroll">
        <el-col :span="24" align="center">
          <el-radio-group v-model="saveDialog.selectFolder">
            <el-radio v-for="folder in folders" :key="folder.id" :label="folder.id" border style="width:300px;margin:5px">
              {{ folder.name }}
            </el-radio>
          </el-radio-group>
        </el-col>
      </el-row>

      <span slot="footer" class="dialog-footer">
        <el-button @click="saveDialog.visible = false">取 消</el-button>
        <el-button type="primary">
          创建文件夹
        </el-button>
        <el-button type="primary" @click="saveDialog.visible = false">保存</el-button>

      </span>
    </el-dialog>
  </div>
</template>

<script>
import 'mavon-editor/dist/css/index.css'

export default {
  name: 'EditorIndexPage',
  data () {
    return {
      folders: [
        {
          id: 1,
          name: '默认文件夹'
        },
        {
          id: 2,
          name: '新建文件夹'
        },
        {
          id: 3,
          name: '新建文件夹'
        },
        {
          id: 4,
          name: '新建文件夹'
        },
        {
          id: 5,
          name: '新建文件夹'
        },
        {
          id: 6,
          name: '新建文件夹'
        },
        {
          id: 7,
          name: '新建文件夹'
        },
        {
          id: 8,
          name: '新建文件夹'
        },
        {
          id: 9,
          name: '新建文件夹'
        },
        {
          id: 10,
          name: '新建文件夹'
        }

      ],
      handbook: '#### how to use mavonEditor in nuxt.js',
      imgs: [],
      saveDialog: {
        visible: false,
        selectFolder: 1
      },
      markdownOption: {
        bold: true, // 粗体
        italic: true, // 斜体
        header: true, // 标题
        underline: true, // 下划线
        strikethrough: true, // 中划线
        mark: true, // 标记
        superscript: true, // 上角标
        subscript: true, // 下角标
        quote: true, // 引用
        ol: true, // 有序列表
        ul: true, // 无序列表
        link: true, // 链接
        imagelink: true, // 图片链接
        code: true, // code
        table: true, // 表格
        fullscreen: true, // 全屏编辑
        readmodel: true, // 沉浸式阅读
        htmlcode: true, // 展示html源码
        help: true, // 帮助
        /* 1.3.5 */
        undo: true, // 上一步
        redo: true, // 下一步
        trash: true, // 清空
        save: true, // 保存（触发events中的save事件）
        /* 1.4.2 */
        navigation: true, // 导航目录
        /* 2.1.8 */
        alignleft: true, // 左对齐
        aligncenter: true, // 居中
        alignright: true, // 右对齐
        /* 2.2.1 */
        subfield: true, // 单双栏模式
        preview: true // 预览
      }
    }
  },
  methods: {
    save () {
      this.saveDialog.visible = true
    },
    addImg (pos, file) {
      this.$refs.md.$img2Url(pos, file.name)
    },
    delImg (pos) {
      alert(pos)
    }
  }
}
</script>

<style>
.mavonEditor {
  width: 100%;
  height: 100%;
}
</style>
