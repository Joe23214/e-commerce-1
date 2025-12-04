// vite.generated.ts
import path from "path";
import { existsSync as existsSync5, mkdirSync as mkdirSync2, readdirSync as readdirSync2, readFileSync as readFileSync4, writeFileSync as writeFileSync2 } from "fs";
import { createHash } from "crypto";
import * as net from "net";

// build/plugins/application-theme-plugin/theme-handle.js
import { existsSync as existsSync3, readFileSync as readFileSync2 } from "fs";
import { resolve as resolve3 } from "path";

// build/plugins/application-theme-plugin/theme-generator.js
import { globSync as globSync2 } from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/glob@10.4.5/node_modules/glob/dist/esm/index.js";
import { resolve as resolve2, basename as basename2 } from "path";
import { existsSync as existsSync2, readFileSync, writeFileSync } from "fs";

// build/plugins/application-theme-plugin/theme-copy.js
import { readdirSync, statSync, mkdirSync, existsSync, copyFileSync } from "fs";
import { resolve, basename, relative, extname } from "path";
import { globSync } from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/glob@10.4.5/node_modules/glob/dist/esm/index.js";
var ignoredFileExtensions = [".css", ".js", ".json"];
function copyThemeResources(themeFolder2, projectStaticAssetsOutputFolder, logger) {
  const staticAssetsThemeFolder = resolve(projectStaticAssetsOutputFolder, "themes", basename(themeFolder2));
  const collection = collectFolders(themeFolder2, logger);
  if (collection.files.length > 0) {
    mkdirSync(staticAssetsThemeFolder, { recursive: true });
    collection.directories.forEach((directory) => {
      const relativeDirectory = relative(themeFolder2, directory);
      const targetDirectory = resolve(staticAssetsThemeFolder, relativeDirectory);
      mkdirSync(targetDirectory, { recursive: true });
    });
    collection.files.forEach((file) => {
      const relativeFile = relative(themeFolder2, file);
      const targetFile = resolve(staticAssetsThemeFolder, relativeFile);
      copyFileIfAbsentOrNewer(file, targetFile, logger);
    });
  }
}
function collectFolders(folderToCopy, logger) {
  const collection = { directories: [], files: [] };
  logger.trace("files in directory", readdirSync(folderToCopy));
  readdirSync(folderToCopy).forEach((file) => {
    const fileToCopy = resolve(folderToCopy, file);
    try {
      if (statSync(fileToCopy).isDirectory()) {
        logger.debug("Going through directory", fileToCopy);
        const result = collectFolders(fileToCopy, logger);
        if (result.files.length > 0) {
          collection.directories.push(fileToCopy);
          logger.debug("Adding directory", fileToCopy);
          collection.directories.push.apply(collection.directories, result.directories);
          collection.files.push.apply(collection.files, result.files);
        }
      } else if (!ignoredFileExtensions.includes(extname(fileToCopy))) {
        logger.debug("Adding file", fileToCopy);
        collection.files.push(fileToCopy);
      }
    } catch (error) {
      handleNoSuchFileError(fileToCopy, error, logger);
    }
  });
  return collection;
}
function copyStaticAssets(themeName, themeProperties, projectStaticAssetsOutputFolder, logger) {
  const assets = themeProperties["assets"];
  if (!assets) {
    logger.debug("no assets to handle no static assets were copied");
    return;
  }
  mkdirSync(projectStaticAssetsOutputFolder, {
    recursive: true
  });
  const missingModules = checkModules(Object.keys(assets));
  if (missingModules.length > 0) {
    throw Error(
      "Missing npm modules '" + missingModules.join("', '") + "' for assets marked in 'theme.json'.\nInstall package(s) by adding a @NpmPackage annotation or install it using 'npm/pnpm/bun i'"
    );
  }
  Object.keys(assets).forEach((module) => {
    const copyRules = assets[module];
    Object.keys(copyRules).forEach((copyRule) => {
      const nodeSources = resolve("node_modules/", module, copyRule);
      const files = globSync(nodeSources, { nodir: true });
      const targetFolder = resolve(projectStaticAssetsOutputFolder, "themes", themeName, copyRules[copyRule]);
      mkdirSync(targetFolder, {
        recursive: true
      });
      files.forEach((file) => {
        const copyTarget = resolve(targetFolder, basename(file));
        copyFileIfAbsentOrNewer(file, copyTarget, logger);
      });
    });
  });
}
function checkModules(modules) {
  const missing = [];
  modules.forEach((module) => {
    if (!existsSync(resolve("node_modules/", module))) {
      missing.push(module);
    }
  });
  return missing;
}
function copyFileIfAbsentOrNewer(fileToCopy, copyTarget, logger) {
  try {
    if (!existsSync(copyTarget) || statSync(copyTarget).mtime < statSync(fileToCopy).mtime) {
      logger.trace("Copying: ", fileToCopy, "=>", copyTarget);
      copyFileSync(fileToCopy, copyTarget);
    }
  } catch (error) {
    handleNoSuchFileError(fileToCopy, error, logger);
  }
}
function handleNoSuchFileError(file, error, logger) {
  if (error.code === "ENOENT") {
    logger.warn("Ignoring not existing file " + file + ". File may have been deleted during theme processing.");
  } else {
    throw error;
  }
}

// build/plugins/application-theme-plugin/theme-generator.js
var themeComponentsFolder = "components";
var documentCssFilename = "document.css";
var stylesCssFilename = "styles.css";
var CSSIMPORT_COMMENT = "CSSImport end";
var headerImport = `import 'construct-style-sheets-polyfill';
`;
function writeThemeFiles(themeFolder2, themeName, themeProperties, options) {
  const productionMode = !options.devMode;
  const useDevServerOrInProductionMode = !options.useDevBundle;
  const outputFolder = options.frontendGeneratedFolder;
  const styles = resolve2(themeFolder2, stylesCssFilename);
  const documentCssFile = resolve2(themeFolder2, documentCssFilename);
  const autoInjectComponents = themeProperties.autoInjectComponents ?? true;
  const autoInjectGlobalCssImports = themeProperties.autoInjectGlobalCssImports ?? false;
  const globalFilename = "theme-" + themeName + ".global.generated.js";
  const componentsFilename = "theme-" + themeName + ".components.generated.js";
  const themeFilename = "theme-" + themeName + ".generated.js";
  let themeFileContent = headerImport;
  let globalImportContent = "// When this file is imported, global styles are automatically applied\n";
  let componentsFileContent = "";
  var componentsFiles;
  if (autoInjectComponents) {
    componentsFiles = globSync2("*.css", {
      cwd: resolve2(themeFolder2, themeComponentsFolder),
      nodir: true
    });
    if (componentsFiles.length > 0) {
      componentsFileContent += "import { unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin/register-styles';\n";
    }
  }
  if (themeProperties.parent) {
    themeFileContent += `import { applyTheme as applyBaseTheme } from './theme-${themeProperties.parent}.generated.js';
`;
  }
  themeFileContent += `import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';
`;
  themeFileContent += `import { webcomponentGlobalCssInjector } from 'Frontend/generated/jar-resources/theme-util.js';
`;
  themeFileContent += `import './${componentsFilename}';
`;
  themeFileContent += `let needsReloadOnChanges = false;
`;
  const imports = [];
  const componentCssImports = [];
  const globalFileContent = [];
  const globalCssCode = [];
  const shadowOnlyCss = [];
  const componentCssCode = [];
  const parentTheme = themeProperties.parent ? "applyBaseTheme(target);\n" : "";
  const parentThemeGlobalImport = themeProperties.parent ? `import './theme-${themeProperties.parent}.global.generated.js';
` : "";
  const themeIdentifier = "_vaadintheme_" + themeName + "_";
  const lumoCssFlag = "_vaadinthemelumoimports_";
  const globalCssFlag = themeIdentifier + "globalCss";
  const componentCssFlag = themeIdentifier + "componentCss";
  if (!existsSync2(styles)) {
    if (productionMode) {
      throw new Error(`styles.css file is missing and is needed for '${themeName}' in folder '${themeFolder2}'`);
    }
    writeFileSync(
      styles,
      "/* Import your application global css files here or add the styles directly to this file */",
      "utf8"
    );
  }
  let filename = basename2(styles);
  let variable = camelCase(filename);
  const lumoImports = themeProperties.lumoImports || ["color", "typography"];
  if (lumoImports) {
    lumoImports.forEach((lumoImport) => {
      imports.push(`import { ${lumoImport} } from '@vaadin/vaadin-lumo-styles/${lumoImport}.js';
`);
      if (lumoImport === "utility" || lumoImport === "badge" || lumoImport === "typography" || lumoImport === "color") {
        globalFileContent.push(`import '@vaadin/vaadin-lumo-styles/${lumoImport}-global.js';
`);
      }
    });
    lumoImports.forEach((lumoImport) => {
      shadowOnlyCss.push(`removers.push(injectGlobalCss(${lumoImport}.cssText, '', target, true));
`);
    });
  }
  globalFileContent.push(parentThemeGlobalImport);
  if (useDevServerOrInProductionMode) {
    globalFileContent.push(`import 'themes/${themeName}/${filename}';
`);
    imports.push(`import ${variable} from 'themes/${themeName}/${filename}?inline';
`);
    shadowOnlyCss.push(`removers.push(injectGlobalCss(${variable}.toString(), '', target));
    `);
  }
  if (existsSync2(documentCssFile)) {
    filename = basename2(documentCssFile);
    variable = camelCase(filename);
    if (useDevServerOrInProductionMode) {
      globalFileContent.push(`import 'themes/${themeName}/${filename}';
`);
      imports.push(`import ${variable} from 'themes/${themeName}/${filename}?inline';
`);
      shadowOnlyCss.push(`removers.push(injectGlobalCss(${variable}.toString(),'', document));
    `);
    }
  }
  let i = 0;
  if (themeProperties.documentCss) {
    const missingModules = checkModules(themeProperties.documentCss);
    if (missingModules.length > 0) {
      throw Error(
        "Missing npm modules or files '" + missingModules.join("', '") + "' for documentCss marked in 'theme.json'.\nInstall or update package(s) by adding a @NpmPackage annotation or install it using 'npm/pnpm/bun i'"
      );
    }
    themeProperties.documentCss.forEach((cssImport) => {
      const variable2 = "module" + i++;
      imports.push(`import ${variable2} from '${cssImport}?inline';
`);
      globalCssCode.push(`if(target !== document) {
        removers.push(injectGlobalCss(${variable2}.toString(), '', target));
    }
    `);
      globalCssCode.push(
        `removers.push(injectGlobalCss(${variable2}.toString(), '${CSSIMPORT_COMMENT}', document));
    `
      );
    });
  }
  if (themeProperties.importCss) {
    const missingModules = checkModules(themeProperties.importCss);
    if (missingModules.length > 0) {
      throw Error(
        "Missing npm modules or files '" + missingModules.join("', '") + "' for importCss marked in 'theme.json'.\nInstall or update package(s) by adding a @NpmPackage annotation or install it using 'npm/pnpm/bun i'"
      );
    }
    themeProperties.importCss.forEach((cssPath) => {
      const variable2 = "module" + i++;
      globalFileContent.push(`import '${cssPath}';
`);
      imports.push(`import ${variable2} from '${cssPath}?inline';
`);
      shadowOnlyCss.push(`removers.push(injectGlobalCss(${variable2}.toString(), '${CSSIMPORT_COMMENT}', target));
`);
    });
  }
  if (autoInjectComponents) {
    componentsFiles.forEach((componentCss) => {
      const filename2 = basename2(componentCss);
      const tag = filename2.replace(".css", "");
      const variable2 = camelCase(filename2);
      componentCssImports.push(
        `import ${variable2} from 'themes/${themeName}/${themeComponentsFolder}/${filename2}?inline';
`
      );
      const componentString = `registerStyles(
        '${tag}',
        unsafeCSS(${variable2}.toString())
      );
      `;
      componentCssCode.push(componentString);
    });
  }
  themeFileContent += imports.join("");
  const themeFileApply = `
  let themeRemovers = new WeakMap();
  let targets = [];

  export const applyTheme = (target) => {
    const removers = [];
    if (target !== document) {
      ${shadowOnlyCss.join("")}
      ${autoInjectGlobalCssImports ? `
        webcomponentGlobalCssInjector((css) => {
          removers.push(injectGlobalCss(css, '', target));
        });
        ` : ""}
    }
    ${parentTheme}
    ${globalCssCode.join("")}

    if (import.meta.hot) {
      targets.push(new WeakRef(target));
      themeRemovers.set(target, removers);
    }

  }

`;
  componentsFileContent += `
${componentCssImports.join("")}

if (!document['${componentCssFlag}']) {
  ${componentCssCode.join("")}
  document['${componentCssFlag}'] = true;
}

if (import.meta.hot) {
  import.meta.hot.accept((module) => {
    window.location.reload();
  });
}

`;
  themeFileContent += themeFileApply;
  themeFileContent += `
if (import.meta.hot) {
  import.meta.hot.accept((module) => {

    if (needsReloadOnChanges) {
      window.location.reload();
    } else {
      targets.forEach(targetRef => {
        const target = targetRef.deref();
        if (target) {
          themeRemovers.get(target).forEach(remover => remover())
          module.applyTheme(target);
        }
      })
    }
  });

  import.meta.hot.on('vite:afterUpdate', (update) => {
    document.dispatchEvent(new CustomEvent('vaadin-theme-updated', { detail: update }));
  });
}

`;
  globalImportContent += `
${globalFileContent.join("")}
`;
  writeIfChanged(resolve2(outputFolder, globalFilename), globalImportContent);
  writeIfChanged(resolve2(outputFolder, themeFilename), themeFileContent);
  writeIfChanged(resolve2(outputFolder, componentsFilename), componentsFileContent);
}
function writeIfChanged(file, data) {
  if (!existsSync2(file) || readFileSync(file, { encoding: "utf-8" }) !== data) {
    writeFileSync(file, data);
  }
}
function camelCase(str) {
  return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index) {
    return index === 0 ? word.toLowerCase() : word.toUpperCase();
  }).replace(/\s+/g, "").replace(/\.|\-/g, "");
}

// build/plugins/application-theme-plugin/theme-handle.js
var nameRegex = /theme-(.*)\.generated\.js/;
var prevThemeName = void 0;
var firstThemeName = void 0;
function processThemeResources(options, logger) {
  const themeName = extractThemeName(options.frontendGeneratedFolder);
  if (themeName) {
    if (!prevThemeName && !firstThemeName) {
      firstThemeName = themeName;
    } else if (prevThemeName && prevThemeName !== themeName && firstThemeName !== themeName || !prevThemeName && firstThemeName !== themeName) {
      const warning = `Attention: Active theme is switched to '${themeName}'.`;
      const description = `
      Note that adding new style sheet files to '/themes/${themeName}/components', 
      may not be taken into effect until the next application restart.
      Changes to already existing style sheet files are being reloaded as before.`;
      logger.warn("*******************************************************************");
      logger.warn(warning);
      logger.warn(description);
      logger.warn("*******************************************************************");
    }
    prevThemeName = themeName;
    findThemeFolderAndHandleTheme(themeName, options, logger);
  } else {
    prevThemeName = void 0;
    logger.debug("Skipping Vaadin application theme handling.");
    logger.trace("Most likely no @Theme annotation for application or only themeClass used.");
  }
}
function findThemeFolderAndHandleTheme(themeName, options, logger) {
  let themeFound = false;
  for (let i = 0; i < options.themeProjectFolders.length; i++) {
    const themeProjectFolder = options.themeProjectFolders[i];
    if (existsSync3(themeProjectFolder)) {
      logger.debug("Searching themes folder '" + themeProjectFolder + "' for theme '" + themeName + "'");
      const handled = handleThemes(themeName, themeProjectFolder, options, logger);
      if (handled) {
        if (themeFound) {
          throw new Error(
            "Found theme files in '" + themeProjectFolder + "' and '" + themeFound + "'. Theme should only be available in one folder"
          );
        }
        logger.debug("Found theme files from '" + themeProjectFolder + "'");
        themeFound = themeProjectFolder;
      }
    }
  }
  if (existsSync3(options.themeResourceFolder)) {
    if (themeFound && existsSync3(resolve3(options.themeResourceFolder, themeName))) {
      throw new Error(
        "Theme '" + themeName + `'should not exist inside a jar and in the project at the same time
Extending another theme is possible by adding { "parent": "my-parent-theme" } entry to the theme.json file inside your theme folder.`
      );
    }
    logger.debug(
      "Searching theme jar resource folder '" + options.themeResourceFolder + "' for theme '" + themeName + "'"
    );
    handleThemes(themeName, options.themeResourceFolder, options, logger);
    themeFound = true;
  }
  return themeFound;
}
function handleThemes(themeName, themesFolder, options, logger) {
  const themeFolder2 = resolve3(themesFolder, themeName);
  if (existsSync3(themeFolder2)) {
    logger.debug("Found theme ", themeName, " in folder ", themeFolder2);
    const themeProperties = getThemeProperties(themeFolder2);
    if (themeProperties.parent) {
      const found = findThemeFolderAndHandleTheme(themeProperties.parent, options, logger);
      if (!found) {
        throw new Error(
          "Could not locate files for defined parent theme '" + themeProperties.parent + "'.\nPlease verify that dependency is added or theme folder exists."
        );
      }
    }
    copyStaticAssets(themeName, themeProperties, options.projectStaticAssetsOutputFolder, logger);
    copyThemeResources(themeFolder2, options.projectStaticAssetsOutputFolder, logger);
    writeThemeFiles(themeFolder2, themeName, themeProperties, options);
    return true;
  }
  return false;
}
function getThemeProperties(themeFolder2) {
  const themePropertyFile = resolve3(themeFolder2, "theme.json");
  if (!existsSync3(themePropertyFile)) {
    return {};
  }
  const themePropertyFileAsString = readFileSync2(themePropertyFile);
  if (themePropertyFileAsString.length === 0) {
    return {};
  }
  return JSON.parse(themePropertyFileAsString);
}
function extractThemeName(frontendGeneratedFolder) {
  if (!frontendGeneratedFolder) {
    throw new Error(
      "Couldn't extract theme name from 'theme.js', because the path to folder containing this file is empty. Please set the a correct folder path in ApplicationThemePlugin constructor parameters."
    );
  }
  const generatedThemeFile = resolve3(frontendGeneratedFolder, "theme.js");
  if (existsSync3(generatedThemeFile)) {
    const themeName = nameRegex.exec(readFileSync2(generatedThemeFile, { encoding: "utf8" }))[1];
    if (!themeName) {
      throw new Error("Couldn't parse theme name from '" + generatedThemeFile + "'.");
    }
    return themeName;
  } else {
    return "";
  }
}

// build/plugins/theme-loader/theme-loader-utils.js
import { existsSync as existsSync4, readFileSync as readFileSync3 } from "fs";
import { resolve as resolve4, basename as basename3 } from "path";
import { globSync as globSync3 } from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/glob@10.4.5/node_modules/glob/dist/esm/index.js";
var urlMatcher = /(url\(\s*)(\'|\")?(\.\/|\.\.\/)((?:\3)*)?(\S*)(\2\s*\))/g;
function assetsContains(fileUrl, themeFolder2, logger) {
  const themeProperties = getThemeProperties2(themeFolder2);
  if (!themeProperties) {
    logger.debug("No theme properties found.");
    return false;
  }
  const assets = themeProperties["assets"];
  if (!assets) {
    logger.debug("No defined assets in theme properties");
    return false;
  }
  for (let module of Object.keys(assets)) {
    const copyRules = assets[module];
    for (let copyRule of Object.keys(copyRules)) {
      if (fileUrl.startsWith(copyRules[copyRule])) {
        const targetFile = fileUrl.replace(copyRules[copyRule], "");
        const files = globSync3(resolve4("node_modules/", module, copyRule), { nodir: true });
        for (let file of files) {
          if (file.endsWith(targetFile)) return true;
        }
      }
    }
  }
  return false;
}
function getThemeProperties2(themeFolder2) {
  const themePropertyFile = resolve4(themeFolder2, "theme.json");
  if (!existsSync4(themePropertyFile)) {
    return {};
  }
  const themePropertyFileAsString = readFileSync3(themePropertyFile);
  if (themePropertyFileAsString.length === 0) {
    return {};
  }
  return JSON.parse(themePropertyFileAsString);
}
function rewriteCssUrls(source, handledResourceFolder, themeFolder2, logger, options) {
  source = source.replace(urlMatcher, function(match, url, quoteMark, replace2, additionalDotSegments, fileUrl, endString) {
    let absolutePath = resolve4(handledResourceFolder, replace2, additionalDotSegments || "", fileUrl);
    let existingThemeResource = absolutePath.startsWith(themeFolder2) && existsSync4(absolutePath);
    if (!existingThemeResource && additionalDotSegments) {
      absolutePath = resolve4(handledResourceFolder, replace2, fileUrl);
      existingThemeResource = absolutePath.startsWith(themeFolder2) && existsSync4(absolutePath);
    }
    const isAsset = assetsContains(fileUrl, themeFolder2, logger);
    if (existingThemeResource || isAsset) {
      const replacement = options.devMode ? "./" : "../static/";
      const skipLoader = existingThemeResource ? "" : replacement;
      const frontendThemeFolder = skipLoader + "themes/" + basename3(themeFolder2);
      logger.log(
        "Updating url for file",
        "'" + replace2 + fileUrl + "'",
        "to use",
        "'" + frontendThemeFolder + "/" + fileUrl + "'"
      );
      const pathResolved = isAsset ? "/" + fileUrl : absolutePath.substring(themeFolder2.length).replace(/\\/g, "/");
      return url + (quoteMark ?? "") + frontendThemeFolder + pathResolved + endString;
    } else if (options.devMode) {
      logger.log("No rewrite for '", match, "' as the file was not found.");
    } else {
      return url + (quoteMark ?? "") + "../../" + fileUrl + endString;
    }
    return match;
  });
  return source;
}

// build/plugins/react-function-location-plugin/react-function-location-plugin.js
import * as t from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/@babel+types@7.26.5/node_modules/@babel/types/lib/index.js";
function addFunctionComponentSourceLocationBabel() {
  function isReactFunctionName(name) {
    return name && name.match(/^[A-Z].*/);
  }
  function addDebugInfo(path2, name, filename, loc) {
    const lineNumber = loc.start.line;
    const columnNumber = loc.start.column + 1;
    const debugSourceMember = t.memberExpression(t.identifier(name), t.identifier("__debugSourceDefine"));
    const debugSourceDefine = t.objectExpression([
      t.objectProperty(t.identifier("fileName"), t.stringLiteral(filename)),
      t.objectProperty(t.identifier("lineNumber"), t.numericLiteral(lineNumber)),
      t.objectProperty(t.identifier("columnNumber"), t.numericLiteral(columnNumber))
    ]);
    const assignment = t.expressionStatement(t.assignmentExpression("=", debugSourceMember, debugSourceDefine));
    const condition = t.binaryExpression(
      "===",
      t.unaryExpression("typeof", t.identifier(name)),
      t.stringLiteral("function")
    );
    const ifFunction = t.ifStatement(condition, t.blockStatement([assignment]));
    path2.insertAfter(ifFunction);
  }
  return {
    visitor: {
      VariableDeclaration(path2, state) {
        path2.node.declarations.forEach((declaration) => {
          if (declaration.id.type !== "Identifier") {
            return;
          }
          const name = declaration?.id?.name;
          if (!isReactFunctionName(name)) {
            return;
          }
          const filename = state.file.opts.filename;
          if (declaration?.init?.body?.loc) {
            addDebugInfo(path2, name, filename, declaration.init.body.loc);
          }
        });
      },
      FunctionDeclaration(path2, state) {
        const node = path2.node;
        const name = node?.id?.name;
        if (!isReactFunctionName(name)) {
          return;
        }
        const filename = state.file.opts.filename;
        addDebugInfo(path2, name, filename, node.body.loc);
      }
    }
  };
}

// build/vaadin-dev-server-settings.json
var vaadin_dev_server_settings_default = {
  frontendFolder: "C:/Users/utente/IdeaProjects/shopEcomstore/./src/main/frontend",
  themeFolder: "themes",
  themeResourceFolder: "C:/Users/utente/IdeaProjects/shopEcomstore/./src/main/frontend/generated/jar-resources",
  staticOutput: "C:/Users/utente/IdeaProjects/shopEcomstore/build/classes/META-INF/VAADIN/webapp/VAADIN/static",
  generatedFolder: "generated",
  statsOutput: "C:\\Users\\utente\\IdeaProjects\\shopEcomstore\\build\\classes\\META-INF\\VAADIN\\config",
  frontendBundleOutput: "C:\\Users\\utente\\IdeaProjects\\shopEcomstore\\build\\classes\\META-INF\\VAADIN\\webapp",
  devBundleOutput: "C:/Users/utente/IdeaProjects/shopEcomstore/build/dev-bundle/webapp",
  devBundleStatsOutput: "C:/Users/utente/IdeaProjects/shopEcomstore/build/dev-bundle/config",
  jarResourcesFolder: "C:/Users/utente/IdeaProjects/shopEcomstore/./src/main/frontend/generated/jar-resources",
  themeName: "ecomShop",
  clientServiceWorkerSource: "C:\\Users\\utente\\IdeaProjects\\shopEcomstore\\build\\sw.ts",
  pwaEnabled: true,
  offlineEnabled: true,
  offlinePath: "'.'"
};

// vite.generated.ts
import {
  defineConfig,
  mergeConfig
} from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/vite@5.4.8_terser@5.37.0/node_modules/vite/dist/node/index.js";
import { getManifest } from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/workbox-build@7.1.1_@types+babel__core@7.20.5/node_modules/workbox-build/build/index.js";
import * as rollup from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/rollup@2.79.2/node_modules/rollup/dist/es/rollup.js";
import brotli from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/rollup-plugin-brotli@3.1.0/node_modules/rollup-plugin-brotli/lib/index.cjs.js";
import replace from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/@rollup+plugin-replace@5.0.7_rollup@2.79.2/node_modules/@rollup/plugin-replace/dist/es/index.js";
import checker from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/vite-plugin-checker@0.6.4_t_bdf34be798301d78f3dddceb30778e03/node_modules/vite-plugin-checker/dist/esm/main.js";

// build/plugins/rollup-plugin-postcss-lit-custom/rollup-plugin-postcss-lit.js
import { createFilter } from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/@rollup+pluginutils@5.1.2_rollup@2.79.2/node_modules/@rollup/pluginutils/dist/es/index.js";
import transformAst from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/transform-ast@2.4.4/node_modules/transform-ast/index.js";
var assetUrlRE = /__VITE_ASSET__([\w$]+)__(?:\$_(.*?)__)?/g;
var escape = (str) => str.replace(assetUrlRE, '${unsafeCSSTag("__VITE_ASSET__$1__$2")}').replace(/`/g, "\\`").replace(/\\(?!`)/g, "\\\\");
function postcssLit(options = {}) {
  const defaultOptions = {
    include: "**/*.{css,sss,pcss,styl,stylus,sass,scss,less}",
    exclude: null,
    importPackage: "lit"
  };
  const opts = { ...defaultOptions, ...options };
  const filter = createFilter(opts.include, opts.exclude);
  return {
    name: "postcss-lit",
    enforce: "post",
    transform(code, id) {
      if (!filter(id)) return;
      const ast = this.parse(code, {});
      let defaultExportName;
      let isDeclarationLiteral = false;
      const magicString = transformAst(code, { ast }, (node) => {
        if (node.type === "ExportDefaultDeclaration") {
          defaultExportName = node.declaration.name;
          isDeclarationLiteral = node.declaration.type === "Literal";
        }
      });
      if (!defaultExportName && !isDeclarationLiteral) {
        return;
      }
      magicString.walk((node) => {
        if (defaultExportName && node.type === "VariableDeclaration") {
          const exportedVar = node.declarations.find((d) => d.id.name === defaultExportName);
          if (exportedVar) {
            exportedVar.init.edit.update(`cssTag\`${escape(exportedVar.init.value)}\``);
          }
        }
        if (isDeclarationLiteral && node.type === "ExportDefaultDeclaration") {
          node.declaration.edit.update(`cssTag\`${escape(node.declaration.value)}\``);
        }
      });
      magicString.prepend(`import {css as cssTag, unsafeCSS as unsafeCSSTag} from '${opts.importPackage}';
`);
      return {
        code: magicString.toString(),
        map: magicString.generateMap({
          hires: true
        })
      };
    }
  };
}

// vite.generated.ts
import { createRequire } from "module";
import { visualizer } from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/rollup-plugin-visualizer@5.12.0_rollup@2.79.2/node_modules/rollup-plugin-visualizer/dist/plugin/index.js";
import reactPlugin from "file:///C:/Users/utente/IdeaProjects/shopEcomstore/node_modules/.pnpm/@vitejs+plugin-react@4.3.2_vite@5.4.8_terser@5.37.0_/node_modules/@vitejs/plugin-react/dist/index.mjs";
var __vite_injected_original_dirname = "C:\\Users\\utente\\IdeaProjects\\shopEcomstore";
var __vite_injected_original_import_meta_url = "file:///C:/Users/utente/IdeaProjects/shopEcomstore/vite.generated.ts";
var require2 = createRequire(__vite_injected_original_import_meta_url);
var appShellUrl = ".";
var frontendFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.frontendFolder);
var themeFolder = path.resolve(frontendFolder, vaadin_dev_server_settings_default.themeFolder);
var frontendBundleFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.frontendBundleOutput);
var devBundleFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.devBundleOutput);
var devBundle = !!process.env.devBundle;
var jarResourcesFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.jarResourcesFolder);
var themeResourceFolder = path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.themeResourceFolder);
var projectPackageJsonFile = path.resolve(__vite_injected_original_dirname, "package.json");
var buildOutputFolder = devBundle ? devBundleFolder : frontendBundleFolder;
var statsFolder = path.resolve(__vite_injected_original_dirname, devBundle ? vaadin_dev_server_settings_default.devBundleStatsOutput : vaadin_dev_server_settings_default.statsOutput);
var statsFile = path.resolve(statsFolder, "stats.json");
var bundleSizeFile = path.resolve(statsFolder, "bundle-size.html");
var nodeModulesFolder = path.resolve(__vite_injected_original_dirname, "node_modules");
var webComponentTags = "";
var projectIndexHtml = path.resolve(frontendFolder, "index.html");
var projectStaticAssetsFolders = [
  path.resolve(__vite_injected_original_dirname, "src", "main", "resources", "META-INF", "resources"),
  path.resolve(__vite_injected_original_dirname, "src", "main", "resources", "static"),
  frontendFolder
];
var themeProjectFolders = projectStaticAssetsFolders.map((folder) => path.resolve(folder, vaadin_dev_server_settings_default.themeFolder));
var themeOptions = {
  devMode: false,
  useDevBundle: devBundle,
  // The following matches folder 'frontend/generated/themes/'
  // (not 'frontend/themes') for theme in JAR that is copied there
  themeResourceFolder: path.resolve(themeResourceFolder, vaadin_dev_server_settings_default.themeFolder),
  themeProjectFolders,
  projectStaticAssetsOutputFolder: devBundle ? path.resolve(devBundleFolder, "../assets") : path.resolve(__vite_injected_original_dirname, vaadin_dev_server_settings_default.staticOutput),
  frontendGeneratedFolder: path.resolve(frontendFolder, vaadin_dev_server_settings_default.generatedFolder)
};
var hasExportedWebComponents = existsSync5(path.resolve(frontendFolder, "web-component.html"));
console.trace = () => {
};
console.debug = () => {
};
function injectManifestToSWPlugin() {
  const rewriteManifestIndexHtmlUrl = (manifest) => {
    const indexEntry = manifest.find((entry) => entry.url === "index.html");
    if (indexEntry) {
      indexEntry.url = appShellUrl;
    }
    return { manifest, warnings: [] };
  };
  return {
    name: "vaadin:inject-manifest-to-sw",
    async transform(code, id) {
      if (/sw\.(ts|js)$/.test(id)) {
        const { manifestEntries } = await getManifest({
          globDirectory: buildOutputFolder,
          globPatterns: ["**/*"],
          globIgnores: ["**/*.br"],
          manifestTransforms: [rewriteManifestIndexHtmlUrl],
          maximumFileSizeToCacheInBytes: 100 * 1024 * 1024
          // 100mb,
        });
        return code.replace("self.__WB_MANIFEST", JSON.stringify(manifestEntries));
      }
    }
  };
}
function buildSWPlugin(opts) {
  let config;
  const devMode = opts.devMode;
  const swObj = {};
  async function build(action, additionalPlugins = []) {
    const includedPluginNames = [
      "vite:esbuild",
      "rollup-plugin-dynamic-import-variables",
      "vite:esbuild-transpile",
      "vite:terser"
    ];
    const plugins = config.plugins.filter((p) => {
      return includedPluginNames.includes(p.name);
    });
    const resolver = config.createResolver();
    const resolvePlugin = {
      name: "resolver",
      resolveId(source, importer, _options) {
        return resolver(source, importer);
      }
    };
    plugins.unshift(resolvePlugin);
    plugins.push(
      replace({
        values: {
          "process.env.NODE_ENV": JSON.stringify(config.mode),
          ...config.define
        },
        preventAssignment: true
      })
    );
    if (additionalPlugins) {
      plugins.push(...additionalPlugins);
    }
    const bundle = await rollup.rollup({
      input: path.resolve(vaadin_dev_server_settings_default.clientServiceWorkerSource),
      plugins
    });
    try {
      return await bundle[action]({
        file: path.resolve(buildOutputFolder, "sw.js"),
        format: "es",
        exports: "none",
        sourcemap: config.command === "serve" || config.build.sourcemap,
        inlineDynamicImports: true
      });
    } finally {
      await bundle.close();
    }
  }
  return {
    name: "vaadin:build-sw",
    enforce: "post",
    async configResolved(resolvedConfig) {
      config = resolvedConfig;
    },
    async buildStart() {
      if (devMode) {
        const { output } = await build("generate");
        swObj.code = output[0].code;
        swObj.map = output[0].map;
      }
    },
    async load(id) {
      if (id.endsWith("sw.js")) {
        return "";
      }
    },
    async transform(_code, id) {
      if (id.endsWith("sw.js")) {
        return swObj;
      }
    },
    async closeBundle() {
      if (!devMode) {
        await build("write", [injectManifestToSWPlugin(), brotli()]);
      }
    }
  };
}
function statsExtracterPlugin() {
  function collectThemeJsonsInFrontend(themeJsonContents, themeName) {
    const themeJson = path.resolve(frontendFolder, vaadin_dev_server_settings_default.themeFolder, themeName, "theme.json");
    if (existsSync5(themeJson)) {
      const themeJsonContent = readFileSync4(themeJson, { encoding: "utf-8" }).replace(/\r\n/g, "\n");
      themeJsonContents[themeName] = themeJsonContent;
      const themeJsonObject = JSON.parse(themeJsonContent);
      if (themeJsonObject.parent) {
        collectThemeJsonsInFrontend(themeJsonContents, themeJsonObject.parent);
      }
    }
  }
  return {
    name: "vaadin:stats",
    enforce: "post",
    async writeBundle(options, bundle) {
      const modules = Object.values(bundle).flatMap((b) => b.modules ? Object.keys(b.modules) : []);
      const nodeModulesFolders = modules.map((id) => id.replace(/\\/g, "/")).filter((id) => id.startsWith(nodeModulesFolder.replace(/\\/g, "/"))).map((id) => id.substring(nodeModulesFolder.length + 1));
      const npmModules = nodeModulesFolders.map((id) => id.replace(/\\/g, "/")).map((id) => {
        const parts = id.split("/");
        if (id.startsWith("@")) {
          return parts[0] + "/" + parts[1];
        } else {
          return parts[0];
        }
      }).sort().filter((value, index, self) => self.indexOf(value) === index);
      const npmModuleAndVersion = Object.fromEntries(npmModules.map((module) => [module, getVersion(module)]));
      const cvdls = Object.fromEntries(
        npmModules.filter((module) => getCvdlName(module) != null).map((module) => [module, { name: getCvdlName(module), version: getVersion(module) }])
      );
      mkdirSync2(path.dirname(statsFile), { recursive: true });
      const projectPackageJson = JSON.parse(readFileSync4(projectPackageJsonFile, { encoding: "utf-8" }));
      const entryScripts = Object.values(bundle).filter((bundle2) => bundle2.isEntry).map((bundle2) => bundle2.fileName);
      const generatedIndexHtml = path.resolve(buildOutputFolder, "index.html");
      const customIndexData = readFileSync4(projectIndexHtml, { encoding: "utf-8" });
      const generatedIndexData = readFileSync4(generatedIndexHtml, {
        encoding: "utf-8"
      });
      const customIndexRows = new Set(customIndexData.split(/[\r\n]/).filter((row) => row.trim() !== ""));
      const generatedIndexRows = generatedIndexData.split(/[\r\n]/).filter((row) => row.trim() !== "");
      const rowsGenerated = [];
      generatedIndexRows.forEach((row) => {
        if (!customIndexRows.has(row)) {
          rowsGenerated.push(row);
        }
      });
      const parseImports = (filename, result) => {
        const content = readFileSync4(filename, { encoding: "utf-8" });
        const lines = content.split("\n");
        const staticImports = lines.filter((line) => line.startsWith("import ")).map((line) => line.substring(line.indexOf("'") + 1, line.lastIndexOf("'"))).map((line) => line.includes("?") ? line.substring(0, line.lastIndexOf("?")) : line);
        const dynamicImports = lines.filter((line) => line.includes("import(")).map((line) => line.replace(/.*import\(/, "")).map((line) => line.split(/'/)[1]).map((line) => line.includes("?") ? line.substring(0, line.lastIndexOf("?")) : line);
        staticImports.forEach((staticImport) => result.add(staticImport));
        dynamicImports.map((dynamicImport) => {
          const importedFile = path.resolve(path.dirname(filename), dynamicImport);
          parseImports(importedFile, result);
        });
      };
      const generatedImportsSet = /* @__PURE__ */ new Set();
      parseImports(
        path.resolve(themeOptions.frontendGeneratedFolder, "flow", "generated-flow-imports.js"),
        generatedImportsSet
      );
      const generatedImports = Array.from(generatedImportsSet).sort();
      const frontendFiles = {};
      frontendFiles["index.html"] = createHash("sha256").update(customIndexData.replace(/\r\n/g, "\n"), "utf8").digest("hex");
      const projectFileExtensions = [".js", ".js.map", ".ts", ".ts.map", ".tsx", ".tsx.map", ".css", ".css.map"];
      const isThemeComponentsResource = (id) => id.startsWith(themeOptions.frontendGeneratedFolder.replace(/\\/g, "/")) && id.match(/.*\/jar-resources\/themes\/[^\/]+\/components\//);
      const isGeneratedWebComponentResource = (id) => id.startsWith(themeOptions.frontendGeneratedFolder.replace(/\\/g, "/")) && id.match(/.*\/flow\/web-components\//);
      const isFrontendResourceCollected = (id) => !id.startsWith(themeOptions.frontendGeneratedFolder.replace(/\\/g, "/")) || isThemeComponentsResource(id) || isGeneratedWebComponentResource(id);
      modules.map((id) => id.replace(/\\/g, "/")).filter((id) => id.startsWith(frontendFolder.replace(/\\/g, "/"))).filter(isFrontendResourceCollected).map((id) => id.substring(frontendFolder.length + 1)).map((line) => line.includes("?") ? line.substring(0, line.lastIndexOf("?")) : line).forEach((line) => {
        const filePath = path.resolve(frontendFolder, line);
        if (projectFileExtensions.includes(path.extname(filePath))) {
          const fileBuffer = readFileSync4(filePath, { encoding: "utf-8" }).replace(/\r\n/g, "\n");
          frontendFiles[line] = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
        }
      });
      generatedImports.filter((line) => line.includes("generated/jar-resources")).forEach((line) => {
        let filename = line.substring(line.indexOf("generated"));
        const fileBuffer = readFileSync4(path.resolve(frontendFolder, filename), { encoding: "utf-8" }).replace(
          /\r\n/g,
          "\n"
        );
        const hash = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
        const fileKey = line.substring(line.indexOf("jar-resources/") + 14);
        frontendFiles[fileKey] = hash;
      });
      let frontendFolderAlias = "Frontend";
      generatedImports.filter((line) => line.startsWith(frontendFolderAlias + "/")).filter((line) => !line.startsWith(frontendFolderAlias + "/generated/")).filter((line) => !line.startsWith(frontendFolderAlias + "/themes/")).map((line) => line.substring(frontendFolderAlias.length + 1)).filter((line) => !frontendFiles[line]).forEach((line) => {
        const filePath = path.resolve(frontendFolder, line);
        if (projectFileExtensions.includes(path.extname(filePath)) && existsSync5(filePath)) {
          const fileBuffer = readFileSync4(filePath, { encoding: "utf-8" }).replace(/\r\n/g, "\n");
          frontendFiles[line] = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
        }
      });
      if (existsSync5(path.resolve(frontendFolder, "index.ts"))) {
        const fileBuffer = readFileSync4(path.resolve(frontendFolder, "index.ts"), { encoding: "utf-8" }).replace(
          /\r\n/g,
          "\n"
        );
        frontendFiles[`index.ts`] = createHash("sha256").update(fileBuffer, "utf8").digest("hex");
      }
      const themeJsonContents = {};
      const themesFolder = path.resolve(jarResourcesFolder, "themes");
      if (existsSync5(themesFolder)) {
        readdirSync2(themesFolder).forEach((themeFolder2) => {
          const themeJson = path.resolve(themesFolder, themeFolder2, "theme.json");
          if (existsSync5(themeJson)) {
            themeJsonContents[path.basename(themeFolder2)] = readFileSync4(themeJson, { encoding: "utf-8" }).replace(
              /\r\n/g,
              "\n"
            );
          }
        });
      }
      collectThemeJsonsInFrontend(themeJsonContents, vaadin_dev_server_settings_default.themeName);
      let webComponents = [];
      if (webComponentTags) {
        webComponents = webComponentTags.split(";");
      }
      const stats = {
        packageJsonDependencies: projectPackageJson.dependencies,
        npmModules: npmModuleAndVersion,
        bundleImports: generatedImports,
        frontendHashes: frontendFiles,
        themeJsonContents,
        entryScripts,
        webComponents,
        cvdlModules: cvdls,
        packageJsonHash: projectPackageJson?.vaadin?.hash,
        indexHtmlGenerated: rowsGenerated
      };
      writeFileSync2(statsFile, JSON.stringify(stats, null, 1));
    }
  };
}
function vaadinBundlesPlugin() {
  const disabledMessage = "Vaadin component dependency bundles are disabled.";
  const modulesDirectory = nodeModulesFolder.replace(/\\/g, "/");
  let vaadinBundleJson;
  function parseModuleId(id) {
    const [scope, scopedPackageName] = id.split("/", 3);
    const packageName = scope.startsWith("@") ? `${scope}/${scopedPackageName}` : scope;
    const modulePath = `.${id.substring(packageName.length)}`;
    return {
      packageName,
      modulePath
    };
  }
  function getExports(id) {
    const { packageName, modulePath } = parseModuleId(id);
    const packageInfo = vaadinBundleJson.packages[packageName];
    if (!packageInfo) return;
    const exposeInfo = packageInfo.exposes[modulePath];
    if (!exposeInfo) return;
    const exportsSet = /* @__PURE__ */ new Set();
    for (const e of exposeInfo.exports) {
      if (typeof e === "string") {
        exportsSet.add(e);
      } else {
        const { namespace, source } = e;
        if (namespace) {
          exportsSet.add(namespace);
        } else {
          const sourceExports = getExports(source);
          if (sourceExports) {
            sourceExports.forEach((e2) => exportsSet.add(e2));
          }
        }
      }
    }
    return Array.from(exportsSet);
  }
  function getExportBinding(binding) {
    return binding === "default" ? "_default as default" : binding;
  }
  function getImportAssigment(binding) {
    return binding === "default" ? "default: _default" : binding;
  }
  return {
    name: "vaadin:bundles",
    enforce: "pre",
    apply(config, { command }) {
      if (command !== "serve") return false;
      try {
        const vaadinBundleJsonPath = require2.resolve("@vaadin/bundles/vaadin-bundle.json");
        vaadinBundleJson = JSON.parse(readFileSync4(vaadinBundleJsonPath, { encoding: "utf8" }));
      } catch (e) {
        if (typeof e === "object" && e.code === "MODULE_NOT_FOUND") {
          vaadinBundleJson = { packages: {} };
          console.info(`@vaadin/bundles npm package is not found, ${disabledMessage}`);
          return false;
        } else {
          throw e;
        }
      }
      const versionMismatches = [];
      for (const [name, packageInfo] of Object.entries(vaadinBundleJson.packages)) {
        let installedVersion = void 0;
        try {
          const { version: bundledVersion } = packageInfo;
          const installedPackageJsonFile = path.resolve(modulesDirectory, name, "package.json");
          const packageJson = JSON.parse(readFileSync4(installedPackageJsonFile, { encoding: "utf8" }));
          installedVersion = packageJson.version;
          if (installedVersion && installedVersion !== bundledVersion) {
            versionMismatches.push({
              name,
              bundledVersion,
              installedVersion
            });
          }
        } catch (_) {
        }
      }
      if (versionMismatches.length) {
        console.info(`@vaadin/bundles has version mismatches with installed packages, ${disabledMessage}`);
        console.info(`Packages with version mismatches: ${JSON.stringify(versionMismatches, void 0, 2)}`);
        vaadinBundleJson = { packages: {} };
        return false;
      }
      return true;
    },
    async config(config) {
      return mergeConfig(
        {
          optimizeDeps: {
            exclude: [
              // Vaadin bundle
              "@vaadin/bundles",
              ...Object.keys(vaadinBundleJson.packages),
              "@vaadin/vaadin-material-styles"
            ]
          }
        },
        config
      );
    },
    load(rawId) {
      const [path2, params] = rawId.split("?");
      if (!path2.startsWith(modulesDirectory)) return;
      const id = path2.substring(modulesDirectory.length + 1);
      const bindings = getExports(id);
      if (bindings === void 0) return;
      const cacheSuffix = params ? `?${params}` : "";
      const bundlePath = `@vaadin/bundles/vaadin.js${cacheSuffix}`;
      return `import { init as VaadinBundleInit, get as VaadinBundleGet } from '${bundlePath}';
await VaadinBundleInit('default');
const { ${bindings.map(getImportAssigment).join(", ")} } = (await VaadinBundleGet('./node_modules/${id}'))();
export { ${bindings.map(getExportBinding).join(", ")} };`;
    }
  };
}
function themePlugin(opts) {
  const fullThemeOptions = { ...themeOptions, devMode: opts.devMode };
  return {
    name: "vaadin:theme",
    config() {
      processThemeResources(fullThemeOptions, console);
    },
    configureServer(server) {
      function handleThemeFileCreateDelete(themeFile, stats) {
        if (themeFile.startsWith(themeFolder)) {
          const changed = path.relative(themeFolder, themeFile);
          console.debug("Theme file " + (!!stats ? "created" : "deleted"), changed);
          processThemeResources(fullThemeOptions, console);
        }
      }
      server.watcher.on("add", handleThemeFileCreateDelete);
      server.watcher.on("unlink", handleThemeFileCreateDelete);
    },
    handleHotUpdate(context) {
      const contextPath = path.resolve(context.file);
      const themePath = path.resolve(themeFolder);
      if (contextPath.startsWith(themePath)) {
        const changed = path.relative(themePath, contextPath);
        console.debug("Theme file changed", changed);
        if (changed.startsWith(vaadin_dev_server_settings_default.themeName)) {
          processThemeResources(fullThemeOptions, console);
        }
      }
    },
    async resolveId(id, importer) {
      if (path.resolve(themeOptions.frontendGeneratedFolder, "theme.js") === importer && !existsSync5(path.resolve(themeOptions.frontendGeneratedFolder, id))) {
        console.debug("Generate theme file " + id + " not existing. Processing theme resource");
        processThemeResources(fullThemeOptions, console);
        return;
      }
      if (!id.startsWith(vaadin_dev_server_settings_default.themeFolder)) {
        return;
      }
      for (const location of [themeResourceFolder, frontendFolder]) {
        const result = await this.resolve(path.resolve(location, id));
        if (result) {
          return result;
        }
      }
    },
    async transform(raw, id, options) {
      const [bareId, query] = id.split("?");
      if (!bareId?.startsWith(themeFolder) && !bareId?.startsWith(themeOptions.themeResourceFolder) || !bareId?.endsWith(".css")) {
        return;
      }
      const resourceThemeFolder = bareId.startsWith(themeFolder) ? themeFolder : themeOptions.themeResourceFolder;
      const [themeName] = bareId.substring(resourceThemeFolder.length + 1).split("/");
      return rewriteCssUrls(raw, path.dirname(bareId), path.resolve(resourceThemeFolder, themeName), console, opts);
    }
  };
}
function runWatchDog(watchDogPort, watchDogHost) {
  const client = new net.Socket();
  client.setEncoding("utf8");
  client.on("error", function(err) {
    console.log("Watchdog connection error. Terminating vite process...", err);
    client.destroy();
    process.exit(0);
  });
  client.on("close", function() {
    client.destroy();
    runWatchDog(watchDogPort, watchDogHost);
  });
  client.connect(watchDogPort, watchDogHost || "localhost");
}
var allowedFrontendFolders = [frontendFolder, nodeModulesFolder];
function showRecompileReason() {
  return {
    name: "vaadin:why-you-compile",
    handleHotUpdate(context) {
      console.log("Recompiling because", context.file, "changed");
    }
  };
}
var DEV_MODE_START_REGEXP = /\/\*[\*!]\s+vaadin-dev-mode:start/;
var DEV_MODE_CODE_REGEXP = /\/\*[\*!]\s+vaadin-dev-mode:start([\s\S]*)vaadin-dev-mode:end\s+\*\*\//i;
function preserveUsageStats() {
  return {
    name: "vaadin:preserve-usage-stats",
    transform(src, id) {
      if (id.includes("vaadin-usage-statistics")) {
        if (src.includes("vaadin-dev-mode:start")) {
          const newSrc = src.replace(DEV_MODE_START_REGEXP, "/*! vaadin-dev-mode:start");
          if (newSrc === src) {
            console.error("Comment replacement failed to change anything");
          } else if (!newSrc.match(DEV_MODE_CODE_REGEXP)) {
            console.error("New comment fails to match original regexp");
          } else {
            return { code: newSrc };
          }
        }
      }
      return { code: src };
    }
  };
}
var vaadinConfig = (env) => {
  const devMode = env.mode === "development";
  const productionMode = !devMode && !devBundle;
  if (devMode && process.env.watchDogPort) {
    runWatchDog(parseInt(process.env.watchDogPort), process.env.watchDogHost);
  }
  return {
    root: frontendFolder,
    base: "",
    publicDir: false,
    resolve: {
      alias: {
        "@vaadin/flow-frontend": jarResourcesFolder,
        Frontend: frontendFolder
      },
      preserveSymlinks: true
    },
    define: {
      OFFLINE_PATH: vaadin_dev_server_settings_default.offlinePath,
      VITE_ENABLED: "true"
    },
    server: {
      host: "127.0.0.1",
      strictPort: true,
      fs: {
        allow: allowedFrontendFolders
      }
    },
    build: {
      minify: productionMode,
      outDir: buildOutputFolder,
      emptyOutDir: devBundle,
      assetsDir: "VAADIN/build",
      target: ["esnext", "safari15"],
      rollupOptions: {
        input: {
          indexhtml: projectIndexHtml,
          ...hasExportedWebComponents ? { webcomponenthtml: path.resolve(frontendFolder, "web-component.html") } : {}
        },
        onwarn: (warning, defaultHandler) => {
          const ignoreEvalWarning = [
            "generated/jar-resources/FlowClient.js",
            "generated/jar-resources/vaadin-spreadsheet/spreadsheet-export.js",
            "@vaadin/charts/src/helpers.js"
          ];
          if (warning.code === "EVAL" && warning.id && !!ignoreEvalWarning.find((id) => warning.id?.endsWith(id))) {
            return;
          }
          defaultHandler(warning);
        }
      }
    },
    optimizeDeps: {
      entries: [
        // Pre-scan entrypoints in Vite to avoid reloading on first open
        "generated/vaadin.ts"
      ],
      exclude: [
        "@vaadin/router",
        "@vaadin/vaadin-license-checker",
        "@vaadin/vaadin-usage-statistics",
        "workbox-core",
        "workbox-precaching",
        "workbox-routing",
        "workbox-strategies"
      ]
    },
    plugins: [
      productionMode && brotli(),
      devMode && vaadinBundlesPlugin(),
      devMode && showRecompileReason(),
      vaadin_dev_server_settings_default.offlineEnabled && buildSWPlugin({ devMode }),
      !devMode && statsExtracterPlugin(),
      !productionMode && preserveUsageStats(),
      themePlugin({ devMode }),
      postcssLit({
        include: ["**/*.css", /.*\/.*\.css\?.*/],
        exclude: [
          `${themeFolder}/**/*.css`,
          new RegExp(`${themeFolder}/.*/.*\\.css\\?.*`),
          `${themeResourceFolder}/**/*.css`,
          new RegExp(`${themeResourceFolder}/.*/.*\\.css\\?.*`),
          new RegExp(".*/.*\\?html-proxy.*")
        ]
      }),
      // The React plugin provides fast refresh and debug source info
      reactPlugin({
        include: "**/*.tsx",
        babel: {
          // We need to use babel to provide the source information for it to be correct
          // (otherwise Babel will slightly rewrite the source file and esbuild generate source info for the modified file)
          presets: [["@babel/preset-react", { runtime: "automatic", development: !productionMode }]],
          // React writes the source location for where components are used, this writes for where they are defined
          plugins: [
            !productionMode && addFunctionComponentSourceLocationBabel()
          ].filter(Boolean)
        }
      }),
      {
        name: "vaadin:force-remove-html-middleware",
        configureServer(server) {
          return () => {
            server.middlewares.stack = server.middlewares.stack.filter((mw) => {
              const handleName = `${mw.handle}`;
              return !handleName.includes("viteHtmlFallbackMiddleware");
            });
          };
        }
      },
      hasExportedWebComponents && {
        name: "vaadin:inject-entrypoints-to-web-component-html",
        transformIndexHtml: {
          order: "pre",
          handler(_html, { path: path2, server }) {
            if (path2 !== "/web-component.html") {
              return;
            }
            return [
              {
                tag: "script",
                attrs: { type: "module", src: `/generated/vaadin-web-component.ts` },
                injectTo: "head"
              }
            ];
          }
        }
      },
      {
        name: "vaadin:inject-entrypoints-to-index-html",
        transformIndexHtml: {
          order: "pre",
          handler(_html, { path: path2, server }) {
            if (path2 !== "/index.html") {
              return;
            }
            const scripts = [];
            if (devMode) {
              scripts.push({
                tag: "script",
                attrs: { type: "module", src: `/generated/vite-devmode.ts`, onerror: "document.location.reload()" },
                injectTo: "head"
              });
            }
            scripts.push({
              tag: "script",
              attrs: { type: "module", src: "/generated/vaadin.ts" },
              injectTo: "head"
            });
            return scripts;
          }
        }
      },
      checker({
        typescript: true
      }),
      productionMode && visualizer({ brotliSize: true, filename: bundleSizeFile })
    ]
  };
};
var overrideVaadinConfig = (customConfig2) => {
  return defineConfig((env) => mergeConfig(vaadinConfig(env), customConfig2(env)));
};
function getVersion(module) {
  const packageJson = path.resolve(nodeModulesFolder, module, "package.json");
  return JSON.parse(readFileSync4(packageJson, { encoding: "utf-8" })).version;
}
function getCvdlName(module) {
  const packageJson = path.resolve(nodeModulesFolder, module, "package.json");
  return JSON.parse(readFileSync4(packageJson, { encoding: "utf-8" })).cvdlName;
}

// vite.config.ts
var customConfig = (env) => ({
  // Here you can add custom Vite parameters
  // https://vitejs.dev/config/
});
var vite_config_default = overrideVaadinConfig(customConfig);
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5nZW5lcmF0ZWQudHMiLCAiYnVpbGQvcGx1Z2lucy9hcHBsaWNhdGlvbi10aGVtZS1wbHVnaW4vdGhlbWUtaGFuZGxlLmpzIiwgImJ1aWxkL3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWdlbmVyYXRvci5qcyIsICJidWlsZC9wbHVnaW5zL2FwcGxpY2F0aW9uLXRoZW1lLXBsdWdpbi90aGVtZS1jb3B5LmpzIiwgImJ1aWxkL3BsdWdpbnMvdGhlbWUtbG9hZGVyL3RoZW1lLWxvYWRlci11dGlscy5qcyIsICJidWlsZC9wbHVnaW5zL3JlYWN0LWZ1bmN0aW9uLWxvY2F0aW9uLXBsdWdpbi9yZWFjdC1mdW5jdGlvbi1sb2NhdGlvbi1wbHVnaW4uanMiLCAiYnVpbGQvdmFhZGluLWRldi1zZXJ2ZXItc2V0dGluZ3MuanNvbiIsICJidWlsZC9wbHVnaW5zL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQtY3VzdG9tL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQuanMiLCAidml0ZS5jb25maWcudHMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFx1dGVudGVcXFxcSWRlYVByb2plY3RzXFxcXGR1ZWVtbWVzdG9yZVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcdml0ZS5nZW5lcmF0ZWQudHNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL3V0ZW50ZS9JZGVhUHJvamVjdHMvZHVlZW1tZXN0b3JlL3ZpdGUuZ2VuZXJhdGVkLnRzXCI7LyoqXG4gKiBOT1RJQ0U6IHRoaXMgaXMgYW4gYXV0by1nZW5lcmF0ZWQgZmlsZVxuICpcbiAqIFRoaXMgZmlsZSBoYXMgYmVlbiBnZW5lcmF0ZWQgYnkgdGhlIGBmbG93OnByZXBhcmUtZnJvbnRlbmRgIG1hdmVuIGdvYWwuXG4gKiBUaGlzIGZpbGUgd2lsbCBiZSBvdmVyd3JpdHRlbiBvbiBldmVyeSBydW4uIEFueSBjdXN0b20gY2hhbmdlcyBzaG91bGQgYmUgbWFkZSB0byB2aXRlLmNvbmZpZy50c1xuICovXG5pbXBvcnQgcGF0aCBmcm9tICdwYXRoJztcbmltcG9ydCB7IGV4aXN0c1N5bmMsIG1rZGlyU3luYywgcmVhZGRpclN5bmMsIHJlYWRGaWxlU3luYywgd3JpdGVGaWxlU3luYywgU3RhdHMgfSBmcm9tICdmcyc7XG5pbXBvcnQgeyBjcmVhdGVIYXNoIH0gZnJvbSAnY3J5cHRvJztcbmltcG9ydCAqIGFzIG5ldCBmcm9tICduZXQnO1xuXG5pbXBvcnQgeyBwcm9jZXNzVGhlbWVSZXNvdXJjZXMgfSBmcm9tICcuL2J1aWxkL3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWhhbmRsZS5qcyc7XG5pbXBvcnQgeyByZXdyaXRlQ3NzVXJscyB9IGZyb20gJy4vYnVpbGQvcGx1Z2lucy90aGVtZS1sb2FkZXIvdGhlbWUtbG9hZGVyLXV0aWxzLmpzJztcbmltcG9ydCB7IGFkZEZ1bmN0aW9uQ29tcG9uZW50U291cmNlTG9jYXRpb25CYWJlbCB9IGZyb20gJy4vYnVpbGQvcGx1Z2lucy9yZWFjdC1mdW5jdGlvbi1sb2NhdGlvbi1wbHVnaW4vcmVhY3QtZnVuY3Rpb24tbG9jYXRpb24tcGx1Z2luLmpzJztcbmltcG9ydCBzZXR0aW5ncyBmcm9tICcuL2J1aWxkL3ZhYWRpbi1kZXYtc2VydmVyLXNldHRpbmdzLmpzb24nO1xuaW1wb3J0IHtcbiAgQXNzZXRJbmZvLFxuICBDaHVua0luZm8sXG4gIGRlZmluZUNvbmZpZyxcbiAgbWVyZ2VDb25maWcsXG4gIE91dHB1dE9wdGlvbnMsXG4gIFBsdWdpbk9wdGlvbixcbiAgUmVzb2x2ZWRDb25maWcsXG4gIFVzZXJDb25maWdGblxufSBmcm9tICd2aXRlJztcbmltcG9ydCB7IGdldE1hbmlmZXN0LCB0eXBlIE1hbmlmZXN0VHJhbnNmb3JtIH0gZnJvbSAnd29ya2JveC1idWlsZCc7XG5cbmltcG9ydCAqIGFzIHJvbGx1cCBmcm9tICdyb2xsdXAnO1xuaW1wb3J0IGJyb3RsaSBmcm9tICdyb2xsdXAtcGx1Z2luLWJyb3RsaSc7XG5pbXBvcnQgcmVwbGFjZSBmcm9tICdAcm9sbHVwL3BsdWdpbi1yZXBsYWNlJztcbmltcG9ydCBjaGVja2VyIGZyb20gJ3ZpdGUtcGx1Z2luLWNoZWNrZXInO1xuaW1wb3J0IHBvc3Rjc3NMaXQgZnJvbSAnLi9idWlsZC9wbHVnaW5zL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQtY3VzdG9tL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQuanMnO1xuXG5pbXBvcnQgeyBjcmVhdGVSZXF1aXJlIH0gZnJvbSAnbW9kdWxlJztcblxuaW1wb3J0IHsgdmlzdWFsaXplciB9IGZyb20gJ3JvbGx1cC1wbHVnaW4tdmlzdWFsaXplcic7XG5pbXBvcnQgcmVhY3RQbHVnaW4gZnJvbSAnQHZpdGVqcy9wbHVnaW4tcmVhY3QnO1xuXG5cblxuLy8gTWFrZSBgcmVxdWlyZWAgY29tcGF0aWJsZSB3aXRoIEVTIG1vZHVsZXNcbmNvbnN0IHJlcXVpcmUgPSBjcmVhdGVSZXF1aXJlKGltcG9ydC5tZXRhLnVybCk7XG5cbmNvbnN0IGFwcFNoZWxsVXJsID0gJy4nO1xuXG5jb25zdCBmcm9udGVuZEZvbGRlciA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIHNldHRpbmdzLmZyb250ZW5kRm9sZGVyKTtcbmNvbnN0IHRoZW1lRm9sZGVyID0gcGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCBzZXR0aW5ncy50aGVtZUZvbGRlcik7XG5jb25zdCBmcm9udGVuZEJ1bmRsZUZvbGRlciA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIHNldHRpbmdzLmZyb250ZW5kQnVuZGxlT3V0cHV0KTtcbmNvbnN0IGRldkJ1bmRsZUZvbGRlciA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIHNldHRpbmdzLmRldkJ1bmRsZU91dHB1dCk7XG5jb25zdCBkZXZCdW5kbGUgPSAhIXByb2Nlc3MuZW52LmRldkJ1bmRsZTtcbmNvbnN0IGphclJlc291cmNlc0ZvbGRlciA9IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsIHNldHRpbmdzLmphclJlc291cmNlc0ZvbGRlcik7XG5jb25zdCB0aGVtZVJlc291cmNlRm9sZGVyID0gcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgc2V0dGluZ3MudGhlbWVSZXNvdXJjZUZvbGRlcik7XG5jb25zdCBwcm9qZWN0UGFja2FnZUpzb25GaWxlID0gcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgJ3BhY2thZ2UuanNvbicpO1xuXG5jb25zdCBidWlsZE91dHB1dEZvbGRlciA9IGRldkJ1bmRsZSA/IGRldkJ1bmRsZUZvbGRlciA6IGZyb250ZW5kQnVuZGxlRm9sZGVyO1xuY29uc3Qgc3RhdHNGb2xkZXIgPSBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCBkZXZCdW5kbGUgPyBzZXR0aW5ncy5kZXZCdW5kbGVTdGF0c091dHB1dCA6IHNldHRpbmdzLnN0YXRzT3V0cHV0KTtcbmNvbnN0IHN0YXRzRmlsZSA9IHBhdGgucmVzb2x2ZShzdGF0c0ZvbGRlciwgJ3N0YXRzLmpzb24nKTtcbmNvbnN0IGJ1bmRsZVNpemVGaWxlID0gcGF0aC5yZXNvbHZlKHN0YXRzRm9sZGVyLCAnYnVuZGxlLXNpemUuaHRtbCcpO1xuY29uc3Qgbm9kZU1vZHVsZXNGb2xkZXIgPSBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAnbm9kZV9tb2R1bGVzJyk7XG5jb25zdCB3ZWJDb21wb25lbnRUYWdzID0gJyc7XG5cbmNvbnN0IHByb2plY3RJbmRleEh0bWwgPSBwYXRoLnJlc29sdmUoZnJvbnRlbmRGb2xkZXIsICdpbmRleC5odG1sJyk7XG5cbmNvbnN0IHByb2plY3RTdGF0aWNBc3NldHNGb2xkZXJzID0gW1xuICBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAnc3JjJywgJ21haW4nLCAncmVzb3VyY2VzJywgJ01FVEEtSU5GJywgJ3Jlc291cmNlcycpLFxuICBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCAnc3JjJywgJ21haW4nLCAncmVzb3VyY2VzJywgJ3N0YXRpYycpLFxuICBmcm9udGVuZEZvbGRlclxuXTtcblxuLy8gRm9sZGVycyBpbiB0aGUgcHJvamVjdCB3aGljaCBjYW4gY29udGFpbiBhcHBsaWNhdGlvbiB0aGVtZXNcbmNvbnN0IHRoZW1lUHJvamVjdEZvbGRlcnMgPSBwcm9qZWN0U3RhdGljQXNzZXRzRm9sZGVycy5tYXAoKGZvbGRlcikgPT4gcGF0aC5yZXNvbHZlKGZvbGRlciwgc2V0dGluZ3MudGhlbWVGb2xkZXIpKTtcblxuY29uc3QgdGhlbWVPcHRpb25zID0ge1xuICBkZXZNb2RlOiBmYWxzZSxcbiAgdXNlRGV2QnVuZGxlOiBkZXZCdW5kbGUsXG4gIC8vIFRoZSBmb2xsb3dpbmcgbWF0Y2hlcyBmb2xkZXIgJ2Zyb250ZW5kL2dlbmVyYXRlZC90aGVtZXMvJ1xuICAvLyAobm90ICdmcm9udGVuZC90aGVtZXMnKSBmb3IgdGhlbWUgaW4gSkFSIHRoYXQgaXMgY29waWVkIHRoZXJlXG4gIHRoZW1lUmVzb3VyY2VGb2xkZXI6IHBhdGgucmVzb2x2ZSh0aGVtZVJlc291cmNlRm9sZGVyLCBzZXR0aW5ncy50aGVtZUZvbGRlciksXG4gIHRoZW1lUHJvamVjdEZvbGRlcnM6IHRoZW1lUHJvamVjdEZvbGRlcnMsXG4gIHByb2plY3RTdGF0aWNBc3NldHNPdXRwdXRGb2xkZXI6IGRldkJ1bmRsZVxuICAgID8gcGF0aC5yZXNvbHZlKGRldkJ1bmRsZUZvbGRlciwgJy4uL2Fzc2V0cycpXG4gICAgOiBwYXRoLnJlc29sdmUoX19kaXJuYW1lLCBzZXR0aW5ncy5zdGF0aWNPdXRwdXQpLFxuICBmcm9udGVuZEdlbmVyYXRlZEZvbGRlcjogcGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCBzZXR0aW5ncy5nZW5lcmF0ZWRGb2xkZXIpXG59O1xuXG5jb25zdCBoYXNFeHBvcnRlZFdlYkNvbXBvbmVudHMgPSBleGlzdHNTeW5jKHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgJ3dlYi1jb21wb25lbnQuaHRtbCcpKTtcblxuLy8gQmxvY2sgZGVidWcgYW5kIHRyYWNlIGxvZ3MuXG5jb25zb2xlLnRyYWNlID0gKCkgPT4ge307XG5jb25zb2xlLmRlYnVnID0gKCkgPT4ge307XG5cbmZ1bmN0aW9uIGluamVjdE1hbmlmZXN0VG9TV1BsdWdpbigpOiByb2xsdXAuUGx1Z2luIHtcbiAgY29uc3QgcmV3cml0ZU1hbmlmZXN0SW5kZXhIdG1sVXJsOiBNYW5pZmVzdFRyYW5zZm9ybSA9IChtYW5pZmVzdCkgPT4ge1xuICAgIGNvbnN0IGluZGV4RW50cnkgPSBtYW5pZmVzdC5maW5kKChlbnRyeSkgPT4gZW50cnkudXJsID09PSAnaW5kZXguaHRtbCcpO1xuICAgIGlmIChpbmRleEVudHJ5KSB7XG4gICAgICBpbmRleEVudHJ5LnVybCA9IGFwcFNoZWxsVXJsO1xuICAgIH1cblxuICAgIHJldHVybiB7IG1hbmlmZXN0LCB3YXJuaW5nczogW10gfTtcbiAgfTtcblxuICByZXR1cm4ge1xuICAgIG5hbWU6ICd2YWFkaW46aW5qZWN0LW1hbmlmZXN0LXRvLXN3JyxcbiAgICBhc3luYyB0cmFuc2Zvcm0oY29kZSwgaWQpIHtcbiAgICAgIGlmICgvc3dcXC4odHN8anMpJC8udGVzdChpZCkpIHtcbiAgICAgICAgY29uc3QgeyBtYW5pZmVzdEVudHJpZXMgfSA9IGF3YWl0IGdldE1hbmlmZXN0KHtcbiAgICAgICAgICBnbG9iRGlyZWN0b3J5OiBidWlsZE91dHB1dEZvbGRlcixcbiAgICAgICAgICBnbG9iUGF0dGVybnM6IFsnKiovKiddLFxuICAgICAgICAgIGdsb2JJZ25vcmVzOiBbJyoqLyouYnInXSxcbiAgICAgICAgICBtYW5pZmVzdFRyYW5zZm9ybXM6IFtyZXdyaXRlTWFuaWZlc3RJbmRleEh0bWxVcmxdLFxuICAgICAgICAgIG1heGltdW1GaWxlU2l6ZVRvQ2FjaGVJbkJ5dGVzOiAxMDAgKiAxMDI0ICogMTAyNCAvLyAxMDBtYixcbiAgICAgICAgfSk7XG5cbiAgICAgICAgcmV0dXJuIGNvZGUucmVwbGFjZSgnc2VsZi5fX1dCX01BTklGRVNUJywgSlNPTi5zdHJpbmdpZnkobWFuaWZlc3RFbnRyaWVzKSk7XG4gICAgICB9XG4gICAgfVxuICB9O1xufVxuXG5mdW5jdGlvbiBidWlsZFNXUGx1Z2luKG9wdHM6IHsgZGV2TW9kZTogYm9vbGVhbiB9KTogUGx1Z2luT3B0aW9uIHtcbiAgbGV0IGNvbmZpZzogUmVzb2x2ZWRDb25maWc7XG4gIGNvbnN0IGRldk1vZGUgPSBvcHRzLmRldk1vZGU7XG5cbiAgY29uc3Qgc3dPYmo6IHsgY29kZT86IHN0cmluZywgbWFwPzogcm9sbHVwLlNvdXJjZU1hcCB8IG51bGwgfSA9IHt9O1xuXG4gIGFzeW5jIGZ1bmN0aW9uIGJ1aWxkKGFjdGlvbjogJ2dlbmVyYXRlJyB8ICd3cml0ZScsIGFkZGl0aW9uYWxQbHVnaW5zOiByb2xsdXAuUGx1Z2luW10gPSBbXSkge1xuICAgIGNvbnN0IGluY2x1ZGVkUGx1Z2luTmFtZXMgPSBbXG4gICAgICAndml0ZTplc2J1aWxkJyxcbiAgICAgICdyb2xsdXAtcGx1Z2luLWR5bmFtaWMtaW1wb3J0LXZhcmlhYmxlcycsXG4gICAgICAndml0ZTplc2J1aWxkLXRyYW5zcGlsZScsXG4gICAgICAndml0ZTp0ZXJzZXInXG4gICAgXTtcbiAgICBjb25zdCBwbHVnaW5zOiByb2xsdXAuUGx1Z2luW10gPSBjb25maWcucGx1Z2lucy5maWx0ZXIoKHApID0+IHtcbiAgICAgIHJldHVybiBpbmNsdWRlZFBsdWdpbk5hbWVzLmluY2x1ZGVzKHAubmFtZSk7XG4gICAgfSk7XG4gICAgY29uc3QgcmVzb2x2ZXIgPSBjb25maWcuY3JlYXRlUmVzb2x2ZXIoKTtcbiAgICBjb25zdCByZXNvbHZlUGx1Z2luOiByb2xsdXAuUGx1Z2luID0ge1xuICAgICAgbmFtZTogJ3Jlc29sdmVyJyxcbiAgICAgIHJlc29sdmVJZChzb3VyY2UsIGltcG9ydGVyLCBfb3B0aW9ucykge1xuICAgICAgICByZXR1cm4gcmVzb2x2ZXIoc291cmNlLCBpbXBvcnRlcik7XG4gICAgICB9XG4gICAgfTtcbiAgICBwbHVnaW5zLnVuc2hpZnQocmVzb2x2ZVBsdWdpbik7IC8vIFB1dCByZXNvbHZlIGZpcnN0XG4gICAgcGx1Z2lucy5wdXNoKFxuICAgICAgcmVwbGFjZSh7XG4gICAgICAgIHZhbHVlczoge1xuICAgICAgICAgICdwcm9jZXNzLmVudi5OT0RFX0VOVic6IEpTT04uc3RyaW5naWZ5KGNvbmZpZy5tb2RlKSxcbiAgICAgICAgICAuLi5jb25maWcuZGVmaW5lXG4gICAgICAgIH0sXG4gICAgICAgIHByZXZlbnRBc3NpZ25tZW50OiB0cnVlXG4gICAgICB9KVxuICAgICk7XG4gICAgaWYgKGFkZGl0aW9uYWxQbHVnaW5zKSB7XG4gICAgICBwbHVnaW5zLnB1c2goLi4uYWRkaXRpb25hbFBsdWdpbnMpO1xuICAgIH1cbiAgICBjb25zdCBidW5kbGUgPSBhd2FpdCByb2xsdXAucm9sbHVwKHtcbiAgICAgIGlucHV0OiBwYXRoLnJlc29sdmUoc2V0dGluZ3MuY2xpZW50U2VydmljZVdvcmtlclNvdXJjZSksXG4gICAgICBwbHVnaW5zXG4gICAgfSk7XG5cbiAgICB0cnkge1xuICAgICAgcmV0dXJuIGF3YWl0IGJ1bmRsZVthY3Rpb25dKHtcbiAgICAgICAgZmlsZTogcGF0aC5yZXNvbHZlKGJ1aWxkT3V0cHV0Rm9sZGVyLCAnc3cuanMnKSxcbiAgICAgICAgZm9ybWF0OiAnZXMnLFxuICAgICAgICBleHBvcnRzOiAnbm9uZScsXG4gICAgICAgIHNvdXJjZW1hcDogY29uZmlnLmNvbW1hbmQgPT09ICdzZXJ2ZScgfHwgY29uZmlnLmJ1aWxkLnNvdXJjZW1hcCxcbiAgICAgICAgaW5saW5lRHluYW1pY0ltcG9ydHM6IHRydWVcbiAgICAgIH0pO1xuICAgIH0gZmluYWxseSB7XG4gICAgICBhd2FpdCBidW5kbGUuY2xvc2UoKTtcbiAgICB9XG4gIH1cblxuICByZXR1cm4ge1xuICAgIG5hbWU6ICd2YWFkaW46YnVpbGQtc3cnLFxuICAgIGVuZm9yY2U6ICdwb3N0JyxcbiAgICBhc3luYyBjb25maWdSZXNvbHZlZChyZXNvbHZlZENvbmZpZykge1xuICAgICAgY29uZmlnID0gcmVzb2x2ZWRDb25maWc7XG4gICAgfSxcbiAgICBhc3luYyBidWlsZFN0YXJ0KCkge1xuICAgICAgaWYgKGRldk1vZGUpIHtcbiAgICAgICAgY29uc3QgeyBvdXRwdXQgfSA9IGF3YWl0IGJ1aWxkKCdnZW5lcmF0ZScpO1xuICAgICAgICBzd09iai5jb2RlID0gb3V0cHV0WzBdLmNvZGU7XG4gICAgICAgIHN3T2JqLm1hcCA9IG91dHB1dFswXS5tYXA7XG4gICAgICB9XG4gICAgfSxcbiAgICBhc3luYyBsb2FkKGlkKSB7XG4gICAgICBpZiAoaWQuZW5kc1dpdGgoJ3N3LmpzJykpIHtcbiAgICAgICAgcmV0dXJuICcnO1xuICAgICAgfVxuICAgIH0sXG4gICAgYXN5bmMgdHJhbnNmb3JtKF9jb2RlLCBpZCkge1xuICAgICAgaWYgKGlkLmVuZHNXaXRoKCdzdy5qcycpKSB7XG4gICAgICAgIHJldHVybiBzd09iajtcbiAgICAgIH1cbiAgICB9LFxuICAgIGFzeW5jIGNsb3NlQnVuZGxlKCkge1xuICAgICAgaWYgKCFkZXZNb2RlKSB7XG4gICAgICAgIGF3YWl0IGJ1aWxkKCd3cml0ZScsIFtpbmplY3RNYW5pZmVzdFRvU1dQbHVnaW4oKSwgYnJvdGxpKCldKTtcbiAgICAgIH1cbiAgICB9XG4gIH07XG59XG5cbmZ1bmN0aW9uIHN0YXRzRXh0cmFjdGVyUGx1Z2luKCk6IFBsdWdpbk9wdGlvbiB7XG4gIGZ1bmN0aW9uIGNvbGxlY3RUaGVtZUpzb25zSW5Gcm9udGVuZCh0aGVtZUpzb25Db250ZW50czogUmVjb3JkPHN0cmluZywgc3RyaW5nPiwgdGhlbWVOYW1lOiBzdHJpbmcpIHtcbiAgICBjb25zdCB0aGVtZUpzb24gPSBwYXRoLnJlc29sdmUoZnJvbnRlbmRGb2xkZXIsIHNldHRpbmdzLnRoZW1lRm9sZGVyLCB0aGVtZU5hbWUsICd0aGVtZS5qc29uJyk7XG4gICAgaWYgKGV4aXN0c1N5bmModGhlbWVKc29uKSkge1xuICAgICAgY29uc3QgdGhlbWVKc29uQ29udGVudCA9IHJlYWRGaWxlU3luYyh0aGVtZUpzb24sIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkucmVwbGFjZSgvXFxyXFxuL2csICdcXG4nKTtcbiAgICAgIHRoZW1lSnNvbkNvbnRlbnRzW3RoZW1lTmFtZV0gPSB0aGVtZUpzb25Db250ZW50O1xuICAgICAgY29uc3QgdGhlbWVKc29uT2JqZWN0ID0gSlNPTi5wYXJzZSh0aGVtZUpzb25Db250ZW50KTtcbiAgICAgIGlmICh0aGVtZUpzb25PYmplY3QucGFyZW50KSB7XG4gICAgICAgIGNvbGxlY3RUaGVtZUpzb25zSW5Gcm9udGVuZCh0aGVtZUpzb25Db250ZW50cywgdGhlbWVKc29uT2JqZWN0LnBhcmVudCk7XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOnN0YXRzJyxcbiAgICBlbmZvcmNlOiAncG9zdCcsXG4gICAgYXN5bmMgd3JpdGVCdW5kbGUob3B0aW9uczogT3V0cHV0T3B0aW9ucywgYnVuZGxlOiB7IFtmaWxlTmFtZTogc3RyaW5nXTogQXNzZXRJbmZvIHwgQ2h1bmtJbmZvIH0pIHtcbiAgICAgIGNvbnN0IG1vZHVsZXMgPSBPYmplY3QudmFsdWVzKGJ1bmRsZSkuZmxhdE1hcCgoYikgPT4gKGIubW9kdWxlcyA/IE9iamVjdC5rZXlzKGIubW9kdWxlcykgOiBbXSkpO1xuICAgICAgY29uc3Qgbm9kZU1vZHVsZXNGb2xkZXJzID0gbW9kdWxlc1xuICAgICAgICAubWFwKChpZCkgPT4gaWQucmVwbGFjZSgvXFxcXC9nLCAnLycpKVxuICAgICAgICAuZmlsdGVyKChpZCkgPT4gaWQuc3RhcnRzV2l0aChub2RlTW9kdWxlc0ZvbGRlci5yZXBsYWNlKC9cXFxcL2csICcvJykpKVxuICAgICAgICAubWFwKChpZCkgPT4gaWQuc3Vic3RyaW5nKG5vZGVNb2R1bGVzRm9sZGVyLmxlbmd0aCArIDEpKTtcbiAgICAgIGNvbnN0IG5wbU1vZHVsZXMgPSBub2RlTW9kdWxlc0ZvbGRlcnNcbiAgICAgICAgLm1hcCgoaWQpID0+IGlkLnJlcGxhY2UoL1xcXFwvZywgJy8nKSlcbiAgICAgICAgLm1hcCgoaWQpID0+IHtcbiAgICAgICAgICBjb25zdCBwYXJ0cyA9IGlkLnNwbGl0KCcvJyk7XG4gICAgICAgICAgaWYgKGlkLnN0YXJ0c1dpdGgoJ0AnKSkge1xuICAgICAgICAgICAgcmV0dXJuIHBhcnRzWzBdICsgJy8nICsgcGFydHNbMV07XG4gICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgIHJldHVybiBwYXJ0c1swXTtcbiAgICAgICAgICB9XG4gICAgICAgIH0pXG4gICAgICAgIC5zb3J0KClcbiAgICAgICAgLmZpbHRlcigodmFsdWUsIGluZGV4LCBzZWxmKSA9PiBzZWxmLmluZGV4T2YodmFsdWUpID09PSBpbmRleCk7XG4gICAgICBjb25zdCBucG1Nb2R1bGVBbmRWZXJzaW9uID0gT2JqZWN0LmZyb21FbnRyaWVzKG5wbU1vZHVsZXMubWFwKChtb2R1bGUpID0+IFttb2R1bGUsIGdldFZlcnNpb24obW9kdWxlKV0pKTtcbiAgICAgIGNvbnN0IGN2ZGxzID0gT2JqZWN0LmZyb21FbnRyaWVzKFxuICAgICAgICBucG1Nb2R1bGVzXG4gICAgICAgICAgLmZpbHRlcigobW9kdWxlKSA9PiBnZXRDdmRsTmFtZShtb2R1bGUpICE9IG51bGwpXG4gICAgICAgICAgLm1hcCgobW9kdWxlKSA9PiBbbW9kdWxlLCB7IG5hbWU6IGdldEN2ZGxOYW1lKG1vZHVsZSksIHZlcnNpb246IGdldFZlcnNpb24obW9kdWxlKSB9XSlcbiAgICAgICk7XG5cbiAgICAgIG1rZGlyU3luYyhwYXRoLmRpcm5hbWUoc3RhdHNGaWxlKSwgeyByZWN1cnNpdmU6IHRydWUgfSk7XG4gICAgICBjb25zdCBwcm9qZWN0UGFja2FnZUpzb24gPSBKU09OLnBhcnNlKHJlYWRGaWxlU3luYyhwcm9qZWN0UGFja2FnZUpzb25GaWxlLCB7IGVuY29kaW5nOiAndXRmLTgnIH0pKTtcblxuICAgICAgY29uc3QgZW50cnlTY3JpcHRzID0gT2JqZWN0LnZhbHVlcyhidW5kbGUpXG4gICAgICAgIC5maWx0ZXIoKGJ1bmRsZSkgPT4gYnVuZGxlLmlzRW50cnkpXG4gICAgICAgIC5tYXAoKGJ1bmRsZSkgPT4gYnVuZGxlLmZpbGVOYW1lKTtcblxuICAgICAgY29uc3QgZ2VuZXJhdGVkSW5kZXhIdG1sID0gcGF0aC5yZXNvbHZlKGJ1aWxkT3V0cHV0Rm9sZGVyLCAnaW5kZXguaHRtbCcpO1xuICAgICAgY29uc3QgY3VzdG9tSW5kZXhEYXRhOiBzdHJpbmcgPSByZWFkRmlsZVN5bmMocHJvamVjdEluZGV4SHRtbCwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KTtcbiAgICAgIGNvbnN0IGdlbmVyYXRlZEluZGV4RGF0YTogc3RyaW5nID0gcmVhZEZpbGVTeW5jKGdlbmVyYXRlZEluZGV4SHRtbCwge1xuICAgICAgICBlbmNvZGluZzogJ3V0Zi04J1xuICAgICAgfSk7XG5cbiAgICAgIGNvbnN0IGN1c3RvbUluZGV4Um93cyA9IG5ldyBTZXQoY3VzdG9tSW5kZXhEYXRhLnNwbGl0KC9bXFxyXFxuXS8pLmZpbHRlcigocm93KSA9PiByb3cudHJpbSgpICE9PSAnJykpO1xuICAgICAgY29uc3QgZ2VuZXJhdGVkSW5kZXhSb3dzID0gZ2VuZXJhdGVkSW5kZXhEYXRhLnNwbGl0KC9bXFxyXFxuXS8pLmZpbHRlcigocm93KSA9PiByb3cudHJpbSgpICE9PSAnJyk7XG5cbiAgICAgIGNvbnN0IHJvd3NHZW5lcmF0ZWQ6IHN0cmluZ1tdID0gW107XG4gICAgICBnZW5lcmF0ZWRJbmRleFJvd3MuZm9yRWFjaCgocm93KSA9PiB7XG4gICAgICAgIGlmICghY3VzdG9tSW5kZXhSb3dzLmhhcyhyb3cpKSB7XG4gICAgICAgICAgcm93c0dlbmVyYXRlZC5wdXNoKHJvdyk7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuXG4gICAgICAvL0FmdGVyIGRldi1idW5kbGUgYnVpbGQgYWRkIHVzZWQgRmxvdyBmcm9udGVuZCBpbXBvcnRzIEpzTW9kdWxlL0phdmFTY3JpcHQvQ3NzSW1wb3J0XG5cbiAgICAgIGNvbnN0IHBhcnNlSW1wb3J0cyA9IChmaWxlbmFtZTogc3RyaW5nLCByZXN1bHQ6IFNldDxzdHJpbmc+KTogdm9pZCA9PiB7XG4gICAgICAgIGNvbnN0IGNvbnRlbnQ6IHN0cmluZyA9IHJlYWRGaWxlU3luYyhmaWxlbmFtZSwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KTtcbiAgICAgICAgY29uc3QgbGluZXMgPSBjb250ZW50LnNwbGl0KCdcXG4nKTtcbiAgICAgICAgY29uc3Qgc3RhdGljSW1wb3J0cyA9IGxpbmVzXG4gICAgICAgICAgLmZpbHRlcigobGluZSkgPT4gbGluZS5zdGFydHNXaXRoKCdpbXBvcnQgJykpXG4gICAgICAgICAgLm1hcCgobGluZSkgPT4gbGluZS5zdWJzdHJpbmcobGluZS5pbmRleE9mKFwiJ1wiKSArIDEsIGxpbmUubGFzdEluZGV4T2YoXCInXCIpKSlcbiAgICAgICAgICAubWFwKChsaW5lKSA9PiAobGluZS5pbmNsdWRlcygnPycpID8gbGluZS5zdWJzdHJpbmcoMCwgbGluZS5sYXN0SW5kZXhPZignPycpKSA6IGxpbmUpKTtcbiAgICAgICAgY29uc3QgZHluYW1pY0ltcG9ydHMgPSBsaW5lc1xuICAgICAgICAgIC5maWx0ZXIoKGxpbmUpID0+IGxpbmUuaW5jbHVkZXMoJ2ltcG9ydCgnKSlcbiAgICAgICAgICAubWFwKChsaW5lKSA9PiBsaW5lLnJlcGxhY2UoLy4qaW1wb3J0XFwoLywgJycpKVxuICAgICAgICAgIC5tYXAoKGxpbmUpID0+IGxpbmUuc3BsaXQoLycvKVsxXSlcbiAgICAgICAgICAubWFwKChsaW5lKSA9PiAobGluZS5pbmNsdWRlcygnPycpID8gbGluZS5zdWJzdHJpbmcoMCwgbGluZS5sYXN0SW5kZXhPZignPycpKSA6IGxpbmUpKTtcblxuICAgICAgICBzdGF0aWNJbXBvcnRzLmZvckVhY2goKHN0YXRpY0ltcG9ydCkgPT4gcmVzdWx0LmFkZChzdGF0aWNJbXBvcnQpKTtcblxuICAgICAgICBkeW5hbWljSW1wb3J0cy5tYXAoKGR5bmFtaWNJbXBvcnQpID0+IHtcbiAgICAgICAgICBjb25zdCBpbXBvcnRlZEZpbGUgPSBwYXRoLnJlc29sdmUocGF0aC5kaXJuYW1lKGZpbGVuYW1lKSwgZHluYW1pY0ltcG9ydCk7XG4gICAgICAgICAgcGFyc2VJbXBvcnRzKGltcG9ydGVkRmlsZSwgcmVzdWx0KTtcbiAgICAgICAgfSk7XG4gICAgICB9O1xuXG4gICAgICBjb25zdCBnZW5lcmF0ZWRJbXBvcnRzU2V0ID0gbmV3IFNldDxzdHJpbmc+KCk7XG4gICAgICBwYXJzZUltcG9ydHMoXG4gICAgICAgIHBhdGgucmVzb2x2ZSh0aGVtZU9wdGlvbnMuZnJvbnRlbmRHZW5lcmF0ZWRGb2xkZXIsICdmbG93JywgJ2dlbmVyYXRlZC1mbG93LWltcG9ydHMuanMnKSxcbiAgICAgICAgZ2VuZXJhdGVkSW1wb3J0c1NldFxuICAgICAgKTtcbiAgICAgIGNvbnN0IGdlbmVyYXRlZEltcG9ydHMgPSBBcnJheS5mcm9tKGdlbmVyYXRlZEltcG9ydHNTZXQpLnNvcnQoKTtcblxuICAgICAgY29uc3QgZnJvbnRlbmRGaWxlczogUmVjb3JkPHN0cmluZywgc3RyaW5nPiA9IHt9O1xuICAgICAgZnJvbnRlbmRGaWxlc1snaW5kZXguaHRtbCddID0gY3JlYXRlSGFzaCgnc2hhMjU2JykudXBkYXRlKGN1c3RvbUluZGV4RGF0YS5yZXBsYWNlKC9cXHJcXG4vZywgJ1xcbicpLCAndXRmOCcpLmRpZ2VzdCgnaGV4Jyk7XG5cbiAgICAgIGNvbnN0IHByb2plY3RGaWxlRXh0ZW5zaW9ucyA9IFsnLmpzJywgJy5qcy5tYXAnLCAnLnRzJywgJy50cy5tYXAnLCAnLnRzeCcsICcudHN4Lm1hcCcsICcuY3NzJywgJy5jc3MubWFwJ107XG5cbiAgICAgIGNvbnN0IGlzVGhlbWVDb21wb25lbnRzUmVzb3VyY2UgPSAoaWQ6IHN0cmluZykgPT5cbiAgICAgICAgICBpZC5zdGFydHNXaXRoKHRoZW1lT3B0aW9ucy5mcm9udGVuZEdlbmVyYXRlZEZvbGRlci5yZXBsYWNlKC9cXFxcL2csICcvJykpXG4gICAgICAgICAgICAgICYmIGlkLm1hdGNoKC8uKlxcL2phci1yZXNvdXJjZXNcXC90aGVtZXNcXC9bXlxcL10rXFwvY29tcG9uZW50c1xcLy8pO1xuXG4gICAgICBjb25zdCBpc0dlbmVyYXRlZFdlYkNvbXBvbmVudFJlc291cmNlID0gKGlkOiBzdHJpbmcpID0+XG4gICAgICAgICAgaWQuc3RhcnRzV2l0aCh0aGVtZU9wdGlvbnMuZnJvbnRlbmRHZW5lcmF0ZWRGb2xkZXIucmVwbGFjZSgvXFxcXC9nLCAnLycpKVxuICAgICAgICAgICAgICAmJiBpZC5tYXRjaCgvLipcXC9mbG93XFwvd2ViLWNvbXBvbmVudHNcXC8vKTtcblxuICAgICAgY29uc3QgaXNGcm9udGVuZFJlc291cmNlQ29sbGVjdGVkID0gKGlkOiBzdHJpbmcpID0+XG4gICAgICAgICAgIWlkLnN0YXJ0c1dpdGgodGhlbWVPcHRpb25zLmZyb250ZW5kR2VuZXJhdGVkRm9sZGVyLnJlcGxhY2UoL1xcXFwvZywgJy8nKSlcbiAgICAgICAgICB8fCBpc1RoZW1lQ29tcG9uZW50c1Jlc291cmNlKGlkKVxuICAgICAgICAgIHx8IGlzR2VuZXJhdGVkV2ViQ29tcG9uZW50UmVzb3VyY2UoaWQpO1xuXG4gICAgICAvLyBjb2xsZWN0cyBwcm9qZWN0J3MgZnJvbnRlbmQgcmVzb3VyY2VzIGluIGZyb250ZW5kIGZvbGRlciwgZXhjbHVkaW5nXG4gICAgICAvLyAnZ2VuZXJhdGVkJyBzdWItZm9sZGVyLCBleGNlcHQgZm9yIGxlZ2FjeSBzaGFkb3cgRE9NIHN0eWxlc2hlZXRzXG4gICAgICAvLyBwYWNrYWdlZCBpbiBgdGhlbWUvY29tcG9uZW50cy9gIGZvbGRlclxuICAgICAgLy8gYW5kIGdlbmVyYXRlZCB3ZWIgY29tcG9uZW50IHJlc291cmNlcyBpbiBgZmxvdy93ZWItY29tcG9uZW50c2AgZm9sZGVyLlxuICAgICAgbW9kdWxlc1xuICAgICAgICAubWFwKChpZCkgPT4gaWQucmVwbGFjZSgvXFxcXC9nLCAnLycpKVxuICAgICAgICAuZmlsdGVyKChpZCkgPT4gaWQuc3RhcnRzV2l0aChmcm9udGVuZEZvbGRlci5yZXBsYWNlKC9cXFxcL2csICcvJykpKVxuICAgICAgICAuZmlsdGVyKGlzRnJvbnRlbmRSZXNvdXJjZUNvbGxlY3RlZClcbiAgICAgICAgLm1hcCgoaWQpID0+IGlkLnN1YnN0cmluZyhmcm9udGVuZEZvbGRlci5sZW5ndGggKyAxKSlcbiAgICAgICAgLm1hcCgobGluZTogc3RyaW5nKSA9PiAobGluZS5pbmNsdWRlcygnPycpID8gbGluZS5zdWJzdHJpbmcoMCwgbGluZS5sYXN0SW5kZXhPZignPycpKSA6IGxpbmUpKVxuICAgICAgICAuZm9yRWFjaCgobGluZTogc3RyaW5nKSA9PiB7XG4gICAgICAgICAgLy8gXFxyXFxuIGZyb20gd2luZG93cyBtYWRlIGZpbGVzIG1heSBiZSB1c2VkIHNvIGNoYW5nZSB0byBcXG5cbiAgICAgICAgICBjb25zdCBmaWxlUGF0aCA9IHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgbGluZSk7XG4gICAgICAgICAgaWYgKHByb2plY3RGaWxlRXh0ZW5zaW9ucy5pbmNsdWRlcyhwYXRoLmV4dG5hbWUoZmlsZVBhdGgpKSkge1xuICAgICAgICAgICAgY29uc3QgZmlsZUJ1ZmZlciA9IHJlYWRGaWxlU3luYyhmaWxlUGF0aCwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KS5yZXBsYWNlKC9cXHJcXG4vZywgJ1xcbicpO1xuICAgICAgICAgICAgZnJvbnRlbmRGaWxlc1tsaW5lXSA9IGNyZWF0ZUhhc2goJ3NoYTI1NicpLnVwZGF0ZShmaWxlQnVmZmVyLCAndXRmOCcpLmRpZ2VzdCgnaGV4Jyk7XG4gICAgICAgICAgfVxuICAgICAgICB9KTtcblxuICAgICAgLy8gY29sbGVjdHMgZnJvbnRlbmQgcmVzb3VyY2VzIGZyb20gdGhlIEpBUnNcbiAgICAgIGdlbmVyYXRlZEltcG9ydHNcbiAgICAgICAgLmZpbHRlcigobGluZTogc3RyaW5nKSA9PiBsaW5lLmluY2x1ZGVzKCdnZW5lcmF0ZWQvamFyLXJlc291cmNlcycpKVxuICAgICAgICAuZm9yRWFjaCgobGluZTogc3RyaW5nKSA9PiB7XG4gICAgICAgICAgbGV0IGZpbGVuYW1lID0gbGluZS5zdWJzdHJpbmcobGluZS5pbmRleE9mKCdnZW5lcmF0ZWQnKSk7XG4gICAgICAgICAgLy8gXFxyXFxuIGZyb20gd2luZG93cyBtYWRlIGZpbGVzIG1heSBiZSB1c2VkIHJvIHJlbW92ZSB0byBiZSBvbmx5IFxcblxuICAgICAgICAgIGNvbnN0IGZpbGVCdWZmZXIgPSByZWFkRmlsZVN5bmMocGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCBmaWxlbmFtZSksIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkucmVwbGFjZShcbiAgICAgICAgICAgIC9cXHJcXG4vZyxcbiAgICAgICAgICAgICdcXG4nXG4gICAgICAgICAgKTtcbiAgICAgICAgICBjb25zdCBoYXNoID0gY3JlYXRlSGFzaCgnc2hhMjU2JykudXBkYXRlKGZpbGVCdWZmZXIsICd1dGY4JykuZGlnZXN0KCdoZXgnKTtcblxuICAgICAgICAgIGNvbnN0IGZpbGVLZXkgPSBsaW5lLnN1YnN0cmluZyhsaW5lLmluZGV4T2YoJ2phci1yZXNvdXJjZXMvJykgKyAxNCk7XG4gICAgICAgICAgZnJvbnRlbmRGaWxlc1tmaWxlS2V5XSA9IGhhc2g7XG4gICAgICAgIH0pO1xuICAgICAgLy8gY29sbGVjdHMgYW5kIGhhc2ggcmVzdCBvZiB0aGUgRnJvbnRlbmQgcmVzb3VyY2VzIGV4Y2x1ZGluZyBmaWxlcyBpbiAvZ2VuZXJhdGVkLyBhbmQgL3RoZW1lcy9cbiAgICAgIC8vIGFuZCBmaWxlcyBhbHJlYWR5IGluIGZyb250ZW5kRmlsZXMuXG4gICAgICBsZXQgZnJvbnRlbmRGb2xkZXJBbGlhcyA9IFwiRnJvbnRlbmRcIjtcbiAgICAgIGdlbmVyYXRlZEltcG9ydHNcbiAgICAgICAgLmZpbHRlcigobGluZTogc3RyaW5nKSA9PiBsaW5lLnN0YXJ0c1dpdGgoZnJvbnRlbmRGb2xkZXJBbGlhcyArICcvJykpXG4gICAgICAgIC5maWx0ZXIoKGxpbmU6IHN0cmluZykgPT4gIWxpbmUuc3RhcnRzV2l0aChmcm9udGVuZEZvbGRlckFsaWFzICsgJy9nZW5lcmF0ZWQvJykpXG4gICAgICAgIC5maWx0ZXIoKGxpbmU6IHN0cmluZykgPT4gIWxpbmUuc3RhcnRzV2l0aChmcm9udGVuZEZvbGRlckFsaWFzICsgJy90aGVtZXMvJykpXG4gICAgICAgIC5tYXAoKGxpbmUpID0+IGxpbmUuc3Vic3RyaW5nKGZyb250ZW5kRm9sZGVyQWxpYXMubGVuZ3RoICsgMSkpXG4gICAgICAgIC5maWx0ZXIoKGxpbmU6IHN0cmluZykgPT4gIWZyb250ZW5kRmlsZXNbbGluZV0pXG4gICAgICAgIC5mb3JFYWNoKChsaW5lOiBzdHJpbmcpID0+IHtcbiAgICAgICAgICBjb25zdCBmaWxlUGF0aCA9IHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgbGluZSk7XG4gICAgICAgICAgaWYgKHByb2plY3RGaWxlRXh0ZW5zaW9ucy5pbmNsdWRlcyhwYXRoLmV4dG5hbWUoZmlsZVBhdGgpKSAmJiBleGlzdHNTeW5jKGZpbGVQYXRoKSkge1xuICAgICAgICAgICAgY29uc3QgZmlsZUJ1ZmZlciA9IHJlYWRGaWxlU3luYyhmaWxlUGF0aCwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KS5yZXBsYWNlKC9cXHJcXG4vZywgJ1xcbicpO1xuICAgICAgICAgICAgZnJvbnRlbmRGaWxlc1tsaW5lXSA9IGNyZWF0ZUhhc2goJ3NoYTI1NicpLnVwZGF0ZShmaWxlQnVmZmVyLCAndXRmOCcpLmRpZ2VzdCgnaGV4Jyk7XG4gICAgICAgICAgfVxuICAgICAgICB9KTtcbiAgICAgIC8vIElmIGEgaW5kZXgudHMgZXhpc3RzIGhhc2ggaXQgdG8gYmUgYWJsZSB0byBzZWUgaWYgaXQgY2hhbmdlcy5cbiAgICAgIGlmIChleGlzdHNTeW5jKHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgJ2luZGV4LnRzJykpKSB7XG4gICAgICAgIGNvbnN0IGZpbGVCdWZmZXIgPSByZWFkRmlsZVN5bmMocGF0aC5yZXNvbHZlKGZyb250ZW5kRm9sZGVyLCAnaW5kZXgudHMnKSwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KS5yZXBsYWNlKFxuICAgICAgICAgIC9cXHJcXG4vZyxcbiAgICAgICAgICAnXFxuJ1xuICAgICAgICApO1xuICAgICAgICBmcm9udGVuZEZpbGVzW2BpbmRleC50c2BdID0gY3JlYXRlSGFzaCgnc2hhMjU2JykudXBkYXRlKGZpbGVCdWZmZXIsICd1dGY4JykuZGlnZXN0KCdoZXgnKTtcbiAgICAgIH1cblxuICAgICAgY29uc3QgdGhlbWVKc29uQ29udGVudHM6IFJlY29yZDxzdHJpbmcsIHN0cmluZz4gPSB7fTtcbiAgICAgIGNvbnN0IHRoZW1lc0ZvbGRlciA9IHBhdGgucmVzb2x2ZShqYXJSZXNvdXJjZXNGb2xkZXIsICd0aGVtZXMnKTtcbiAgICAgIGlmIChleGlzdHNTeW5jKHRoZW1lc0ZvbGRlcikpIHtcbiAgICAgICAgcmVhZGRpclN5bmModGhlbWVzRm9sZGVyKS5mb3JFYWNoKCh0aGVtZUZvbGRlcikgPT4ge1xuICAgICAgICAgIGNvbnN0IHRoZW1lSnNvbiA9IHBhdGgucmVzb2x2ZSh0aGVtZXNGb2xkZXIsIHRoZW1lRm9sZGVyLCAndGhlbWUuanNvbicpO1xuICAgICAgICAgIGlmIChleGlzdHNTeW5jKHRoZW1lSnNvbikpIHtcbiAgICAgICAgICAgIHRoZW1lSnNvbkNvbnRlbnRzW3BhdGguYmFzZW5hbWUodGhlbWVGb2xkZXIpXSA9IHJlYWRGaWxlU3luYyh0aGVtZUpzb24sIHsgZW5jb2Rpbmc6ICd1dGYtOCcgfSkucmVwbGFjZShcbiAgICAgICAgICAgICAgL1xcclxcbi9nLFxuICAgICAgICAgICAgICAnXFxuJ1xuICAgICAgICAgICAgKTtcbiAgICAgICAgICB9XG4gICAgICAgIH0pO1xuICAgICAgfVxuXG4gICAgICBjb2xsZWN0VGhlbWVKc29uc0luRnJvbnRlbmQodGhlbWVKc29uQ29udGVudHMsIHNldHRpbmdzLnRoZW1lTmFtZSk7XG5cbiAgICAgIGxldCB3ZWJDb21wb25lbnRzOiBzdHJpbmdbXSA9IFtdO1xuICAgICAgaWYgKHdlYkNvbXBvbmVudFRhZ3MpIHtcbiAgICAgICAgd2ViQ29tcG9uZW50cyA9IHdlYkNvbXBvbmVudFRhZ3Muc3BsaXQoJzsnKTtcbiAgICAgIH1cblxuICAgICAgY29uc3Qgc3RhdHMgPSB7XG4gICAgICAgIHBhY2thZ2VKc29uRGVwZW5kZW5jaWVzOiBwcm9qZWN0UGFja2FnZUpzb24uZGVwZW5kZW5jaWVzLFxuICAgICAgICBucG1Nb2R1bGVzOiBucG1Nb2R1bGVBbmRWZXJzaW9uLFxuICAgICAgICBidW5kbGVJbXBvcnRzOiBnZW5lcmF0ZWRJbXBvcnRzLFxuICAgICAgICBmcm9udGVuZEhhc2hlczogZnJvbnRlbmRGaWxlcyxcbiAgICAgICAgdGhlbWVKc29uQ29udGVudHM6IHRoZW1lSnNvbkNvbnRlbnRzLFxuICAgICAgICBlbnRyeVNjcmlwdHMsXG4gICAgICAgIHdlYkNvbXBvbmVudHMsXG4gICAgICAgIGN2ZGxNb2R1bGVzOiBjdmRscyxcbiAgICAgICAgcGFja2FnZUpzb25IYXNoOiBwcm9qZWN0UGFja2FnZUpzb24/LnZhYWRpbj8uaGFzaCxcbiAgICAgICAgaW5kZXhIdG1sR2VuZXJhdGVkOiByb3dzR2VuZXJhdGVkXG4gICAgICB9O1xuICAgICAgd3JpdGVGaWxlU3luYyhzdGF0c0ZpbGUsIEpTT04uc3RyaW5naWZ5KHN0YXRzLCBudWxsLCAxKSk7XG4gICAgfVxuICB9O1xufVxuZnVuY3Rpb24gdmFhZGluQnVuZGxlc1BsdWdpbigpOiBQbHVnaW5PcHRpb24ge1xuICB0eXBlIEV4cG9ydEluZm8gPVxuICAgIHwgc3RyaW5nXG4gICAgfCB7XG4gICAgICAgIG5hbWVzcGFjZT86IHN0cmluZztcbiAgICAgICAgc291cmNlOiBzdHJpbmc7XG4gICAgICB9O1xuXG4gIHR5cGUgRXhwb3NlSW5mbyA9IHtcbiAgICBleHBvcnRzOiBFeHBvcnRJbmZvW107XG4gIH07XG5cbiAgdHlwZSBQYWNrYWdlSW5mbyA9IHtcbiAgICB2ZXJzaW9uOiBzdHJpbmc7XG4gICAgZXhwb3NlczogUmVjb3JkPHN0cmluZywgRXhwb3NlSW5mbz47XG4gIH07XG5cbiAgdHlwZSBCdW5kbGVKc29uID0ge1xuICAgIHBhY2thZ2VzOiBSZWNvcmQ8c3RyaW5nLCBQYWNrYWdlSW5mbz47XG4gIH07XG5cbiAgY29uc3QgZGlzYWJsZWRNZXNzYWdlID0gJ1ZhYWRpbiBjb21wb25lbnQgZGVwZW5kZW5jeSBidW5kbGVzIGFyZSBkaXNhYmxlZC4nO1xuXG4gIGNvbnN0IG1vZHVsZXNEaXJlY3RvcnkgPSBub2RlTW9kdWxlc0ZvbGRlci5yZXBsYWNlKC9cXFxcL2csICcvJyk7XG5cbiAgbGV0IHZhYWRpbkJ1bmRsZUpzb246IEJ1bmRsZUpzb247XG5cbiAgZnVuY3Rpb24gcGFyc2VNb2R1bGVJZChpZDogc3RyaW5nKTogeyBwYWNrYWdlTmFtZTogc3RyaW5nOyBtb2R1bGVQYXRoOiBzdHJpbmcgfSB7XG4gICAgY29uc3QgW3Njb3BlLCBzY29wZWRQYWNrYWdlTmFtZV0gPSBpZC5zcGxpdCgnLycsIDMpO1xuICAgIGNvbnN0IHBhY2thZ2VOYW1lID0gc2NvcGUuc3RhcnRzV2l0aCgnQCcpID8gYCR7c2NvcGV9LyR7c2NvcGVkUGFja2FnZU5hbWV9YCA6IHNjb3BlO1xuICAgIGNvbnN0IG1vZHVsZVBhdGggPSBgLiR7aWQuc3Vic3RyaW5nKHBhY2thZ2VOYW1lLmxlbmd0aCl9YDtcbiAgICByZXR1cm4ge1xuICAgICAgcGFja2FnZU5hbWUsXG4gICAgICBtb2R1bGVQYXRoXG4gICAgfTtcbiAgfVxuXG4gIGZ1bmN0aW9uIGdldEV4cG9ydHMoaWQ6IHN0cmluZyk6IHN0cmluZ1tdIHwgdW5kZWZpbmVkIHtcbiAgICBjb25zdCB7IHBhY2thZ2VOYW1lLCBtb2R1bGVQYXRoIH0gPSBwYXJzZU1vZHVsZUlkKGlkKTtcbiAgICBjb25zdCBwYWNrYWdlSW5mbyA9IHZhYWRpbkJ1bmRsZUpzb24ucGFja2FnZXNbcGFja2FnZU5hbWVdO1xuXG4gICAgaWYgKCFwYWNrYWdlSW5mbykgcmV0dXJuO1xuXG4gICAgY29uc3QgZXhwb3NlSW5mbzogRXhwb3NlSW5mbyA9IHBhY2thZ2VJbmZvLmV4cG9zZXNbbW9kdWxlUGF0aF07XG4gICAgaWYgKCFleHBvc2VJbmZvKSByZXR1cm47XG5cbiAgICBjb25zdCBleHBvcnRzU2V0ID0gbmV3IFNldDxzdHJpbmc+KCk7XG4gICAgZm9yIChjb25zdCBlIG9mIGV4cG9zZUluZm8uZXhwb3J0cykge1xuICAgICAgaWYgKHR5cGVvZiBlID09PSAnc3RyaW5nJykge1xuICAgICAgICBleHBvcnRzU2V0LmFkZChlKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIGNvbnN0IHsgbmFtZXNwYWNlLCBzb3VyY2UgfSA9IGU7XG4gICAgICAgIGlmIChuYW1lc3BhY2UpIHtcbiAgICAgICAgICBleHBvcnRzU2V0LmFkZChuYW1lc3BhY2UpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIGNvbnN0IHNvdXJjZUV4cG9ydHMgPSBnZXRFeHBvcnRzKHNvdXJjZSk7XG4gICAgICAgICAgaWYgKHNvdXJjZUV4cG9ydHMpIHtcbiAgICAgICAgICAgIHNvdXJjZUV4cG9ydHMuZm9yRWFjaCgoZSkgPT4gZXhwb3J0c1NldC5hZGQoZSkpO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cbiAgICByZXR1cm4gQXJyYXkuZnJvbShleHBvcnRzU2V0KTtcbiAgfVxuXG4gIGZ1bmN0aW9uIGdldEV4cG9ydEJpbmRpbmcoYmluZGluZzogc3RyaW5nKSB7XG4gICAgcmV0dXJuIGJpbmRpbmcgPT09ICdkZWZhdWx0JyA/ICdfZGVmYXVsdCBhcyBkZWZhdWx0JyA6IGJpbmRpbmc7XG4gIH1cblxuICBmdW5jdGlvbiBnZXRJbXBvcnRBc3NpZ21lbnQoYmluZGluZzogc3RyaW5nKSB7XG4gICAgcmV0dXJuIGJpbmRpbmcgPT09ICdkZWZhdWx0JyA/ICdkZWZhdWx0OiBfZGVmYXVsdCcgOiBiaW5kaW5nO1xuICB9XG5cbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOmJ1bmRsZXMnLFxuICAgIGVuZm9yY2U6ICdwcmUnLFxuICAgIGFwcGx5KGNvbmZpZywgeyBjb21tYW5kIH0pIHtcbiAgICAgIGlmIChjb21tYW5kICE9PSAnc2VydmUnKSByZXR1cm4gZmFsc2U7XG5cbiAgICAgIHRyeSB7XG4gICAgICAgIGNvbnN0IHZhYWRpbkJ1bmRsZUpzb25QYXRoID0gcmVxdWlyZS5yZXNvbHZlKCdAdmFhZGluL2J1bmRsZXMvdmFhZGluLWJ1bmRsZS5qc29uJyk7XG4gICAgICAgIHZhYWRpbkJ1bmRsZUpzb24gPSBKU09OLnBhcnNlKHJlYWRGaWxlU3luYyh2YWFkaW5CdW5kbGVKc29uUGF0aCwgeyBlbmNvZGluZzogJ3V0ZjgnIH0pKTtcbiAgICAgIH0gY2F0Y2ggKGU6IHVua25vd24pIHtcbiAgICAgICAgaWYgKHR5cGVvZiBlID09PSAnb2JqZWN0JyAmJiAoZSBhcyB7IGNvZGU6IHN0cmluZyB9KS5jb2RlID09PSAnTU9EVUxFX05PVF9GT1VORCcpIHtcbiAgICAgICAgICB2YWFkaW5CdW5kbGVKc29uID0geyBwYWNrYWdlczoge30gfTtcbiAgICAgICAgICBjb25zb2xlLmluZm8oYEB2YWFkaW4vYnVuZGxlcyBucG0gcGFja2FnZSBpcyBub3QgZm91bmQsICR7ZGlzYWJsZWRNZXNzYWdlfWApO1xuICAgICAgICAgIHJldHVybiBmYWxzZTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICB0aHJvdyBlO1xuICAgICAgICB9XG4gICAgICB9XG5cbiAgICAgIGNvbnN0IHZlcnNpb25NaXNtYXRjaGVzOiBBcnJheTx7IG5hbWU6IHN0cmluZzsgYnVuZGxlZFZlcnNpb246IHN0cmluZzsgaW5zdGFsbGVkVmVyc2lvbjogc3RyaW5nIH0+ID0gW107XG4gICAgICBmb3IgKGNvbnN0IFtuYW1lLCBwYWNrYWdlSW5mb10gb2YgT2JqZWN0LmVudHJpZXModmFhZGluQnVuZGxlSnNvbi5wYWNrYWdlcykpIHtcbiAgICAgICAgbGV0IGluc3RhbGxlZFZlcnNpb246IHN0cmluZyB8IHVuZGVmaW5lZCA9IHVuZGVmaW5lZDtcbiAgICAgICAgdHJ5IHtcbiAgICAgICAgICBjb25zdCB7IHZlcnNpb246IGJ1bmRsZWRWZXJzaW9uIH0gPSBwYWNrYWdlSW5mbztcbiAgICAgICAgICBjb25zdCBpbnN0YWxsZWRQYWNrYWdlSnNvbkZpbGUgPSBwYXRoLnJlc29sdmUobW9kdWxlc0RpcmVjdG9yeSwgbmFtZSwgJ3BhY2thZ2UuanNvbicpO1xuICAgICAgICAgIGNvbnN0IHBhY2thZ2VKc29uID0gSlNPTi5wYXJzZShyZWFkRmlsZVN5bmMoaW5zdGFsbGVkUGFja2FnZUpzb25GaWxlLCB7IGVuY29kaW5nOiAndXRmOCcgfSkpO1xuICAgICAgICAgIGluc3RhbGxlZFZlcnNpb24gPSBwYWNrYWdlSnNvbi52ZXJzaW9uO1xuICAgICAgICAgIGlmIChpbnN0YWxsZWRWZXJzaW9uICYmIGluc3RhbGxlZFZlcnNpb24gIT09IGJ1bmRsZWRWZXJzaW9uKSB7XG4gICAgICAgICAgICB2ZXJzaW9uTWlzbWF0Y2hlcy5wdXNoKHtcbiAgICAgICAgICAgICAgbmFtZSxcbiAgICAgICAgICAgICAgYnVuZGxlZFZlcnNpb24sXG4gICAgICAgICAgICAgIGluc3RhbGxlZFZlcnNpb25cbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgIH1cbiAgICAgICAgfSBjYXRjaCAoXykge1xuICAgICAgICAgIC8vIGlnbm9yZSBwYWNrYWdlIG5vdCBmb3VuZFxuICAgICAgICB9XG4gICAgICB9XG4gICAgICBpZiAodmVyc2lvbk1pc21hdGNoZXMubGVuZ3RoKSB7XG4gICAgICAgIGNvbnNvbGUuaW5mbyhgQHZhYWRpbi9idW5kbGVzIGhhcyB2ZXJzaW9uIG1pc21hdGNoZXMgd2l0aCBpbnN0YWxsZWQgcGFja2FnZXMsICR7ZGlzYWJsZWRNZXNzYWdlfWApO1xuICAgICAgICBjb25zb2xlLmluZm8oYFBhY2thZ2VzIHdpdGggdmVyc2lvbiBtaXNtYXRjaGVzOiAke0pTT04uc3RyaW5naWZ5KHZlcnNpb25NaXNtYXRjaGVzLCB1bmRlZmluZWQsIDIpfWApO1xuICAgICAgICB2YWFkaW5CdW5kbGVKc29uID0geyBwYWNrYWdlczoge30gfTtcbiAgICAgICAgcmV0dXJuIGZhbHNlO1xuICAgICAgfVxuXG4gICAgICByZXR1cm4gdHJ1ZTtcbiAgICB9LFxuICAgIGFzeW5jIGNvbmZpZyhjb25maWcpIHtcbiAgICAgIHJldHVybiBtZXJnZUNvbmZpZyhcbiAgICAgICAge1xuICAgICAgICAgIG9wdGltaXplRGVwczoge1xuICAgICAgICAgICAgZXhjbHVkZTogW1xuICAgICAgICAgICAgICAvLyBWYWFkaW4gYnVuZGxlXG4gICAgICAgICAgICAgICdAdmFhZGluL2J1bmRsZXMnLFxuICAgICAgICAgICAgICAuLi5PYmplY3Qua2V5cyh2YWFkaW5CdW5kbGVKc29uLnBhY2thZ2VzKSxcbiAgICAgICAgICAgICAgJ0B2YWFkaW4vdmFhZGluLW1hdGVyaWFsLXN0eWxlcydcbiAgICAgICAgICAgIF1cbiAgICAgICAgICB9XG4gICAgICAgIH0sXG4gICAgICAgIGNvbmZpZ1xuICAgICAgKTtcbiAgICB9LFxuICAgIGxvYWQocmF3SWQpIHtcbiAgICAgIGNvbnN0IFtwYXRoLCBwYXJhbXNdID0gcmF3SWQuc3BsaXQoJz8nKTtcbiAgICAgIGlmICghcGF0aC5zdGFydHNXaXRoKG1vZHVsZXNEaXJlY3RvcnkpKSByZXR1cm47XG5cbiAgICAgIGNvbnN0IGlkID0gcGF0aC5zdWJzdHJpbmcobW9kdWxlc0RpcmVjdG9yeS5sZW5ndGggKyAxKTtcbiAgICAgIGNvbnN0IGJpbmRpbmdzID0gZ2V0RXhwb3J0cyhpZCk7XG4gICAgICBpZiAoYmluZGluZ3MgPT09IHVuZGVmaW5lZCkgcmV0dXJuO1xuXG4gICAgICBjb25zdCBjYWNoZVN1ZmZpeCA9IHBhcmFtcyA/IGA/JHtwYXJhbXN9YCA6ICcnO1xuICAgICAgY29uc3QgYnVuZGxlUGF0aCA9IGBAdmFhZGluL2J1bmRsZXMvdmFhZGluLmpzJHtjYWNoZVN1ZmZpeH1gO1xuXG4gICAgICByZXR1cm4gYGltcG9ydCB7IGluaXQgYXMgVmFhZGluQnVuZGxlSW5pdCwgZ2V0IGFzIFZhYWRpbkJ1bmRsZUdldCB9IGZyb20gJyR7YnVuZGxlUGF0aH0nO1xuYXdhaXQgVmFhZGluQnVuZGxlSW5pdCgnZGVmYXVsdCcpO1xuY29uc3QgeyAke2JpbmRpbmdzLm1hcChnZXRJbXBvcnRBc3NpZ21lbnQpLmpvaW4oJywgJyl9IH0gPSAoYXdhaXQgVmFhZGluQnVuZGxlR2V0KCcuL25vZGVfbW9kdWxlcy8ke2lkfScpKSgpO1xuZXhwb3J0IHsgJHtiaW5kaW5ncy5tYXAoZ2V0RXhwb3J0QmluZGluZykuam9pbignLCAnKX0gfTtgO1xuICAgIH1cbiAgfTtcbn1cblxuZnVuY3Rpb24gdGhlbWVQbHVnaW4ob3B0czogeyBkZXZNb2RlOiBib29sZWFuIH0pOiBQbHVnaW5PcHRpb24ge1xuICBjb25zdCBmdWxsVGhlbWVPcHRpb25zID0geyAuLi50aGVtZU9wdGlvbnMsIGRldk1vZGU6IG9wdHMuZGV2TW9kZSB9O1xuICByZXR1cm4ge1xuICAgIG5hbWU6ICd2YWFkaW46dGhlbWUnLFxuICAgIGNvbmZpZygpIHtcbiAgICAgIHByb2Nlc3NUaGVtZVJlc291cmNlcyhmdWxsVGhlbWVPcHRpb25zLCBjb25zb2xlKTtcbiAgICB9LFxuICAgIGNvbmZpZ3VyZVNlcnZlcihzZXJ2ZXIpIHtcbiAgICAgIGZ1bmN0aW9uIGhhbmRsZVRoZW1lRmlsZUNyZWF0ZURlbGV0ZSh0aGVtZUZpbGU6IHN0cmluZywgc3RhdHM/OiBTdGF0cykge1xuICAgICAgICBpZiAodGhlbWVGaWxlLnN0YXJ0c1dpdGgodGhlbWVGb2xkZXIpKSB7XG4gICAgICAgICAgY29uc3QgY2hhbmdlZCA9IHBhdGgucmVsYXRpdmUodGhlbWVGb2xkZXIsIHRoZW1lRmlsZSk7XG4gICAgICAgICAgY29uc29sZS5kZWJ1ZygnVGhlbWUgZmlsZSAnICsgKCEhc3RhdHMgPyAnY3JlYXRlZCcgOiAnZGVsZXRlZCcpLCBjaGFuZ2VkKTtcbiAgICAgICAgICBwcm9jZXNzVGhlbWVSZXNvdXJjZXMoZnVsbFRoZW1lT3B0aW9ucywgY29uc29sZSk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICAgIHNlcnZlci53YXRjaGVyLm9uKCdhZGQnLCBoYW5kbGVUaGVtZUZpbGVDcmVhdGVEZWxldGUpO1xuICAgICAgc2VydmVyLndhdGNoZXIub24oJ3VubGluaycsIGhhbmRsZVRoZW1lRmlsZUNyZWF0ZURlbGV0ZSk7XG4gICAgfSxcbiAgICBoYW5kbGVIb3RVcGRhdGUoY29udGV4dCkge1xuICAgICAgY29uc3QgY29udGV4dFBhdGggPSBwYXRoLnJlc29sdmUoY29udGV4dC5maWxlKTtcbiAgICAgIGNvbnN0IHRoZW1lUGF0aCA9IHBhdGgucmVzb2x2ZSh0aGVtZUZvbGRlcik7XG4gICAgICBpZiAoY29udGV4dFBhdGguc3RhcnRzV2l0aCh0aGVtZVBhdGgpKSB7XG4gICAgICAgIGNvbnN0IGNoYW5nZWQgPSBwYXRoLnJlbGF0aXZlKHRoZW1lUGF0aCwgY29udGV4dFBhdGgpO1xuXG4gICAgICAgIGNvbnNvbGUuZGVidWcoJ1RoZW1lIGZpbGUgY2hhbmdlZCcsIGNoYW5nZWQpO1xuXG4gICAgICAgIGlmIChjaGFuZ2VkLnN0YXJ0c1dpdGgoc2V0dGluZ3MudGhlbWVOYW1lKSkge1xuICAgICAgICAgIHByb2Nlc3NUaGVtZVJlc291cmNlcyhmdWxsVGhlbWVPcHRpb25zLCBjb25zb2xlKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH0sXG4gICAgYXN5bmMgcmVzb2x2ZUlkKGlkLCBpbXBvcnRlcikge1xuICAgICAgLy8gZm9yY2UgdGhlbWUgZ2VuZXJhdGlvbiBpZiBnZW5lcmF0ZWQgdGhlbWUgc291cmNlcyBkb2VzIG5vdCB5ZXQgZXhpc3RcbiAgICAgIC8vIHRoaXMgbWF5IGhhcHBlbiBmb3IgZXhhbXBsZSBkdXJpbmcgSmF2YSBob3QgcmVsb2FkIHdoZW4gdXBkYXRpbmdcbiAgICAgIC8vIEBUaGVtZSBhbm5vdGF0aW9uIHZhbHVlXG4gICAgICBpZiAoXG4gICAgICAgIHBhdGgucmVzb2x2ZSh0aGVtZU9wdGlvbnMuZnJvbnRlbmRHZW5lcmF0ZWRGb2xkZXIsICd0aGVtZS5qcycpID09PSBpbXBvcnRlciAmJlxuICAgICAgICAhZXhpc3RzU3luYyhwYXRoLnJlc29sdmUodGhlbWVPcHRpb25zLmZyb250ZW5kR2VuZXJhdGVkRm9sZGVyLCBpZCkpXG4gICAgICApIHtcbiAgICAgICAgY29uc29sZS5kZWJ1ZygnR2VuZXJhdGUgdGhlbWUgZmlsZSAnICsgaWQgKyAnIG5vdCBleGlzdGluZy4gUHJvY2Vzc2luZyB0aGVtZSByZXNvdXJjZScpO1xuICAgICAgICBwcm9jZXNzVGhlbWVSZXNvdXJjZXMoZnVsbFRoZW1lT3B0aW9ucywgY29uc29sZSk7XG4gICAgICAgIHJldHVybjtcbiAgICAgIH1cbiAgICAgIGlmICghaWQuc3RhcnRzV2l0aChzZXR0aW5ncy50aGVtZUZvbGRlcikpIHtcbiAgICAgICAgcmV0dXJuO1xuICAgICAgfVxuICAgICAgZm9yIChjb25zdCBsb2NhdGlvbiBvZiBbdGhlbWVSZXNvdXJjZUZvbGRlciwgZnJvbnRlbmRGb2xkZXJdKSB7XG4gICAgICAgIGNvbnN0IHJlc3VsdCA9IGF3YWl0IHRoaXMucmVzb2x2ZShwYXRoLnJlc29sdmUobG9jYXRpb24sIGlkKSk7XG4gICAgICAgIGlmIChyZXN1bHQpIHtcbiAgICAgICAgICByZXR1cm4gcmVzdWx0O1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfSxcbiAgICBhc3luYyB0cmFuc2Zvcm0ocmF3LCBpZCwgb3B0aW9ucykge1xuICAgICAgLy8gcmV3cml0ZSB1cmxzIGZvciB0aGUgYXBwbGljYXRpb24gdGhlbWUgY3NzIGZpbGVzXG4gICAgICBjb25zdCBbYmFyZUlkLCBxdWVyeV0gPSBpZC5zcGxpdCgnPycpO1xuICAgICAgaWYgKFxuICAgICAgICAoIWJhcmVJZD8uc3RhcnRzV2l0aCh0aGVtZUZvbGRlcikgJiYgIWJhcmVJZD8uc3RhcnRzV2l0aCh0aGVtZU9wdGlvbnMudGhlbWVSZXNvdXJjZUZvbGRlcikpIHx8XG4gICAgICAgICFiYXJlSWQ/LmVuZHNXaXRoKCcuY3NzJylcbiAgICAgICkge1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICBjb25zdCByZXNvdXJjZVRoZW1lRm9sZGVyID0gYmFyZUlkLnN0YXJ0c1dpdGgodGhlbWVGb2xkZXIpID8gdGhlbWVGb2xkZXIgOiB0aGVtZU9wdGlvbnMudGhlbWVSZXNvdXJjZUZvbGRlcjtcbiAgICAgIGNvbnN0IFt0aGVtZU5hbWVdID0gIGJhcmVJZC5zdWJzdHJpbmcocmVzb3VyY2VUaGVtZUZvbGRlci5sZW5ndGggKyAxKS5zcGxpdCgnLycpO1xuICAgICAgcmV0dXJuIHJld3JpdGVDc3NVcmxzKHJhdywgcGF0aC5kaXJuYW1lKGJhcmVJZCksIHBhdGgucmVzb2x2ZShyZXNvdXJjZVRoZW1lRm9sZGVyLCB0aGVtZU5hbWUpLCBjb25zb2xlLCBvcHRzKTtcbiAgICB9XG4gIH07XG59XG5cbmZ1bmN0aW9uIHJ1bldhdGNoRG9nKHdhdGNoRG9nUG9ydDogbnVtYmVyLCB3YXRjaERvZ0hvc3Q6IHN0cmluZyB8IHVuZGVmaW5lZCkge1xuICBjb25zdCBjbGllbnQgPSBuZXcgbmV0LlNvY2tldCgpO1xuICBjbGllbnQuc2V0RW5jb2RpbmcoJ3V0ZjgnKTtcbiAgY2xpZW50Lm9uKCdlcnJvcicsIGZ1bmN0aW9uIChlcnIpIHtcbiAgICBjb25zb2xlLmxvZygnV2F0Y2hkb2cgY29ubmVjdGlvbiBlcnJvci4gVGVybWluYXRpbmcgdml0ZSBwcm9jZXNzLi4uJywgZXJyKTtcbiAgICBjbGllbnQuZGVzdHJveSgpO1xuICAgIHByb2Nlc3MuZXhpdCgwKTtcbiAgfSk7XG4gIGNsaWVudC5vbignY2xvc2UnLCBmdW5jdGlvbiAoKSB7XG4gICAgY2xpZW50LmRlc3Ryb3koKTtcbiAgICBydW5XYXRjaERvZyh3YXRjaERvZ1BvcnQsIHdhdGNoRG9nSG9zdCk7XG4gIH0pO1xuXG4gIGNsaWVudC5jb25uZWN0KHdhdGNoRG9nUG9ydCwgd2F0Y2hEb2dIb3N0IHx8ICdsb2NhbGhvc3QnKTtcbn1cblxuY29uc3QgYWxsb3dlZEZyb250ZW5kRm9sZGVycyA9IFtmcm9udGVuZEZvbGRlciwgbm9kZU1vZHVsZXNGb2xkZXJdO1xuXG5mdW5jdGlvbiBzaG93UmVjb21waWxlUmVhc29uKCk6IFBsdWdpbk9wdGlvbiB7XG4gIHJldHVybiB7XG4gICAgbmFtZTogJ3ZhYWRpbjp3aHkteW91LWNvbXBpbGUnLFxuICAgIGhhbmRsZUhvdFVwZGF0ZShjb250ZXh0KSB7XG4gICAgICBjb25zb2xlLmxvZygnUmVjb21waWxpbmcgYmVjYXVzZScsIGNvbnRleHQuZmlsZSwgJ2NoYW5nZWQnKTtcbiAgICB9XG4gIH07XG59XG5cbmNvbnN0IERFVl9NT0RFX1NUQVJUX1JFR0VYUCA9IC9cXC9cXCpbXFwqIV1cXHMrdmFhZGluLWRldi1tb2RlOnN0YXJ0LztcbmNvbnN0IERFVl9NT0RFX0NPREVfUkVHRVhQID0gL1xcL1xcKltcXCohXVxccyt2YWFkaW4tZGV2LW1vZGU6c3RhcnQoW1xcc1xcU10qKXZhYWRpbi1kZXYtbW9kZTplbmRcXHMrXFwqXFwqXFwvL2k7XG5cbmZ1bmN0aW9uIHByZXNlcnZlVXNhZ2VTdGF0cygpIHtcbiAgcmV0dXJuIHtcbiAgICBuYW1lOiAndmFhZGluOnByZXNlcnZlLXVzYWdlLXN0YXRzJyxcblxuICAgIHRyYW5zZm9ybShzcmM6IHN0cmluZywgaWQ6IHN0cmluZykge1xuICAgICAgaWYgKGlkLmluY2x1ZGVzKCd2YWFkaW4tdXNhZ2Utc3RhdGlzdGljcycpKSB7XG4gICAgICAgIGlmIChzcmMuaW5jbHVkZXMoJ3ZhYWRpbi1kZXYtbW9kZTpzdGFydCcpKSB7XG4gICAgICAgICAgY29uc3QgbmV3U3JjID0gc3JjLnJlcGxhY2UoREVWX01PREVfU1RBUlRfUkVHRVhQLCAnLyohIHZhYWRpbi1kZXYtbW9kZTpzdGFydCcpO1xuICAgICAgICAgIGlmIChuZXdTcmMgPT09IHNyYykge1xuICAgICAgICAgICAgY29uc29sZS5lcnJvcignQ29tbWVudCByZXBsYWNlbWVudCBmYWlsZWQgdG8gY2hhbmdlIGFueXRoaW5nJyk7XG4gICAgICAgICAgfSBlbHNlIGlmICghbmV3U3JjLm1hdGNoKERFVl9NT0RFX0NPREVfUkVHRVhQKSkge1xuICAgICAgICAgICAgY29uc29sZS5lcnJvcignTmV3IGNvbW1lbnQgZmFpbHMgdG8gbWF0Y2ggb3JpZ2luYWwgcmVnZXhwJyk7XG4gICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgIHJldHVybiB7IGNvZGU6IG5ld1NyYyB9O1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuXG4gICAgICByZXR1cm4geyBjb2RlOiBzcmMgfTtcbiAgICB9XG4gIH07XG59XG5cbmV4cG9ydCBjb25zdCB2YWFkaW5Db25maWc6IFVzZXJDb25maWdGbiA9IChlbnYpID0+IHtcbiAgY29uc3QgZGV2TW9kZSA9IGVudi5tb2RlID09PSAnZGV2ZWxvcG1lbnQnO1xuICBjb25zdCBwcm9kdWN0aW9uTW9kZSA9ICFkZXZNb2RlICYmICFkZXZCdW5kbGVcblxuICBpZiAoZGV2TW9kZSAmJiBwcm9jZXNzLmVudi53YXRjaERvZ1BvcnQpIHtcbiAgICAvLyBPcGVuIGEgY29ubmVjdGlvbiB3aXRoIHRoZSBKYXZhIGRldi1tb2RlIGhhbmRsZXIgaW4gb3JkZXIgdG8gZmluaXNoXG4gICAgLy8gdml0ZSB3aGVuIGl0IGV4aXRzIG9yIGNyYXNoZXMuXG4gICAgcnVuV2F0Y2hEb2cocGFyc2VJbnQocHJvY2Vzcy5lbnYud2F0Y2hEb2dQb3J0KSwgcHJvY2Vzcy5lbnYud2F0Y2hEb2dIb3N0KTtcbiAgfVxuXG4gIHJldHVybiB7XG4gICAgcm9vdDogZnJvbnRlbmRGb2xkZXIsXG4gICAgYmFzZTogJycsXG4gICAgcHVibGljRGlyOiBmYWxzZSxcbiAgICByZXNvbHZlOiB7XG4gICAgICBhbGlhczoge1xuICAgICAgICAnQHZhYWRpbi9mbG93LWZyb250ZW5kJzogamFyUmVzb3VyY2VzRm9sZGVyLFxuICAgICAgICBGcm9udGVuZDogZnJvbnRlbmRGb2xkZXJcbiAgICAgIH0sXG4gICAgICBwcmVzZXJ2ZVN5bWxpbmtzOiB0cnVlXG4gICAgfSxcbiAgICBkZWZpbmU6IHtcbiAgICAgIE9GRkxJTkVfUEFUSDogc2V0dGluZ3Mub2ZmbGluZVBhdGgsXG4gICAgICBWSVRFX0VOQUJMRUQ6ICd0cnVlJ1xuICAgIH0sXG4gICAgc2VydmVyOiB7XG4gICAgICBob3N0OiAnMTI3LjAuMC4xJyxcbiAgICAgIHN0cmljdFBvcnQ6IHRydWUsXG4gICAgICBmczoge1xuICAgICAgICBhbGxvdzogYWxsb3dlZEZyb250ZW5kRm9sZGVyc1xuICAgICAgfVxuICAgIH0sXG4gICAgYnVpbGQ6IHtcbiAgICAgIG1pbmlmeTogcHJvZHVjdGlvbk1vZGUsXG4gICAgICBvdXREaXI6IGJ1aWxkT3V0cHV0Rm9sZGVyLFxuICAgICAgZW1wdHlPdXREaXI6IGRldkJ1bmRsZSxcbiAgICAgIGFzc2V0c0RpcjogJ1ZBQURJTi9idWlsZCcsXG4gICAgICB0YXJnZXQ6IFtcImVzbmV4dFwiLCBcInNhZmFyaTE1XCJdLFxuICAgICAgcm9sbHVwT3B0aW9uczoge1xuICAgICAgICBpbnB1dDoge1xuICAgICAgICAgIGluZGV4aHRtbDogcHJvamVjdEluZGV4SHRtbCxcblxuICAgICAgICAgIC4uLihoYXNFeHBvcnRlZFdlYkNvbXBvbmVudHMgPyB7IHdlYmNvbXBvbmVudGh0bWw6IHBhdGgucmVzb2x2ZShmcm9udGVuZEZvbGRlciwgJ3dlYi1jb21wb25lbnQuaHRtbCcpIH0gOiB7fSlcbiAgICAgICAgfSxcbiAgICAgICAgb253YXJuOiAod2FybmluZzogcm9sbHVwLlJvbGx1cExvZywgZGVmYXVsdEhhbmRsZXI6IHJvbGx1cC5Mb2dnaW5nRnVuY3Rpb24pID0+IHtcbiAgICAgICAgICBjb25zdCBpZ25vcmVFdmFsV2FybmluZyA9IFtcbiAgICAgICAgICAgICdnZW5lcmF0ZWQvamFyLXJlc291cmNlcy9GbG93Q2xpZW50LmpzJyxcbiAgICAgICAgICAgICdnZW5lcmF0ZWQvamFyLXJlc291cmNlcy92YWFkaW4tc3ByZWFkc2hlZXQvc3ByZWFkc2hlZXQtZXhwb3J0LmpzJyxcbiAgICAgICAgICAgICdAdmFhZGluL2NoYXJ0cy9zcmMvaGVscGVycy5qcydcbiAgICAgICAgICBdO1xuICAgICAgICAgIGlmICh3YXJuaW5nLmNvZGUgPT09ICdFVkFMJyAmJiB3YXJuaW5nLmlkICYmICEhaWdub3JlRXZhbFdhcm5pbmcuZmluZCgoaWQpID0+IHdhcm5pbmcuaWQ/LmVuZHNXaXRoKGlkKSkpIHtcbiAgICAgICAgICAgIHJldHVybjtcbiAgICAgICAgICB9XG4gICAgICAgICAgZGVmYXVsdEhhbmRsZXIod2FybmluZyk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9LFxuICAgIG9wdGltaXplRGVwczoge1xuICAgICAgZW50cmllczogW1xuICAgICAgICAvLyBQcmUtc2NhbiBlbnRyeXBvaW50cyBpbiBWaXRlIHRvIGF2b2lkIHJlbG9hZGluZyBvbiBmaXJzdCBvcGVuXG4gICAgICAgICdnZW5lcmF0ZWQvdmFhZGluLnRzJ1xuICAgICAgXSxcbiAgICAgIGV4Y2x1ZGU6IFtcbiAgICAgICAgJ0B2YWFkaW4vcm91dGVyJyxcbiAgICAgICAgJ0B2YWFkaW4vdmFhZGluLWxpY2Vuc2UtY2hlY2tlcicsXG4gICAgICAgICdAdmFhZGluL3ZhYWRpbi11c2FnZS1zdGF0aXN0aWNzJyxcbiAgICAgICAgJ3dvcmtib3gtY29yZScsXG4gICAgICAgICd3b3JrYm94LXByZWNhY2hpbmcnLFxuICAgICAgICAnd29ya2JveC1yb3V0aW5nJyxcbiAgICAgICAgJ3dvcmtib3gtc3RyYXRlZ2llcydcbiAgICAgIF1cbiAgICB9LFxuICAgIHBsdWdpbnM6IFtcbiAgICAgIHByb2R1Y3Rpb25Nb2RlICYmIGJyb3RsaSgpLFxuICAgICAgZGV2TW9kZSAmJiB2YWFkaW5CdW5kbGVzUGx1Z2luKCksXG4gICAgICBkZXZNb2RlICYmIHNob3dSZWNvbXBpbGVSZWFzb24oKSxcbiAgICAgIHNldHRpbmdzLm9mZmxpbmVFbmFibGVkICYmIGJ1aWxkU1dQbHVnaW4oeyBkZXZNb2RlIH0pLFxuICAgICAgIWRldk1vZGUgJiYgc3RhdHNFeHRyYWN0ZXJQbHVnaW4oKSxcbiAgICAgICFwcm9kdWN0aW9uTW9kZSAmJiBwcmVzZXJ2ZVVzYWdlU3RhdHMoKSxcbiAgICAgIHRoZW1lUGx1Z2luKHsgZGV2TW9kZSB9KSxcbiAgICAgIHBvc3Rjc3NMaXQoe1xuICAgICAgICBpbmNsdWRlOiBbJyoqLyouY3NzJywgLy4qXFwvLipcXC5jc3NcXD8uKi9dLFxuICAgICAgICBleGNsdWRlOiBbXG4gICAgICAgICAgYCR7dGhlbWVGb2xkZXJ9LyoqLyouY3NzYCxcbiAgICAgICAgICBuZXcgUmVnRXhwKGAke3RoZW1lRm9sZGVyfS8uKi8uKlxcXFwuY3NzXFxcXD8uKmApLFxuICAgICAgICAgIGAke3RoZW1lUmVzb3VyY2VGb2xkZXJ9LyoqLyouY3NzYCxcbiAgICAgICAgICBuZXcgUmVnRXhwKGAke3RoZW1lUmVzb3VyY2VGb2xkZXJ9Ly4qLy4qXFxcXC5jc3NcXFxcPy4qYCksXG4gICAgICAgICAgbmV3IFJlZ0V4cCgnLiovLipcXFxcP2h0bWwtcHJveHkuKicpXG4gICAgICAgIF1cbiAgICAgIH0pLFxuICAgICAgLy8gVGhlIFJlYWN0IHBsdWdpbiBwcm92aWRlcyBmYXN0IHJlZnJlc2ggYW5kIGRlYnVnIHNvdXJjZSBpbmZvXG4gICAgICByZWFjdFBsdWdpbih7XG4gICAgICAgIGluY2x1ZGU6ICcqKi8qLnRzeCcsXG4gICAgICAgIGJhYmVsOiB7XG4gICAgICAgICAgLy8gV2UgbmVlZCB0byB1c2UgYmFiZWwgdG8gcHJvdmlkZSB0aGUgc291cmNlIGluZm9ybWF0aW9uIGZvciBpdCB0byBiZSBjb3JyZWN0XG4gICAgICAgICAgLy8gKG90aGVyd2lzZSBCYWJlbCB3aWxsIHNsaWdodGx5IHJld3JpdGUgdGhlIHNvdXJjZSBmaWxlIGFuZCBlc2J1aWxkIGdlbmVyYXRlIHNvdXJjZSBpbmZvIGZvciB0aGUgbW9kaWZpZWQgZmlsZSlcbiAgICAgICAgICBwcmVzZXRzOiBbWydAYmFiZWwvcHJlc2V0LXJlYWN0JywgeyBydW50aW1lOiAnYXV0b21hdGljJywgZGV2ZWxvcG1lbnQ6ICFwcm9kdWN0aW9uTW9kZSB9XV0sXG4gICAgICAgICAgLy8gUmVhY3Qgd3JpdGVzIHRoZSBzb3VyY2UgbG9jYXRpb24gZm9yIHdoZXJlIGNvbXBvbmVudHMgYXJlIHVzZWQsIHRoaXMgd3JpdGVzIGZvciB3aGVyZSB0aGV5IGFyZSBkZWZpbmVkXG4gICAgICAgICAgcGx1Z2luczogW1xuICAgICAgICAgICAgIXByb2R1Y3Rpb25Nb2RlICYmIGFkZEZ1bmN0aW9uQ29tcG9uZW50U291cmNlTG9jYXRpb25CYWJlbCgpXG4gICAgICAgICAgXS5maWx0ZXIoQm9vbGVhbilcbiAgICAgICAgfVxuICAgICAgfSksXG4gICAgICB7XG4gICAgICAgIG5hbWU6ICd2YWFkaW46Zm9yY2UtcmVtb3ZlLWh0bWwtbWlkZGxld2FyZScsXG4gICAgICAgIGNvbmZpZ3VyZVNlcnZlcihzZXJ2ZXIpIHtcbiAgICAgICAgICByZXR1cm4gKCkgPT4ge1xuICAgICAgICAgICAgc2VydmVyLm1pZGRsZXdhcmVzLnN0YWNrID0gc2VydmVyLm1pZGRsZXdhcmVzLnN0YWNrLmZpbHRlcigobXcpID0+IHtcbiAgICAgICAgICAgICAgY29uc3QgaGFuZGxlTmFtZSA9IGAke213LmhhbmRsZX1gO1xuICAgICAgICAgICAgICByZXR1cm4gIWhhbmRsZU5hbWUuaW5jbHVkZXMoJ3ZpdGVIdG1sRmFsbGJhY2tNaWRkbGV3YXJlJyk7XG4gICAgICAgICAgICB9KTtcbiAgICAgICAgICB9O1xuICAgICAgICB9LFxuICAgICAgfSxcbiAgICAgIGhhc0V4cG9ydGVkV2ViQ29tcG9uZW50cyAmJiB7XG4gICAgICAgIG5hbWU6ICd2YWFkaW46aW5qZWN0LWVudHJ5cG9pbnRzLXRvLXdlYi1jb21wb25lbnQtaHRtbCcsXG4gICAgICAgIHRyYW5zZm9ybUluZGV4SHRtbDoge1xuICAgICAgICAgIG9yZGVyOiAncHJlJyxcbiAgICAgICAgICBoYW5kbGVyKF9odG1sLCB7IHBhdGgsIHNlcnZlciB9KSB7XG4gICAgICAgICAgICBpZiAocGF0aCAhPT0gJy93ZWItY29tcG9uZW50Lmh0bWwnKSB7XG4gICAgICAgICAgICAgIHJldHVybjtcbiAgICAgICAgICAgIH1cblxuICAgICAgICAgICAgcmV0dXJuIFtcbiAgICAgICAgICAgICAge1xuICAgICAgICAgICAgICAgIHRhZzogJ3NjcmlwdCcsXG4gICAgICAgICAgICAgICAgYXR0cnM6IHsgdHlwZTogJ21vZHVsZScsIHNyYzogYC9nZW5lcmF0ZWQvdmFhZGluLXdlYi1jb21wb25lbnQudHNgIH0sXG4gICAgICAgICAgICAgICAgaW5qZWN0VG86ICdoZWFkJ1xuICAgICAgICAgICAgICB9XG4gICAgICAgICAgICBdO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfSxcbiAgICAgIHtcbiAgICAgICAgbmFtZTogJ3ZhYWRpbjppbmplY3QtZW50cnlwb2ludHMtdG8taW5kZXgtaHRtbCcsXG4gICAgICAgIHRyYW5zZm9ybUluZGV4SHRtbDoge1xuICAgICAgICAgIG9yZGVyOiAncHJlJyxcbiAgICAgICAgICBoYW5kbGVyKF9odG1sLCB7IHBhdGgsIHNlcnZlciB9KSB7XG4gICAgICAgICAgICBpZiAocGF0aCAhPT0gJy9pbmRleC5odG1sJykge1xuICAgICAgICAgICAgICByZXR1cm47XG4gICAgICAgICAgICB9XG5cbiAgICAgICAgICAgIGNvbnN0IHNjcmlwdHMgPSBbXTtcblxuICAgICAgICAgICAgaWYgKGRldk1vZGUpIHtcbiAgICAgICAgICAgICAgc2NyaXB0cy5wdXNoKHtcbiAgICAgICAgICAgICAgICB0YWc6ICdzY3JpcHQnLFxuICAgICAgICAgICAgICAgIGF0dHJzOiB7IHR5cGU6ICdtb2R1bGUnLCBzcmM6IGAvZ2VuZXJhdGVkL3ZpdGUtZGV2bW9kZS50c2AsIG9uZXJyb3I6IFwiZG9jdW1lbnQubG9jYXRpb24ucmVsb2FkKClcIiB9LFxuICAgICAgICAgICAgICAgIGluamVjdFRvOiAnaGVhZCdcbiAgICAgICAgICAgICAgfSk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgICBzY3JpcHRzLnB1c2goe1xuICAgICAgICAgICAgICB0YWc6ICdzY3JpcHQnLFxuICAgICAgICAgICAgICBhdHRyczogeyB0eXBlOiAnbW9kdWxlJywgc3JjOiAnL2dlbmVyYXRlZC92YWFkaW4udHMnIH0sXG4gICAgICAgICAgICAgIGluamVjdFRvOiAnaGVhZCdcbiAgICAgICAgICAgIH0pO1xuICAgICAgICAgICAgcmV0dXJuIHNjcmlwdHM7XG4gICAgICAgICAgfVxuICAgICAgICB9XG4gICAgICB9LFxuICAgICAgY2hlY2tlcih7XG4gICAgICAgIHR5cGVzY3JpcHQ6IHRydWVcbiAgICAgIH0pLFxuICAgICAgcHJvZHVjdGlvbk1vZGUgJiYgdmlzdWFsaXplcih7IGJyb3RsaVNpemU6IHRydWUsIGZpbGVuYW1lOiBidW5kbGVTaXplRmlsZSB9KVxuICAgICAgXG4gICAgXVxuICB9O1xufTtcblxuZXhwb3J0IGNvbnN0IG92ZXJyaWRlVmFhZGluQ29uZmlnID0gKGN1c3RvbUNvbmZpZzogVXNlckNvbmZpZ0ZuKSA9PiB7XG4gIHJldHVybiBkZWZpbmVDb25maWcoKGVudikgPT4gbWVyZ2VDb25maWcodmFhZGluQ29uZmlnKGVudiksIGN1c3RvbUNvbmZpZyhlbnYpKSk7XG59O1xuZnVuY3Rpb24gZ2V0VmVyc2lvbihtb2R1bGU6IHN0cmluZyk6IHN0cmluZyB7XG4gIGNvbnN0IHBhY2thZ2VKc29uID0gcGF0aC5yZXNvbHZlKG5vZGVNb2R1bGVzRm9sZGVyLCBtb2R1bGUsICdwYWNrYWdlLmpzb24nKTtcbiAgcmV0dXJuIEpTT04ucGFyc2UocmVhZEZpbGVTeW5jKHBhY2thZ2VKc29uLCB7IGVuY29kaW5nOiAndXRmLTgnIH0pKS52ZXJzaW9uO1xufVxuZnVuY3Rpb24gZ2V0Q3ZkbE5hbWUobW9kdWxlOiBzdHJpbmcpOiBzdHJpbmcge1xuICBjb25zdCBwYWNrYWdlSnNvbiA9IHBhdGgucmVzb2x2ZShub2RlTW9kdWxlc0ZvbGRlciwgbW9kdWxlLCAncGFja2FnZS5qc29uJyk7XG4gIHJldHVybiBKU09OLnBhcnNlKHJlYWRGaWxlU3luYyhwYWNrYWdlSnNvbiwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KSkuY3ZkbE5hbWU7XG59XG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXHV0ZW50ZVxcXFxJZGVhUHJvamVjdHNcXFxcZHVlZW1tZXN0b3JlXFxcXGJ1aWxkXFxcXHBsdWdpbnNcXFxcYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFx1dGVudGVcXFxcSWRlYVByb2plY3RzXFxcXGR1ZWVtbWVzdG9yZVxcXFxidWlsZFxcXFxwbHVnaW5zXFxcXGFwcGxpY2F0aW9uLXRoZW1lLXBsdWdpblxcXFx0aGVtZS1oYW5kbGUuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL3V0ZW50ZS9JZGVhUHJvamVjdHMvZHVlZW1tZXN0b3JlL2J1aWxkL3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWhhbmRsZS5qc1wiOy8qXG4gKiBDb3B5cmlnaHQgMjAwMC0yMDI0IFZhYWRpbiBMdGQuXG4gKlxuICogTGljZW5zZWQgdW5kZXIgdGhlIEFwYWNoZSBMaWNlbnNlLCBWZXJzaW9uIDIuMCAodGhlIFwiTGljZW5zZVwiKTsgeW91IG1heSBub3RcbiAqIHVzZSB0aGlzIGZpbGUgZXhjZXB0IGluIGNvbXBsaWFuY2Ugd2l0aCB0aGUgTGljZW5zZS4gWW91IG1heSBvYnRhaW4gYSBjb3B5IG9mXG4gKiB0aGUgTGljZW5zZSBhdFxuICpcbiAqIGh0dHA6Ly93d3cuYXBhY2hlLm9yZy9saWNlbnNlcy9MSUNFTlNFLTIuMFxuICpcbiAqIFVubGVzcyByZXF1aXJlZCBieSBhcHBsaWNhYmxlIGxhdyBvciBhZ3JlZWQgdG8gaW4gd3JpdGluZywgc29mdHdhcmVcbiAqIGRpc3RyaWJ1dGVkIHVuZGVyIHRoZSBMaWNlbnNlIGlzIGRpc3RyaWJ1dGVkIG9uIGFuIFwiQVMgSVNcIiBCQVNJUywgV0lUSE9VVFxuICogV0FSUkFOVElFUyBPUiBDT05ESVRJT05TIE9GIEFOWSBLSU5ELCBlaXRoZXIgZXhwcmVzcyBvciBpbXBsaWVkLiBTZWUgdGhlXG4gKiBMaWNlbnNlIGZvciB0aGUgc3BlY2lmaWMgbGFuZ3VhZ2UgZ292ZXJuaW5nIHBlcm1pc3Npb25zIGFuZCBsaW1pdGF0aW9ucyB1bmRlclxuICogdGhlIExpY2Vuc2UuXG4gKi9cblxuLyoqXG4gKiBUaGlzIGZpbGUgY29udGFpbnMgZnVuY3Rpb25zIGZvciBsb29rIHVwIGFuZCBoYW5kbGUgdGhlIHRoZW1lIHJlc291cmNlc1xuICogZm9yIGFwcGxpY2F0aW9uIHRoZW1lIHBsdWdpbi5cbiAqL1xuaW1wb3J0IHsgZXhpc3RzU3luYywgcmVhZEZpbGVTeW5jIH0gZnJvbSAnZnMnO1xuaW1wb3J0IHsgcmVzb2x2ZSB9IGZyb20gJ3BhdGgnO1xuaW1wb3J0IHsgd3JpdGVUaGVtZUZpbGVzIH0gZnJvbSAnLi90aGVtZS1nZW5lcmF0b3IuanMnO1xuaW1wb3J0IHsgY29weVN0YXRpY0Fzc2V0cywgY29weVRoZW1lUmVzb3VyY2VzIH0gZnJvbSAnLi90aGVtZS1jb3B5LmpzJztcblxuLy8gbWF0Y2hlcyB0aGVtZSBuYW1lIGluICcuL3RoZW1lLW15LXRoZW1lLmdlbmVyYXRlZC5qcydcbmNvbnN0IG5hbWVSZWdleCA9IC90aGVtZS0oLiopXFwuZ2VuZXJhdGVkXFwuanMvO1xuXG5sZXQgcHJldlRoZW1lTmFtZSA9IHVuZGVmaW5lZDtcbmxldCBmaXJzdFRoZW1lTmFtZSA9IHVuZGVmaW5lZDtcblxuLyoqXG4gKiBMb29rcyB1cCBmb3IgYSB0aGVtZSByZXNvdXJjZXMgaW4gYSBjdXJyZW50IHByb2plY3QgYW5kIGluIGphciBkZXBlbmRlbmNpZXMsXG4gKiBjb3BpZXMgdGhlIGZvdW5kIHJlc291cmNlcyBhbmQgZ2VuZXJhdGVzL3VwZGF0ZXMgbWV0YSBkYXRhIGZvciB3ZWJwYWNrXG4gKiBjb21waWxhdGlvbi5cbiAqXG4gKiBAcGFyYW0ge29iamVjdH0gb3B0aW9ucyBhcHBsaWNhdGlvbiB0aGVtZSBwbHVnaW4gbWFuZGF0b3J5IG9wdGlvbnMsXG4gKiBAc2VlIHtAbGluayBBcHBsaWNhdGlvblRoZW1lUGx1Z2lufVxuICpcbiAqIEBwYXJhbSBsb2dnZXIgYXBwbGljYXRpb24gdGhlbWUgcGx1Z2luIGxvZ2dlclxuICovXG5mdW5jdGlvbiBwcm9jZXNzVGhlbWVSZXNvdXJjZXMob3B0aW9ucywgbG9nZ2VyKSB7XG4gIGNvbnN0IHRoZW1lTmFtZSA9IGV4dHJhY3RUaGVtZU5hbWUob3B0aW9ucy5mcm9udGVuZEdlbmVyYXRlZEZvbGRlcik7XG4gIGlmICh0aGVtZU5hbWUpIHtcbiAgICBpZiAoIXByZXZUaGVtZU5hbWUgJiYgIWZpcnN0VGhlbWVOYW1lKSB7XG4gICAgICBmaXJzdFRoZW1lTmFtZSA9IHRoZW1lTmFtZTtcbiAgICB9IGVsc2UgaWYgKFxuICAgICAgKHByZXZUaGVtZU5hbWUgJiYgcHJldlRoZW1lTmFtZSAhPT0gdGhlbWVOYW1lICYmIGZpcnN0VGhlbWVOYW1lICE9PSB0aGVtZU5hbWUpIHx8XG4gICAgICAoIXByZXZUaGVtZU5hbWUgJiYgZmlyc3RUaGVtZU5hbWUgIT09IHRoZW1lTmFtZSlcbiAgICApIHtcbiAgICAgIC8vIFdhcm5pbmcgbWVzc2FnZSBpcyBzaG93biB0byB0aGUgZGV2ZWxvcGVyIHdoZW46XG4gICAgICAvLyAxLiBIZSBpcyBzd2l0Y2hpbmcgdG8gYW55IHRoZW1lLCB3aGljaCBpcyBkaWZmZXIgZnJvbSBvbmUgYmVpbmcgc2V0IHVwXG4gICAgICAvLyBvbiBhcHBsaWNhdGlvbiBzdGFydHVwLCBieSBjaGFuZ2luZyB0aGVtZSBuYW1lIGluIGBAVGhlbWUoKWBcbiAgICAgIC8vIDIuIEhlIHJlbW92ZXMgb3IgY29tbWVudHMgb3V0IGBAVGhlbWUoKWAgdG8gc2VlIGhvdyB0aGUgYXBwXG4gICAgICAvLyBsb29rcyBsaWtlIHdpdGhvdXQgdGhlbWluZywgYW5kIHRoZW4gYWdhaW4gYnJpbmdzIGBAVGhlbWUoKWAgYmFja1xuICAgICAgLy8gd2l0aCBhIHRoZW1lTmFtZSB3aGljaCBpcyBkaWZmZXIgZnJvbSBvbmUgYmVpbmcgc2V0IHVwIG9uIGFwcGxpY2F0aW9uXG4gICAgICAvLyBzdGFydHVwLlxuICAgICAgY29uc3Qgd2FybmluZyA9IGBBdHRlbnRpb246IEFjdGl2ZSB0aGVtZSBpcyBzd2l0Y2hlZCB0byAnJHt0aGVtZU5hbWV9Jy5gO1xuICAgICAgY29uc3QgZGVzY3JpcHRpb24gPSBgXG4gICAgICBOb3RlIHRoYXQgYWRkaW5nIG5ldyBzdHlsZSBzaGVldCBmaWxlcyB0byAnL3RoZW1lcy8ke3RoZW1lTmFtZX0vY29tcG9uZW50cycsIFxuICAgICAgbWF5IG5vdCBiZSB0YWtlbiBpbnRvIGVmZmVjdCB1bnRpbCB0aGUgbmV4dCBhcHBsaWNhdGlvbiByZXN0YXJ0LlxuICAgICAgQ2hhbmdlcyB0byBhbHJlYWR5IGV4aXN0aW5nIHN0eWxlIHNoZWV0IGZpbGVzIGFyZSBiZWluZyByZWxvYWRlZCBhcyBiZWZvcmUuYDtcbiAgICAgIGxvZ2dlci53YXJuKCcqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqJyk7XG4gICAgICBsb2dnZXIud2Fybih3YXJuaW5nKTtcbiAgICAgIGxvZ2dlci53YXJuKGRlc2NyaXB0aW9uKTtcbiAgICAgIGxvZ2dlci53YXJuKCcqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqKioqJyk7XG4gICAgfVxuICAgIHByZXZUaGVtZU5hbWUgPSB0aGVtZU5hbWU7XG5cbiAgICBmaW5kVGhlbWVGb2xkZXJBbmRIYW5kbGVUaGVtZSh0aGVtZU5hbWUsIG9wdGlvbnMsIGxvZ2dlcik7XG4gIH0gZWxzZSB7XG4gICAgLy8gVGhpcyBpcyBuZWVkZWQgaW4gdGhlIHNpdHVhdGlvbiB0aGF0IHRoZSB1c2VyIGRlY2lkZXMgdG8gY29tbWVudCBvclxuICAgIC8vIHJlbW92ZSB0aGUgQFRoZW1lKC4uLikgY29tcGxldGVseSB0byBzZWUgaG93IHRoZSBhcHBsaWNhdGlvbiBsb29rc1xuICAgIC8vIHdpdGhvdXQgYW55IHRoZW1lLiBUaGVuIHdoZW4gdGhlIHVzZXIgYnJpbmdzIGJhY2sgb25lIG9mIHRoZSB0aGVtZXMsXG4gICAgLy8gdGhlIHByZXZpb3VzIHRoZW1lIHNob3VsZCBiZSB1bmRlZmluZWQgdG8gZW5hYmxlIHVzIHRvIGRldGVjdCB0aGUgY2hhbmdlLlxuICAgIHByZXZUaGVtZU5hbWUgPSB1bmRlZmluZWQ7XG4gICAgbG9nZ2VyLmRlYnVnKCdTa2lwcGluZyBWYWFkaW4gYXBwbGljYXRpb24gdGhlbWUgaGFuZGxpbmcuJyk7XG4gICAgbG9nZ2VyLnRyYWNlKCdNb3N0IGxpa2VseSBubyBAVGhlbWUgYW5ub3RhdGlvbiBmb3IgYXBwbGljYXRpb24gb3Igb25seSB0aGVtZUNsYXNzIHVzZWQuJyk7XG4gIH1cbn1cblxuLyoqXG4gKiBTZWFyY2ggZm9yIHRoZSBnaXZlbiB0aGVtZSBpbiB0aGUgcHJvamVjdCBhbmQgcmVzb3VyY2UgZm9sZGVycy5cbiAqXG4gKiBAcGFyYW0ge3N0cmluZ30gdGhlbWVOYW1lIG5hbWUgb2YgdGhlbWUgdG8gZmluZFxuICogQHBhcmFtIHtvYmplY3R9IG9wdGlvbnMgYXBwbGljYXRpb24gdGhlbWUgcGx1Z2luIG1hbmRhdG9yeSBvcHRpb25zLFxuICogQHNlZSB7QGxpbmsgQXBwbGljYXRpb25UaGVtZVBsdWdpbn1cbiAqIEBwYXJhbSBsb2dnZXIgYXBwbGljYXRpb24gdGhlbWUgcGx1Z2luIGxvZ2dlclxuICogQHJldHVybiB0cnVlIG9yIGZhbHNlIGZvciBpZiB0aGVtZSB3YXMgZm91bmRcbiAqL1xuZnVuY3Rpb24gZmluZFRoZW1lRm9sZGVyQW5kSGFuZGxlVGhlbWUodGhlbWVOYW1lLCBvcHRpb25zLCBsb2dnZXIpIHtcbiAgbGV0IHRoZW1lRm91bmQgPSBmYWxzZTtcbiAgZm9yIChsZXQgaSA9IDA7IGkgPCBvcHRpb25zLnRoZW1lUHJvamVjdEZvbGRlcnMubGVuZ3RoOyBpKyspIHtcbiAgICBjb25zdCB0aGVtZVByb2plY3RGb2xkZXIgPSBvcHRpb25zLnRoZW1lUHJvamVjdEZvbGRlcnNbaV07XG4gICAgaWYgKGV4aXN0c1N5bmModGhlbWVQcm9qZWN0Rm9sZGVyKSkge1xuICAgICAgbG9nZ2VyLmRlYnVnKFwiU2VhcmNoaW5nIHRoZW1lcyBmb2xkZXIgJ1wiICsgdGhlbWVQcm9qZWN0Rm9sZGVyICsgXCInIGZvciB0aGVtZSAnXCIgKyB0aGVtZU5hbWUgKyBcIidcIik7XG4gICAgICBjb25zdCBoYW5kbGVkID0gaGFuZGxlVGhlbWVzKHRoZW1lTmFtZSwgdGhlbWVQcm9qZWN0Rm9sZGVyLCBvcHRpb25zLCBsb2dnZXIpO1xuICAgICAgaWYgKGhhbmRsZWQpIHtcbiAgICAgICAgaWYgKHRoZW1lRm91bmQpIHtcbiAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXG4gICAgICAgICAgICBcIkZvdW5kIHRoZW1lIGZpbGVzIGluICdcIiArXG4gICAgICAgICAgICAgIHRoZW1lUHJvamVjdEZvbGRlciArXG4gICAgICAgICAgICAgIFwiJyBhbmQgJ1wiICtcbiAgICAgICAgICAgICAgdGhlbWVGb3VuZCArXG4gICAgICAgICAgICAgIFwiJy4gVGhlbWUgc2hvdWxkIG9ubHkgYmUgYXZhaWxhYmxlIGluIG9uZSBmb2xkZXJcIlxuICAgICAgICAgICk7XG4gICAgICAgIH1cbiAgICAgICAgbG9nZ2VyLmRlYnVnKFwiRm91bmQgdGhlbWUgZmlsZXMgZnJvbSAnXCIgKyB0aGVtZVByb2plY3RGb2xkZXIgKyBcIidcIik7XG4gICAgICAgIHRoZW1lRm91bmQgPSB0aGVtZVByb2plY3RGb2xkZXI7XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgaWYgKGV4aXN0c1N5bmMob3B0aW9ucy50aGVtZVJlc291cmNlRm9sZGVyKSkge1xuICAgIGlmICh0aGVtZUZvdW5kICYmIGV4aXN0c1N5bmMocmVzb2x2ZShvcHRpb25zLnRoZW1lUmVzb3VyY2VGb2xkZXIsIHRoZW1lTmFtZSkpKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoXG4gICAgICAgIFwiVGhlbWUgJ1wiICtcbiAgICAgICAgICB0aGVtZU5hbWUgK1xuICAgICAgICAgIFwiJ3Nob3VsZCBub3QgZXhpc3QgaW5zaWRlIGEgamFyIGFuZCBpbiB0aGUgcHJvamVjdCBhdCB0aGUgc2FtZSB0aW1lXFxuXCIgK1xuICAgICAgICAgICdFeHRlbmRpbmcgYW5vdGhlciB0aGVtZSBpcyBwb3NzaWJsZSBieSBhZGRpbmcgeyBcInBhcmVudFwiOiBcIm15LXBhcmVudC10aGVtZVwiIH0gZW50cnkgdG8gdGhlIHRoZW1lLmpzb24gZmlsZSBpbnNpZGUgeW91ciB0aGVtZSBmb2xkZXIuJ1xuICAgICAgKTtcbiAgICB9XG4gICAgbG9nZ2VyLmRlYnVnKFxuICAgICAgXCJTZWFyY2hpbmcgdGhlbWUgamFyIHJlc291cmNlIGZvbGRlciAnXCIgKyBvcHRpb25zLnRoZW1lUmVzb3VyY2VGb2xkZXIgKyBcIicgZm9yIHRoZW1lICdcIiArIHRoZW1lTmFtZSArIFwiJ1wiXG4gICAgKTtcbiAgICBoYW5kbGVUaGVtZXModGhlbWVOYW1lLCBvcHRpb25zLnRoZW1lUmVzb3VyY2VGb2xkZXIsIG9wdGlvbnMsIGxvZ2dlcik7XG4gICAgdGhlbWVGb3VuZCA9IHRydWU7XG4gIH1cbiAgcmV0dXJuIHRoZW1lRm91bmQ7XG59XG5cbi8qKlxuICogQ29waWVzIHN0YXRpYyByZXNvdXJjZXMgZm9yIHRoZW1lIGFuZCBnZW5lcmF0ZXMvd3JpdGVzIHRoZVxuICogW3RoZW1lLW5hbWVdLmdlbmVyYXRlZC5qcyBmb3Igd2VicGFjayB0byBoYW5kbGUuXG4gKlxuICogTm90ZSEgSWYgYSBwYXJlbnQgdGhlbWUgaXMgZGVmaW5lZCBpdCB3aWxsIGFsc28gYmUgaGFuZGxlZCBoZXJlIHNvIHRoYXQgdGhlIHBhcmVudCB0aGVtZSBnZW5lcmF0ZWQgZmlsZSBpc1xuICogZ2VuZXJhdGVkIGluIGFkdmFuY2Ugb2YgdGhlIHRoZW1lIGdlbmVyYXRlZCBmaWxlLlxuICpcbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZU5hbWUgbmFtZSBvZiB0aGVtZSB0byBoYW5kbGVcbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZXNGb2xkZXIgZm9sZGVyIGNvbnRhaW5pbmcgYXBwbGljYXRpb24gdGhlbWUgZm9sZGVyc1xuICogQHBhcmFtIHtvYmplY3R9IG9wdGlvbnMgYXBwbGljYXRpb24gdGhlbWUgcGx1Z2luIG1hbmRhdG9yeSBvcHRpb25zLFxuICogQHNlZSB7QGxpbmsgQXBwbGljYXRpb25UaGVtZVBsdWdpbn1cbiAqIEBwYXJhbSB7b2JqZWN0fSBsb2dnZXIgcGx1Z2luIGxvZ2dlciBpbnN0YW5jZVxuICpcbiAqIEB0aHJvd3MgRXJyb3IgaWYgcGFyZW50IHRoZW1lIGRlZmluZWQsIGJ1dCBjYW4ndCBsb2NhdGUgcGFyZW50IHRoZW1lXG4gKlxuICogQHJldHVybnMgdHJ1ZSBpZiB0aGVtZSB3YXMgZm91bmQgZWxzZSBmYWxzZS5cbiAqL1xuZnVuY3Rpb24gaGFuZGxlVGhlbWVzKHRoZW1lTmFtZSwgdGhlbWVzRm9sZGVyLCBvcHRpb25zLCBsb2dnZXIpIHtcbiAgY29uc3QgdGhlbWVGb2xkZXIgPSByZXNvbHZlKHRoZW1lc0ZvbGRlciwgdGhlbWVOYW1lKTtcbiAgaWYgKGV4aXN0c1N5bmModGhlbWVGb2xkZXIpKSB7XG4gICAgbG9nZ2VyLmRlYnVnKCdGb3VuZCB0aGVtZSAnLCB0aGVtZU5hbWUsICcgaW4gZm9sZGVyICcsIHRoZW1lRm9sZGVyKTtcblxuICAgIGNvbnN0IHRoZW1lUHJvcGVydGllcyA9IGdldFRoZW1lUHJvcGVydGllcyh0aGVtZUZvbGRlcik7XG5cbiAgICAvLyBJZiB0aGVtZSBoYXMgcGFyZW50IGhhbmRsZSBwYXJlbnQgdGhlbWUgaW1tZWRpYXRlbHkuXG4gICAgaWYgKHRoZW1lUHJvcGVydGllcy5wYXJlbnQpIHtcbiAgICAgIGNvbnN0IGZvdW5kID0gZmluZFRoZW1lRm9sZGVyQW5kSGFuZGxlVGhlbWUodGhlbWVQcm9wZXJ0aWVzLnBhcmVudCwgb3B0aW9ucywgbG9nZ2VyKTtcbiAgICAgIGlmICghZm91bmQpIHtcbiAgICAgICAgdGhyb3cgbmV3IEVycm9yKFxuICAgICAgICAgIFwiQ291bGQgbm90IGxvY2F0ZSBmaWxlcyBmb3IgZGVmaW5lZCBwYXJlbnQgdGhlbWUgJ1wiICtcbiAgICAgICAgICAgIHRoZW1lUHJvcGVydGllcy5wYXJlbnQgK1xuICAgICAgICAgICAgXCInLlxcblwiICtcbiAgICAgICAgICAgICdQbGVhc2UgdmVyaWZ5IHRoYXQgZGVwZW5kZW5jeSBpcyBhZGRlZCBvciB0aGVtZSBmb2xkZXIgZXhpc3RzLidcbiAgICAgICAgKTtcbiAgICAgIH1cbiAgICB9XG4gICAgY29weVN0YXRpY0Fzc2V0cyh0aGVtZU5hbWUsIHRoZW1lUHJvcGVydGllcywgb3B0aW9ucy5wcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCBsb2dnZXIpO1xuICAgIGNvcHlUaGVtZVJlc291cmNlcyh0aGVtZUZvbGRlciwgb3B0aW9ucy5wcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCBsb2dnZXIpO1xuXG4gICAgd3JpdGVUaGVtZUZpbGVzKHRoZW1lRm9sZGVyLCB0aGVtZU5hbWUsIHRoZW1lUHJvcGVydGllcywgb3B0aW9ucyk7XG4gICAgcmV0dXJuIHRydWU7XG4gIH1cbiAgcmV0dXJuIGZhbHNlO1xufVxuXG5mdW5jdGlvbiBnZXRUaGVtZVByb3BlcnRpZXModGhlbWVGb2xkZXIpIHtcbiAgY29uc3QgdGhlbWVQcm9wZXJ0eUZpbGUgPSByZXNvbHZlKHRoZW1lRm9sZGVyLCAndGhlbWUuanNvbicpO1xuICBpZiAoIWV4aXN0c1N5bmModGhlbWVQcm9wZXJ0eUZpbGUpKSB7XG4gICAgcmV0dXJuIHt9O1xuICB9XG4gIGNvbnN0IHRoZW1lUHJvcGVydHlGaWxlQXNTdHJpbmcgPSByZWFkRmlsZVN5bmModGhlbWVQcm9wZXJ0eUZpbGUpO1xuICBpZiAodGhlbWVQcm9wZXJ0eUZpbGVBc1N0cmluZy5sZW5ndGggPT09IDApIHtcbiAgICByZXR1cm4ge307XG4gIH1cbiAgcmV0dXJuIEpTT04ucGFyc2UodGhlbWVQcm9wZXJ0eUZpbGVBc1N0cmluZyk7XG59XG5cbi8qKlxuICogRXh0cmFjdHMgY3VycmVudCB0aGVtZSBuYW1lIGZyb20gYXV0by1nZW5lcmF0ZWQgJ3RoZW1lLmpzJyBmaWxlIGxvY2F0ZWQgb24gYVxuICogZ2l2ZW4gZm9sZGVyLlxuICogQHBhcmFtIGZyb250ZW5kR2VuZXJhdGVkRm9sZGVyIGZvbGRlciBpbiBwcm9qZWN0IGNvbnRhaW5pbmcgJ3RoZW1lLmpzJyBmaWxlXG4gKiBAcmV0dXJucyB7c3RyaW5nfSBjdXJyZW50IHRoZW1lIG5hbWVcbiAqL1xuZnVuY3Rpb24gZXh0cmFjdFRoZW1lTmFtZShmcm9udGVuZEdlbmVyYXRlZEZvbGRlcikge1xuICBpZiAoIWZyb250ZW5kR2VuZXJhdGVkRm9sZGVyKSB7XG4gICAgdGhyb3cgbmV3IEVycm9yKFxuICAgICAgXCJDb3VsZG4ndCBleHRyYWN0IHRoZW1lIG5hbWUgZnJvbSAndGhlbWUuanMnLFwiICtcbiAgICAgICAgJyBiZWNhdXNlIHRoZSBwYXRoIHRvIGZvbGRlciBjb250YWluaW5nIHRoaXMgZmlsZSBpcyBlbXB0eS4gUGxlYXNlIHNldCcgK1xuICAgICAgICAnIHRoZSBhIGNvcnJlY3QgZm9sZGVyIHBhdGggaW4gQXBwbGljYXRpb25UaGVtZVBsdWdpbiBjb25zdHJ1Y3RvcicgK1xuICAgICAgICAnIHBhcmFtZXRlcnMuJ1xuICAgICk7XG4gIH1cbiAgY29uc3QgZ2VuZXJhdGVkVGhlbWVGaWxlID0gcmVzb2x2ZShmcm9udGVuZEdlbmVyYXRlZEZvbGRlciwgJ3RoZW1lLmpzJyk7XG4gIGlmIChleGlzdHNTeW5jKGdlbmVyYXRlZFRoZW1lRmlsZSkpIHtcbiAgICAvLyByZWFkIHRoZW1lIG5hbWUgZnJvbSB0aGUgJ2dlbmVyYXRlZC90aGVtZS5qcycgYXMgdGhlcmUgd2UgYWx3YXlzXG4gICAgLy8gbWFyayB0aGUgdXNlZCB0aGVtZSBmb3Igd2VicGFjayB0byBoYW5kbGUuXG4gICAgY29uc3QgdGhlbWVOYW1lID0gbmFtZVJlZ2V4LmV4ZWMocmVhZEZpbGVTeW5jKGdlbmVyYXRlZFRoZW1lRmlsZSwgeyBlbmNvZGluZzogJ3V0ZjgnIH0pKVsxXTtcbiAgICBpZiAoIXRoZW1lTmFtZSkge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKFwiQ291bGRuJ3QgcGFyc2UgdGhlbWUgbmFtZSBmcm9tICdcIiArIGdlbmVyYXRlZFRoZW1lRmlsZSArIFwiJy5cIik7XG4gICAgfVxuICAgIHJldHVybiB0aGVtZU5hbWU7XG4gIH0gZWxzZSB7XG4gICAgcmV0dXJuICcnO1xuICB9XG59XG5cbi8qKlxuICogRmluZHMgYWxsIHRoZSBwYXJlbnQgdGhlbWVzIGxvY2F0ZWQgaW4gdGhlIHByb2plY3QgdGhlbWVzIGZvbGRlcnMgYW5kIGluXG4gKiB0aGUgSkFSIGRlcGVuZGVuY2llcyB3aXRoIHJlc3BlY3QgdG8gdGhlIGdpdmVuIGN1c3RvbSB0aGVtZSB3aXRoXG4gKiB7QGNvZGUgdGhlbWVOYW1lfS5cbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZU5hbWUgZ2l2ZW4gY3VzdG9tIHRoZW1lIG5hbWUgdG8gbG9vayBwYXJlbnRzIGZvclxuICogQHBhcmFtIHtvYmplY3R9IG9wdGlvbnMgYXBwbGljYXRpb24gdGhlbWUgcGx1Z2luIG1hbmRhdG9yeSBvcHRpb25zLFxuICogQHNlZSB7QGxpbmsgQXBwbGljYXRpb25UaGVtZVBsdWdpbn1cbiAqIEByZXR1cm5zIHtzdHJpbmdbXX0gYXJyYXkgb2YgcGF0aHMgdG8gZm91bmQgcGFyZW50IHRoZW1lcyB3aXRoIHJlc3BlY3QgdG8gdGhlXG4gKiBnaXZlbiBjdXN0b20gdGhlbWVcbiAqL1xuZnVuY3Rpb24gZmluZFBhcmVudFRoZW1lcyh0aGVtZU5hbWUsIG9wdGlvbnMpIHtcbiAgY29uc3QgZXhpc3RpbmdUaGVtZUZvbGRlcnMgPSBbb3B0aW9ucy50aGVtZVJlc291cmNlRm9sZGVyLCAuLi5vcHRpb25zLnRoZW1lUHJvamVjdEZvbGRlcnNdLmZpbHRlcigoZm9sZGVyKSA9PlxuICAgIGV4aXN0c1N5bmMoZm9sZGVyKVxuICApO1xuICByZXR1cm4gY29sbGVjdFBhcmVudFRoZW1lcyh0aGVtZU5hbWUsIGV4aXN0aW5nVGhlbWVGb2xkZXJzLCBmYWxzZSk7XG59XG5cbmZ1bmN0aW9uIGNvbGxlY3RQYXJlbnRUaGVtZXModGhlbWVOYW1lLCB0aGVtZUZvbGRlcnMsIGlzUGFyZW50KSB7XG4gIGxldCBmb3VuZFBhcmVudFRoZW1lcyA9IFtdO1xuICB0aGVtZUZvbGRlcnMuZm9yRWFjaCgoZm9sZGVyKSA9PiB7XG4gICAgY29uc3QgdGhlbWVGb2xkZXIgPSByZXNvbHZlKGZvbGRlciwgdGhlbWVOYW1lKTtcbiAgICBpZiAoZXhpc3RzU3luYyh0aGVtZUZvbGRlcikpIHtcbiAgICAgIGNvbnN0IHRoZW1lUHJvcGVydGllcyA9IGdldFRoZW1lUHJvcGVydGllcyh0aGVtZUZvbGRlcik7XG5cbiAgICAgIGlmICh0aGVtZVByb3BlcnRpZXMucGFyZW50KSB7XG4gICAgICAgIGZvdW5kUGFyZW50VGhlbWVzLnB1c2goLi4uY29sbGVjdFBhcmVudFRoZW1lcyh0aGVtZVByb3BlcnRpZXMucGFyZW50LCB0aGVtZUZvbGRlcnMsIHRydWUpKTtcbiAgICAgICAgaWYgKCFmb3VuZFBhcmVudFRoZW1lcy5sZW5ndGgpIHtcbiAgICAgICAgICB0aHJvdyBuZXcgRXJyb3IoXG4gICAgICAgICAgICBcIkNvdWxkIG5vdCBsb2NhdGUgZmlsZXMgZm9yIGRlZmluZWQgcGFyZW50IHRoZW1lICdcIiArXG4gICAgICAgICAgICAgIHRoZW1lUHJvcGVydGllcy5wYXJlbnQgK1xuICAgICAgICAgICAgICBcIicuXFxuXCIgK1xuICAgICAgICAgICAgICAnUGxlYXNlIHZlcmlmeSB0aGF0IGRlcGVuZGVuY3kgaXMgYWRkZWQgb3IgdGhlbWUgZm9sZGVyIGV4aXN0cy4nXG4gICAgICAgICAgKTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgICAgLy8gQWRkIGEgdGhlbWUgcGF0aCB0byByZXN1bHQgY29sbGVjdGlvbiBvbmx5IGlmIGEgZ2l2ZW4gdGhlbWVOYW1lXG4gICAgICAvLyBpcyBzdXBwb3NlZCB0byBiZSBhIHBhcmVudCB0aGVtZVxuICAgICAgaWYgKGlzUGFyZW50KSB7XG4gICAgICAgIGZvdW5kUGFyZW50VGhlbWVzLnB1c2godGhlbWVGb2xkZXIpO1xuICAgICAgfVxuICAgIH1cbiAgfSk7XG4gIHJldHVybiBmb3VuZFBhcmVudFRoZW1lcztcbn1cblxuZXhwb3J0IHsgcHJvY2Vzc1RoZW1lUmVzb3VyY2VzLCBleHRyYWN0VGhlbWVOYW1lLCBmaW5kUGFyZW50VGhlbWVzIH07XG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXHV0ZW50ZVxcXFxJZGVhUHJvamVjdHNcXFxcZHVlZW1tZXN0b3JlXFxcXGJ1aWxkXFxcXHBsdWdpbnNcXFxcYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFx1dGVudGVcXFxcSWRlYVByb2plY3RzXFxcXGR1ZWVtbWVzdG9yZVxcXFxidWlsZFxcXFxwbHVnaW5zXFxcXGFwcGxpY2F0aW9uLXRoZW1lLXBsdWdpblxcXFx0aGVtZS1nZW5lcmF0b3IuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL3V0ZW50ZS9JZGVhUHJvamVjdHMvZHVlZW1tZXN0b3JlL2J1aWxkL3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWdlbmVyYXRvci5qc1wiOy8qXG4gKiBDb3B5cmlnaHQgMjAwMC0yMDI0IFZhYWRpbiBMdGQuXG4gKlxuICogTGljZW5zZWQgdW5kZXIgdGhlIEFwYWNoZSBMaWNlbnNlLCBWZXJzaW9uIDIuMCAodGhlIFwiTGljZW5zZVwiKTsgeW91IG1heSBub3RcbiAqIHVzZSB0aGlzIGZpbGUgZXhjZXB0IGluIGNvbXBsaWFuY2Ugd2l0aCB0aGUgTGljZW5zZS4gWW91IG1heSBvYnRhaW4gYSBjb3B5IG9mXG4gKiB0aGUgTGljZW5zZSBhdFxuICpcbiAqIGh0dHA6Ly93d3cuYXBhY2hlLm9yZy9saWNlbnNlcy9MSUNFTlNFLTIuMFxuICpcbiAqIFVubGVzcyByZXF1aXJlZCBieSBhcHBsaWNhYmxlIGxhdyBvciBhZ3JlZWQgdG8gaW4gd3JpdGluZywgc29mdHdhcmVcbiAqIGRpc3RyaWJ1dGVkIHVuZGVyIHRoZSBMaWNlbnNlIGlzIGRpc3RyaWJ1dGVkIG9uIGFuIFwiQVMgSVNcIiBCQVNJUywgV0lUSE9VVFxuICogV0FSUkFOVElFUyBPUiBDT05ESVRJT05TIE9GIEFOWSBLSU5ELCBlaXRoZXIgZXhwcmVzcyBvciBpbXBsaWVkLiBTZWUgdGhlXG4gKiBMaWNlbnNlIGZvciB0aGUgc3BlY2lmaWMgbGFuZ3VhZ2UgZ292ZXJuaW5nIHBlcm1pc3Npb25zIGFuZCBsaW1pdGF0aW9ucyB1bmRlclxuICogdGhlIExpY2Vuc2UuXG4gKi9cblxuLyoqXG4gKiBUaGlzIGZpbGUgaGFuZGxlcyB0aGUgZ2VuZXJhdGlvbiBvZiB0aGUgJ1t0aGVtZS1uYW1lXS5qcycgdG9cbiAqIHRoZSB0aGVtZXMvW3RoZW1lLW5hbWVdIGZvbGRlciBhY2NvcmRpbmcgdG8gcHJvcGVydGllcyBmcm9tICd0aGVtZS5qc29uJy5cbiAqL1xuaW1wb3J0IHsgZ2xvYlN5bmMgfSBmcm9tICdnbG9iJztcbmltcG9ydCB7IHJlc29sdmUsIGJhc2VuYW1lIH0gZnJvbSAncGF0aCc7XG5pbXBvcnQgeyBleGlzdHNTeW5jLCByZWFkRmlsZVN5bmMsIHdyaXRlRmlsZVN5bmMgfSBmcm9tICdmcyc7XG5pbXBvcnQgeyBjaGVja01vZHVsZXMgfSBmcm9tICcuL3RoZW1lLWNvcHkuanMnO1xuXG4vLyBTcGVjaWFsIGZvbGRlciBpbnNpZGUgYSB0aGVtZSBmb3IgY29tcG9uZW50IHRoZW1lcyB0aGF0IGdvIGluc2lkZSB0aGUgY29tcG9uZW50IHNoYWRvdyByb290XG5jb25zdCB0aGVtZUNvbXBvbmVudHNGb2xkZXIgPSAnY29tcG9uZW50cyc7XG4vLyBUaGUgY29udGVudHMgb2YgYSBnbG9iYWwgQ1NTIGZpbGUgd2l0aCB0aGlzIG5hbWUgaW4gYSB0aGVtZSBpcyBhbHdheXMgYWRkZWQgdG9cbi8vIHRoZSBkb2N1bWVudC4gRS5nLiBAZm9udC1mYWNlIG11c3QgYmUgaW4gdGhpc1xuY29uc3QgZG9jdW1lbnRDc3NGaWxlbmFtZSA9ICdkb2N1bWVudC5jc3MnO1xuLy8gc3R5bGVzLmNzcyBpcyB0aGUgb25seSBlbnRyeXBvaW50IGNzcyBmaWxlIHdpdGggZG9jdW1lbnQuY3NzLiBFdmVyeXRoaW5nIGVsc2Ugc2hvdWxkIGJlIGltcG9ydGVkIHVzaW5nIGNzcyBAaW1wb3J0XG5jb25zdCBzdHlsZXNDc3NGaWxlbmFtZSA9ICdzdHlsZXMuY3NzJztcblxuY29uc3QgQ1NTSU1QT1JUX0NPTU1FTlQgPSAnQ1NTSW1wb3J0IGVuZCc7XG5jb25zdCBoZWFkZXJJbXBvcnQgPSBgaW1wb3J0ICdjb25zdHJ1Y3Qtc3R5bGUtc2hlZXRzLXBvbHlmaWxsJztcbmA7XG5cbi8qKlxuICogR2VuZXJhdGUgdGhlIFt0aGVtZU5hbWVdLmpzIGZpbGUgZm9yIHRoZW1lRm9sZGVyIHdoaWNoIGNvbGxlY3RzIGFsbCByZXF1aXJlZCBpbmZvcm1hdGlvbiBmcm9tIHRoZSBmb2xkZXIuXG4gKlxuICogQHBhcmFtIHtzdHJpbmd9IHRoZW1lRm9sZGVyIGZvbGRlciBvZiB0aGUgdGhlbWVcbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZU5hbWUgbmFtZSBvZiB0aGUgaGFuZGxlZCB0aGVtZVxuICogQHBhcmFtIHtKU09OfSB0aGVtZVByb3BlcnRpZXMgY29udGVudCBvZiB0aGVtZS5qc29uXG4gKiBAcGFyYW0ge09iamVjdH0gb3B0aW9ucyBidWlsZCBvcHRpb25zIChlLmcuIHByb2Qgb3IgZGV2IG1vZGUpXG4gKiBAcmV0dXJucyB7c3RyaW5nfSB0aGVtZSBmaWxlIGNvbnRlbnRcbiAqL1xuZnVuY3Rpb24gd3JpdGVUaGVtZUZpbGVzKHRoZW1lRm9sZGVyLCB0aGVtZU5hbWUsIHRoZW1lUHJvcGVydGllcywgb3B0aW9ucykge1xuICBjb25zdCBwcm9kdWN0aW9uTW9kZSA9ICFvcHRpb25zLmRldk1vZGU7XG4gIGNvbnN0IHVzZURldlNlcnZlck9ySW5Qcm9kdWN0aW9uTW9kZSA9ICFvcHRpb25zLnVzZURldkJ1bmRsZTtcbiAgY29uc3Qgb3V0cHV0Rm9sZGVyID0gb3B0aW9ucy5mcm9udGVuZEdlbmVyYXRlZEZvbGRlcjtcbiAgY29uc3Qgc3R5bGVzID0gcmVzb2x2ZSh0aGVtZUZvbGRlciwgc3R5bGVzQ3NzRmlsZW5hbWUpO1xuICBjb25zdCBkb2N1bWVudENzc0ZpbGUgPSByZXNvbHZlKHRoZW1lRm9sZGVyLCBkb2N1bWVudENzc0ZpbGVuYW1lKTtcbiAgY29uc3QgYXV0b0luamVjdENvbXBvbmVudHMgPSB0aGVtZVByb3BlcnRpZXMuYXV0b0luamVjdENvbXBvbmVudHMgPz8gdHJ1ZTtcbiAgY29uc3QgYXV0b0luamVjdEdsb2JhbENzc0ltcG9ydHMgPSB0aGVtZVByb3BlcnRpZXMuYXV0b0luamVjdEdsb2JhbENzc0ltcG9ydHMgPz8gZmFsc2U7XG4gIGNvbnN0IGdsb2JhbEZpbGVuYW1lID0gJ3RoZW1lLScgKyB0aGVtZU5hbWUgKyAnLmdsb2JhbC5nZW5lcmF0ZWQuanMnO1xuICBjb25zdCBjb21wb25lbnRzRmlsZW5hbWUgPSAndGhlbWUtJyArIHRoZW1lTmFtZSArICcuY29tcG9uZW50cy5nZW5lcmF0ZWQuanMnO1xuICBjb25zdCB0aGVtZUZpbGVuYW1lID0gJ3RoZW1lLScgKyB0aGVtZU5hbWUgKyAnLmdlbmVyYXRlZC5qcyc7XG5cbiAgbGV0IHRoZW1lRmlsZUNvbnRlbnQgPSBoZWFkZXJJbXBvcnQ7XG4gIGxldCBnbG9iYWxJbXBvcnRDb250ZW50ID0gJy8vIFdoZW4gdGhpcyBmaWxlIGlzIGltcG9ydGVkLCBnbG9iYWwgc3R5bGVzIGFyZSBhdXRvbWF0aWNhbGx5IGFwcGxpZWRcXG4nO1xuICBsZXQgY29tcG9uZW50c0ZpbGVDb250ZW50ID0gJyc7XG4gIHZhciBjb21wb25lbnRzRmlsZXM7XG5cbiAgaWYgKGF1dG9JbmplY3RDb21wb25lbnRzKSB7XG4gICAgY29tcG9uZW50c0ZpbGVzID0gZ2xvYlN5bmMoJyouY3NzJywge1xuICAgICAgY3dkOiByZXNvbHZlKHRoZW1lRm9sZGVyLCB0aGVtZUNvbXBvbmVudHNGb2xkZXIpLFxuICAgICAgbm9kaXI6IHRydWVcbiAgICB9KTtcblxuICAgIGlmIChjb21wb25lbnRzRmlsZXMubGVuZ3RoID4gMCkge1xuICAgICAgY29tcG9uZW50c0ZpbGVDb250ZW50ICs9XG4gICAgICAgIFwiaW1wb3J0IHsgdW5zYWZlQ1NTLCByZWdpc3RlclN0eWxlcyB9IGZyb20gJ0B2YWFkaW4vdmFhZGluLXRoZW1hYmxlLW1peGluL3JlZ2lzdGVyLXN0eWxlcyc7XFxuXCI7XG4gICAgfVxuICB9XG5cbiAgaWYgKHRoZW1lUHJvcGVydGllcy5wYXJlbnQpIHtcbiAgICB0aGVtZUZpbGVDb250ZW50ICs9IGBpbXBvcnQgeyBhcHBseVRoZW1lIGFzIGFwcGx5QmFzZVRoZW1lIH0gZnJvbSAnLi90aGVtZS0ke3RoZW1lUHJvcGVydGllcy5wYXJlbnR9LmdlbmVyYXRlZC5qcyc7XFxuYDtcbiAgfVxuXG4gIHRoZW1lRmlsZUNvbnRlbnQgKz0gYGltcG9ydCB7IGluamVjdEdsb2JhbENzcyB9IGZyb20gJ0Zyb250ZW5kL2dlbmVyYXRlZC9qYXItcmVzb3VyY2VzL3RoZW1lLXV0aWwuanMnO1xcbmA7XG4gIHRoZW1lRmlsZUNvbnRlbnQgKz0gYGltcG9ydCB7IHdlYmNvbXBvbmVudEdsb2JhbENzc0luamVjdG9yIH0gZnJvbSAnRnJvbnRlbmQvZ2VuZXJhdGVkL2phci1yZXNvdXJjZXMvdGhlbWUtdXRpbC5qcyc7XFxuYDtcbiAgdGhlbWVGaWxlQ29udGVudCArPSBgaW1wb3J0ICcuLyR7Y29tcG9uZW50c0ZpbGVuYW1lfSc7XFxuYDtcblxuICB0aGVtZUZpbGVDb250ZW50ICs9IGBsZXQgbmVlZHNSZWxvYWRPbkNoYW5nZXMgPSBmYWxzZTtcXG5gO1xuICBjb25zdCBpbXBvcnRzID0gW107XG4gIGNvbnN0IGNvbXBvbmVudENzc0ltcG9ydHMgPSBbXTtcbiAgY29uc3QgZ2xvYmFsRmlsZUNvbnRlbnQgPSBbXTtcbiAgY29uc3QgZ2xvYmFsQ3NzQ29kZSA9IFtdO1xuICBjb25zdCBzaGFkb3dPbmx5Q3NzID0gW107XG4gIGNvbnN0IGNvbXBvbmVudENzc0NvZGUgPSBbXTtcbiAgY29uc3QgcGFyZW50VGhlbWUgPSB0aGVtZVByb3BlcnRpZXMucGFyZW50ID8gJ2FwcGx5QmFzZVRoZW1lKHRhcmdldCk7XFxuJyA6ICcnO1xuICBjb25zdCBwYXJlbnRUaGVtZUdsb2JhbEltcG9ydCA9IHRoZW1lUHJvcGVydGllcy5wYXJlbnRcbiAgICA/IGBpbXBvcnQgJy4vdGhlbWUtJHt0aGVtZVByb3BlcnRpZXMucGFyZW50fS5nbG9iYWwuZ2VuZXJhdGVkLmpzJztcXG5gXG4gICAgOiAnJztcblxuICBjb25zdCB0aGVtZUlkZW50aWZpZXIgPSAnX3ZhYWRpbnRoZW1lXycgKyB0aGVtZU5hbWUgKyAnXyc7XG4gIGNvbnN0IGx1bW9Dc3NGbGFnID0gJ192YWFkaW50aGVtZWx1bW9pbXBvcnRzXyc7XG4gIGNvbnN0IGdsb2JhbENzc0ZsYWcgPSB0aGVtZUlkZW50aWZpZXIgKyAnZ2xvYmFsQ3NzJztcbiAgY29uc3QgY29tcG9uZW50Q3NzRmxhZyA9IHRoZW1lSWRlbnRpZmllciArICdjb21wb25lbnRDc3MnO1xuXG4gIGlmICghZXhpc3RzU3luYyhzdHlsZXMpKSB7XG4gICAgaWYgKHByb2R1Y3Rpb25Nb2RlKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoYHN0eWxlcy5jc3MgZmlsZSBpcyBtaXNzaW5nIGFuZCBpcyBuZWVkZWQgZm9yICcke3RoZW1lTmFtZX0nIGluIGZvbGRlciAnJHt0aGVtZUZvbGRlcn0nYCk7XG4gICAgfVxuICAgIHdyaXRlRmlsZVN5bmMoXG4gICAgICBzdHlsZXMsXG4gICAgICAnLyogSW1wb3J0IHlvdXIgYXBwbGljYXRpb24gZ2xvYmFsIGNzcyBmaWxlcyBoZXJlIG9yIGFkZCB0aGUgc3R5bGVzIGRpcmVjdGx5IHRvIHRoaXMgZmlsZSAqLycsXG4gICAgICAndXRmOCdcbiAgICApO1xuICB9XG5cbiAgLy8gc3R5bGVzLmNzcyB3aWxsIGFsd2F5cyBiZSBhdmFpbGFibGUgYXMgd2Ugd3JpdGUgb25lIGlmIGl0IGRvZXNuJ3QgZXhpc3QuXG4gIGxldCBmaWxlbmFtZSA9IGJhc2VuYW1lKHN0eWxlcyk7XG4gIGxldCB2YXJpYWJsZSA9IGNhbWVsQ2FzZShmaWxlbmFtZSk7XG5cbiAgLyogTFVNTyAqL1xuICBjb25zdCBsdW1vSW1wb3J0cyA9IHRoZW1lUHJvcGVydGllcy5sdW1vSW1wb3J0cyB8fCBbJ2NvbG9yJywgJ3R5cG9ncmFwaHknXTtcbiAgaWYgKGx1bW9JbXBvcnRzKSB7XG4gICAgbHVtb0ltcG9ydHMuZm9yRWFjaCgobHVtb0ltcG9ydCkgPT4ge1xuICAgICAgaW1wb3J0cy5wdXNoKGBpbXBvcnQgeyAke2x1bW9JbXBvcnR9IH0gZnJvbSAnQHZhYWRpbi92YWFkaW4tbHVtby1zdHlsZXMvJHtsdW1vSW1wb3J0fS5qcyc7XFxuYCk7XG4gICAgICBpZiAobHVtb0ltcG9ydCA9PT0gJ3V0aWxpdHknIHx8IGx1bW9JbXBvcnQgPT09ICdiYWRnZScgfHwgbHVtb0ltcG9ydCA9PT0gJ3R5cG9ncmFwaHknIHx8IGx1bW9JbXBvcnQgPT09ICdjb2xvcicpIHtcbiAgICAgICAgLy8gSW5qZWN0IGludG8gbWFpbiBkb2N1bWVudCB0aGUgc2FtZSB3YXkgYXMgb3RoZXIgTHVtbyBzdHlsZXMgYXJlIGluamVjdGVkXG4gICAgICAgIC8vIEx1bW8gaW1wb3J0cyBnbyB0byB0aGUgdGhlbWUgZ2xvYmFsIGltcG9ydHMgZmlsZSB0byBwcmV2ZW50IHN0eWxlIGxlYWtzXG4gICAgICAgIC8vIHdoZW4gdGhlIHRoZW1lIGlzIGFwcGxpZWQgdG8gYW4gZW1iZWRkZWQgY29tcG9uZW50XG4gICAgICAgIGdsb2JhbEZpbGVDb250ZW50LnB1c2goYGltcG9ydCAnQHZhYWRpbi92YWFkaW4tbHVtby1zdHlsZXMvJHtsdW1vSW1wb3J0fS1nbG9iYWwuanMnO1xcbmApO1xuICAgICAgfVxuICAgIH0pO1xuXG4gICAgbHVtb0ltcG9ydHMuZm9yRWFjaCgobHVtb0ltcG9ydCkgPT4ge1xuICAgICAgLy8gTHVtbyBpcyBpbmplY3RlZCB0byB0aGUgZG9jdW1lbnQgYnkgTHVtbyBpdHNlbGZcbiAgICAgIHNoYWRvd09ubHlDc3MucHVzaChgcmVtb3ZlcnMucHVzaChpbmplY3RHbG9iYWxDc3MoJHtsdW1vSW1wb3J0fS5jc3NUZXh0LCAnJywgdGFyZ2V0LCB0cnVlKSk7XFxuYCk7XG4gICAgfSk7XG4gIH1cblxuICAvKiBUaGVtZSAqL1xuICBnbG9iYWxGaWxlQ29udGVudC5wdXNoKHBhcmVudFRoZW1lR2xvYmFsSW1wb3J0KTtcbiAgaWYgKHVzZURldlNlcnZlck9ySW5Qcm9kdWN0aW9uTW9kZSkge1xuICAgIGdsb2JhbEZpbGVDb250ZW50LnB1c2goYGltcG9ydCAndGhlbWVzLyR7dGhlbWVOYW1lfS8ke2ZpbGVuYW1lfSc7XFxuYCk7XG5cbiAgICBpbXBvcnRzLnB1c2goYGltcG9ydCAke3ZhcmlhYmxlfSBmcm9tICd0aGVtZXMvJHt0aGVtZU5hbWV9LyR7ZmlsZW5hbWV9P2lubGluZSc7XFxuYCk7XG4gICAgc2hhZG93T25seUNzcy5wdXNoKGByZW1vdmVycy5wdXNoKGluamVjdEdsb2JhbENzcygke3ZhcmlhYmxlfS50b1N0cmluZygpLCAnJywgdGFyZ2V0KSk7XFxuICAgIGApO1xuICB9XG4gIGlmIChleGlzdHNTeW5jKGRvY3VtZW50Q3NzRmlsZSkpIHtcbiAgICBmaWxlbmFtZSA9IGJhc2VuYW1lKGRvY3VtZW50Q3NzRmlsZSk7XG4gICAgdmFyaWFibGUgPSBjYW1lbENhc2UoZmlsZW5hbWUpO1xuXG4gICAgaWYgKHVzZURldlNlcnZlck9ySW5Qcm9kdWN0aW9uTW9kZSkge1xuICAgICAgZ2xvYmFsRmlsZUNvbnRlbnQucHVzaChgaW1wb3J0ICd0aGVtZXMvJHt0aGVtZU5hbWV9LyR7ZmlsZW5hbWV9JztcXG5gKTtcblxuICAgICAgaW1wb3J0cy5wdXNoKGBpbXBvcnQgJHt2YXJpYWJsZX0gZnJvbSAndGhlbWVzLyR7dGhlbWVOYW1lfS8ke2ZpbGVuYW1lfT9pbmxpbmUnO1xcbmApO1xuICAgICAgc2hhZG93T25seUNzcy5wdXNoKGByZW1vdmVycy5wdXNoKGluamVjdEdsb2JhbENzcygke3ZhcmlhYmxlfS50b1N0cmluZygpLCcnLCBkb2N1bWVudCkpO1xcbiAgICBgKTtcbiAgICB9XG4gIH1cblxuICBsZXQgaSA9IDA7XG4gIGlmICh0aGVtZVByb3BlcnRpZXMuZG9jdW1lbnRDc3MpIHtcbiAgICBjb25zdCBtaXNzaW5nTW9kdWxlcyA9IGNoZWNrTW9kdWxlcyh0aGVtZVByb3BlcnRpZXMuZG9jdW1lbnRDc3MpO1xuICAgIGlmIChtaXNzaW5nTW9kdWxlcy5sZW5ndGggPiAwKSB7XG4gICAgICB0aHJvdyBFcnJvcihcbiAgICAgICAgXCJNaXNzaW5nIG5wbSBtb2R1bGVzIG9yIGZpbGVzICdcIiArXG4gICAgICAgICAgbWlzc2luZ01vZHVsZXMuam9pbihcIicsICdcIikgK1xuICAgICAgICAgIFwiJyBmb3IgZG9jdW1lbnRDc3MgbWFya2VkIGluICd0aGVtZS5qc29uJy5cXG5cIiArXG4gICAgICAgICAgXCJJbnN0YWxsIG9yIHVwZGF0ZSBwYWNrYWdlKHMpIGJ5IGFkZGluZyBhIEBOcG1QYWNrYWdlIGFubm90YXRpb24gb3IgaW5zdGFsbCBpdCB1c2luZyAnbnBtL3BucG0vYnVuIGknXCJcbiAgICAgICk7XG4gICAgfVxuICAgIHRoZW1lUHJvcGVydGllcy5kb2N1bWVudENzcy5mb3JFYWNoKChjc3NJbXBvcnQpID0+IHtcbiAgICAgIGNvbnN0IHZhcmlhYmxlID0gJ21vZHVsZScgKyBpKys7XG4gICAgICBpbXBvcnRzLnB1c2goYGltcG9ydCAke3ZhcmlhYmxlfSBmcm9tICcke2Nzc0ltcG9ydH0/aW5saW5lJztcXG5gKTtcbiAgICAgIC8vIER1ZSB0byBjaHJvbWUgYnVnIGh0dHBzOi8vYnVncy5jaHJvbWl1bS5vcmcvcC9jaHJvbWl1bS9pc3N1ZXMvZGV0YWlsP2lkPTMzNjg3NiBmb250LWZhY2Ugd2lsbCBub3Qgd29ya1xuICAgICAgLy8gaW5zaWRlIHNoYWRvd1Jvb3Qgc28gd2UgbmVlZCB0byBpbmplY3QgaXQgdGhlcmUgYWxzby5cbiAgICAgIGdsb2JhbENzc0NvZGUucHVzaChgaWYodGFyZ2V0ICE9PSBkb2N1bWVudCkge1xuICAgICAgICByZW1vdmVycy5wdXNoKGluamVjdEdsb2JhbENzcygke3ZhcmlhYmxlfS50b1N0cmluZygpLCAnJywgdGFyZ2V0KSk7XG4gICAgfVxcbiAgICBgKTtcbiAgICAgIGdsb2JhbENzc0NvZGUucHVzaChcbiAgICAgICAgYHJlbW92ZXJzLnB1c2goaW5qZWN0R2xvYmFsQ3NzKCR7dmFyaWFibGV9LnRvU3RyaW5nKCksICcke0NTU0lNUE9SVF9DT01NRU5UfScsIGRvY3VtZW50KSk7XFxuICAgIGBcbiAgICAgICk7XG4gICAgfSk7XG4gIH1cbiAgaWYgKHRoZW1lUHJvcGVydGllcy5pbXBvcnRDc3MpIHtcbiAgICBjb25zdCBtaXNzaW5nTW9kdWxlcyA9IGNoZWNrTW9kdWxlcyh0aGVtZVByb3BlcnRpZXMuaW1wb3J0Q3NzKTtcbiAgICBpZiAobWlzc2luZ01vZHVsZXMubGVuZ3RoID4gMCkge1xuICAgICAgdGhyb3cgRXJyb3IoXG4gICAgICAgIFwiTWlzc2luZyBucG0gbW9kdWxlcyBvciBmaWxlcyAnXCIgK1xuICAgICAgICAgIG1pc3NpbmdNb2R1bGVzLmpvaW4oXCInLCAnXCIpICtcbiAgICAgICAgICBcIicgZm9yIGltcG9ydENzcyBtYXJrZWQgaW4gJ3RoZW1lLmpzb24nLlxcblwiICtcbiAgICAgICAgICBcIkluc3RhbGwgb3IgdXBkYXRlIHBhY2thZ2UocykgYnkgYWRkaW5nIGEgQE5wbVBhY2thZ2UgYW5ub3RhdGlvbiBvciBpbnN0YWxsIGl0IHVzaW5nICducG0vcG5wbS9idW4gaSdcIlxuICAgICAgKTtcbiAgICB9XG4gICAgdGhlbWVQcm9wZXJ0aWVzLmltcG9ydENzcy5mb3JFYWNoKChjc3NQYXRoKSA9PiB7XG4gICAgICBjb25zdCB2YXJpYWJsZSA9ICdtb2R1bGUnICsgaSsrO1xuICAgICAgZ2xvYmFsRmlsZUNvbnRlbnQucHVzaChgaW1wb3J0ICcke2Nzc1BhdGh9JztcXG5gKTtcbiAgICAgIGltcG9ydHMucHVzaChgaW1wb3J0ICR7dmFyaWFibGV9IGZyb20gJyR7Y3NzUGF0aH0/aW5saW5lJztcXG5gKTtcbiAgICAgIHNoYWRvd09ubHlDc3MucHVzaChgcmVtb3ZlcnMucHVzaChpbmplY3RHbG9iYWxDc3MoJHt2YXJpYWJsZX0udG9TdHJpbmcoKSwgJyR7Q1NTSU1QT1JUX0NPTU1FTlR9JywgdGFyZ2V0KSk7XFxuYCk7XG4gICAgfSk7XG4gIH1cblxuICBpZiAoYXV0b0luamVjdENvbXBvbmVudHMpIHtcbiAgICBjb21wb25lbnRzRmlsZXMuZm9yRWFjaCgoY29tcG9uZW50Q3NzKSA9PiB7XG4gICAgICBjb25zdCBmaWxlbmFtZSA9IGJhc2VuYW1lKGNvbXBvbmVudENzcyk7XG4gICAgICBjb25zdCB0YWcgPSBmaWxlbmFtZS5yZXBsYWNlKCcuY3NzJywgJycpO1xuICAgICAgY29uc3QgdmFyaWFibGUgPSBjYW1lbENhc2UoZmlsZW5hbWUpO1xuICAgICAgY29tcG9uZW50Q3NzSW1wb3J0cy5wdXNoKFxuICAgICAgICBgaW1wb3J0ICR7dmFyaWFibGV9IGZyb20gJ3RoZW1lcy8ke3RoZW1lTmFtZX0vJHt0aGVtZUNvbXBvbmVudHNGb2xkZXJ9LyR7ZmlsZW5hbWV9P2lubGluZSc7XFxuYFxuICAgICAgKTtcbiAgICAgIC8vIERvbid0IGZvcm1hdCBhcyB0aGUgZ2VuZXJhdGVkIGZpbGUgZm9ybWF0dGluZyB3aWxsIGdldCB3b25reSFcbiAgICAgIGNvbnN0IGNvbXBvbmVudFN0cmluZyA9IGByZWdpc3RlclN0eWxlcyhcbiAgICAgICAgJyR7dGFnfScsXG4gICAgICAgIHVuc2FmZUNTUygke3ZhcmlhYmxlfS50b1N0cmluZygpKVxuICAgICAgKTtcbiAgICAgIGA7XG4gICAgICBjb21wb25lbnRDc3NDb2RlLnB1c2goY29tcG9uZW50U3RyaW5nKTtcbiAgICB9KTtcbiAgfVxuXG4gIHRoZW1lRmlsZUNvbnRlbnQgKz0gaW1wb3J0cy5qb2luKCcnKTtcblxuICAvLyBEb24ndCBmb3JtYXQgYXMgdGhlIGdlbmVyYXRlZCBmaWxlIGZvcm1hdHRpbmcgd2lsbCBnZXQgd29ua3khXG4gIC8vIElmIHRhcmdldHMgY2hlY2sgdGhhdCB3ZSBvbmx5IHJlZ2lzdGVyIHRoZSBzdHlsZSBwYXJ0cyBvbmNlLCBjaGVja3MgZXhpc3QgZm9yIGdsb2JhbCBjc3MgYW5kIGNvbXBvbmVudCBjc3NcbiAgY29uc3QgdGhlbWVGaWxlQXBwbHkgPSBgXG4gIGxldCB0aGVtZVJlbW92ZXJzID0gbmV3IFdlYWtNYXAoKTtcbiAgbGV0IHRhcmdldHMgPSBbXTtcblxuICBleHBvcnQgY29uc3QgYXBwbHlUaGVtZSA9ICh0YXJnZXQpID0+IHtcbiAgICBjb25zdCByZW1vdmVycyA9IFtdO1xuICAgIGlmICh0YXJnZXQgIT09IGRvY3VtZW50KSB7XG4gICAgICAke3NoYWRvd09ubHlDc3Muam9pbignJyl9XG4gICAgICAke2F1dG9JbmplY3RHbG9iYWxDc3NJbXBvcnRzID8gYFxuICAgICAgICB3ZWJjb21wb25lbnRHbG9iYWxDc3NJbmplY3RvcigoY3NzKSA9PiB7XG4gICAgICAgICAgcmVtb3ZlcnMucHVzaChpbmplY3RHbG9iYWxDc3MoY3NzLCAnJywgdGFyZ2V0KSk7XG4gICAgICAgIH0pO1xuICAgICAgICBgIDogJyd9XG4gICAgfVxuICAgICR7cGFyZW50VGhlbWV9XG4gICAgJHtnbG9iYWxDc3NDb2RlLmpvaW4oJycpfVxuXG4gICAgaWYgKGltcG9ydC5tZXRhLmhvdCkge1xuICAgICAgdGFyZ2V0cy5wdXNoKG5ldyBXZWFrUmVmKHRhcmdldCkpO1xuICAgICAgdGhlbWVSZW1vdmVycy5zZXQodGFyZ2V0LCByZW1vdmVycyk7XG4gICAgfVxuXG4gIH1cblxuYDtcbiAgY29tcG9uZW50c0ZpbGVDb250ZW50ICs9IGBcbiR7Y29tcG9uZW50Q3NzSW1wb3J0cy5qb2luKCcnKX1cblxuaWYgKCFkb2N1bWVudFsnJHtjb21wb25lbnRDc3NGbGFnfSddKSB7XG4gICR7Y29tcG9uZW50Q3NzQ29kZS5qb2luKCcnKX1cbiAgZG9jdW1lbnRbJyR7Y29tcG9uZW50Q3NzRmxhZ30nXSA9IHRydWU7XG59XG5cbmlmIChpbXBvcnQubWV0YS5ob3QpIHtcbiAgaW1wb3J0Lm1ldGEuaG90LmFjY2VwdCgobW9kdWxlKSA9PiB7XG4gICAgd2luZG93LmxvY2F0aW9uLnJlbG9hZCgpO1xuICB9KTtcbn1cblxuYDtcblxuICB0aGVtZUZpbGVDb250ZW50ICs9IHRoZW1lRmlsZUFwcGx5O1xuICB0aGVtZUZpbGVDb250ZW50ICs9IGBcbmlmIChpbXBvcnQubWV0YS5ob3QpIHtcbiAgaW1wb3J0Lm1ldGEuaG90LmFjY2VwdCgobW9kdWxlKSA9PiB7XG5cbiAgICBpZiAobmVlZHNSZWxvYWRPbkNoYW5nZXMpIHtcbiAgICAgIHdpbmRvdy5sb2NhdGlvbi5yZWxvYWQoKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdGFyZ2V0cy5mb3JFYWNoKHRhcmdldFJlZiA9PiB7XG4gICAgICAgIGNvbnN0IHRhcmdldCA9IHRhcmdldFJlZi5kZXJlZigpO1xuICAgICAgICBpZiAodGFyZ2V0KSB7XG4gICAgICAgICAgdGhlbWVSZW1vdmVycy5nZXQodGFyZ2V0KS5mb3JFYWNoKHJlbW92ZXIgPT4gcmVtb3ZlcigpKVxuICAgICAgICAgIG1vZHVsZS5hcHBseVRoZW1lKHRhcmdldCk7XG4gICAgICAgIH1cbiAgICAgIH0pXG4gICAgfVxuICB9KTtcblxuICBpbXBvcnQubWV0YS5ob3Qub24oJ3ZpdGU6YWZ0ZXJVcGRhdGUnLCAodXBkYXRlKSA9PiB7XG4gICAgZG9jdW1lbnQuZGlzcGF0Y2hFdmVudChuZXcgQ3VzdG9tRXZlbnQoJ3ZhYWRpbi10aGVtZS11cGRhdGVkJywgeyBkZXRhaWw6IHVwZGF0ZSB9KSk7XG4gIH0pO1xufVxuXG5gO1xuXG4gIGdsb2JhbEltcG9ydENvbnRlbnQgKz0gYFxuJHtnbG9iYWxGaWxlQ29udGVudC5qb2luKCcnKX1cbmA7XG5cbiAgd3JpdGVJZkNoYW5nZWQocmVzb2x2ZShvdXRwdXRGb2xkZXIsIGdsb2JhbEZpbGVuYW1lKSwgZ2xvYmFsSW1wb3J0Q29udGVudCk7XG4gIHdyaXRlSWZDaGFuZ2VkKHJlc29sdmUob3V0cHV0Rm9sZGVyLCB0aGVtZUZpbGVuYW1lKSwgdGhlbWVGaWxlQ29udGVudCk7XG4gIHdyaXRlSWZDaGFuZ2VkKHJlc29sdmUob3V0cHV0Rm9sZGVyLCBjb21wb25lbnRzRmlsZW5hbWUpLCBjb21wb25lbnRzRmlsZUNvbnRlbnQpO1xufVxuXG5mdW5jdGlvbiB3cml0ZUlmQ2hhbmdlZChmaWxlLCBkYXRhKSB7XG4gIGlmICghZXhpc3RzU3luYyhmaWxlKSB8fCByZWFkRmlsZVN5bmMoZmlsZSwgeyBlbmNvZGluZzogJ3V0Zi04JyB9KSAhPT0gZGF0YSkge1xuICAgIHdyaXRlRmlsZVN5bmMoZmlsZSwgZGF0YSk7XG4gIH1cbn1cblxuLyoqXG4gKiBNYWtlIGdpdmVuIHN0cmluZyBpbnRvIGNhbWVsQ2FzZS5cbiAqXG4gKiBAcGFyYW0ge3N0cmluZ30gc3RyIHN0cmluZyB0byBtYWtlIGludG8gY2FtZUNhc2VcbiAqIEByZXR1cm5zIHtzdHJpbmd9IGNhbWVsQ2FzZWQgdmVyc2lvblxuICovXG5mdW5jdGlvbiBjYW1lbENhc2Uoc3RyKSB7XG4gIHJldHVybiBzdHJcbiAgICAucmVwbGFjZSgvKD86Xlxcd3xbQS1aXXxcXGJcXHcpL2csIGZ1bmN0aW9uICh3b3JkLCBpbmRleCkge1xuICAgICAgcmV0dXJuIGluZGV4ID09PSAwID8gd29yZC50b0xvd2VyQ2FzZSgpIDogd29yZC50b1VwcGVyQ2FzZSgpO1xuICAgIH0pXG4gICAgLnJlcGxhY2UoL1xccysvZywgJycpXG4gICAgLnJlcGxhY2UoL1xcLnxcXC0vZywgJycpO1xufVxuXG5leHBvcnQgeyB3cml0ZVRoZW1lRmlsZXMgfTtcbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcYnVpbGRcXFxccGx1Z2luc1xcXFxhcHBsaWNhdGlvbi10aGVtZS1wbHVnaW5cIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXHV0ZW50ZVxcXFxJZGVhUHJvamVjdHNcXFxcZHVlZW1tZXN0b3JlXFxcXGJ1aWxkXFxcXHBsdWdpbnNcXFxcYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luXFxcXHRoZW1lLWNvcHkuanNcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfaW1wb3J0X21ldGFfdXJsID0gXCJmaWxlOi8vL0M6L1VzZXJzL3V0ZW50ZS9JZGVhUHJvamVjdHMvZHVlZW1tZXN0b3JlL2J1aWxkL3BsdWdpbnMvYXBwbGljYXRpb24tdGhlbWUtcGx1Z2luL3RoZW1lLWNvcHkuanNcIjsvKlxuICogQ29weXJpZ2h0IDIwMDAtMjAyNCBWYWFkaW4gTHRkLlxuICpcbiAqIExpY2Vuc2VkIHVuZGVyIHRoZSBBcGFjaGUgTGljZW5zZSwgVmVyc2lvbiAyLjAgKHRoZSBcIkxpY2Vuc2VcIik7IHlvdSBtYXkgbm90XG4gKiB1c2UgdGhpcyBmaWxlIGV4Y2VwdCBpbiBjb21wbGlhbmNlIHdpdGggdGhlIExpY2Vuc2UuIFlvdSBtYXkgb2J0YWluIGEgY29weSBvZlxuICogdGhlIExpY2Vuc2UgYXRcbiAqXG4gKiBodHRwOi8vd3d3LmFwYWNoZS5vcmcvbGljZW5zZXMvTElDRU5TRS0yLjBcbiAqXG4gKiBVbmxlc3MgcmVxdWlyZWQgYnkgYXBwbGljYWJsZSBsYXcgb3IgYWdyZWVkIHRvIGluIHdyaXRpbmcsIHNvZnR3YXJlXG4gKiBkaXN0cmlidXRlZCB1bmRlciB0aGUgTGljZW5zZSBpcyBkaXN0cmlidXRlZCBvbiBhbiBcIkFTIElTXCIgQkFTSVMsIFdJVEhPVVRcbiAqIFdBUlJBTlRJRVMgT1IgQ09ORElUSU9OUyBPRiBBTlkgS0lORCwgZWl0aGVyIGV4cHJlc3Mgb3IgaW1wbGllZC4gU2VlIHRoZVxuICogTGljZW5zZSBmb3IgdGhlIHNwZWNpZmljIGxhbmd1YWdlIGdvdmVybmluZyBwZXJtaXNzaW9ucyBhbmQgbGltaXRhdGlvbnMgdW5kZXJcbiAqIHRoZSBMaWNlbnNlLlxuICovXG5cbi8qKlxuICogVGhpcyBjb250YWlucyBmdW5jdGlvbnMgYW5kIGZlYXR1cmVzIHVzZWQgdG8gY29weSB0aGVtZSBmaWxlcy5cbiAqL1xuXG5pbXBvcnQgeyByZWFkZGlyU3luYywgc3RhdFN5bmMsIG1rZGlyU3luYywgZXhpc3RzU3luYywgY29weUZpbGVTeW5jIH0gZnJvbSAnZnMnO1xuaW1wb3J0IHsgcmVzb2x2ZSwgYmFzZW5hbWUsIHJlbGF0aXZlLCBleHRuYW1lIH0gZnJvbSAncGF0aCc7XG5pbXBvcnQgeyBnbG9iU3luYyB9IGZyb20gJ2dsb2InO1xuXG5jb25zdCBpZ25vcmVkRmlsZUV4dGVuc2lvbnMgPSBbJy5jc3MnLCAnLmpzJywgJy5qc29uJ107XG5cbi8qKlxuICogQ29weSB0aGVtZSBzdGF0aWMgcmVzb3VyY2VzIHRvIHN0YXRpYyBhc3NldHMgZm9sZGVyLiBBbGwgZmlsZXMgaW4gdGhlIHRoZW1lXG4gKiBmb2xkZXIgd2lsbCBiZSBjb3BpZWQgZXhjbHVkaW5nIGNzcywganMgYW5kIGpzb24gZmlsZXMgdGhhdCB3aWxsIGJlXG4gKiBoYW5kbGVkIGJ5IHdlYnBhY2sgYW5kIG5vdCBiZSBzaGFyZWQgYXMgc3RhdGljIGZpbGVzLlxuICpcbiAqIEBwYXJhbSB7c3RyaW5nfSB0aGVtZUZvbGRlciBGb2xkZXIgd2l0aCB0aGVtZSBmaWxlXG4gKiBAcGFyYW0ge3N0cmluZ30gcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciByZXNvdXJjZXMgb3V0cHV0IGZvbGRlclxuICogQHBhcmFtIHtvYmplY3R9IGxvZ2dlciBwbHVnaW4gbG9nZ2VyXG4gKi9cbmZ1bmN0aW9uIGNvcHlUaGVtZVJlc291cmNlcyh0aGVtZUZvbGRlciwgcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciwgbG9nZ2VyKSB7XG4gIGNvbnN0IHN0YXRpY0Fzc2V0c1RoZW1lRm9sZGVyID0gcmVzb2x2ZShwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCAndGhlbWVzJywgYmFzZW5hbWUodGhlbWVGb2xkZXIpKTtcbiAgY29uc3QgY29sbGVjdGlvbiA9IGNvbGxlY3RGb2xkZXJzKHRoZW1lRm9sZGVyLCBsb2dnZXIpO1xuXG4gIC8vIE9ubHkgY3JlYXRlIGFzc2V0cyBmb2xkZXIgaWYgdGhlcmUgYXJlIGZpbGVzIHRvIGNvcHkuXG4gIGlmIChjb2xsZWN0aW9uLmZpbGVzLmxlbmd0aCA+IDApIHtcbiAgICBta2RpclN5bmMoc3RhdGljQXNzZXRzVGhlbWVGb2xkZXIsIHsgcmVjdXJzaXZlOiB0cnVlIH0pO1xuICAgIC8vIGNyZWF0ZSBmb2xkZXJzIHdpdGhcbiAgICBjb2xsZWN0aW9uLmRpcmVjdG9yaWVzLmZvckVhY2goKGRpcmVjdG9yeSkgPT4ge1xuICAgICAgY29uc3QgcmVsYXRpdmVEaXJlY3RvcnkgPSByZWxhdGl2ZSh0aGVtZUZvbGRlciwgZGlyZWN0b3J5KTtcbiAgICAgIGNvbnN0IHRhcmdldERpcmVjdG9yeSA9IHJlc29sdmUoc3RhdGljQXNzZXRzVGhlbWVGb2xkZXIsIHJlbGF0aXZlRGlyZWN0b3J5KTtcblxuICAgICAgbWtkaXJTeW5jKHRhcmdldERpcmVjdG9yeSwgeyByZWN1cnNpdmU6IHRydWUgfSk7XG4gICAgfSk7XG5cbiAgICBjb2xsZWN0aW9uLmZpbGVzLmZvckVhY2goKGZpbGUpID0+IHtcbiAgICAgIGNvbnN0IHJlbGF0aXZlRmlsZSA9IHJlbGF0aXZlKHRoZW1lRm9sZGVyLCBmaWxlKTtcbiAgICAgIGNvbnN0IHRhcmdldEZpbGUgPSByZXNvbHZlKHN0YXRpY0Fzc2V0c1RoZW1lRm9sZGVyLCByZWxhdGl2ZUZpbGUpO1xuICAgICAgY29weUZpbGVJZkFic2VudE9yTmV3ZXIoZmlsZSwgdGFyZ2V0RmlsZSwgbG9nZ2VyKTtcbiAgICB9KTtcbiAgfVxufVxuXG4vKipcbiAqIENvbGxlY3QgYWxsIGZvbGRlcnMgd2l0aCBjb3B5YWJsZSBmaWxlcyBhbmQgYWxsIGZpbGVzIHRvIGJlIGNvcGllZC5cbiAqIEZvbGVkIHdpbGwgbm90IGJlIGFkZGVkIGlmIG5vIGZpbGVzIGluIGZvbGRlciBvciBzdWJmb2xkZXJzLlxuICpcbiAqIEZpbGVzIHdpbGwgbm90IGNvbnRhaW4gZmlsZXMgd2l0aCBpZ25vcmVkIGV4dGVuc2lvbnMgYW5kIGZvbGRlcnMgb25seSBjb250YWluaW5nIGlnbm9yZWQgZmlsZXMgd2lsbCBub3QgYmUgYWRkZWQuXG4gKlxuICogQHBhcmFtIGZvbGRlclRvQ29weSBmb2xkZXIgd2Ugd2lsbCBjb3B5IGZpbGVzIGZyb21cbiAqIEBwYXJhbSBsb2dnZXIgcGx1Z2luIGxvZ2dlclxuICogQHJldHVybiB7e2RpcmVjdG9yaWVzOiBbXSwgZmlsZXM6IFtdfX0gb2JqZWN0IGNvbnRhaW5pbmcgZGlyZWN0b3JpZXMgdG8gY3JlYXRlIGFuZCBmaWxlcyB0byBjb3B5XG4gKi9cbmZ1bmN0aW9uIGNvbGxlY3RGb2xkZXJzKGZvbGRlclRvQ29weSwgbG9nZ2VyKSB7XG4gIGNvbnN0IGNvbGxlY3Rpb24gPSB7IGRpcmVjdG9yaWVzOiBbXSwgZmlsZXM6IFtdIH07XG4gIGxvZ2dlci50cmFjZSgnZmlsZXMgaW4gZGlyZWN0b3J5JywgcmVhZGRpclN5bmMoZm9sZGVyVG9Db3B5KSk7XG4gIHJlYWRkaXJTeW5jKGZvbGRlclRvQ29weSkuZm9yRWFjaCgoZmlsZSkgPT4ge1xuICAgIGNvbnN0IGZpbGVUb0NvcHkgPSByZXNvbHZlKGZvbGRlclRvQ29weSwgZmlsZSk7XG4gICAgdHJ5IHtcbiAgICAgIGlmIChzdGF0U3luYyhmaWxlVG9Db3B5KS5pc0RpcmVjdG9yeSgpKSB7XG4gICAgICAgIGxvZ2dlci5kZWJ1ZygnR29pbmcgdGhyb3VnaCBkaXJlY3RvcnknLCBmaWxlVG9Db3B5KTtcbiAgICAgICAgY29uc3QgcmVzdWx0ID0gY29sbGVjdEZvbGRlcnMoZmlsZVRvQ29weSwgbG9nZ2VyKTtcbiAgICAgICAgaWYgKHJlc3VsdC5maWxlcy5sZW5ndGggPiAwKSB7XG4gICAgICAgICAgY29sbGVjdGlvbi5kaXJlY3Rvcmllcy5wdXNoKGZpbGVUb0NvcHkpO1xuICAgICAgICAgIGxvZ2dlci5kZWJ1ZygnQWRkaW5nIGRpcmVjdG9yeScsIGZpbGVUb0NvcHkpO1xuICAgICAgICAgIGNvbGxlY3Rpb24uZGlyZWN0b3JpZXMucHVzaC5hcHBseShjb2xsZWN0aW9uLmRpcmVjdG9yaWVzLCByZXN1bHQuZGlyZWN0b3JpZXMpO1xuICAgICAgICAgIGNvbGxlY3Rpb24uZmlsZXMucHVzaC5hcHBseShjb2xsZWN0aW9uLmZpbGVzLCByZXN1bHQuZmlsZXMpO1xuICAgICAgICB9XG4gICAgICB9IGVsc2UgaWYgKCFpZ25vcmVkRmlsZUV4dGVuc2lvbnMuaW5jbHVkZXMoZXh0bmFtZShmaWxlVG9Db3B5KSkpIHtcbiAgICAgICAgbG9nZ2VyLmRlYnVnKCdBZGRpbmcgZmlsZScsIGZpbGVUb0NvcHkpO1xuICAgICAgICBjb2xsZWN0aW9uLmZpbGVzLnB1c2goZmlsZVRvQ29weSk7XG4gICAgICB9XG4gICAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICAgIGhhbmRsZU5vU3VjaEZpbGVFcnJvcihmaWxlVG9Db3B5LCBlcnJvciwgbG9nZ2VyKTtcbiAgICB9XG4gIH0pO1xuICByZXR1cm4gY29sbGVjdGlvbjtcbn1cblxuLyoqXG4gKiBDb3B5IGFueSBzdGF0aWMgbm9kZV9tb2R1bGVzIGFzc2V0cyBtYXJrZWQgaW4gdGhlbWUuanNvbiB0b1xuICogcHJvamVjdCBzdGF0aWMgYXNzZXRzIGZvbGRlci5cbiAqXG4gKiBUaGUgdGhlbWUuanNvbiBjb250ZW50IGZvciBhc3NldHMgaXMgc2V0IHVwIGFzOlxuICoge1xuICogICBhc3NldHM6IHtcbiAqICAgICBcIm5vZGVfbW9kdWxlIGlkZW50aWZpZXJcIjoge1xuICogICAgICAgXCJjb3B5LXJ1bGVcIjogXCJ0YXJnZXQvZm9sZGVyXCIsXG4gKiAgICAgfVxuICogICB9XG4gKiB9XG4gKlxuICogVGhpcyB3b3VsZCBtZWFuIHRoYXQgYW4gYXNzZXQgd291bGQgYmUgYnVpbHQgYXM6XG4gKiBcIkBmb3J0YXdlc29tZS9mb250YXdlc29tZS1mcmVlXCI6IHtcbiAqICAgXCJzdmdzL3JlZ3VsYXIvKipcIjogXCJmb3J0YXdlc29tZS9pY29uc1wiXG4gKiB9XG4gKiBXaGVyZSAnQGZvcnRhd2Vzb21lL2ZvbnRhd2Vzb21lLWZyZWUnIGlzIHRoZSBucG0gcGFja2FnZSwgJ3N2Z3MvcmVndWxhci8qKicgaXMgd2hhdCBzaG91bGQgYmUgY29waWVkXG4gKiBhbmQgJ2ZvcnRhd2Vzb21lL2ljb25zJyBpcyB0aGUgdGFyZ2V0IGRpcmVjdG9yeSB1bmRlciBwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyIHdoZXJlIHRoaW5nc1xuICogd2lsbCBnZXQgY29waWVkIHRvLlxuICpcbiAqIE5vdGUhIHRoZXJlIGNhbiBiZSBtdWx0aXBsZSBjb3B5LXJ1bGVzIHdpdGggdGFyZ2V0IGZvbGRlcnMgZm9yIG9uZSBucG0gcGFja2FnZSBhc3NldC5cbiAqXG4gKiBAcGFyYW0ge3N0cmluZ30gdGhlbWVOYW1lIG5hbWUgb2YgdGhlIHRoZW1lIHdlIGFyZSBjb3B5aW5nIGFzc2V0cyBmb3JcbiAqIEBwYXJhbSB7anNvbn0gdGhlbWVQcm9wZXJ0aWVzIHRoZW1lIHByb3BlcnRpZXMganNvbiB3aXRoIGRhdGEgb24gYXNzZXRzXG4gKiBAcGFyYW0ge3N0cmluZ30gcHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciBwcm9qZWN0IG91dHB1dCBmb2xkZXIgd2hlcmUgd2UgY29weSBhc3NldHMgdG8gdW5kZXIgdGhlbWUvW3RoZW1lTmFtZV1cbiAqIEBwYXJhbSB7b2JqZWN0fSBsb2dnZXIgcGx1Z2luIGxvZ2dlclxuICovXG5mdW5jdGlvbiBjb3B5U3RhdGljQXNzZXRzKHRoZW1lTmFtZSwgdGhlbWVQcm9wZXJ0aWVzLCBwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCBsb2dnZXIpIHtcbiAgY29uc3QgYXNzZXRzID0gdGhlbWVQcm9wZXJ0aWVzWydhc3NldHMnXTtcbiAgaWYgKCFhc3NldHMpIHtcbiAgICBsb2dnZXIuZGVidWcoJ25vIGFzc2V0cyB0byBoYW5kbGUgbm8gc3RhdGljIGFzc2V0cyB3ZXJlIGNvcGllZCcpO1xuICAgIHJldHVybjtcbiAgfVxuXG4gIG1rZGlyU3luYyhwcm9qZWN0U3RhdGljQXNzZXRzT3V0cHV0Rm9sZGVyLCB7XG4gICAgcmVjdXJzaXZlOiB0cnVlXG4gIH0pO1xuICBjb25zdCBtaXNzaW5nTW9kdWxlcyA9IGNoZWNrTW9kdWxlcyhPYmplY3Qua2V5cyhhc3NldHMpKTtcbiAgaWYgKG1pc3NpbmdNb2R1bGVzLmxlbmd0aCA+IDApIHtcbiAgICB0aHJvdyBFcnJvcihcbiAgICAgIFwiTWlzc2luZyBucG0gbW9kdWxlcyAnXCIgK1xuICAgICAgICBtaXNzaW5nTW9kdWxlcy5qb2luKFwiJywgJ1wiKSArXG4gICAgICAgIFwiJyBmb3IgYXNzZXRzIG1hcmtlZCBpbiAndGhlbWUuanNvbicuXFxuXCIgK1xuICAgICAgICBcIkluc3RhbGwgcGFja2FnZShzKSBieSBhZGRpbmcgYSBATnBtUGFja2FnZSBhbm5vdGF0aW9uIG9yIGluc3RhbGwgaXQgdXNpbmcgJ25wbS9wbnBtL2J1biBpJ1wiXG4gICAgKTtcbiAgfVxuICBPYmplY3Qua2V5cyhhc3NldHMpLmZvckVhY2goKG1vZHVsZSkgPT4ge1xuICAgIGNvbnN0IGNvcHlSdWxlcyA9IGFzc2V0c1ttb2R1bGVdO1xuICAgIE9iamVjdC5rZXlzKGNvcHlSdWxlcykuZm9yRWFjaCgoY29weVJ1bGUpID0+IHtcbiAgICAgIGNvbnN0IG5vZGVTb3VyY2VzID0gcmVzb2x2ZSgnbm9kZV9tb2R1bGVzLycsIG1vZHVsZSwgY29weVJ1bGUpO1xuICAgICAgY29uc3QgZmlsZXMgPSBnbG9iU3luYyhub2RlU291cmNlcywgeyBub2RpcjogdHJ1ZSB9KTtcbiAgICAgIGNvbnN0IHRhcmdldEZvbGRlciA9IHJlc29sdmUocHJvamVjdFN0YXRpY0Fzc2V0c091dHB1dEZvbGRlciwgJ3RoZW1lcycsIHRoZW1lTmFtZSwgY29weVJ1bGVzW2NvcHlSdWxlXSk7XG5cbiAgICAgIG1rZGlyU3luYyh0YXJnZXRGb2xkZXIsIHtcbiAgICAgICAgcmVjdXJzaXZlOiB0cnVlXG4gICAgICB9KTtcbiAgICAgIGZpbGVzLmZvckVhY2goKGZpbGUpID0+IHtcbiAgICAgICAgY29uc3QgY29weVRhcmdldCA9IHJlc29sdmUodGFyZ2V0Rm9sZGVyLCBiYXNlbmFtZShmaWxlKSk7XG4gICAgICAgIGNvcHlGaWxlSWZBYnNlbnRPck5ld2VyKGZpbGUsIGNvcHlUYXJnZXQsIGxvZ2dlcik7XG4gICAgICB9KTtcbiAgICB9KTtcbiAgfSk7XG59XG5cbmZ1bmN0aW9uIGNoZWNrTW9kdWxlcyhtb2R1bGVzKSB7XG4gIGNvbnN0IG1pc3NpbmcgPSBbXTtcblxuICBtb2R1bGVzLmZvckVhY2goKG1vZHVsZSkgPT4ge1xuICAgIGlmICghZXhpc3RzU3luYyhyZXNvbHZlKCdub2RlX21vZHVsZXMvJywgbW9kdWxlKSkpIHtcbiAgICAgIG1pc3NpbmcucHVzaChtb2R1bGUpO1xuICAgIH1cbiAgfSk7XG5cbiAgcmV0dXJuIG1pc3Npbmc7XG59XG5cbi8qKlxuICogQ29waWVzIGdpdmVuIGZpbGUgdG8gYSBnaXZlbiB0YXJnZXQgcGF0aCwgaWYgdGFyZ2V0IGZpbGUgZG9lc24ndCBleGlzdCBvciBpZlxuICogZmlsZSB0byBjb3B5IGlzIG5ld2VyLlxuICogQHBhcmFtIHtzdHJpbmd9IGZpbGVUb0NvcHkgcGF0aCBvZiB0aGUgZmlsZSB0byBjb3B5XG4gKiBAcGFyYW0ge3N0cmluZ30gY29weVRhcmdldCBwYXRoIG9mIHRoZSB0YXJnZXQgZmlsZVxuICogQHBhcmFtIHtvYmplY3R9IGxvZ2dlciBwbHVnaW4gbG9nZ2VyXG4gKi9cbmZ1bmN0aW9uIGNvcHlGaWxlSWZBYnNlbnRPck5ld2VyKGZpbGVUb0NvcHksIGNvcHlUYXJnZXQsIGxvZ2dlcikge1xuICB0cnkge1xuICAgIGlmICghZXhpc3RzU3luYyhjb3B5VGFyZ2V0KSB8fCBzdGF0U3luYyhjb3B5VGFyZ2V0KS5tdGltZSA8IHN0YXRTeW5jKGZpbGVUb0NvcHkpLm10aW1lKSB7XG4gICAgICBsb2dnZXIudHJhY2UoJ0NvcHlpbmc6ICcsIGZpbGVUb0NvcHksICc9PicsIGNvcHlUYXJnZXQpO1xuICAgICAgY29weUZpbGVTeW5jKGZpbGVUb0NvcHksIGNvcHlUYXJnZXQpO1xuICAgIH1cbiAgfSBjYXRjaCAoZXJyb3IpIHtcbiAgICBoYW5kbGVOb1N1Y2hGaWxlRXJyb3IoZmlsZVRvQ29weSwgZXJyb3IsIGxvZ2dlcik7XG4gIH1cbn1cblxuLy8gSWdub3JlcyBlcnJvcnMgZHVlIHRvIGZpbGUgbWlzc2luZyBkdXJpbmcgdGhlbWUgcHJvY2Vzc2luZ1xuLy8gVGhpcyBtYXkgaGFwcGVuIGZvciBleGFtcGxlIHdoZW4gYW4gSURFIGNyZWF0ZXMgYSB0ZW1wb3JhcnkgZmlsZVxuLy8gYW5kIHRoZW4gaW1tZWRpYXRlbHkgZGVsZXRlcyBpdFxuZnVuY3Rpb24gaGFuZGxlTm9TdWNoRmlsZUVycm9yKGZpbGUsIGVycm9yLCBsb2dnZXIpIHtcbiAgaWYgKGVycm9yLmNvZGUgPT09ICdFTk9FTlQnKSB7XG4gICAgbG9nZ2VyLndhcm4oJ0lnbm9yaW5nIG5vdCBleGlzdGluZyBmaWxlICcgKyBmaWxlICsgJy4gRmlsZSBtYXkgaGF2ZSBiZWVuIGRlbGV0ZWQgZHVyaW5nIHRoZW1lIHByb2Nlc3NpbmcuJyk7XG4gIH0gZWxzZSB7XG4gICAgdGhyb3cgZXJyb3I7XG4gIH1cbn1cblxuZXhwb3J0IHsgY2hlY2tNb2R1bGVzLCBjb3B5U3RhdGljQXNzZXRzLCBjb3B5VGhlbWVSZXNvdXJjZXMgfTtcbiIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcYnVpbGRcXFxccGx1Z2luc1xcXFx0aGVtZS1sb2FkZXJcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXHV0ZW50ZVxcXFxJZGVhUHJvamVjdHNcXFxcZHVlZW1tZXN0b3JlXFxcXGJ1aWxkXFxcXHBsdWdpbnNcXFxcdGhlbWUtbG9hZGVyXFxcXHRoZW1lLWxvYWRlci11dGlscy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvdXRlbnRlL0lkZWFQcm9qZWN0cy9kdWVlbW1lc3RvcmUvYnVpbGQvcGx1Z2lucy90aGVtZS1sb2FkZXIvdGhlbWUtbG9hZGVyLXV0aWxzLmpzXCI7aW1wb3J0IHsgZXhpc3RzU3luYywgcmVhZEZpbGVTeW5jIH0gZnJvbSAnZnMnO1xuaW1wb3J0IHsgcmVzb2x2ZSwgYmFzZW5hbWUgfSBmcm9tICdwYXRoJztcbmltcG9ydCB7IGdsb2JTeW5jIH0gZnJvbSAnZ2xvYic7XG5cbi8vIENvbGxlY3QgZ3JvdXBzIFt1cmwoXSBbJ3xcIl1vcHRpb25hbCAnLi98Li4vJywgb3RoZXIgJy4uLycgc2VnbWVudHMgb3B0aW9uYWwsIGZpbGUgcGFydCBhbmQgZW5kIG9mIHVybFxuLy8gVGhlIGFkZGl0aW9uYWwgZG90IHNlZ21lbnRzIGNvdWxkIGJlIFVSTCByZWZlcmVuY2luZyBhc3NldHMgaW4gbmVzdGVkIGltcG9ydGVkIENTU1xuLy8gV2hlbiBWaXRlIGlubGluZXMgQ1NTIGltcG9ydCBpdCBkb2VzIG5vdCByZXdyaXRlIHJlbGF0aXZlIFVSTCBmb3Igbm90LXJlc29sdmFibGUgcmVzb3VyY2Vcbi8vIHNvIHRoZSBmaW5hbCBDU1MgZW5kcyB1cCB3aXRoIHdyb25nIHJlbGF0aXZlIFVSTHMgKHIuZy4gLi4vLi4vcGtnL2ljb24uc3ZnKVxuLy8gSWYgdGhlIFVSTCBpcyByZWxhdGl2ZSwgd2Ugc2hvdWxkIHRyeSB0byBjaGVjayBpZiBpdCBpcyBhbiBhc3NldCBieSBpZ25vcmluZyB0aGUgYWRkaXRpb25hbCBkb3Qgc2VnbWVudHNcbmNvbnN0IHVybE1hdGNoZXIgPSAvKHVybFxcKFxccyopKFxcJ3xcXFwiKT8oXFwuXFwvfFxcLlxcLlxcLykoKD86XFwzKSopPyhcXFMqKShcXDJcXHMqXFwpKS9nO1xuXG5mdW5jdGlvbiBhc3NldHNDb250YWlucyhmaWxlVXJsLCB0aGVtZUZvbGRlciwgbG9nZ2VyKSB7XG4gIGNvbnN0IHRoZW1lUHJvcGVydGllcyA9IGdldFRoZW1lUHJvcGVydGllcyh0aGVtZUZvbGRlcik7XG4gIGlmICghdGhlbWVQcm9wZXJ0aWVzKSB7XG4gICAgbG9nZ2VyLmRlYnVnKCdObyB0aGVtZSBwcm9wZXJ0aWVzIGZvdW5kLicpO1xuICAgIHJldHVybiBmYWxzZTtcbiAgfVxuICBjb25zdCBhc3NldHMgPSB0aGVtZVByb3BlcnRpZXNbJ2Fzc2V0cyddO1xuICBpZiAoIWFzc2V0cykge1xuICAgIGxvZ2dlci5kZWJ1ZygnTm8gZGVmaW5lZCBhc3NldHMgaW4gdGhlbWUgcHJvcGVydGllcycpO1xuICAgIHJldHVybiBmYWxzZTtcbiAgfVxuICAvLyBHbyB0aHJvdWdoIGVhY2ggYXNzZXQgbW9kdWxlXG4gIGZvciAobGV0IG1vZHVsZSBvZiBPYmplY3Qua2V5cyhhc3NldHMpKSB7XG4gICAgY29uc3QgY29weVJ1bGVzID0gYXNzZXRzW21vZHVsZV07XG4gICAgLy8gR28gdGhyb3VnaCBlYWNoIGNvcHkgcnVsZVxuICAgIGZvciAobGV0IGNvcHlSdWxlIG9mIE9iamVjdC5rZXlzKGNvcHlSdWxlcykpIHtcbiAgICAgIC8vIGlmIGZpbGUgc3RhcnRzIHdpdGggY29weVJ1bGUgdGFyZ2V0IGNoZWNrIGlmIGZpbGUgd2l0aCBwYXRoIGFmdGVyIGNvcHkgdGFyZ2V0IGNhbiBiZSBmb3VuZFxuICAgICAgaWYgKGZpbGVVcmwuc3RhcnRzV2l0aChjb3B5UnVsZXNbY29weVJ1bGVdKSkge1xuICAgICAgICBjb25zdCB0YXJnZXRGaWxlID0gZmlsZVVybC5yZXBsYWNlKGNvcHlSdWxlc1tjb3B5UnVsZV0sICcnKTtcbiAgICAgICAgY29uc3QgZmlsZXMgPSBnbG9iU3luYyhyZXNvbHZlKCdub2RlX21vZHVsZXMvJywgbW9kdWxlLCBjb3B5UnVsZSksIHsgbm9kaXI6IHRydWUgfSk7XG5cbiAgICAgICAgZm9yIChsZXQgZmlsZSBvZiBmaWxlcykge1xuICAgICAgICAgIGlmIChmaWxlLmVuZHNXaXRoKHRhcmdldEZpbGUpKSByZXR1cm4gdHJ1ZTtcbiAgICAgICAgfVxuICAgICAgfVxuICAgIH1cbiAgfVxuICByZXR1cm4gZmFsc2U7XG59XG5cbmZ1bmN0aW9uIGdldFRoZW1lUHJvcGVydGllcyh0aGVtZUZvbGRlcikge1xuICBjb25zdCB0aGVtZVByb3BlcnR5RmlsZSA9IHJlc29sdmUodGhlbWVGb2xkZXIsICd0aGVtZS5qc29uJyk7XG4gIGlmICghZXhpc3RzU3luYyh0aGVtZVByb3BlcnR5RmlsZSkpIHtcbiAgICByZXR1cm4ge307XG4gIH1cbiAgY29uc3QgdGhlbWVQcm9wZXJ0eUZpbGVBc1N0cmluZyA9IHJlYWRGaWxlU3luYyh0aGVtZVByb3BlcnR5RmlsZSk7XG4gIGlmICh0aGVtZVByb3BlcnR5RmlsZUFzU3RyaW5nLmxlbmd0aCA9PT0gMCkge1xuICAgIHJldHVybiB7fTtcbiAgfVxuICByZXR1cm4gSlNPTi5wYXJzZSh0aGVtZVByb3BlcnR5RmlsZUFzU3RyaW5nKTtcbn1cblxuZnVuY3Rpb24gcmV3cml0ZUNzc1VybHMoc291cmNlLCBoYW5kbGVkUmVzb3VyY2VGb2xkZXIsIHRoZW1lRm9sZGVyLCBsb2dnZXIsIG9wdGlvbnMpIHtcbiAgc291cmNlID0gc291cmNlLnJlcGxhY2UodXJsTWF0Y2hlciwgZnVuY3Rpb24gKG1hdGNoLCB1cmwsIHF1b3RlTWFyaywgcmVwbGFjZSwgYWRkaXRpb25hbERvdFNlZ21lbnRzLCBmaWxlVXJsLCBlbmRTdHJpbmcpIHtcbiAgICBsZXQgYWJzb2x1dGVQYXRoID0gcmVzb2x2ZShoYW5kbGVkUmVzb3VyY2VGb2xkZXIsIHJlcGxhY2UsIGFkZGl0aW9uYWxEb3RTZWdtZW50cyB8fCAnJywgZmlsZVVybCk7XG4gICAgbGV0IGV4aXN0aW5nVGhlbWVSZXNvdXJjZSA9IGFic29sdXRlUGF0aC5zdGFydHNXaXRoKHRoZW1lRm9sZGVyKSAmJiBleGlzdHNTeW5jKGFic29sdXRlUGF0aCk7XG4gICAgaWYgKCFleGlzdGluZ1RoZW1lUmVzb3VyY2UgJiYgYWRkaXRpb25hbERvdFNlZ21lbnRzKSB7XG4gICAgICAvLyBUcnkgdG8gcmVzb2x2ZSBwYXRoIHdpdGhvdXQgZG90IHNlZ21lbnRzIGFzIGl0IG1heSBiZSBhbiB1bnJlc29sdmFibGVcbiAgICAgIC8vIHJlbGF0aXZlIFVSTCBmcm9tIGFuIGlubGluZWQgbmVzdGVkIENTU1xuICAgICAgYWJzb2x1dGVQYXRoID0gcmVzb2x2ZShoYW5kbGVkUmVzb3VyY2VGb2xkZXIsIHJlcGxhY2UsIGZpbGVVcmwpO1xuICAgICAgZXhpc3RpbmdUaGVtZVJlc291cmNlID0gYWJzb2x1dGVQYXRoLnN0YXJ0c1dpdGgodGhlbWVGb2xkZXIpICYmIGV4aXN0c1N5bmMoYWJzb2x1dGVQYXRoKTtcbiAgICB9XG4gICAgY29uc3QgaXNBc3NldCA9IGFzc2V0c0NvbnRhaW5zKGZpbGVVcmwsIHRoZW1lRm9sZGVyLCBsb2dnZXIpO1xuICAgIGlmIChleGlzdGluZ1RoZW1lUmVzb3VyY2UgfHwgaXNBc3NldCkge1xuICAgICAgLy8gQWRkaW5nIC4vIHdpbGwgc2tpcCBjc3MtbG9hZGVyLCB3aGljaCBzaG91bGQgYmUgZG9uZSBmb3IgYXNzZXQgZmlsZXNcbiAgICAgIC8vIEluIGEgcHJvZHVjdGlvbiBidWlsZCwgdGhlIGNzcyBmaWxlIGlzIGluIFZBQURJTi9idWlsZCBhbmQgc3RhdGljIGZpbGVzIGFyZSBpbiBWQUFESU4vc3RhdGljLCBzbyAuLi9zdGF0aWMgbmVlZHMgdG8gYmUgYWRkZWRcbiAgICAgIGNvbnN0IHJlcGxhY2VtZW50ID0gb3B0aW9ucy5kZXZNb2RlID8gJy4vJyA6ICcuLi9zdGF0aWMvJztcblxuICAgICAgY29uc3Qgc2tpcExvYWRlciA9IGV4aXN0aW5nVGhlbWVSZXNvdXJjZSA/ICcnIDogcmVwbGFjZW1lbnQ7XG4gICAgICBjb25zdCBmcm9udGVuZFRoZW1lRm9sZGVyID0gc2tpcExvYWRlciArICd0aGVtZXMvJyArIGJhc2VuYW1lKHRoZW1lRm9sZGVyKTtcbiAgICAgIGxvZ2dlci5sb2coXG4gICAgICAgICdVcGRhdGluZyB1cmwgZm9yIGZpbGUnLFxuICAgICAgICBcIidcIiArIHJlcGxhY2UgKyBmaWxlVXJsICsgXCInXCIsXG4gICAgICAgICd0byB1c2UnLFxuICAgICAgICBcIidcIiArIGZyb250ZW5kVGhlbWVGb2xkZXIgKyAnLycgKyBmaWxlVXJsICsgXCInXCJcbiAgICAgICk7XG4gICAgICAvLyBhc3NldHMgYXJlIGFsd2F5cyByZWxhdGl2ZSB0byB0aGVtZSBmb2xkZXJcbiAgICAgIGNvbnN0IHBhdGhSZXNvbHZlZCA9IGlzQXNzZXQgPyAnLycgKyBmaWxlVXJsXG4gICAgICAgICAgOiBhYnNvbHV0ZVBhdGguc3Vic3RyaW5nKHRoZW1lRm9sZGVyLmxlbmd0aCkucmVwbGFjZSgvXFxcXC9nLCAnLycpO1xuXG4gICAgICAvLyBrZWVwIHRoZSB1cmwgdGhlIHNhbWUgZXhjZXB0IHJlcGxhY2UgdGhlIC4vIG9yIC4uLyB0byB0aGVtZXMvW3RoZW1lRm9sZGVyXVxuICAgICAgcmV0dXJuIHVybCArIChxdW90ZU1hcmsgPz8gJycpICsgZnJvbnRlbmRUaGVtZUZvbGRlciArIHBhdGhSZXNvbHZlZCArIGVuZFN0cmluZztcbiAgICB9IGVsc2UgaWYgKG9wdGlvbnMuZGV2TW9kZSkge1xuICAgICAgbG9nZ2VyLmxvZyhcIk5vIHJld3JpdGUgZm9yICdcIiwgbWF0Y2gsIFwiJyBhcyB0aGUgZmlsZSB3YXMgbm90IGZvdW5kLlwiKTtcbiAgICB9IGVsc2Uge1xuICAgICAgLy8gSW4gcHJvZHVjdGlvbiwgdGhlIGNzcyBpcyBpbiBWQUFESU4vYnVpbGQgYnV0IHRoZSB0aGVtZSBmaWxlcyBhcmUgaW4gLlxuICAgICAgcmV0dXJuIHVybCArIChxdW90ZU1hcmsgPz8gJycpICsgJy4uLy4uLycgKyBmaWxlVXJsICsgZW5kU3RyaW5nO1xuICAgIH1cbiAgICByZXR1cm4gbWF0Y2g7XG4gIH0pO1xuICByZXR1cm4gc291cmNlO1xufVxuXG5leHBvcnQgeyByZXdyaXRlQ3NzVXJscyB9O1xuIiwgImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFx1dGVudGVcXFxcSWRlYVByb2plY3RzXFxcXGR1ZWVtbWVzdG9yZVxcXFxidWlsZFxcXFxwbHVnaW5zXFxcXHJlYWN0LWZ1bmN0aW9uLWxvY2F0aW9uLXBsdWdpblwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcYnVpbGRcXFxccGx1Z2luc1xcXFxyZWFjdC1mdW5jdGlvbi1sb2NhdGlvbi1wbHVnaW5cXFxccmVhY3QtZnVuY3Rpb24tbG9jYXRpb24tcGx1Z2luLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy91dGVudGUvSWRlYVByb2plY3RzL2R1ZWVtbWVzdG9yZS9idWlsZC9wbHVnaW5zL3JlYWN0LWZ1bmN0aW9uLWxvY2F0aW9uLXBsdWdpbi9yZWFjdC1mdW5jdGlvbi1sb2NhdGlvbi1wbHVnaW4uanNcIjtpbXBvcnQgKiBhcyB0IGZyb20gJ0BiYWJlbC90eXBlcyc7XG5cbmV4cG9ydCBmdW5jdGlvbiBhZGRGdW5jdGlvbkNvbXBvbmVudFNvdXJjZUxvY2F0aW9uQmFiZWwoKSB7XG4gIGZ1bmN0aW9uIGlzUmVhY3RGdW5jdGlvbk5hbWUobmFtZSkge1xuICAgIC8vIEEgUmVhY3QgY29tcG9uZW50IGZ1bmN0aW9uIGFsd2F5cyBzdGFydHMgd2l0aCBhIENhcGl0YWwgbGV0dGVyXG4gICAgcmV0dXJuIG5hbWUgJiYgbmFtZS5tYXRjaCgvXltBLVpdLiovKTtcbiAgfVxuXG4gIC8qKlxuICAgKiBXcml0ZXMgZGVidWcgaW5mbyBhcyBOYW1lLl9fZGVidWdTb3VyY2VEZWZpbmU9ey4uLn0gYWZ0ZXIgdGhlIGdpdmVuIHN0YXRlbWVudCAoXCJwYXRoXCIpLlxuICAgKiBUaGlzIGlzIHVzZWQgdG8gbWFrZSB0aGUgc291cmNlIGxvY2F0aW9uIG9mIHRoZSBmdW5jdGlvbiAoZGVmaW5lZCBieSB0aGUgbG9jIHBhcmFtZXRlcikgYXZhaWxhYmxlIGluIHRoZSBicm93c2VyIGluIGRldmVsb3BtZW50IG1vZGUuXG4gICAqIFRoZSBuYW1lIF9fZGVidWdTb3VyY2VEZWZpbmUgaXMgcHJlZml4ZWQgYnkgX18gdG8gbWFyayB0aGlzIGlzIG5vdCBhIHB1YmxpYyBBUEkuXG4gICAqL1xuICBmdW5jdGlvbiBhZGREZWJ1Z0luZm8ocGF0aCwgbmFtZSwgZmlsZW5hbWUsIGxvYykge1xuICAgIGNvbnN0IGxpbmVOdW1iZXIgPSBsb2Muc3RhcnQubGluZTtcbiAgICBjb25zdCBjb2x1bW5OdW1iZXIgPSBsb2Muc3RhcnQuY29sdW1uICsgMTtcbiAgICBjb25zdCBkZWJ1Z1NvdXJjZU1lbWJlciA9IHQubWVtYmVyRXhwcmVzc2lvbih0LmlkZW50aWZpZXIobmFtZSksIHQuaWRlbnRpZmllcignX19kZWJ1Z1NvdXJjZURlZmluZScpKTtcbiAgICBjb25zdCBkZWJ1Z1NvdXJjZURlZmluZSA9IHQub2JqZWN0RXhwcmVzc2lvbihbXG4gICAgICB0Lm9iamVjdFByb3BlcnR5KHQuaWRlbnRpZmllcignZmlsZU5hbWUnKSwgdC5zdHJpbmdMaXRlcmFsKGZpbGVuYW1lKSksXG4gICAgICB0Lm9iamVjdFByb3BlcnR5KHQuaWRlbnRpZmllcignbGluZU51bWJlcicpLCB0Lm51bWVyaWNMaXRlcmFsKGxpbmVOdW1iZXIpKSxcbiAgICAgIHQub2JqZWN0UHJvcGVydHkodC5pZGVudGlmaWVyKCdjb2x1bW5OdW1iZXInKSwgdC5udW1lcmljTGl0ZXJhbChjb2x1bW5OdW1iZXIpKVxuICAgIF0pO1xuICAgIGNvbnN0IGFzc2lnbm1lbnQgPSB0LmV4cHJlc3Npb25TdGF0ZW1lbnQodC5hc3NpZ25tZW50RXhwcmVzc2lvbignPScsIGRlYnVnU291cmNlTWVtYmVyLCBkZWJ1Z1NvdXJjZURlZmluZSkpO1xuICAgIGNvbnN0IGNvbmRpdGlvbiA9IHQuYmluYXJ5RXhwcmVzc2lvbihcbiAgICAgICc9PT0nLFxuICAgICAgdC51bmFyeUV4cHJlc3Npb24oJ3R5cGVvZicsIHQuaWRlbnRpZmllcihuYW1lKSksXG4gICAgICB0LnN0cmluZ0xpdGVyYWwoJ2Z1bmN0aW9uJylcbiAgICApO1xuICAgIGNvbnN0IGlmRnVuY3Rpb24gPSB0LmlmU3RhdGVtZW50KGNvbmRpdGlvbiwgdC5ibG9ja1N0YXRlbWVudChbYXNzaWdubWVudF0pKTtcbiAgICBwYXRoLmluc2VydEFmdGVyKGlmRnVuY3Rpb24pO1xuICB9XG5cbiAgcmV0dXJuIHtcbiAgICB2aXNpdG9yOiB7XG4gICAgICBWYXJpYWJsZURlY2xhcmF0aW9uKHBhdGgsIHN0YXRlKSB7XG4gICAgICAgIC8vIEZpbmRzIGRlY2xhcmF0aW9ucyBzdWNoIGFzXG4gICAgICAgIC8vIGNvbnN0IEZvbyA9ICgpID0+IDxkaXYvPlxuICAgICAgICAvLyBleHBvcnQgY29uc3QgQmFyID0gKCkgPT4gPHNwYW4vPlxuXG4gICAgICAgIC8vIGFuZCB3cml0ZXMgYSBGb28uX19kZWJ1Z1NvdXJjZURlZmluZT0gey4ufSBhZnRlciBpdCwgcmVmZXJyaW5nIHRvIHRoZSBzdGFydCBvZiB0aGUgZnVuY3Rpb24gYm9keVxuICAgICAgICBwYXRoLm5vZGUuZGVjbGFyYXRpb25zLmZvckVhY2goKGRlY2xhcmF0aW9uKSA9PiB7XG4gICAgICAgICAgaWYgKGRlY2xhcmF0aW9uLmlkLnR5cGUgIT09ICdJZGVudGlmaWVyJykge1xuICAgICAgICAgICAgcmV0dXJuO1xuICAgICAgICAgIH1cbiAgICAgICAgICBjb25zdCBuYW1lID0gZGVjbGFyYXRpb24/LmlkPy5uYW1lO1xuICAgICAgICAgIGlmICghaXNSZWFjdEZ1bmN0aW9uTmFtZShuYW1lKSkge1xuICAgICAgICAgICAgcmV0dXJuO1xuICAgICAgICAgIH1cblxuICAgICAgICAgIGNvbnN0IGZpbGVuYW1lID0gc3RhdGUuZmlsZS5vcHRzLmZpbGVuYW1lO1xuICAgICAgICAgIGlmIChkZWNsYXJhdGlvbj8uaW5pdD8uYm9keT8ubG9jKSB7XG4gICAgICAgICAgICBhZGREZWJ1Z0luZm8ocGF0aCwgbmFtZSwgZmlsZW5hbWUsIGRlY2xhcmF0aW9uLmluaXQuYm9keS5sb2MpO1xuICAgICAgICAgIH1cbiAgICAgICAgfSk7XG4gICAgICB9LFxuXG4gICAgICBGdW5jdGlvbkRlY2xhcmF0aW9uKHBhdGgsIHN0YXRlKSB7XG4gICAgICAgIC8vIEZpbmRzIGRlY2xhcmF0aW9ucyBzdWNoIGFzXG4gICAgICAgIC8vIGZ1bmN0aW8gRm9vKCkgeyByZXR1cm4gPGRpdi8+OyB9XG4gICAgICAgIC8vIGV4cG9ydCBmdW5jdGlvbiBCYXIoKSB7IHJldHVybiA8c3Bhbj5IZWxsbzwvc3Bhbj47fVxuXG4gICAgICAgIC8vIGFuZCB3cml0ZXMgYSBGb28uX19kZWJ1Z1NvdXJjZURlZmluZT0gey4ufSBhZnRlciBpdCwgcmVmZXJyaW5nIHRvIHRoZSBzdGFydCBvZiB0aGUgZnVuY3Rpb24gYm9keVxuICAgICAgICBjb25zdCBub2RlID0gcGF0aC5ub2RlO1xuICAgICAgICBjb25zdCBuYW1lID0gbm9kZT8uaWQ/Lm5hbWU7XG4gICAgICAgIGlmICghaXNSZWFjdEZ1bmN0aW9uTmFtZShuYW1lKSkge1xuICAgICAgICAgIHJldHVybjtcbiAgICAgICAgfVxuICAgICAgICBjb25zdCBmaWxlbmFtZSA9IHN0YXRlLmZpbGUub3B0cy5maWxlbmFtZTtcbiAgICAgICAgYWRkRGVidWdJbmZvKHBhdGgsIG5hbWUsIGZpbGVuYW1lLCBub2RlLmJvZHkubG9jKTtcbiAgICAgIH1cbiAgICB9XG4gIH07XG59XG4iLCAie1xuICBcImZyb250ZW5kRm9sZGVyXCI6IFwiQzovVXNlcnMvdXRlbnRlL0lkZWFQcm9qZWN0cy9kdWVlbW1lc3RvcmUvLi9zcmMvbWFpbi9mcm9udGVuZFwiLFxuICBcInRoZW1lRm9sZGVyXCI6IFwidGhlbWVzXCIsXG4gIFwidGhlbWVSZXNvdXJjZUZvbGRlclwiOiBcIkM6L1VzZXJzL3V0ZW50ZS9JZGVhUHJvamVjdHMvZHVlZW1tZXN0b3JlLy4vc3JjL21haW4vZnJvbnRlbmQvZ2VuZXJhdGVkL2phci1yZXNvdXJjZXNcIixcbiAgXCJzdGF0aWNPdXRwdXRcIjogXCJDOi9Vc2Vycy91dGVudGUvSWRlYVByb2plY3RzL2R1ZWVtbWVzdG9yZS9idWlsZC9jbGFzc2VzL01FVEEtSU5GL1ZBQURJTi93ZWJhcHAvVkFBRElOL3N0YXRpY1wiLFxuICBcImdlbmVyYXRlZEZvbGRlclwiOiBcImdlbmVyYXRlZFwiLFxuICBcInN0YXRzT3V0cHV0XCI6IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcYnVpbGRcXFxcY2xhc3Nlc1xcXFxNRVRBLUlORlxcXFxWQUFESU5cXFxcY29uZmlnXCIsXG4gIFwiZnJvbnRlbmRCdW5kbGVPdXRwdXRcIjogXCJDOlxcXFxVc2Vyc1xcXFx1dGVudGVcXFxcSWRlYVByb2plY3RzXFxcXGR1ZWVtbWVzdG9yZVxcXFxidWlsZFxcXFxjbGFzc2VzXFxcXE1FVEEtSU5GXFxcXFZBQURJTlxcXFx3ZWJhcHBcIixcbiAgXCJkZXZCdW5kbGVPdXRwdXRcIjogXCJDOi9Vc2Vycy91dGVudGUvSWRlYVByb2plY3RzL2R1ZWVtbWVzdG9yZS9idWlsZC9kZXYtYnVuZGxlL3dlYmFwcFwiLFxuICBcImRldkJ1bmRsZVN0YXRzT3V0cHV0XCI6IFwiQzovVXNlcnMvdXRlbnRlL0lkZWFQcm9qZWN0cy9kdWVlbW1lc3RvcmUvYnVpbGQvZGV2LWJ1bmRsZS9jb25maWdcIixcbiAgXCJqYXJSZXNvdXJjZXNGb2xkZXJcIjogXCJDOi9Vc2Vycy91dGVudGUvSWRlYVByb2plY3RzL2R1ZWVtbWVzdG9yZS8uL3NyYy9tYWluL2Zyb250ZW5kL2dlbmVyYXRlZC9qYXItcmVzb3VyY2VzXCIsXG4gIFwidGhlbWVOYW1lXCI6IFwiam1peHByb2dldHRvc3R1ZGlvXCIsXG4gIFwiY2xpZW50U2VydmljZVdvcmtlclNvdXJjZVwiOiBcIkM6XFxcXFVzZXJzXFxcXHV0ZW50ZVxcXFxJZGVhUHJvamVjdHNcXFxcZHVlZW1tZXN0b3JlXFxcXGJ1aWxkXFxcXHN3LnRzXCIsXG4gIFwicHdhRW5hYmxlZFwiOiB0cnVlLFxuICBcIm9mZmxpbmVFbmFibGVkXCI6IHRydWUsXG4gIFwib2ZmbGluZVBhdGhcIjogXCInLidcIlxufSIsICJjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZGlybmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcYnVpbGRcXFxccGx1Z2luc1xcXFxyb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LWN1c3RvbVwiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9maWxlbmFtZSA9IFwiQzpcXFxcVXNlcnNcXFxcdXRlbnRlXFxcXElkZWFQcm9qZWN0c1xcXFxkdWVlbW1lc3RvcmVcXFxcYnVpbGRcXFxccGx1Z2luc1xcXFxyb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LWN1c3RvbVxcXFxyb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0LmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9DOi9Vc2Vycy91dGVudGUvSWRlYVByb2plY3RzL2R1ZWVtbWVzdG9yZS9idWlsZC9wbHVnaW5zL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQtY3VzdG9tL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQuanNcIjsvKipcbiAqIE1JVCBMaWNlbnNlXG5cbkNvcHlyaWdodCAoYykgMjAxOSBVbWJlcnRvIFBlcGF0b1xuXG5QZXJtaXNzaW9uIGlzIGhlcmVieSBncmFudGVkLCBmcmVlIG9mIGNoYXJnZSwgdG8gYW55IHBlcnNvbiBvYnRhaW5pbmcgYSBjb3B5XG5vZiB0aGlzIHNvZnR3YXJlIGFuZCBhc3NvY2lhdGVkIGRvY3VtZW50YXRpb24gZmlsZXMgKHRoZSBcIlNvZnR3YXJlXCIpLCB0byBkZWFsXG5pbiB0aGUgU29mdHdhcmUgd2l0aG91dCByZXN0cmljdGlvbiwgaW5jbHVkaW5nIHdpdGhvdXQgbGltaXRhdGlvbiB0aGUgcmlnaHRzXG50byB1c2UsIGNvcHksIG1vZGlmeSwgbWVyZ2UsIHB1Ymxpc2gsIGRpc3RyaWJ1dGUsIHN1YmxpY2Vuc2UsIGFuZC9vciBzZWxsXG5jb3BpZXMgb2YgdGhlIFNvZnR3YXJlLCBhbmQgdG8gcGVybWl0IHBlcnNvbnMgdG8gd2hvbSB0aGUgU29mdHdhcmUgaXNcbmZ1cm5pc2hlZCB0byBkbyBzbywgc3ViamVjdCB0byB0aGUgZm9sbG93aW5nIGNvbmRpdGlvbnM6XG5cblRoZSBhYm92ZSBjb3B5cmlnaHQgbm90aWNlIGFuZCB0aGlzIHBlcm1pc3Npb24gbm90aWNlIHNoYWxsIGJlIGluY2x1ZGVkIGluIGFsbFxuY29waWVzIG9yIHN1YnN0YW50aWFsIHBvcnRpb25zIG9mIHRoZSBTb2Z0d2FyZS5cblxuVEhFIFNPRlRXQVJFIElTIFBST1ZJREVEIFwiQVMgSVNcIiwgV0lUSE9VVCBXQVJSQU5UWSBPRiBBTlkgS0lORCwgRVhQUkVTUyBPUlxuSU1QTElFRCwgSU5DTFVESU5HIEJVVCBOT1QgTElNSVRFRCBUTyBUSEUgV0FSUkFOVElFUyBPRiBNRVJDSEFOVEFCSUxJVFksXG5GSVRORVNTIEZPUiBBIFBBUlRJQ1VMQVIgUFVSUE9TRSBBTkQgTk9OSU5GUklOR0VNRU5ULiBJTiBOTyBFVkVOVCBTSEFMTCBUSEVcbkFVVEhPUlMgT1IgQ09QWVJJR0hUIEhPTERFUlMgQkUgTElBQkxFIEZPUiBBTlkgQ0xBSU0sIERBTUFHRVMgT1IgT1RIRVJcbkxJQUJJTElUWSwgV0hFVEhFUiBJTiBBTiBBQ1RJT04gT0YgQ09OVFJBQ1QsIFRPUlQgT1IgT1RIRVJXSVNFLCBBUklTSU5HIEZST00sXG5PVVQgT0YgT1IgSU4gQ09OTkVDVElPTiBXSVRIIFRIRSBTT0ZUV0FSRSBPUiBUSEUgVVNFIE9SIE9USEVSIERFQUxJTkdTIElOIFRIRVxuU09GVFdBUkUuXG4gKi9cbi8vIFRoaXMgaXMgaHR0cHM6Ly9naXRodWIuY29tL3VtYm9wZXBhdG8vcm9sbHVwLXBsdWdpbi1wb3N0Y3NzLWxpdCAyLjAuMCArIGh0dHBzOi8vZ2l0aHViLmNvbS91bWJvcGVwYXRvL3JvbGx1cC1wbHVnaW4tcG9zdGNzcy1saXQvcHVsbC81NFxuLy8gdG8gbWFrZSBpdCB3b3JrIHdpdGggVml0ZSAzXG4vLyBPbmNlIC8gaWYgaHR0cHM6Ly9naXRodWIuY29tL3VtYm9wZXBhdG8vcm9sbHVwLXBsdWdpbi1wb3N0Y3NzLWxpdC9wdWxsLzU0IGlzIG1lcmdlZCB0aGlzIHNob3VsZCBiZSByZW1vdmVkIGFuZCByb2xsdXAtcGx1Z2luLXBvc3Rjc3MtbGl0IHNob3VsZCBiZSB1c2VkIGluc3RlYWRcblxuaW1wb3J0IHsgY3JlYXRlRmlsdGVyIH0gZnJvbSAnQHJvbGx1cC9wbHVnaW51dGlscyc7XG5pbXBvcnQgdHJhbnNmb3JtQXN0IGZyb20gJ3RyYW5zZm9ybS1hc3QnO1xuXG5jb25zdCBhc3NldFVybFJFID0gL19fVklURV9BU1NFVF9fKFtcXHckXSspX18oPzpcXCRfKC4qPylfXyk/L2dcblxuY29uc3QgZXNjYXBlID0gKHN0cikgPT5cbiAgc3RyXG4gICAgLnJlcGxhY2UoYXNzZXRVcmxSRSwgJyR7dW5zYWZlQ1NTVGFnKFwiX19WSVRFX0FTU0VUX18kMV9fJDJcIil9JylcbiAgICAucmVwbGFjZSgvYC9nLCAnXFxcXGAnKVxuICAgIC5yZXBsYWNlKC9cXFxcKD8hYCkvZywgJ1xcXFxcXFxcJyk7XG5cbmV4cG9ydCBkZWZhdWx0IGZ1bmN0aW9uIHBvc3Rjc3NMaXQob3B0aW9ucyA9IHt9KSB7XG4gIGNvbnN0IGRlZmF1bHRPcHRpb25zID0ge1xuICAgIGluY2x1ZGU6ICcqKi8qLntjc3Msc3NzLHBjc3Msc3R5bCxzdHlsdXMsc2FzcyxzY3NzLGxlc3N9JyxcbiAgICBleGNsdWRlOiBudWxsLFxuICAgIGltcG9ydFBhY2thZ2U6ICdsaXQnXG4gIH07XG5cbiAgY29uc3Qgb3B0cyA9IHsgLi4uZGVmYXVsdE9wdGlvbnMsIC4uLm9wdGlvbnMgfTtcbiAgY29uc3QgZmlsdGVyID0gY3JlYXRlRmlsdGVyKG9wdHMuaW5jbHVkZSwgb3B0cy5leGNsdWRlKTtcblxuICByZXR1cm4ge1xuICAgIG5hbWU6ICdwb3N0Y3NzLWxpdCcsXG4gICAgZW5mb3JjZTogJ3Bvc3QnLFxuICAgIHRyYW5zZm9ybShjb2RlLCBpZCkge1xuICAgICAgaWYgKCFmaWx0ZXIoaWQpKSByZXR1cm47XG4gICAgICBjb25zdCBhc3QgPSB0aGlzLnBhcnNlKGNvZGUsIHt9KTtcbiAgICAgIC8vIGV4cG9ydCBkZWZhdWx0IGNvbnN0IGNzcztcbiAgICAgIGxldCBkZWZhdWx0RXhwb3J0TmFtZTtcblxuICAgICAgLy8gZXhwb3J0IGRlZmF1bHQgJy4uLic7XG4gICAgICBsZXQgaXNEZWNsYXJhdGlvbkxpdGVyYWwgPSBmYWxzZTtcbiAgICAgIGNvbnN0IG1hZ2ljU3RyaW5nID0gdHJhbnNmb3JtQXN0KGNvZGUsIHsgYXN0OiBhc3QgfSwgKG5vZGUpID0+IHtcbiAgICAgICAgaWYgKG5vZGUudHlwZSA9PT0gJ0V4cG9ydERlZmF1bHREZWNsYXJhdGlvbicpIHtcbiAgICAgICAgICBkZWZhdWx0RXhwb3J0TmFtZSA9IG5vZGUuZGVjbGFyYXRpb24ubmFtZTtcblxuICAgICAgICAgIGlzRGVjbGFyYXRpb25MaXRlcmFsID0gbm9kZS5kZWNsYXJhdGlvbi50eXBlID09PSAnTGl0ZXJhbCc7XG4gICAgICAgIH1cbiAgICAgIH0pO1xuXG4gICAgICBpZiAoIWRlZmF1bHRFeHBvcnROYW1lICYmICFpc0RlY2xhcmF0aW9uTGl0ZXJhbCkge1xuICAgICAgICByZXR1cm47XG4gICAgICB9XG4gICAgICBtYWdpY1N0cmluZy53YWxrKChub2RlKSA9PiB7XG4gICAgICAgIGlmIChkZWZhdWx0RXhwb3J0TmFtZSAmJiBub2RlLnR5cGUgPT09ICdWYXJpYWJsZURlY2xhcmF0aW9uJykge1xuICAgICAgICAgIGNvbnN0IGV4cG9ydGVkVmFyID0gbm9kZS5kZWNsYXJhdGlvbnMuZmluZCgoZCkgPT4gZC5pZC5uYW1lID09PSBkZWZhdWx0RXhwb3J0TmFtZSk7XG4gICAgICAgICAgaWYgKGV4cG9ydGVkVmFyKSB7XG4gICAgICAgICAgICBleHBvcnRlZFZhci5pbml0LmVkaXQudXBkYXRlKGBjc3NUYWdcXGAke2VzY2FwZShleHBvcnRlZFZhci5pbml0LnZhbHVlKX1cXGBgKTtcbiAgICAgICAgICB9XG4gICAgICAgIH1cblxuICAgICAgICBpZiAoaXNEZWNsYXJhdGlvbkxpdGVyYWwgJiYgbm9kZS50eXBlID09PSAnRXhwb3J0RGVmYXVsdERlY2xhcmF0aW9uJykge1xuICAgICAgICAgIG5vZGUuZGVjbGFyYXRpb24uZWRpdC51cGRhdGUoYGNzc1RhZ1xcYCR7ZXNjYXBlKG5vZGUuZGVjbGFyYXRpb24udmFsdWUpfVxcYGApO1xuICAgICAgICB9XG4gICAgICB9KTtcbiAgICAgIG1hZ2ljU3RyaW5nLnByZXBlbmQoYGltcG9ydCB7Y3NzIGFzIGNzc1RhZywgdW5zYWZlQ1NTIGFzIHVuc2FmZUNTU1RhZ30gZnJvbSAnJHtvcHRzLmltcG9ydFBhY2thZ2V9JztcXG5gKTtcbiAgICAgIHJldHVybiB7XG4gICAgICAgIGNvZGU6IG1hZ2ljU3RyaW5nLnRvU3RyaW5nKCksXG4gICAgICAgIG1hcDogbWFnaWNTdHJpbmcuZ2VuZXJhdGVNYXAoe1xuICAgICAgICAgIGhpcmVzOiB0cnVlXG4gICAgICAgIH0pXG4gICAgICB9O1xuICAgIH1cbiAgfTtcbn07XG4iLCAiY29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2Rpcm5hbWUgPSBcIkM6XFxcXFVzZXJzXFxcXHV0ZW50ZVxcXFxJZGVhUHJvamVjdHNcXFxcZHVlZW1tZXN0b3JlXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJDOlxcXFxVc2Vyc1xcXFx1dGVudGVcXFxcSWRlYVByb2plY3RzXFxcXGR1ZWVtbWVzdG9yZVxcXFx2aXRlLmNvbmZpZy50c1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vQzovVXNlcnMvdXRlbnRlL0lkZWFQcm9qZWN0cy9kdWVlbW1lc3RvcmUvdml0ZS5jb25maWcudHNcIjtpbXBvcnQgeyBVc2VyQ29uZmlnRm4gfSBmcm9tICd2aXRlJztcbmltcG9ydCB7IG92ZXJyaWRlVmFhZGluQ29uZmlnIH0gZnJvbSAnLi92aXRlLmdlbmVyYXRlZCc7XG5cbmNvbnN0IGN1c3RvbUNvbmZpZzogVXNlckNvbmZpZ0ZuID0gKGVudikgPT4gKHtcbiAgLy8gSGVyZSB5b3UgY2FuIGFkZCBjdXN0b20gVml0ZSBwYXJhbWV0ZXJzXG4gIC8vIGh0dHBzOi8vdml0ZWpzLmRldi9jb25maWcvXG59KTtcblxuZXhwb3J0IGRlZmF1bHQgb3ZlcnJpZGVWYWFkaW5Db25maWcoY3VzdG9tQ29uZmlnKTtcbiJdLAogICJtYXBwaW5ncyI6ICI7QUFNQSxPQUFPLFVBQVU7QUFDakIsU0FBUyxjQUFBQSxhQUFZLGFBQUFDLFlBQVcsZUFBQUMsY0FBYSxnQkFBQUMsZUFBYyxpQkFBQUMsc0JBQTRCO0FBQ3ZGLFNBQVMsa0JBQWtCO0FBQzNCLFlBQVksU0FBUzs7O0FDV3JCLFNBQVMsY0FBQUMsYUFBWSxnQkFBQUMscUJBQW9CO0FBQ3pDLFNBQVMsV0FBQUMsZ0JBQWU7OztBQ0R4QixTQUFTLFlBQUFDLGlCQUFnQjtBQUN6QixTQUFTLFdBQUFDLFVBQVMsWUFBQUMsaUJBQWdCO0FBQ2xDLFNBQVMsY0FBQUMsYUFBWSxjQUFjLHFCQUFxQjs7O0FDRnhELFNBQVMsYUFBYSxVQUFVLFdBQVcsWUFBWSxvQkFBb0I7QUFDM0UsU0FBUyxTQUFTLFVBQVUsVUFBVSxlQUFlO0FBQ3JELFNBQVMsZ0JBQWdCO0FBRXpCLElBQU0sd0JBQXdCLENBQUMsUUFBUSxPQUFPLE9BQU87QUFXckQsU0FBUyxtQkFBbUJDLGNBQWEsaUNBQWlDLFFBQVE7QUFDaEYsUUFBTSwwQkFBMEIsUUFBUSxpQ0FBaUMsVUFBVSxTQUFTQSxZQUFXLENBQUM7QUFDeEcsUUFBTSxhQUFhLGVBQWVBLGNBQWEsTUFBTTtBQUdyRCxNQUFJLFdBQVcsTUFBTSxTQUFTLEdBQUc7QUFDL0IsY0FBVSx5QkFBeUIsRUFBRSxXQUFXLEtBQUssQ0FBQztBQUV0RCxlQUFXLFlBQVksUUFBUSxDQUFDLGNBQWM7QUFDNUMsWUFBTSxvQkFBb0IsU0FBU0EsY0FB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a0IsV0FBVyxFQUFFLFlBQVksTUFBTSxVQUFVLGVBQWUsQ0FBQztBQUFBLElBRTdFO0FBQUEsRUFDRjtBQUNGO0FBRU8sSUFBTSx1QkFBdUIsQ0FBQ0Msa0JBQStCO0FBQ2xFLFNBQU8sYUFBYSxDQUFDLFFBQVEsWUFBWSxhQUFhLEdBQUcsR0FBR0EsY0FBYSxHQUFHLENBQUMsQ0FBQztBQUNoRjtBQUNBLFNBQVMsV0FBVyxRQUF3QjtBQUMxQyxRQUFNLGNBQWMsS0FBSyxRQUFRLG1CQUFtQixRQUFRLGNBQWM7QUFDMUUsU0FBTyxLQUFLLE1BQU1SLGNBQWEsYUFBYSxFQUFFLFVBQVUsUUFBUSxDQUFDLENBQUMsRUFBRTtBQUN0RTtBQUNBLFNBQVMsWUFBWSxRQUF3QjtBQUMzQyxRQUFNLGNBQWMsS0FBSyxRQUFRLG1CQUFtQixRQUFRLGNBQWM7QUFDMUUsU0FBTyxLQUFLLE1BQU1BLGNBQWEsYUFBYSxFQUFFLFVBQVUsUUFBUSxDQUFDLENBQUMsRUFBRTtBQUN0RTs7O0FRaDJCQSxJQUFNLGVBQTZCLENBQUMsU0FBUztBQUFBO0FBQUE7QUFHN0M7QUFFQSxJQUFPLHNCQUFRLHFCQUFxQixZQUFZOyIsCiAgIm5hbWVzIjogWyJleGlzdHNTeW5jIiwgIm1rZGlyU3luYyIsICJyZWFkZGlyU3luYyIsICJyZWFkRmlsZVN5bmMiLCAid3JpdGVGaWxlU3luYyIsICJleGlzdHNTeW5jIiwgInJlYWRGaWxlU3luYyIsICJyZXNvbHZlIiwgImdsb2JTeW5jIiwgInJlc29sdmUiLCAiYmFzZW5hbWUiLCAiZXhpc3RzU3luYyIsICJ0aGVtZUZvbGRlciIsICJ0aGVtZUZvbGRlciIsICJyZXNvbHZlIiwgImdsb2JTeW5jIiwgImV4aXN0c1N5bmMiLCAiYmFzZW5hbWUiLCAidmFyaWFibGUiLCAiZmlsZW5hbWUiLCAiZXhpc3RzU3luYyIsICJyZXNvbHZlIiwgInRoZW1lRm9sZGVyIiwgInJlYWRGaWxlU3luYyIsICJleGlzdHNTeW5jIiwgInJlYWRGaWxlU3luYyIsICJyZXNvbHZlIiwgImJhc2VuYW1lIiwgImdsb2JTeW5jIiwgInRoZW1lRm9sZGVyIiwgImdldFRoZW1lUHJvcGVydGllcyIsICJnbG9iU3luYyIsICJyZXNvbHZlIiwgImV4aXN0c1N5bmMiLCAicmVhZEZpbGVTeW5jIiwgInJlcGxhY2UiLCAiYmFzZW5hbWUiLCAicGF0aCIsICJyZXF1aXJlIiwgImV4aXN0c1N5bmMiLCAicmVhZEZpbGVTeW5jIiwgIm1rZGlyU3luYyIsICJidW5kbGUiLCAicmVhZGRpclN5bmMiLCAidGhlbWVGb2xkZXIiLCAid3JpdGVGaWxlU3luYyIsICJlIiwgInBhdGgiLCAiY3VzdG9tQ29uZmlnIl0KfQo=
