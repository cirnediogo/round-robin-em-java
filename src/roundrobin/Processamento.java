package roundrobin;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Diogo
 */
public class Processamento extends Thread {

    private int quantum;
    private int overhead;
    private int numProcessos;
    protected Processo Processo1;
    protected Processo Processo2;
    protected Processo Processo3;
    protected Processo Processo4;
    protected Processo Processo5;
    private PriorityQueue<Processo> filaChegada;
    private Queue<Processo> filaPronto;
    private PriorityQueue<Processo> filaBloqueio;
    private int sec;
    public Grafico g;
    private boolean proc_vazio;
    private int processoEmExecucao;
    private int tempoExecucaoSO;
    private boolean mesmo_processo;
    private boolean game_over;

    public Processamento(int quantum, int overhead, int numProcessos, int[] tempoChegada, int[] duracao, int[] tempoBloqueio, int[] duracaoBloqueio) {
        super();
        this.quantum = quantum;
        this.overhead = overhead;
        this.numProcessos = numProcessos;

        Processo1 = new Processo(1, tempoChegada[0], duracao[0], tempoBloqueio[0], duracaoBloqueio[0], "P1");
        Processo2 = null;
        Processo3 = null;
        Processo4 = null;
        Processo5 = null;
        if (numProcessos > 1) {
            Processo2 = new Processo(2, tempoChegada[1], duracao[1], tempoBloqueio[1], duracaoBloqueio[1], "P2");
        }
        if (numProcessos > 2) {
            Processo3 = new Processo(3, tempoChegada[2], duracao[2], tempoBloqueio[2], duracaoBloqueio[2], "P3");
        }
        if (numProcessos > 3) {
            Processo4 = new Processo(4, tempoChegada[3], duracao[3], tempoBloqueio[3], duracaoBloqueio[3], "P4");
        }
        if (numProcessos > 4) {
            Processo5 = new Processo(5, tempoChegada[4], duracao[4], tempoBloqueio[4], duracaoBloqueio[4], "P5");
        }

        filaChegada = new PriorityQueue(5, new Comparator<Processo>() {

            public int compare(Processo processoA, Processo processoB) {
                int a = processoA.getTempoChegada();
                int b = processoB.getTempoChegada();
                if (a < b) {
                    return -1;
                }
                if (b < a) {
                    return 1;
                }
                if (a == b) {
                    a = processoA.getIndice();
                    b = processoB.getIndice();
                    if (a < b) {
                        return -1;
                    }
                    if (b < a) {
                        return 1;
                    }
                }
                return 0;
            }
        });

        filaChegada.offer(Processo1);
        if (numProcessos > 1) {
            filaChegada.offer(Processo2);
        }
        if (numProcessos > 2) {
            filaChegada.offer(Processo3);
        }
        if (numProcessos > 3) {
            filaChegada.offer(Processo4);
        }
        if (numProcessos > 4) {
            filaChegada.offer(Processo5);
        }

        filaPronto = new ConcurrentLinkedQueue<Processo>();
        filaBloqueio = new PriorityQueue<Processo>(5, new Comparator<Processo>() {

            public int compare(Processo processoA, Processo processoB) {
                int a = processoA.getTempoEmExecucao();
                int b = processoB.getTempoEmExecucao();
                if (a < b) {
                    return -1;
                }
                if (b < a) {
                    return 1;
                }
                return 0;
            }
        });

        g = new Grafico("Round Robin", numProcessos);
        g.pack();
        g.setVisible(true);

        sec = 0;
        proc_vazio = true;
        processoEmExecucao = 0;
        mesmo_processo = false;
        game_over = false;

    }

    public Processo getProcesso(int indice) {
        if (indice == 1) {
            return Processo1;
        }
        if (indice == 2) {
            return Processo2;
        }
        if (indice == 3) {
            return Processo3;
        }
        if (indice == 4) {
            return Processo4;
        }
        if (indice == 5) {
            return Processo5;
        }
        return null;
    }

    @Override
    public void run() {

        while (!game_over) {
            while ((!filaChegada.isEmpty()) && (filaChegada.element().getTempoChegada() == sec)) {
                filaPronto.offer(filaChegada.remove());
            }

            Queue<Processo> filaAux = new ConcurrentLinkedQueue();
            while (!filaBloqueio.isEmpty()) {
                Processo noBloqueio = filaBloqueio.remove();
                boolean mesmo_bloqueio = true;
                if (noBloqueio.getDuracaoBloqueio() == noBloqueio.getTempoEmExecucao()) {
                    mesmo_bloqueio = false;
                }
                noBloqueio.setTempoEmExecucao(noBloqueio.getTempoEmExecucao() - 1);
                g.newTask(noBloqueio.getIndice(), sec - 1, sec, true, mesmo_bloqueio, sec);
                filaAux.offer(noBloqueio);
            }
            while (!filaAux.isEmpty()) {
                filaBloqueio.offer(filaAux.remove());
            }
            while ((!filaBloqueio.isEmpty()) && (filaBloqueio.element().getTempoEmExecucao() == 0)) {
                filaBloqueio.element().setEntrouNaEspera(sec);
                filaPronto.offer(filaBloqueio.remove());
            }
            if ((!proc_vazio) && processoEmExecucao == 0) {
                g.newTask(processoEmExecucao, sec - 1, sec, false, mesmo_processo, sec);
                tempoExecucaoSO--;
                mesmo_processo = true;
                if (tempoExecucaoSO == 0) {
                    proc_vazio = true;
                    mesmo_processo = false;
                }
            }
            if ((!proc_vazio) && processoEmExecucao != 0) {
                Processo noProcessador = getProcesso(processoEmExecucao);
                g.newTask(processoEmExecucao, sec - 1, sec, false, mesmo_processo, sec);
                noProcessador.setTempoEmExecucao(noProcessador.getTempoEmExecucao() - 1);
                noProcessador.setDuracao(noProcessador.getDuracao() - 1);
                noProcessador.setTempoBloqueio(noProcessador.getTempoBloqueio() - 1);
                mesmo_processo = true;
                if (noProcessador.getTempoEmExecucao() == 0) {
                    if (noProcessador.getTempoBloqueio() == 0) {
                        noProcessador.setTempoBloqueio(-1);
                        noProcessador.setTempoEmExecucao(noProcessador.getDuracaoBloqueio());
                        filaBloqueio.offer(noProcessador);
                    } else if (noProcessador.getDuracao() != 0) {
                        noProcessador.setEntrouNaEspera(sec);
                        filaPronto.offer(noProcessador);
                    } else {
                        noProcessador.setTempoTotalExecução(sec - noProcessador.getTempoChegada());
                    }
                    processoEmExecucao = 0;
                    mesmo_processo = false;
                    tempoExecucaoSO = overhead;
                }
            }
            if (proc_vazio && (!filaPronto.isEmpty())) {
                Processo vaiExecutar = filaPronto.remove();
                processoEmExecucao = vaiExecutar.getIndice();
                while ((!filaPronto.isEmpty()) && (vaiExecutar.getDuracao() == 0)) {
                    vaiExecutar.setTempoTotalExecução(sec - vaiExecutar.getTempoChegada());
                    vaiExecutar = filaPronto.remove();
                    processoEmExecucao = vaiExecutar.getIndice();
                }
                if (vaiExecutar.getDuracao() != 0) {
                    if (!vaiExecutar.isJaExecutou()) {
                        vaiExecutar.setTempoResposta(sec - vaiExecutar.getTempoChegada());
                        vaiExecutar.setJaExecutou(true);
                        vaiExecutar.setEntrouNaEspera(vaiExecutar.getTempoChegada());
                    }
                    vaiExecutar.setTempoEspera(vaiExecutar.getTempoEspera() + sec - vaiExecutar.getEntrouNaEspera());
                    int tmp = vaiExecutar.getTempoBloqueio();
                    if ((quantum < tmp) || (tmp < 0)) {
                        tmp = quantum;
                    }
                    if (vaiExecutar.getDuracao() < tmp) {
                        tmp = vaiExecutar.getDuracao();
                    }
                    vaiExecutar.setTempoEmExecucao(tmp);
                    proc_vazio = false;
                } else {
                    vaiExecutar.setTempoTotalExecução(sec - vaiExecutar.getTempoChegada());
                }
            }
            sec++;
            if (filaBloqueio.isEmpty() && filaChegada.isEmpty() && filaBloqueio.isEmpty() && proc_vazio) {
                game_over = true;
                int[] tempoTotalExec = {Processo1.getTempoTotalExecução(), 0, 0, 0, 0};
                int[] tempoResp = {Processo1.getTempoResposta(), 0, 0, 0, 0};
                int[] tempoEsp = {Processo1.getTempoEspera(), 0, 0, 0, 0};
                if (numProcessos > 1) {
                    tempoTotalExec[1] = Processo2.getTempoTotalExecução();
                    tempoResp[1] = Processo2.getTempoResposta();
                    tempoEsp[1] = Processo2.getTempoEspera();
                }
                if (numProcessos > 2) {
                    tempoTotalExec[2] = Processo3.getTempoTotalExecução();
                    tempoResp[2] = Processo3.getTempoResposta();
                    tempoEsp[2] = Processo3.getTempoEspera();
                }
                if (numProcessos > 3) {
                    tempoTotalExec[3] = Processo4.getTempoTotalExecução();
                    tempoResp[3] = Processo4.getTempoResposta();
                    tempoEsp[3] = Processo4.getTempoEspera();
                }
                if (numProcessos > 4) {
                    tempoTotalExec[4] = Processo5.getTempoTotalExecução();
                    tempoResp[4] = Processo5.getTempoResposta();
                    tempoEsp[4] = Processo5.getTempoEspera();
                }
                TabelaFinal tf = new TabelaFinal(g, numProcessos, tempoTotalExec, tempoResp, tempoEsp);
                tf.setVisible(true);
            }
            try {
                Processamento.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
