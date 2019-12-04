运行本项目的方法：
目前项目由2个项目
一个业务逻辑  一个界面 UI
https://git.dev.tencent.com/GitRepository/motionpay-PaymentLogic.git
https://git.dev.tencent.com/GitRepository/motionpay-PaymentHandUI.git

1 下载2个项目
    1 下载项目https://git.dev.tencent.com/GitRepository/motionpay-PaymentLogic.git,并将其命名为WizarposPaymentLogic,而且使用分支origin/Feature_International
    2 下载项目https://git.dev.tencent.com/GitRepository/motionpay-PaymentHandUI.git,并将其命名为WizarposPaymentHandUI,而且使用分支origin/Feature_International

2 在项目WizarposPaymentHandUI中有一个文件夹root-build-gradle
    把里面的setting.gradle和build.gradle拷贝到项目的根目录，作为androidstudio的根结构

3 经过上面三步可以形成如下文件夹结构
      1 WizarposPaymentLogic
      2 WizarposPaymentHandUI
      3 setting.gradle
      4 build.gradle
