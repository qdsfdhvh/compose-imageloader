import SwiftUI
import combine

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        let window = UIWindow(frame: UIScreen.main.bounds)
        let mainViewController = MainKt.MainViewController(window: window)
        window.rootViewController = mainViewController
        window.makeKeyAndVisible()
        return true
    }
}
