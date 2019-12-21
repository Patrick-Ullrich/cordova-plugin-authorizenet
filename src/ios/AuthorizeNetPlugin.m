#import "AuthorizeNetPlugin.h"
@import AuthorizeNetAccept;

@interface AuthorizeNetPlugin : CDVPlugin
@property (strong, atomic) AcceptSDKHandler* handler;
@end
@implementation AuthorizeNetPlugin : CDVPlugin

- (void)setEnvironment:(CDVInvokedUrlCommand*)command
{
    NSString* environment = [[command arguments] objectAtIndex:0];
    
    self.handler = [[AcceptSDKHandler alloc] initWithEnvironment:AcceptSDKEnvironmentENV_TEST];

        CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus: CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)createToken:(CDVInvokedUrlCommand*)command
{
    NSDictionary* arguments = [[command arguments] objectAtIndex:0];
    NSString* cardNumber = arguments[@"cardNumber"];
    NSString* expirationMonth = arguments[@"expirationMonth"];
    NSString* expirationYear = arguments[@"expirationYear"];
    NSString* cardCode = arguments[@"cardCvv"];
    NSString* loginId = arguments[@"loginId"];
    NSString* clientKey = arguments[@"clientKey"];

    AcceptSDKRequest *request = [[AcceptSDKRequest alloc] init];
    request.merchantAuthentication.name = loginId;
    request.merchantAuthentication.clientKey = clientKey;

    request.securePaymentContainerRequest.webCheckOutDataType.token.cardNumber = cardNumber;
    request.securePaymentContainerRequest.webCheckOutDataType.token.expirationMonth = expirationMonth;
    request.securePaymentContainerRequest.webCheckOutDataType.token.expirationYear = expirationYear;
    request.securePaymentContainerRequest.webCheckOutDataType.token.cardCode = cardCode;

    [self.handler getTokenWithRequest:request successHandler:^(AcceptSDKTokenResponse * _Nonnull token) {
        NSString *successMessage = [NSString stringWithFormat:@"%@ : %@", token.getOpaqueData.getDataDescriptor, token.getOpaqueData.getDataValue];
               CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:successMessage];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } failureHandler:^(AcceptSDKErrorResponse * _Nonnull error) {
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:error.getMessages.getMessages.firstObject.getText ];

        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

@end
