package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
	public static void main(String[] args)  {
		try {
			// poderia ser alterado para um nome ou endereÁo IP que n„o fosse o do host local
			Socket sock = new Socket("127.0.0.1",6013);
			InputStream in = sock.getInputStream();
			BufferedReader bin = new BufferedReader(new InputStreamReader(in));

			String line;
			while( (line = bin.readLine()) != null)
				System.out.println(line);

			PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);

			Scanner entrada = new Scanner(System.in);
			System.out.println("Digite uma mensagem para ser enviada ao servidor: ");
			String mensagem = entrada.nextLine();

			pout.println(mensagem);

			sock.close();
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
	}
}
