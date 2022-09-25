module FitnessBusinessLogic {
    requires java.sql;
    requires org.postgresql.jdbc;
    requires javafx.base;
    requires lombok;
    exports com.BusinessLogic;
    exports com.BindingModels;
    exports com.ViewModels;
    exports com.Interfaces;
}