package app.zf.scan.com.BaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/11/7.
 */

public class JsonHelper {

    public JsonHelper() {

    }

    /**
     * 此方法用于json字符串对象转换成map数据 使用 parseJsonObject 有唯一要求：数组必须要属于一个键的值
     * <p>
     * 详情见类说明
     *
     * @param jo
     * @param map
     * @return
     */
    public static Map<String, Object> jsonObjectToMap(JSONObject jo,
                                                      Map<String, Object> map) {

        JSONArray ja = jo.names();
        for (int i = 0; i < ja.length(); i++) {
            String strKey = ja.optString(i);
            String strRawJson = jo.opt(strKey).toString();
            char cTestChar = ' ';
            if (strRawJson.length() > 0) {
                cTestChar = (strRawJson.charAt(0));
            }
            switch (cTestChar) {
                case '{':
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    if (jo.optJSONObject(strKey) != null) {
                        map2 = jsonObjectToMap(jo.optJSONObject(strKey), map2);
                        map.put(strKey, map2);
                    }
                    break;
                case '[':
                    List<Object> array = new ArrayList<Object>();
                    if (jo.optJSONArray(strKey) != null) {
                        array = parseJsonArray(jo.optJSONArray(strKey), array,
                                strKey);
                        map.put(strKey, array);
                    }
                    break;
                default:
                    map.put(strKey, strRawJson);
                    break;
            }
        }
        return map;
    }

    /**
     * 解析json字符串数组,服务于parseJsonObject
     *
     * @param ja
     * @param array
     * @param key
     * @return
     */
    private static List<Object> parseJsonArray(JSONArray ja,
                                               List<Object> array, String key) {

        for (int i = 0; i < ja.length(); i++) {
            String strRawJson = ja.opt(i).toString();
            char cTestChar = ' ';
            if (strRawJson.length() > 0) {
                cTestChar = (strRawJson.charAt(0));
            }
            switch (cTestChar) {
                case '{':
                    Map<String, Object> map = new HashMap<String, Object>();
                    JSONObject jo = ja.optJSONObject(i);
                    if (jo != null) {
                        map = jsonObjectToMap(jo, map);
                        array.add(map);
                    }

                    break;
                case '[':
                    List<Object> newArray = new ArrayList<Object>();
                    JSONArray jo1 = ja.optJSONArray(i);
                    if (jo1 != null) {
                        newArray = parseJsonArray(jo1, newArray, key);
                    }
                    break;
                default:
                    array.add(strRawJson);
            }
        }
        return array;
    }

}

