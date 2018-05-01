import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_qr_scanner/flutter_qr_scanner.dart';
import 'dart:typed_data';


void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {

  String _qrCode = "";

  String text = "hello";

  Uint8List _imageData;


  @override
  initState() {
    super.initState();

    createQRImageData();
  }

  createQRImageData() async {
    Uint8List data = await FlutterQrScanner.createQRImageData(text, 200);

    if (mounted) {
      setState(() {
        _imageData = data;
      });
    }
  }

  scan() async {
    String result = await FlutterQrScanner.scan();
    if (!mounted) {
      return;
    }

    setState(() {
      _qrCode = result;
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Plugin example app'),
          actions: <Widget>[
            new IconButton(icon: new Icon(Icons.camera_alt), onPressed: scan)
          ],
        ),
        body: new Center(
          child: _imageData != null ? new Image.memory(_imageData) : new Text(
              _qrCode),
        ),
      ),
    );
  }
}
