<template>
  <div>
    login 中间内容
  </div>
</template>

<script>
import testApi from '@/api/test'

export default {
  name: 'LoginPage',
  layout: 'SignLayout',

  data () {
    return {

    }
  },
  created () {
    testApi.getTestData().then((response) => {
      alert(response.data.data.data)
    })
  }
}
</script>
