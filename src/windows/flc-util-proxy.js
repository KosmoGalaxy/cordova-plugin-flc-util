const component = FullLegitCode.Util.Util;

module.exports = {

  decodeImage: function(successCallback, errorCallback, args) {
    try {
      const bytes = args[0];
      console.log('[FlcUtil.decodeImage] bytes', bytes);
      component.decodeImage(bytes).then(
        data => successCallback(Uint8Array.from(data).buffer),
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
