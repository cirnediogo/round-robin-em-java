package roundrobin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author Diogo
 */
public class Grafico extends ApplicationFrame {

    private int numProcessos;
    private ArrayList<String> lista_tempo;
    static TaskSeriesCollection tsc;
    private CategoryPlot plot;

    public Grafico(String title, int numProcessos) {
        super(title);
        this.numProcessos = numProcessos;
        createDatasetBase();
        JFreeChart chart = ChartFactory.createGanttChart("Gráfico1", "Processos", "tempo", tsc, true, false, false);
        plot = (CategoryPlot) chart.getPlot();
        lista_tempo = new ArrayList<String>(50);
        lista_tempo.add("0");
        for (int i = 1; i < 50; i++) {
            lista_tempo.add("");
        }
        String[] eixoT = (String[]) lista_tempo.toArray(new String[0]);
        ValueAxis a = new SymbolAxis("tempo", eixoT);
        a.setLabelFont(plot.getDomainAxis().getLabelFont());
        a.setVerticalTickLabels(true);
        a.setTickLabelsVisible(true);
        plot.setRangeAxis(a);
        CategoryItemRenderer renderer = (CategoryItemRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.black);
        renderer.setSeriesPaint(1, new Color(0, 204, 0));
        renderer.setSeriesPaint(2, new Color(0, 153, 255));
        renderer.setSeriesPaint(3, new Color(255, 153, 51));
        renderer.setSeriesPaint(4, new Color(153, 153, 255));
        renderer.setSeriesPaint(5, new Color(0, 102, 102));
        plot.setRenderer(renderer);

        int size_high = 200;
        if (numProcessos > 1) {
            size_high = 225;
        }
        if (numProcessos > 2) {
            size_high = 250;
        }
        if (numProcessos > 3) {
            size_high = 275;
        }
        if (numProcessos > 4) {
            size_high = 300;
        }
        JPanel painel = new ChartPanel(chart);
        painel.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 15, size_high));
        painel.setLayout(new BorderLayout());
        this.add(painel);
    }

    public void newTask(int serie_index, long start_time, long end_time, boolean bloq, boolean mesmo_processo, int tempo) {
        TaskSeries serie = tsc.getSeries(serie_index);
        if (tempo > lista_tempo.size()) {
            lista_tempo.add("");
            String[] eixoT = (String[]) lista_tempo.toArray(new String[0]);
            ValueAxis a = new SymbolAxis("tempo", eixoT);
            a.setVerticalTickLabels(true);
            a.setTickLabelsVisible(true);
            plot.setRangeAxis(a);
        }
        if (!mesmo_processo) {
            lista_tempo.set(tempo - 1, String.valueOf(tempo - 1));
            String[] eixoT = (String[]) lista_tempo.toArray(new String[0]);
            ValueAxis a = new SymbolAxis("tempo", eixoT);
            a.setVerticalTickLabels(true);
            a.setTickLabelsVisible(true);
            plot.setRangeAxis(a);
            if (serie.getTasks().isEmpty()) {
                serie.add(new Task("", new SimpleTimePeriod(start_time, end_time)));
            }
            Task task = serie.get(0);
            task.addSubtask(new Task("", new SimpleTimePeriod(start_time, end_time)));
            if (bloq) {
                task.getSubtask(task.getSubtaskCount() - 1).setPercentComplete(0);
            }
            task.setDuration(new SimpleTimePeriod(task.getDuration().getStart().getTime(), end_time));
            tsc.seriesChanged(null);
        } else {
            Task task = serie.get(0);
            start_time = task.getSubtask(task.getSubtaskCount() - 1).getDuration().getStart().getTime();
            task.getSubtask(task.getSubtaskCount() - 1).setDuration(new SimpleTimePeriod(start_time, end_time));
            task.setDuration(new SimpleTimePeriod(task.getDuration().getStart().getTime(), end_time));
            tsc.seriesChanged(null);
        }
    }

    public void createDatasetBase() {
        tsc = new TaskSeriesCollection();

        TaskSeries SO_Serie = new TaskSeries("SO");
        tsc.add(SO_Serie);

        TaskSeries P1_Serie = new TaskSeries("P1");
        tsc.add(P1_Serie);
        if (numProcessos > 1) {
            TaskSeries P2_Serie = new TaskSeries("P2");
            tsc.add(P2_Serie);
        }
        if (numProcessos > 2) {
            TaskSeries P3_Serie = new TaskSeries("P3");
            tsc.add(P3_Serie);
        }
        if (numProcessos > 3) {
            TaskSeries P4_Serie = new TaskSeries("P4");
            tsc.add(P4_Serie);
        }
        if (numProcessos > 4) {
            TaskSeries P5_Serie = new TaskSeries("P5");
            tsc.add(P5_Serie);
        }
    }
}
