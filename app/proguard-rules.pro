-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-dontoptimize
-dontnote
-verbose

-keepattributes SourceFile,LineNumberTable,Signature,RuntimeVisibleAnnotations
-keepattributes *Annotation*,EnclosingMethod

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.crittercism.**
-keep class com.badlogic.**
-keep public class com.google.code.linkedinapi.**
-keep class com.google.**
-keep public class * implements java.lang.reflect.Type
-keep class java.lang.reflect.**
-keep class com.facebook.** {*;}
-keep class javax.**  { *; }
-keep class org.**  { *; }
-keep class twitter4j.**  { *; }
-keep class java.lang.management.**  { *; }
-keep class com.google.code.**  { *; }
-keep class oauth.signpost.**  { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.j256.**
-keep class org.codehaus.** { *; }
-keep class org.apache.commons.** { *; }
-keep class org.w3c.** { *; }
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class **.model.** { *; }
-keep public class **.model.** {
  public void set*(***);
  public *** get*();
}
-keep public class com.androidquery.*,com.androidquery.callback.*,com.androidquery.util.AQUtility,com.androidquery.util.Constants
-keep public class *{
    public protected *;
}
-keepclassmembers public class com.crittercism.*{*;}

-dontwarn twitter4j.**
-dontwarn javax.xml.**
-dontwarn javax.xml.stream.events.**
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn org.apache.**
-dontwarn oauth.signpost.**

-keepnames class org.codehaus.jackson.** { *; }

-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keepclassmembers public final enum org.codehaus.jackson.annotate.JsonAutoDetect$Visibility {
    public static final org.codehaus.jackson.annotate.JsonAutoDetect$Visibility *;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
   java.lang.Object readResolve();
}