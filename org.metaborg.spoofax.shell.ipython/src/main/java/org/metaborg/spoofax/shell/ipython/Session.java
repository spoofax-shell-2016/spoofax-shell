package org.metaborg.spoofax.shell.ipython;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import com.google.common.collect.Maps;

public class Session {
    private static final int CHANNELS = 4;

    private enum Channel {
        HEARTBEAT(ZMQ.REP, "hb_port", ZMQ.Poller.POLLIN),
        SHELL(ZMQ.ROUTER, "shell_port", ZMQ.Poller.POLLIN),
        CONTROL(ZMQ.ROUTER, "control_port", ZMQ.Poller.POLLIN),
        STDIN(ZMQ.ROUTER, "stdin_port", ZMQ.Poller.POLLIN),
        IOPUB(ZMQ.PUB, "iopub_port", -1);

        public int type;
        public String json_port_id;
        private int poller;

        private Channel(int type, String json_port_id, Integer poller) {
            this.type = type;
            this.json_port_id = json_port_id;
            this.poller = poller;
        }
    }

    ZContext ctx;
    ZMQ.Poller sockets;

    /**
     * {
     *   "control_port": 50160,
     *   "shell_port": 57503,
     *   "transport": "tcp",
     *   "signature_scheme": "hmac-sha256",
     *   "stdin_port": 52597,
     *   "hb_port": 42540,
     *   "ip": "127.0.0.1",
     *   "iopub_port": 40885,
     *   "key": "a0436f6c-1916-498b-8eb9-e81ab9368e84"
     * }
     */
    JSONObject connection;

    private HashMap<Integer, Socket> socketmap;

    public Session() {
        String ip = connection.getString("ip");
        String transport = connection.getString("transport");

        ctx = new ZContext();
        sockets = new ZMQ.Poller(CHANNELS);
        socketmap = Maps.newHashMap();

        for (Channel type : Channel.values()) {
            Socket socket = ctx.createSocket(type.type);
            String port = connection.getString(type.json_port_id);
            socket.bind(String.format("%s://%s:%s", transport, ip, port));

            if (type.poller < 0) {
                continue;
            }
            socketmap.put(sockets.register(socket, type.poller), socket);
        }
    }

    public void run() {
        while (true) {
            sockets.poll();
            for (Entry<Integer, Socket> entry : socketmap.entrySet()) {
                if (sockets.pollin(entry.getKey())) {
                    byte[] message = entry.getValue().recv(0);
                }
            }
        }
    }
}
