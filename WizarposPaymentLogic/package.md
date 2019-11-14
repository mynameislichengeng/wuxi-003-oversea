#收款打包
##收款目前服务器地址有:

    public static final String IP_TRAIN     = "train.wizarpos.com";         //train
    public static final String IP_CASHIER   = "cashier.91huishang.com";     //91
    public static final String IP_C2        = "cashier2.wizarpos.com";      //c2
    public static final String IP_P3        = "portal3.wizarpos.com";       //p3
    public static final String IP_ZQL       = "cashier.skypay.cn";          //中青旅
    public static final String IP_DZX       = "app.diangedan.net";          //点个单
    public static final String IP_YJJ       = "app2.yjinjing.com";          //易筋经

##打包方法:

###对 com.wizarpos.pay.app.AppConfigInitUtil 类中 channelInit 方法 channelName 属性赋渠道值
例如:
    
    final String channelName = "laswon";

目前渠道有:

    train,
    p3,
    c2,
    cahiser,
    lawson(罗森),
    zql(中青旅),
    dzx(东志信),
    yjj(易筋经)

###确认 com.wizarpos.pay.common.Constants 中 以下字段是否正确:
   
    DEFAULT_IP (接口版本)
    SERVER_VERSION (服务器地址)