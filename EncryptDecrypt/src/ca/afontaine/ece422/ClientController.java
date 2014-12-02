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
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-11-30
 */
public class ClientController extends Controller {


    public ClientController(Client client) {
        super(client);
    }

    public ByteBuffer createLoginMessage() {
        ByteBuffer message = ByteBuffer.wrap(getClient().getUser().getBytes());
        return encryptData(message);
    }

    public void connectSocket(String addr) throws IOException {
        getSock().connect(new InetSocketAddress(addr, Server.PORT));
        setIn(new DataInputStream(getSock().getInputStream()));
        setOut(new DataOutputStream(getSock().getOutputStream()));
    }

    private boolean compareBufferWithAck(ByteBuffer message) {
        return new String(message.array()).equals(Server.ACK);
    }

    public void processLine(String line) {
        ByteBuffer size = ByteBuffer.allocate(Integer.BYTES);
        ByteBuffer sending = ByteBuffer.wrap(line.getBytes());
        sending = encryptData(sending);
        size.putInt(sending.limit() / Long.BYTES);
        size = encryptData(size);
        try {
            sendMessage(size);
            sendMessage(sending);
            ByteBuffer receiving = receiveMessage(2 * Long.BYTES);
            receiving = decryptData(receiving);
            if(compareBufferWithAck(receiving)) {
                if(line.equals("finished"))
                    return;
                receiving = receiveMessage(2 * Long.BYTES);
                receiving = decryptData(receiving);
                receiving = receiveMessage(Long.BYTES * receiving.getInt());
                receiving = decryptData(receiving);
                Files.write(Paths.get(new File(line).getName()), receiving.array());
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
        sendMessage(login);

        login = receiveMessage(2 * Long.BYTES);
        login = decryptData(login);
        if(!compareBufferWithAck(login)) {
            System.err.println("Could not log in. Credentials were wrong.");
            return false;
        }
        return true;
    }
}
