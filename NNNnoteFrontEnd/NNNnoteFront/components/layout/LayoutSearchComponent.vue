<template>
  <el-input v-model="criteria" placeholder="请输入内容" class="input-with-select" @keyup.enter.native="search">
    <el-button slot="append" icon="el-icon-search" @click="search" />
  </el-input>
</template>

<script>
// import noteApi from '@/api/note'

export default {
  name: 'NoteCommentComponent',
  data () {
    return {
      criteria: ''
    }
  },
  methods: {
    search () {
      if (this.criteria) {
        // 转码搜索条件，防止因特殊字符导致跳转失败
        const param = encodeURIComponent(this.criteria)
        this.$router.push({ path: '/search/' + param })
      }
    }
  }

}
</script>

<style scoped>

</style>
