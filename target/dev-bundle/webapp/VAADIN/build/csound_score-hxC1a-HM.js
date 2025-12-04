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
var csound_score$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/csound_score"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(csound_score$2);
var csound_scoreExports = csound_score$2.exports;
const csound_score = /* @__PURE__ */ getDefaultExportFromCjs(csound_scoreExports);
const csound_score$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: csound_score
}, [csound_scoreExports]);
export {
  csound_score$1 as c
};
