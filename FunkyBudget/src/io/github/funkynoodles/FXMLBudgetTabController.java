package io.github.funkynoodles;

import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class FXMLBudgetTabController {

	@FXML TreeTableView<WeekBudgetData> treeTableView;
	@FXML TreeTableColumn<WeekBudgetData, String> firstColumn;

	@FXML
	public void initialize(){

	}
}
