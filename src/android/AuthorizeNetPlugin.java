package com.patrickullrich.cordova.plugin;

// The native Toast API
import android.widget.Toast;
// Cordova-required packages
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.authorize.acceptsdk.AcceptSDKApiClient;
import net.authorize.acceptsdk.datamodel.merchant.ClientKeyBasedMerchantAuthentication;
import net.authorize.acceptsdk.datamodel.common.Message;
import net.authorize.acceptsdk.datamodel.transaction.CardData;
import net.authorize.acceptsdk.datamodel.transaction.EncryptTransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionType;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;

public class AuthorizeNetPlugin extends CordovaPlugin implements EncryptTransactionCallback {
    private static final String DURATION_LONG = "long";
    private AcceptSDKApiClient apiClient;
    private CallbackContext callbackContext = null;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {

        try {
            if (action.equals("createToken")) {
                createToken(args.getJSONObject(0), callbackContext);
            } else if (action.equals("setEnvironment")) {
                setEnvironment(args.getString(0), callbackContext);
            } else {
                callbackContext.error("\"" + action + "\" is not a recognized action.");
                return false;
            }
        } catch (JSONException e) {
            return false;
        }

        this.callbackContext = callbackContext;
        PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        this.callbackContext.sendPluginResult(pluginResult);
        return true;
    }

    private void createToken(final JSONObject options, final CallbackContext callbackContext) {
        try {
            String cardNumber = options.getString("cardNumber");
            String expirationMonth = options.getString("expirationMonth");
            String expirationYear = options.getString("expirationYear");
            String cardCvv = options.getString("cardCvv");
            String loginId = options.getString("loginId");
            String clientKey = options.getString("clientKey");

            ClientKeyBasedMerchantAuthentication merchantAuthentication = ClientKeyBasedMerchantAuthentication
                    .createMerchantAuthentication(loginId, clientKey);

            CardData cardData = new CardData.Builder(cardNumber, expirationMonth, expirationYear).cvvCode(cardCvv)
                    .build();

            EncryptTransactionObject transactionObject = TransactionObject
                    .createTransactionObject(TransactionType.SDK_TRANSACTION_ENCRYPTION).cardData(cardData)
                    .merchantAuthentication(merchantAuthentication).build();

            apiClient.getTokenWithRequest(transactionObject, this);
        } catch (JSONException e) {
            callbackContext.error("Error encountered: " + e.getMessage());
        }
    }

    private void setEnvironment(final String environment, final CallbackContext callbackContext) {
        AcceptSDKApiClient.Environment env = AcceptSDKApiClient.Environment.SANDBOX;
        if (environment == "PRODUCTION") {
            env = AcceptSDKApiClient.Environment.PRODUCTION;
        }

        apiClient = new AcceptSDKApiClient.Builder(cordova.getActivity(), env).connectionTimeout(5000).build();
        callbackContext.success();
    }

    @Override
    public void onEncryptionFinished(EncryptTransactionResponse response) {
        PluginResult result = new PluginResult(PluginResult.Status.OK,
                response.getDataDescriptor() + " : " + response.getDataValue());
        result.setKeepCallback(false);
        if (callbackContext != null) {
            callbackContext.sendPluginResult(result);
            callbackContext = null;
        }
    }

    @Override
    public void onErrorReceived(ErrorTransactionResponse errorResponse) {
        Message error = errorResponse.getFirstErrorMessage();
        PluginResult result = new PluginResult(PluginResult.Status.ERROR, error.toString());
        result.setKeepCallback(false);
        if (callbackContext != null) {
            callbackContext.sendPluginResult(result);
            callbackContext = null;
        }
    }
}