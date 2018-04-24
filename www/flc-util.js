const exec = require('cordova/exec');

function FlcUtil() {}

FlcUtil.acquireWakeLock = function(a, b, c) {
  if (a !== null && typeof a !== 'number') {
    FlcUtil.acquireWakeLock(null, a, b);
    return;
  }
  const timeout = a;
  const successCallback = b;
  const errorCallback = c;
  exec(
    function() {
      if (successCallback) {
        successCallback();
      }
    },
    function(error) {
      if (errorCallback) {
        errorCallback(error);
      }
    },
    'FlcUtil',
    'acquireWakeLock',
    [timeout || null]
  );
};

FlcUtil.releaseWakeLock = function(successCallback, errorCallback) {
  exec(
    function() {
      if (successCallback) {
        successCallback();
      }
    },
    function(error) {
      if (errorCallback) {
        errorCallback(error);
      }
    },
    'FlcUtil',
    'releaseWakeLock'
  );
};

FlcUtil.setKeepScreenOn = function(value, successCallback, errorCallback) {
  exec(
    function() {
      if (successCallback) {
        successCallback();
      }
    },
    function(error) {
      if (errorCallback) {
        errorCallback(error);
      }
    },
    'FlcUtil',
    'setKeepScreenOn',
    [value]
  );
};

FlcUtil.decodeImage = function(buffer, successCallback, errorCallback) {
  const startTime = Date.now();
  console.log('[FlcUtil.decodeImage] start (time)=' + startTime);
  exec(
    function(buffer) {
      const endTime = Date.now();
      console.log(`[FlcUtil.decodeImage] end (time)=${endTime} (time delta)=${endTime - startTime}`);
      if (successCallback) {
        successCallback(buffer);
      }
    },
    function(error) {
      if (errorCallback) {
        errorCallback(error);
      }
    },
    'FlcUtil',
    'decodeImage',
    [buffer]
  );
};

FlcUtil.getIp = function(successCallback, errorCallback) {
  exec(
    function(ip) {
      if (successCallback) {
        successCallback(ip);
      }
    },
    function(error) {
      if (errorCallback) {
        errorCallback(error);
      }
    },
    'FlcUtil',
    'getIp'
  );
};

module.exports = FlcUtil;

