<template>
  <div>
    <el-row align="middle" type="flex">
      <el-col :span="2">
        <el-avatar :src="avatar" :size="50" />
      </el-col>
      <el-col :span="23">
        <el-input
          v-model="commentContent"
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
        <el-button size="small" style="margin:5px 0px 5px 5px;" type="primary" @click="postComment(null,null,null,null,null)">
          发送
        </el-button>
      </el-col>
    </el-row>
    <el-divider />
    <el-card shadow="never" style="margin:5px;">
      <el-row align="middle" type="flex">
        <el-col :span="4">
          <el-divider direction="vertical" />
          评论总数 {{ commentCount }}
        </el-col>

        <el-col :offset="16" :span="4">
          <el-select v-model="value" size="mini" placeholder="请选择" @change="sortBy">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.value"
              :value="item.value"
            />
          </el-select>
        </el-col>
      </el-row>

      <el-card
        v-for="(item) in items"
        :key="item.id"
        :body-style="{padding: '10px'}"
        shadow="never"
        style="margin:5px;font-size:14px;"
      >
        <div class="centerVertical" style="position:relative;">
          <nuxt-link :to="'/visitor/'+item.userId">
            <el-avatar size="small" :src="item.avatar" />
          </nuxt-link>

          <nuxt-link :to="'/visitor/'+item.userId">
            <el-button type="text" style="margin-left:10px;">
              {{ item.nickname }}
            </el-button>
          </nuxt-link>

          <p style="position:absolute;float:right;right:0px">
            {{ item.gmtCreate }}
          </p>
        </div>
        <div style="margin-left:40px">
          {{ item.content }}
        </div>
        <div style="margin-left:40px">
          <el-button type="text" size="small" @click="likeComment(item.id,item)">
            <i :ref="`like`+item.id" class="alibaba_icons_good" />{{ item.likes }}
          </el-button>

          <el-button type="text" size="small" @click="openReplyInput(item.id)">
            <i class="alibaba_icons_rely" /> 回复
          </el-button>
        </div>

        <div v-show="false" :ref="item.id" style="margin-left:40px">
          <el-input v-model="item.inputValue" size="small" :placeholder="'回复 ' + item.nickname">
            <el-button slot="append" @click="postComment(item.inputValue,item.id,item.id)">
              发送
            </el-button>
          </el-input>
        </div>

        <div v-for="(replyItem) in repies[String(item.id)]" :key="replyItem.id" style="margin:10px 0px;">
          <div class="centerVertical" style="margin-left:40px">
            <nuxt-link :to="'/visitor/'+item.userId">
              <el-avatar :src="replyItem.avatar" size="small" />
            </nuxt-link>

            <nuxt-link :to="'/visitor/'+item.userId">
              <el-button style="padding:0px;margin-left:5px;" type="text">
                {{ replyItem.nickname }}
              </el-button>
            </nuxt-link>

            <div v-if="replyItem.replyNickname">
              <span style="margin:0 5px;">回复</span>

              <nuxt-link :to="'/visitor/'+replyItem.replyUserId">
                <el-button style="padding:0px" type="text">
                  {{ replyItem.replyNickname }}
                </el-button>
              </nuxt-link>
            </div>
          </div>
          <div style="margin-left:80px">
            {{ replyItem.content }}
          </div>
          <div style="margin-left:80px">
            <el-button type="text" size="small" @click="likeComment(replyItem.id,replyItem)">
              <i :ref="`like`+replyItem.id" class="alibaba_icons_good" />{{ replyItem.likes }}
            </el-button>

            <el-button type="text" size="small" @click="openReplyInput(replyItem.id)">
              <i class="alibaba_icons_rely" /> 回复
            </el-button>
          </div>

          <div v-show="false" :ref="replyItem.id" style="margin-left:80px">
            <el-input v-model="replyItem.inputValue" size="small" :placeholder="'回复 '+replyItem.replyNickname">
              <el-button slot="append" @click="postComment(replyItem.inputValue,item.id,replyItem.id,replyItem.nickname,replyItem.userId)">
                发送
              </el-button>
            </el-input>
          </div>
        </div>

        <div v-if="item.replyCount >3 && repies[String(item.id)].length <= 3" class="centerHorizontal">
          <el-button type="text" @click="getReplies(item.id)">
            更多评论
          </el-button>
        </div>
      </el-card>
    </el-card>

    <el-row justify="center" type="flex">
      <el-pagination
        layout="prev, pager, next"
        :current-page="pager.current"
        :page-size="pager.limit"
        :total="pager.total"
        @current-change="getComments"
      />
    </el-row>
  </div>
</template>

<script>
import qs from 'qs'
import noteApi from '@/api/note'
export default {
  name: 'NoteCommentComponent',
  props: {
    userId: {
      type: String,
      required: false,
      default: ''
    },
    avatar: {
      type: String,
      required: false,
      default: ''
    },
    nickname: {
      type: String,
      required: false,
      default: ''
    },
    noteId: {
      type: String,
      required: true
    },
    commentCount: {
      type: Number,
      required: false,
      default: 0
    }
  },
  data () {
    return {
      value: '点赞数',
      options: [
        { value: '点赞数' },
        { value: '时间正序' },
        { value: '时间倒序' }
      ],
      items: null,
      repies: null,
      pager: {
        current: 1,
        limit: 10,
        sortCondition: 'likes',
        total: null
      },
      commentContent: ''
    }
  },
  created () {
    this.getComments()
  },
  methods: {
    getComments (page = 1) {
      this.pager.current = page
      const param = {
        noteId: this.noteId,
        current: this.pager.current,
        limit: this.pager.limit,
        sortCondition: this.pager.sortCondition
      }

      noteApi.getComments(param).then((response) => {
        this.items = response.data.data.items
        this.repies = response.data.data.replies
        this.pager.total = response.data.data.total
        this.pager.current = response.data.data.current
        this.pager.limit = response.data.data.size
      })
    },
    sortBy (value) {
      switch (value) {
        case '点赞数':
          this.pager.sortCondition = 'likes'
          break
        case '时间正序':
          this.pager.sortCondition = 'timeAsc'
          break
        case '时间倒序':
          this.pager.sortCondition = 'timeDesc'
          break
        default:
          this.pager.sortCondition = 'likes'
      }
      this.getComments(1)
    },
    // 展开回复输入框
    openReplyInput (id) {
      if (this.$refs[`${id}`][0].style.display === 'none') {
        this.$refs[`${id}`][0].style.display = 'block'
      } else {
        this.$refs[`${id}`][0].style.display = 'none'
      }
    },
    // 发送评论
    postComment (inputValue, replyCommentId, closeInputId, replyNickname, replyUserId) {
      const params = {
        noteId: this.noteId,
        nickname: this.nickname,
        avatar: this.avatar,
        content: this.commentContent
      }

      if (inputValue != null) {
        params.content = inputValue
        if (replyCommentId != null) {
          params.replyCommentId = replyCommentId
        }
        if (replyUserId != null) {
          params.replyUserId = replyUserId
          params.replyNickname = replyNickname
        }
      }

      noteApi.postComment(qs.stringify(params)).then((response) => {
        if (inputValue != null) {
          this.getReplies(replyCommentId)
          inputValue = null
          this.openReplyInput(closeInputId)
        } else {
          this.getComments()
          this.commentContent = ''
        }
      })
    },
    // 获取评论下的所有回复
    getReplies (id) {
      const param = { replyCommentId: id }
      noteApi.getReplies(param).then((response) => {
        this.repies[String(id)] = response.data.data.data
      })
    },
    // 笔记点赞与取消
    likeComment (id, item) {
      const dataform = { commentId: id }
      noteApi.likeComment(qs.stringify(dataform)).then((response) => {
        if (response.data.code === 20000) {
          item.likes = response.data.data.likes
          if (!response.data.data.exist) {
            this.$refs[`like${id}`][0].setAttribute('class', 'alibaba_icons_good-fill')
          } else {
            this.$refs[`like${id}`][0].setAttribute('class', 'alibaba_icons_good')
          }
        }
      })
    }
  }

}
</script>

<style scoped>
</style>
