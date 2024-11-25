import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professor {
    private String nome;
    private Map<String, List<Integer>> disponibilidade;

    public Professor(String nome) {
        this.nome = nome;
        this.disponibilidade = new HashMap<>();
    }

    public void adicionarDisponibilidade(String dia, List<Integer> aulasDisponiveis) {
        this.disponibilidade.put(dia, aulasDisponiveis);
    }

    public String getNome() {
        return nome;
    }

    public Map<String, List<Integer>> getDisponibilidade() {
        return this.disponibilidade;
    }

    public void imprimirDisponibilidade() {
        System.out.println("Disponibilidade de " + nome + ":");
        for (Map.Entry<String, List<Integer>> entry : disponibilidade.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
