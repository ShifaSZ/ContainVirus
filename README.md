# ContainVirus
Helping people to identify infection risk while there is a pandemic disease.

## 背景
最近流行的2019 nCoV病毒潜伏期长，传染性强。很多人得病后很难弄清自己是在哪里被谁传染的。为了更有效地阻止病毒传播，我们开发这个手机应用，精确追踪到其过去半个月到一个月的行程。如果自己不幸染病，可以把数据以匿名方式提交给疾病控制部门分析感染源，并提醒相关人员主动隔离，防范病毒进一步扩散。如果没有得病，也可以利用这个软件根据自己的行踪和疾控部门公布的数据分析自己被传染的风险。

## 1. 软件安装和使用说明
ncov软件是全志愿者开发，完全开源、免费的软件，请放心下载使用。
### 1.1 下载
下载链接：https://github.com/ShifaSZ/ContainVirus/blob/master/ncov/app/release/app-release.apk
这是试用版，稍后在正式版在各手机应用市场上线。我们保证正式版和试用版兼任，所以可以放心使用。

### 1.2 安装运行
安装时要给这个软件打开获取位置，访问网络，和存储权限，并允许后台运行。<br/>
这个软件按最小处理能力占用，最小网络流量，最小电源消耗，适用100%安卓手机的原则设计，所以可以放心让它在后台运行。<br/>
运行节面如下：<br/>
<img src="https://user-images.githubusercontent.com/33550059/73868681-e6fc5b80-4816-11ea-8d7e-310ee2471d4e.png" width="380">
<br/>安装后不需要任何操作，按"Home"键将其置于后台运行即可。这个软件每分钟收集一个位置信息，并保存在一个数据文件中。这个数据文件用数字签名密钥加密，任何其他软件都不能读取这个文件，所以不用担心木马或其他软件窃取您的隐私。<br/>
随时可以打开这个软件查看数据收集状态，只要按下面的“查看记录”按钮，就可以看到最近的16条位置记录。每天记录前面有个序号，表示连续记录了多少条记录了。如果需要从新从1开始记录了，说明软件被强制退出。为了保证数据连续记录，确保这个软件(ncov)不会被节能设置、内存清理软件、手机管家这些软件强制退出。<br/>
当连续运行15天（要求中断比较少）后，将给您解锁利用行踪数据和疾控中心的数据新冠病毒感染风险分析功能。<br/>

## 2. Use the source code
If you like to use the source code to embed it into your software, what you need to do is to copy the libs under ncov/app/libs and ncov/app/src/main/java/com/baidu/* into your project and use the code in ncov/app/src/main/java/com/contain/ncov/MainActivity.java as a reference to write your own codes. <br/><br/>
It uses the Baidu map API SDK. The SDK needs a Application Key (AK). You have to apply one one the Baidu website following the instructions in http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/create-project/key. Then, you can following the guides in http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/create-project/android-studio or http://lbsyun.baidu.com/index.php?title=android-locsdk/guide/create-project/eclipse to configure your project.

## 问题反馈
欢迎直接在github issues上反馈遇到的问题，链接是：https://github.com/ShifaSZ/ContainVirus/issues
<br/>欢迎各种建议。
