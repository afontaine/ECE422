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
 * @since 2014-11-24
 */
public class Cryptographer {

    static {
        System.loadLibrary("Cryptographer");
    }

    static public ByteBuffer encrypt(ByteBuffer value, long[] key) {
        ByteBuffer newBuffer = ByteBuffer.allocate(value.limit()
                + Integer.BYTES + ((2 * Long.BYTES) - ((value.limit() + Integer.BYTES) % (2 * Long.BYTES))));
        for(int i = 0; i < newBuffer.limit(); i++)
            newBuffer.put((byte) 0);
        newBuffer.position(0);
        newBuffer.putInt(value.limit());
        newBuffer.put(value.array());
        value = newBuffer;
        encryptMessage(value.array(), key);
        return value;
    }

    static public ByteBuffer decrypt(ByteBuffer value, long[] key) {
        ByteBuffer newBuffer = ByteBuffer.allocate(value.limit());
        newBuffer.put(value.array());
        value = newBuffer;
        decryptMessage(value.array(), key);
        value.position(0);
        int size = value.getInt();
        ByteBuffer message = ByteBuffer.allocate(size);
        message.put(value.array(), Integer.BYTES, size);
        message.position(0);
        return message;

    }

    native static private void encryptMessage(byte[] value, long[] key);
    native static private void decryptMessage(byte[] value, long[] key);

}
