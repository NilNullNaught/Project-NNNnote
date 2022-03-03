export const state = () => ({
  userInfo: { },
  dataCount: {
    noteCount: 0,
    draftCount: 0,
    deletedCount: 0
  }
})
export const mutations = {
  updateDataCount (state, { data }) {
    state.dataCount = data
  },
  updateUserInfo (state, { data }) {
    state.userInfo = data
  }
}
export const actions = {
  updateDataCount (context, { data }) {
    context.commit('updateDataCount', { data })
  },
  updateUserInfo (context, { data }) {
    context.commit('updateUserInfo', { data })
  }
}
