package com.aisvalidator.publisher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UDPService {
    private DatagramSocket socket;
    private InetAddress address;
    @Autowired
    private Environment env;
    private byte[] buf;

    public UDPService() {}

    public void sendRawNMEAMessage(List<String> rawMessage) throws SocketException, UnknownHostException {
        Boolean sendingEnabled = Optional.ofNullable(env.getProperty("udp.enabled", Boolean.class)).orElse(true);
        if (sendingEnabled) {
            try {
                if (socket == null || address == null) {
                    socket = new DatagramSocket();
                    address = InetAddress.getByName(env.getProperty("udp.host"));
                }
                rawMessage.forEach(s -> {
                    buf = s.getBytes();
                    DatagramPacket packet
                            = new DatagramPacket(buf, buf.length, address, env.getProperty("udp.port", Integer.class));
                    try {
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                log.debug("Exception sending AIS track.");
            }
        }
    }

    public void close() {
        socket.close();
    }
}
