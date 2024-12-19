# require "json"
# package = JSON.parse(File.read(File.join(__dir__, "package.json")))
# folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'

# Pod::Spec.new do |s|
#   s.name = "react-native-ble-qnsdk"
#   s.version      = package["version"]
#   s.summary      = package["description"]
#   s.homepage     = package["homepage"]
#   s.license      = package["license"]
#   s.authors      = package["author"]
#   s.platforms    = { :ios => min_ios_version_supported }
#   s.source       = { :git => ".git", :tag => "#{s.version}" }
#   s.source_files = "ios/**/*.{h,m,mm,swift}"
  
#   s.dependency "React-Core"
#   s.dependency "QNSDK", "2.9.0"


  # if respond_to?(:install_modules_dependencies, true)
  #   install_modules_dependencies(s)
  # else
  #   if ENV['RCT_NEW_ARCH_ENABLED'] == '1' then
  #     s.compiler_flags = folly_compiler_flags + " -DRCT_NEW_ARCH_ENABLED=1"
  #     s.pod_target_xcconfig    = {
  #         "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost\"",
  #         "OTHER_CPLUSPLUSFLAGS" => "-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1",
  #         "CLANG_CXX_LANGUAGE_STANDARD" => "c++17"
  #     }
  #     s.dependency "React-Codegen"
  #     s.dependency "RCT-Folly"
  #     s.dependency "RCTRequired"
  #     s.dependency "RCTTypeSafety"
  #     s.dependency "ReactCommon/turbomodule/core"
  #   end
  # end
# end


require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))
# folly_compiler_flags = '-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1 -Wno-comma -Wno-shorten-64-to-32'


Pod::Spec.new do |s|
  s.name            = "react-native-ble-qnsdk"
  s.version         = package["version"]
  s.summary         = package["description"]
  s.description     = package["description"]
  s.homepage        = "https://github.com/coachcare/react-native-ble-qnsdk"
  s.license         = package["license"]
  s.platforms       = { :ios => "11.0" }
  s.author          = package["author"]
  s.source          = { :git => package["repository"], :tag => "#{s.version}" }

  s.source_files    = "ios/**/*.{h,m,mm,swift}"
  s.dependency "QNSDK", "2.9.0"
  s.requires_arc = true

  # if respond_to?(:install_modules_dependencies, true)
  #   install_modules_dependencies(s)
  # else
  #   if ENV['RCT_NEW_ARCH_ENABLED'] == '1' then
      # s.compiler_flags = folly_compiler_flags + " -DRCT_NEW_ARCH_ENABLED=1"
      # s.pod_target_xcconfig    = {
      #     "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost\"",
      #     "OTHER_CPLUSPLUSFLAGS" => "-DFOLLY_NO_CONFIG -DFOLLY_MOBILE=1 -DFOLLY_USE_LIBCPP=1",
      #     "CLANG_CXX_LANGUAGE_STANDARD" => "c++17"
      # }
  #     s.dependency "React-Codegen"
  #     s.dependency "RCT-Folly"
  #     s.dependency "RCTRequired"
  #     s.dependency "RCTTypeSafety"
  #     s.dependency "ReactCommon/turbomodule/core"
  #   end
  # end

  # s.static_framework = true
  # s.compiler_flags = folly_compiler_flags + " -DRCT_NEW_ARCH_ENABLED=1"
  s.pod_target_xcconfig = {
    'DEFINES_MODULE' => 'YES',
    # "HEADER_SEARCH_PATHS" => "\"$(PODS_ROOT)/boost\"",
    "OTHER_CPLUSPLUSFLAGS" => "-DRCT_NEW_ARCH_ENABLED=1",
    # "CLANG_CXX_LANGUAGE_STANDARD" => "c++17"
  }
  # s.pod_target_xcconfig    = {
  #   +    "DEFINES_MODULE" => "YES",
  #   +    "OTHER_CPLUSPLUSFLAGS" => "-DRCT_NEW_ARCH_ENABLED=1"
  #   +  }
    
  #   +  install_modules_dependencies(s)
      
  install_modules_dependencies(s)
end