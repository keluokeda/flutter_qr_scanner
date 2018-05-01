import 'dart:async';

import 'package:flutter/services.dart';

class FlutterQrScanner {
  static const MethodChannel _channel =
      const MethodChannel('github.com/keluokeda/scanner');



  static Future<String> scan() async{
    final String result = await _channel.invokeMethod("scan");
    return result;
  }
}
