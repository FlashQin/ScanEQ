package app.zf.scan.com.BaseHelper;

import com.google.gson.Gson;

import java.util.Map;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/11/22.
 */

public class ToJson {
    public ToJson() {

    }

    /***
     * map To Json
     * @param map
     * @param <T>
     * @return
     */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }
}
