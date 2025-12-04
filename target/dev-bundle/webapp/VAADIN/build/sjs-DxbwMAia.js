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
var sjs$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/sjs"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(sjs$2);
var sjsExports = sjs$2.exports;
const sjs = /* @__PURE__ */ getDefaultExportFromCjs(sjsExports);
const sjs$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: sjs
}, [sjsExports]);
export {
  sjs$1 as s
};
