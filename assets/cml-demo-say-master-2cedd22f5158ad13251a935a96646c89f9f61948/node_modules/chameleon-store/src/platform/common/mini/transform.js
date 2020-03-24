import {computed, action} from 'mobx'

export function transformGetters(getters, module, store) {
  const newGetters = {}
  for (let key in getters) {
    if (key in store.getters) {
      console.warn(new Error(`duplicate getter type: ${key}`))
    }
    newGetters[key] = typeof getters[key] === 'function' ? computed(function() {
      return getters[key](module.state, store.getters, store.state)
    }) : getters[key]
  }
  return newGetters
}

export function transformMutations(mutations, module, store) {
  const newMutations = {}
  for (let key in mutations) {
    if (store.mutations[key]) {
      console.warn(new Error(`duplicate mutation type: ${key}`))
    }
    newMutations[key] = typeof mutations[key] === 'function' ? action(function(...payload) {
      return mutations[key](module.state, ...payload)
    }) : mutations[key]
  }
  return newMutations
}

export function transformActions(actions, module, store) {
  const newActions = {}
  for (let key in actions) {
    if (store.actions[key]) {
      console.warn(new Error(`duplicate action type: ${key}`))
    }
    newActions[key] = typeof actions[key] === 'function' ? function(...payload) {
      return Promise.resolve().then(() => {
        return actions[key]({
          rootState: store.state,
          state: module.state,
          getters: store.getters,
          dispatch: store.dispatch.bind(store),
          commit: store.commit.bind(store)
        }, ...payload)
      })
    } : actions[key]
  }
  return newActions
}