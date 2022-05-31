import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_subscreen_plugin/flutter_subscreen_plugin.dart';

///主屏widget
class MainApp extends StatefulWidget {
  const MainApp({Key key}) : super(key: key);

  @override
  _MainAppState createState() => _MainAppState();
}

class _MainAppState extends State<MainApp> {
  final _messangerKey = GlobalKey<ScaffoldMessengerState>();
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

  void sendMsgToSubScreen() {
    SubScreenPlugin.isMultipleScreen.then((isMultipleScreen) {
      if (isMultipleScreen) {
        final randomData = Random().nextInt(100).toString();
        SubScreenPlugin.sendMsgToViceScreen(
          "text",
          params: {"num": randomData},
        );
      } else {
        _messangerKey.currentState.showSnackBar(
          const SnackBar(
            content: Text('未查询到可用副屏'),
          ),
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      scaffoldMessengerKey: _messangerKey,
      home: Scaffold(
          appBar: AppBar(
            title: const Text('主屏'),
          ),
          body: Center(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Text('接收到的副屏数据为：$receiveData'),
                SizedBox(height: 30),
                TextButton(
                    onPressed: sendMsgToSubScreen, child: Text('发送数据给副屏')),
              ],
            ),
          )),
    );
  }
}
