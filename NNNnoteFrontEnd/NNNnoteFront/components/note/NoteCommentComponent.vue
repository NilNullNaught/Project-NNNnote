<template>
  <div>
    <el-row align="middle" type="flex">
      <el-col :span="2">
        <el-avatar :size="50" />
      </el-col>
      <el-col :span="23">
        <el-input
          v-model="textarea"
          type="textarea"
          :rows="2"
          placeholder="请输入评论"
          maxlength="100"
          show-word-limit
        />
      </el-col>
    </el-row>
    <el-row justify="end" type="flex">
      <el-col :span="2">
        <el-button size="small" style="margin:5px 0px 5px 5px;" type="primary">
          发送
        </el-button>
      </el-col>
    </el-row>
    <el-divider />
    <el-card shadow="never" style="margin:5px;">
      <el-row align="middle" type="flex">
        <el-col :span="4">
          <el-divider direction="vertical" />
          评论数 12
        </el-col>

        <el-col :offset="16" :span="4">
          <el-select v-model="value" size="mini" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.value"
              :value="item.value"
            />
          </el-select>
        </el-col>
      </el-row>

      <el-card :body-style="{padding: '10px'}" shadow="never" style="margin:5px;font-size:14px;">
        <el-row align="middle" type="flex">
          <el-col :span="1">
            <el-avatar size="small" />
          </el-col>
          <el-col :span="4">
            <el-button type="text">
              一二三四五六七八九十
            </el-button>
          </el-col>
          <el-col :offset="14" :span="5" align="right">
            2022-2-22 22:22
          </el-col>
        </el-row>
        <el-row align="middle" type="flex">
          <el-col :offset="1" :span="23">
            一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十
            一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十
            一二三四五六七八九十一二三四五六七八九十
          </el-col>
        </el-row>
        <el-row align="middle" type="flex">
          <el-col :offset="1" :span="23">
            <el-button type="text" size="small">
              <i class="alibaba_icons_good" />10000000000
            </el-button>
            <el-button type="text" size="small">
              <i class="alibaba_icons_bad" />10000000000
            </el-button>
            <el-button type="text" size="small" @click="rely()">
              <i class="alibaba_icons_rely" /> 回复
            </el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :offset="1" :span="23">
            <el-input size="small" placeholder="回复  一二三四五六七八九十">
              <el-button slot="append">
                发送
              </el-button>
            </el-input>
          </el-col>
        </el-row>
        <div style="margin:10px 0px;">
          <el-row align="middle" type="flex">
            <el-col :offset="1" :span="1">
              <el-avatar size="small" />
            </el-col>
            <el-col :span="22">
              <el-button style="padding:0px" type="text">
                一二三四五六七八九十
              </el-button>
              回复
              <el-button style="padding:0px" type="text">
                一二三四五六七八九十
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="2" :span="22">
              一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十
              一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十
              一二三四五六七八九十一二三四五六七八九十
            </el-col>
          </el-row>
          <el-row align="middle" type="flex">
            <el-col :offset="2" :span="22">
              <el-button type="text" size="small">
                <i class="alibaba_icons_good" />10000000000
              </el-button>
              <el-button type="text" size="small">
                <i class="alibaba_icons_bad" />10000000000
              </el-button>
              <el-button type="text" size="small" @click="rely()">
                <i class="alibaba_icons_rely" /> 回复
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="2" :span="22">
              <el-input size="small" placeholder="回复  一二三四五六七八九十">
                <el-button slot="append">
                  发送
                </el-button>
              </el-input>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script>
import '~/assets/icon/iconfont.css'

export default {
  name: 'NoteCommentComponent',
  data () {
    return {
      textarea: '',
      value: '时间正序',
      options: [
        { value: '时间正序' },
        { value: '时间倒序' },
        { value: '点赞数量' }]
    }
  }

}
</script>

<style scoped>
.el-button+.el-button {
    margin-left: 0px;
}
</style>
