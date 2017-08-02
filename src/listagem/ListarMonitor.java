package listagem;

import cadastro.CadastroMonitor;
import com.jfoenix.controls.JFXButton;
import entidade.Monitor;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistencia.Dao;

public class ListarMonitor extends Application {

    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private JFXButton bEditar;
    private JFXButton bRemover;
    private JFXButton bSair;
    private static Stage stage;

    private Monitor monitor;

    TableColumn colunaId;
    TableColumn colunaNome;

    List<Monitor> monitores = Dao.listar(Monitor.class);
    ObservableList<Monitor> listItens = FXCollections.observableArrayList(monitores);

    public void setMonitor(Monitor m) {
        monitor = m;
    }

    @Override
    public void start(Stage parent) {
        initComponents();

        initListeners();
        initLayout();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Tabela Livro");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        stage.showAndWait();
    }

    public void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(795, 445);

        txPesquisa = new TextField();
        txPesquisa.setPromptText("Pesquisar");

        bEditar = new JFXButton("Editar");

        bRemover = new JFXButton("Remover");

        bSair = new JFXButton("Sair");

        //responsaveis = Dao.listar(Responsavel.class);
        tabela = new TableView<>();
        colunaId = new TableColumn<>("Id");
        colunaNome = new TableColumn<>("Nome");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tabela.setItems(listItens);
        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela

        tabela.getColumns().addAll(colunaId, colunaNome);
        pane.getChildren().addAll(tabela, txPesquisa, bSair);
        if (monitor.getSupervisor()) {
            pane.getChildren().add(bRemover);
            pane.getChildren().add(bEditar);
        }
    }

    public void initLayout() {
        bSair.setLayoutX(10);
        bSair.setLayoutY(10);
        bEditar.setLayoutX(50);
        bEditar.setLayoutY(10);
        bRemover.setLayoutX(100);
        bRemover.setLayoutY(10);

        tabela.setLayoutX(10);
        tabela.setLayoutY(45);
        txPesquisa.setLayoutX(645);
        txPesquisa.setLayoutY(10);
    }

    public void initListeners() {
        FilteredList<Monitor> filteredData = new FilteredList<>(listItens, (e) -> true);
        txPesquisa.setOnKeyReleased((e) -> {
            txPesquisa.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Monitor>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if ((user.getId() + "").contains(newValue)) {
                        return true;
                    } else if (user.getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if ((user.getNomeUsuario() + "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<Monitor> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabela.comparatorProperty());
            tabela.setItems(sortedData);
        });

        bSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarMonitor.stage.hide();
            }
        });

        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroMonitor cm = new CadastroMonitor();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    cm.setMonitor((Monitor) tabela.getSelectionModel().getSelectedItem());

                    try {
                        cm.start(ListarMonitor.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listItens.setAll(Dao.listar(Monitor.class));
                    tabela.requestFocus();
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });

        bRemover.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    if (new Alert(Alert.AlertType.NONE, "Tem certeza que deseja remover o item selecionado?", ButtonType.CANCEL, ButtonType.YES).showAndWait().get().equals(ButtonType.YES)) {
                        if (!monitor.equals((Monitor) tabela.getSelectionModel().getSelectedItem())) {
                            try {
                                Dao.remover((Monitor) tabela.getSelectionModel().getSelectedItem());
                            } catch (SQLIntegrityConstraintViolationException ex) {
                                Logger.getLogger(ListarMonitor.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            listItens.setAll(Dao.listar(Monitor.class));
                            tabela.requestFocus();
                        }else{
                            new Alert(Alert.AlertType.NONE, "Você não pode se auto remover!", ButtonType.OK).showAndWait();
                            tabela.requestFocus();
                            
                        }
                    }
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
