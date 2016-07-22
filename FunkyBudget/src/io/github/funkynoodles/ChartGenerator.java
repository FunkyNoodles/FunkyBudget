package io.github.funkynoodles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class ChartGenerator {

	public static void generateExpensePieChart(BorderPane borderPane, Asset asset, LocalDate fromDate, LocalDate toDate){
		ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
		double[] data = new double[Category.values().length];
		for (int i = 0; i < data.length; i++) {
			data[i] = 0.0;
		}
		TransferField tf;
		for (int i = 0; i < asset.size(); i++) {
			tf = asset.getTransferField().get(i);
			if (tf.getCategoryStr().contains("Expense:") && isBetweenDatesInclusive(tf.getDate(), fromDate, toDate)) {
				Category c = tf.getCategory();
				data[c.ordinal()] += Math.abs(tf.getAmount());
			}
		}
		// If not zero, then add entry
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0.0) {
				chartData.add(new PieChart.Data(EnumUtils.categoryMap.get(Category.values()[i]).substring(8) ,Math.abs(data[i])));
			}
		}
		// Setup Chart Info
		((Label)borderPane.lookup("#infoXScaleLabel")).setText("N/A");
		// Create pie chart
		PieChart pieChart = new PieChart(chartData);
		pieChart.setTitle("Expense by Category from " + fromDate.toString() + " to " + toDate.toString());
		pieChart.setLegendSide(Side.LEFT);
		pieChart.setLabelLineLength(20);
		borderPane.setCenter(pieChart);
	}

	public static void generateExpenseOverTimeChart(BorderPane borderPane, boolean resetXScale, String xScale) {
		String assetName = ((Label)borderPane.lookup("#infoAssetSelectedLabel")).getText();
		String fromDateStr = ((Label)borderPane.lookup("#infoFromDateLabel")).getText();
		String toDateStr = ((Label)borderPane.lookup("#infoToDateLabel")).getText();

		int assetIndex = Main.findAssetIndexByName(assetName);
		Asset asset = Main.assets.getAssetsList().get(assetIndex);
		LocalDate fromDate = LocalDate.parse(fromDateStr);
		LocalDate toDate = LocalDate.parse(toDateStr);

		generateExpenseOverTimeChart(borderPane, asset, fromDate, toDate, resetXScale, xScale);
	}

	/**
	 *
	 * @param asset
	 * @param fromDate
	 * @param toDate
	 * @param resetXScale true if wants to auto-generate xScale
	 * @param xScale scale used for x-axis
	 */
	public static void generateExpenseOverTimeChart(BorderPane borderPane, Asset asset, LocalDate fromDate, LocalDate toDate, boolean resetXScale, String xScale){
		if (resetXScale) {
			long days = ChronoUnit.DAYS.between(fromDate, toDate);
			// Set scale of the chart
			if (days <= 28) {
				xScale = Reference.CHART_X_AXIS_SCALE_DAILY;
			}else if (days <= 7 * 20) {
				xScale = Reference.CHART_X_AXIS_SCALE_WEEKLY;
			}else if (days <= 24 * 30) {
				xScale = Reference.CHART_X_AXIS_SCALE_MONTHLY;
			}else{
				xScale = Reference.CHART_X_AXIS_SCALE_YEARLY;
			}
		}
		generateExpenseOverTimeChart(borderPane, asset, fromDate, toDate, xScale);
	}

	private static void generateExpenseOverTimeChart(BorderPane borderPane, Asset asset, LocalDate fromDate, LocalDate toDate, String xScale){

		// Axes
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();


		// Set axis labels
		switch (xScale) {
		case Reference.CHART_X_AXIS_SCALE_DAILY:
			xAxis.setLabel("Time (Daily)");
			break;
		case Reference.CHART_X_AXIS_SCALE_WEEKLY:
			xAxis.setLabel("Time (Weekly)");
			break;
		case Reference.CHART_X_AXIS_SCALE_MONTHLY:
			xAxis.setLabel("Time (Monthly)");
			break;
		case Reference.CHART_X_AXIS_SCALE_YEARLY:
			xAxis.setLabel("Time (Yearly)");
			break;
		default:
			break;
		}
		yAxis.setLabel("Expenses");
		// Line Chart
		LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setTitle("Expense from " + fromDate.toString() + " to " + toDate.toString());
		// Generate total series
		XYChart.Series<String, Number> totalSeries = generateExpenseOverTimeSeries(asset, fromDate, toDate, "Expense:", 0, xScale);
		// Setup Chart Info
		((Label)borderPane.lookup("#infoXScaleLabel")).setText(xScale);
		// Setup Chart
		lineChart.getData().add(totalSeries);
		borderPane.setCenter(lineChart);
	}

	public static XYChart.Series<String, Number> generateExpenseOverTimeSeries(Asset asset, LocalDate fromDate, LocalDate toDate, String categoryFilter, int lineChartSize, String xScale){
		// Populate series
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Total Expense");
		// Collect data
		TransferField tf;
		Map<String, Double> data = new TreeMap<String, Double>();
		for (int i = 0; i < asset.size(); i++) {
			tf = asset.getTransferField().get(i);
			if (tf.getCategoryStr().contains(categoryFilter) && isBetweenDatesInclusive(tf.getDate(), fromDate, toDate)) {
				String dateStr = tf.getDateStr();
				if (data.containsKey(dateStr)) {
					data.put(dateStr, data.get(dateStr) + Math.abs(tf.getAmount()));
				}else{
					data.put(dateStr, Math.abs(tf.getAmount()));
				}
			}
		}
		LocalDate date;
		// Add series using options
		switch (xScale) {
		case Reference.CHART_X_AXIS_SCALE_DAILY:
			// Add to series
			for (String key : data.keySet()) {
				XYChart.Data<String, Number> d = new XYChart.Data<String, Number>(key, data.get(key));
				d.setNode(new ChartNode(d.getYValue(), lineChartSize));
				series.getData().add(d);
			}
			break;
		case Reference.CHART_X_AXIS_SCALE_WEEKLY:
			// Collapse data down to weekly
			Map<String, Double> weekData = new TreeMap<>();
			for (String key : data.keySet()) {
				date = LocalDate.parse(key);
				String dateStr = date.toString();
				DayOfWeek week = date.getDayOfWeek();
				int daysAfterSun = week.getValue() % 7;
				LocalDate sunday = date.minusDays(daysAfterSun);
				LocalDate wednesday = sunday.plusDays(3);
				String wednesdayStr = wednesday.toString();
				if (weekData.containsKey(wednesdayStr)) {
					weekData.put(wednesdayStr, weekData.get(wednesdayStr) + data.get(dateStr));
				}else{
					weekData.put(wednesdayStr, data.get(dateStr));
				}
			}
			// Add to series
			for (String key : weekData.keySet()) {
				XYChart.Data<String, Number> d = new XYChart.Data<String, Number>(key, weekData.get(key));
				d.setNode(new ChartNode(d.getYValue(), lineChartSize));
				series.getData().add(d);
			}
			break;
		case Reference.CHART_X_AXIS_SCALE_MONTHLY:
			// Collapse data down to monthly
			Map<String, Double> monthData = new TreeMap<String, Double>();
			for (String key : data.keySet()) {
				date = LocalDate.parse(key);
				String dateStr = date.toString();
				int year = date.getYear();
				Month month = date.getMonth();
				String monthStr = month.toString().substring(0, 3);
				String yearMonthStr = Integer.toString(year) + "-" + monthStr;
				if (monthData.containsKey(yearMonthStr)) {
					monthData.put(yearMonthStr, monthData.get(yearMonthStr) + data.get(dateStr));
				}else{
					monthData.put(yearMonthStr, data.get(dateStr));
				}
			}
			// Add to series
			for (String key : monthData.keySet()) {
				XYChart.Data<String, Number> d = new XYChart.Data<String, Number>(key, monthData.get(key));
				d.setNode(new ChartNode(d.getYValue(), lineChartSize));
				series.getData().add(d);
			}
			break;
		case Reference.CHART_X_AXIS_SCALE_YEARLY:
			// Collapse data down to yearly
			Map<String, Double> yearData = new TreeMap<>();
			for (String key : data.keySet()) {
				date = LocalDate.parse(key);
				String dateStr = date.toString();
				int year = date.getYear();
				String yearStr = Integer.toString(year);
				if (yearData.containsKey(yearStr)) {
					yearData.put(yearStr, yearData.get(yearStr) + data.get(dateStr));
				}else{
					yearData.put(yearStr, data.get(dateStr));
				}
			}
			// Add to series
			for (String key : yearData.keySet()) {
				XYChart.Data<String, Number> d = new XYChart.Data<String, Number>(key, yearData.get(key));
				d.setNode(new ChartNode(d.getYValue(), lineChartSize));
				series.getData().add(d);
			}
			break;
		default:
			break;
		}
		return series;
	}

	private static boolean isBetweenDatesInclusive(LocalDate date, LocalDate fromDate, LocalDate toDate){
		return !date.isAfter(toDate) && !date.isBefore(fromDate);
	}
}
