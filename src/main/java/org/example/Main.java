package org.example;

public class Main {

    public static void main(String[] args) {
        ExpDistribution chart = new ExpDistribution("Графики");
        chart.pack();
        ExpDistribution.centerFrameOnScreen(chart);  // Центрирование окна
        chart.setVisible(true);
    }
}
