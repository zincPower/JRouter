package com.zinc.jrouter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zinc.libannotation.Param;
import com.zinc.librouter.impl.RouteRequest;
import com.zinc.librouter.impl.Router;
import com.zinc.librouter.matcher.AbsImplicitMatcher;
import com.zinc.librouter.utils.RLog;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/17
 * @description
 */

public class MyBrowserMatcher extends AbsImplicitMatcher {

    public MyBrowserMatcher(int priority) {
        super(priority);
    }

    @Override
    public boolean match(Context context, Uri uri, @Nullable String route, RouteRequest routeRequest) {

        if (!TextUtils.isEmpty(uri.getQuery())) {
            StringBuilder stringBuilder = new StringBuilder();
            Set<String> paramNames = uri.getQueryParameterNames();
            Iterator<String> iterator = paramNames.iterator();
            while (iterator.hasNext()) {
                String paramName = iterator.next();
                String paramValue = uri.getQueryParameter(paramName);
                generatorQueryParam(stringBuilder, paramName, paramValue);
            }

            String path = uri.toString().substring(0, uri.toString().lastIndexOf("?" + uri.getQuery()));

            uri = Uri.parse(path + "?" + stringBuilder.substring(0, stringBuilder.lastIndexOf("&")));

        }

        if (Router.getCommonParams().size() > 0) {        //需要加通用参数

            String url = uri.toString();

            boolean isAddParam = false;

            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : Router.getCommonParams().entrySet()) {
                if (!TextUtils.isEmpty(uri.getQueryParameter(entry.getKey()))) {   //若uri中有该参数，无操作。
                    continue;
                }
                isAddParam = true;
                generatorQueryParam(stringBuilder, entry.getKey(), entry.getValue());
            }

            if (TextUtils.isEmpty(uri.getQuery())) {      //链接无参，需要增加"？"
                url = url + "?";
            }

            if (isAddParam) {
                url = url + "&" + stringBuilder.substring(0, stringBuilder.lastIndexOf("&"));
            }

            routeRequest.setUri(Uri.parse(url));

        }

        return (uri.toString().toLowerCase().startsWith("http://")
                || uri.toString().toLowerCase().startsWith("https://"));
    }

    private String replaceParamValue(String paramName, String paramValue) {
        if (paramValue.matches("^\\{{3}.*\\}{3}$")) {
            RLog.i(String.format("需找到需要动态配置参数：%s--->%s", paramName, paramValue));
            String innerValue = paramValue.substring(0, paramValue.lastIndexOf("}}}")).replaceFirst("\\{{3}", "");
            return getParamRealValue(innerValue);
        } else {
            return paramValue;
        }
    }

    private String getParamRealValue(String content) {
        if (content.length() <= 0) {
            return "";
        }
        String[] params;
        if (content.contains(".")) {
            params = content.split("\\.");
        } else {
            params = new String[]{content};
        }

        String result = "";
        String type = params[0].toLowerCase();
        String param = params.length > 1 ? params[1] : "";
        switch (type) {
            case "user":
                try {
                    Class clazz = UserCache.user.getClass();
                    Method method = clazz.getDeclaredMethod(obtainMethodName(param));
                    result = method.invoke(UserCache.user).toString();
                } catch (Exception e) {
                    RLog.e(String.format("未找到方法:%s", obtainMethodName(param)));
                    e.printStackTrace();
                }
                break;

            case "team":
                result = "jiang";
                break;
        }
        return result;
    }

    private void generatorQueryParam(StringBuilder stringBuilder, String key, String value) {
        stringBuilder.append(key);
        stringBuilder.append("=");
        stringBuilder.append(replaceParamValue(key, value));
        stringBuilder.append("&");
    }

    private String obtainMethodName(String param){
        if(param.length()>1){
            return "get"+Character.toUpperCase(param.charAt(0))+param.substring(1);
        }else if(param.length() == 1){
            return "get"+Character.toUpperCase(param.charAt(0));
        }
        return "";
    }

}
