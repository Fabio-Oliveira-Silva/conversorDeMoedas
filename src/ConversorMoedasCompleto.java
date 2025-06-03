import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConversorMoedasCompleto {


    private static final String API_KEY = "97580142f29ef9b552d35a32";
    private static final String API_URL_BASE = "https://v6.exchangerate-api.com/v6/";


    private static final List<String> MOEDAS_FILTRADAS = Arrays.asList("ARS", "BOB", "BRL", "CLP", "COP", "USD");

    private static Gson gson = new Gson();
    private static HttpClient httpClient = HttpClient.newHttpClient();



    static class ApiResponse {
        private String result;
        @SerializedName("base_code")
        private String baseCode;
        @SerializedName("conversion_rates")
        private Map<String, Double> conversionRates;

        // Getters
        public String getResult() {
            return result;
        }

        public String getBaseCode() {
            return baseCode;
        }

        public Map<String, Double> getConversionRates() {
            return conversionRates;
        }


        public void setResult(String result) {
            this.result = result;
        }

        public void setBaseCode(String baseCode) {
            this.baseCode = baseCode;
        }

        public void setConversionRates(Map<String, Double> conversionRates) {
            this.conversionRates = conversionRates;
        }
    }

    /**
     * Etapa 1, 2 e 3: Cliente HTTP, Requisição e Resposta
     * Etapa 4 parte B e Etapa 5: Analisar JSON e Filtrar Moedas
     */
    public static Map<String, Double> buscarTaxasDeCambio(String moedaBase) throws IOException, InterruptedException {
        if (API_KEY.equals("SUA_API_KEY_AQUI")) {
            System.out.println("*********************************************************************");
            System.out.println("ERRO: Por favor, substitua 'SUA_API_KEY_AQUI' pela sua chave da API.");
            System.out.println("Você pode obter uma chave em https://www.exchangerate-api.com");
            System.out.println("*********************************************************************");
            return new HashMap<>();
        }

        String url = API_URL_BASE + API_KEY + "/latest/" + moedaBase;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.out.println("Erro ao buscar taxas de câmbio: Status " + response.statusCode());
            System.out.println("Corpo da resposta: " + response.body());
            throw new IOException("Falha na API com status: " + response.statusCode());
        }

        // 4) analisar a resposta em formato JSON
        ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);

        if (apiResponse == null || !"success".equalsIgnoreCase(apiResponse.getResult()) || apiResponse.getConversionRates() == null) {
            System.out.println("Resposta da API não foi bem-sucedida ou está malformada.");
            System.out.println("JSON recebido: " + response.body());
            throw new IOException("Resposta da API inválida ou com erro: " + (apiResponse != null ? apiResponse.getResult() : "null"));
        }


        Map<String, Double> taxasFiltradas = apiResponse.getConversionRates().entrySet().stream()
                .filter(entry -> MOEDAS_FILTRADAS.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return taxasFiltradas;
    }

    /**
     * Etapa 6: Converter os valores
     */
    public static double converterMoeda(double valor, String moedaOrigem, String moedaDestino, Map<String, Double> taxas) {
        if (!taxas.containsKey(moedaOrigem) || !taxas.containsKey(moedaDestino)) {
            throw new IllegalArgumentException("Moeda de origem ou destino não encontrada nas taxas disponíveis.");
        }


        double taxaOrigem = taxas.get(moedaOrigem);
        double taxaDestino = taxas.get(moedaDestino);

        return valor * (taxaDestino / taxaOrigem);
    }

    /**
     * Etapa 7: Interagir com o usuário
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Double> taxasDeCambio = null;

        try {

            System.out.println("Buscando taxas de câmbio atualizadas...");
            taxasDeCambio = buscarTaxasDeCambio("USD");
            if (taxasDeCambio.isEmpty() && API_KEY.equals("SUA_API_KEY_AQUI")) {
                System.out.println("Programa encerrado devido à falta da API Key.");
                return; // Encerra se a API key não foi configurada
            }
            System.out.println("Taxas carregadas com sucesso para: " + taxasDeCambio.keySet());
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro crítico ao buscar taxas de câmbio iniciais: " + e.getMessage());
            // e.printStackTrace();
            System.out.println("Não foi possível carregar as taxas de câmbio. O programa será encerrado.");
            return;
        }

        if (taxasDeCambio == null || taxasDeCambio.isEmpty()) {
            System.out.println("Não foi possível carregar as taxas de câmbio. O programa será encerrado.");
            return;
        }

        int opcao = 0;
        while (opcao != 7) {
            System.out.println("\n**********************************************");
            System.out.println("Bem-vindo ao Conversor de Moedas Alura!");
            System.out.println("Moedas disponíveis: " + String.join(", ", MOEDAS_FILTRADAS));
            System.out.println("Escolha uma opção:");
            System.out.println("1. USD (Dólar Americano) => BRL (Real Brasileiro)");
            System.out.println("2. BRL (Real Brasileiro) => USD (Dólar Americano)");
            System.out.println("3. USD (Dólar Americano) => ARS (Peso Argentino)");
            System.out.println("4. ARS (Peso Argentino) => USD (Dólar Americano)");
            System.out.println("5. BRL (Real Brasileiro) => COP (Peso Colombiano)");
            System.out.println("6. COP (Peso Colombiano) => BRL (Real Brasileiro)");
            System.out.println("7. Sair");
            System.out.println("8. Outra conversão (entre as disponíveis)");
            System.out.print("Digite a opção desejada: ");


            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
            } else {
                System.out.println("Opção inválida. Por favor, digite um número.");
                scanner.next();
                continue;
            }
            scanner.nextLine();

            String moedaOrigem = "";
            String moedaDestino = "";
            double valorParaConverter = 0;

            try {
                switch (opcao) {
                    case 1:
                        moedaOrigem = "USD"; moedaDestino = "BRL";
                        break;
                    case 2:
                        moedaOrigem = "BRL"; moedaDestino = "USD";
                        break;
                    case 3:
                        moedaOrigem = "USD"; moedaDestino = "ARS";
                        break;
                    case 4:
                        moedaOrigem = "ARS"; moedaDestino = "USD";
                        break;
                    case 5:
                        moedaOrigem = "BRL"; moedaDestino = "COP";
                        break;
                    case 6:
                        moedaOrigem = "COP"; moedaDestino = "BRL";
                        break;
                    case 8:
                        System.out.print("Digite a sigla da moeda de ORIGEM (ex: USD): ");
                        moedaOrigem = scanner.nextLine().toUpperCase();
                        if (!MOEDAS_FILTRADAS.contains(moedaOrigem) || !taxasDeCambio.containsKey(moedaOrigem)) {
                            System.out.println("Moeda de origem inválida ou não disponível.");
                            continue;
                        }

                        System.out.print("Digite a sigla da moeda de DESTINO (ex: BRL): ");
                        moedaDestino = scanner.nextLine().toUpperCase();
                        if (!MOEDAS_FILTRADAS.contains(moedaDestino) || !taxasDeCambio.containsKey(moedaDestino)) {
                            System.out.println("Moeda de destino inválida ou não disponível.");
                            continue;
                        }
                        break;
                    case 7:
                        System.out.println("Obrigado por usar o Conversor de Moedas! Saindo...");
                        continue;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        continue;
                }


                System.out.print("Digite o valor a ser convertido em " + moedaOrigem + ": ");
                if (scanner.hasNextDouble()) {
                    valorParaConverter = scanner.nextDouble();
                    if (valorParaConverter <= 0) {
                        System.out.println("O valor para conversão deve ser positivo.");
                        scanner.nextLine();
                        continue;
                    }
                } else {
                    System.out.println("Valor inválido. Por favor, digite um número.");
                    scanner.nextLine();
                    continue;
                }
                scanner.nextLine();

                double valorConvertido = converterMoeda(valorParaConverter, moedaOrigem, moedaDestino, taxasDeCambio);

                System.out.printf("\n%.2f %s equivalem a %.2f %s\n",
                        valorParaConverter, moedaOrigem, valorConvertido, moedaDestino);

            } catch (IllegalArgumentException e) {
                System.err.println("Erro na conversão: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());

            }
        }
        scanner.close();
        System.out.println("Programa finalizado.");
    }
}