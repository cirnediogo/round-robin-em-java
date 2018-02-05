package roundrobin;

/**
 *
 * @author Diogo
 */
public class Processo {

    private int indice;
    private int tempoChegada;
    private int duracao;
    private int tempoBloqueio;
    private int duracaoBloqueio;
    private int tempoEmExecucao;
    private int tempoTotalExecução;
    private int tempoResposta;
    private int tempoEspera;
    private boolean jaExecutou;
    private int entrouNaEspera;

    public Processo(int indice, int tempoChegada, int duracao, int tempoBloqueio, int duracaoBloqueio, String nome) {
        this.indice = indice;
        this.tempoChegada = tempoChegada;
        this.duracao = duracao;
        this.tempoBloqueio = tempoBloqueio;
        this.duracaoBloqueio = duracaoBloqueio;
        this.tempoEmExecucao = 0;
        this.tempoTotalExecução = -1;
        this.tempoResposta = -1;
        this.tempoEspera = 0;
        this.jaExecutou = false;
        this.entrouNaEspera = -1;
    }

    public int getTempoEmExecucao() {
        return tempoEmExecucao;
    }

    public void setTempoEmExecucao(int tempoEmExecucao) {
        this.tempoEmExecucao = tempoEmExecucao;
    }

    public int getIndice() {
        return indice;
    }

    public int getDuracao() {
        return duracao;
    }

    public int getDuracaoBloqueio() {
        return duracaoBloqueio;
    }

    public int getTempoBloqueio() {
        return tempoBloqueio;
    }

    public int getTempoChegada() {
        return tempoChegada;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public int getTempoResposta() {
        return tempoResposta;
    }

    public void setTempoResposta(int tempoResposta) {
        this.tempoResposta = tempoResposta;
    }

    public int getTempoTotalExecução() {
        return tempoTotalExecução;
    }

    public void setTempoTotalExecução(int tempoTotalExecução) {
        this.tempoTotalExecução = tempoTotalExecução;
    }

    public int getEntrouNaEspera() {
        return entrouNaEspera;
    }

    public void setEntrouNaEspera(int entrouNaEspera) {
        this.entrouNaEspera = entrouNaEspera;
    }

    public boolean isJaExecutou() {
        return jaExecutou;
    }

    public void setJaExecutou(boolean jaExecutou) {
        this.jaExecutou = jaExecutou;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void setDuracaoBloqueio(int duracaoBloqueio) {
        this.duracaoBloqueio = duracaoBloqueio;
    }

    public void setTempoBloqueio(int tempoBloqueio) {
        this.tempoBloqueio = tempoBloqueio;
    }
}
