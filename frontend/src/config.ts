interface Config {
  API_URL: string;
  // Only certain types qualify as "Simple requests" (and thus do not require the preflight request).
  // Used for local dev setup.
  // https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#Simple_requests
  POST_CONTENT_TYPE?: string;
}

let localConfig = {};
try {
  localConfig = require('./config.json');
} catch (err) {
  console.warn('No config file found, using default');
}

let config = {
  // Add any dev or default configs here, but note that they will be overridden
  // by values in `src/config.json`, if present.
  API_URL: 'http://localhost:8080',
};

const appConfig: Config = Object.assign({}, config, localConfig);
export default appConfig;
