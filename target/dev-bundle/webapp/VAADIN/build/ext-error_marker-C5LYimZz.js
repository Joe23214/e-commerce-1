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
var extError_marker$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/ext/error_marker"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(extError_marker$2);
var extError_markerExports = extError_marker$2.exports;
const extError_marker = /* @__PURE__ */ getDefaultExportFromCjs(extError_markerExports);
const extError_marker$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: extError_marker
}, [extError_markerExports]);
export {
  extError_marker$1 as e
};
