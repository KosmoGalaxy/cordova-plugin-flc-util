const component = FullLegitCode.Util.Util;

module.exports = {

  decodeImage: function(successCallback, errorCallback, args) {
    try {
      const startTime = Date.now();
      console.log(`[FlcUtil.decodeImage] windows proxy start (time)=${startTime} --5`);
      const buffer = args[0];
      const bytes = new Uint8Array(buffer);
      const array = Array.prototype.slice.call(bytes, 0, bytes.length);
      component.decodeImage(array).then(
        data => {
          const receiveTime = Date.now();
          console.log(`[FlcUtil.decodeImage] windows proxy receive (time)=${receiveTime} (start time delta)=${receiveTime - startTime}`);
          const bytes = new Uint8Array(data.length);
          bytes.set(data);
          const endTime = Date.now();
          console.log(`[FlcUtil.decodeImage] windows proxy end (time)=${endTime} (receive time delta)=${endTime - receiveTime} (start time delta)=${endTime - startTime}`);
          successCallback(bytes.buffer);
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
