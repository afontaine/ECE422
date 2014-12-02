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

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Andrew Fontaine
 * @version 1.0
 * @since 2014-12-01
 */
public class ServerService {

    private static String USAGE = "<file with user and keys> <number of threads>";


    ExecutorService service;
    ServerSocket serverSocket;
    Server server;

    public ServerService(Server server, int threads) throws IOException {
        this.server = server;
        service = Executors.newFixedThreadPool(threads);
        serverSocket = new ServerSocket(Server.PORT);
    }

    public void run() {
        while(true) {
            try {
                service.execute(new ServerController(serverSocket.accept(), server));
            } catch (IOException e) {
                service.shutdown();
            }
        }
    }

    public static void main(String[] args) {
        if(args.length != 2) {
            System.err.println(USAGE);
            System.exit(1);
        }
        int threads = 0;
        try {
            threads = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e) {
            System.err.println(USAGE);
            System.exit(1);
        }
        try {
            new ServerService(new Server(readFile(args[0])), threads).run();
        } catch (IOException e) {
            System.err.println("Could not open server file");
        }
    }

    private static HashMap<long[], String> readFile(String filename) throws IOException {
        HashMap<long[], String> users = new HashMap<>();
        String user;
        long[] key;
        for(String line : Files.readAllLines(Paths.get(filename))) {
            user = line.split(",")[0];
            key = Arrays.stream(line.split(",")).skip(1).mapToLong(Long::parseLong).toArray();
            if(key.length == 4) {
                users.put(key, user);
            }
            else {
                System.err.println(user + " not added");
            }
        }
        return users;
    }
}
