# ==============
# Capacitor v3 插件和注解的混淆规则
# ==============
-keep @com.getcapacitor.annotation.CapacitorPlugin public class * {
    @com.getcapacitor.annotation.PermissionCallback <methods>;
    @com.getcapacitor.annotation.ActivityCallback <methods>;
    @com.getcapacitor.annotation.Permission <methods>;
    @com.getcapacitor.PluginMethod public <methods>;
}

-keep public class * extends com.getcapacitor.Plugin { *; }

# Capacitor v2 插件规则（如果不使用可以删除）
-keep @com.getcapacitor.NativePlugin public class * {
  @com.getcapacitor.PluginMethod public <methods>;
}

# Cordova 插件的混淆规则（如果不使用可以删除）
-keep public class * extends org.apache.cordova.* {
  public <methods>;
  public <fields>;
}

# 保留 MainActivity
-keep class jp.hbox.ImoutoFantasy.MainActivity { *; }

# 保留所有 Capacitor 相关类
-keep class com.getcapacitor.** { *; }

# ==============
# Kotlin 相关混淆规则
# ==============
-dontwarn kotlin.**
-keep class kotlin.** { *; }
-keep class kotlin.reflect.jvm.internal.impl.** { *; }

# 忽略 DebugProbesKt 类的所有成员
-dontwarn kotlin.io.*

# ==============
# 其他第三方库的混淆规则
# ==============
# 比如，如果使用了 Gson，可以保留以下规则
-keep class com.google.gson.** { *; }

# 如果使用了 Retrofit
-keep class retrofit2.** { *; }
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
