<template>
  <div>
    <h1 class="centerHorizontal">
      NNN 笔记
    </h1>
    <p
      style="    font-size: 18px;
    line-height: 28px;
    color: #888;
    margin-top: 10px;
    margin-bottom:20px;"
      class="centerHorizontal"
    >
      属于每个人的线上笔记本。
    </p>

    <div align="center">
      <img class="centerHorizontal" :src="require('@/static/img/笔记.png')" style="margin:20px;height:30%;width:30%;display: block;">
    </div>

    <el-row justify="space-around" type="flex">
      <el-col :span="6">
        <el-card :body-style="{ padding: '0px' }">
          <div style="padding: 14px;">
            <h4 class="centerHorizontal">
              纯净
            </h4>
            <div
              style="    font-size: 14px;
    color: #99a9bf;
    padding: 0 25px;
    line-height: 20px;"
            >
              无广告、无弹窗，提供最干净的书写体验。
            </div>
          </div>
          <img :src="require('@/static/img/pure.png')" class="image">
        </el-card>
      </el-col>
      <el-col :span="6" :offset="1">
        <el-card :body-style="{ padding: '0px' }">
          <div style="padding: 14px;">
            <h4 class="centerHorizontal">
              分享
            </h4>
            <div
              style="    font-size: 14px;
    color: #99a9bf;
    padding: 0 25px;
    line-height: 20px;"
            >
              人道待人，乐于分享，发扬 ubuntu 精神。
            </div>
          </div>
          <img :src="require('@/static/img/share.png')" class="image">
        </el-card>
      </el-col>
      <el-col :span="6" :offset="1">
        <el-card :body-style="{ padding: '0px' }">
          <div style="padding: 14px;">
            <h4 class="centerHorizontal">
              成长
            </h4>
            <div
              style="    font-size: 14px;
    color: #99a9bf;
    padding: 0 25px;
    line-height: 20px;"
            >
              更多功能，更好体验，种子已经种下。
            </div>
          </div>
          <img :src="require('@/static/img/grow.png')" class="image">
        </el-card>
      </el-col>
    </el-row>
    <br>
  </div>
</template>

<script>
export default {
  name: 'IndexPage',
  layout: 'BaseLayout',

  data () {
    return {
      // count: 0
    }
  },
  created () {},
  methods: {
    // addFirstStore2 () {
    //
    // }
  }
}
</script>
<style scoped>
  .image {
    width: 100%;
    display: block;
  }
</style>
