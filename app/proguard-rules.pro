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

-keep class retrofit2.** { *; }
-keep class com.sparta.museo.** { *; }
-keep interface com.sparta.museo.** { *; }

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class com.kakao.** { *; }

-keep class com.kakao.vectormap.** { *; }
-keep interface com.kakao.** { *; }

-keep class net.daum.** { *; }

-keep class android.** { *; }

