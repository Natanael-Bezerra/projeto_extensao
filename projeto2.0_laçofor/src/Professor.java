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

    public void adicionarDisponibilidade(String dia, List<String> horarios) {
        this.disponibilidade.put(dia, horarios);
    }

    public String getNome() {
        return nome;
    }

    public Map<String, List<String>> getDisponibilidade() {
        return disponibilidade;
    }

    public void imprimirDisponibilidade() {
        System.out.println("Disponibilidade de " + nome + ":");
        for (Map.Entry<String, List<String>> entry : disponibilidade.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + String.join(", ", entry.getValue()));
        }
    }
}
