import java.util.HashMap;
import java.util.Map;

public class ConfiguracaoHorario {
    private String turno;
    private String horarioInicio;
    private int duracaoAula;
    private int intervalo;
    private int aulasAntesIntervalo;
    private Map<String, Integer> aulasPorDia;

    public ConfiguracaoHorario() {
        this.aulasPorDia = new HashMap<>();
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public int getDuracaoAula() {
        return duracaoAula;
    }

    public void setDuracaoAula(int duracaoAula) {
        this.duracaoAula = duracaoAula;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public int getAulasAntesIntervalo() {
        return aulasAntesIntervalo;
    }

    public void setAulasAntesIntervalo(int aulasAntesIntervalo) {
        this.aulasAntesIntervalo = aulasAntesIntervalo;
    }

    public void adicionarAulasPorDia(String dia, int quantidade) {
        this.aulasPorDia.put(dia, quantidade);
    }

    public Map<String, Integer> getAulasPorDia() {
        return aulasPorDia;
    }
}
