/*
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-11-30
 */
public class ClientController {
    static int PORT = 16000;

    Client client;
    Socket sock;
    DataInputStream in;
    DataOutputStream out;


    public ClientController(Client client) {
        this.client = client;
        sock = new Socket();
    }

    public Client getClient() {
        return client;
    }

    public ByteBuffer createLoginMessage() {
        ByteBuffer message = ByteBuffer.wrap(client.getUser().getBytes());
        Cryptographer.encrypt(message, client.getKey());
        return message;
    }

    public void encryptData(ByteBuffer buffer) {
        Cryptographer.encrypt(buffer, client.getKey());
    }

    public void decryptData(ByteBuffer buffer) {
        Cryptographer.decrypt(buffer, client.getKey());
    }

    public void connectSocket(String addr) throws IOException {
        sock.connect(new InetSocketAddress(addr, PORT));
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
    }

    public void sendMessage(ByteBuffer buffer) throws IOException {
        out.write(buffer.array());
    }

    public ByteBuffer recieveMessage(int size) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(size);
        in.readFully(buf.array());
        return buf;
    }

    public void close() throws IOException {
        sock.close();
    }

    private boolean compareBufferWithAck(ByteBuffer message) {
        return new String(message.asCharBuffer().array()).equals(Server.ACK);
    }

    public void processLine(String line) {
        ByteBuffer sending = ByteBuffer.wrap(line.getBytes());
        encryptData(sending);
        try {
            sendMessage(sending);
            ByteBuffer receiving = recieveMessage(2 * Long.BYTES);
            decryptData(receiving);
            if(compareBufferWithAck(receiving)) {
                if(line.equals("finished"))
                    return;
                receiving = recieveMessage(2 * Long.BYTES);
                decryptData(receiving);
                receiving = recieveMessage(2 * Long.BYTES * receiving.asIntBuffer().get());
                decryptData(receiving);
                new DataOutputStream(new FileOutputStream(line, false)).write(receiving.array());
            }
            else {
                System.err.println("File not found on server");
            }
        } catch (IOException e) {
            System.err.println("Unable to connect to server");
        }
    }

    public boolean login() throws IOException {
        ByteBuffer login = createLoginMessage();
        encryptData(login);
        sendMessage(login);

        login = recieveMessage(2 * Long.BYTES);
        decryptData(login);
        if(!compareBufferWithAck(login)) {
            System.err.println("Could not log in. Credentials were wrong.");
            return false;
        }
        return true;
    }
}
