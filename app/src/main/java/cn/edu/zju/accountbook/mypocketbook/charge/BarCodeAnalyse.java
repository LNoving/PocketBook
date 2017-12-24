package cn.edu.zju.accountbook.mypocketbook.charge;

/**
 * Created by 张昊 on 2017/11/19.
 */

import android.util.Log;

public class BarCodeAnalyse {
    private String info;
    private String price;
    private String name;
    private String img;

    public BarCodeAnalyse(String info){
        this.info = info;
        Log.v("创建",info);
        analyze();
    }

    public String getPrice() {

        return price;
    }

    public String getName() {
        return name;

    }

    public String getImg() {
        return img;
    }

    void analyze(){
        int beginIndex;
        int endIndex;

        try{
            beginIndex = info.indexOf("\"price\":\"")+9;
            endIndex = info.lastIndexOf("\",\"flag\"");
            price = info.substring(beginIndex,endIndex);Log.v("创",info);
            Log.v("分析结果：",price);
        }catch (Exception e){
            price = "获取价格时出错";
            Log.v("获取价格时出错：",info);
        }

        try {
            beginIndex = info.indexOf("goodsName\":\"")+12;
            endIndex = info.lastIndexOf("\",\"zzjb");
            name = info.substring(beginIndex,endIndex);
            Log.v("分析结果：",name);
        }catch (Exception e){
            name = "获取信息出错";
            Log.v("获取信息时出错：",info);
        }


        try {
            beginIndex = info.indexOf("\"img\":\"")+7;
            endIndex = info.lastIndexOf("\",\"code");
            img = info.substring(beginIndex,endIndex);
            Log.v("分析结果：",img);
        }catch (Exception e){
            img = "";
            Log.v("获取照片时出错：",info);
        }

    }

}