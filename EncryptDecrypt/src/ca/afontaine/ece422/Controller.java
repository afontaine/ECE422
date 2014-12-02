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


    Client client;
    Socket sock;
    DataInputStream in;
    DataOutputStream out;

    public Controller() {

    }

    public Controller(Client client) {
        this.client = client;
        sock = new Socket();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Socket getSock() {
        return sock;
    }

    public void setSock(Socket sock) {
        this.sock = sock;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public ByteBuffer encryptData(ByteBuffer buffer) {
        return Cryptographer.encrypt(buffer, client.getKey());
    }

    public ByteBuffer decryptData(ByteBuffer buffer) {
        return Cryptographer.decrypt(buffer, client.getKey());
    }

    public void sendMessage(ByteBuffer buffer) throws IOException {
        System.out.println("Sending " + buffer.limit() + " bytes");
        out.write(buffer.array());
        System.out.println(buffer.limit() + " bytes sent");
    }

    public ByteBuffer receiveMessage(int size) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(size);
        System.out.println("Receiving " + size + " bytes");
        in.readFully(buf.array());
        System.out.println(size + " bytes received");
        return buf;
    }

    public void close() throws IOException {
        sock.close();
    }
}
