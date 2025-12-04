import { l as getDefaultExportFromCjs } from "./indexhtml-DmsHVRX3.js";
function _mergeNamespaces(n, m) {
  for (var i = 0; i < m.length; i++) {
    const e = m[i];
    if (typeof e !== "string" && !Array.isArray(e)) {
      for (const k in e) {
        if (k !== "default" && !(k in n)) {
          const d = Object.getOwnPropertyDescriptor(e, k);
          if (d) {
            Object.defineProperty(n, k, d.get ? d : {
              enumerable: true,
              get: () => e[k]
            });
          }
        }
      }
    }
  }
  return Object.freeze(Object.defineProperty(n, Symbol.toStringTag, { value: "Module" }));
}
var c9search$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/c9search"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(c9search$2);
var c9searchExports = c9search$2.exports;
const c9search = /* @__PURE__ */ getDefaultExportFromCjs(c9searchExports);
const c9search$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: c9search
}, [c9searchExports]);
export {
  c9search$1 as c
};
