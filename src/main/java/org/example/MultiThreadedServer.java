package org.example;

import java.io.*;
import java.net.*;

public class MultiThreadedServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSock = new ServerSocket(6013);
            System.out.println("Servidor Multi-Cliente aguardando conexões na porta 6013...");

            // Loop infinito para aceitar conexões
            while (true) {
                // 1. Aguarda um novo cliente
                Socket clientSocket = serverSock.accept();
                System.out.println("Novo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                // 2. Cria uma nova Thread (ClientHandler) para cuidar deste cliente
                ClientHandler clientThread = new ClientHandler(clientSocket);

                // 3. Inicia a thread. O método run() do ClientHandler começará a executar em paralelo.
                clientThread.start();

                // 4. O loop 'while' continua imediatamente, voltando ao passo 1 (accept)
                // para esperar o próximo cliente, sem esperar o cliente atual terminar.
            }
        } catch (IOException ioe) {
            System.err.println("Erro no servidor principal: " + ioe.getMessage());
        }
    }
}
