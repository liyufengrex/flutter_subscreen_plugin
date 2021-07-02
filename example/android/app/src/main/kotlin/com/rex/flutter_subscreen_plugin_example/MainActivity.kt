package com.rex.flutter_subscreen_plugin_example

import android.os.Bundle
import com.rex.flutter_subscreen_plugin.FlutterSubScreenProvider
import com.rex.flutter_subscreen_plugin.FlutterSubscreenPlugin
import io.flutter.embedding.android.FlutterActivity

class MainActivity: FlutterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FlutterSubScreenProvider.initSubScreen(context, flutterEngine)
    }

}
