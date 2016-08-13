package com.appleframework.dubbo.cache.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionUtil {

    private static final String symbol = "-";

    public static String convertExp(String content){

        List<Integer> indexs = new ArrayList<Integer>();
        findExpIndexs(content, indexs);

        Map<String,String> variables = new HashMap<String, String>();
        for(int i=0; i<indexs.size()/2; i++){
            int j = i*2;
            String o = content.substring(indexs.get(j), indexs.get(j+1));
            variables.put(o+symbol, o.replace(symbol, "${") + "}");
        }

        for(Map.Entry<String, String> entry : variables.entrySet()){
            if(content.indexOf(entry.getKey()) != -1){
                content = content.replace(entry.getKey(), entry.getValue());
            }
        }

        return content;
    }

    public static void findExpIndexs(String content, List<Integer> indexs){

        int i = content.indexOf(symbol);
        if(i!=-1){

            if(indexs.size()==0){
                indexs.add(i);
            }else{
                indexs.add(indexs.get(indexs.size()-1) + i + 1);
            }

            findExpIndexs(content.substring(i+1), indexs);
        }
    }

    public static void main(String[] args) {
        String str = "-starId-.-id-.-abc-";
        System.out.println(ExpressionUtil.convertExp(str));
    }
}
