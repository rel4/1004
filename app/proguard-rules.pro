# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
#-dontpreverify
-dontwarn
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


#-----------keep-------------------

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.view.ViewPager

-dontwarn com.android.volley.**
-keep class com.android.volley.**{*;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.**{*;}

-keep class com.cdcm.bean.**{*;}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

-keepattributes Exceptions,InnerClasses
-keep public class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}


-ignorewarning
-keep public class * extends android.widget.TextView


#--------------unionpay 3.1.0--------------
-keep class com.unionpay.** {*;}
-keep class com.UCMobile.PayPlugin.** {*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service.** {*;}

#--------------ecopay2.0--------------
-keep class com.payeco.android.plugin.** {*;}
-keep class org.payeco.http.entity.mime.** {*;}
-dontwarn com.payeco.android.plugin.**

-keepclassmembers class com.payeco.android.plugin {
  public *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*


#--------------sms--------------
-keep class com.iapppay.sms.** {*;}

#--------------alipay-------------
-keep class com.ta.utdid2.** {
    public <fields>;
    public <methods>;
}
-keep class com.ut.device.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.android.app.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.sdk.** {
    public <fields>;
    public <methods>;
}
-keep class com.alipay.mobilesecuritysdk.** {
    public <fields>;
    public <methods>;
}
-keep class HttpUtils.** {
    public <fields>;
    public <methods>;
}


#-----------keep iapppay-------------------
-keep class com.iapppay.account.channel.ipay.IpayAccountApi {*;}
-keep class com.iapppay.account.channel.ipay.IpayOpenidApi {*;}
-keep class com.iapppay.pay.channel.oneclickpay.OnekeyPayHandler {*;}
-keep class com.iapppay.utils.RSAHelper {*;}
-keep class com.iapppay.account.channel.ipay.view.** {
    public <fields>;
    public <methods>;
}
-keep class com.iapppay.sdk.main.** {
    public <fields>;
    public <methods>;
}
-keep class com.iapppay.interfaces.callback.** {*;}

-keep class com.iapppay.interfaces.** {
    public <fields>;
    public <methods>;
}


#iapppay UI
-keep public class com.iapppay.ui.activity.** {
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.fastpay.ui.** {
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.fastpay.view.** {
    public <fields>;
    public <methods>;
}

# View
-keep public class com.iapppay.ui.widget.**{
    public <fields>;
    public <methods>;
}

-keep public class com.iapppay.ui.view.**{
    public <fields>;
    public <methods>;
}


#iapppay_sub_pay
-keep public class com.iapppay.pay.channel.** {
    public <fields>;
    public <methods>;
}

-keep class com.iapppay.tool {*;}
-keep class com.iapppay.service {*;}
-keep class com.iapppay.provider {*;}
-keep class com.iapppay.apppaysystem {*;}

#openid sdk
-keep class com.iapppay.openid.channel.IpayOpenidApi {*;}








