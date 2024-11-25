import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeradorHorario {

    public static void gerarHorario(Horario horario, ConfiguracaoHorario configuracao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Turma turma : horario.getTurmas()) {
            System.out.println("\nHorário da turma: " + turma.getNome());

            Map<String, Integer> aulasPorDia = configuracao.getAulasPorDia();
            List<String> diasComAulas = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : aulasPorDia.entrySet()) {
                if (entry.getValue() > 0) {
                    diasComAulas.add(entry.getKey());
                }
            }

            System.out.printf("%-15s", "Aula");
            for (String dia : diasComAulas) {
                System.out.printf("%-20s", dia);
            }
            System.out.println();
            System.out.println("-".repeat(15 + 20 * diasComAulas.size()));

            for (int aulaIndex = 1; aulaIndex <= aulasPorDia.values().stream().max(Integer::compare).orElse(0); aulaIndex++) {
                System.out.printf("%-15s", "Aula " + aulaIndex);

                for (String dia : diasComAulas) {
                    if (aulaIndex <= aulasPorDia.get(dia)) {
                        Professor professorAlocado = null;

                        for (Professor professor : turma.getProfessores()) {
                            List<Integer> aulasDisponiveis = professor.getDisponibilidade().getOrDefault(dia, new ArrayList<>());
                            if (aulasDisponiveis.contains(aulaIndex)) {
                                professorAlocado = professor;
                                break;
                            }
                        }

                        System.out.printf("%-20s", professorAlocado != null ? professorAlocado.getNome() : "Vago");
                    } else {
                        System.out.printf("%-20s", " ");
                    }
                }
                System.out.println();
            }
        }
    }
}
