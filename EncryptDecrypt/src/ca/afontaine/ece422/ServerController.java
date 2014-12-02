/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Andrew Fontaine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.afontaine.ece422;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-12-01
 */
public class ServerController extends Controller implements Runnable {

    Server server;

    public ServerController(Socket sock, Server server) {
        super();
        setSock(sock);
        this.server = server;
    }

    private void setStreams() throws IOException {
        setIn(new DataInputStream(getSock().getInputStream()));
        setOut(new DataOutputStream(getSock().getOutputStream()));
    }

    @Override
    public void run() {
        try {
            setStreams();
        } catch (IOException e) {
            System.err.println("Unable to connect to client");
            return;
        }
        try {
            ByteBuffer message = receiveMessage(2 * Long.BYTES);
            for(long[] key : server.getUsers().keySet()) {
                if(server.getUsers().containsValue(new String(decryptData(message).array()))) {
                    sendMessage(encryptData(ByteBuffer.wrap(Server.ACK.getBytes())));
                    setClient(new Client(server.getUsers().get(key), key));
                    processCommands();
                    break;
                }
            }
            sendMessage(ByteBuffer.wrap("User not found".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCommands() {
        String line = "";
        while(line.equals("finished")) {
            try {
                ByteBuffer size = receiveMessage(2 * Long.BYTES);
                size = decryptData(size);
                ByteBuffer message = receiveMessage(size.getInt());
                message = decryptData(message);
                String filename = new String(message.array());
                if(filename.equals("finished")) {
                    System.out.println("Connection terminated");
                    continue;
                }
                System.out.println("Requesting " + filename);
                ByteBuffer returnMessage = ByteBuffer.wrap(Files.readAllBytes(Paths.get(filename)));
                returnMessage = encryptData(returnMessage);
                sendMessage(returnMessage);

            } catch (IOException e) {
                System.err.println("Something went horribly wrong");
            }
        }
    }
}
