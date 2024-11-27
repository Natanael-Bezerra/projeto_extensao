import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.*;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class APIServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/processar", new ProcessarHandler());
        server.setExecutor(null); // cria o servidor com o executor padrão
        server.start();
        System.out.println("Servidor rodando em http://localhost:8080");
    }

    static class ProcessarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            // Set CORS headers to allow all origins
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

            // Handle OPTIONS method for CORS pre-flight requests
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                // Send a 200 response to pre-flight requests
                exchange.sendResponseHeaders(200, -1); // -1 indicates no body in response
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                // Lê o corpo da requisição
                InputStreamReader reader = new InputStreamReader(exchange.getRequestBody());
                Gson gson = new Gson();
                Dados dados = gson.fromJson(reader, Dados.class);

                Horario horario = new Horario();
                ConfiguracaoHorario configuracaoHorario = new ConfiguracaoHorario();

                // Fazer configurações gerais
                configuracaoHorario.setHorarioInicio(dados.horario[0]);
                configuracaoHorario.setDuracaoAula(Integer.parseInt(dados.horario[1]));
                configuracaoHorario.setAulasAntesIntervalo(Integer.parseInt(dados.horario[2]));
                configuracaoHorario.setIntervalo(Integer.parseInt(dados.horario[3]));

                for (Map.Entry<String, Integer> entry : dados.diasAula.entrySet()) {
                    String dia = entry.getKey();  // Nome do dia (ex: "segunda", "terca")
                    int numeroDeAulas = entry.getValue();  // Número de aulas para o dia

                    // Chama o metodo passando o nome do dia e o número de aulas
                    configuracaoHorario.adicionarAulasPorDia(dia, numeroDeAulas);
                }

                for (TurmaJson turmaJson : dados.turmas) {
                    Turma turma = new Turma(turmaJson.nomeTurma);

                    for (ProfessorJson professorJson : turmaJson.professores) {
                        Professor professor = new Professor(professorJson.nomeProfessor);

                        for (Map.Entry<String, List<Integer>> entry : professorJson.disponibilidade.entrySet()) {
                            String dia = entry.getKey();  // Ex: "segunda"
                            List<Integer> aulas = entry.getValue();  // Ex: [1, 2]
                            professor.adicionarDisponibilidade(dia, aulas);
                        }

                        turma.adicionarProfessor(professor);
                    }

                    horario.adicionarTurma(turma);
                }

                // Geração do quadro de horários para todas as turmas
                String horarioJson = GeradorHorario.gerarHorario(horario, configuracaoHorario);

                // Responde ao cliente com a mensagem de sucesso e o JSON
                String response = horarioJson;

                // Envia o código de resposta HTTP 200
                exchange.sendResponseHeaders(200, response.getBytes().length);

                // Obtém o corpo da resposta
                OutputStream os = exchange.getResponseBody();

                // Escreve a resposta (mensagem + JSON)
                os.write(response.getBytes());

                // Fecha a saída
                os.close();

            }
        }
    }

    // Modelo de Dados
    static class Dados {
        public String[] horario;
        public Map<String, Integer> diasAula;
        public List<TurmaJson> turmas;
    }

    static class TurmaJson {
        public String nomeTurma;
        public List<ProfessorJson> professores;
    }

    static class ProfessorJson {
        public String nomeProfessor;
        public Map<String, List<Integer>> disponibilidade;
    }
}
