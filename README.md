**README – Sistema de Impressão Elgin (Java + JNA)**

Componentes do grupo: César, Igor, Sophia Barros, Gustavo e Lucas Vieira

**Descrição**

Este projeto implementa um sistema de comunicação com impressoras térmicas Elgin utilizando Java e a biblioteca JNA para acessar as funções da DLL E1_Impressora01.dll.

O programa funciona via console e oferece um menu interativo com as principais operações de impressão.

**Funcionalidades**

•	Abrir e fechar conexão

•	Imprimir texto com estilos e alinhamentos

•	Imprimir QR Code

•	Imprimir código de barras

•	Imprimir XML SAT

•	Imprimir XML de cancelamento SAT

•	Emitir sinal sonoro

•	Abrir gaveta padrão ou personalizada

•	Avançar papel

•	Executar corte total ou parcial

**Estrutura**

Projeto/

├── Main.java

├── E1_Impressora01.dll

└── README.md

**Tecnologias**

•	Java 8 ou superior

•	JNA

•	DLL oficial Elgin

•	Swing (para seleção de arquivos XML)

**Como Executar**

1.	Ajuste o caminho da DLL no código:

ImpressoraDLL INSTANCE = Native.load("caminho/para/DLL/E1_Impressora01.dll", ImpressoraDLL.class);

2.	Compile o projeto:

javac Main.java

3.	Execute:

java Main

**Menu de Opções**

1  - Configurar Conexao

2  - Abrir Conexao

3  - Impressao Texto

4  - Impressao QRCode

5  - Impressao Cod Barras

6  - Impressao XML SAT

7  - Impressao XML Cancelamento SAT

8  - Abrir Gaveta Elgin

9  - Abrir Gaveta

10 - Sinal Sonoro

11 - Avancar Papel

12 - Corte

0  - Sair

**Principais Funções da DLL**

<table>
  <thead>
    <tr>
      <th>Função</th>
      <th>Descrição</th>
    </tr>
  </thead>
  <tbody>
    <tr><td>AbreConexaoImpressora</td><td>Abre conexão com a impressora</td></tr>
    <tr><td>FechaConexaoImpressora</td><td>Encerra conexão</td></tr>
    <tr><td>ImpressaoTexto</td><td>Imprime texto</td></tr>
    <tr><td>ImpressaoQRCode</td><td>Imprime QR Code</td></tr>
    <tr><td>ImpressaoCodigoBarras</td><td>Imprime código de barras</td></tr>
    <tr><td>ImprimeXMLSAT</td><td>Imprime XML SAT</td></tr>
    <tr><td>ImprimeXMLCancelamentoSAT</td><td>Imprime XML de cancelamento SAT</td></tr>
    <tr><td>AvancaPapel</td><td>Avança linhas de papel</td></tr>
    <tr><td>Corte</td><td>Realiza corte</td></tr>
    <tr><td>AbreGaveta</td><td>Abre gaveta com parâmetros</td></tr>
    <tr><td>AbreGavetaElgin</td><td>Abre gaveta padrão Elgin</td></tr>
    <tr><td>SinalSonoro</td><td>Emite sinal sonoro</td></tr>
  </tbody>
</table>

**Objetivos Acadêmicos Atingidos**

•	Uso adequado das funções da impressora

•	Aplicação de laços e condicionais

•	Organização do código em funções próprias

•	Estrutura e clareza do programa

•	Documentação completa e funcional
