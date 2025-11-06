package org.example;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSock = new ServerSocket(6013);
            System.out.println("Servidor de Chat aguardando conexão na porta 6013...");

            // Servidor de chat simples: só aceita um cliente
            Socket client = serverSock.accept();
            System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());

            // Streams para ler e escrever para o cliente
            BufferedReader ClientBufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter saidaCliente = new PrintWriter(client.getOutputStream(), true);

            // Stream para ler do console (do "operador" do servidor)
            Scanner entradaServidor = new Scanner(System.in);

            String inputLine, outputLine;

            // Loop de chat
            while (true) {
                // 1. Lê a mensagem do cliente
                inputLine = ClientBufferedReader.readLine();
                if ((inputLine) != null) {
                    System.out.println("CLIENTE: " + inputLine);
                    if (inputLine.equalsIgnoreCase("sair")) {
                        break;
                    }
                }

                // 2. Lê a mensagem do console do servidor e envia ao cliente
                System.out.print("SERVIDOR: ");
                outputLine = entradaServidor.nextLine();
                saidaCliente.println(outputLine);
                if (outputLine.equalsIgnoreCase("sair")) {
                    break;
                }
            }

            // Fecha tudo
            System.out.println("Fechando conexão.");
            client.close();
            serverSock.close();
            entradaServidor.close();

        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
