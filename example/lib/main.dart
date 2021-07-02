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
  runApp(MainApp());
}

//副屏ui
void viceScreenMain() {
  runApp(SubApp());
}

//主屏widget
class MainApp extends StatefulWidget {
  const MainApp({Key key}) : super(key: key);

  @override
  _MainAppState createState() => _MainAppState();
}

class _MainAppState extends State<MainApp> {
  String receiveData = 'null';

  @override
  void initState() {
    super.initState();
    SubScreenPlugin.mainStream.listen((event) {
      setState(() {
        receiveData = event.arguments.toString();
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('主屏'),
        ),
        body: Container(
          color: Colors.greenAccent.withAlpha(50),
          child: Center(
            child: Text('接收到的副屏数据为：$receiveData'),
          ),
        ),
        floatingActionButton: FloatingActionButton(
          child: Text('发送数据给副屏'),
          onPressed: () {
            final randomData = Random().nextInt(100).toString();
            SubScreenPlugin.sendMsgToViceScreen("text",
                params: {"num": randomData});
          },
        ),
      ),
    );
  }
}

//副屏widget
class SubApp extends StatefulWidget {
  const SubApp({Key key}) : super(key: key);

  @override
  _SubAppState createState() => _SubAppState();
}

class _SubAppState extends State<SubApp> {
  String receiveData = 'null';

  @override
  void initState() {
    super.initState();
    SubScreenPlugin.viceStream.listen((event) {
      setState(() {
        receiveData = event.arguments.toString();
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('副屏'),
        ),
        body: Container(
          color: Colors.yellowAccent.withAlpha(50),
          child: Center(
            child: Text('接收到的主屏数据为：$receiveData'),
          ),
        ),
        floatingActionButton: FloatingActionButton(
          child: Text('发送数据给主屏'),
          onPressed: () {
            final randomData = Random().nextInt(100).toString();
            SubScreenPlugin.sendMsgToViceScreen("text",
                params: {"num": randomData});
          },
        ),
      ),
    );
  }
}
