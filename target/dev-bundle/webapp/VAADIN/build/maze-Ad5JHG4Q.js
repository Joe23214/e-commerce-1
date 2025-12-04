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
var maze$2 = { exports: {} };
(function(module, exports) {
  ace.define("ace/snippets/maze.snippets", ["require", "exports", "module"], function(require2, exports2, module2) {
    module2.exports = "snippet >\ndescription assignment\nscope maze\n	-> ${1}= ${2}\n\nsnippet >\ndescription if\nscope maze\n	-> IF ${2:**} THEN %${3:L} ELSE %${4:R}\n";
  });
  ace.define("ace/snippets/maze", ["require", "exports", "module", "ace/snippets/maze.snippets"], function(require2, exports2, module2) {
    exports2.snippetText = require2("./maze.snippets");
    exports2.scope = "maze";
  });
  (function() {
    ace.require(["ace/snippets/maze"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(maze$2);
var mazeExports = maze$2.exports;
const maze = /* @__PURE__ */ getDefaultExportFromCjs(mazeExports);
const maze$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: maze
}, [mazeExports]);
export {
  maze$1 as m
};
