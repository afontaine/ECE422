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

import java.nio.ByteBuffer;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-11-30
 */
public class ClientController {

    Client client;
    Cryptographer crypt;


    public ClientController(Client client) {
        this.client = client;
        crypt = new Cryptographer(client.getKey());
    }

    public ByteBuffer createLoginMessage() {
        long[] message = ByteBuffer.wrap(client.getUser().getBytes()).asLongBuffer().array();
        crypt.encryptMessage(message, client.getKey());
        ByteBuffer out = ByteBuffer.allocate(message.length * 4);
        out.asLongBuffer().put(message);
        return out;
    }

    public ByteBuffer encryptData(ByteBuffer buffer) {
        long[] message = buffer.asLongBuffer().array();
        crypt.encryptMessage(message, client.getKey());
        ByteBuffer out = ByteBuffer.allocate(message.length * 4);
        out.asLongBuffer().put(message);
        return out;
    }

    public ByteBuffer decryptData(ByteBuffer buffer) {
        long[] message = buffer.asLongBuffer().array();
        crypt.decryptMessage(message, client.getKey());
        ByteBuffer out = ByteBuffer.allocate(message.length * 4);
        out.asLongBuffer().put(message);
        return out;
    }
}
