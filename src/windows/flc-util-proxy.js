const component = FullLegitCode.Util.Util;

let debug = false;

function decodeImage(successCallback, errorCallback, args) {
  try {
    let startTime;
    if (debug) {
      startTime = Date.now();
      console.log(`[FlcUtil.decodeImage] windows proxy start (time)=${startTime}`);
    }
    const encodedBytes = new Uint8Array(args[0]);
    const decodedBytes = new FullLegitCode.Util.ByteArrayWrapper();
    component.decodeImage(encodedBytes, decodedBytes).then(
      () => {
        if (debug) {
          const endTime = Date.now();
          console.log(`[FlcUtil.decodeImage] windows proxy end (time)=${endTime} (time delta)=${endTime - startTime}`);
        }
        successCallback(decodedBytes.bytes.buffer);
      },
      errorCallback
    );
  } catch (e) { errorCallback(e) }
}

function getIp(successCallback, errorCallback) {
  try {
    component.getIp().then(successCallback, errorCallback);
  } catch (e) { errorCallback(e) }
}

function setDebug(successCallback, errorCallback, args) {
  try {
    debug = args[0];
    successCallback();
  } catch (e) { errorCallback(e) }
}

module.exports = {
  decodeImage: decodeImage,
  getIp: getIp,
  setDebug: setDebug
};

require('cordova/exec/proxy').add('FlcUtil', module.exports);
