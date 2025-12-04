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
var apache_conf$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/apache_conf"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(apache_conf$2);
var apache_confExports = apache_conf$2.exports;
const apache_conf = /* @__PURE__ */ getDefaultExportFromCjs(apache_confExports);
const apache_conf$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: apache_conf
}, [apache_confExports]);
export {
  apache_conf$1 as a
};
