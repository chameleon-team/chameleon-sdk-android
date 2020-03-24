import {observable, extendObservable} from 'mobx'
import {proxy, getByPath} from './util'
import mapStore from './mapStore'
import { transformGetters, transformMutations, transformActions } from './transform'
import { mergeDeps } from './merge'

function Store(options) {
  options = mergeDeps(options)
  this.getters = {}
  this.mutations = {}
  this.actions = {}
  this.state = this.registerModule('', options).state
  Object.assign(this, mapStore(this))
}

Store.prototype.dispatch = function(type, ...payload) {
  const action = getByPath(this.actions, type)
  if (!action) {
    return Promise.reject(new Error(`unknown action type: ${type}`))
  } else {
    return action(...payload)
  }
}

Store.prototype.commit = function(type, ...payload) {
  const mutation = getByPath(this.mutations, type)
  if (!mutation) {
    console.warn(new Error(`unknown mutation type: ${type}`))
  } else {
    return mutation(...payload)
  }
}

Store.prototype.registerModule = function(path, module) {
  const reactiveModuleOption = {
    state: module.state || {}
  }
  const reactiveModule = observable(reactiveModuleOption)
  if (module.getters) {
    extendObservable(reactiveModule, {
      getters: transformGetters(module.getters, reactiveModule, this)
    })
    // 使用proxy，保证store.getters的属性是可观察的
    proxy(this.getters, reactiveModule.getters, Object.keys(module.getters), true)
  }
  if (module.mutations) {
    Object.assign(this.mutations, transformMutations(module.mutations, reactiveModule, this))
  }
  if (module.actions) {
    Object.assign(this.actions, transformActions(module.actions, reactiveModule, this))
  }
  if (module.modules) {
    const childs = module.modules
    Object.keys(childs).forEach(key => {
      extendObservable(reactiveModule.state, {
        [key]: this.registerModule('', childs[key]).state
      })
    })
  }
  return reactiveModule
}

export default function createStore(...args) {
  return new Store(...args)
}
