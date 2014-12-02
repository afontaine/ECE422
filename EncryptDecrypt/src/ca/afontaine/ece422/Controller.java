package ca.afontaine.ece422;/*
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-12-01
 */
public class Controller {


    protected Client client;
    protected Socket sock;
    protected DataInputStream in;
    protected DataOutputStream out;

    public Controller() {

    }

    public Controller(Client client) {
        this.client = client;
        sock = new Socket();
    }

    public Client getClient() {
        return client;
    }

    protected void setClient(Client client) {
        this.client = client;
    }

    protected Socket getSock() {
        return sock;
    }

    protected void setSock(Socket sock) {
        this.sock = sock;
    }

    protected void setIn(DataInputStream in) {
        this.in = in;
    }

    protected void setOut(DataOutputStream out) {
        this.out = out;
    }

    protected ByteBuffer encryptData(ByteBuffer buffer) {
        return Cryptographer.encrypt(buffer, client.getKey());
    }

    protected ByteBuffer decryptData(ByteBuffer buffer) {
        return Cryptographer.decrypt(buffer, client.getKey());
    }

    protected void sendMessage(ByteBuffer buffer) throws IOException {
        out.write(buffer.array());
    }

    protected ByteBuffer receiveMessage(int size) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(size);
        in.readFully(buf.array());
        return buf;
    }

    public void close() throws IOException {
        sock.close();
    }
}
