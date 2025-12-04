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
var makefile$2 = { exports: {} };
(function(module, exports) {
  ace.define("ace/snippets/makefile.snippets", ["require", "exports", "module"], function(require2, exports2, module2) {
    module2.exports = "snippet ifeq\n	ifeq (${1:cond0},${2:cond1})\n		${3:code}\n	endif\n";
  });
  ace.define("ace/snippets/makefile", ["require", "exports", "module", "ace/snippets/makefile.snippets"], function(require2, exports2, module2) {
    exports2.snippetText = require2("./makefile.snippets");
    exports2.scope = "makefile";
  });
  (function() {
    ace.require(["ace/snippets/makefile"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(makefile$2);
var makefileExports = makefile$2.exports;
const makefile = /* @__PURE__ */ getDefaultExportFromCjs(makefileExports);
const makefile$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: makefile
}, [makefileExports]);
export {
  makefile$1 as m
};
