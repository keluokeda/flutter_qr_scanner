import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_qr_scanner/flutter_qr_scanner.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {

  String _qrCode = "";

  @override
  initState() {
    super.initState();
  }

  scan() async{
    String result =await FlutterQrScanner.scan();
    if(!mounted){
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
          child: new Text(_qrCode),
        ),
      ),
    );
  }
}
