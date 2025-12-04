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
var matlab$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/matlab"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(matlab$2);
var matlabExports = matlab$2.exports;
const matlab = /* @__PURE__ */ getDefaultExportFromCjs(matlabExports);
const matlab$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: matlab
}, [matlabExports]);
export {
  matlab$1 as m
};
