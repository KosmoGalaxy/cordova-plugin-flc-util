import Foundation

@objc(FlcUtilPlugin) class FlcUtilPlugin : CDVPlugin {
  
  @objc(acquireWakeLock:) func acquireWakeLock(command: CDVInvokedUrlCommand) {
    self.commandDelegate!.run(inBackground: {
      let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    })
  }
  
  @objc(releaseWakeLock:) func releaseWakeLock(command: CDVInvokedUrlCommand) {
    self.commandDelegate!.run(inBackground: {
      let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    })
  }
  
  @objc(setKeepScreenOn:) func setKeepScreenOn(command: CDVInvokedUrlCommand) {
    let keepScreenOn: Bool = command.argument(at: 0) as? Bool ?? false
    FlcUtil.setKeepScreenOn(keepScreenOn)
    self.commandDelegate!.run(inBackground: {
      let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    })
  }
  
  @objc(decodeImage:) func decodeImage(command: CDVInvokedUrlCommand) {
    self.commandDelegate!.run(inBackground: {
      let input = command.argument(at: 0);
      var pluginResult: CDVPluginResult
      if input != nil {
        let output: Data? = FlcUtil.decodeImage(input as! Data)
        if output != nil {
          pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAsArrayBuffer: output!)
        } else {
          pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Error decoding image")
        }
      } else {
        pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR, messageAs: "Error: invalid argument passed")
      }
      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    })
  }
  
  @objc(getIp:) func getIp(command: CDVInvokedUrlCommand) {
    self.commandDelegate!.run(inBackground: {
      let ip: String = FlcUtil.getIp();
      let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: ip)
      self.commandDelegate.send(pluginResult, callbackId: command.callbackId)
    })
  }
  
}
