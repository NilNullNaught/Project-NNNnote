export const state = () => ({
  userInfo: { },
  userMember: {},
  dataCount: {
    noteCount: 0,
    draftCount: 0,
    deletedCount: 0
  }
})
export const mutations = {
  updateUserInfo (state, { data }) {
    state.userInfo = data
  },
  updateUserMember (state, { data }) {
    state.userMember = data
  },
  updateDataCount (state, { data }) {
    state.dataCount = data
  }
}
export const actions = {
  updateUserInfo (context, { data }) {
    context.commit('updateUserInfo', { data })
  },
  updateUserMember (context, { data }) {
    context.commit('updateUserMember', { data })
  },
  updateDataCount (context, { data }) {
    context.commit('updateDataCount', { data })
  }
}
