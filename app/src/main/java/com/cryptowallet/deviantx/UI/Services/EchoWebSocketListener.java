package com.cryptowallet.deviantx.UI.Services;

import com.cryptowallet.deviantx.Utilities.CommonUtilities;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static com.instabug.library.Instabug.getApplicationContext;

public class EchoWebSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
//        webSocket.send("Hello, it's SSaurel !");
//        webSocket.send("What's up ?");
//        webSocket.send(ByteString.decodeHex("deadbeef"));
        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        CommonUtilities.ShowToastMessage(getApplicationContext(), "Receiving : " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
//        output("Receiving bytes : " + bytes.hex());
        CommonUtilities.ShowToastMessage(getApplicationContext(), "Receiving bytes : " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
//        output("Closing : " + code + " / " + reason);
        CommonUtilities.ShowToastMessage(getApplicationContext(), "Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//        output("Error : " + t.getMessage());
        CommonUtilities.ShowToastMessage(getApplicationContext(), "Error : " + t.getMessage());
    }
}