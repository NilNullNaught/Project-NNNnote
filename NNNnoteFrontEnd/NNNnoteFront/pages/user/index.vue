<template>
  <div>
    <el-button-group>
      <el-button type="primary" icon="el-icon-arrow-left">
        上一页
      </el-button>
      <el-button type="primary">
        编辑文件夹
      </el-button>
      <el-button type="primary">
        新建文件夹
      </el-button>
    </el-button-group>

    <el-row>
      <el-col v-for="(o) in 20" :key="o" :span="4">
        <el-card class="my-el-card" style="margin:5px;" shadow="hover">
          <div>
            <el-checkbox v-model="checked" />
          </div>
          <div>
            <img src="~/assets/img/mine/folder.png" alt>
          </div>
          <div>
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

    }
  },
  created () {}
}
</script>
<style>
.my-el-card:hover{
  background-color: #f1f5fa;
  border: 1px solid #90d8ff;
  border-radius: 5px;
}
</style>
