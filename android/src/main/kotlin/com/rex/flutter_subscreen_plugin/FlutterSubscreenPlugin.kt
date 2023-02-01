package com.rex.flutter_subscreen_plugin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
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
        when (call.method) {
            Constant.METHOD_SUPPORT_DOUBLE_SCREEN -> {
                val isMultipleScreen = FlutterSubScreenProvider.instance.supportViceScreen()
                result.success(isMultipleScreen)
            }
            Constant.METHOD_CHECK_OVERLAY_PERMISSION -> {
                //校验是否具有 overlay 权限
                result.success(FlutterSubScreenProvider.instance.checkOverlayPermission())
            }
            Constant.METHOD_REQUEST_OVERLAY_PERMISSION -> {
                //申请 overlay 权限
                if (Build.VERSION.SDK_INT >= 23) {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
            Constant.METHOD_DOUBLE_SCREEN_SHOW -> {
                //显示副屏
                FlutterSubScreenProvider.instance.showSubDisplay()
            }
            Constant.METHOD_DOUBLE_SCREEN_CANCEL -> {
                //关闭副屏
                FlutterSubScreenProvider.instance.closeSubDisplay()
            }
            else -> {
                //主屏通过mainChannel将事件和参数传递给副屏subChannel
                subChannel.invokeMethod(call.method, call.arguments)
            }
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        mainChannel.setMethodCallHandler(null)
        subChannel.setMethodCallHandler(null)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        //your plugin is now attached to an Activity
        FlutterSubScreenProvider.instance.setFlutterSubCallback(object : IFlutterSubCallback {
            override fun onSubFlutterEngineCreated() {
                //副屏 engine 初始化后，将副屏事件进行分发
                FlutterSubScreenProvider.instance.flutterEngine?.let { engine ->
                    onCreateViceChannel(engine.dartExecutor)
                }
            }
        })
        val autoShowSubScreenWhenInit =
            context.resources.getBoolean(R.bool.autoShowSubScreenWhenInit)
        FlutterSubScreenProvider.instance.doInit(binding.activity, autoShowSubScreenWhenInit)
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
