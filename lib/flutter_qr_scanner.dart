import 'dart:async';

import 'dart:typed_data';
import 'package:flutter/services.dart';

class FlutterQrScanner {
  static const MethodChannel _channel =
  const MethodChannel('github.com/keluokeda/scanner');


  static Future<String> scan() async {
    final String result = await _channel.invokeMethod("scan");
    return result;
  }

  static Future<Uint8List> createQRImageData(String content, int size) async {
    final Uint8List data = await _channel.invokeMethod(
        "createQRImageData", {"content": content, "size": size});

    return data;
  }
}
