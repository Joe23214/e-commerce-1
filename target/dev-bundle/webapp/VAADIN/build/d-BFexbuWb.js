import { l as getDefaultExportFromCjs } from "./indexhtml-DmsHVRX3.js";
function _mergeNamespaces(n, m) {
  for (var i = 0; i < m.length; i++) {
    const e = m[i];
    if (typeof e !== "string" && !Array.isArray(e)) {
      for (const k in e) {
        if (k !== "default" && !(k in n)) {
          const d2 = Object.getOwnPropertyDescriptor(e, k);
          if (d2) {
            Object.defineProperty(n, k, d2.get ? d2 : {
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
var d$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/d"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(d$2);
var dExports = d$2.exports;
const d = /* @__PURE__ */ getDefaultExportFromCjs(dExports);
const d$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: d
}, [dExports]);
export {
  d$1 as d
};
