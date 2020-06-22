export function obj2str(obj) {
  return Object.keys(obj)
    .map(key => `${key}:${obj[key]}`)
    .join(";");
}