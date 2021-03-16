import 'dart:math';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_subscreen_plugin/sub_screen_plugin.dart';

void main() {
  var defaultRouteName = window.defaultRouteName;
  if ("subMain" == defaultRouteName) {
    viceScreenMain();
  } else {
    defaultMain();
  }
}

//主屏ui
void defaultMain() {
  runApp(MyApp(
    isViceScreen: false,
  ));
}

//副屏ui
void viceScreenMain() {
  runApp(MyApp(
    isViceScreen: true,
  ));
}

class MyApp extends StatefulWidget {
  final bool isViceScreen;

  MyApp({this.isViceScreen = false}) : super();

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String message;

  @override
  void initState() {
    super.initState();
    message =
        widget.isViceScreen ? 'this is vice screen' : 'this is main screen';

    if (widget.isViceScreen) {
      SubScreenPlugin.viceStream.listen((event) {
        setState(() {
          message = event.arguments.toString();
        });
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    //隐藏状态栏和系统底部栏
    SystemChrome.setEnabledSystemUIOverlays([]);
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          color: widget.isViceScreen ? Colors.green : Colors.red,
          child: Center(
            child: Text(message),
          ),
        ),
        floatingActionButton: FloatingActionButton(
          child: Icon(Icons.add),
          onPressed: () {
            final randomInt = Random().nextInt(100);
            SubScreenPlugin.sendMsgToViceScreen("text",
                params: {"num": randomInt.toString()});
          },
        ),
      ),
    );
  }
}
