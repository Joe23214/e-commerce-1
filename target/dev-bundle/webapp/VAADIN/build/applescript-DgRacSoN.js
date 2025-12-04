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
var applescript$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/applescript"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(applescript$2);
var applescriptExports = applescript$2.exports;
const applescript = /* @__PURE__ */ getDefaultExportFromCjs(applescriptExports);
const applescript$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: applescript
}, [applescriptExports]);
export {
  applescript$1 as a
};
