(function (win) {
    var ws;
    var auth = false;
    var Client = function (options) {
        var MAX_CONNECT_TIME = 10;
        var DELAY = 15000;
        this.options = options || {};
        this.createConnect(MAX_CONNECT_TIME, DELAY);
    }

    Client.prototype.send = function () {
        var from_shell_id = document.getElementById("from_shell_id").value;
        var to_shell_id = document.getElementById("to_shell_id").value;
        var content = document.getElementById("content").value;

        ws.send(JSON.stringify({
            'version': 1,
            'commandId': 3,
            //'seqId': 3,
            'shellId': 723181,
            'body': {
                'from_shell_id': 723181,
                'to_shell_id': 852963,
                'content': content
            }
        }));
    }

    Client.prototype.createConnect = function (max, delay) {
        var self = this;
        if (max === 0) {
            return;
        }
        connect();

        var heartbeatInterval;

        function connect() {
            ws = new WebSocket('ws://localhost:19999/chat');

            ws.onopen = function () {
                getAuth();
            }

            ws.onmessage = function (evt) {
                var data = JSON.parse(evt.data);
                if (data.commandId == 1) {
                    auth = true;
                    heartbeat();
                    heartbeatInterval = setInterval(heartbeat, 4 * 60 * 1000);
                }
                if (!auth) {
                    setTimeout(getAuth, delay);
                }
                if (auth && data.commandId == 3) {
                    var notify = self.options.notify;
                    if (notify) {
                        notify(data.body);
                    }
                }
            }

            ws.onclose = function () {
                if (heartbeatInterval) {
                    clearInterval(heartbeatInterval);
                }
                setTimeout(reConnect, delay);
            }

            function heartbeat() {
                ws.send(JSON.stringify({
                    'version': 1,
                    'commandId': 7,
                    'shellId': 723181,
                    'body': {
                        'content': 'hello'
                    }
                }));
            }

            function getAuth() {
                ws.send(JSON.stringify({
                	'version': 1,
                    'commandId': 1,
                    'shellId': 723181,
                    'body': {
                        'shell_id': 723181,
                        'token': 'test'
                    }
                }));
            }
        }

        function reConnect() {
            self.createConnect(--max, delay * 2);
        }
    }

    win['MyClient'] = Client;
})(window);