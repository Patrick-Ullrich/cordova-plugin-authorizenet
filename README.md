# cordova-plugin-authorizenet

Not official plugin. Not actively maintained. Only implements accept API. PR's welcome. Android in better state than iOS.

usage:
```
    window.plugins.authorizeNetPlugin.setEnvironment(
        'SANDBOX',
        function() {
          console.log('environment is set!');
        },
        function(err) {
          console.log('environment is not set!' + err);
        },
      );

      window.plugins.authorizeNetPlugin.createToken(
        cardNumber,
        month,
        year,
        cardCode,
        apiLoginID,
        clientKey,
        function(result) {
          let [dataDescriptor, dataValue] = result.split(':');
          callback(dataValue.trim());
        },
        function(err) {
          let serverError = err.messages.message.reduce(
            (acc: string, err: any) => `${acc}${err.text}<br />`,
            '',
          );
        },
      );
```