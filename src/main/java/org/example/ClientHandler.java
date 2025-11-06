package org.example;

import java.io.*;
import java.net.*;

// Esta classe gerencia a comunicação com um cliente em uma Thread separada.
public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    // Este é o método que executa na nova thread
    public void run() {
        try {
            // Streams para ler e escrever para o cliente
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;

            // Loop de chat: lê do cliente e "ecoa" de volta
            while ((inputLine = inFromClient.readLine()) != null) {
                System.out.println("Cliente [" + clientSocket.getPort() + "] disse: " + inputLine);

                if (inputLine.equalsIgnoreCase("sair")) {
                    outToClient.println("Até logo!");
                    break;
                }

                // Ecoa a mensagem de volta para o cliente em maiúsculas
                outToClient.println("Servidor ecoou: " + inputLine.toUpperCase());
            }

            // Fecha a conexão com este cliente
            System.out.println("Cliente [" + clientSocket.getPort() + "] desconectou.");
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("Erro no ClientHandler: " + e.getMessage());
        }
    }
}
