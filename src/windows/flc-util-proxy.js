const component = FullLegitCode.Util.Util;

module.exports = {

  decodeImage: function(successCallback, errorCallback, args) {
    try {
      const buffer = args[0];
      const bytes = new Uint8Array(buffer);
      const array = Array.prototype.slice.call(bytes, 0, bytes.length);
      component.decodeImage(array).then(
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
