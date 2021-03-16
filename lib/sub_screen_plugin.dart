import 'dart:async';

import 'package:flutter/services.dart';

///封装方法用于主副屏交互
class SubScreenPlugin {
  static const _mainChannelName = 'screen_plugin_main_channel';
  static const _subChannelName = 'screen_plugin_sub_channel';

  static MethodChannel _mainChannel = MethodChannel(_mainChannelName)
    ..setMethodCallHandler(_onMainChannelMethodHandler);

  static Future<dynamic> _onMainChannelMethodHandler(MethodCall call) async {
    print(call.method);
  }

  static MethodChannel _subChannel;

  // ignore: close_sinks
  static StreamController<MethodCall> _subStreamController;

  static Stream<MethodCall> get viceStream {
    if (_subChannel == null) {
      _subChannel = MethodChannel(_subChannelName)
        ..setMethodCallHandler(_onSubChannelMethodHandler);
    }
    if (_subStreamController == null) {
      _subStreamController = StreamController<MethodCall>.broadcast();
    }
    return _subStreamController.stream;
  }

  static Future<dynamic> _onSubChannelMethodHandler(MethodCall call) async {
    //副屏channel 没接收到一个事件都放进去流里, 由外部监听
    _subStreamController?.sink?.add(call);
    return "success";
  }

  //给主屏幕调用，发送事件体给副屏
  static Future<void> sendMsgToViceScreen(
    String method, {
    Map<String, dynamic> params,
  }) async {
    await _mainChannel.invokeMethod(method, params);
  }
}
