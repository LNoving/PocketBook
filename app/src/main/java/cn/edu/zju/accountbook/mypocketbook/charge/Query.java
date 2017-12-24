package cn.edu.zju.accountbook.mypocketbook.charge;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;


/**
 * Created by 张昊 on 2017/11/14.
 */

public class Query implements Callable<String> {
    private static String code;
    public Query(String code){
        Query.code = code;
    }

    private String query() {
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;

        String result = null;
        String host = "https://ali-barcode.showapi.com";

        String path = "/barcode";
        String method = "GET";
        String appcode = "6df12c0a624348d7bb6a5e6131ee37b6";
        String paramter="code=";

        try{
            url = new URL(host+path+"?"+paramter+code);

            Log.v("URL啦啦啦", url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization","APPCODE "+appcode);

            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public String call() {
        return query();
    }


}
