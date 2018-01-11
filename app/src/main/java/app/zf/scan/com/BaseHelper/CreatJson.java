package app.zf.scan.com.BaseHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ( )Created by ${Ethan_Zeng} on 2017/12/18.
 */

public class CreatJson {
   public static String mapCreateJsonGai(Map<String,Object> map ,List<Map<String,Object>> listData ){
       List<Map> jsonObjects = new ArrayList<Map>();
       for (int i = 0; i < listData.size(); i++) {
           Map<String,Object> map1 = new HashMap<String,Object>();
           String id=listData.get(i).get("codeGenerateRecordId").toString();
           //String st=listData.get(i).get("startCode").toString();
           map1.put("codeGenerateRecordId",listData.get(i).get("codeGenerateRecordId").toString());
           map1.put("startNum",listData.get(i).get("startCode").toString());
           map1.put("endNum",listData.get(i).get("endCode").toString());
           jsonObjects.add(map1);
       }


        map.put("changeCodeApplyNums",jsonObjects);
        System.out.println("集合中Map创建json对象:" + new JSONObject(map));

       return  (new JSONObject(map)).toString();
    }
    public static String mapCreateJsonFei(Map<String,Object> map ,List<Map<String,Object>> listData ){
        List<Map> jsonObjects = new ArrayList<Map>();
        for (int i = 0; i < listData.size(); i++) {
            Map<String,Object> map1 = new HashMap<String,Object>();
            map1.put("codeGenerateRecordId",listData.get(i).get("codeGenerateRecordId"));
            map1.put("startNum",listData.get(i).get("startCode"));
            map1.put("endNum",listData.get(i).get("endCode"));
            jsonObjects.add(map1);
        }


        map.put("destoryCodeApplyNums",jsonObjects);
        System.out.println("集合中Map创建json对象:" + new JSONObject(map));

        return  (new JSONObject(map)).toString();
    }
}
