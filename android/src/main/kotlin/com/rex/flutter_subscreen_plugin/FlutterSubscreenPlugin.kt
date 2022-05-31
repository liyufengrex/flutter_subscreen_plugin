package com.rex.flutter_subscreen_plugin

import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.dart.DartExecutor

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterSubscreenPlugin */
class FlutterSubscreenPlugin : FlutterPlugin, ActivityAware, MethodCallHandler {

    private lateinit var context: Context
    private lateinit var mainChannel: MethodChannel
    private lateinit var subChannel: MethodChannel

    companion object {
        private const val mainChannelName = "screen_plugin_main_channel"
        private const val subChannelName = "screen_plugin_sub_channel"
        //用于设置副屏 flutterEngine 需要引入的三方插件库
        var tripPlugins: ArrayList<FlutterPlugin>? = null
        //主屏路由
        const val mainRouter = "main"
        //副屏路由
        const val subMainRouter = "subMain"
    }

    private fun onCreateViceChannel(dartExecutor: DartExecutor) {
        subChannel = MethodChannel(dartExecutor, subChannelName)
        //将副屏事件中转给主屏的engine
        subChannel.setMethodCallHandler { call, _ ->
            mainChannel.invokeMethod(call.method, call.arguments)
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        mainChannel = MethodChannel(flutterPluginBinding.binaryMessenger, mainChannelName)
        mainChannel.setMethodCallHandler(this)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        //提供方法查询是否支持双屏
        if (call.method == "supportDoubleScreen") {
            val isMultipleScreen = FlutterSubScreenProvider.instance.supportViceScreen()
            result.success(isMultipleScreen)
            return
        }
        //主屏通过mainChannel将事件和参数传递给副屏subChannel
        subChannel.invokeMethod(call.method, call.arguments)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        mainChannel.setMethodCallHandler(null)
        subChannel.setMethodCallHandler(null)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        //your plugin is now attached to an Activity
        FlutterSubScreenProvider.instance.doInit(binding.activity)
        //将副屏事件进行分发
        FlutterSubScreenProvider.instance.flutterEngine?.let { engine ->
            onCreateViceChannel(engine.dartExecutor)
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        //the Activity your plugin was attached to was
        // destroyed to change configuration.
        // This call will be followed by onReattachedToActivityForConfigChanges().
        //暂无处理
    }

    override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
        //your plugin is now attached to a new Activity
        // after a configuration change.
        //暂无处理
    }

    override fun onDetachedFromActivity() {
        //your plugin is no longer associated with an Activity.
        FlutterSubScreenProvider.instance.onDispose()
    }
}
