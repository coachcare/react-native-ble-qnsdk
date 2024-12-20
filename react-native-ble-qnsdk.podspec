require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))
folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'

Pod::Spec.new do |s|
  s.name = "react-native-ble-qnsdk"
  s.version = package["version"]
  s.summary = package["description"]
  s.description = <<-DESC
    YolandaSync is a powerful Bluetooth communication library designed to simplify the process of synchronizing data with Yolanda scales. With a seamless integration into your app, YolandaSync allows you to effortlessly connect to Yolanda scales over Bluetooth and retrieve measurement data with just a few lines of code.

    Key Features:
    - Streamlined Bluetooth pairing and communication with Yolanda scales.
    - Retrieve precise weight and measurement data with ease.
    - Seamlessly integrate weight measurements into your app's workflow.

    GitHub: https://github.com/coachcare/react-native-ble-qnsdk
  DESC
  s.homepage = "https://github.com/coachcare/react-native-ble-qnsdk"
  s.license = "MIT"
  s.authors = { "Jeff Drakos" => "jeffdrakos@gmail.com" }
  s.platforms = { :ios => "11.0" }
  s.source = { :git => "https://github.com/coachcare/react-native-ble-qnsdk.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  # s.dependency "React"
  s.dependency "QNSDK", "2.9.0"

  # s.pod_target_xcconfig = {
  #   "CLANG_CXX_LANGUAGE_STANDARD" => "c++17",
  #   'DEFINES_MODULE' => 'YES',
  #   'SWIFT_COMPILATION_MODE' => 'wholemodule'
  # }

  s.compiler_flags = folly_compiler_flags + " -DRCT_NEW_ARCH_ENABLED=1"
  s.pod_target_xcconfig = {
    "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost\" \"$(PODS_ROOT)/boost-for-react-native\" \"$(PODS_ROOT)/glog\"",
    "OTHER_CPLUSPLUSFLAGS" => "-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1",
    "CLANG_CXX_LANGUAGE_STANDARD" => "c++17",
    "CLANG_CXX_LIBRARY" => "libc++",
    'DEFINES_MODULE' => 'YES'
  }
  s.dependency "React-Codegen"
  s.dependency "RCT-Folly"
  s.dependency "RCTRequired"
  s.dependency "RCTTypeSafety"
  s.dependency "ReactCommon/turbomodule/core"

  install_modules_dependencies(s)
end
