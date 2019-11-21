# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\user\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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


#-keep class com.pitambari.smartsales.** {
#   *;
#}
#
#-dontwarn okio.**
#-dontwarn com.google.common.**
#-dontwarn com.google.android.gms.**
#-dontwarn retrofit2.Platform$Java8
-ignorewarnings
-keep class * {
    public private *;
}


#ACRA specifics
# Restore some Source file names and restore approximate line numbers in the stack traces,
# otherwise the stack traces are pretty useless
-keepattributes SourceFile,LineNumberTable

# ACRA needs "annotations" so add this...
# Note: This may already be defined in the default "proguard-android-optimize.txt"
# file in the SDK. If it is, then you don't need to duplicate it. See your
# "project.properties" file to get the path to the default "proguard-android-optimize.txt".
-keepattributes *Annotation*

# ACRA loads Plugins using reflection, so we need to keep all Plugin classes
-keep class * implements org.acra.plugins.Plugin {*;}

# ACRA uses enum fields in annotations, so we have to keep those
-keep enum org.acra.** {*;}

-dontwarn android.support.**

# Gson uses generic type information stored in a class file when working with fields.
# Proguard removes such information by default, so configure it to keep all of it.
-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Annotation

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }