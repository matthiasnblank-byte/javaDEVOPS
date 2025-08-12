// Karma configuration for Angular CLI project
// Adds a no-sandbox headless Chrome launcher for CI Linux environments

module.exports = function (config) {
  config.set({
    browsers: ['ChromeHeadless'],
    customLaunchers: {
      ChromeHeadlessNoSandbox: {
        base: 'ChromeHeadless',
        flags: [
          '--no-sandbox',
          '--disable-gpu',
          '--disable-dev-shm-usage',
          '--disable-setuid-sandbox',
          '--remote-debugging-port=9222',
        ],
      },
    },
  });
};


