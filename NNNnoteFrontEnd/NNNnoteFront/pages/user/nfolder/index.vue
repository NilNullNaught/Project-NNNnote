<template>
  <div>
    <el-container class="userindex-height">
      <el-main>
        <el-row>
          <el-col :span="16">
            <el-button
              type="primary"
              :disabled="searchKeyWord !== ''"
            >
              新建文件夹
            </el-button>
            <el-button-group
              v-if="checkedList.length !== 0"
              ref="userIndex_ebg"
              :disabled="searchKeyWord !== ''"
            >
              <el-button type="primary">
                <i class="el-icon-edit el-icon--left" />
                编辑文件夹
              </el-button>

              <el-button type="primary">
                <i class="el-icon-delete el-icon--left" />
                删除文件夹
              </el-button>
            </el-button-group>
          </el-col>
          <el-col :span="8">
            <el-input
              v-model="searchKeyWord"
              placeholder="请输入内容"
              clearable
            >
              <i slot="append" class="el-input__icon el-icon-search" @click="search()" />
            </el-input>
          </el-col>
        </el-row>

        <div style="margin:10px">
          <el-checkbox
            v-model="selectAll"
            :disabled="searchKeyWord !== ''"
            :indeterminate="isIndeterminate"
            :label="selectSum"
            @change="handleCheckAll"
          />
        </div>

        <el-divider />

        <el-row v-if="!searchKeyWord">
          <el-col v-for="(o) in list" :key="o.id" :span="4">
            <el-card :id="'no'+o.id" class="userindex-el-card" shadow="hover" style="position: relative;">
              <input v-if="o.id !== 1" v-model="o.ischecked" style="position:absolute;top: 10px;left: 10px;" type="checkbox" @change="handleChecked(o)">
              <span />
              <div @click="route(o.id)">
                <div style="display: flex;justify-content:center;">
                  <img src="~/assets/img/mine/folder.png" alt>
                </div>
                <div style="display: flex;justify-content:center;">
                  <span>文件夹名</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row v-if="searchKeyWord">
          <el-col v-for="(o) in list" :key="o.id" :span="4">
            <el-card :id="'no'+o.id" class="userindex-el-card" shadow="hover" style="position: relative;">
              <input v-model="o.ischecked" style="position:absolute;top: 10px;left: 10px;" type="checkbox" @change="handleChecked(o)">
              <span />
              <div @click="route($event,o.id)">
                <div style="display: flex;justify-content:center;">
                  <img src="~/assets/img/mine/note.png" alt>
                </div>
                <div style="display: flex;justify-content:center;">
                  <span>笔记名</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>

      <el-footer height="30px">
        <el-row justify="center" type="flex">
          <el-pagination
            v-if="!searchKeyWord"
            layout="prev, pager, next"
            :total="50"
          />
          <el-pagination
            v-if="searchKeyWord"
            layout="prev, pager, next"
            :total="50"
          />
        </el-row>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'UserIndexPage',
  layout: 'BaseLayout',

  data () {
    return {
      list: [],
      checkedList: [],
      selectAll: false,
      isIndeterminate: false,
      selectSum: '全选',
      searchKeyWord: ''
    }
  },
  created () {
    for (let i = 1; i <= 10; i++) {
      this.list.push({
        id: i
      })
    }
    this.list.forEach((o) => {
      o.ischecked = false
    })
  },
  mounted () {
  },
  methods: {
    // 选中部分
    // 选取全部
    handleCheckAll () {
      if (this.selectAll) {
        this.checkedList = [...this.list]
        this.checkedList.splice(0, 1)
        this.selectSum = '已选中' + this.checkedList.length + '个文件夹'
      } else {
        this.checkedList = []
        this.selectSum = '全选'
      }
      this.checkedList.forEach((o) => {
        o.ischecked = this.selectAll
        this.addOrRemoveStyle(o.id, this.selectAll)
      })
    },
    // 选取单个
    handleChecked (o) {
      this.addOrRemoveStyle(o.id, o.ischecked)
      if (this.checkedList.includes(o)) {
        this.checkedList.splice(this.checkedList.findIndex(O => O === o), 1)
      } else {
        this.checkedList.push(o)
      }
      if (this.checkedList.length === 0) {
        this.selectSum = '全选'
      } else {
        this.selectSum = '已选中' + this.checkedList.length + '个文件夹'
      }
    },
    // 选中后修改样式以及取消选中后去除样式
    // 问题：通过 JS 设置 visibility 后，再通过 css 设置 visibility 会失效
    addOrRemoveStyle (id, checked) {
      const element = document.getElementById('no' + id)
      // const input = element.getElementsByTagName('input')

      if (checked) {
        element.classList.add('userindex-el-card--select')
        // input[0].style.visibility = 'visible'
      } else {
        element.classList.remove('userindex-el-card--select')
        // input[0].style.visibility = 'hidden'
      }
    },
    route (id) {
      // 如果没有被选中的文件夹，则进行跳转
      if (this.checkedList.length === 0) {
        this.$router.push({ path: '/user/nfolder/' + id })
      }
    },
    search () {
      this.$message.error('搜索功能尚未完成')
    }
  }
}
</script>
<style>
.userindex-height{
  min-height: calc(85vh);
}
.userindex-el-button{
  margin-bottom: 10px;
}
.userindex-el-card{
  margin:10px;
}
.userindex-el-card:hover{
  background-color: #f1f5fa;
}
.userindex-el-card input {
  visibility: hidden;
}
.userindex-el-card:hover input {
  visibility: visible;
}
.userindex-el-card--select{
  border: 1px solid #90d8ff;
  background-color: #f1f5fa;
}
.el-divider--horizontal{
   margin:10px;
}
</style>
