package com.voicebot.services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.voicebot.process.json.JsonRequestObject;
import com.voicebot.utils.VoiceBotConst;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.voicebot.services.VoiceBotService.JsonRequestSender.gson;

/**
 * Created by koushik on 6/5/2017.
 */

public class VoiceBotService {

    private static ArrayList<JsonObject> mResultJsonArrayObjects = new ArrayList<JsonObject>();
    private static Gson gson = new Gson();


    public static class JsonRequestSender {

        private static HashMap<String, String> mHeaderData;
        private static Gson gson = new Gson();

        private static final String TAG = JsonRequestSender.class.getSimpleName();

        public JsonRequestSender() {
            if (mHeaderData == null) {
                mHeaderData = new HashMap();
            }
        }
        public static HttpClient getNewHttpClient(HttpParams params) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

               // SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
               // sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

//            HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
              //  registry.register(new Scheme("https", sf, 8443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                return new DefaultHttpClient();
            }
        }

        private String sendRequest(String endpoint, JsonRequestObject request) {
            String responseString = null;
            try {
                String jsonStr = gson.toJson(request);
                Log.i("json request", jsonStr);
//				HttpUtil.sendHttpRequest(endpoint, jsonStr);
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 50000);
                HttpConnectionParams.setSoTimeout(httpParameters, 50000);
                HttpClient httpclient = getNewHttpClient(httpParameters);

                HttpPost post = new HttpPost("" + endpoint);

                post.addHeader("Content-Type", "application/json;charset=utf-8");
                post.addHeader("Accept", "application/json");
                StringEntity entity = new StringEntity(jsonStr, "UTF-8");
                post.setEntity(entity);
                post.setParams(httpParameters);
                HttpResponse resp = httpclient.execute(post);
                int statusCode = resp.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    Log.e(TAG, "HTTP Error Code:" + statusCode);
                }
                InputStream i = resp.getEntity().getContent();
                InputStreamReader reader = new InputStreamReader(i);
                StringBuilder sb = new StringBuilder();
                int c;
                while ((c = reader.read()) != -1)
                {
                    sb.append((char)c);
                }
                reader.close();
                responseString = sb.toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

    }
        public static class JsonResponseReader {
            private static final String TAG = JsonResponseReader.class.getSimpleName();
            private String mResponseCode;
            private String mErrorMessage;
            private HashMap<String, String> mData;
            public JsonResponseReader(String result) {
                if (result == null) {

                }
                try {
                    JsonParser parser = new JsonParser();
                    JsonElement e = parser.parse(result);
                    JsonObject responseHeader = e.getAsJsonObject().getAsJsonObject(VoiceBotConst.JSON_HEADER);
                    if (parseHeader(responseHeader)) {
                        JsonObject responseData = e.getAsJsonObject().getAsJsonObject(VoiceBotConst.JSON_DATA);
                        if (responseData != null) {
                            mData = new HashMap();
                            parseData(responseData);
                        }else{
                            mErrorMessage = "Unexpected Json Response";
                        }
                    }
                    Log.i(TAG, "responseJson=" + result);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            private boolean parseHeader(JsonObject responseHeader) {
                if (responseHeader != null && responseHeader.has(VoiceBotConst.RESPONSE_CODE)) {
                    JsonElement responseCode = responseHeader.get(VoiceBotConst.RESPONSE_CODE);
                    mResponseCode = responseCode.getAsString();
                    if ("0000".equals(mResponseCode)) {
                        return true;
                    }else if (responseHeader.has(VoiceBotConst.ERROR_MESSAGE)) {
                        JsonElement errorMessage = responseHeader.get(VoiceBotConst.ERROR_MESSAGE);
                        mErrorMessage = errorMessage.getAsString();
                    }else{
                        mErrorMessage = "Unexpected Json Response";
                    }
                }else{
                    mErrorMessage = "Unexpected Json Response";
                }
                return false;
            }

            private void parseData(JsonObject responseData) {
                Iterator<Map.Entry<String, JsonElement>> iterator = responseData.entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String, JsonElement> pair = iterator.next();
                    String key = pair.getKey();
                    JsonElement value = pair.getValue();
                    if (value.isJsonObject()) {
                        String jsonString = gson.toJson(value);
                        Log.i(TAG, "json=" + jsonString);
                        mData.put(key, jsonString);
                    }
                    else if (key != null && value != null && !value.isJsonNull()&&!value.isJsonArray()){
                        Log.i(key, value.getAsString());
                        mData.put(key, value.getAsString());
                    }
                /*
                        If the Value is Json Array ,the following block gets executed
                        Added for Collection of lists in the response
                 */
                    else if(value.isJsonArray()){
                        JsonArray jsonArray = value.getAsJsonArray();
                        ArrayList<JsonObject> mJsonArrayObjects = new ArrayList<JsonObject>();

                        for(int i=0;i<jsonArray.size();i++){
                            JsonObject childJsonObject = (JsonObject)jsonArray.get(i);
                            mJsonArrayObjects.add(childJsonObject);

                        }
                        mResultJsonArrayObjects = mJsonArrayObjects;

                    }
                }
            }

            public String getResponseCode() {
                return mResponseCode;
            }

            public String getErrorMessage() {
                return mErrorMessage;
            }

            public HashMap<String, String> getData() {
                return mData;
            }
            /*
                Reyurns list of JSON Array Objects
             */
            public ArrayList<JsonObject> getJsonArrayObjects(){
                return mResultJsonArrayObjects;
            }
        }
}

