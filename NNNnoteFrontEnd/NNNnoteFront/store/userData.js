export const state = () => ({
  list: [],
  dataCount: {
    noteCount: 0,
    draftCount: 0,
    deletedCount: 0
  }
})
export const mutations = {
  updateDataCount (state, { data }) {
    state.dataCount = data
  }
}
export const actions = {
  updateDataCount (context, { data }) {
    context.commit('updateDataCount', { data })
  }
}
