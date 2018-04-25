const component = FullLegitCode.Util.Util;

module.exports = {

  decodeImage: function(successCallback, errorCallback, args) {
    try {
      const startTime = Date.now();
      console.log(`[FlcUtil.decodeImage] windows proxy start (time)=${startTime}`);
      const encodedBytes = new Uint8Array(args[0]);
      const decodedBytes = new FullLegitCode.Util.ByteArrayWrapper();
      component.decodeImage(encodedBytes, decodedBytes).then(
        () => {
          const endTime = Date.now();
          console.log(`[FlcUtil.decodeImage] windows proxy end (time)=${endTime} (time delta)=${endTime - startTime}`);
          successCallback(decodedBytes.bytes.buffer);
        },
        errorCallback
      );
    } catch (e) { errorCallback(e) }
  },

  getIp: function(successCallback, errorCallback) {
    try {
      component.getIp().then(successCallback, errorCallback);
    } catch (e) { errorCallback(e) }
  }

};

require('cordova/exec/proxy').add('FlcUtil', module.exports);
