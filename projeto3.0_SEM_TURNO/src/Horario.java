import java.util.ArrayList;
import java.util.List;

public class Horario {
    private List<Turma> turmas;

    public Horario() {
        this.turmas = new ArrayList<>();
    }

    public void adicionarTurma(Turma turma) {
        turmas.add(turma);
    }

    public List<Turma> getTurmas() {
        return turmas;
    }
}
