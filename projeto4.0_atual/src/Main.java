import java.util.*;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Horario horario = new Horario();
        ConfiguracaoHorario configuracaoHorario = new ConfiguracaoHorario();

        // Perguntar configurações gerais
        configuracaoHorario.setHorarioInicio(obterHorarioInicio(scanner));
        configuracaoHorario.setDuracaoAula(obterDuracaoAula(scanner));
        configuracaoHorario.setAulasAntesIntervalo(obterAulasAntesIntervalo(scanner));
        configuracaoHorario.setIntervalo(obterIntervalo(scanner));

        System.out.println("Defina a quantidade de aulas por dia:");
        for (String dia : Arrays.asList("segunda", "terça", "quarta", "quinta", "sexta")) {
            configuracaoHorario.adicionarAulasPorDia(dia, obterAulasPorDia(scanner, dia));
        }

        System.out.println("Digite o número de turmas:");
        int numeroTurmas = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numeroTurmas; i++) {
            System.out.println("Digite o nome da turma " + (i + 1) + ":");
            String nomeTurma = scanner.nextLine();
            Turma turma = new Turma(nomeTurma);

            System.out.println("Digite o número de professores para a turma " + nomeTurma + ":");
            int numeroProfessores = scanner.nextInt();
            scanner.nextLine();

            for (int j = 0; j < numeroProfessores; j++) {
                System.out.println("Digite o nome do professor " + (j + 1) + ":");
                String nomeProfessor = scanner.nextLine();
                Professor professor = new Professor(nomeProfessor);

                System.out.println("Digite o número de dias disponíveis para " + nomeProfessor + ":");
                int numeroDias = scanner.nextInt();
                scanner.nextLine();

                for (int k = 0; k < numeroDias; k++) {
                    System.out.println("Digite o nome do dia (ex: Segunda, Terça) " + (k + 1) + ":");
                    String dia = scanner.nextLine();

                    System.out.println("Digite os números das aulas disponíveis para " + dia + " separados por vírgula (ex: 1,3,5):");
                    String aulasInput = scanner.nextLine();
                    List<Integer> aulas = new ArrayList<>();
                    for (String aula : aulasInput.split(",")) {
                        aulas.add(Integer.parseInt(aula.trim()));
                    }

                    professor.adicionarDisponibilidade(dia, aulas);
                }

                turma.adicionarProfessor(professor);
            }

            horario.adicionarTurma(turma);
        }

        // Geração do quadro de horários para todas as turmas
        GeradorHorario.gerarHorario(horario, configuracaoHorario);

        scanner.close();
    }

    private static String obterHorarioInicio(Scanner scanner) {
        String horarioInicio;
        while (true) {
            System.out.println("Digite o horário de início das aulas (formato HH:mm):");
            horarioInicio = scanner.nextLine().trim();
            try {
                DateTimeFormatter.ofPattern("HH:mm").parse(horarioInicio);
                return horarioInicio;
            } catch (Exception e) {
                System.out.println("Horário inválido. Certifique-se de usar o formato HH:mm e tente novamente.");
            }
        }
    }

    private static int obterDuracaoAula(Scanner scanner) {
        int duracaoAula;
        while (true) {
            System.out.println("Digite a duração de cada aula em minutos:");
            if (scanner.hasNextInt()) {
                duracaoAula = scanner.nextInt();
                scanner.nextLine();
                if (duracaoAula > 0) {
                    return duracaoAula;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Duração inválida. Digite um número positivo.");
        }
    }

    private static int obterAulasAntesIntervalo(Scanner scanner) {
        int aulasAntesIntervalo;
        while (true) {
            System.out.println("Digite a quantidade de aulas antes do intervalo:");
            if (scanner.hasNextInt()) {
                aulasAntesIntervalo = scanner.nextInt();
                scanner.nextLine();
                if (aulasAntesIntervalo > 0) {
                    return aulasAntesIntervalo;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Quantidade inválida. Digite um número positivo.");
        }
    }

    private static int obterIntervalo(Scanner scanner) {
        int intervalo;
        while (true) {
            System.out.println("Digite a duração do intervalo em minutos:");
            if (scanner.hasNextInt()) {
                intervalo = scanner.nextInt();
                scanner.nextLine();
                if (intervalo > 0) {
                    return intervalo;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Intervalo inválido. Digite um número positivo.");
        }
    }

    private static int obterAulasPorDia(Scanner scanner, String dia) {
        int aulasPorDia;
        while (true) {
            System.out.println("Digite o número de aulas para " + dia + ": (Digite 0 se não houver aulas)");
            if (scanner.hasNextInt()) {
                aulasPorDia = scanner.nextInt();
                scanner.nextLine();
                if (aulasPorDia >= 0) {
                    return aulasPorDia;
                }
            } else {
                scanner.nextLine();
            }
            System.out.println("Número de aulas inválido. Digite um número inteiro não negativo.");
        }
    }
}
