module edward.audit {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.jetbrains.annotations;

    opens edward.audit to javafx.fxml;
    exports edward.audit;
}