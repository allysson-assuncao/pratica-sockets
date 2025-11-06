package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            // 1. Cria um novo Socket, tentando se conectar ao IP "127.0.0.1"
            // (localhost, esta própria máquina) na porta 6013.
            // O SO inicia o "TCP handshake" (SYN, SYN-ACK, ACK).
            Socket sock = new Socket("127.0.0.1", 6013);

            // 2. Obtém o "canal de leitura" (entrada) do socket.
            // É por aqui que os dados *vindos do servidor* chegarão.
            InputStream in = sock.getInputStream();

            // 3. Cria um BufferedReader para ler dados de texto linha por linha
            // de forma eficiente.
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            String line;
            // 4. Lê uma linha do servidor. Fica bloqueado até o servidor
            // enviar uma linha (terminada com '\n') ou fechar a conexão.
            while ((line = bin.readLine()) != null)
                // 5. Imprime a linha recebida (a data) no console.
                System.out.println(line);

            // 6. Fecha a conexão do lado do cliente.
            sock.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
}
