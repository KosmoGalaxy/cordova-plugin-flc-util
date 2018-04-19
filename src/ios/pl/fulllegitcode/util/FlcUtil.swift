import Foundation

class FlcUtil {
  
  static func setKeepScreenOn(_ value: Bool) {
    UIApplication.shared.isIdleTimerDisabled = value
  }
  
  static func getIp() -> String {
    let ips: [AnyHashable: String] = FlcUtilObjectiveC.getIPAddresses()
    var myIp: String
    if String(ips["hotspot"]!) != "" {
      myIp = String(ips["hotspot"]!)
    } else {
      myIp = String(ips["wireless"]!)
    }
    return myIp
  }
  
  static func decodeImage(_ input: Data) -> Data? {
    let image: UIImage? = UIImage(data: input)
    if image == nil{
      return nil
    }
    let size = image!.size
    let dataSize = size.width * size.height * 4
    var pixelData = [UInt8](repeating: 0, count: Int(dataSize))
    let colorSpace = CGColorSpaceCreateDeviceRGB()
    let context = CGContext(
      data: &pixelData,
      width: Int(size.width),
      height: Int(size.height),
      bitsPerComponent: 8,
      bytesPerRow: 4 * Int(size.width),
      space: colorSpace,
      bitmapInfo: CGImageAlphaInfo.noneSkipLast.rawValue
    )
    guard let cgImage = image!.cgImage else { return nil }
    context?.draw(cgImage, in: CGRect(x: 0, y: 0, width: size.width, height: size.height))
    return Data(bytes: pixelData)
  }
  
}
