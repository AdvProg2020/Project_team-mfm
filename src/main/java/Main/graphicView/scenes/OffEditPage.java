package Main.graphicView.scenes;

import Main.graphicView.GraphicMain;
import Main.model.requests.EditOffRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class OffEditPage {


    public static final String FXML_PATH = "src/main/sceneResources/SellerPanel/offEditPage.fxml";
    public static final String TITLE = "Off Edit Page";

    @FXML
    private TextField startDate;
    @FXML
    private TextField endDate;
    @FXML
    private TextField offAmount;
    @FXML
    private TextField productIdToBeAdded;
    @FXML
    private TextField productIdToBeRemoved;

    public void submitEdits(){
        try {
            EditOffRequest editOffRequest = GraphicMain.sellerController.getOffToEdit(SellerOffsPage.selectedOff);
            if(!startDate.getText().isEmpty()){
                editOffRequest.addEditedFieldTitle("start date");
                editOffRequest.setStartDate(startDate.getText());
            }
            if(!endDate.getText().isEmpty()){
                editOffRequest.addEditedFieldTitle("end date");
                editOffRequest.setStartDate(endDate.getText());
            }
            if(!offAmount.getText().isEmpty()){
                editOffRequest.addEditedFieldTitle("off amount");
                editOffRequest.setStartDate(offAmount.getText());
            }
            if(!productIdToBeAdded.getText().isEmpty()){
                editOffRequest.addEditedFieldTitle("add product");
                editOffRequest.setStartDate(productIdToBeAdded.getText());
            }
            if(!productIdToBeRemoved.getText().isEmpty()){
                editOffRequest.addEditedFieldTitle("remove product");
                editOffRequest.setStartDate(productIdToBeRemoved.getText());
            }
            GraphicMain.sellerController.submitOffEdits(editOffRequest);
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }

    public void goBack(){
        GraphicMain.graphicMain.back();
    }

    public void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();
    }
}