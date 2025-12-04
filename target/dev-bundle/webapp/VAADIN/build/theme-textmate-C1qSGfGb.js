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
var themeTextmate$2 = { exports: {} };
(function(module, exports) {
  ace.define("ace/theme/textmate", ["require", "exports", "module", "ace/theme/textmate.css", "ace/lib/dom"], function(require2, exports2, module2) {
    exports2.isDark = false;
    exports2.cssClass = "ace-tm";
    exports2.cssText = require2("./textmate.css");
    exports2.$id = "ace/theme/textmate";
    var dom = require2("../lib/dom");
    dom.importCssString(exports2.cssText, exports2.cssClass, false);
  });
  (function() {
    ace.require(["ace/theme/textmate"], function(m) {
      {
        module.exports = m;
      }
    });
  })();
})(themeTextmate$2);
var themeTextmateExports = themeTextmate$2.exports;
const themeTextmate = /* @__PURE__ */ getDefaultExportFromCjs(themeTextmateExports);
const themeTextmate$1 = /* @__PURE__ */ _mergeNamespaces({
  __proto__: null,
  default: themeTextmate
}, [themeTextmateExports]);
export {
  themeTextmate$1 as t
};
