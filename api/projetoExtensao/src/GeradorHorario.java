import com.google.gson.Gson;
import java.util.*;

public class GeradorHorario {

    public static String gerarHorario(Horario horario, ConfiguracaoHorario configuracao) {
        // Inicializa o Gson para gerar o JSON
        Gson gson = new Gson();

        // Lista que irá armazenar as turmas
        List<Map<String, Object>> turmasJson = new ArrayList<>();

        // Itera sobre as turmas
        for (Turma turma : horario.getTurmas()) {
            Map<String, Object> turmaJson = new HashMap<>();
            turmaJson.put("nomeTurma", turma.getNome());

            // Cria o mapa de dias da semana
            Map<String, List<String>> diasDaSemana = new HashMap<>();

            // Pega a configuração de aulas por dia
            Map<String, Integer> aulasPorDia = configuracao.getAulasPorDia();
            List<String> diasComAulas = new ArrayList<>();

            // Filtra os dias que possuem aulas (aula > 0)
            for (Map.Entry<String, Integer> entry : aulasPorDia.entrySet()) {
                if (entry.getValue() > 0) {
                    diasComAulas.add(entry.getKey());
                }
            }

            // Ordena os dias da semana para garantir que sempre seja na ordem correta
            List<String> diasDaSemanaOrdenados = Arrays.asList("segunda", "terca", "quarta", "quinta", "sexta");

            // Preenche as aulas para cada dia da semana
            for (String dia : diasDaSemanaOrdenados) {
                if (diasComAulas.contains(dia)) {
                    List<String> aulas = new ArrayList<>();
                    // Itera sobre as aulas
                    for (int aulaIndex = 1; aulaIndex <= aulasPorDia.get(dia); aulaIndex++) {
                        String professorAlocado = null;

                        // Aloca um professor para essa aula
                        for (Professor professor : turma.getProfessores()) {
                            List<Integer> aulasDisponiveis = professor.getDisponibilidade().getOrDefault(dia, new ArrayList<>());
                            if (aulasDisponiveis.contains(aulaIndex)) {
                                professorAlocado = professor.getNome();
                                break;
                            }
                        }

                        // Se não houver professor alocado, coloca "--------" (intervalo)
                        if (professorAlocado == null) {
                            aulas.add("--------");
                        } else {
                            aulas.add(professorAlocado);
                        }
                    }
                    diasDaSemana.put(dia, aulas);
                } else {
                    // Caso não tenha aula nesse dia, preenche com uma lista vazia
                    diasDaSemana.put(dia, new ArrayList<>());
                }
            }

            turmaJson.put("dias", diasDaSemana);
            turmasJson.add(turmaJson);
        }

        // Cria o objeto final com a lista de turmas
        Map<String, Object> resultJson = new HashMap<>();
        resultJson.put("turmas", turmasJson);

        // Converte o objeto para JSON
        return gson.toJson(resultJson);
    }
}
// versao que retorna json 1