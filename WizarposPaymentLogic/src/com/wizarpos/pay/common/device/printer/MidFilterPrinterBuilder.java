package com.wizarpos.pay.common.device.printer;

import com.wizarpos.device.printer.DefaultPrinterImpl;
import com.wizarpos.pay.db.AppConfigDef;
import com.wizarpos.pay.db.AppConfigHelper;

/**
 * 打印实现<br>
 * 移除商户号
 *
 * @author wu
 */
public class MidFilterPrinterBuilder extends DefaultPrinterImpl {

    @Override
    public void print(String str) {
//        str = str.replaceAll("P" + AppConfigHelper.getConfig(AppConfigDef.mid),
//                "P");
        // 移除商户号
//        str = str.replaceAll("T" + AppConfigHelper.getConfig(AppConfigDef.mid), "T");
        //券号中去掉mid@hong 20151221
        super.print(str);
    }
}
