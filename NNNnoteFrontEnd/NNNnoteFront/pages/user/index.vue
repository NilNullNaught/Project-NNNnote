<template>
  <div>
    <el-button type="primary">
      新建文件夹
    </el-button>
    <el-button-group>
      <el-button type="primary">
        <i class="el-icon-edit el-icon--left" />

        编辑文件夹
      </el-button>

      <el-button type="primary">
        <i class="el-icon-delete el-icon--left" />
        删除文件夹
      </el-button>
    </el-button-group>

    <div style="margin:10px">
      <el-checkbox v-model="selectAll" :indeterminate="isIndeterminate" label="全选" @change="handleCheckAll" />
    </div>
    <el-divider />

    <el-row>
      <el-col v-for="(o) in list" :key="o.id" :span="4">
        <el-card :id="'no'+o.id" class="userindex-el-card" shadow="hover" style="position: relative;">
          <div style="position:absolute;top: 10px;left: 10px;">
            <input v-model="o.ischecked" type="checkbox" @change="handleChecked(o)">
          </div>
          <div style="display: flex;justify-content:center;">
            <img src="~/assets/img/mine/folder.png" alt>
          </div>
          <div style="display: flex;justify-content:center;">
            <span>文件夹名</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
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
      isIndeterminate: false
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
  methods: {
    // 选取全部
    handleCheckAll () {
      this.checkedList = (this.selectAll ? this.list : [])
      this.list.forEach((o) => {
        o.ischecked = this.selectAll
        this.addOrRemoveStyle(o.id, this.selectAll)
      })
    },
    handleChecked (o) {
      alert(o.ischecked)
      this.addOrRemoveStyle(o.id, o.ischecked)
      if (this.checkedList.includes(o)) {
        this.checkedList = this.checkedList.splice(this.checkedList.findIndex(O => O === o), 0)
      } else {
        this.checkedList.push(o)
      }
    },
    // 修改样式
    addOrRemoveStyle (id, checked) {
      const element = document.getElementById('no' + id)
      if (checked) {
        element.classList.add('userindex-el-card--select')
      } else {
        element.classList.remove('userindex-el-card--select')
      }
    }
  }
}
</script>
<style>
.userindex-el-button{
  margin-bottom: 10px;
}
.userindex-el-card{
  margin:10px;
}
.userindex-el-card:hover{
  background-color: #f1f5fa;
}
.userindex-el-card--select{
  border: 1px solid #90d8ff;
  background-color: #f1f5fa;
}
.el-divider--horizontal{
   margin:10px;
}
</style>
