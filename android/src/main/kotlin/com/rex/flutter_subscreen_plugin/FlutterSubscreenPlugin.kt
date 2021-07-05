package com.rex.flutter_subscreen_plugin

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.dart.DartExecutor

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** FlutterSubscreenPlugin */
class FlutterSubscreenPlugin: FlutterPlugin, ActivityAware, MethodCallHandler {
  private lateinit var mainChannel : MethodChannel
  private lateinit var subChannel : MethodChannel

  companion object {
    private const val mainChannelName = "screen_plugin_main_channel"
    private const val subChannelName = "screen_plugin_sub_channel"

    //主屏路由
    const val mainRouter = "main"
    //副屏路由
    const val subMainRouter = "subMain"
  }

  fun onCreateViceChannel(dartExecutor: DartExecutor) {
    subChannel = MethodChannel(dartExecutor, subChannelName)
    //将副屏事件中转给主屏的engine
    subChannel.setMethodCallHandler { call, _ ->
      mainChannel.invokeMethod(call.method, call.arguments)
    }
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    mainChannel = MethodChannel(flutterPluginBinding.binaryMessenger, mainChannelName)
    mainChannel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    //主屏通过mainChannel将事件和参数传递给副屏subChannel
    if (subChannel != null) {
      subChannel.invokeMethod(call.method, call.arguments)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    mainChannel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    //your plugin is now attached to an Activity
    //初始化副屏
    FlutterSubScreenProvider.configSecondDisplay(this, binding.activity)
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
    //暂无处理
  }
}
