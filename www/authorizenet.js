function AuthorizeNetPlugin() {}

AuthorizeNetPlugin.prototype.createToken = function(
  cardNumber,
  expirationMonth,
  expirationYear,
  cardCvv,
  loginId,
  clientKey,
  successCallback,
  errorCallback
) {
  var options = {
    cardNumber,
    expirationMonth,
    expirationYear,
    cardCvv,
    loginId,
    clientKey
  };
  cordova.exec(
    successCallback,
    errorCallback,
    "AuthorizeNetPlugin",
    "createToken",
    [options]
  );
};

AuthorizeNetPlugin.prototype.setEnvironment = function(
  environment,
  successCallback,
  errorCallback
) {
  cordova.exec(
    successCallback,
    errorCallback,
    "AuthorizeNetPlugin",
    "setEnvironment",
    [environment]
  );
};

// Installation constructor that binds AuthorizeNetPlugin to window
AuthorizeNetPlugin.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.authorizeNetPlugin = new AuthorizeNetPlugin();
  return window.plugins.authorizeNetPlugin;
};
cordova.addConstructor(AuthorizeNetPlugin.install);
