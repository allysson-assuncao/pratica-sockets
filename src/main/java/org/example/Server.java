package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try {
            // 1. Cria um ServerSocket que "escuta" na porta 6013.
            // O SO reserva esta porta para este processo.
            ServerSocket sock = new ServerSocket(6013);

            // 2. Inicia um loop infinito para manter o servidor sempre no ar.
            while (true) {
                // 3. Ponto CRÍTICO: é uma chamada bloqueante.
                // O programa para aqui e o SO assume, esperando um cliente.
                // Quando um cliente conecta, o SO cria um *novo* Socket
                // para essa conexão específica, e o método retorna.
                Socket client = sock.accept();

                // 4. Obtém o "canal de escrita" (saída) para o cliente.
                // O 'true' (autoFlush) garante que as mensagens sejam enviadas
                // imediatamente após um println.
                PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

                // 5. Envia a data e hora atuais como uma String para o cliente.
                pout.println(new java.util.Date().toString());

                // 6. Fecha a conexão *apenas* com este cliente específico.
                client.close();

                // 7. O loop 'while' repete, e o servidor volta para a linha 3 (accept),
                // esperando o *próximo* cliente.
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
