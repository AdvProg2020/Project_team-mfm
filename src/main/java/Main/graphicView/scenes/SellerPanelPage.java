package Main.graphicView.scenes;

import Main.graphicView.GraphicMain;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SellerPanelPage implements Initializable{

    @FXML
    private Label personalInfoLabel;
    @FXML
    private Label titleLabel;

    public static final String FXML_PATH = "src/main/sceneResources/SellerPanel/sellerPanelPage.fxml";
    public static final String TITLE = "Seller Panel";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personalInfoLabel.setText("role: seller\n" + GraphicMain.generalController.viewPersonalInfo());
    }

    public void goToEditInfoPage() throws IOException {
        GraphicMain.graphicMain.goToPage(EditSellerPersonalInformationPage.FXML_PATH,
                EditSellerPersonalInformationPage.TITLE);
    }

    public void goBack(){
        GraphicMain.graphicMain.back();
    }

    public void viewCompanyInformation(){
        titleLabel.setText("company information");
        personalInfoLabel.setText(GraphicMain.sellerController.viewCompanyInformation());
    }

    public void goToSalesHistoryPage() throws IOException {
        GraphicMain.graphicMain.goToPage(SalesHistoryPage.FXML_PATH,SalesHistoryPage.TITLE);
    }

    public void viewBalance(){
        showInformationAlert(GraphicMain.sellerController.viewSellerBalance());
    }

    public void showInformationAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void goToSellerProductsPage() throws IOException {
        GraphicMain.graphicMain.goToPage(SellerProductsPage.FXML_PATH,SellerProductsPage.TITLE);
    }
}
