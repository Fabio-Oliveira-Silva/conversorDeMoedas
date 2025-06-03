# Conversor de Moedas - Desafio Alura

Um conversor de moedas simples em Java, criado para o desafio da Alura. O programa busca taxas de câmbio atuais de uma API e permite converter valores entre BRL, USD, ARS, BOB, CLP e COP.

## 🛠️ Tecnologias

* Java 11+
* Biblioteca Gson (para JSON)
* ExchangeRate-API (para taxas de câmbio)

## 🚀 Configuração Essencial

1.  **API Key:**
    * Obtenha uma API Key gratuita em [ExchangeRate-API](https://www.exchangerate-api.com).
    * No arquivo `ConversorMoedasCompleto.java` (ou similar), substitua `"SUA_API_KEY_AQUI"` pela sua chave:
        ```java
        private static final String API_KEY = "SUA_API_KEY_AQUI";
        ```

2.  **Biblioteca Gson:**
    * **Maven:** Adicione ao `pom.xml`:
        ```xml
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
        ```
    * **Gradle:** Adicione ao `build.gradle`:
        ```gradle
        implementation 'com.google.code.gson:gson:2.10.1'
        ```
    * **Manual:** Baixe o JAR do Gson e adicione ao classpath do seu projeto na IDE.

## ▶️ Como Executar

1.  Abra o projeto em sua IDE Java (ex: IntelliJ IDEA).
2.  Configure a API Key e a dependência Gson conforme acima.
3.  Execute o método `main` da classe principal (`ConversorMoedasCompleto.java`).

## 💻 Uso

O programa apresentará um menu no console. Digite o número da opção desejada para:
* Converter entre pares de moedas pré-definidos.
* Escolher moedas de origem e destino para conversão.
* Sair do programa.

Siga as instruções no console para informar os valores a serem convertidos.
