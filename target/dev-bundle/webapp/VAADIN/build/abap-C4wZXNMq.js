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
var abap$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/abap"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(abap$2);
var abapExports = abap$2.exports;
const abap = /* @__PURE__ */ getDefaultExportFromCjs(abapExports);
const abap$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: abap
}, [abapExports]);
export {
  abap$1 as a
};
