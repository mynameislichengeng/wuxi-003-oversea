package com.lc.baseui.tools.data;

import org.xml.sax.ContentHandler;

/**
 * Created by Administrator on 2017/4/23.
 */

public class XMLSAXUtil {
    public interface XMLSerialize extends ContentHandler {
        public <T> T getResult();
    }

    /**
     * 进行解析
     *
     * @param xmlStr              需要解析的xml
     * @param contentHandlerClass 继承sax的解析器，它同时要实现 XMLSerialize
     * @return 返回解析的t
     **/
    public static <T> T fromXml(String xmlStr, Class<? extends XMLSerialize> contentHandlerClass) {
        try {
            ContentHandler handler = contentHandlerClass.newInstance();
            android.util.Xml.parse(xmlStr, handler);
            T t = ((XMLSerialize) handler).getResult();
            return t;
        } catch (Exception e) {

        }
        return null;
    }
}
