<template>
  <div v-dompurify-html="result" />
</template>

<script>

export default {
  name: 'NoteIndexPage',
  layout: 'BaseLayout',
  data () {
    return {
      handbook: '#### how to use mavonEditor in nuxt.js',
      imgs: [],
      result: ''
    }
  },
  created () {
    const md = require('markdown-it')()
    this.result = md.render(this.handbook)
  }
}
</script>

<style>

</style>
