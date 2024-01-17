import 'dart:ui';
import 'package:flutter/material.dart';
import 'main_widget.dart';
import 'sub_main_widget.dart';

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
  runApp(MaterialApp(home: MainApp(),));
}

//副屏ui
void viceScreenMain() {
  runApp(SubApp());
}
