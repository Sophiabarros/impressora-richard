import com.sun.jna.Library;
import com.sun.jna.Native;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Sistema de integração Java + Impressora Elgin (via DLL / JNA)
 * Este programa oferece um menu interativo para que o usuário execute
 * todas as funções principais da impressora térmica.
 *
 * Feito para fins educacionais.
 */
public class Main {

    /**
     * Interface JNA para carregar a DLL da Elgin.
     * Aqui mapeamos cada função da DLL para ser usada em Java.
     */
    public interface ImpressoraDLL extends Library {

        // Carregamento da DLL via caminho absoluto
        ImpressoraDLL INSTANCE = Native.load(
                "C:\\Users\\fernandes_igor\\Downloads\\Java-Aluno EM\\Java-Aluno EM\\Java-Aluno EM\\E1_Impressora01.dll",
                ImpressoraDLL.class
        );

        // Funções fornecidas pela DLL
        int AbreConexaoImpressora(int tipo, String modelo, String conexao, int param);
        int FechaConexaoImpressora();
        int ImpressaoTexto(String dados, int posicao, int estilo, int tamanho);
        int Corte(int avanco);
        int ImpressaoQRCode(String dados, int tamanho, int nivelCorrecao);
        int ImpressaoCodigoBarras(int tipo, String dados, int altura, int largura, int HRI);
        int AvancaPapel(int linhas);
        int StatusImpressora(int param);
        int AbreGavetaElgin();
        int AbreGaveta(int pino, int ti, int tf);
        int SinalSonoro(int qtd, int tempoInicio, int tempoFim);
        int ModoPagina();
        int LimpaBufferModoPagina();
        int ImprimeModoPagina();
        int ModoPadrao();
        int PosicaoImpressaoHorizontal(int posicao);
        int PosicaoImpressaoVertical(int posicao);
        int ImprimeXMLSAT(String dados, int param);
        int ImprimeXMLCancelamentoSAT(String dados, String assQRCode, int param);
    }

    // Variáveis de estado da aplicação
    private static boolean conexaoAberta = false;
    private static int tipo = 1;
    private static String modelo = "";
    private static String conexao = "";
    private static int parametro = 0;

    // Scanner para capturar entrada do usuário
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Função utilitária para capturar texto do usuário.
     */
    private static String capturarEntrada(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    /**
     * Configura parâmetros de conexão com a impressora.
     */
    public static void configurarConexao() {
        if (!conexaoAberta) {

            System.out.println("Digite o tipo de conexão (ex: 1 para USB, 2 para serial, etc.): ");
            try {
                tipo = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Valor inválido. Mantendo tipo padrão.");
            }

            System.out.print("Digite o modelo (ex: modelo da impressora): ");
            modelo = scanner.nextLine();

            System.out.print("Digite a string de conexão (ex: porta COM, IP:porta ou caminho): ");
            conexao = scanner.nextLine();

            System.out.print("Digite um parâmetro inteiro (opcional): ");
            try {
                parametro = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                parametro = 0;
            }

            System.out.println("Configuração salva.");
        } else {
            System.out.println("Conexão já aberta. Feche para reconfigurar.");
        }
    }

    /**
     * Abre a conexão com a impressora usando os dados configurados.
     */
    public static void abrirConexao() {
        if (!conexaoAberta) {

            int retorno = ImpressoraDLL.INSTANCE.AbreConexaoImpressora(tipo, modelo, conexao, parametro);

            if (retorno == 0) {
                conexaoAberta = true;
                System.out.println("Conexão aberta com sucesso.");
            } else {
                System.out.println("Erro ao abrir conexão. Código de erro: " + retorno);
            }

        } else {
            System.out.println("Conexão já está aberta.");
        }
    }

    /**
     * Fecha a conexão com a impressora.
     */
    public static void fecharConexao() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int retorno = ImpressoraDLL.INSTANCE.FechaConexaoImpressora();
        if (retorno == 0) {
            conexaoAberta = false;
            System.out.println("Conexão fechada com sucesso.");
        } else {
            System.out.println("Erro ao fechar conexão. Código: " + retorno);
        }
    }

    /**
     * Imprime texto simples na impressora.
     */
    public static void ImpressaoTexto() {
        if (!conexaoAberta) {
            System.out.println("Erro: não há conexao aberta com a impressora.");
            return;
        }

        String dados = capturarEntrada("Digite o texto que deseja imprimir: ");
        int posicao = Integer.parseInt(capturarEntrada("Posição (0 Esq, 1 Centro, 2 Dir): "));
        int estilo = Integer.parseInt(capturarEntrada("Estilo (0 normal, 8 negrito etc.): "));
        int tamanho = Integer.parseInt(capturarEntrada("Tamanho (0 normal): "));

        int retorno = ImpressoraDLL.INSTANCE.ImpressaoTexto(dados, posicao, estilo, tamanho);

        if (retorno >= 0) System.out.println("Texto enviado com sucesso!");
        else System.out.println("Erro ao enviar texto. Código: " + retorno);
    }

    /**
     * Realiza o corte do papel.
     */
    public static void Corte() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int retorno = ImpressoraDLL.INSTANCE.Corte(2);

        if (retorno == 0) System.out.println("Corte realizado com sucesso.");
        else System.out.println("Erro ao realizar corte. Código: " + retorno);
    }

    /**
     * Imprime um QR Code.
     */
    public static void ImpressaoQRCode() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        String dados = capturarEntrada("Digite os dados do QRCode: ");
        int tamanho = Integer.parseInt(capturarEntrada("Tamanho (1-6): "));
        int nivel = Integer.parseInt(capturarEntrada("Nível de correção (1-4): "));

        int retorno = ImpressoraDLL.INSTANCE.ImpressaoQRCode(dados, tamanho, nivel);

        if (retorno >= 0) System.out.println("QRCode enviado com sucesso.");
        else System.out.println("Erro ao enviar QRCode. Código: " + retorno);
    }

    /**
     * Imprime código de barras.
     */
    public static void ImpressaoCodigoBarras() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int tipo = Integer.parseInt(capturarEntrada("Tipo de código (0..8): "));
        String dados = capturarEntrada("Dados do código: ");
        int altura = Integer.parseInt(capturarEntrada("Altura (1-255): "));
        int largura = Integer.parseInt(capturarEntrada("Largura (1-6): "));
        int hri = Integer.parseInt(capturarEntrada("HRI (1 acima, 2 abaixo, 4 não imprimir): "));

        int retorno = ImpressoraDLL.INSTANCE.ImpressaoCodigoBarras(tipo, dados, altura, largura, hri);

        if (retorno == 0) System.out.println("Código de barras impresso!");
        else System.out.println("Erro ao imprimir código. Código: " + retorno);
    }

    /**
     * Avança papel em quantidade de linhas.
     */
    public static void AvancaPapel() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int linhas = Integer.parseInt(capturarEntrada("Linhas para avançar: "));

        int retorno = ImpressoraDLL.INSTANCE.AvancaPapel(linhas);

        if (retorno >= 0) System.out.println("Papel avançado.");
        else System.out.println("Erro ao avançar papel. Código: " + retorno);
    }

    /**
     * Abre gaveta padrão Elgin.
     */
    public static void AbreGavetaElgin() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int retorno = ImpressoraDLL.INSTANCE.AbreGavetaElgin();

        if (retorno == 0) System.out.println("Gaveta aberta com sucesso.");
        else System.out.println("Erro ao abrir gaveta. Código: " + retorno);
    }

    /**
     * Abre gaveta com parâmetros personalizados.
     */
    public static void AbreGaveta() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int pino = Integer.parseInt(capturarEntrada("Pino (0/1): "));
        int ti = Integer.parseInt(capturarEntrada("Tempo inicial (1-255): "));
        int tf = Integer.parseInt(capturarEntrada("Tempo final (1-255): "));

        int retorno = ImpressoraDLL.INSTANCE.AbreGaveta(pino, ti, tf);

        if (retorno == 0) System.out.println("Gaveta aberta.");
        else System.out.println("Erro ao abrir gaveta. Código: " + retorno);
    }

    /**
     * Envia sinal sonoro ("bip") para a impressora.
     */
    public static void SinalSonoro() {
        if (!conexaoAberta) {
            System.out.println("Não há conexão aberta.");
            return;
        }

        int qtd = Integer.parseInt(capturarEntrada("Quantidade (1-63): "));
        int inicio = Integer.parseInt(capturarEntrada("Tempo início (1-25): "));
        int fim = Integer.parseInt(capturarEntrada("Tempo fim (1-25): "));

        int retorno = ImpressoraDLL.INSTANCE.SinalSonoro(qtd, inicio, fim);

        if (retorno == 0) System.out.println("Sinal sonoro enviado.");
        else System.out.println("Erro ao enviar sinal. Código: " + retorno);
    }

    /**
     * Lê arquivo XML e retorna como string.
     */
    private static String lerArquivoComoString(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        byte[] data = fis.readAllBytes();
        fis.close();
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * Abre um FileChooser para impressão de XML SAT.
     */
    public static int imprimeXMLSAT() {
        if (!conexaoAberta) {
            System.out.println("Erro: Conexão não está aberta!");
        } else {
            System.out.println("Imprimindo XML SAT...");

            int tentativas = 0;
            int retorno = -1;

            do {
                retorno = ImpressoraDLL.INSTANCE.ImprimeXMLSAT("Teste de impressão XML SAT", 0);
                tentativas++;
            } while (retorno != 0 && tentativas < 3);

            if (retorno == 0) {
                System.out.println("Impressão XML SAT concluída com sucesso!");
                ImpressoraDLL.INSTANCE.Corte(5);
            } else {
                System.out.println("Erro ao imprimir XML SAT após " + tentativas + " tentativas.");
            }
        }
        return 0;
    }

    public static int imprimeXMLCancelamentoSAT () {
        if (!conexaoAberta) {
            System.out.println("Erro: Conexão não está aberta!");
        } else {
            System.out.println("Iniciando impressão de cancelamento SAT...");
            String assQRCode = "Q5DLkpdRijIRGY6YSSNsTWK1TztHL1vD0V1Jc4spo/CEUqICEb9SFy82ym8EhBRZjbh3btsZhF+sjHqEMR159i4agru9x6KsepK/q0E2e5xlU5cv3m1woYfgHyOkWDNcSdMsS6bBh2Bpq6s89yJ9Q6qh/J8YHi306ce9Tqb/drKvN2XdE5noRSS32TAWuaQEVd7u+TrvXlOQsE3fHR1D5f1saUwQLPSdIv01NF6Ny7jZwjCwv1uNDgGZONJdlTJ6p0ccqnZvuE70aHOI09elpjEO6Cd+orI7XHHrFCwhFhAcbalc+ZfO5b/+vkyAHS6CYVFCDtYR9Hi5qgdk31v23w==";

            int retorno = -1;
            int tentativas = 0;

            while (tentativas < 3) {
                retorno = ImpressoraDLL.INSTANCE.ImprimeXMLCancelamentoSAT("Cancelamento teste", assQRCode, 0);
                if (retorno == 0) {
                    System.out.println("Impressão de cancelamento realizada com sucesso!");
                    ImpressoraDLL.INSTANCE.Corte(5);
                    break;
                } else {
                    System.out.println("Tentativa " + (tentativas + 1) + " falhou. Código: " + retorno);
                }
                tentativas++;
            }

            if (retorno != 0) {
                System.out.println("Erro: não foi possível imprimir o cancelamento após 3 tentativas.");
            }
        }
        return 0;
    }

    /**
     * Método principal — menu do programa.
     */
    public static void main(String[] args) {

        while (true) {

            System.out.println("\n**************** MENU IMPRESSORA *******************");
            System.out.println("1  - Configurar Conexao");
            System.out.println("2  - Abrir Conexao");
            System.out.println("3  - Impressao Texto");
            System.out.println("4  - Impressao QRCode");
            System.out.println("5  - Impressao Cod Barras");
            System.out.println("6  - Impressao XML SAT");
            System.out.println("7  - Impressao XML Cancelamento SAT");
            System.out.println("8  - Abrir Gaveta Elgin");
            System.out.println("9  - Abrir Gaveta");
            System.out.println("10 - Sinal Sonoro");
            System.out.println("11 - Avancar Papel");
            System.out.println("12 - Corte");
            System.out.println("0  - Sair");

            String escolha = capturarEntrada("Digite a opção desejada: ");

            switch (escolha) {
                case "1": configurarConexao(); break;
                case "2": abrirConexao(); break;
                case "3": ImpressaoTexto(); break;
                case "4": ImpressaoQRCode(); break;
                case "5": ImpressaoCodigoBarras(); break;
                case "6": imprimeXMLSAT(); break;
                case "7": imprimeXMLCancelamentoSAT(); break;
                case "8": AbreGavetaElgin(); break;
                case "9": AbreGaveta(); break;
                case "10": SinalSonoro(); break;
                case "11": AvancaPapel(); break;
                case "12": Corte(); break;

                case "0":
                    fecharConexao();
                    System.out.println("Programa encerrado.");
                    scanner.close();
                    return;

                default:
                    System.out.println("OPÇÃO INVÁLIDA");
            }
        }
    }
}