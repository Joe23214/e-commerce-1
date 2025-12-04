try {
  self["workbox:core:7.2.0"] && _();
} catch (e) {
}
const logger = null;
const fallback = (code, ...args) => {
  let msg = code;
  if (args.length > 0) {
    msg += ` :: ${JSON.stringify(args)}`;
  }
  return msg;
};
const messageGenerator = fallback;
class WorkboxError extends Error {
  /**
   *
   * @param {string} errorCode The error code that
   * identifies this particular error.
   * @param {Object=} details Any relevant arguments
   * that will help developers identify issues should
   * be added as a key on the context object.
   */
  constructor(errorCode, details) {
    const message = messageGenerator(errorCode, details);
    super(message);
    this.name = errorCode;
    this.details = details;
  }
}
const quotaErrorCallbacks = /* @__PURE__ */ new Set();
const _cacheNameDetails = {
  googleAnalytics: "googleAnalytics",
  precache: "precache-v2",
  prefix: "workbox",
  runtime: "runtime",
  suffix: typeof registration !== "undefined" ? registration.scope : ""
};
const _createCacheName = (cacheName) => {
  return [_cacheNameDetails.prefix, cacheName, _cacheNameDetails.suffix].filter((value) => value && value.length > 0).join("-");
};
const eachCacheNameDetail = (fn) => {
  for (const key of Object.keys(_cacheNameDetails)) {
    fn(key);
  }
};
const cacheNames = {
  updateDetails: (details) => {
    eachCacheNameDetail((key) => {
      if (typeof details[key] === "string") {
        _cacheNameDetails[key] = details[key];
      }
    });
  },
  getGoogleAnalyticsName: (userCacheName) => {
    return userCacheName || _createCacheName(_cacheNameDetails.googleAnalytics);
  },
  getPrecacheName: (userCacheName) => {
    return userCacheName || _createCacheName(_cacheNameDetails.precache);
  },
  getPrefix: () => {
    return _cacheNameDetails.prefix;
  },
  getRuntimeName: (userCacheName) => {
    return userCacheName || _createCacheName(_cacheNameDetails.runtime);
  },
  getSuffix: () => {
    return _cacheNameDetails.suffix;
  }
};
function stripParams(fullURL, ignoreParams) {
  const strippedURL = new URL(fullURL);
  for (const param of ignoreParams) {
    strippedURL.searchParams.delete(param);
  }
  return strippedURL.href;
}
async function cacheMatchIgnoreParams(cache, request, ignoreParams, matchOptions) {
  const strippedRequestURL = stripParams(request.url, ignoreParams);
  if (request.url === strippedRequestURL) {
    return cache.match(request, matchOptions);
  }
  const keysOptions = Object.assign(Object.assign({}, matchOptions), { ignoreSearch: true });
  const cacheKeys = await cache.keys(request, keysOptions);
  for (const cacheKey of cacheKeys) {
    const strippedCacheKeyURL = stripParams(cacheKey.url, ignoreParams);
    if (strippedRequestURL === strippedCacheKeyURL) {
      return cache.match(cacheKey, matchOptions);
    }
  }
  return;
}
let supportStatus;
function canConstructResponseFromBodyStream() {
  if (supportStatus === void 0) {
    const testResponse = new Response("");
    if ("body" in testResponse) {
      try {
        new Response(testResponse.body);
        supportStatus = true;
      } catch (error) {
        supportStatus = false;
      }
    }
    supportStatus = false;
  }
  return supportStatus;
}
class Deferred {
  /**
   * Creates a promise and exposes its resolve and reject functions as methods.
   */
  constructor() {
    this.promise = new Promise((resolve, reject) => {
      this.resolve = resolve;
      this.reject = reject;
    });
  }
}
async function executeQuotaErrorCallbacks() {
  for (const callback of quotaErrorCallbacks) {
    await callback();
  }
}
const getFriendlyURL = (url) => {
  const urlObj = new URL(String(url), location.href);
  return urlObj.href.replace(new RegExp(`^${location.origin}`), "");
};
function timeout(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
function waitUntil(event, asyncFn) {
  const returnPromise = asyncFn();
  event.waitUntil(returnPromise);
  return returnPromise;
}
async function copyResponse(response, modifier) {
  let origin = null;
  if (response.url) {
    const responseURL = new URL(response.url);
    origin = responseURL.origin;
  }
  if (origin !== self.location.origin) {
    throw new WorkboxError("cross-origin-copy-response", { origin });
  }
  const clonedResponse = response.clone();
  const responseInit = {
    headers: new Headers(clonedResponse.headers),
    status: clonedResponse.status,
    statusText: clonedResponse.statusText
  };
  const modifiedResponseInit = modifier ? modifier(responseInit) : responseInit;
  const body = canConstructResponseFromBodyStream() ? clonedResponse.body : await clonedResponse.blob();
  return new Response(body, modifiedResponseInit);
}
function clientsClaim() {
  self.addEventListener("activate", () => self.clients.claim());
}
try {
  self["workbox:precaching:7.2.0"] && _();
} catch (e) {
}
const REVISION_SEARCH_PARAM = "__WB_REVISION__";
function createCacheKey(entry) {
  if (!entry) {
    throw new WorkboxError("add-to-cache-list-unexpected-type", { entry });
  }
  if (typeof entry === "string") {
    const urlObject = new URL(entry, location.href);
    return {
      cacheKey: urlObject.href,
      url: urlObject.href
    };
  }
  const { revision, url } = entry;
  if (!url) {
    throw new WorkboxError("add-to-cache-list-unexpected-type", { entry });
  }
  if (!revision) {
    const urlObject = new URL(url, location.href);
    return {
      cacheKey: urlObject.href,
      url: urlObject.href
    };
  }
  const cacheKeyURL = new URL(url, location.href);
  const originalURL = new URL(url, location.href);
  cacheKeyURL.searchParams.set(REVISION_SEARCH_PARAM, revision);
  return {
    cacheKey: cacheKeyURL.href,
    url: originalURL.href
  };
}
class PrecacheInstallReportPlugin {
  constructor() {
    this.updatedURLs = [];
    this.notUpdatedURLs = [];
    this.handlerWillStart = async ({ request, state }) => {
      if (state) {
        state.originalRequest = request;
      }
    };
    this.cachedResponseWillBeUsed = async ({ event, state, cachedResponse }) => {
      if (event.type === "install") {
        if (state && state.originalRequest && state.originalRequest instanceof Request) {
          const url = state.originalRequest.url;
          if (cachedResponse) {
            this.notUpdatedURLs.push(url);
          } else {
            this.updatedURLs.push(url);
          }
        }
      }
      return cachedResponse;
    };
  }
}
class PrecacheCacheKeyPlugin {
  constructor({ precacheController: precacheController2 }) {
    this.cacheKeyWillBeUsed = async ({ request, params }) => {
      const cacheKey = (params === null || params === void 0 ? void 0 : params.cacheKey) || this._precacheController.getCacheKeyForURL(request.url);
      return cacheKey ? new Request(cacheKey, { headers: request.headers }) : request;
    };
    this._precacheController = precacheController2;
  }
}
try {
  self["workbox:strategies:7.0.0"] && _();
} catch (e) {
}
function toRequest(input) {
  return typeof input === "string" ? new Request(input) : input;
}
class StrategyHandler {
  /**
   * Creates a new instance associated with the passed strategy and event
   * that's handling the request.
   *
   * The constructor also initializes the state that will be passed to each of
   * the plugins handling this request.
   *
   * @param {workbox-strategies.Strategy} strategy
   * @param {Object} options
   * @param {Request|string} options.request A request to run this strategy for.
   * @param {ExtendableEvent} options.event The event associated with the
   *     request.
   * @param {URL} [options.url]
   * @param {*} [options.params] The return value from the
   *     {@link workbox-routing~matchCallback} (if applicable).
   */
  constructor(strategy, options) {
    this._cacheKeys = {};
    Object.assign(this, options);
    this.event = options.event;
    this._strategy = strategy;
    this._handlerDeferred = new Deferred();
    this._extendLifetimePromises = [];
    this._plugins = [...strategy.plugins];
    this._pluginStateMap = /* @__PURE__ */ new Map();
    for (const plugin of this._plugins) {
      this._pluginStateMap.set(plugin, {});
    }
    this.event.waitUntil(this._handlerDeferred.promise);
  }
  /**
   * Fetches a given request (and invokes any applicable plugin callback
   * methods) using the `fetchOptions` (for non-navigation requests) and
   * `plugins` defined on the `Strategy` object.
   *
   * The following plugin lifecycle methods are invoked when using this method:
   * - `requestWillFetch()`
   * - `fetchDidSucceed()`
   * - `fetchDidFail()`
   *
   * @param {Request|string} input The URL or request to fetch.
   * @return {Promise<Response>}
   */
  async fetch(input) {
    const { event } = this;
    let request = toRequest(input);
    if (request.mode === "navigate" && event instanceof FetchEvent && event.preloadResponse) {
      const possiblePreloadResponse = await event.preloadResponse;
      if (possiblePreloadResponse) {
        return possiblePreloadResponse;
      }
    }
    const originalRequest = this.hasCallback("fetchDidFail") ? request.clone() : null;
    try {
      for (const cb of this.iterateCallbacks("requestWillFetch")) {
        request = await cb({ request: request.clone(), event });
      }
    } catch (err) {
      if (err instanceof Error) {
        throw new WorkboxError("plugin-error-request-will-fetch", {
          thrownErrorMessage: err.message
        });
      }
    }
    const pluginFilteredRequest = request.clone();
    try {
      let fetchResponse;
      fetchResponse = await fetch(request, request.mode === "navigate" ? void 0 : this._strategy.fetchOptions);
      if (false) ;
      for (const callback of this.iterateCallbacks("fetchDidSucceed")) {
        fetchResponse = await callback({
          event,
          request: pluginFilteredRequest,
          response: fetchResponse
        });
      }
      return fetchResponse;
    } catch (error) {
      if (originalRequest) {
        await this.runCallbacks("fetchDidFail", {
          error,
          event,
          originalRequest: originalRequest.clone(),
          request: pluginFilteredRequest.clone()
        });
      }
      throw error;
    }
  }
  /**
   * Calls `this.fetch()` and (in the background) runs `this.cachePut()` on
   * the response generated by `this.fetch()`.
   *
   * The call to `this.cachePut()` automatically invokes `this.waitUntil()`,
   * so you do not have to manually call `waitUntil()` on the event.
   *
   * @param {Request|string} input The request or URL to fetch and cache.
   * @return {Promise<Response>}
   */
  async fetchAndCachePut(input) {
    const response = await this.fetch(input);
    const responseClone = response.clone();
    void this.waitUntil(this.cachePut(input, responseClone));
    return response;
  }
  /**
   * Matches a request from the cache (and invokes any applicable plugin
   * callback methods) using the `cacheName`, `matchOptions`, and `plugins`
   * defined on the strategy object.
   *
   * The following plugin lifecycle methods are invoked when using this method:
   * - cacheKeyWillByUsed()
   * - cachedResponseWillByUsed()
   *
   * @param {Request|string} key The Request or URL to use as the cache key.
   * @return {Promise<Response|undefined>} A matching response, if found.
   */
  async cacheMatch(key) {
    const request = toRequest(key);
    let cachedResponse;
    const { cacheName, matchOptions } = this._strategy;
    const effectiveRequest = await this.getCacheKey(request, "read");
    const multiMatchOptions = Object.assign(Object.assign({}, matchOptions), { cacheName });
    cachedResponse = await caches.match(effectiveRequest, multiMatchOptions);
    for (const callback of this.iterateCallbacks("cachedResponseWillBeUsed")) {
      cachedResponse = await callback({
        cacheName,
        matchOptions,
        cachedResponse,
        request: effectiveRequest,
        event: this.event
      }) || void 0;
    }
    return cachedResponse;
  }
  /**
   * Puts a request/response pair in the cache (and invokes any applicable
   * plugin callback methods) using the `cacheName` and `plugins` defined on
   * the strategy object.
   *
   * The following plugin lifecycle methods are invoked when using this method:
   * - cacheKeyWillByUsed()
   * - cacheWillUpdate()
   * - cacheDidUpdate()
   *
   * @param {Request|string} key The request or URL to use as the cache key.
   * @param {Response} response The response to cache.
   * @return {Promise<boolean>} `false` if a cacheWillUpdate caused the response
   * not be cached, and `true` otherwise.
   */
  async cachePut(key, response) {
    const request = toRequest(key);
    await timeout(0);
    const effectiveRequest = await this.getCacheKey(request, "write");
    if (!response) {
      throw new WorkboxError("cache-put-with-no-response", {
        url: getFriendlyURL(effectiveRequest.url)
      });
    }
    const responseToCache = await this._ensureResponseSafeToCache(response);
    if (!responseToCache) {
      return false;
    }
    const { cacheName, matchOptions } = this._strategy;
    const cache = await self.caches.open(cacheName);
    const hasCacheUpdateCallback = this.hasCallback("cacheDidUpdate");
    const oldResponse = hasCacheUpdateCallback ? await cacheMatchIgnoreParams(
      // TODO(philipwalton): the `__WB_REVISION__` param is a precaching
      // feature. Consider into ways to only add this behavior if using
      // precaching.
      cache,
      effectiveRequest.clone(),
      ["__WB_REVISION__"],
      matchOptions
    ) : null;
    try {
      await cache.put(effectiveRequest, hasCacheUpdateCallback ? responseToCache.clone() : responseToCache);
    } catch (error) {
      if (error instanceof Error) {
        if (error.name === "QuotaExceededError") {
          await executeQuotaErrorCallbacks();
        }
        throw error;
      }
    }
    for (const callback of this.iterateCallbacks("cacheDidUpdate")) {
      await callback({
        cacheName,
        oldResponse,
        newResponse: responseToCache.clone(),
        request: effectiveRequest,
        event: this.event
      });
    }
    return true;
  }
  /**
   * Checks the list of plugins for the `cacheKeyWillBeUsed` callback, and
   * executes any of those callbacks found in sequence. The final `Request`
   * object returned by the last plugin is treated as the cache key for cache
   * reads and/or writes. If no `cacheKeyWillBeUsed` plugin callbacks have
   * been registered, the passed request is returned unmodified
   *
   * @param {Request} request
   * @param {string} mode
   * @return {Promise<Request>}
   */
  async getCacheKey(request, mode) {
    const key = `${request.url} | ${mode}`;
    if (!this._cacheKeys[key]) {
      let effectiveRequest = request;
      for (const callback of this.iterateCallbacks("cacheKeyWillBeUsed")) {
        effectiveRequest = toRequest(await callback({
          mode,
          request: effectiveRequest,
          event: this.event,
          // params has a type any can't change right now.
          params: this.params
          // eslint-disable-line
        }));
      }
      this._cacheKeys[key] = effectiveRequest;
    }
    return this._cacheKeys[key];
  }
  /**
   * Returns true if the strategy has at least one plugin with the given
   * callback.
   *
   * @param {string} name The name of the callback to check for.
   * @return {boolean}
   */
  hasCallback(name) {
    for (const plugin of this._strategy.plugins) {
      if (name in plugin) {
        return true;
      }
    }
    return false;
  }
  /**
   * Runs all plugin callbacks matching the given name, in order, passing the
   * given param object (merged ith the current plugin state) as the only
   * argument.
   *
   * Note: since this method runs all plugins, it's not suitable for cases
   * where the return value of a callback needs to be applied prior to calling
   * the next callback. See
   * {@link workbox-strategies.StrategyHandler#iterateCallbacks}
   * below for how to handle that case.
   *
   * @param {string} name The name of the callback to run within each plugin.
   * @param {Object} param The object to pass as the first (and only) param
   *     when executing each callback. This object will be merged with the
   *     current plugin state prior to callback execution.
   */
  async runCallbacks(name, param) {
    for (const callback of this.iterateCallbacks(name)) {
      await callback(param);
    }
  }
  /**
   * Accepts a callback and returns an iterable of matching plugin callbacks,
   * where each callback is wrapped with the current handler state (i.e. when
   * you call each callback, whatever object parameter you pass it will
   * be merged with the plugin's current state).
   *
   * @param {string} name The name fo the callback to run
   * @return {Array<Function>}
   */
  *iterateCallbacks(name) {
    for (const plugin of this._strategy.plugins) {
      if (typeof plugin[name] === "function") {
        const state = this._pluginStateMap.get(plugin);
        const statefulCallback = (param) => {
          const statefulParam = Object.assign(Object.assign({}, param), { state });
          return plugin[name](statefulParam);
        };
        yield statefulCallback;
      }
    }
  }
  /**
   * Adds a promise to the
   * [extend lifetime promises]{@link https://w3c.github.io/ServiceWorker/#extendableevent-extend-lifetime-promises}
   * of the event event associated with the request being handled (usually a
   * `FetchEvent`).
   *
   * Note: you can await
   * {@link workbox-strategies.StrategyHandler~doneWaiting}
   * to know when all added promises have settled.
   *
   * @param {Promise} promise A promise to add to the extend lifetime promises
   *     of the event that triggered the request.
   */
  waitUntil(promise) {
    this._extendLifetimePromises.push(promise);
    return promise;
  }
  /**
   * Returns a promise that resolves once all promises passed to
   * {@link workbox-strategies.StrategyHandler~waitUntil}
   * have settled.
   *
   * Note: any work done after `doneWaiting()` settles should be manually
   * passed to an event's `waitUntil()` method (not this handler's
   * `waitUntil()` method), otherwise the service worker thread my be killed
   * prior to your work completing.
   */
  async doneWaiting() {
    let promise;
    while (promise = this._extendLifetimePromises.shift()) {
      await promise;
    }
  }
  /**
   * Stops running the strategy and immediately resolves any pending
   * `waitUntil()` promises.
   */
  destroy() {
    this._handlerDeferred.resolve(null);
  }
  /**
   * This method will call cacheWillUpdate on the available plugins (or use
   * status === 200) to determine if the Response is safe and valid to cache.
   *
   * @param {Request} options.request
   * @param {Response} options.response
   * @return {Promise<Response|undefined>}
   *
   * @private
   */
  async _ensureResponseSafeToCache(response) {
    let responseToCache = response;
    let pluginsUsed = false;
    for (const callback of this.iterateCallbacks("cacheWillUpdate")) {
      responseToCache = await callback({
        request: this.request,
        response: responseToCache,
        event: this.event
      }) || void 0;
      pluginsUsed = true;
      if (!responseToCache) {
        break;
      }
    }
    if (!pluginsUsed) {
      if (responseToCache && responseToCache.status !== 200) {
        responseToCache = void 0;
      }
    }
    return responseToCache;
  }
}
class Strategy {
  /**
   * Creates a new instance of the strategy and sets all documented option
   * properties as public instance properties.
   *
   * Note: if a custom strategy class extends the base Strategy class and does
   * not need more than these properties, it does not need to define its own
   * constructor.
   *
   * @param {Object} [options]
   * @param {string} [options.cacheName] Cache name to store and retrieve
   * requests. Defaults to the cache names provided by
   * {@link workbox-core.cacheNames}.
   * @param {Array<Object>} [options.plugins] [Plugins]{@link https://developers.google.com/web/tools/workbox/guides/using-plugins}
   * to use in conjunction with this caching strategy.
   * @param {Object} [options.fetchOptions] Values passed along to the
   * [`init`](https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch#Parameters)
   * of [non-navigation](https://github.com/GoogleChrome/workbox/issues/1796)
   * `fetch()` requests made by this strategy.
   * @param {Object} [options.matchOptions] The
   * [`CacheQueryOptions`]{@link https://w3c.github.io/ServiceWorker/#dictdef-cachequeryoptions}
   * for any `cache.match()` or `cache.put()` calls made by this strategy.
   */
  constructor(options = {}) {
    this.cacheName = cacheNames.getRuntimeName(options.cacheName);
    this.plugins = options.plugins || [];
    this.fetchOptions = options.fetchOptions;
    this.matchOptions = options.matchOptions;
  }
  /**
   * Perform a request strategy and returns a `Promise` that will resolve with
   * a `Response`, invoking all relevant plugin callbacks.
   *
   * When a strategy instance is registered with a Workbox
   * {@link workbox-routing.Route}, this method is automatically
   * called when the route matches.
   *
   * Alternatively, this method can be used in a standalone `FetchEvent`
   * listener by passing it to `event.respondWith()`.
   *
   * @param {FetchEvent|Object} options A `FetchEvent` or an object with the
   *     properties listed below.
   * @param {Request|string} options.request A request to run this strategy for.
   * @param {ExtendableEvent} options.event The event associated with the
   *     request.
   * @param {URL} [options.url]
   * @param {*} [options.params]
   */
  handle(options) {
    const [responseDone] = this.handleAll(options);
    return responseDone;
  }
  /**
   * Similar to {@link workbox-strategies.Strategy~handle}, but
   * instead of just returning a `Promise` that resolves to a `Response` it
   * it will return an tuple of `[response, done]` promises, where the former
   * (`response`) is equivalent to what `handle()` returns, and the latter is a
   * Promise that will resolve once any promises that were added to
   * `event.waitUntil()` as part of performing the strategy have completed.
   *
   * You can await the `done` promise to ensure any extra work performed by
   * the strategy (usually caching responses) completes successfully.
   *
   * @param {FetchEvent|Object} options A `FetchEvent` or an object with the
   *     properties listed below.
   * @param {Request|string} options.request A request to run this strategy for.
   * @param {ExtendableEvent} options.event The event associated with the
   *     request.
   * @param {URL} [options.url]
   * @param {*} [options.params]
   * @return {Array<Promise>} A tuple of [response, done]
   *     promises that can be used to determine when the response resolves as
   *     well as when the handler has completed all its work.
   */
  handleAll(options) {
    if (options instanceof FetchEvent) {
      options = {
        event: options,
        request: options.request
      };
    }
    const event = options.event;
    const request = typeof options.request === "string" ? new Request(options.request) : options.request;
    const params = "params" in options ? options.params : void 0;
    const handler = new StrategyHandler(this, { event, request, params });
    const responseDone = this._getResponse(handler, request, event);
    const handlerDone = this._awaitComplete(responseDone, handler, request, event);
    return [responseDone, handlerDone];
  }
  async _getResponse(handler, request, event) {
    await handler.runCallbacks("handlerWillStart", { event, request });
    let response = void 0;
    try {
      response = await this._handle(request, handler);
      if (!response || response.type === "error") {
        throw new WorkboxError("no-response", { url: request.url });
      }
    } catch (error) {
      if (error instanceof Error) {
        for (const callback of handler.iterateCallbacks("handlerDidError")) {
          response = await callback({ error, event, request });
          if (response) {
            break;
          }
        }
      }
      if (!response) {
        throw error;
      }
    }
    for (const callback of handler.iterateCallbacks("handlerWillRespond")) {
      response = await callback({ event, request, response });
    }
    return response;
  }
  async _awaitComplete(responseDone, handler, request, event) {
    let response;
    let error;
    try {
      response = await responseDone;
    } catch (error2) {
    }
    try {
      await handler.runCallbacks("handlerDidRespond", {
        event,
        request,
        response
      });
      await handler.doneWaiting();
    } catch (waitUntilError) {
      if (waitUntilError instanceof Error) {
        error = waitUntilError;
      }
    }
    await handler.runCallbacks("handlerDidComplete", {
      event,
      request,
      response,
      error
    });
    handler.destroy();
    if (error) {
      throw error;
    }
  }
}
class PrecacheStrategy extends Strategy {
  /**
   *
   * @param {Object} [options]
   * @param {string} [options.cacheName] Cache name to store and retrieve
   * requests. Defaults to the cache names provided by
   * {@link workbox-core.cacheNames}.
   * @param {Array<Object>} [options.plugins] {@link https://developers.google.com/web/tools/workbox/guides/using-plugins|Plugins}
   * to use in conjunction with this caching strategy.
   * @param {Object} [options.fetchOptions] Values passed along to the
   * {@link https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch#Parameters|init}
   * of all fetch() requests made by this strategy.
   * @param {Object} [options.matchOptions] The
   * {@link https://w3c.github.io/ServiceWorker/#dictdef-cachequeryoptions|CacheQueryOptions}
   * for any `cache.match()` or `cache.put()` calls made by this strategy.
   * @param {boolean} [options.fallbackToNetwork=true] Whether to attempt to
   * get the response from the network if there's a precache miss.
   */
  constructor(options = {}) {
    options.cacheName = cacheNames.getPrecacheName(options.cacheName);
    super(options);
    this._fallbackToNetwork = options.fallbackToNetwork === false ? false : true;
    this.plugins.push(PrecacheStrategy.copyRedirectedCacheableResponsesPlugin);
  }
  /**
   * @private
   * @param {Request|string} request A request to run this strategy for.
   * @param {workbox-strategies.StrategyHandler} handler The event that
   *     triggered the request.
   * @return {Promise<Response>}
   */
  async _handle(request, handler) {
    const response = await handler.cacheMatch(request);
    if (response) {
      return response;
    }
    if (handler.event && handler.event.type === "install") {
      return await this._handleInstall(request, handler);
    }
    return await this._handleFetch(request, handler);
  }
  async _handleFetch(request, handler) {
    let response;
    const params = handler.params || {};
    if (this._fallbackToNetwork) {
      const integrityInManifest = params.integrity;
      const integrityInRequest = request.integrity;
      const noIntegrityConflict = !integrityInRequest || integrityInRequest === integrityInManifest;
      response = await handler.fetch(new Request(request, {
        integrity: request.mode !== "no-cors" ? integrityInRequest || integrityInManifest : void 0
      }));
      if (integrityInManifest && noIntegrityConflict && request.mode !== "no-cors") {
        this._useDefaultCacheabilityPluginIfNeeded();
        await handler.cachePut(request, response.clone());
      }
    } else {
      throw new WorkboxError("missing-precache-entry", {
        cacheName: this.cacheName,
        url: request.url
      });
    }
    return response;
  }
  async _handleInstall(request, handler) {
    this._useDefaultCacheabilityPluginIfNeeded();
    const response = await handler.fetch(request);
    const wasCached = await handler.cachePut(request, response.clone());
    if (!wasCached) {
      throw new WorkboxError("bad-precaching-response", {
        url: request.url,
        status: response.status
      });
    }
    return response;
  }
  /**
   * This method is complex, as there a number of things to account for:
   *
   * The `plugins` array can be set at construction, and/or it might be added to
   * to at any time before the strategy is used.
   *
   * At the time the strategy is used (i.e. during an `install` event), there
   * needs to be at least one plugin that implements `cacheWillUpdate` in the
   * array, other than `copyRedirectedCacheableResponsesPlugin`.
   *
   * - If this method is called and there are no suitable `cacheWillUpdate`
   * plugins, we need to add `defaultPrecacheCacheabilityPlugin`.
   *
   * - If this method is called and there is exactly one `cacheWillUpdate`, then
   * we don't have to do anything (this might be a previously added
   * `defaultPrecacheCacheabilityPlugin`, or it might be a custom plugin).
   *
   * - If this method is called and there is more than one `cacheWillUpdate`,
   * then we need to check if one is `defaultPrecacheCacheabilityPlugin`. If so,
   * we need to remove it. (This situation is unlikely, but it could happen if
   * the strategy is used multiple times, the first without a `cacheWillUpdate`,
   * and then later on after manually adding a custom `cacheWillUpdate`.)
   *
   * See https://github.com/GoogleChrome/workbox/issues/2737 for more context.
   *
   * @private
   */
  _useDefaultCacheabilityPluginIfNeeded() {
    let defaultPluginIndex = null;
    let cacheWillUpdatePluginCount = 0;
    for (const [index, plugin] of this.plugins.entries()) {
      if (plugin === PrecacheStrategy.copyRedirectedCacheableResponsesPlugin) {
        continue;
      }
      if (plugin === PrecacheStrategy.defaultPrecacheCacheabilityPlugin) {
        defaultPluginIndex = index;
      }
      if (plugin.cacheWillUpdate) {
        cacheWillUpdatePluginCount++;
      }
    }
    if (cacheWillUpdatePluginCount === 0) {
      this.plugins.push(PrecacheStrategy.defaultPrecacheCacheabilityPlugin);
    } else if (cacheWillUpdatePluginCount > 1 && defaultPluginIndex !== null) {
      this.plugins.splice(defaultPluginIndex, 1);
    }
  }
}
PrecacheStrategy.defaultPrecacheCacheabilityPlugin = {
  async cacheWillUpdate({ response }) {
    if (!response || response.status >= 400) {
      return null;
    }
    return response;
  }
};
PrecacheStrategy.copyRedirectedCacheableResponsesPlugin = {
  async cacheWillUpdate({ response }) {
    return response.redirected ? await copyResponse(response) : response;
  }
};
class PrecacheController {
  /**
   * Create a new PrecacheController.
   *
   * @param {Object} [options]
   * @param {string} [options.cacheName] The cache to use for precaching.
   * @param {string} [options.plugins] Plugins to use when precaching as well
   * as responding to fetch events for precached assets.
   * @param {boolean} [options.fallbackToNetwork=true] Whether to attempt to
   * get the response from the network if there's a precache miss.
   */
  constructor({ cacheName, plugins = [], fallbackToNetwork = true } = {}) {
    this._urlsToCacheKeys = /* @__PURE__ */ new Map();
    this._urlsToCacheModes = /* @__PURE__ */ new Map();
    this._cacheKeysToIntegrities = /* @__PURE__ */ new Map();
    this._strategy = new PrecacheStrategy({
      cacheName: cacheNames.getPrecacheName(cacheName),
      plugins: [
        ...plugins,
        new PrecacheCacheKeyPlugin({ precacheController: this })
      ],
      fallbackToNetwork
    });
    this.install = this.install.bind(this);
    this.activate = this.activate.bind(this);
  }
  /**
   * @type {workbox-precaching.PrecacheStrategy} The strategy created by this controller and
   * used to cache assets and respond to fetch events.
   */
  get strategy() {
    return this._strategy;
  }
  /**
   * Adds items to the precache list, removing any duplicates and
   * stores the files in the
   * {@link workbox-core.cacheNames|"precache cache"} when the service
   * worker installs.
   *
   * This method can be called multiple times.
   *
   * @param {Array<Object|string>} [entries=[]] Array of entries to precache.
   */
  precache(entries) {
    this.addToCacheList(entries);
    if (!this._installAndActiveListenersAdded) {
      self.addEventListener("install", this.install);
      self.addEventListener("activate", this.activate);
      this._installAndActiveListenersAdded = true;
    }
  }
  /**
   * This method will add items to the precache list, removing duplicates
   * and ensuring the information is valid.
   *
   * @param {Array<workbox-precaching.PrecacheController.PrecacheEntry|string>} entries
   *     Array of entries to precache.
   */
  addToCacheList(entries) {
    const urlsToWarnAbout = [];
    for (const entry of entries) {
      if (typeof entry === "string") {
        urlsToWarnAbout.push(entry);
      } else if (entry && entry.revision === void 0) {
        urlsToWarnAbout.push(entry.url);
      }
      const { cacheKey, url } = createCacheKey(entry);
      const cacheMode = typeof entry !== "string" && entry.revision ? "reload" : "default";
      if (this._urlsToCacheKeys.has(url) && this._urlsToCacheKeys.get(url) !== cacheKey) {
        throw new WorkboxError("add-to-cache-list-conflicting-entries", {
          firstEntry: this._urlsToCacheKeys.get(url),
          secondEntry: cacheKey
        });
      }
      if (typeof entry !== "string" && entry.integrity) {
        if (this._cacheKeysToIntegrities.has(cacheKey) && this._cacheKeysToIntegrities.get(cacheKey) !== entry.integrity) {
          throw new WorkboxError("add-to-cache-list-conflicting-integrities", {
            url
          });
        }
        this._cacheKeysToIntegrities.set(cacheKey, entry.integrity);
      }
      this._urlsToCacheKeys.set(url, cacheKey);
      this._urlsToCacheModes.set(url, cacheMode);
      if (urlsToWarnAbout.length > 0) {
        const warningMessage = `Workbox is precaching URLs without revision info: ${urlsToWarnAbout.join(", ")}
This is generally NOT safe. Learn more at https://bit.ly/wb-precache`;
        {
          console.warn(warningMessage);
        }
      }
    }
  }
  /**
   * Precaches new and updated assets. Call this method from the service worker
   * install event.
   *
   * Note: this method calls `event.waitUntil()` for you, so you do not need
   * to call it yourself in your event handlers.
   *
   * @param {ExtendableEvent} event
   * @return {Promise<workbox-precaching.InstallResult>}
   */
  install(event) {
    return waitUntil(event, async () => {
      const installReportPlugin = new PrecacheInstallReportPlugin();
      this.strategy.plugins.push(installReportPlugin);
      for (const [url, cacheKey] of this._urlsToCacheKeys) {
        const integrity = this._cacheKeysToIntegrities.get(cacheKey);
        const cacheMode = this._urlsToCacheModes.get(url);
        const request = new Request(url, {
          integrity,
          cache: cacheMode,
          credentials: "same-origin"
        });
        await Promise.all(this.strategy.handleAll({
          params: { cacheKey },
          request,
          event
        }));
      }
      const { updatedURLs, notUpdatedURLs } = installReportPlugin;
      return { updatedURLs, notUpdatedURLs };
    });
  }
  /**
   * Deletes assets that are no longer present in the current precache manifest.
   * Call this method from the service worker activate event.
   *
   * Note: this method calls `event.waitUntil()` for you, so you do not need
   * to call it yourself in your event handlers.
   *
   * @param {ExtendableEvent} event
   * @return {Promise<workbox-precaching.CleanupResult>}
   */
  activate(event) {
    return waitUntil(event, async () => {
      const cache = await self.caches.open(this.strategy.cacheName);
      const currentlyCachedRequests = await cache.keys();
      const expectedCacheKeys = new Set(this._urlsToCacheKeys.values());
      const deletedURLs = [];
      for (const request of currentlyCachedRequests) {
        if (!expectedCacheKeys.has(request.url)) {
          await cache.delete(request);
          deletedURLs.push(request.url);
        }
      }
      return { deletedURLs };
    });
  }
  /**
   * Returns a mapping of a precached URL to the corresponding cache key, taking
   * into account the revision information for the URL.
   *
   * @return {Map<string, string>} A URL to cache key mapping.
   */
  getURLsToCacheKeys() {
    return this._urlsToCacheKeys;
  }
  /**
   * Returns a list of all the URLs that have been precached by the current
   * service worker.
   *
   * @return {Array<string>} The precached URLs.
   */
  getCachedURLs() {
    return [...this._urlsToCacheKeys.keys()];
  }
  /**
   * Returns the cache key used for storing a given URL. If that URL is
   * unversioned, like `/index.html', then the cache key will be the original
   * URL with a search parameter appended to it.
   *
   * @param {string} url A URL whose cache key you want to look up.
   * @return {string} The versioned URL that corresponds to a cache key
   * for the original URL, or undefined if that URL isn't precached.
   */
  getCacheKeyForURL(url) {
    const urlObject = new URL(url, location.href);
    return this._urlsToCacheKeys.get(urlObject.href);
  }
  /**
   * @param {string} url A cache key whose SRI you want to look up.
   * @return {string} The subresource integrity associated with the cache key,
   * or undefined if it's not set.
   */
  getIntegrityForCacheKey(cacheKey) {
    return this._cacheKeysToIntegrities.get(cacheKey);
  }
  /**
   * This acts as a drop-in replacement for
   * [`cache.match()`](https://developer.mozilla.org/en-US/docs/Web/API/Cache/match)
   * with the following differences:
   *
   * - It knows what the name of the precache is, and only checks in that cache.
   * - It allows you to pass in an "original" URL without versioning parameters,
   * and it will automatically look up the correct cache key for the currently
   * active revision of that URL.
   *
   * E.g., `matchPrecache('index.html')` will find the correct precached
   * response for the currently active service worker, even if the actual cache
   * key is `'/index.html?__WB_REVISION__=1234abcd'`.
   *
   * @param {string|Request} request The key (without revisioning parameters)
   * to look up in the precache.
   * @return {Promise<Response|undefined>}
   */
  async matchPrecache(request) {
    const url = request instanceof Request ? request.url : request;
    const cacheKey = this.getCacheKeyForURL(url);
    if (cacheKey) {
      const cache = await self.caches.open(this.strategy.cacheName);
      return cache.match(cacheKey);
    }
    return void 0;
  }
  /**
   * Returns a function that looks up `url` in the precache (taking into
   * account revision information), and returns the corresponding `Response`.
   *
   * @param {string} url The precached URL which will be used to lookup the
   * `Response`.
   * @return {workbox-routing~handlerCallback}
   */
  createHandlerBoundToURL(url) {
    const cacheKey = this.getCacheKeyForURL(url);
    if (!cacheKey) {
      throw new WorkboxError("non-precached-url", { url });
    }
    return (options) => {
      options.request = new Request(url);
      options.params = Object.assign({ cacheKey }, options.params);
      return this.strategy.handle(options);
    };
  }
}
let precacheController;
const getOrCreatePrecacheController = () => {
  if (!precacheController) {
    precacheController = new PrecacheController();
  }
  return precacheController;
};
try {
  self["workbox:routing:7.0.0"] && _();
} catch (e) {
}
const defaultMethod = "GET";
const normalizeHandler = (handler) => {
  if (handler && typeof handler === "object") {
    return handler;
  } else {
    return { handle: handler };
  }
};
class Route {
  /**
   * Constructor for Route class.
   *
   * @param {workbox-routing~matchCallback} match
   * A callback function that determines whether the route matches a given
   * `fetch` event by returning a non-falsy value.
   * @param {workbox-routing~handlerCallback} handler A callback
   * function that returns a Promise resolving to a Response.
   * @param {string} [method='GET'] The HTTP method to match the Route
   * against.
   */
  constructor(match, handler, method = defaultMethod) {
    this.handler = normalizeHandler(handler);
    this.match = match;
    this.method = method;
  }
  /**
   *
   * @param {workbox-routing-handlerCallback} handler A callback
   * function that returns a Promise resolving to a Response
   */
  setCatchHandler(handler) {
    this.catchHandler = normalizeHandler(handler);
  }
}
class RegExpRoute extends Route {
  /**
   * If the regular expression contains
   * [capture groups]{@link https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/RegExp#grouping-back-references},
   * the captured values will be passed to the
   * {@link workbox-routing~handlerCallback} `params`
   * argument.
   *
   * @param {RegExp} regExp The regular expression to match against URLs.
   * @param {workbox-routing~handlerCallback} handler A callback
   * function that returns a Promise resulting in a Response.
   * @param {string} [method='GET'] The HTTP method to match the Route
   * against.
   */
  constructor(regExp, handler, method) {
    const match = ({ url }) => {
      const result = regExp.exec(url.href);
      if (!result) {
        return;
      }
      if (url.origin !== location.origin && result.index !== 0) {
        return;
      }
      return result.slice(1);
    };
    super(match, handler, method);
  }
}
class Router {
  /**
   * Initializes a new Router.
   */
  constructor() {
    this._routes = /* @__PURE__ */ new Map();
    this._defaultHandlerMap = /* @__PURE__ */ new Map();
  }
  /**
   * @return {Map<string, Array<workbox-routing.Route>>} routes A `Map` of HTTP
   * method name ('GET', etc.) to an array of all the corresponding `Route`
   * instances that are registered.
   */
  get routes() {
    return this._routes;
  }
  /**
   * Adds a fetch event listener to respond to events when a route matches
   * the event's request.
   */
  addFetchListener() {
    self.addEventListener("fetch", (event) => {
      const { request } = event;
      const responsePromise = this.handleRequest({ request, event });
      if (responsePromise) {
        event.respondWith(responsePromise);
      }
    });
  }
  /**
   * Adds a message event listener for URLs to cache from the window.
   * This is useful to cache resources loaded on the page prior to when the
   * service worker started controlling it.
   *
   * The format of the message data sent from the window should be as follows.
   * Where the `urlsToCache` array may consist of URL strings or an array of
   * URL string + `requestInit` object (the same as you'd pass to `fetch()`).
   *
   * ```
   * {
   *   type: 'CACHE_URLS',
   *   payload: {
   *     urlsToCache: [
   *       './script1.js',
   *       './script2.js',
   *       ['./script3.js', {mode: 'no-cors'}],
   *     ],
   *   },
   * }
   * ```
   */
  addCacheListener() {
    self.addEventListener("message", (event) => {
      if (event.data && event.data.type === "CACHE_URLS") {
        const { payload } = event.data;
        const requestPromises = Promise.all(payload.urlsToCache.map((entry) => {
          if (typeof entry === "string") {
            entry = [entry];
          }
          const request = new Request(...entry);
          return this.handleRequest({ request, event });
        }));
        event.waitUntil(requestPromises);
        if (event.ports && event.ports[0]) {
          void requestPromises.then(() => event.ports[0].postMessage(true));
        }
      }
    });
  }
  /**
   * Apply the routing rules to a FetchEvent object to get a Response from an
   * appropriate Route's handler.
   *
   * @param {Object} options
   * @param {Request} options.request The request to handle.
   * @param {ExtendableEvent} options.event The event that triggered the
   *     request.
   * @return {Promise<Response>|undefined} A promise is returned if a
   *     registered route can handle the request. If there is no matching
   *     route and there's no `defaultHandler`, `undefined` is returned.
   */
  handleRequest({ request, event }) {
    const url = new URL(request.url, location.href);
    if (!url.protocol.startsWith("http")) {
      return;
    }
    const sameOrigin = url.origin === location.origin;
    const { params, route } = this.findMatchingRoute({
      event,
      request,
      sameOrigin,
      url
    });
    let handler = route && route.handler;
    const method = request.method;
    if (!handler && this._defaultHandlerMap.has(method)) {
      handler = this._defaultHandlerMap.get(method);
    }
    if (!handler) {
      return;
    }
    let responsePromise;
    try {
      responsePromise = handler.handle({ url, request, event, params });
    } catch (err) {
      responsePromise = Promise.reject(err);
    }
    const catchHandler = route && route.catchHandler;
    if (responsePromise instanceof Promise && (this._catchHandler || catchHandler)) {
      responsePromise = responsePromise.catch(async (err) => {
        if (catchHandler) {
          try {
            return await catchHandler.handle({ url, request, event, params });
          } catch (catchErr) {
            if (catchErr instanceof Error) {
              err = catchErr;
            }
          }
        }
        if (this._catchHandler) {
          return this._catchHandler.handle({ url, request, event });
        }
        throw err;
      });
    }
    return responsePromise;
  }
  /**
   * Checks a request and URL (and optionally an event) against the list of
   * registered routes, and if there's a match, returns the corresponding
   * route along with any params generated by the match.
   *
   * @param {Object} options
   * @param {URL} options.url
   * @param {boolean} options.sameOrigin The result of comparing `url.origin`
   *     against the current origin.
   * @param {Request} options.request The request to match.
   * @param {Event} options.event The corresponding event.
   * @return {Object} An object with `route` and `params` properties.
   *     They are populated if a matching route was found or `undefined`
   *     otherwise.
   */
  findMatchingRoute({ url, sameOrigin, request, event }) {
    const routes = this._routes.get(request.method) || [];
    for (const route of routes) {
      let params;
      const matchResult = route.match({ url, sameOrigin, request, event });
      if (matchResult) {
        params = matchResult;
        if (Array.isArray(params) && params.length === 0) {
          params = void 0;
        } else if (matchResult.constructor === Object && // eslint-disable-line
        Object.keys(matchResult).length === 0) {
          params = void 0;
        } else if (typeof matchResult === "boolean") {
          params = void 0;
        }
        return { route, params };
      }
    }
    return {};
  }
  /**
   * Define a default `handler` that's called when no routes explicitly
   * match the incoming request.
   *
   * Each HTTP method ('GET', 'POST', etc.) gets its own default handler.
   *
   * Without a default handler, unmatched requests will go against the
   * network as if there were no service worker present.
   *
   * @param {workbox-routing~handlerCallback} handler A callback
   * function that returns a Promise resulting in a Response.
   * @param {string} [method='GET'] The HTTP method to associate with this
   * default handler. Each method has its own default.
   */
  setDefaultHandler(handler, method = defaultMethod) {
    this._defaultHandlerMap.set(method, normalizeHandler(handler));
  }
  /**
   * If a Route throws an error while handling a request, this `handler`
   * will be called and given a chance to provide a response.
   *
   * @param {workbox-routing~handlerCallback} handler A callback
   * function that returns a Promise resulting in a Response.
   */
  setCatchHandler(handler) {
    this._catchHandler = normalizeHandler(handler);
  }
  /**
   * Registers a route with the router.
   *
   * @param {workbox-routing.Route} route The route to register.
   */
  registerRoute(route) {
    if (!this._routes.has(route.method)) {
      this._routes.set(route.method, []);
    }
    this._routes.get(route.method).push(route);
  }
  /**
   * Unregisters a route with the router.
   *
   * @param {workbox-routing.Route} route The route to unregister.
   */
  unregisterRoute(route) {
    if (!this._routes.has(route.method)) {
      throw new WorkboxError("unregister-route-but-not-found-with-method", {
        method: route.method
      });
    }
    const routeIndex = this._routes.get(route.method).indexOf(route);
    if (routeIndex > -1) {
      this._routes.get(route.method).splice(routeIndex, 1);
    } else {
      throw new WorkboxError("unregister-route-route-not-registered");
    }
  }
}
let defaultRouter;
const getOrCreateDefaultRouter = () => {
  if (!defaultRouter) {
    defaultRouter = new Router();
    defaultRouter.addFetchListener();
    defaultRouter.addCacheListener();
  }
  return defaultRouter;
};
function registerRoute(capture, handler, method) {
  let route;
  if (typeof capture === "string") {
    const captureUrl = new URL(capture, location.href);
    const matchCallback = ({ url }) => {
      return url.href === captureUrl.href;
    };
    route = new Route(matchCallback, handler, method);
  } else if (capture instanceof RegExp) {
    route = new RegExpRoute(capture, handler, method);
  } else if (typeof capture === "function") {
    route = new Route(capture, handler, method);
  } else if (capture instanceof Route) {
    route = capture;
  } else {
    throw new WorkboxError("unsupported-route-type", {
      moduleName: "workbox-routing",
      funcName: "registerRoute",
      paramName: "capture"
    });
  }
  const defaultRouter2 = getOrCreateDefaultRouter();
  defaultRouter2.registerRoute(route);
  return route;
}
function removeIgnoredSearchParams(urlObject, ignoreURLParametersMatching = []) {
  for (const paramName of [...urlObject.searchParams.keys()]) {
    if (ignoreURLParametersMatching.some((regExp) => regExp.test(paramName))) {
      urlObject.searchParams.delete(paramName);
    }
  }
  return urlObject;
}
function* generateURLVariations(url, { ignoreURLParametersMatching = [/^utm_/, /^fbclid$/], directoryIndex = "index.html", cleanURLs = true, urlManipulation } = {}) {
  const urlObject = new URL(url, location.href);
  urlObject.hash = "";
  yield urlObject.href;
  const urlWithoutIgnoredParams = removeIgnoredSearchParams(urlObject, ignoreURLParametersMatching);
  yield urlWithoutIgnoredParams.href;
  if (directoryIndex && urlWithoutIgnoredParams.pathname.endsWith("/")) {
    const directoryURL = new URL(urlWithoutIgnoredParams.href);
    directoryURL.pathname += directoryIndex;
    yield directoryURL.href;
  }
  if (cleanURLs) {
    const cleanURL = new URL(urlWithoutIgnoredParams.href);
    cleanURL.pathname += ".html";
    yield cleanURL.href;
  }
  if (urlManipulation) {
    const additionalURLs = urlManipulation({ url: urlObject });
    for (const urlToAttempt of additionalURLs) {
      yield urlToAttempt.href;
    }
  }
}
class PrecacheRoute extends Route {
  /**
   * @param {PrecacheController} precacheController A `PrecacheController`
   * instance used to both match requests and respond to fetch events.
   * @param {Object} [options] Options to control how requests are matched
   * against the list of precached URLs.
   * @param {string} [options.directoryIndex=index.html] The `directoryIndex` will
   * check cache entries for a URLs ending with '/' to see if there is a hit when
   * appending the `directoryIndex` value.
   * @param {Array<RegExp>} [options.ignoreURLParametersMatching=[/^utm_/, /^fbclid$/]] An
   * array of regex's to remove search params when looking for a cache match.
   * @param {boolean} [options.cleanURLs=true] The `cleanURLs` option will
   * check the cache for the URL with a `.html` added to the end of the end.
   * @param {workbox-precaching~urlManipulation} [options.urlManipulation]
   * This is a function that should take a URL and return an array of
   * alternative URLs that should be checked for precache matches.
   */
  constructor(precacheController2, options) {
    const match = ({ request }) => {
      const urlsToCacheKeys = precacheController2.getURLsToCacheKeys();
      for (const possibleURL of generateURLVariations(request.url, options)) {
        const cacheKey = urlsToCacheKeys.get(possibleURL);
        if (cacheKey) {
          const integrity = precacheController2.getIntegrityForCacheKey(cacheKey);
          return { cacheKey, integrity };
        }
      }
      return;
    };
    super(match, precacheController2.strategy);
  }
}
function addRoute(options) {
  const precacheController2 = getOrCreatePrecacheController();
  const precacheRoute = new PrecacheRoute(precacheController2, options);
  registerRoute(precacheRoute);
}
function getCacheKeyForURL(url) {
  const precacheController2 = getOrCreatePrecacheController();
  return precacheController2.getCacheKeyForURL(url);
}
function matchPrecache(request) {
  const precacheController2 = getOrCreatePrecacheController();
  return precacheController2.matchPrecache(request);
}
function precache(entries) {
  const precacheController2 = getOrCreatePrecacheController();
  precacheController2.precache(entries);
}
function precacheAndRoute(entries, options) {
  precache(entries);
  addRoute(options);
}
class NavigationRoute extends Route {
  /**
   * If both `denylist` and `allowlist` are provided, the `denylist` will
   * take precedence and the request will not match this route.
   *
   * The regular expressions in `allowlist` and `denylist`
   * are matched against the concatenated
   * [`pathname`]{@link https://developer.mozilla.org/en-US/docs/Web/API/HTMLHyperlinkElementUtils/pathname}
   * and [`search`]{@link https://developer.mozilla.org/en-US/docs/Web/API/HTMLHyperlinkElementUtils/search}
   * portions of the requested URL.
   *
   * *Note*: These RegExps may be evaluated against every destination URL during
   * a navigation. Avoid using
   * [complex RegExps](https://github.com/GoogleChrome/workbox/issues/3077),
   * or else your users may see delays when navigating your site.
   *
   * @param {workbox-routing~handlerCallback} handler A callback
   * function that returns a Promise resulting in a Response.
   * @param {Object} options
   * @param {Array<RegExp>} [options.denylist] If any of these patterns match,
   * the route will not handle the request (even if a allowlist RegExp matches).
   * @param {Array<RegExp>} [options.allowlist=[/./]] If any of these patterns
   * match the URL's pathname and search parameter, the route will handle the
   * request (assuming the denylist doesn't match).
   */
  constructor(handler, { allowlist = [/./], denylist = [] } = {}) {
    super((options) => this._match(options), handler);
    this._allowlist = allowlist;
    this._denylist = denylist;
  }
  /**
   * Routes match handler.
   *
   * @param {Object} options
   * @param {URL} options.url
   * @param {Request} options.request
   * @return {boolean}
   *
   * @private
   */
  _match({ url, request }) {
    if (request && request.mode !== "navigate") {
      return false;
    }
    const pathnameAndSearch = url.pathname + url.search;
    for (const regExp of this._denylist) {
      if (regExp.test(pathnameAndSearch)) {
        return false;
      }
    }
    if (this._allowlist.some((regExp) => regExp.test(pathnameAndSearch))) {
      return true;
    }
    return false;
  }
}
const cacheOkAndOpaquePlugin = {
  /**
   * Returns a valid response (to allow caching) if the status is 200 (OK) or
   * 0 (opaque).
   *
   * @param {Object} options
   * @param {Response} options.response
   * @return {Response|null}
   *
   * @private
   */
  cacheWillUpdate: async ({ response }) => {
    if (response.status === 200 || response.status === 0) {
      return response;
    }
    return null;
  }
};
class NetworkFirst extends Strategy {
  /**
   * @param {Object} [options]
   * @param {string} [options.cacheName] Cache name to store and retrieve
   * requests. Defaults to cache names provided by
   * {@link workbox-core.cacheNames}.
   * @param {Array<Object>} [options.plugins] [Plugins]{@link https://developers.google.com/web/tools/workbox/guides/using-plugins}
   * to use in conjunction with this caching strategy.
   * @param {Object} [options.fetchOptions] Values passed along to the
   * [`init`](https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch#Parameters)
   * of [non-navigation](https://github.com/GoogleChrome/workbox/issues/1796)
   * `fetch()` requests made by this strategy.
   * @param {Object} [options.matchOptions] [`CacheQueryOptions`](https://w3c.github.io/ServiceWorker/#dictdef-cachequeryoptions)
   * @param {number} [options.networkTimeoutSeconds] If set, any network requests
   * that fail to respond within the timeout will fallback to the cache.
   *
   * This option can be used to combat
   * "[lie-fi]{@link https://developers.google.com/web/fundamentals/performance/poor-connectivity/#lie-fi}"
   * scenarios.
   */
  constructor(options = {}) {
    super(options);
    if (!this.plugins.some((p) => "cacheWillUpdate" in p)) {
      this.plugins.unshift(cacheOkAndOpaquePlugin);
    }
    this._networkTimeoutSeconds = options.networkTimeoutSeconds || 0;
  }
  /**
   * @private
   * @param {Request|string} request A request to run this strategy for.
   * @param {workbox-strategies.StrategyHandler} handler The event that
   *     triggered the request.
   * @return {Promise<Response>}
   */
  async _handle(request, handler) {
    const logs = [];
    const promises = [];
    let timeoutId;
    if (this._networkTimeoutSeconds) {
      const { id, promise } = this._getTimeoutPromise({ request, logs, handler });
      timeoutId = id;
      promises.push(promise);
    }
    const networkPromise = this._getNetworkPromise({
      timeoutId,
      request,
      logs,
      handler
    });
    promises.push(networkPromise);
    const response = await handler.waitUntil((async () => {
      return await handler.waitUntil(Promise.race(promises)) || // If Promise.race() resolved with null, it might be due to a network
      // timeout + a cache miss. If that were to happen, we'd rather wait until
      // the networkPromise resolves instead of returning null.
      // Note that it's fine to await an already-resolved promise, so we don't
      // have to check to see if it's still "in flight".
      await networkPromise;
    })());
    if (!response) {
      throw new WorkboxError("no-response", { url: request.url });
    }
    return response;
  }
  /**
   * @param {Object} options
   * @param {Request} options.request
   * @param {Array} options.logs A reference to the logs array
   * @param {Event} options.event
   * @return {Promise<Response>}
   *
   * @private
   */
  _getTimeoutPromise({ request, logs, handler }) {
    let timeoutId;
    const timeoutPromise = new Promise((resolve) => {
      const onNetworkTimeout = async () => {
        resolve(await handler.cacheMatch(request));
      };
      timeoutId = setTimeout(onNetworkTimeout, this._networkTimeoutSeconds * 1e3);
    });
    return {
      promise: timeoutPromise,
      id: timeoutId
    };
  }
  /**
   * @param {Object} options
   * @param {number|undefined} options.timeoutId
   * @param {Request} options.request
   * @param {Array} options.logs A reference to the logs Array.
   * @param {Event} options.event
   * @return {Promise<Response>}
   *
   * @private
   */
  async _getNetworkPromise({ timeoutId, request, logs, handler }) {
    let error;
    let response;
    try {
      response = await handler.fetchAndCachePut(request);
    } catch (fetchError) {
      if (fetchError instanceof Error) {
        error = fetchError;
      }
    }
    if (timeoutId) {
      clearTimeout(timeoutId);
    }
    if (error || !response) {
      response = await handler.cacheMatch(request);
    }
    return response;
  }
}
class NetworkOnly extends Strategy {
  /**
   * @param {Object} [options]
   * @param {Array<Object>} [options.plugins] [Plugins]{@link https://developers.google.com/web/tools/workbox/guides/using-plugins}
   * to use in conjunction with this caching strategy.
   * @param {Object} [options.fetchOptions] Values passed along to the
   * [`init`](https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/fetch#Parameters)
   * of [non-navigation](https://github.com/GoogleChrome/workbox/issues/1796)
   * `fetch()` requests made by this strategy.
   * @param {number} [options.networkTimeoutSeconds] If set, any network requests
   * that fail to respond within the timeout will result in a network error.
   */
  constructor(options = {}) {
    super(options);
    this._networkTimeoutSeconds = options.networkTimeoutSeconds || 0;
  }
  /**
   * @private
   * @param {Request|string} request A request to run this strategy for.
   * @param {workbox-strategies.StrategyHandler} handler The event that
   *     triggered the request.
   * @return {Promise<Response>}
   */
  async _handle(request, handler) {
    let error = void 0;
    let response;
    try {
      const promises = [
        handler.fetch(request)
      ];
      if (this._networkTimeoutSeconds) {
        const timeoutPromise = timeout(this._networkTimeoutSeconds * 1e3);
        promises.push(timeoutPromise);
      }
      response = await Promise.race(promises);
      if (!response) {
        throw new Error(`Timed out the network response after ${this._networkTimeoutSeconds} seconds.`);
      }
    } catch (err) {
      if (err instanceof Error) {
        error = err;
      }
    }
    if (!response) {
      throw new WorkboxError("no-response", { url: request.url, error });
    }
    return response;
  }
}
importScripts("sw-runtime-resources-precache.js");
self.skipWaiting();
clientsClaim();
let manifestEntries = [{ "url": ".", "revision": "a5812e04b8cbfba99d9782f6a6d7b8bf" }, { "url": "VAADIN/build/abap-C4wZXNMq.js", "revision": "a44cffdd52deb4a54c250a2ab86515b6" }, { "url": "VAADIN/build/abc-DmCq50ke.js", "revision": "5d399685c1021f8de8788c0fbc9171ab" }, { "url": "VAADIN/build/actionscript-DDuIhuFx.js", "revision": "fb0a8f457940595210e0111fd7a1e032" }, { "url": "VAADIN/build/ada-ahj0PjII.js", "revision": "19e60d42e285253db14f48265ac423fe" }, { "url": "VAADIN/build/alda-DMAmvHWO.js", "revision": "511d84efa2f210101f65dc6d42e88fb5" }, { "url": "VAADIN/build/apache_conf-BSy7Bn15.js", "revision": "21cfe087f82dedb773b5bc3b7115d61f" }, { "url": "VAADIN/build/apex-DwV3Zf0q.js", "revision": "bf6c5e070295d6e6d2f173fff2ce3451" }, { "url": "VAADIN/build/applescript-DgRacSoN.js", "revision": "e59e99569f6cc4d1f2cff88ae46eaa9d" }, { "url": "VAADIN/build/aql-BOT1Tbf2.js", "revision": "0a5a3682bd92ec48cd030bb47703a667" }, { "url": "VAADIN/build/asciidoc-BUjZC5sT.js", "revision": "80927979221f9ebc27781f3c925ef890" }, { "url": "VAADIN/build/asl-EXELl6bK.js", "revision": "c76efac847e082aa27fc28a0addf71f3" }, { "url": "VAADIN/build/assembly_x86-3HfLv3Tn.js", "revision": "04ecefbc31f7ce4c0e490616b7f1f721" }, { "url": "VAADIN/build/autohotkey-BxQxHEt7.js", "revision": "9c699520074afeb32882653cab323ef7" }, { "url": "VAADIN/build/batchfile-Dg_RkhOh.js", "revision": "16c887f7d6d96bd43a898e89cf56634a" }, { "url": "VAADIN/build/bibtex-BydQMk10.js", "revision": "e74ced477a927aa2f86c94e4f4b395d8" }, { "url": "VAADIN/build/c_cpp-BkY8ybvt.js", "revision": "130fc718014713d8dad9403a38fb7b2c" }, { "url": "VAADIN/build/c9search-T4q5hJGn.js", "revision": "bc979b0512e794ff8fc70d50459e5ce0" }, { "url": "VAADIN/build/cirru-B5g9Dh6s.js", "revision": "25187a0a6e7334fdeda0715dd087fc89" }, { "url": "VAADIN/build/clojure-BXSVHUPs.js", "revision": "e7f85624fedbbdbefad4b947845d36c0" }, { "url": "VAADIN/build/cobol-D7LHIwcc.js", "revision": "7f8e8851c86a2defb538ace86e45a176" }, { "url": "VAADIN/build/coffee-ixEa4FbZ.js", "revision": "bebe355fc3217946221732eb396b548d" }, { "url": "VAADIN/build/coldfusion-8iy3YG7i.js", "revision": "788de43006c54e98166713bcf03ee01c" }, { "url": "VAADIN/build/crystal-Dyr4ba71.js", "revision": "033a93d1865f0ba8f0b512017c735a5c" }, { "url": "VAADIN/build/csharp-CMXdGTXO.js", "revision": "380b5535187b2bb4bce60063e06f3e51" }, { "url": "VAADIN/build/csound_document-DW-9Ug68.js", "revision": "c5c9e12e7f9ebeb9474405d4a95730e9" }, { "url": "VAADIN/build/csound_orchestra-COoXeAiv.js", "revision": "90ef11aee0e550f63790e3fd3a6a93c2" }, { "url": "VAADIN/build/csound_score-hxC1a-HM.js", "revision": "4363d995ff802ec225926e6f2bd82920" }, { "url": "VAADIN/build/csp-p0DqX9Do.js", "revision": "2a7144ef8569dd9101230b9255a7333c" }, { "url": "VAADIN/build/css-DCiIfTqB.js", "revision": "d309174d498211310ccb7e14c68d99b8" }, { "url": "VAADIN/build/curly-DGOH3oPY.js", "revision": "ea3c5d3ada72ef8cacec02a1348b2b10" }, { "url": "VAADIN/build/d-BFexbuWb.js", "revision": "ffc2dedb18f4468b3990be9e4d42e017" }, { "url": "VAADIN/build/dart-o-cVcW_f.js", "revision": "9d789c5018a9429b9e27ffeb93e99fc7" }, { "url": "VAADIN/build/diff-BD1xuLG9.js", "revision": "cf40eb2a6853090eda50d776082b8fc9" }, { "url": "VAADIN/build/django-nFpejyIY.js", "revision": "8709c0ce77cc80d36fafd529cad38d2a" }, { "url": "VAADIN/build/dockerfile--jxWL5_X.js", "revision": "dbd6a06d55bd12d573c28aff449b9351" }, { "url": "VAADIN/build/dot-BbqoDcyU.js", "revision": "ee6c342556c964e8bf1f9f0e5948a6eb" }, { "url": "VAADIN/build/drools-BEvkrmrz.js", "revision": "ddaceabd01fd5e22cc2786f3d1b8dae9" }, { "url": "VAADIN/build/edifact-DcurzQp7.js", "revision": "d4f944e018199f04291cc8fc2e07efb8" }, { "url": "VAADIN/build/eiffel-CsMz3WgC.js", "revision": "eeccc23415e94fa548d2a34f0ec2c7ca" }, { "url": "VAADIN/build/ejs-Bna9tmO6.js", "revision": "8d0e967c881fd47c8348337c5001cf6e" }, { "url": "VAADIN/build/elixir-DNjqubff.js", "revision": "d1a8eddb6a6054f4c93311619ffa8aaf" }, { "url": "VAADIN/build/elm-DMBouiH4.js", "revision": "667ca0dbf22bdbe1ce7e95e76e81c47c" }, { "url": "VAADIN/build/erlang-BzDlyHS1.js", "revision": "c065b5c2d791f6abf7fe7eb4c8c0271a" }, { "url": "VAADIN/build/ext-beautify-D7sOxgb7.js", "revision": "eb7f93ccd5b8d9ab126fba701396a240" }, { "url": "VAADIN/build/ext-code_lens-CX3JoLHM.js", "revision": "f1c120e7fa18e73d08d4116b7365b8d5" }, { "url": "VAADIN/build/ext-elastic_tabstops_lite-DPpmsw2r.js", "revision": "3a8b074c3b17b32cfbf920b6ea9a2c60" }, { "url": "VAADIN/build/ext-emmet-5peB3oBx.js", "revision": "87bb9d4759de3b9bcc87e1abc2984054" }, { "url": "VAADIN/build/ext-error_marker-C5LYimZz.js", "revision": "30815f4d1b7cce1801a2112d50deedfb" }, { "url": "VAADIN/build/ext-hardwrap-CsFrbbTp.js", "revision": "bdd20d0b8951cf84b809ace1139554c9" }, { "url": "VAADIN/build/ext-inline_autocomplete-Culz0rW0.js", "revision": "3ef35e7a12618f75e118749e81dea1cc" }, { "url": "VAADIN/build/ext-keybinding_menu-B_uFceOy.js", "revision": "a91927b9bccc731c971eba5431b1bf11" }, { "url": "VAADIN/build/ext-language_tools-jNqzHCqu.js", "revision": "ddd836b29ef2595e4438b73dee1e2dc6" }, { "url": "VAADIN/build/ext-linking-Co_3CZo4.js", "revision": "1823ed4dfa57e4ad12098c7bd536e650" }, { "url": "VAADIN/build/ext-modelist-BJ3hqMZi.js", "revision": "43c7699bd70a59c058fd807796ff39f1" }, { "url": "VAADIN/build/ext-options-CJ0Dtcx_.js", "revision": "9d9abd6230a0f7875bf92c1982780061" }, { "url": "VAADIN/build/ext-prompt-BW1_hE1x.js", "revision": "ec3c9926b84bd461101effb69fe1ec42" }, { "url": "VAADIN/build/ext-rtl-DbLvM_MG.js", "revision": "09c8f342e7764a7fb4ffa8483aa1f333" }, { "url": "VAADIN/build/ext-searchbox-CRw2Mac3.js", "revision": "930fc713645fdd41ded3881c74a23d77" }, { "url": "VAADIN/build/ext-settings_menu-3uktpocu.js", "revision": "a1a1d454ac070dd31203585b8913c737" }, { "url": "VAADIN/build/ext-spellcheck-eZjHW0Dt.js", "revision": "ed1eabc0489963431c92284efc6d9d3b" }, { "url": "VAADIN/build/ext-split-DowjvFFb.js", "revision": "0c6205e9d6908c492da86309b93a50e3" }, { "url": "VAADIN/build/ext-static_highlight-BgAZCq6B.js", "revision": "724e3ebb9d746b99fd2de70603f26aa4" }, { "url": "VAADIN/build/ext-statusbar-BiSpVls1.js", "revision": "23b478a51669f0017db483208afe322d" }, { "url": "VAADIN/build/ext-textarea-XEcGOEH5.js", "revision": "258b1830da7601bdc0ebb8256810a7c6" }, { "url": "VAADIN/build/ext-themelist-Du7ngnU0.js", "revision": "49dd10e592b86e9f3c7e7c38d4f72206" }, { "url": "VAADIN/build/ext-whitespace-DV8U6SlX.js", "revision": "47eedc93fa3f1461c2b4e8313520a22b" }, { "url": "VAADIN/build/fetch_stream-16QTEJmh.js", "revision": "8b9c29a3864324f8bb239080ace585c3" }, { "url": "VAADIN/build/FlowBootstrap-BStXjIWo.js", "revision": "96ef8de8a8f605c3d8b92695f797601d" }, { "url": "VAADIN/build/FlowClient-B-FuFaal.js", "revision": "aae6ee4578b6bcf960bb4e5ccbbb16b4" }, { "url": "VAADIN/build/forth-Dl7yXL9A.js", "revision": "c8d2645279f88454893e258f3ccbfa9c" }, { "url": "VAADIN/build/fortran-CGQC-CiI.js", "revision": "ad9b3e8437b12984a304bfdc9568b2e3" }, { "url": "VAADIN/build/fsharp-4BzQV93h.js", "revision": "22e188febbab510975ade86595f56253" }, { "url": "VAADIN/build/fsl-ycWjWL2A.js", "revision": "742b4f94d0eab75bbafe1744fe1144ab" }, { "url": "VAADIN/build/ftl-9kDFusMn.js", "revision": "6caad7c59f3e2fb90a9bda91793e82a8" }, { "url": "VAADIN/build/gcode-CT-qxdjn.js", "revision": "4f74f5cf6025ea1e990fb63e77cfab54" }, { "url": "VAADIN/build/generated-flow-imports-A-LZuJq_.js", "revision": "3fe36f7b742f392314ae3f0032ecb9b6" }, { "url": "VAADIN/build/gherkin-g_7Ub1F6.js", "revision": "3a924b7f1ad4fa41bc89984bf39d1aad" }, { "url": "VAADIN/build/gitignore-B4f-nr4R.js", "revision": "aa31a739355d97bfa2a8fa31420d08be" }, { "url": "VAADIN/build/glsl-BYvQiA7Z.js", "revision": "2c99bd6c2a0a55adf95081ff33b12695" }, { "url": "VAADIN/build/gobstones-D4ZsTNk0.js", "revision": "3826b69d1ebb026a46d1304b0ad181ff" }, { "url": "VAADIN/build/golang-CrlD178-.js", "revision": "7080739dac27c6413c4c92e4b6be2790" }, { "url": "VAADIN/build/graphqlschema-ThjnMq8J.js", "revision": "f8c4f3719de10671b294aba3ff2bd2fe" }, { "url": "VAADIN/build/groovy-CgUoZfXT.js", "revision": "41fa8ce4917c7a02ac7ceef4e0ebf8e1" }, { "url": "VAADIN/build/haml-rNciPG9L.js", "revision": "4d9de60863e7edd8d961b35dea1f6be6" }, { "url": "VAADIN/build/handlebars-DH0WvhLh.js", "revision": "9abbbefe0df89fc1e42722a731dc8c83" }, { "url": "VAADIN/build/haskell_cabal-B0A154p_.js", "revision": "e51b480cf65d7917c051480abc62e929" }, { "url": "VAADIN/build/haskell-B2uj0pAh.js", "revision": "34b251d34603f8e00ee2ee2a15846239" }, { "url": "VAADIN/build/haxe-BmEteBRU.js", "revision": "097b94afe365dc92a40b447a1702910a" }, { "url": "VAADIN/build/hjson-D419LcSw.js", "revision": "157549a381e2e992f5886c00be5d4f35" }, { "url": "VAADIN/build/html_elixir-BKn_vRA9.js", "revision": "11bb31e8e4dac1242b8a45deab983528" }, { "url": "VAADIN/build/html_ruby-wa41VBdQ.js", "revision": "45b0f7a5885078b6f2b33f34794ab2fc" }, { "url": "VAADIN/build/html-C23MHtwE.js", "revision": "baec0fb05bf98ccb206f3c5549d7229d" }, { "url": "VAADIN/build/indexhtml-DmsHVRX3.js", "revision": "e5bc4ecfcf083a539ee991164d1b775c" }, { "url": "VAADIN/build/ini-BEPUf3mI.js", "revision": "f1beaa22d367adac54339e2d39b13026" }, { "url": "VAADIN/build/io-bKi0ov_H.js", "revision": "f262bcba77426ca6edc0ab61467193d6" }, { "url": "VAADIN/build/ion-DVVAAHhO.js", "revision": "8bef4f3847360cbac5fc0c67f7e2b688" }, { "url": "VAADIN/build/jack-Ck7DNGrx.js", "revision": "aa499088bea55416153d1e2b13e314e2" }, { "url": "VAADIN/build/jade-CQQKEM2y.js", "revision": "6dff22d853d4b7057fa6a9369261e416" }, { "url": "VAADIN/build/java-Dv1WknID.js", "revision": "63dd6a357ae95daf50463f9deb0577b4" }, { "url": "VAADIN/build/javascript-CswimQqQ.js", "revision": "f836fa6a4db9c7ef7cbdb01afa138df2" }, { "url": "VAADIN/build/jexl-DyUsBgX9.js", "revision": "62ebfa8d96d7c0ee1c2dd6635e376b6f" }, { "url": "VAADIN/build/json-CHzDuItf.js", "revision": "d1fa618332c89268fbca3ba5fdbbba91" }, { "url": "VAADIN/build/json5-CftP9Q_s.js", "revision": "c55a5f83e56a5f2b3417ad3f3a72f4cc" }, { "url": "VAADIN/build/jsoniq-VQTx89Tf.js", "revision": "29aa5c29681090135a69ec98c52ddf3b" }, { "url": "VAADIN/build/jsp-I_R-d5pu.js", "revision": "a21d17670ef25a4687e2350d0db31bcc" }, { "url": "VAADIN/build/jssm-CXJy-Mhd.js", "revision": "73b3998e26ae0a46ba4472ead76f0d75" }, { "url": "VAADIN/build/jsx-DAvEbCUJ.js", "revision": "dae896a8e0ae826316ae5370af1ddc52" }, { "url": "VAADIN/build/julia-BSHcWRps.js", "revision": "1c4ac766d00bff17e1a7ed98bf63b2bf" }, { "url": "VAADIN/build/keybinding-emacs-CQmQisc_.js", "revision": "41c06ec04eed49b98ca64d02e4f5ee52" }, { "url": "VAADIN/build/keybinding-sublime-C5YEj0DV.js", "revision": "651aea9687a0e004b1a2c4007a008031" }, { "url": "VAADIN/build/keybinding-vim-DupmpXJ1.js", "revision": "64c80eb399f1c6ab79b11a942db48cc1" }, { "url": "VAADIN/build/keybinding-vscode-B9NYlB5A.js", "revision": "87112fbf434b1fe0249e81b894ff2b2f" }, { "url": "VAADIN/build/kotlin-BH7HYvdV.js", "revision": "d3ef305e25e87cf73e44cfc8f1134220" }, { "url": "VAADIN/build/latex-CsdJUk3g.js", "revision": "b84de2918e222504f7fd2d77a9af7653" }, { "url": "VAADIN/build/latte-XSovkBbs.js", "revision": "aeb8889924486ab68bfcdc5d35cdf200" }, { "url": "VAADIN/build/less-D6jA2Ud0.js", "revision": "b94f8e90b778e530c5bf775923869d8e" }, { "url": "VAADIN/build/liquid-Dj1yAnt4.js", "revision": "da111c6853b20c20c2035597a4de527e" }, { "url": "VAADIN/build/lisp-DtuvXOpW.js", "revision": "d1543042874903a5db3bb94875a04ab0" }, { "url": "VAADIN/build/livescript-C9DzoxH3.js", "revision": "fac158a4db947b7e991c9f00e0b1896b" }, { "url": "VAADIN/build/logiql-BnprHCL7.js", "revision": "ef8f86e57a5542ad21f89d34367f13ac" }, { "url": "VAADIN/build/logtalk-Dkx4ma9O.js", "revision": "36eef555b81712ea5831aa085f4c7e8d" }, { "url": "VAADIN/build/lsl-C29FRMuh.js", "revision": "763f428e1b49b6b5a68f4d06596fb92d" }, { "url": "VAADIN/build/lua-Ccvk_c-t.js", "revision": "362653266008144bcf9fb96c8dd4231d" }, { "url": "VAADIN/build/luapage-BRiXcfp4.js", "revision": "fd48ad48002b0e0149f8318a88e0494f" }, { "url": "VAADIN/build/lucene-DbZjoIrE.js", "revision": "1459a64a5b16eb3714a0865d58a56545" }, { "url": "VAADIN/build/makefile-Bpm8sQoC.js", "revision": "bbf24b44a999e91e0bb77e89c84d7429" }, { "url": "VAADIN/build/markdown-CaMca8U5.js", "revision": "946f63d16393578ffb0fce41988b04be" }, { "url": "VAADIN/build/mask-BvzeKZt8.js", "revision": "8dcbd3f3744b90ae506fc761f845e04a" }, { "url": "VAADIN/build/matlab-Dh7Zrspm.js", "revision": "f98342de6dc30943d70f32a2ee87d86c" }, { "url": "VAADIN/build/maze-Ad5JHG4Q.js", "revision": "248130654f827aa0efa6fe24ac577906" }, { "url": "VAADIN/build/mediawiki-DLi1LaG5.js", "revision": "be9d5161838b9539e5bb3c84b06fd1ff" }, { "url": "VAADIN/build/mel-5FJut93i.js", "revision": "d942a282e4b60f1551090436fbf7435c" }, { "url": "VAADIN/build/mips-XPM7-eYr.js", "revision": "01a138107e8fdbfc0e2ca23c3cbe056d" }, { "url": "VAADIN/build/mixal-CB7vPpjy.js", "revision": "b498d44d1397f36c5223a6af18cae96f" }, { "url": "VAADIN/build/mode-abap-D_SqP9bU.js", "revision": "3735cf3e06699d6193b725affdc5d5e1" }, { "url": "VAADIN/build/mode-abc-CHAXh4JK.js", "revision": "d7f4ace4df5309dc6af85d3db8924724" }, { "url": "VAADIN/build/mode-actionscript-CGTxVPS4.js", "revision": "fd6d8f9af36f2ab6c21712fd5f979b46" }, { "url": "VAADIN/build/mode-ada-ifIYrSbz.js", "revision": "e2e301edf857a9d07afb731bf56c81d7" }, { "url": "VAADIN/build/mode-alda-D1_fnVcU.js", "revision": "8ddaa449ee60795ddc8f2bb969045083" }, { "url": "VAADIN/build/mode-apache_conf-CJVd9GSh.js", "revision": "1082b9575796ae9d36845a63933d0827" }, { "url": "VAADIN/build/mode-apex-DnmZlK4u.js", "revision": "1471e3fe059a782738b83da27f49d700" }, { "url": "VAADIN/build/mode-applescript-jA-2l-tM.js", "revision": "ba06a8eed58aadc0e7cb11906160cd63" }, { "url": "VAADIN/build/mode-aql-BngN2TNM.js", "revision": "3f507be8c8268fcdf51317dd70c0f4ec" }, { "url": "VAADIN/build/mode-asciidoc-nTb6TUSf.js", "revision": "94fd519c461cd7db1b7a5cf6d6119196" }, { "url": "VAADIN/build/mode-asl-C4eMNHkb.js", "revision": "7fcdc0d70c27dc4a1e4ab397d18ecae8" }, { "url": "VAADIN/build/mode-assembly_x86-Bt2sBH9K.js", "revision": "981cee583041c8b6cefe848b9b94cdbb" }, { "url": "VAADIN/build/mode-autohotkey-Dvf1PCEk.js", "revision": "2a9d84ebba38015d5cc261e4464d65e9" }, { "url": "VAADIN/build/mode-batchfile-BocyvDba.js", "revision": "548bcfa9b8df9492d3462ecc748396a8" }, { "url": "VAADIN/build/mode-bibtex-QMzZ9nwD.js", "revision": "12a4a239bede954d37590340cdd63fef" }, { "url": "VAADIN/build/mode-c_cpp-UoZiV5RQ.js", "revision": "def0db4aa3d0fe0f93f5fd1df0ba32cc" }, { "url": "VAADIN/build/mode-c9search-DxJpslOe.js", "revision": "6c20c2bab3d3b00284652ebf0d2f1481" }, { "url": "VAADIN/build/mode-cirru-B_LMLCpO.js", "revision": "d4c250fefbc83c3d33bc5a3f57acd9a8" }, { "url": "VAADIN/build/mode-clojure-LARH4C7s.js", "revision": "d9d5e4d649cb0f654d47a9b472095bc5" }, { "url": "VAADIN/build/mode-cobol-DbNi5pEn.js", "revision": "907c28346f2cc66fd72e7ee776a5b76b" }, { "url": "VAADIN/build/mode-coffee-CSGw5Hu7.js", "revision": "3b58be75e273cb062220162cb98c40f2" }, { "url": "VAADIN/build/mode-coldfusion-R-UFa4X8.js", "revision": "860047f13bc850f41ebfeb52baa24b9e" }, { "url": "VAADIN/build/mode-crystal-DBlJDutl.js", "revision": "9e4f891c1d1f39cd5585f573bc6fc69c" }, { "url": "VAADIN/build/mode-csharp-FQGYUay5.js", "revision": "4b6ae0c4b57a18dc8f090758335a2688" }, { "url": "VAADIN/build/mode-csound_document-DutlTvd4.js", "revision": "73c7a95d554ce8594d72a74b69afd0c2" }, { "url": "VAADIN/build/mode-csound_orchestra-CrMgb2Pe.js", "revision": "b97a451807614f7c4e5c52374fbdc8b6" }, { "url": "VAADIN/build/mode-csound_score-CFsQyx39.js", "revision": "54d32646f7915de5d7708dc010c868fc" }, { "url": "VAADIN/build/mode-csp-DfMaEXtP.js", "revision": "458657102b3e1d62bbd50736d139ea7f" }, { "url": "VAADIN/build/mode-css-Ce9Nf4Ud.js", "revision": "3d149fc8a79c4718b835ed442e1fc144" }, { "url": "VAADIN/build/mode-curly-BBUnx5Os.js", "revision": "f47ea43d981da711000fc20bdfc06384" }, { "url": "VAADIN/build/mode-d-cvxN9Dsj.js", "revision": "f511f3c47b3a4b89939843413c604442" }, { "url": "VAADIN/build/mode-dart-B3MwECol.js", "revision": "bd88058ad4032bafc55fb40589eb9da3" }, { "url": "VAADIN/build/mode-diff-BZ5cJWDo.js", "revision": "1ab6a7a4a5d0df7c35ee147baa52f6c9" }, { "url": "VAADIN/build/mode-django-vJ8K2BGp.js", "revision": "a4fcebfd59d9e5f3e068aa439617200a" }, { "url": "VAADIN/build/mode-dockerfile-Ukob7baM.js", "revision": "63b2069e9a19ada988f8b9321449124f" }, { "url": "VAADIN/build/mode-dot-CzZ0oxM3.js", "revision": "03eb1e6fbb2d5fa613c8945b78366b3c" }, { "url": "VAADIN/build/mode-drools-BsQMlFNX.js", "revision": "fc91c82e713b55dccc7c0e59db4ebb83" }, { "url": "VAADIN/build/mode-edifact-D0IqXDFo.js", "revision": "33e5917fd0c23821d75075d8d89751a5" }, { "url": "VAADIN/build/mode-eiffel-C98vALyR.js", "revision": "4dbe082aa918800057217f0751ce16b3" }, { "url": "VAADIN/build/mode-ejs-D8gAU1Ol.js", "revision": "81f20c489af0146fff11fc6f8695f72d" }, { "url": "VAADIN/build/mode-elixir-B3SaXdQ9.js", "revision": "d00d3d9f681621067a26cd001470b7f4" }, { "url": "VAADIN/build/mode-elm-v0GfPz3K.js", "revision": "1d96bc6c4cd5f57581e3e977aba077ee" }, { "url": "VAADIN/build/mode-erlang-DygCdJXV.js", "revision": "aeba327eadfdb77bc0b29ec67783f536" }, { "url": "VAADIN/build/mode-forth-BSDxCvcd.js", "revision": "aac27e3230a47591c449b253ed4b2a3b" }, { "url": "VAADIN/build/mode-fortran-CfCv3xpp.js", "revision": "a59ef864532d70e1f4686b5221a42fc7" }, { "url": "VAADIN/build/mode-fsharp-JYJu1peb.js", "revision": "6c879108848281f2df6e732d135100fa" }, { "url": "VAADIN/build/mode-fsl-D_UpB9eU.js", "revision": "d7c76c7a3a900589ce7ff019c21e4bb6" }, { "url": "VAADIN/build/mode-ftl-34xGFSNN.js", "revision": "73d040bf053531e5f1d6dc424826a880" }, { "url": "VAADIN/build/mode-gcode-wJiEisWz.js", "revision": "5cc0d97f20794f8e9ec659d97c38ca20" }, { "url": "VAADIN/build/mode-gherkin-DM0HzSXO.js", "revision": "f10ab45e9ce4108cb1d2521ce1851dcc" }, { "url": "VAADIN/build/mode-gitignore-PrOQD-VE.js", "revision": "a3d72732ed88376e1a3b363803c7b7ee" }, { "url": "VAADIN/build/mode-glsl-DwEqWArK.js", "revision": "4fb1a23ffbaf94cb9eb3896cd80ff815" }, { "url": "VAADIN/build/mode-gobstones-kS-060pk.js", "revision": "93afdecc4953d5fa0b28a2b5338e1ff1" }, { "url": "VAADIN/build/mode-golang-DrGIltrg.js", "revision": "00f64f9601724cc32ef475e89498ebaa" }, { "url": "VAADIN/build/mode-graphqlschema-CIc0EkUq.js", "revision": "71c54f9b78ddd75451afefd43744d7dd" }, { "url": "VAADIN/build/mode-groovy-B6jxSVaL.js", "revision": "4d18fcbaa4d6152ff734386fc0f14079" }, { "url": "VAADIN/build/mode-haml-DLEBObZ0.js", "revision": "2c63e28f4391c78f4cc6c3209a84a0bb" }, { "url": "VAADIN/build/mode-handlebars-VU4d-eLR.js", "revision": "a00f3b61b3c2bc2850789613dc5a1a5a" }, { "url": "VAADIN/build/mode-haskell_cabal-ohSapcUi.js", "revision": "4b2f00443f8aa1742f6cf7a59312815f" }, { "url": "VAADIN/build/mode-haskell-Bl57DekV.js", "revision": "f5fb8b5fd4d312ec73bd853fed7a59d8" }, { "url": "VAADIN/build/mode-haxe-ieYjuaMp.js", "revision": "5ef821339ec5fa2d04ae65dfd26dd744" }, { "url": "VAADIN/build/mode-hjson-uG6YBwEK.js", "revision": "6e694e8141e59f879c675061a594a3ce" }, { "url": "VAADIN/build/mode-html_elixir-Bp8SjzJE.js", "revision": "224eb8210644220cb7aac97e0c3598d4" }, { "url": "VAADIN/build/mode-html_ruby-Dn15Z5g_.js", "revision": "45062c09c96023ada9a97d070cc4b673" }, { "url": "VAADIN/build/mode-html-C7F0gRxN.js", "revision": "e71a4b127a8a7a5ec2ddd77261afa4b3" }, { "url": "VAADIN/build/mode-ini-tupvX5Aw.js", "revision": "1e5c20a061e8fd5a538fff530bd5691e" }, { "url": "VAADIN/build/mode-io-CAg4j6-x.js", "revision": "298af7f1a51950b21c590240dcf3c7e1" }, { "url": "VAADIN/build/mode-ion-DkrhDPT1.js", "revision": "0d4c8e6092bfcc94d7f9336aeaa1c122" }, { "url": "VAADIN/build/mode-jack-DcjcJokL.js", "revision": "4e31a8b3b7e16d9092282bbed6c5c89e" }, { "url": "VAADIN/build/mode-jade-BDxRihvh.js", "revision": "b6e424a3c69b0e8be111151af9affd1b" }, { "url": "VAADIN/build/mode-java-DhXU5o7y.js", "revision": "c40f135912325f64429d2c63d2753904" }, { "url": "VAADIN/build/mode-javascript-CtU-gPzW.js", "revision": "f41c1088462e703c12c38dc307ceb2d3" }, { "url": "VAADIN/build/mode-jexl-D5ptFoT9.js", "revision": "2169aa6619e77dda11ef04f5c81ed481" }, { "url": "VAADIN/build/mode-json-D5q84HwW.js", "revision": "05cc6cdd2110ecbc94428c39ff1ca5cb" }, { "url": "VAADIN/build/mode-json5-BuyM-pYa.js", "revision": "7ffd267dcfa48573b2024e4cdbd173fb" }, { "url": "VAADIN/build/mode-jsoniq-CtdxauJr.js", "revision": "3891993cefaa604a107100346797aaf9" }, { "url": "VAADIN/build/mode-jsp-BDbR100a.js", "revision": "7c92f6fb0a3fa79a220d9e0d584e7cdf" }, { "url": "VAADIN/build/mode-jssm-CbsmnKXD.js", "revision": "42bd7ec796d2cc22fb8f4d47d635bb01" }, { "url": "VAADIN/build/mode-jsx-B8HtysKh.js", "revision": "8ec3d5b625471df5e5afc7d00c17619c" }, { "url": "VAADIN/build/mode-julia-BpdP-mUQ.js", "revision": "fa138450d9ea767c9f3847a708f23640" }, { "url": "VAADIN/build/mode-kotlin-BxjWaaOe.js", "revision": "3a0277b4a4b6ed6f77eb14498bcc8483" }, { "url": "VAADIN/build/mode-latex-Dkk4g5Od.js", "revision": "59a2b9e6f8efc1270d5e1a116e0227a2" }, { "url": "VAADIN/build/mode-latte-Bl7wpHBk.js", "revision": "d6d5391373897720e951d21e9f09cc30" }, { "url": "VAADIN/build/mode-less-DkwKYBid.js", "revision": "1e313b16b74c94ee0b3de0830fd6c14e" }, { "url": "VAADIN/build/mode-liquid-BlEiuTka.js", "revision": "5554d276b825e0425dda4bb6fc454e0d" }, { "url": "VAADIN/build/mode-lisp-DMJvUxzC.js", "revision": "438b8d93f6fc72b02b5426f268ad393f" }, { "url": "VAADIN/build/mode-livescript-C5cX23hB.js", "revision": "44fe2ddaaed5cbcd420ae0d159f91dbd" }, { "url": "VAADIN/build/mode-logiql-CgPVrZUv.js", "revision": "0dbc7ceb2a9e702f13fca0c97765e4c4" }, { "url": "VAADIN/build/mode-logtalk-DSu9tdhF.js", "revision": "985987e0783c11fb64085f5c5a6d4747" }, { "url": "VAADIN/build/mode-lsl-AcAmVrG2.js", "revision": "2df647e37890a9b82edb39aeee17328e" }, { "url": "VAADIN/build/mode-lua-DJT_9EIc.js", "revision": "ea58ce22c4458f7ba325ea506fdbda69" }, { "url": "VAADIN/build/mode-luapage-DmXe2eOR.js", "revision": "3eeae7effec0c3999c2ed8710b29ed7c" }, { "url": "VAADIN/build/mode-lucene-UUfOcqJo.js", "revision": "527bff4e280fd53724bf09546c757652" }, { "url": "VAADIN/build/mode-makefile-CkdUAiVo.js", "revision": "20aea53fac76b6914a59e19cdd970911" }, { "url": "VAADIN/build/mode-markdown-D-wFYh78.js", "revision": "345558ff3282909df5278bdf5a93681c" }, { "url": "VAADIN/build/mode-mask-9W39FBGG.js", "revision": "96cff411d156cda2e972ffd94b40b4a9" }, { "url": "VAADIN/build/mode-matlab-BuAK7b5a.js", "revision": "5e257f56308e766aa53feb2b0c2eb459" }, { "url": "VAADIN/build/mode-maze-TR7KWJ9k.js", "revision": "267e917b6717014dc110d5a4d91e658d" }, { "url": "VAADIN/build/mode-mediawiki-2I2JKM2S.js", "revision": "963905cf2c5cb0dedb274902f398e8e2" }, { "url": "VAADIN/build/mode-mel-DuaT_-9m.js", "revision": "dcd0d3d57f6dac9be29dbaea1c596bff" }, { "url": "VAADIN/build/mode-mips-CIeeG0Qq.js", "revision": "a191cac27ed972ed380764e72b562536" }, { "url": "VAADIN/build/mode-mixal-D4TFFMC8.js", "revision": "73b948b8af0c1b1826af37cf0345a94a" }, { "url": "VAADIN/build/mode-mushcode-DNnvq-I0.js", "revision": "f45ccaefc2864ae1e4a8286797503582" }, { "url": "VAADIN/build/mode-mysql-Bi_X5DHc.js", "revision": "4771158722d1c3b0864307ebd08ef0a8" }, { "url": "VAADIN/build/mode-nginx-Cyv8pcJo.js", "revision": "9b635f63b26f08ff494d2c050acdf9ff" }, { "url": "VAADIN/build/mode-nim-DmDRv6IF.js", "revision": "1bcffdffa27a798047ad491d22d89c77" }, { "url": "VAADIN/build/mode-nix-CQk9lHD7.js", "revision": "dec5363d9641913e4d9872c9d5f3e0eb" }, { "url": "VAADIN/build/mode-nsis-lvCPEA_f.js", "revision": "37fa97b7422f262052f28063d8949b24" }, { "url": "VAADIN/build/mode-nunjucks-CJ2xb7cK.js", "revision": "e3853a7d4e07d4968239a42478edfa6a" }, { "url": "VAADIN/build/mode-objectivec-BPldVdbc.js", "revision": "947b6f17090cf02ed5ea2e0df164a5dd" }, { "url": "VAADIN/build/mode-ocaml-YV9yhm9q.js", "revision": "499c2f4f9df135541f854bcc013522bb" }, { "url": "VAADIN/build/mode-partiql-CG9hd0lS.js", "revision": "f5d9a174aca49053f51f441c056f6859" }, { "url": "VAADIN/build/mode-pascal-F047OR0j.js", "revision": "8411e17d17f7adb7a9f581c416da4f80" }, { "url": "VAADIN/build/mode-perl-CP4uWtyx.js", "revision": "caebb9beaa4ffe941cca5d4019232610" }, { "url": "VAADIN/build/mode-pgsql-BEWn6WJZ.js", "revision": "2e86120415d2d21a92ca95e6b97d6677" }, { "url": "VAADIN/build/mode-php_laravel_blade-B3vUE9Qn.js", "revision": "92ce0d04684ab046b5676610257667a6" }, { "url": "VAADIN/build/mode-php-dtPhtN_B.js", "revision": "8e22a2190218f222f49b12a6b758b8a8" }, { "url": "VAADIN/build/mode-pig-CJea13pr.js", "revision": "4252dff32e1d792bc280a6e7158d305a" }, { "url": "VAADIN/build/mode-plain_text-UAN1g6e0.js", "revision": "17268cf008a12241c7659decac27898f" }, { "url": "VAADIN/build/mode-plsql-CJ34bkmy.js", "revision": "4905164de833967e5e8358fc013b404c" }, { "url": "VAADIN/build/mode-powershell-CGSrbxlB.js", "revision": "08af361a564d903cd1c5a8d6624b2abd" }, { "url": "VAADIN/build/mode-praat-B2GSVSCj.js", "revision": "95aca7667290f9ab82882e9dbbf4d941" }, { "url": "VAADIN/build/mode-prisma-BTKbKn0U.js", "revision": "55b2a2d2126b8c8521bb37371609cc76" }, { "url": "VAADIN/build/mode-prolog-Dg1ZV2vX.js", "revision": "d422e0480e1f83c835ac1c187f7b9525" }, { "url": "VAADIN/build/mode-properties-qUqM8rz-.js", "revision": "0d3274765e54d56dca1006768e6394da" }, { "url": "VAADIN/build/mode-protobuf-D-CVk4GV.js", "revision": "aa650da76b03e01fd56249ffed8b7bd6" }, { "url": "VAADIN/build/mode-puppet-CLuDjo2Q.js", "revision": "ddc3feb6eafc8c1783f460f394ccab3f" }, { "url": "VAADIN/build/mode-python-C07JxQG7.js", "revision": "e65469c663929139f509f30e61a4192a" }, { "url": "VAADIN/build/mode-qml-Hqhyfo_N.js", "revision": "4411dfb2a1f57385f30e3e73c54d5b02" }, { "url": "VAADIN/build/mode-r-DqnUjJ8P.js", "revision": "2e69c6a186efdb2805ee0d2692f1d9f7" }, { "url": "VAADIN/build/mode-raku-BokGoH5j.js", "revision": "0e28a4808cc9ad9de70791b1d23469e9" }, { "url": "VAADIN/build/mode-razor-C0Rvd4hM.js", "revision": "df9c027220fe3b858cc219c8497fdf56" }, { "url": "VAADIN/build/mode-rdoc-CnecEklQ.js", "revision": "bf0750d20d5aa2cd9dc7f907a8e9b0e2" }, { "url": "VAADIN/build/mode-red-CmNmX6Qm.js", "revision": "b13ef5fefbd55ad904fec8889daba2f9" }, { "url": "VAADIN/build/mode-redshift-CzidbGPN.js", "revision": "fed29b5f0e519221a870afd85e4dc2f4" }, { "url": "VAADIN/build/mode-rhtml-CNusU366.js", "revision": "bd25d3e7ed422f351626ba23cd42b2b9" }, { "url": "VAADIN/build/mode-robot-Ds7HhERn.js", "revision": "da352f94f85505de1f1c73b3b56ae3bd" }, { "url": "VAADIN/build/mode-rst-jM2cKBTb.js", "revision": "83d1d49d46765b9ce7094809e719e1ed" }, { "url": "VAADIN/build/mode-ruby-qGKsPRHD.js", "revision": "be25fc2ca7c80bd1319c9729bf479dfb" }, { "url": "VAADIN/build/mode-rust-C4EchWJO.js", "revision": "91bc3b58920e2e76a4a8408353f61c7a" }, { "url": "VAADIN/build/mode-sac-CN9tuQ9x.js", "revision": "4865bc346456aacd23c1882f2016725a" }, { "url": "VAADIN/build/mode-sass-D7k5bLGa.js", "revision": "ef04c19f60f43fe84ed9dce3e8fd8e9c" }, { "url": "VAADIN/build/mode-scad-CwDuRdcC.js", "revision": "5fefbdd7b51ea940a995e4d5b5dc6796" }, { "url": "VAADIN/build/mode-scala-Dhyk3SAb.js", "revision": "393a6a515011f3f5da2403dc4206f8e8" }, { "url": "VAADIN/build/mode-scheme-_9KJ0QS3.js", "revision": "dfde4b2da87020a571e6bd3679ed5c02" }, { "url": "VAADIN/build/mode-scrypt-CHjW3X24.js", "revision": "8cd9616e24afc4197f1546941ff5e62e" }, { "url": "VAADIN/build/mode-scss-DRacYDSz.js", "revision": "1afdf917f395b56fbe419dff1814f2f8" }, { "url": "VAADIN/build/mode-sh-GFZVEPxh.js", "revision": "f59efe42cf908756fd09b31661735018" }, { "url": "VAADIN/build/mode-sjs-OToxnpwh.js", "revision": "2483b5c01dbe6ad185a0c95a1872b821" }, { "url": "VAADIN/build/mode-slim-H0ez5TQf.js", "revision": "f4ea6dc8ddc9d792b98eb9fc599dfac0" }, { "url": "VAADIN/build/mode-smarty-D2SqpTeA.js", "revision": "011938c29ee57a2fef723d9b712558da" }, { "url": "VAADIN/build/mode-smithy-DK0FKwbQ.js", "revision": "f9f0c129e23467a181f248269c7232eb" }, { "url": "VAADIN/build/mode-snippets-CM3pYZ1N.js", "revision": "7e7274e99b17ad783eb330b3b50066b5" }, { "url": "VAADIN/build/mode-soy_template-BuUbfMA3.js", "revision": "660e817b74cafaf2cf3622a20ac9c270" }, { "url": "VAADIN/build/mode-space-h8rPW8SU.js", "revision": "096c46dc99084b51769e71550b4d3343" }, { "url": "VAADIN/build/mode-sparql-Dr8ZnHDw.js", "revision": "17d4dc0b7f5339d9ca24ae8a1d42dfcc" }, { "url": "VAADIN/build/mode-sql-TwRi_iJ0.js", "revision": "8547d43c37cdc2ed58668310bd12b06e" }, { "url": "VAADIN/build/mode-sqlserver-Bcq_XSe9.js", "revision": "52fca1dd5bb32913b8a10d9193353090" }, { "url": "VAADIN/build/mode-stylus-DxUGI1sc.js", "revision": "7c57d97bae7f2762b5fef14ee5f79c85" }, { "url": "VAADIN/build/mode-svg-ISAqX6Ku.js", "revision": "f296fc29d0e4720070a6220acc123be2" }, { "url": "VAADIN/build/mode-swift-CBNs1qVh.js", "revision": "cea2b04234386c6ab056b9ad7e82e0cc" }, { "url": "VAADIN/build/mode-tcl-BTkf6eN8.js", "revision": "5174843c565b6d282d4ed686379b6757" }, { "url": "VAADIN/build/mode-terraform-kz4jjmCb.js", "revision": "0ebb770d934072aeb1a21c711ec76c88" }, { "url": "VAADIN/build/mode-tex-D1uHDtLr.js", "revision": "2908208c9d735f9439ff62c02563a431" }, { "url": "VAADIN/build/mode-text-CNbZQ7ch.js", "revision": "8d03b5c2eafe37746c18c6cb8b6c5595" }, { "url": "VAADIN/build/mode-textile-BD9pp9kK.js", "revision": "4c6e87101529911b2045913c3fe71948" }, { "url": "VAADIN/build/mode-toml-D2B6uSTA.js", "revision": "72f8faa505104dcdc027fc12be1ebb6f" }, { "url": "VAADIN/build/mode-tsx-CfyGJBEh.js", "revision": "0f61f826a8938344a5997b608af9106b" }, { "url": "VAADIN/build/mode-turtle-DcSD8cuR.js", "revision": "094b661b7bb3784d470fa8bf7732c6da" }, { "url": "VAADIN/build/mode-twig-Cycl17mu.js", "revision": "1229291a20507b8b4b7ae04e4f8fefc3" }, { "url": "VAADIN/build/mode-typescript-D6E_6oZl.js", "revision": "f533588fd72b334984202b3224693ada" }, { "url": "VAADIN/build/mode-vala-Dsh4Pbxm.js", "revision": "c84634064739c98321a306ad98cce016" }, { "url": "VAADIN/build/mode-vbscript-LQX0YIk3.js", "revision": "1dfcc457ca6b5b3fe80f0a78341e8f0e" }, { "url": "VAADIN/build/mode-velocity-DseSjFGb.js", "revision": "69594796c9f2a389d6059d1ac015139b" }, { "url": "VAADIN/build/mode-verilog-L-vpcA0f.js", "revision": "fe3e54b8d13e31476ec78b73420e80b9" }, { "url": "VAADIN/build/mode-vhdl-C91Qnecz.js", "revision": "03309fc78aedfacc96df38dcaeeb38e8" }, { "url": "VAADIN/build/mode-visualforce-jkyv7T9y.js", "revision": "c7de2b7090f6740995bac954f87eefbf" }, { "url": "VAADIN/build/mode-wollok-CpI9jgIp.js", "revision": "82ab1f8b3adcc7627a5850777faeb1b9" }, { "url": "VAADIN/build/mode-xml-DjXnf2Ck.js", "revision": "09c924e99e78e93afcda34b0caa3f22f" }, { "url": "VAADIN/build/mode-xquery-6KYheM-A.js", "revision": "863e858bf7cb8653a24e8fb25017c1b0" }, { "url": "VAADIN/build/mode-yaml-DfISer63.js", "revision": "add08b41b4762d277276ffaf839cc6ec" }, { "url": "VAADIN/build/mode-zeek-BQItrTMI.js", "revision": "9b59046326dd3bb10be2817fd3777c74" }, { "url": "VAADIN/build/mushcode-BTzsGiCo.js", "revision": "723ce60143b756471a36426f86590190" }, { "url": "VAADIN/build/mysql-BAxiBshq.js", "revision": "32fd5a8d50df6d7656632407dc8d6c3a" }, { "url": "VAADIN/build/network_utils-D8q6tbg2.js", "revision": "a7aff159c2d1ddd077b1290c88afcb1d" }, { "url": "VAADIN/build/network-DQ71W2j7.js", "revision": "9bcf2bfa7eb0a43acc28c444c033c23e" }, { "url": "VAADIN/build/nginx-C4hGVytX.js", "revision": "9e43f5e49728f713246676d804d9c2a1" }, { "url": "VAADIN/build/nim-CxabCrSQ.js", "revision": "5e8e239b5b28b539348d9cf3178bfb1c" }, { "url": "VAADIN/build/nix-B0wWsIER.js", "revision": "300099db79ad21acdde8b4dcc491937b" }, { "url": "VAADIN/build/nsis-B3JMgGM5.js", "revision": "7c8920ac0dbaab93008a9c1c5aa55b7d" }, { "url": "VAADIN/build/nunjucks-C10hMHlL.js", "revision": "b198c9e5f7b728cde3ba269651f17b59" }, { "url": "VAADIN/build/objectivec-DI01-YvV.js", "revision": "5db943bfbcec28442535967fb502b14d" }, { "url": "VAADIN/build/ocaml-CO_iXOx1.js", "revision": "5f9658b6d35baf7a9681f03fab7cc684" }, { "url": "VAADIN/build/partiql-DwjyLVWa.js", "revision": "70c120cf72aa4f7dd6fa2f3cf7b1a87c" }, { "url": "VAADIN/build/pascal-DgmTK82U.js", "revision": "ce181c2b5a89946919f18d049a17f4ab" }, { "url": "VAADIN/build/perl-DSAGy57W.js", "revision": "10f3564433c0f495659fc71c238df328" }, { "url": "VAADIN/build/pgsql-CC-W2Bk3.js", "revision": "fd08d7c3d19d47fbe440e1af7dc8aefe" }, { "url": "VAADIN/build/php_laravel_blade-3-5XWaKL.js", "revision": "3d8dc1da79917a8c482076189bdafd01" }, { "url": "VAADIN/build/php-BKAo83vw.js", "revision": "02d63d1f035fbcc4525231cde4dc651a" }, { "url": "VAADIN/build/pig-DdOyM12y.js", "revision": "93f1a8705af3c85e8a782a9efdb10ee7" }, { "url": "VAADIN/build/plain_text-CFSz2CgM.js", "revision": "7dc97872d35e4243125f63b80bd6518d" }, { "url": "VAADIN/build/plsql-BjPAAcyb.js", "revision": "9b7982cb272e66aca27568dd15fe9b8e" }, { "url": "VAADIN/build/powershell-KDXzftK8.js", "revision": "4e1d94ce321c39f574a1aa452d0d7d5c" }, { "url": "VAADIN/build/praat-Dx4607PJ.js", "revision": "3cefba84886ecce53cccee1a6a553413" }, { "url": "VAADIN/build/prisma-Ljp5_IW7.js", "revision": "f739c78422edaa580139a9ac6344ade6" }, { "url": "VAADIN/build/prolog-BZ88XgdS.js", "revision": "626958b5275ebe114818e54d1dfa0ca7" }, { "url": "VAADIN/build/properties-Bu0mIGMc.js", "revision": "6859c9f78a3c0d905a0f3576f19de380" }, { "url": "VAADIN/build/protobuf-DcAoLwK-.js", "revision": "d41e99cccfdbc27a035f0f7c891bd4a2" }, { "url": "VAADIN/build/puppet-CTgtNyV1.js", "revision": "0397324f2bc34313ad1c6f7e28dd9f50" }, { "url": "VAADIN/build/python-CsdgoHCf.js", "revision": "e909b8c603f25b01cec64e6ef82f0df1" }, { "url": "VAADIN/build/qml-0KJQqBMu.js", "revision": "bb14f0592a1dc9b095a27b983e3b28e7" }, { "url": "VAADIN/build/r-DPVrkYkv.js", "revision": "e9c443e8dfb4b6a39e30b4833bc4e24c" }, { "url": "VAADIN/build/raku-Ooc0zOz4.js", "revision": "1cf1fe8b76e35f08e5473ca24a90c07b" }, { "url": "VAADIN/build/razor-CAHTi-am.js", "revision": "c8d2c6b85e8b1fad359e76afe7545bfa" }, { "url": "VAADIN/build/rdoc-DUT14Z3M.js", "revision": "136faa326e867510458d320ffbfda80d" }, { "url": "VAADIN/build/red-Dw40_KUl.js", "revision": "e540eea2ec029af9d9f4667548d1b553" }, { "url": "VAADIN/build/redshift-CVMNG_kM.js", "revision": "e5c7655bf8e1a50acbd082fcc6a9529f" }, { "url": "VAADIN/build/rhtml-CRbd-LHv.js", "revision": "30d5cbf91c75627d9b5c0ec57dfe9bce" }, { "url": "VAADIN/build/robot-BOKUqWxJ.js", "revision": "898deede0c25616c114393fd28133a88" }, { "url": "VAADIN/build/rst-CzolmwWx.js", "revision": "bc9085a8c0f24091ea3e2421305bf49e" }, { "url": "VAADIN/build/ruby-5KbBmAcE.js", "revision": "f0a359bc0da8b15db5ad423b8317041f" }, { "url": "VAADIN/build/rust-DeBMaUAk.js", "revision": "ebdcd839783f31a3b06d37878f4eb24d" }, { "url": "VAADIN/build/sac-B7wTJdBF.js", "revision": "d7813ded300e6732f4d42a2797df04b7" }, { "url": "VAADIN/build/sass-L5IUQ-Ov.js", "revision": "46dccfe28b3db4920d60d5558dc4c5a5" }, { "url": "VAADIN/build/scad-2QVH5Qrw.js", "revision": "afb6d93be3dab6b65a83213a465d6ca4" }, { "url": "VAADIN/build/scala-DyrNbsd4.js", "revision": "d373f3c03b41625804e2346ba4360a45" }, { "url": "VAADIN/build/scheme-CXywU8WP.js", "revision": "7a477e3f61d21da9d0fee43c638e3663" }, { "url": "VAADIN/build/scrypt-cjgfQXUi.js", "revision": "0539068cf84bd54424fac8a45fee0a16" }, { "url": "VAADIN/build/scss-DCqFXPrx.js", "revision": "aa4acfb24393692d3cdc07bf3817a40c" }, { "url": "VAADIN/build/sh-CUXqli-k.js", "revision": "93e02bbea614d6714aa24c69fb2af68c" }, { "url": "VAADIN/build/sjs-DxbwMAia.js", "revision": "3d629f2a597a9a2503daa338a4131a71" }, { "url": "VAADIN/build/slim-8LRXtIeS.js", "revision": "5170f35c187a0ec4c2a3d066dc5e9693" }, { "url": "VAADIN/build/smarty-jC4PGs-v.js", "revision": "dac9ff4044bdf8c0c4a50e14eea4c7b1" }, { "url": "VAADIN/build/smithy-Ba0JWULR.js", "revision": "10e0d111764f75000a61bc21e07a4bbc" }, { "url": "VAADIN/build/snippets-CUkuxtIb.js", "revision": "aff9e8eae7a7ea0053ffca2330e04e0d" }, { "url": "VAADIN/build/soy_template-Dw1tAtgv.js", "revision": "38ab9d0cc50356706ba251372aa65d9d" }, { "url": "VAADIN/build/space-fQKd2I6P.js", "revision": "4ab840291cf1050ed01a0293aa1f9ad2" }, { "url": "VAADIN/build/sparql-b8RUZxxC.js", "revision": "64491642da1e7d32ec6d949e1e8a00b9" }, { "url": "VAADIN/build/sql-C8a-HlJg.js", "revision": "a8c09793fefe434550b649b0fde70d80" }, { "url": "VAADIN/build/sqlserver-D9bBM1fO.js", "revision": "903b61a638b24f8d71927790dacba5f3" }, { "url": "VAADIN/build/stylus-C7HT_yxI.js", "revision": "785dd26615cc75cec7c30cf1c3e9e227" }, { "url": "VAADIN/build/svg-BNSG92dH.js", "revision": "737defb30ab9acaed9fffe3453a76f29" }, { "url": "VAADIN/build/swift-CVve_aOZ.js", "revision": "5bca55aee1177fc5fff3a0d61963e980" }, { "url": "VAADIN/build/tcl-COcj21Vd.js", "revision": "574e5c3d998b41bda863e84b91d3ece5" }, { "url": "VAADIN/build/terraform-CtbYMEbz.js", "revision": "04c7a3e06049bbf191f2cc093f4d6c94" }, { "url": "VAADIN/build/tex-ClGF4MIc.js", "revision": "7940d8873d15e6f919775c7a9551baa9" }, { "url": "VAADIN/build/text-BRgu3ccp.js", "revision": "bb4918ef25c756f846c09ceeba9ea31b" }, { "url": "VAADIN/build/textile-ze3ZTT4C.js", "revision": "d66815d40773bfc656b1c02589b1c82a" }, { "url": "VAADIN/build/theme-ambiance-DPsxB89J.js", "revision": "db5bf2865d81884184b17ec70719465b" }, { "url": "VAADIN/build/theme-chaos-BWLuYvyE.js", "revision": "811e26098724f713b8fed0a82aef838f" }, { "url": "VAADIN/build/theme-chrome-7Bv8aRG1.js", "revision": "d21db252e82baaeed83b01fd4a9f3fed" }, { "url": "VAADIN/build/theme-cloud9_day-CPm6oT8E.js", "revision": "5a2305ec0fbf232aca1d31497aabc62b" }, { "url": "VAADIN/build/theme-cloud9_night_low_color-C9BbDvkV.js", "revision": "bba50c39e932c48446ea9c3b46d437ad" }, { "url": "VAADIN/build/theme-cloud9_night-CF4EzIRI.js", "revision": "a67220c5fab684e5a6197226ef107c09" }, { "url": "VAADIN/build/theme-clouds_midnight-CXI_O0pH.js", "revision": "65c563f051c2456b422e4fd313f1b697" }, { "url": "VAADIN/build/theme-clouds-CWazKKSC.js", "revision": "c3ed0dfe8016266d959c41ee1fc3efad" }, { "url": "VAADIN/build/theme-cobalt-CqZuienG.js", "revision": "055ace1090ab078a0bcf6a42ce81f46c" }, { "url": "VAADIN/build/theme-crimson_editor-CMoPwZMV.js", "revision": "e10ac6dfc2f3ee23c5d76d8fdbf76172" }, { "url": "VAADIN/build/theme-dawn-BYeWjL3w.js", "revision": "2f78810f32bdb059e1a376b0bc1641b7" }, { "url": "VAADIN/build/theme-dracula-CALACo--.js", "revision": "2b24fa175fad23887f796c111d26203e" }, { "url": "VAADIN/build/theme-dreamweaver-N9E16G0w.js", "revision": "de7d249ad0bf54b09d818d1d454c0439" }, { "url": "VAADIN/build/theme-eclipse-MficWMIw.js", "revision": "0dd60d03cfd79a34d5fc04105f970659" }, { "url": "VAADIN/build/theme-github-CqlTqZ0j.js", "revision": "2f2a4fb5d43ef370df603f95d54948f6" }, { "url": "VAADIN/build/theme-gob-BUZEIBVj.js", "revision": "6ee4d2471f71d58924a4fa4cd697a631" }, { "url": "VAADIN/build/theme-gruvbox_dark_hard-ocoBgniQ.js", "revision": "cfc106465f3bd8385c53e1fbe3dde993" }, { "url": "VAADIN/build/theme-gruvbox_light_hard-DSTDeljz.js", "revision": "f03c65f84ac5f850635850cb6c398009" }, { "url": "VAADIN/build/theme-gruvbox-0Or2nqsk.js", "revision": "e976f467997699c55f3a17c0c3758dbf" }, { "url": "VAADIN/build/theme-idle_fingers-CFG3Q4I-.js", "revision": "7325d08df1ce2f8c178e6560c4f26840" }, { "url": "VAADIN/build/theme-iplastic-DpVJT8Lc.js", "revision": "292b9265d40e7069d43c950738c59c52" }, { "url": "VAADIN/build/theme-katzenmilch-_tERT0dq.js", "revision": "67106289daf0ef7d9cb0669e875b5340" }, { "url": "VAADIN/build/theme-kr_theme-DooxqV9d.js", "revision": "e7639012f1f1c66af5e328cf9478554c" }, { "url": "VAADIN/build/theme-kuroir-BoNXf9g_.js", "revision": "940b853191eba5eb2f7cd31c3f924c08" }, { "url": "VAADIN/build/theme-merbivore_soft-BAOqDtvm.js", "revision": "b40f3c8fc4020b84cf7e1f9427c1dc17" }, { "url": "VAADIN/build/theme-merbivore-WuU4dpBT.js", "revision": "3bca3065bc47c8e2093851a92a0bed8e" }, { "url": "VAADIN/build/theme-mono_industrial-CgSelEJX.js", "revision": "d20f85c17db4db44983905aeaa1fa8ec" }, { "url": "VAADIN/build/theme-monokai-Wp9mxv7e.js", "revision": "a1bf42489c9c3ef74b8e329229d9d8e4" }, { "url": "VAADIN/build/theme-nord_dark-BC5EjzhY.js", "revision": "d74e839663c510cb006b59e4a7f4ea0d" }, { "url": "VAADIN/build/theme-one_dark-L3FIyZ9b.js", "revision": "24c9293dc6f975040c575465a2754c30" }, { "url": "VAADIN/build/theme-pastel_on_dark-_9Ie98LR.js", "revision": "8b1f8603bf733d03eb801e9f877d314b" }, { "url": "VAADIN/build/theme-solarized_dark-8XR1PIcH.js", "revision": "14796fde8dc0949e960984127f5d1a30" }, { "url": "VAADIN/build/theme-solarized_light-Cy741MpR.js", "revision": "032ecc5ebacb3f012c1fc17cd26866a0" }, { "url": "VAADIN/build/theme-sqlserver-DIh9foKA.js", "revision": "4949ec6506ec69d14eece03417e80eae" }, { "url": "VAADIN/build/theme-terminal-CA555wF1.js", "revision": "8970dac015f8bd9896ddf9c215e25ed8" }, { "url": "VAADIN/build/theme-textmate-C1qSGfGb.js", "revision": "81f96dbe2e99fe85ea074a9897e39fb0" }, { "url": "VAADIN/build/theme-tomorrow_night_blue-BdrPT3Mu.js", "revision": "b012b31a339aecded64d5d78344baf74" }, { "url": "VAADIN/build/theme-tomorrow_night_bright-CGJKpXsx.js", "revision": "34c7650772e44411d113b23a1df9dab6" }, { "url": "VAADIN/build/theme-tomorrow_night_eighties-s3haYDFc.js", "revision": "29e536e8e049e3c2afd923a1a9ea1efb" }, { "url": "VAADIN/build/theme-tomorrow_night-C2eR9ITj.js", "revision": "97bbd45142f4b8f86df5ec85b0d286d1" }, { "url": "VAADIN/build/theme-tomorrow-DmoUu9Ca.js", "revision": "d160f3f976c1c17eb803a9c4ddc45945" }, { "url": "VAADIN/build/theme-twilight-B1tSTl4V.js", "revision": "baa1ca86fd18d62b765c4ff74129735b" }, { "url": "VAADIN/build/theme-vibrant_ink-C5jhTyuY.js", "revision": "61fbe450a266f142f3928d6121d3af22" }, { "url": "VAADIN/build/theme-xcode-MDBsd_DA.js", "revision": "6c8e45f367413f23aefdf6c6c40660be" }, { "url": "VAADIN/build/toml-wLHH_BBD.js", "revision": "4e774811b1e2ab9a19cab3e8e4a73e91" }, { "url": "VAADIN/build/tsx-D4i1x-Cv.js", "revision": "92a3a130524c8e675abe8dc3a3a6081d" }, { "url": "VAADIN/build/turtle-Cr92_dzi.js", "revision": "bfc5054e99f4d39dad33dc9ec764f8d8" }, { "url": "VAADIN/build/twig-DD1e8NRm.js", "revision": "73412555759011ee6ff255319a3288cf" }, { "url": "VAADIN/build/typescript-fg5Od6B4.js", "revision": "3740ff583d55d770e5bc99057800263b" }, { "url": "VAADIN/build/vala-BHdwXI_e.js", "revision": "bbc52553add2e4b0813a4affdb0b8d2e" }, { "url": "VAADIN/build/vbscript-Zn6jpK4C.js", "revision": "6bb30b52e02244857643f9e07e8d7bc7" }, { "url": "VAADIN/build/velocity-BE0-4sPx.js", "revision": "20bae244cde1bc4bd717a626b1b995ba" }, { "url": "VAADIN/build/verilog-DVNE1w0o.js", "revision": "0f8139cb24373a8e63f7afd5006f18f6" }, { "url": "VAADIN/build/vhdl-6T3E-LZc.js", "revision": "9f78747a931ccd162b75c5f811a7be4a" }, { "url": "VAADIN/build/visualforce-DIzJLQPW.js", "revision": "e5f16e6a048ed0f55fe00168b55be0ae" }, { "url": "VAADIN/build/wollok-B7sJtEN2.js", "revision": "2263652ce3a819fe4d0713cb837c1942" }, { "url": "VAADIN/build/worker-base-BkPfwM1t.js", "revision": "bcdba836f2062a2e55a93b1034ea8d70" }, { "url": "VAADIN/build/worker-coffee--pIC3qtT.js", "revision": "5124cca92670b65ddcaf08e6a96c0915" }, { "url": "VAADIN/build/worker-css-CgrrlzHp.js", "revision": "8174c1373c54b3253448fd9ff455955d" }, { "url": "VAADIN/build/worker-html-Dc4WOuL1.js", "revision": "d4221c8857f9222a5a9d4cc45e99b50a" }, { "url": "VAADIN/build/worker-javascript-Ctz5SeBu.js", "revision": "7cdd6cf715792cdbff499b46a46e7cf6" }, { "url": "VAADIN/build/worker-json-Dat3zKGG.js", "revision": "84e2e01e10c1c0d96fe2707470bb4c1b" }, { "url": "VAADIN/build/worker-lua-DM_BaMJr.js", "revision": "2fea209a4742f3442699bc5d76032b4c" }, { "url": "VAADIN/build/worker-php-CX3cVJiE.js", "revision": "916800758ceb19d9d0b524f49a9a931e" }, { "url": "VAADIN/build/worker-xml-s_Oi8LlP.js", "revision": "5658528f3b3baf92ac530ec2113259c7" }, { "url": "VAADIN/build/worker-xquery-DNZBr1xw.js", "revision": "b28c0498907913f5902a0fa35ec19a3b" }, { "url": "VAADIN/build/worker-yaml-dx6ABCwK.js", "revision": "0d09a12af94edb94535b838567e2a60c" }, { "url": "VAADIN/build/xml-ISocCAqK.js", "revision": "e28dfc6a4e7fee6ad8db110f79ee6f0d" }, { "url": "VAADIN/build/xquery-Z2wIOFYa.js", "revision": "cddfa624449d926cde2709dc2a7911ec" }, { "url": "VAADIN/build/yaml-CJEdaXLg.js", "revision": "93ae419d8636d9503df6986dd2974c6c" }, { "url": "VAADIN/build/zeek-CkfZOq59.js", "revision": "ef0407d2d54c96bdae041225ce03a1ef" }];
let hasRootEntry = manifestEntries.findIndex((entry) => entry.url === ".") >= 0;
if (self.additionalManifestEntries?.length) {
  manifestEntries.push(...self.additionalManifestEntries.filter((entry) => entry.url !== "." || !hasRootEntry));
}
const offlinePath = ".";
const scope = new URL(self.registration.scope);
async function rewriteBaseHref(response) {
  const html = await response.text();
  return new Response(html.replace(/<base\s+href=[^>]*>/, `<base href="${self.registration.scope}">`), response);
}
function isManifestEntryURL(url) {
  return manifestEntries.some((entry) => getCacheKeyForURL(entry.url) === getCacheKeyForURL(`${url}`));
}
let connectionLost = false;
function checkConnectionPlugin() {
  return {
    async fetchDidFail() {
      connectionLost = true;
    },
    async fetchDidSucceed({ response }) {
      connectionLost = false;
      return response;
    }
  };
}
const networkOnly = new NetworkOnly({
  plugins: [checkConnectionPlugin()]
});
new NetworkFirst({
  plugins: [checkConnectionPlugin()]
});
registerRoute(
  new NavigationRoute(async (context) => {
    async function serveOfflineFallback() {
      const response = await matchPrecache(offlinePath);
      return response ? rewriteBaseHref(response) : void 0;
    }
    function serveResourceFromCache() {
      if (context.url.pathname === scope.pathname) {
        return serveOfflineFallback();
      }
      if (isManifestEntryURL(context.url)) {
        return matchPrecache(context.request);
      }
      return serveOfflineFallback();
    }
    if (!self.navigator.onLine) {
      const response = await serveResourceFromCache();
      if (response) {
        return response;
      }
    }
    try {
      return await networkOnly.handle(context);
    } catch (error) {
      const response = await serveResourceFromCache();
      if (response) {
        return response;
      }
      throw error;
    }
  })
);
precacheAndRoute(manifestEntries);
self.addEventListener("message", (event) => {
  if (typeof event.data !== "object" || !("method" in event.data)) {
    return;
  }
  if (event.data.method === "Vaadin.ServiceWorker.isConnectionLost" && "id" in event.data) {
    event.source?.postMessage({ id: event.data.id, result: connectionLost }, []);
  }
});
self.addEventListener("push", (e) => {
  const data = e.data?.json();
  if (data) {
    self.registration.showNotification(data.title, {
      body: data.body
    });
  }
});
self.addEventListener("notificationclick", (e) => {
  e.notification.close();
  e.waitUntil(focusOrOpenWindow());
});
async function focusOrOpenWindow() {
  const url = new URL("/", self.location.origin).href;
  const allWindows = await self.clients.matchAll({
    type: "window"
  });
  const appWindow = allWindows.find((w) => w.url === url);
  if (appWindow) {
    return appWindow.focus();
  } else {
    return self.clients.openWindow(url);
  }
}
