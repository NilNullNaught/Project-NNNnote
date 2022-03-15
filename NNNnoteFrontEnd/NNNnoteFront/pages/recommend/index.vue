<template>
  <div>
    <el-card style="margin:10px" :body-style="{ padding: '10px' }">
      <div style="display:inline-block;width:680px">
        <p class="card-title">
          文章标题一二三四五六七八九十一二三四五六
        </p>
        <p class="card-preview">
          预览：
          一二三四五六七八九十
          二二三四五六七八九十
          三二三四五六七八九十
          四二三四五六七八九十
          五二三四五六七八九十
          六二三四五六七八九十
          七二三四五六七八九十
          八二三四五六七八九十
          九二三四五六七八九十
          十二三四五六七八九十
          八二三四五六七八九十
          九二三四五六七八九十
          十二三四五六七八九十
        </p>

        <div class="centerVertical">
          <el-avatar style="margin-right:5px" :size="20" />
          <span> 作者昵称一二三四五六</span>

          <el-divider direction="vertical" />
          <span><i class="alibaba_icons_good" /> 111111111</span>

          <el-divider direction="vertical" />
          <span><i class="el-icon-chat-line-round" />11111111</span>

          <el-divider direction="vertical" />
          <span><i class="el-icon-star-off" /> 111111111</span>
        </div>
      </div>
      <el-image
        style="margin:0px 5px;width: 160px; height: 120px;dispaly:inline-block !important;"
        fit="scale-down"
      />
    </el-card>
  </div>
</template>

<script>

export default {
  name: 'RecommendIndexPage',
  layout: 'BaseLayout',

  data () {
    return {
    }
  },
  created () {},
  methods: {

  }
}
</script>
<style scoped>
.card-title{
    font-weight: bolder;
    font-size: 20px;
}
.card-preview{
    font-size: 12px;
    color: #999;
    height:42px;
    margin: 15px 0px;
}
.centerVertical{
    display: flex;
    align-items: center;
    font-size: 10px;
    color: #b4b4b4;
}
</style>
