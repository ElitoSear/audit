package edward.audit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    @FXML
    public Label label;
    @FXML
    public Button button;
    @FXML
    public Label warnings;

    @FXML
    public void onUpdateButton(ActionEvent actionEvent) {

        label.setText("Efectivo: 0.0");
        warnings.setText("");
        String content = "";

        try {
            content = Files.readString(Path.of("C:/ProgramData/respair/queries.ini"));
        } catch (IOException e) {
            System.out.println("File not found: C:/ProgramData/respair/queries.ini");
            warnings.setText("File not found: C:/ProgramData/respair/queries.ini");
            return;
        }

        String[] lines = content.split("\n");

        Pattern cashPattern = Pattern.compile("update\\s*cajero\\s*set\\s*efectivo=(\\d+\\.\\d+),\\s*tarjeta=\\d+\\.\\d+,\\s*cancelado=\\d+\\.\\d+,\\s*propina=(\\d+\\.\\d+)");
        Pattern purcahsePattern = Pattern.compile("update\\s*encargado\\s*set\\s*efectivo=(\\d+\\.\\d+),\\s*credito=\\d+\\.\\d+.*");

        double cash = 0.0;
        double purchase = 0.0;
        double tip = 0.0;

        boolean foundCash = false;
        boolean foundPurchase = false;

        for (String line : lines) {
            Matcher cashMatcher = cashPattern.matcher(line);
            Matcher purchaseMatcher = purcahsePattern.matcher(line);

            if (cashMatcher.find()) {
                cash = Double.parseDouble(cashMatcher.group(1));
                tip = Double.parseDouble(cashMatcher.group(2));
                foundCash = true;
            }

            if (purchaseMatcher.find()) {
                purchase = Double.parseDouble(purchaseMatcher.group(1));
                foundPurchase = true;
            }

        }

        if (!foundCash) {
            warnings.setText("Por favor, cobra una cuenta con efectivo para conocer el total.");
            return;
        }
        if (!foundPurchase) {
            warnings.setText("Por favor, haz una compra a efectivo para conocer el total.");
            return;
        }

        label.setText("Efectivo: " + (cash - (purchase + tip)));
    }
}
