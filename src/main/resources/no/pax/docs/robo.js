var motorA;
var motorB;
var debugValues;
var connectionStatus;

var constance = {
    protocolName: "robo",
    serverName: "SERVER"
};

if (!window.WebSocket) {
    window.WebSocket = window.MozWebSocket;
    if (!window.WebSocket) {
        alert("WebSocket not supported by this browser");
    }
}

var connection = {
    initConnection: function () {
        var location = document.location.toString()
            .replace('http://', 'ws://')
            .replace('https://', 'wss://')
            + constance.protocolName + "/";

        this._ws = new WebSocket(location, constance.protocolName);
        this._ws.onopen = this.onopen;
        this._ws.onmessage = this.onmessage;
        this._ws.onclose = this.onclose;
    },

    onopen: function () {
        connectionStatus.innerHTML = 'Connected';
    },
    send: function (message) {
        this._ws.send(message);
    },
    onmessage: function (m) {
        var jsonObject = eval("(" + m.data + ')');     // eval is evil use JQuery method or something like that.
    },
    onclose: function () {
        connectionStatus.innerHTML = 'Closed';
        this._ws = null;
    }
};

function initStuff() {
    motorA = document.getElementById('motorA');
    motorB = document.getElementById('motorB');
    debugValues = document.getElementById('debugValues');
    connectionStatus = document.getElementById('connectionStatus');

    connection.initConnection();
}

function motorChanged() {
    debugValues.innerHTML = " Motor A:" + motorA.value + ", Motor B: " + motorB.value;

    sendMessage(motorA.value, motorB.value);
}

function nullIt() {
    motorA.value = 0;
    motorB.value = 0;

    motorChanged();
}

function sendMessage(motorA_value, motorB_value) {
    connection.send("{'motorA' : '" + motorA_value + "', 'motorB' : '" + motorB_value + "'}");
}
      