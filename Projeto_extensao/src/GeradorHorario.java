import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeradorHorario {

    public static void gerarHorario(Horario horario, ConfiguracaoHorario configuracao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Turma turma : horario.getTurmas()) {
            System.out.println("\nHorário da turma: " + turma.getNome() + " (" + configuracao.getTurno() + ")");

            Map<String, Integer> aulasPorDia = configuracao.getAulasPorDia();
            List<String> diasComAulas = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : aulasPorDia.entrySet()) {
                if (entry.getValue() > 0) {
                    diasComAulas.add(entry.getKey());
                }
            }

            // Imprimir cabeçalho
            System.out.printf("%-15s", "Horário");
            for (String dia : diasComAulas) {
                System.out.printf("%-20s", dia);
            }
            System.out.println();
            System.out.println("-".repeat(15 + 20 * diasComAulas.size()));

            // Alocar os professores nas aulas
            LocalTime horarioAtual = LocalTime.parse(configuracao.getHorarioInicio(), formatter);
            boolean intervaloAplicado = false;

            // Loop por todas as aulas (e também os dias)
            for (int aulaIndex = 0; aulaIndex < configuracao.getAulasPorDia().values().stream().max(Integer::compare).orElse(0); aulaIndex++) {
                if (!intervaloAplicado && aulaIndex == configuracao.getAulasAntesIntervalo()) {
                    System.out.printf("%-15s", "Intervalo");
                    for (int i = 0; i < diasComAulas.size(); i++) {
                        System.out.printf("%-20s", " ");
                    }
                    System.out.println();
                    horarioAtual = horarioAtual.plusMinutes(configuracao.getIntervalo());
                    intervaloAplicado = true;
                }

                LocalTime horarioTermino = horarioAtual.plusMinutes(configuracao.getDuracaoAula());
                System.out.printf("%-15s", horarioAtual.format(formatter) + " - " + horarioTermino.format(formatter));

                // Atribuição de professores às aulas
                for (String dia : diasComAulas) {
                    if (aulaIndex < aulasPorDia.get(dia)) {
                        Professor professorAlocado = null;

                        for (Professor professor : turma.getProfessores()) {
                            Map<String, List<String>> disponibilidade = professor.getDisponibilidade();
                            if (disponibilidade.containsKey(dia)) {
                                List<String> horariosDisponiveis = disponibilidade.get(dia);

                                for (String horarioDisponivel : horariosDisponiveis) {
                                    LocalTime horarioDisponivelParsed = LocalTime.parse(horarioDisponivel, formatter);

                                    // Verificar se o horário do professor está dentro do intervalo da aula
                                    if (!horarioDisponivelParsed.isBefore(horarioAtual) && !horarioDisponivelParsed.isAfter(horarioTermino)) {
                                        professorAlocado = professor;
                                        break;
                                    }
                                }
                            }

                            if (professorAlocado != null) {
                                break;
                            }
                        }

                        if (professorAlocado != null) {
                            System.out.printf("%-20s", professorAlocado.getNome());
                        } else {
                            System.out.printf("%-20s", "Vago");
                        }
                    } else {
                        System.out.printf("%-20s", " ");
                    }
                }
                System.out.println();
                horarioAtual = horarioTermino;
            }
            System.out.println();
        }
    }
}
