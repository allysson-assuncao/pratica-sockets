# Projeto de Sockets TCP em Java

Este projeto demonstra a comunicação cliente-servidor usando Sockets TCP em Java, explorando como o Sistema Operacional gerencia as conexões. O projeto é dividido em três estágios principais, baseados em um roteiro de aula.

## Objetivo

Compreender na prática como o Sistema Operacional gerencia a comunicação entre processos utilizando sockets TCP, através da construção de uma aplicação cliente-servidor em Java.

## Estrutura do Projeto

O repositório contém diferentes versões do cliente e do servidor, correspondendo às etapas do roteiro:

1.  **Versão Inicial (`Client.java`, `Server.java`):**
    * `Server.java`: Um servidor simples que aguarda uma conexão, envia a data e hora atuais e fecha a conexão.
    * `Client.java`: Um cliente que se conecta ao servidor, lê a mensagem (data e hora) e a imprime no console.

2.  **Versão Chat (`ChatClient.java`, `ChatServer.java`):**
    * Uma evolução da versão inicial onde cliente e servidor podem trocar múltiplas mensagens, simulando um bate-papo.
    * Ambos os lados podem enviar e receber mensagens.

3.  **Versão Multi-Cliente (`MultiThreadedServer.java`, `ClientHandler.java`):**
    * `MultiThreadedServer.java`: A versão final do servidor, capaz de aceitar múltiplas conexões de clientes simultaneamente.
    * `ClientHandler.java`: Uma classe auxiliar que usa *Threads*. Cada instância desta classe é responsável por gerenciar a comunicação (o bate-papo) com um único cliente, permitindo que o servidor principal volte a aguardar por novas conexões.
    * O `ChatClient.java` da Etapa 2 pode ser usado para se conectar a este servidor.
    
### Etapa 1: Executar o código

Ao compilar (`javac Server.java`, `javac Client.java`) e executar os códigos originais fornecidos:

1.  **Terminal 1:** `java Server`
    * O servidor inicia e o terminal fica "parado". O programa está bloqueado na linha `Socket client = sock.accept();`, aguardando uma conexão na porta 6013.
2.  **Terminal 2:** `java Client`
    * O cliente se conecta ao servidor.
    * O servidor (no Terminal 1) detecta a conexão, envia a data e hora atuais (ex: `Thu Nov 06 07:04:06 BRT 2025`), fecha a conexão com esse cliente e *imediatamente* volta a esperar na linha `sock.accept();`.
    * O cliente (no Terminal 2) recebe a string da data, a imprime no console e encerra sua execução.

### Etapa 2: Comando `netstat`

O comando `netstat -an | grep "6013"` é usado para inspecionar o estado das conexões de rede no seu computador.

* `netstat`: Ferramenta de estatísticas de rede.
* `-a`: (all) Mostra todos os sockets, incluindo os que estão apenas "escutando" (LISTEN).
* `-n`: (numeric) Mostra endereços IP e números de porta em formato numérico, em vez de tentar resolvê-los para nomes (ex: mostra `127.0.0.1` em vez de `localhost`).
* `| grep "6013"`: Filtra a saída para mostrar apenas as linhas que contêm "6013".

**Resultados e Explicação:**

1.  **Com o `Server.java` rodando (antes do cliente conectar):**
    ```bash
    $ netstat -an | grep "6013"
    tcp6       0      0 :::6013                 :::* LISTEN
    ```
    * **Explicação:** Isso mostra que o Sistema Operacional abriu a porta 6013 e o processo do `Server.java` está no estado **LISTEN** (Escutando). O `:::` indica que ele está escutando em todos os endereços IPv6 (que, em muitos sistemas, também inclui IPv4). O SO está gerenciando ativamente esta porta, pronto para receber tentativas de conexão.

2.  **Com o `Client.java` conectado (exatamente no momento da troca de dados):**
    * Ao executar o comando no breve momento em que a conexão está ativa:
    ```bash
    $ netstat -an | grep "6013"
    tcp6       0      0 :::6013                 :::* LISTEN
    tcp6       0      0 ::1:6013                ::1:51234               ESTABLISHED
    tcp6       0      0 ::1:51234               ::1:6013                ESTABLISHED
    ```
    * **Explicação:** Além da linha `LISTEN` (o servidor principal ainda escuta), aparecem duas novas linhas:
        * A primeira `ESTABLISHED` é a conexão do *ponto de vista do servidor* (porta 6013) conectado a um cliente em uma porta alta e aleatória (ex: 51234).
        * A segunda `ESTABLISHED` é a conexão do *ponto de vista do cliente* (porta 51234) conectado ao servidor na porta 6013.
    * **Conclusão:** Isso prova que o **Sistema Operacional mantém uma tabela de conexões**, rastreando o estado de cada socket (LISTEN, ESTABLISHED, TIME_WAIT, etc.), os endereços IP e as portas de origem e destino.

### Etapa 3: Explicação Linha a Linha

#### `Server.java` (Original)

```java
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
```

```java
public class Client {
    public static void main(String[] args) {
        try {
            // 1. Cria um novo Socket, tentando se conectar ao IP "127.0.0.1"
            // (localhost) na porta 6013.
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
