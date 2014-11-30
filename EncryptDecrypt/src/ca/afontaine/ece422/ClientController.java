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
import java.io.IOException;
import java.net.InetAddress;
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
    Cryptographer crypt;
    Socket sock;
    DataInputStream in;
    DataOutputStream out;


    public ClientController(Client client) {
        this.client = client;
        crypt = new Cryptographer(client.getKey());
        sock = new Socket();
    }

    public ByteBuffer createLoginMessage() {
        ByteBuffer message = ByteBuffer.wrap(client.getUser().getBytes());
        crypt.encryptMessage(message.asLongBuffer().array(), client.getKey());
        return message;
    }

    public void encryptData(ByteBuffer buffer) {
        crypt.encryptMessage(buffer.asLongBuffer().array(), client.getKey());
    }

    public void decryptData(ByteBuffer buffer) {
        crypt.decryptMessage(buffer.asLongBuffer().array(), client.getKey());
    }

    public void connectSocket(String addr) throws IOException {
        sock.connect(new InetSocketAddress(addr, PORT));
        in = new DataInputStream(sock.getInputStream());
        out = new DataOutputStream(sock.getOutputStream());
    }

    public void sendMessage(ByteBuffer buffer) throws IOException {
        out.write(buffer.array());
    }

    public ByteBuffer recieveMessage() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(2 * Long.BYTES);
        in.read(buf.array());
        return buf;
    }
}
