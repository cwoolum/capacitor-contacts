
  Pod::Spec.new do |s|
    s.name = 'CapacitorContactManager'
    s.version = '0.0.1'
    s.summary = 'A plugin for managing native phone contacts'
    s.license = 'MIT'
    s.homepage = 'https://github.com/cwoolum/capacitor-contacts'
    s.author = 'Chris Woolum'
    s.source = { :git => 'https://github.com/cwoolum/capacitor-contacts', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end