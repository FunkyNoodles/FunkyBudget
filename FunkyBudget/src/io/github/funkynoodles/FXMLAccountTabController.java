package io.github.funkynoodles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class FXMLAccountTabController {

	// Top
	@FXML private Label assetNameLabel;
	@FXML private TextField assetBalanceText;

	// Add field
	@FXML private HBox hbox;
	@FXML private TextField amountText;
	@FXML private ComboBox<String> comboBox;
	@FXML private Button btn;


	// Table
	@FXML private TableView<TransferField> tableView;
	@FXML private TableColumn<TransferField, String> dateCol;
	@FXML private TableColumn<TransferField, String> detailCol;
	@FXML private TableColumn<TransferField, String> categoryCol;
	@FXML private TableColumn<TransferField, String> amountCol;
	@FXML private TableColumn<TransferField, String> balanceCol;



	@FXML
    protected void handleAddButton(final ActionEvent event) {
		Scene scene = btn.getScene();
    	DatePicker datePicker = (DatePicker)hbox.getChildren().get(0);
    	String amountStr = amountText.getText();
    	TextField detailsTextField = (TextField)hbox.getChildren().get(2);
    	String details = detailsTextField.getText();
    	double amountNum = 0.0;
    	LocalDate date = datePicker.getValue();
    	String c = comboBox.getValue();

    	try{
			amountNum = Double.parseDouble(amountStr);
    		date.toString();
    	}catch (NullPointerException e){
    		Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Date");
			errorBox.setContentText("Please Enter a Valid Date");
			errorBox.showAndWait();
			return;
    	}catch (NumberFormatException e){
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Number");
			errorBox.setContentText("Please Enter a Valid Number");
			errorBox.showAndWait();
			return;
    	}
    	if(c == null){
    		Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setTitle("Error");
			errorBox.setHeaderText("Invalid Category");
			errorBox.setContentText("Please Select a Category");
			errorBox.showAndWait();
			return;
    	}
    	amountText.clear();
    	detailsTextField.clear();

    	TransferField tf = new TransferField(date, amountNum, details, EnumUtils.categoryMap.inverse().get(c));
    	Asset asset;
    	TabPane tabPane = (TabPane) scene.lookup("#tabPane");
    	Tab tab = tabPane.getSelectionModel().getSelectedItem();
    	String tabName = tab.getText();

    	int assetIndex = Main.findAssetIndexByName(tabName);
    	asset = Main.assets.getAssetsList().get(assetIndex);
    	asset.insert(tf);
    	asset.updateBalance();

		// Update balance on home tab and top
		Tab tabAsset = null;
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			if(tabPane.getTabs().get(i).getId().compareTo("rootTabAsset") == 0){
				tabAsset = tabPane.getTabs().get(i);
				break;
			}
		}
		VBox rootTabVBox = (VBox)tabAsset.getContent();
		HBox accountFieldHBox = null;
		Label accountFieldLabel = null;
		TextField accountFieldTextField = null;

		for (int i = 1; i < rootTabVBox.getChildren().size() - 1; i++) {
			accountFieldHBox = (HBox)rootTabVBox.getChildren().get(i);
			accountFieldLabel = (Label)accountFieldHBox.getChildren().get(0);
			if(accountFieldLabel.getText() == tabName){
				accountFieldTextField = (TextField) accountFieldHBox.getChildren().get(1);
				accountFieldTextField.setText(asset.getBalanceStr());
			}
		}
		assetBalanceText.setText(asset.getBalanceStr());

		Main.changed = true;
    }

	@FXML
	protected void handleKeyInput(final InputEvent event){
		if (event instanceof KeyEvent)
		{
			final KeyEvent keyEvent = (KeyEvent) event;
			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.ENTER){
			}
		}
	}

	@FXML
	protected void initialize(){
		EnumUtils.populateCategoryComboBox(comboBox, null);

		// Date Column
		dateCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("dateStr"));
		Callback<TableColumn<TransferField, String>, TableCell<TransferField, String>> dateCellFactory = (TableColumn<TransferField, String> param) -> new DateEditingCell();
		dateCol.setCellFactory(dateCellFactory);
		dateCol.setOnEditCommit(
				new EventHandler<CellEditEvent<TransferField, String>>(){
					@Override
					public void handle(CellEditEvent<TransferField, String> e){
						TransferField tf = (TransferField) e.getTableView().getItems().get(e.getTablePosition().getRow());
						tf.setDate(LocalDate.parse(e.getNewValue()));
						Main.changed = true;
					}
				});


		// Detail Column
		detailCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("detailStr"));
		detailCol.setCellFactory(TextFieldTableCell.forTableColumn());
		detailCol.setOnEditCommit(
				new EventHandler<CellEditEvent<TransferField, String>>(){
					@Override
					public void handle(CellEditEvent<TransferField, String> e) {
						((TransferField) e.getTableView().getItems().get(e.getTablePosition().getRow())).setDetail(e.getNewValue());
						Main.changed = true;
					}
				});

		// Category Column
		categoryCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("categoryStr"));
		// Populate category list
		ObservableList<String> categoryList = FXCollections.observableArrayList();
		for(Category c : Category.values()){
			categoryList.add(EnumUtils.categoryMap.get(c));
		}
		categoryCol.setCellFactory(ComboBoxTableCell.forTableColumn(categoryList));
		categoryCol.setOnEditCommit(new EventHandler<CellEditEvent<TransferField, String>>(){
			@Override
			public void handle(CellEditEvent<TransferField, String> e) {
				String categoryStr = e.getNewValue();
				TransferField tf = (TransferField) e.getTableView().getItems().get(e.getTablePosition().getRow());
				tf.setCategory(EnumUtils.categoryMap.inverse().get(categoryStr));
				Main.changed = true;
			}
		});

		// Amount Column
		amountCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("amountStr"));
		amountCol.setCellFactory(TextFieldTableCell.forTableColumn());
		amountCol.setOnEditCommit(new EventHandler<CellEditEvent<TransferField, String>>(){
			@Override
			public void handle(CellEditEvent<TransferField, String> e) {
				String amountStr = e.getNewValue();
				Double amount = 0.0;
				try {
					amount = Double.parseDouble(amountStr);
				} catch (NumberFormatException e2) {
					Alert errorBox = new Alert(AlertType.ERROR);
					errorBox.setTitle("Error");
					errorBox.setHeaderText("Invalid Number");
					errorBox.setContentText("Please Enter a Valid Number");
					errorBox.showAndWait();
					// For refreshing, a workaround
					amountCol.setVisible(false);
					amountCol.setVisible(true);
					return;
				}
				int assetIndex = Main.findAssetIndexByName(assetNameLabel.getText());
				Asset asset = Main.assets.getAssetsList().get(assetIndex);
				TransferField tf = (TransferField) e.getTableView().getItems().get(e.getTablePosition().getRow());
				tf.setAmount(amount);
				asset.updateBalance();
				assetBalanceText.setText(asset.getBalanceStr());
				// Update home tab balance
				Scene scene = assetBalanceText.getScene();
				Tab tabAsset = null;
				TabPane tabPane = (TabPane) scene.lookup("#tabPane");
				for (int i = 0; i < tabPane.getTabs().size(); i++) {
					if(tabPane.getTabs().get(i).getId().compareTo("rootTabAsset") == 0){
						tabAsset = tabPane.getTabs().get(i);
						break;
					}
				}
				VBox rootTabVBox = (VBox)tabAsset.getContent();
				HBox accountFieldHBox = null;
				Label accountFieldLabel = null;
				TextField accountFieldTextField = null;

				for (int i = 1; i < rootTabVBox.getChildren().size() - 1; i++) {
					accountFieldHBox = (HBox)rootTabVBox.getChildren().get(i);
					accountFieldLabel = (Label)accountFieldHBox.getChildren().get(0);
					if(accountFieldLabel.getText() == assetNameLabel.getText()){
						accountFieldTextField = (TextField) accountFieldHBox.getChildren().get(1);
						accountFieldTextField.setText(asset.getBalanceStr());
					}
				}

				// For refreshing, a workaround
				amountCol.setVisible(false);
				amountCol.setVisible(true);
				balanceCol.setVisible(false);
				balanceCol.setVisible(true);

				Main.changed = true;
			}
		});
		balanceCol.setCellValueFactory(new PropertyValueFactory<TransferField, String>("balanceStr"));
	}

	// Date editing cell
	class DateEditingCell extends TableCell<TransferField, String> {

        private DatePicker datePicker;

        private DateEditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createDatePicker();
                setText(null);
                setGraphic(datePicker);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getDate().toString());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (datePicker != null) {
                        datePicker.setValue(getDate());
                    }
                    setText(null);
                    setGraphic(datePicker);
                } else {
                    setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    setGraphic(null);
                }
            }
        }

        private void createDatePicker() {
            datePicker = new DatePicker(getDate());
            datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            datePicker.setOnAction((e) -> {
                System.out.println("Committed: " + datePicker.getValue().toString());
                commitEdit(datePicker.getValue().toString());
            });
//            datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                }
//            });
        }

        private LocalDate getDate() {
            return getItem() == null ? LocalDate.now() : LocalDate.parse(getItem());
        }
    }
}
