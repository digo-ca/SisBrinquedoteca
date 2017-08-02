package cadastro;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import entidade.Escola;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import persistencia.Dao;

/**
 *
 * @author Ivanildo
 */
public class CadastroEscola  extends Application{
    private AnchorPane pane;
    private static Stage stage;
    
    private Label lNome;
    private Label lEndereco;
    private Label lResponsavel;
    private Label lCgResponsavel;
    private Label lTelefone;
    
    private JFXTextField txNome;
    private JFXTextField txEndereco;
    private JFXTextField txResponsavel;
    private JFXTextField txCgResponsavel;
    private JFXTextField txTelefone;
    
    private JFXButton btCadastrar;
    private JFXButton btCancelar;
    private Escola escola;
    
    public void setEscola(Escola e){
        escola = e;
    }
    
    @Override
    public void start(Stage parent) throws Exception {
        initComponents();
        initListeners();
        initLayout();
        
        if(escola != null){
            preencheTela();
        }
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("css/style.css");
        stage.setTitle("Cadastro Escola");
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parent);
        
        //CadastroEscola.stage = stage;
        stage.showAndWait();
    }
    
    private void initComponents() {
        stage = new Stage();
        pane = new AnchorPane();
        pane.setPrefSize(550, 290);
        pane.getStyleClass().add("pane");
        
        txNome = new JFXTextField();
        txNome.setPrefWidth(220);
        txNome.setPromptText("Nome");
        txNome.setLabelFloat(true);
        pane.getChildren().add(txNome);
        txResponsavel = new JFXTextField();
        txResponsavel.setPrefWidth(220);
        txResponsavel.setPromptText("Nome do Responsável");
        txResponsavel.setLabelFloat(true);
        pane.getChildren().add(txResponsavel);
        txCgResponsavel = new JFXTextField();
        txCgResponsavel.setPromptText("Cargo do Responável");
        txCgResponsavel.setLabelFloat(true);
        pane.getChildren().add(txCgResponsavel);
        txTelefone = new JFXTextField();
        txTelefone.setPromptText("Telefone");
        txTelefone.setLabelFloat(true);
        pane.getChildren().add(txTelefone);
        txEndereco = new JFXTextField();
        txEndereco.setPrefWidth(220);
        txEndereco.setPromptText("Endereço");
        txEndereco.setLabelFloat(true);
        pane.getChildren().add(txEndereco);
        
        btCadastrar = new JFXButton("Cadastrar");
        btCadastrar.getStyleClass().add("btCadastrar");
        pane.getChildren().add(btCadastrar);
        btCancelar = new JFXButton("Cancelar");
        btCancelar.getStyleClass().add("btCancelar");
        pane.getChildren().add(btCancelar);
        
    }
    private void initLayout(){
        txNome.setLayoutX(10);
        txNome.setLayoutY(20);
        txResponsavel.setLayoutX(10);
        txResponsavel.setLayoutY(70);
        txCgResponsavel.setLayoutX(10);
        txCgResponsavel.setLayoutY(120);
        txTelefone.setLayoutX(290);
        txTelefone.setLayoutY(20);
        txEndereco.setLayoutX(290);
        txEndereco.setLayoutY(70);
        
        
        btCadastrar.setLayoutX(485);
        btCadastrar.setLayoutY(275);
        btCancelar.setLayoutX(415);
        btCancelar.setLayoutY(275);
    }
    private void initListeners() {
        btCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CadastroEscola.getStage().hide();
            }
        });

        btCadastrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(escola == null)
                    escola = new Escola();
                
                escola.setNome(txNome.getText());
                escola.setEndereco(txEndereco.getText());
                escola.setResponsavel(txResponsavel.getText());
                escola.setCargoResponsavel(txCgResponsavel.getText());
                escola.setTelefone(txTelefone.getText());
                
                Dao.salvar(escola);
                CadastroEscola.getStage().hide();
            }
        });
    }
    
    public void preencheTela(){
        txNome.setText(escola.getNome());
        txEndereco.setText(escola.getEndereco());
        txResponsavel.setText(escola.getResponsavel());
        txCgResponsavel.setText(escola.getCargoResponsavel());
        txTelefone.setText(escola.getTelefone());
    }
    
    public static Stage getStage(){
        return stage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
