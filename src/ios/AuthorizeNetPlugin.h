#import <Cordova/CDV.h>
@import AuthorizeNetAccept;

@interface CDVAuthorizeNetPlugin : CDVPlugin 

- (void)setEnvironment:(CDVInvokedUrlCommand*)command;
- (void)createToken:(CDVInvokedUrlCommand*)command;

@end
