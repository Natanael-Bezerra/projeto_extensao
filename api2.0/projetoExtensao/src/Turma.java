import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String nome;
    private List<Professor> professores;

    public Turma(String nome) {
        this.nome = nome;
        this.professores = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void adicionarProfessor(Professor professor) {
        this.professores.add(professor);
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public void imprimirProfessores() {
        System.out.println("Professores da turma " + nome + ":");
        for (Professor professor : professores) {
            professor.imprimirDisponibilidade();
        }
    }
}
