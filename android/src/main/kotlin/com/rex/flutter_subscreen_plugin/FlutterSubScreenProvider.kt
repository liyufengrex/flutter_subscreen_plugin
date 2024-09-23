package com.rex.flutter_subscreen_plugin

import android.app.Activity
import android.content.Context
import android.media.MediaRouter
import android.media.MediaRouter.ROUTE_TYPE_LIVE_VIDEO
import android.os.Build
import android.view.Display
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.FlutterEngine
import android.media.MediaRouter.SimpleCallback
import android.provider.Settings
import android.view.WindowManager
import com.rex.flutter_subscreen_plugin.FlutterSubscreenPlugin.Companion.addThirdPlugins
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.dart.DartExecutor


/**
 * @Description:    双屏管理提供
 * @Author:         liyufeng
 * @CreateDate:     2021/3/16 10:53 AM
 */

class FlutterSubScreenProvider private constructor() {

    private var mediaRouter: MediaRouter? = null
    var currentActivity: Activity? = null
    var flutterEngine: FlutterEngine? = null
    var presentation: FlutterSubScreenPresentation? = null

    private var iCallback: IFlutterSubCallback? = null

    companion object {
        val instance: FlutterSubScreenProvider by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            FlutterSubScreenProvider()
        }
    }

    fun setFlutterSubCallback(callback: IFlutterSubCallback) {
        iCallback = callback
    }

    private val mMediaRouterCallback: SimpleCallback = object : SimpleCallback() {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun onRouteSelected(router: MediaRouter?, type: Int, info: MediaRouter.RouteInfo) {
            ///发现可用的扩展屏
            showSubDisplay()
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun onRouteUnselected(
            router: MediaRouter?,
            type: Int,
            info: MediaRouter.RouteInfo
        ) {
            ///无可用扩展屏幕
            closeSubDisplay()
        }

        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        override fun onRoutePresentationDisplayChanged(
            router: MediaRouter?,
            info: MediaRouter.RouteInfo
        ) {
            ///可用扩展屏幕发生变更
            showSubDisplay()
        }
    }

    /**
     * 执行初始化，由外部调用
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun doInit(activity: Activity, showSubScreen: Boolean) {
        currentActivity = activity
        mediaRouter = activity.applicationContext.getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter

        //媒体设备监听
        mediaRouter?.addCallback(ROUTE_TYPE_LIVE_VIDEO, mMediaRouterCallback)
        if (showSubScreen) {
            showSubDisplay()
        }
    }

    fun onDispose() {
        try {
            mediaRouter?.removeCallback(mMediaRouterCallback)
        } catch (e: Exception) {

        } finally {
            flutterEngine = null
            mediaRouter = null
            iCallback = null
            presentation = null
            currentActivity = null
        }
    }

    /**
     * 初始化副屏 flutterEngine
     */
    private fun doInitEngine() {
        currentActivity?.let { activity ->
            if (flutterEngine != null) {
                //保证只初始化一次副屏 engine
                return
            }
            //初始化副屏
            flutterEngine = FlutterEngine(activity)
            addThirdPlugins()
            //指定初始化路由
            flutterEngine?.navigationChannel?.setInitialRoute(FlutterSubscreenPlugin.subMainRouter)
            flutterEngine?.dartExecutor?.executeDartEntrypoint(
                DartExecutor.DartEntrypoint(
                    FlutterInjector.instance().flutterLoader().findAppBundlePath(),
                    FlutterSubscreenPlugin.mainRouter
                )
            )
            iCallback?.onSubFlutterEngineCreated()
        }
    }


    /**
     * 获取扩展显示屏
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getPresentationDisplay(): Display? {
        val route = mediaRouter?.getSelectedRoute(ROUTE_TYPE_LIVE_VIDEO)
        if (route != null) {
            return route.presentationDisplay
        }
        return null
    }

    /**
     * 是否支持双屏
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun supportViceScreen(): Boolean {
        return getPresentationDisplay() != null
    }

    /**
     * show副屏
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun showSubDisplay() {
        //先把之前的隐藏
        closeSubDisplay()
        currentActivity?.let {
            if (!it.isFinishing) {
                configSecondDisplay(it)
            }
        }
    }

    /**
     *  hide副屏
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun closeSubDisplay() {
        presentation?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        presentation = null
    }

    /**
     * 校验是否具备 overlay 权限
     */
    fun checkOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23 && currentActivity != null) {
            Settings.canDrawOverlays(currentActivity)
        } else {
            true
        }
    }

    /**
     * 显示扩展屏内容
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun configSecondDisplay(context: Context) {
        val display = getPresentationDisplay()
        if (display != null) {
            doInitEngine()
            flutterEngine?.let { engine ->
                try {
                    presentation = FlutterSubScreenPresentation(context, display, engine)
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        if (checkOverlayPermission()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                presentation?.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY - 1)
                            } else {
                                presentation?.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
                            }
                        }
                    }
                    presentation?.show()
                } catch (e: Throwable) {
                    println(e.message)
                    e.printStackTrace()
                }
            }
        }
    }
}