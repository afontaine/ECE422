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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-11-30
 */
public class ClientView {

    BufferedReader readIn;
    ClientController controller;

    private String getLine() {
        System.out.print(controller.getClient().getUser() + "$ ");
        try {
            return readIn.readLine();
        } catch (IOException e) {
            System.out.println("An error occurred reading the command");
            return null;
        }
    }

    public ClientView(String user, long[] key, String addr) throws IOException {
        readIn = new BufferedReader(new InputStreamReader(System.in));
        controller = new ClientController(new Client(user, key));
        controller.connectSocket(addr);
    }

    public void run() {
        String line = "";
        while(!line.equals("finished")) {
            line = getLine();
            ByteBuffer sending = ByteBuffer.wrap(line.getBytes());
            controller.encryptData(sending);
            try {
                controller.sendMessage(sending);
                ByteBuffer receiving = controller.recieveMessage(3 * Character.BYTES);
                if(new String(receiving.asCharBuffer().array()).equals("ack")) {
                    if(line.equals("finished"))
                        continue;
                    Path receivingFile = Paths.get(line);
                    receiving = controller.recieveMessage(2*Long.BYTES);
                    controller.decryptData(receiving);
                    Files.write(receivingFile, receiving.array(), StandardOpenOption.APPEND);
                }
                else {
                    System.err.println("File not found on server");
                }
            } catch (IOException e) {
                System.err.println("Unable to connect to server");
            }

        }
        try {
            controller.close();
        } catch (IOException e) {
            System.err.println("Could not close connection");
        }
    }
}
