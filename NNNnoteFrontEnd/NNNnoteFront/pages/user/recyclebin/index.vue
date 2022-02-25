<template>
  <div class="UserRecyclebinIndex-height">
    <el-row>
      <el-col :offset="9" :span="6" align="center">
        <span style="font-size: 20px;font-weight:bolder;">回收站</span>
      </el-col>
    </el-row>

    <div style="margin-bottom: 10px">
      <el-button type="primary" plain @click="toggleSelection()">
        取消选择
      </el-button>
      <el-button type="danger" plain>
        删除选中
      </el-button>
    </div>
    <el-table
      ref="multipleTable"
      :data="list.result"
      tooltip-effect="dark"
      style="width: 100%"
      stripe
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column
        label="标题"
        width="160"
      >
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column
        label="预览"
        width="320"
      >
        <template slot-scope="scope">
          {{ scope.row.preview }}…
        </template>
      </el-table-column>

      <el-table-column
        prop="address"
        label="所属文件夹"
        width="160"
      />
      <el-table-column
        label="剩余时间"
        width="160"
      >
        <template slot-scope="scope">
          {{ scope.row.date }}
        </template>
      </el-table-column>

      <el-table-column
        fixed="right"
        label="操作"
        width="150"
      >
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleClick(scope.row)">
            还原
          </el-button>
          <el-button type="text" size="small">
            彻底删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row justify="center" type="flex">
      <el-pagination
        layout="prev, pager, next"
        :current-page="list.current"
        :page-size="list.limit"
        :total="list.total"
      />
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'UserRecyclebinIndexPage',
  layout: 'BaseLayout',
  data () {
    return {
      list: {
        keyword: '',
        current: 1,
        limit: 10,
        total: null,
        result: [
          {
            date: '2016-05-03 16:30',
            name: '一',
            preview: '一',
            address: '一'
          }
          //, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }, {
          //   date: '2016-05-03 16:30',
          //   name: '一',
          //   preview: '一',
          //   address: '一'
          // }
        //  {
        //   date: '2016-05-03 16:30',
        //   name: '一二三四五六七八九十一二三四五六七八九十',
        //   preview: '一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十',
        //   address: '一二三四五六七八九十'
        // }, {
        //   date: '2016-05-03 16:30',
        //   name: '一二三四五六七八九十一二三四五六七八九十',
        //   preview: '一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十',
        //   address: '一二三四五六七八九十'
        // }, {
        //   date: '2016-05-03 16:30',
        //   name: '一二三四五六七八九十一二三四五六七八九十',
        //   preview: '一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十',
        //   address: '一二三四五六七八九十'
        // }, {
        //   date: '2016-05-03 16:30',
        //   name: '一二三四五六七八九十一二三四五六七八九十',
        //   preview: '一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十',
        //   address: '一二三四五六七八九十'
        // }, {
        //   date: '2016-05-03 16:30',
        //   name: '一二三四五六七八九十一二三四五六七八九十',
        //   preview: '一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十一二三四五六七八九十',
        //   address: '一二三四五六七八九十'
        // }
        ]
      },
      multipleSelection: []
    }
  },

  methods: {
    toggleSelection (rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    }
  }
}
</script>

<style>
.UserRecyclebinIndex-height{
  min-height: calc(75vh);
}
</style>
