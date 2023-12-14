# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-assumenosideeffects class android.util.Log {
    public static *** d(...);
   public static *** v(...);
   public static *** w(...);
    public static *** i(...);
    public static *** e(...);
}

-keepclassmembers class com.chubasamuel.clinfind.util.APIModels{*;}
-keepclassmembers class com.chubasamuel.clinfind.util.APIModels$AppUpdateAPIModel{*;}
-keepclassmembers class com.chubasamuel.clinfind.util.APIModels$DevUpdateAPIModel{*;}
-keepclassmembers class com.chubasamuel.clinfind.util.APIModels$APIUpdates{*;}
-keepclassmembers class com.chubasamuel.clinfind.util.UpdatesUtil{*;}
-keepclassmembers class com.chubasamuel.clinfind.di.AppModule{*;}
-keep class com.chubasamuel.clinfind.data.local.Facility{*;}
-keepclassmembers interface com.chubasamuel.clinfind.data.remote.Requests{*;}


-keepattributes *Annotation*,Signature,Exception, EnclosingMethod, InnerClasses
-keepattributes SourceFile,LineNumberTable
-keep class com.google.api.services.drive.* { *; }
-keep class com.google.api.*{*;}
-keep class com.google.android.gms.*{*;}

-keep class com.google.gson.** { *; }
-keep public class com.google.gson.** {public private protected *;}
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class retrofit.** { *; }
-keep class com.google.appengine.** { *; }
-dontwarn com.squareup.okhttp.*
-dontwarn rx.**
-dontwarn javax.xml.stream.**
-dontwarn com.google.appengine.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.**


-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit2.** { *; }
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform

-keep class * extends dagger.hilt.*
-keep interface dagger.**{*;}
-keepclassmembers class *{
native <methods>;
}

# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v7.**
# don't process support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }


-dontwarn okhttp3.internal.platform.*
-dontwarn org.conscrypt.Conscrypt
