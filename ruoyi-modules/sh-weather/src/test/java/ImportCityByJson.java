import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.*;

/**
 * @author ：wt
 * @date ：Created in 2023-04-11 8:38
 * @description：
 * @modified By：wt
 */
public class ImportCityByJson {
    public static void main(String[] args) {
        String jsonStr = getStr(new File("C:\\Users\\13586\\Desktop\\RuoYi-Cloud-Plus\\ruoyi-modules\\sh-weather\\src\\main\\resources\\city.json"));
        JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
        for (Object o : jsonArray) {
            JSONObject cityObj = JSONUtil.parseObj(o);
            String cityName = cityObj.get("hk").toString();
            JSONArray children = cityObj.getJSONArray("children");
            System.out.println(o);
        }
    }


    //把一个文件中的内容读取成一个String字符串
    public static String getStr(File jsonFile){
        String jsonStr = "";
        try {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
