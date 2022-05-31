package com.rex.flutter_subscreen_plugin

import android.app.Presentation
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Display
import androidx.annotation.RequiresApi
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * @Description:    副屏dialog
 * @Author:         liyufeng
 * @CreateDate:     2021/3/16 10:55 AM
 */

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
class FlutterSubScreenPresentation(
    outerContext: Context?,
    display: Display?,
    engine: FlutterEngine
) :
    Presentation(outerContext, display) {

    var flutterEngine: FlutterEngine = engine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flutter_presentation_view)
        val flutterView: FlutterView = findViewById(R.id.flutter_presentation_view)
        flutterView.attachToFlutterEngine(flutterEngine)
    }

    override fun show() {
        super.show()
        // 一定要调用 不然页面会卡死不更新
        flutterEngine.lifecycleChannel.appIsResumed()
    }

    override fun dismiss() {
        flutterEngine.lifecycleChannel.appIsDetached()
        super.dismiss()
    }

}