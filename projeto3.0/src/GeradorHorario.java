import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeradorHorario {

    public static void gerarHorario(Horario horario, ConfiguracaoHorario configuracao) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Turma turma : horario.getTurmas()) {
            System.out.println("\nHorário da turma: " + turma.getNome() + " (" + configuracao.getTurno() + ")");

            // Exibir os professores da turma
            System.out.println("Professores da turma " + turma.getNome() + ":");
            for (Professor professor : turma.getProfessores()) {
                System.out.println("Disponibilidade de " + professor.getNome() + ":");
                professor.getDisponibilidade().forEach((dia, horarios) -> {
                    System.out.println("  " + dia + ": " + String.join(", ", horarios));
                });
            }

            Map<String, Integer> aulasPorDia = configuracao.getAulasPorDia();
            List<String> diasComAulas = new ArrayList<>();

            // Adicionar os dias com aulas
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

                // Imprimir intervalo após o número de aulas antes do intervalo
                if (!intervaloAplicado && aulaIndex == configuracao.getAulasAntesIntervalo()) {
                    System.out.printf("%-15s", "Intervalo");
                    for (int i = 0; i < diasComAulas.size(); i++) {
                        System.out.printf("%-20s", " ");
                    }
                    System.out.println();
                    horarioAtual = horarioAtual.plusMinutes(configuracao.getIntervalo());
                    intervaloAplicado = true;
                }

                // Calcular o horário de término da aula
                LocalTime horarioTermino = horarioAtual.plusMinutes(configuracao.getDuracaoAula());
                System.out.printf("%-15s", horarioAtual.format(formatter) + " - " + horarioTermino.format(formatter));

                // Atribuição de professores às aulas
                for (String dia : diasComAulas) {
                    if (aulaIndex < aulasPorDia.get(dia)) {
                        Professor professorAlocado = null;

                        // Verificar se o professor tem disponibilidade
                        for (Professor professor : turma.getProfessores()) {
                            Map<String, List<String>> disponibilidade = professor.getDisponibilidade();
                            if (disponibilidade.containsKey(dia)) {
                                List<String> horariosDisponiveis = disponibilidade.get(dia);

                                // Verificar se algum dos horários disponíveis do professor coincide com a aula
                                for (String horarioDisponivel : horariosDisponiveis) {
                                    try {
                                        LocalTime horarioDisponivelParsed = LocalTime.parse(horarioDisponivel.trim(), formatter);

                                        // Verificar se o horário do professor está dentro do intervalo da aula
                                        if (!horarioDisponivelParsed.isBefore(horarioAtual) && horarioDisponivelParsed.isBefore(horarioTermino)) {
                                            professorAlocado = professor;
                                            break;
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Erro ao interpretar horário: " + horarioDisponivel);
                                    }
                                }
                            }

                            if (professorAlocado != null) {
                                break;
                            }
                        }

                        // Imprimir o nome do professor alocado ou "Vago"
                        if (professorAlocado != null) {
                            System.out.printf("%-20s", professorAlocado.getNome());
                        } else {
                            System.out.printf("%-20s", "Vago");
                        }
                    } else {
                        // Caso não haja aula nesse horário, imprimir espaço
                        System.out.printf("%-20s", " ");
                    }
                }
                System.out.println();

                // Avançar para o próximo horário
                horarioAtual = horarioTermino;
            }
        }
    }
}
