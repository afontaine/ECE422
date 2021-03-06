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

import java.io.*;
import java.util.Arrays;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-11-30
 */
public class ClientView {

    static private String USAGE = "<IP of server> <username> <key>";

    private BufferedReader readIn;
    private ClientController controller;

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
        try {
            if (!controller.login()) {
                System.out.println("User not found");
                return;
            }
        } catch (IOException e) {
            System.err.println("Could not log in");
            return;
        }
        String line = "";
        while(!line.equals("finished")) {
            line = "";
            while(line.isEmpty())
                line = getLine();
            controller.processLine(line);

        }
        try {
            controller.close();
        } catch (IOException e) {
            System.err.println("Could not close connection");
        }
    }

    public static void main(String[] args) {
        if(args.length < 3) {
            System.err.println(USAGE);
            System.exit(1);
        }
        long[] key = Arrays.stream(args[2].split(",")).mapToLong(Long::parseLong).toArray();
        try {
            ClientView view = new ClientView(args[1], key, args[0]);
            view.run();
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Cannot connect to server.");
            System.exit(1);
        }

    }
}
