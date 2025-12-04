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
var dot$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/dot"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(dot$2);
var dotExports = dot$2.exports;
const dot = /* @__PURE__ */ getDefaultExportFromCjs(dotExports);
const dot$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: dot
}, [dotExports]);
export {
  dot$1 as d
};
