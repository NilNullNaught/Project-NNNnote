<template>
  <div>
    <h1>{{ note.title }}</h1>
    <div v-dompurify-html="note.text" />
  </div>
</template>

<script>
import noteApi from '@/api/note'
export default {
  name: 'NoteIdPage',
  layout: 'BaseLayout',
  data () {
    return {
      note: {
        title: '',
        text: null
      }
    }
  },
  created () {
    noteApi.getNoteInfoToRead(this.$route.params.id).then((response) => {
      if (response.data.code === 20000) {
        this.note.title = response.data.data.noteInfo.title
        const md = require('markdown-it')()
        this.note.text = md.render(response.data.data.noteText.text)
      }
    })
  }
}

</script>

<style scoped>

</style>
