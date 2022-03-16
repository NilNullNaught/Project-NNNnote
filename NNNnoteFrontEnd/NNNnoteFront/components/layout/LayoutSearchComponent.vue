<template>
  <el-input v-model="searchCondition" placeholder="请输入内容" class="input-with-select">
    <el-button slot="append" icon="el-icon-search" @click="search" />
  </el-input>
</template>

<script>
// import noteApi from '@/api/note'

export default {
  name: 'NoteCommentComponent',
  data () {
    return {
      searchCondition: ''
    }
  },
  methods: {
    search () {
      if (this.searchCondition) {
        const param = encodeURIComponent(this.searchCondition)
        decodeURIComponent(param)
      }
    }
  }

}
</script>

<style scoped>

</style>
