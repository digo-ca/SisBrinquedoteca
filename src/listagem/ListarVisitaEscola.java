package listagem;

import app.ItemCrianca;
import app.ItemVisitaEscola;
import cadastro.CadastroVisitacaoEscola;
import com.jfoenix.controls.JFXButton;
import entidade.Crianca;
import entidade.Monitor;
import entidade.Visita;
import entidade.VisitacaoEscola;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import persistencia.Dao;

public class ListarVisitaEscola extends Application{
    private AnchorPane pane;
    private TextField txPesquisa;
    private TableView tabela;
    private JFXButton bEditar;
    private JFXButton bRemover;
    private JFXButton bSair;
    private static Stage stage;
    
    private Monitor monitor;

    TableColumn colunaId;
    TableColumn colunaData;
    TableColumn colunaPeriodo;
    TableColumn colunaMonitor;
    TableColumn colunaEscola;

    List<VisitacaoEscola> visitas = Dao.listar(VisitacaoEscola.class);
    ObservableList<VisitacaoEscola> listItens = FXCollections.observableArrayList(visitas);
    
    public void setMonitor(Monitor m){
        monitor = m;
    }

    @Override
    public void start(Stage parent) {
        initComponents();
//        initValues();
        initListeners();
        initLayout();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Tabela no JavaFX");
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
        
        tabela = new TableView<>();
        colunaId = new TableColumn<>("Id");
        colunaData = new TableColumn<>("Data");
        colunaPeriodo = new TableColumn<>("Período");
        colunaMonitor = new TableColumn<>("Monitor");
        colunaEscola = new TableColumn<>("Escola");

        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colunaPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        colunaMonitor.setCellValueFactory(new PropertyValueFactory<>("monitor"));
        colunaEscola.setCellValueFactory(new PropertyValueFactory<>("escola"));
        
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colunaData.setCellFactory(column -> {
            return new TableCell<VisitacaoEscola, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        
        

        tabela.setPrefSize(785, 400);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //Colunas se posicionam comforme o tamanho da tabela
        tabela.setItems(listItens);

        tabela.getColumns().addAll(colunaId, colunaData, colunaPeriodo, colunaMonitor, colunaEscola);
        pane.getChildren().addAll(tabela, txPesquisa, bSair, bEditar);
        if(monitor.getSupervisor())
            pane.getChildren().add(bRemover);
    }
    
//    private void initValues(){
//        visitas = FXCollections.observableArrayList(Dao.listar(VisitacaoEscola.class));
//        tabela.setItems(visitas);
//        tabela.refresh();
//    }

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
        FilteredList<VisitacaoEscola> filteredData = new FilteredList<>(listItens, (e) -> true);
        txPesquisa.setOnKeyReleased((e) -> {
            txPesquisa.textProperty().addListener((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super VisitacaoEscola>) user -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if ((user.getId() + "").contains(newValue)) {
                        return true;
                    } else if (user.getEscola().getNome().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if ((user.getMonitor()+ "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if ((user.getProfessor()+ "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if ((user.getPeriodo()+ "").toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            SortedList<VisitacaoEscola> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabela.comparatorProperty());
            tabela.setItems(sortedData);
        });
        bSair.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ListarVisitaEscola.stage.hide();
            }
        });

        bEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroVisitacaoEscola cv = new CadastroVisitacaoEscola();
                if (tabela.getSelectionModel().getSelectedIndex() != -1) {
                    cv.setVisitaEscola((VisitacaoEscola) tabela.getSelectionModel().getSelectedItem());
                    try {
                        cv.start(ListarVisitaEscola.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarVisitaEscola.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listItens.setAll(Dao.listar(VisitacaoEscola.class));
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
                        try {
                            Dao.remover((VisitacaoEscola) tabela.getSelectionModel().getSelectedItem());
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            Logger.getLogger(ListarVisitaEscola.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        listItens.setAll(Dao.listar(VisitacaoEscola.class));
                    }
                } else {
                    new Alert(Alert.AlertType.NONE, "Selecione um item na tabela!", ButtonType.OK).showAndWait();
                    tabela.requestFocus();
                }
            }
        });
        tabela.setRowFactory(tv -> {
            TableRow<VisitacaoEscola> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ItemVisitaEscola.setVisita(row.getItem());
                    try {
                        new ItemVisitaEscola().start(ListarVisitaEscola.stage);
                    } catch (Exception ex) {
                        Logger.getLogger(ListarVisitaEscola.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listItens.setAll(Dao.listar(VisitacaoEscola.class));
                }

            });
            return row;
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
