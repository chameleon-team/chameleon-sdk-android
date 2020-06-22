import Vuex from 'vuex'
Vue.use(Vuex)

export default function createStore(...args) {
  const store = new Vuex.Store(...args)

  ;[
    'mapGetters',
    'mapMutations',
    'mapActions',
    'mapState'
  ].forEach((key) => {
    store[key] = Vuex[key]
  })

  return store
}