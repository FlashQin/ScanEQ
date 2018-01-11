package app.zf.scan.com.HttpUtils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import app.zf.scan.com.BaseHelper.GetNetResult;

/**
 * ( NetWorkRequst)Created by ${Ethan_Zeng} on 2017/11/7.
 */

public class HttpUtils {

    public HttpUtils() {
    }


    /***
     *

     * @param okttp
     * @param url
     * @param handler
     * @param HTTPREQUST
     */
    public void DoPostJson( MyOkHttp okttp, String url, final Handler handler, final int HTTPREQUST,String json,String token) {

        //RequestBody requestBody=  FormBody.create(MediaType.parse("application/json;charset=UTF-8"), rul);


        okttp.post().url(url).addHeader("utoken",token).jsonParams(json).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Map<String,Object> mapdata=new HashMap<String, Object>();

                mapdata=new GetNetResult().GetJosn(response.toString());
                Message message = Message.obtain();
                message.what = HTTPREQUST;
                message.obj = response;
                synchronized (handler) {
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {

                Message message = new Message();
                message.what = HTTPREQUST;
                message.obj = error_msg;
                handler.sendMessage(message);

            }
        });


    }

    /***
     *
     * @param okttp
     * @param url
     * @param handler
     * @param HTTPREQUST
     */
    public void DoGet(MyOkHttp okttp, String url, final Handler handler, final int HTTPREQUST) {


        okttp.get().url(url).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                Message message = Message.obtain();
                message.what = HTTPREQUST;
                message.obj = response;
                synchronized (handler) {
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Message message = new Message();
                message.what = HTTPREQUST;
                message.obj = error_msg;
                handler.sendMessage(message);
            }
        });

    }

    /***
     *
     * @param params
     * @param okttp
     * @param url
     * @param handler
     * @param HTTPREQUST
     * @param file
     */
    public void DoUpLoadFiles(Map<String, String> params, MyOkHttp okttp, String url, final Handler handler, final int HTTPREQUST, File file) {
        okttp.upload().url(url).params(params).addFile("file", file).enqueue(new JsonResponseHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                Message message = Message.obtain();
                message.what = HTTPREQUST;
                message.obj = response;
                synchronized (handler) {
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                super.onProgress(currentBytes, totalBytes);
                Message message = Message.obtain();
                message.what = HTTPREQUST;
                message.obj = totalBytes;
                synchronized (handler) {
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(int statusCode, String error_msg) {
                Message message = new Message();
                message.what = HTTPREQUST;
                message.obj = error_msg;
                handler.sendMessage(message);
            }
        });
    }

    /***
     * downloadFiles
     * @param okttp
     * @param url
     * @param handler
     * @param HTTPREQUST
     * @param Filepath
     * @param context
     */
    public void DoDownLoadFiles(MyOkHttp okttp, String url, final Handler handler, final int HTTPREQUST, String Filepath, Context context) {
        okttp.download().url(url).filePath(Filepath).tag(context).enqueue(new DownloadResponseHandler() {
            @Override
            public void onStart(long totalBytes) {
                super.onStart(totalBytes);
            }

            @Override
            public void onFinish(File downloadFile) {
                Message message = Message.obtain();
                message.what = HTTPREQUST;
                message.obj = "ok";
                synchronized (handler) {
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                Message message = Message.obtain();
                message.what = HTTPREQUST;
                message.obj = totalBytes;
                synchronized (handler) {
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(String error_msg) {

            }
        });
    }


}
