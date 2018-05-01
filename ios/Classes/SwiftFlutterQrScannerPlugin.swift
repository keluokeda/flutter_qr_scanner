import Flutter
import UIKit
    
public class SwiftFlutterQrScannerPlugin: NSObject, FlutterPlugin,ScanResultDelegate {
    let rootViewController:UIViewController
    
    init(rootViewController:UIViewController){
        self.rootViewController = rootViewController
    }
    
    
    var result:  FlutterResult?
    
    public func result(scanResult: String) {
        if (result != nil) {
            result!(scanResult)
            result = nil
        }
        
    }
    
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "github.com/keluokeda/scanner", binaryMessenger: registrar.messenger())
    let rootViewController = UIApplication.shared.delegate?.window??.rootViewController;
    
    
    let instance = SwiftFlutterQrScannerPlugin(rootViewController: rootViewController!)
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
//    result("iOS " + UIDevice.current.systemVersion)
    if call.method == "scan" {
        scan(call: call, r: result)
    }else{
        result(FlutterMethodNotImplemented)
    }
  }
    
    
    func scan(call:FlutterMethodCall,r:@escaping FlutterResult)  {
        self.result = r
        
        let vc = QQScanViewController()
        vc.scanDelegate = self
        
        var style = LBXScanViewStyle()
        style.animationImage = UIImage(named: "CodeScan.bundle/qrcode_scan_light_green")
        vc.scanStyle = style
        
        let navigationController = UINavigationController(rootViewController: vc)
        
        self.rootViewController.present(navigationController, animated: false, completion: nil)

    }
    
    
}
