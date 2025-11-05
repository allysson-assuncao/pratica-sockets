package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket sock = new ServerSocket(6013);

            // agora escuta conexıes
            while (true) {
                Socket client = sock.accept();
                // temos uma conexão

                PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

                Scanner entrada = new Scanner(System.in);
                System.out.println("Digite uma mensagem para ser enviada ao cliente: ");
                String mensagem = entrada.nextLine();

                pout.println(mensagem);

                // fecha o socket e volta a escutar conexıes
                client.close();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
