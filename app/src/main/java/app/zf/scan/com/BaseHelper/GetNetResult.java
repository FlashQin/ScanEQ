package app.zf.scan.com.BaseHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * ( JosnResult)Created by ${Ethan_Zeng} on 2017/11/7.
 */

public class GetNetResult {
    public GetNetResult() {


    }

    /***
     *
     * @param response
     * @return
     */
    public static Map<String, Object> GetJosn(String response) {
        String jString = new String(response);
        Map<String, Object> map = new HashMap<String, Object>();
        try {

            JSONObject jsonObject = new JSONObject(jString);

            map = JsonHelper.jsonObjectToMap(jsonObject, map);
            String string = (String) map.get("result_code");
            String msgstr = (String) map.get("message");

        } catch (Exception e) {

        }

        return map;
    }

}
