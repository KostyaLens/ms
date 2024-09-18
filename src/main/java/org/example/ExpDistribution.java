package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static java.awt.Font.PLAIN;

public class ExpDistribution extends ApplicationFrame {

    private static final int SAMPLE_SIZE = 100;
    private static final double LAMBDA = 2.0;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;
    private static final int BIN = 10;

    public ExpDistribution(String title) {
        super(title);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        double[] sample = generateExponentialSample(SAMPLE_SIZE, LAMBDA);
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel chartPanel = new JPanel(new GridLayout(1, 3));
        JFreeChart histogramChart = createHistogramWithPolygon(sample);
        ChartPanel histogramChartPanel = new ChartPanel(histogramChart);
        histogramChartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JFreeChart polygonChart = createPolygon(sample);
        ChartPanel polygonChartPanel = new ChartPanel(polygonChart);
        histogramChartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JFreeChart exponentialChart = createExponentialDistributionChart();
        ChartPanel exponentialChartPanel = new ChartPanel(exponentialChart);
        exponentialChartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        chartPanel.add(histogramChartPanel);
        chartPanel.add(polygonChartPanel);
        chartPanel.add(exponentialChartPanel);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        JTextArea sampleTextArea = new JTextArea();
        sampleTextArea.setEditable(false);
        sampleTextArea.setText(formatSample(generateExponentialSample(SAMPLE_SIZE, LAMBDA)));
        JScrollPane scrollPane = new JScrollPane(sampleTextArea);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }


    private String formatSample(double[] sample) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("\n");
        sb.append("Элементы:\n\n");
        for (int i = 0; i < sample.length; i++) {
            sb.append(String.format("%.4f", sample[i]));
            if (i < sample.length - 1) {
                sb.append(", ");
            }
            if ((i + 1) % 20 == 0) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private double[] generateExponentialSample(int size, double lambda) {
        double[] sample = new double[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sample[i] = -Math.log(random.nextDouble()) / lambda;
        }
        return sample;
    }

    private JFreeChart createHistogramWithPolygon(double[] sample) {
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Экмподициальная выборка", sample, BIN);
        JFreeChart histogram = ChartFactory.createHistogram(
                "Гистограмма экспоненциального распределения",
                "Значение",
                "Частота",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
        XYPlot plot = histogram.getXYPlot();
        XYBarRenderer barRenderer = (XYBarRenderer) plot.getRenderer();
        barRenderer.setSeriesPaint(0, new Color(44, 67, 134));
        barRenderer.setDrawBarOutline(false);
        barRenderer.setBarPainter(new StandardXYBarPainter());
        barRenderer.setShadowVisible(true);
        barRenderer.setShadowXOffset(3);
        barRenderer.setShadowYOffset(3);
        plot.setBackgroundPaint(new Color(230, 230, 250));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setInsets(new RectangleInsets(10, 10, 10, 10));
        XYSeries frequencyPolygon = createFrequencyPolygon(sample, BIN);
        XYSeriesCollection polygonDataset = new XYSeriesCollection(frequencyPolygon);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setDataset(1, polygonDataset);
        plot.setRenderer(1, renderer);

        return histogram;
    }

    private JFreeChart createPolygon(double[] sample){
        XYSeries frequencyPolygon = createFrequencyPolygon(sample, BIN);
        XYSeriesCollection polygonDataset = new XYSeriesCollection(frequencyPolygon);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Полигон экспоненциального распределения",
                "Значение",
                "Частота",
                polygonDataset,
                PlotOrientation.VERTICAL,
                false, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(230, 230, 250));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setInsets(new RectangleInsets(10, 10, 10, 10));
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        return chart;
    }


    private JFreeChart createExponentialDistributionChart() {
        XYSeries series = new XYSeries("Экспоненциальное распределение");
        for (double x = 0; x <= 3; x += 0.1) {
            double y = LAMBDA * Math.exp(-LAMBDA * x);  // Формула плотности
            series.add(x, y);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Экспоненциальная плотность распределения",
                "Значение",
                "Распределения",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(230, 230, 250));
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setInsets(new RectangleInsets(10, 10, 10, 10));
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        return chart;
    }


    private XYSeries createFrequencyPolygon(double[] sample, int bins) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (double v : sample) {
            if (v < min) min = v;
            if (v > max) max = v;
        }
        double binWidth = (max - min) / bins;
        int[] frequencies = new int[bins];
        for (double v : sample) {
            int binIndex = (int) ((v - min) / binWidth);
            if (binIndex == bins) binIndex--;
            frequencies[binIndex]++;
        }
        XYSeries polygon = new XYSeries("Frequency Polygon");
        for (int i = 0; i < bins; i++) {
            double x = min + binWidth * (i + 0.5);
            polygon.add(x, frequencies[i]);
        }

        return polygon;
    }


    public static void centerFrameOnScreen(Window frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation(
                (screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2
        );
    }
}
