import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professor {
    private String nome;
    private Map<String, List<String>> disponibilidade;

    public Professor(String nome) {
        this.nome = nome;
        this.disponibilidade = new HashMap<>();
    }
    //armazenar os dias disponíveis dos professores
    public void adicionarDisponibilidade(String dia, List<String> horarios) {
        this.disponibilidade.put(dia, horarios);
    }

    public String getNome() {
        return nome;
    }

    public Map<String, List<String>> getDisponibilidade() {
        return disponibilidade;
    }
}
