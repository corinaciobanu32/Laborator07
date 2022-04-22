package ro.pub.cs.systems.eim.lab07.calculatorwebservice.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private final TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];
        int method = Integer.parseInt(params[3]);

        // TODO exercise 4
        // signal missing values through error messages
        if (operator1 == null || operator2 == null || operator1.isEmpty() || operator2.isEmpty()) {
            return Constants.ERROR_MESSAGE_EMPTY;
        }

        // create an instance of a HttpClient object
        HttpClient httpClient = new DefaultHttpClient();

        // get method used for sending request from methodsSpinner

        // 1. GET
        if (method == Constants.GET_OPERATION) {
            // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)


            HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                    + "?" + Constants.OPERATION_ATTRIBUTE + "=" + operation
                    + "&" + Constants.OPERATOR1_ATTRIBUTE + "=" + operator1
                    + "&" + Constants.OPERATOR2_ATTRIBUTE + "=" + operator2);
            // b) create an instance of a ResultHandler object
            ResponseHandler<String> responseHandlerGet = new BasicResponseHandler();
            try {
                // c) execute the request, thus generating the result
                return httpClient.execute(httpGet, responseHandlerGet);

            } catch (ClientProtocolException clientProtocolException) {
                Log.e(Constants.TAG, clientProtocolException.getMessage());
                if (Constants.DEBUG) {
                    clientProtocolException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        } else if (method == Constants.POST_OPERATION) {
            // 2. POST

            // a) build the URL into a HttpPost object
            HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
            // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            postParams.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
            postParams.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1));
            postParams.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2));

            try {
                // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);                httpPost.setEntity(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException unsupportedEncodingException) {
                Log.e(Constants.TAG, unsupportedEncodingException.getMessage());
                if (Constants.DEBUG) {
                    unsupportedEncodingException.printStackTrace();
                }
            }
            // d) create an instance of a ResultHandler object
            ResponseHandler<String> responseHandlerPost = new BasicResponseHandler();
            try {
                // e) execute the request, thus generating the result
                return httpClient.execute(httpPost, responseHandlerPost);
            } catch (ClientProtocolException clientProtocolException) {
                Log.e(Constants.TAG, clientProtocolException.getMessage());
                if (Constants.DEBUG) {
                    clientProtocolException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // display the result in resultTextView
        resultTextView.setText(result);
    }

}
