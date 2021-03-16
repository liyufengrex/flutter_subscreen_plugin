package com.rex.flutter_subscreen_plugin

import androidx.annotation.NonNull
import io.flutter.embedding.engine.dart.DartExecutor

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** FlutterSubscreenPlugin */
class FlutterSubscreenPlugin: FlutterPlugin, MethodCallHandler {
  private lateinit var mainChannel : MethodChannel
  private lateinit var subChannel : MethodChannel

  companion object {
    private const val mainChannelName = "screen_plugin_main_channel"
    private const val subChannelName = "screen_plugin_sub_channel"
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
    //主屏通过mainchannel将事件和参数传递给副屏subChannel
    if (subChannel != null) {
      subChannel.invokeMethod(call.method, call.arguments)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    mainChannel.setMethodCallHandler(null)
  }
}
