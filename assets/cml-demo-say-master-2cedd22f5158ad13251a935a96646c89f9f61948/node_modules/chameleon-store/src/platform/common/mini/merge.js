export function mergeDeps(options) {
  const stores = options.deps
  if (!stores) return options
  const mergeProps = ['state', 'getters', 'mutations', 'actions']
  Object.keys(stores).forEach(key => {
    const store = stores[key]
    mergeProps.forEach(prop => {
      if (options[prop] && (key in options[prop])) {
        console.warn(new Error(`deps's name: [${key}] conflicts with ${prop}'s key in current options`))
      } else {
        options[prop] = options[prop] || {}
        options[prop][key] = store[prop]
      }
    })
  })
  delete options.deps
  return options
}