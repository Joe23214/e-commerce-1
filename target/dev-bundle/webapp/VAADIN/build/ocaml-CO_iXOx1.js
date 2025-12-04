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
var ocaml$2 = { exports: {} };
(function(module, exports) {
  (function() {
    ace.require(["ace/snippets/ocaml"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(ocaml$2);
var ocamlExports = ocaml$2.exports;
const ocaml = /* @__PURE__ */ getDefaultExportFromCjs(ocamlExports);
const ocaml$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: ocaml
}, [ocamlExports]);
export {
  ocaml$1 as o
};
