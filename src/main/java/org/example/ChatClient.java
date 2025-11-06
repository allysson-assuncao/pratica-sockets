package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket sock = new Socket("127.0.0.1", 6013);
            System.out.println("Conectado ao servidor de chat. Digite 'sair' para terminar.");

            // Streams para ler e escrever para o servidor
            BufferedReader ServerBufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter saidaServidor = new PrintWriter(sock.getOutputStream(), true);

            // Stream para ler do console do usuário
            Scanner entradaServidor = new Scanner(System.in);

            String inputLine, outputLine;

            // Loop de chat
            while (true) {
                // 1. Lê a mensagem do console e envia ao servidor
                System.out.print("VOCÊ: ");
                outputLine = entradaServidor.nextLine();
                saidaServidor.println(outputLine);
                if (outputLine.equalsIgnoreCase("sair")) {
                    break;
                }

                // 2. Lê a resposta do servidor
                inputLine = ServerBufferedReader.readLine();
                if ((inputLine) != null) {
                    System.out.println("SERVIDOR: " + inputLine);
                    if (inputLine.equalsIgnoreCase("sair")) {
                        break;
                    }
                }
            }

            // Fecha tudo
            System.out.println("Desconectando.");
            sock.close();
            entradaServidor.close();

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
