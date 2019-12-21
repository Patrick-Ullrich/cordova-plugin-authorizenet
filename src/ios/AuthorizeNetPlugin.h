#import <Cordova/CDV.h>
@import AcceptSDK;

@interface CDVAuthorizeNetPlugin : CDVPlugin 

- (void)setEnvironment:(CDVInvokedUrlCommand*)command;
- (void)createToken:(CDVInvokedUrlCommand*)command;

@end
