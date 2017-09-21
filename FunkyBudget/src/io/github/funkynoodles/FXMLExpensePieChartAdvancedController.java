package io.github.funkynoodles;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FXMLExpensePieChartAdvancedController {
    @FXML
    private CheckBox percentageCheckBox;
    @FXML
    private CheckBox valueCheckBox;
    @FXML
    private VBox vBox;

    private BorderPane borderPane;

    @FXML
    protected void onPercentageCheckBoxToggle() {
        borderPane = (BorderPane) vBox.getParent();
        // Rounding
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        PieChart pieChart = (PieChart) borderPane.getCenter();
        if (percentageCheckBox.isSelected()) {
            // If just checked
            double totalAmount = 0;
            // Find totalAmount
            for (PieChart.Data d : pieChart.getData()) {
                totalAmount += d.getPieValue();
            }
            // Find percentages, operations are slightly different depending on the presence of the value
            if (valueCheckBox.isSelected()) {
                for (PieChart.Data d : pieChart.getData()) {
                    int newLineIndex = d.getName().indexOf("\n");
                    String percentage = df.format(d.getPieValue() * 100 / totalAmount) + "%";
                    d.setName(d.getName().substring(0, newLineIndex + 1) + percentage + " " + d.getName().substring(newLineIndex + 1));
                }
            } else {
                for (PieChart.Data d : pieChart.getData()) {
                    String percentage = df.format(d.getPieValue() * 100 / totalAmount) + "%";
                    d.setName(d.getName() + "\n" + percentage);
                }
            }
        } else {
            // If deselected
            if (valueCheckBox.isSelected()) {
                for (PieChart.Data d : pieChart.getData()) {
                    int newLineIndex = d.getName().indexOf("\n");
                    String value = df.format(d.getPieValue());
                    d.setName(d.getName().substring(0, newLineIndex + 1) + value);
                }
            } else {
                for (PieChart.Data d : pieChart.getData()) {
                    int newLineIndex = d.getName().indexOf("\n");
                    d.setName(d.getName().substring(0, newLineIndex));
                }
            }
        }
    }

    @FXML
    protected void onValueCheckBoxToggle() {
        borderPane = (BorderPane) vBox.getParent();
        // Rounding
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        PieChart pieChart = (PieChart) borderPane.getCenter();
        if (valueCheckBox.isSelected()) {
            if (percentageCheckBox.isSelected()) {
                for (PieChart.Data d : pieChart.getData()) {
                    String value = df.format(d.getPieValue());
                    d.setName(d.getName() + " " + value);
                }
            } else {
                for (PieChart.Data d : pieChart.getData()) {
                    String value = df.format(d.getPieValue());
                    d.setName(d.getName() + "\n" + value);
                }
            }
        } else {
            if (percentageCheckBox.isSelected()) {
                for (PieChart.Data d : pieChart.getData()) {
                    int percentSignIndex = d.getName().indexOf("%");
                    d.setName(d.getName().substring(0, percentSignIndex + 1));
                }
            } else {
                for (PieChart.Data d : pieChart.getData()) {
                    int newLineIndex = d.getName().indexOf("\n");
                    d.setName(d.getName().substring(0, newLineIndex));
                }
            }
        }
    }

    @FXML
    public void initialize() {
    }
}
