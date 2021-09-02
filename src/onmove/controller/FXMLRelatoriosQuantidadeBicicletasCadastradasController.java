package onmove.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import onmove.model.database.Database;
import onmove.model.database.DatabaseFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import onmove.model.dao.BicicletaDAO;
import onmove.model.domain.Bicicleta;

public class FXMLRelatoriosQuantidadeBicicletasCadastradasController implements Initializable {
    @FXML
    private TableView<Bicicleta> tableViewBicicletas;
    @FXML
    private TableColumn<Bicicleta, Integer> tableColumnBicicletaCodigo;
    @FXML
    private TableColumn<Bicicleta, String> tableColumnBicicletaNome;
    @FXML
    private TableColumn<Bicicleta, String> tableColumnBicicletaMarca;
    @FXML
    private TableColumn<Bicicleta, String> tableColumnBicicletaModelo;
    @FXML
    private TableColumn<Bicicleta, Double> tableColumnBicicletaPreco;
    @FXML
    private TableColumn<Bicicleta, Integer> tableColumnBicicletaQuantidade;
    @FXML
    private TableColumn<Bicicleta, Double> tableColumnBicicletaCategoria;
    @FXML
    private Button buttonImprimir;
    
    private List<Bicicleta> listBicicletas;
    private ObservableList<Bicicleta> observableListBicicletas;
    
    // Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final BicicletaDAO bicicletaDAO = new BicicletaDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bicicletaDAO.setConnection(connection);
        carregarTableViewBicicletas();
    }    
    
    public void carregarTableViewBicicletas(){
        tableColumnBicicletaCodigo.setCellValueFactory(new PropertyValueFactory<>("cdBicicleta"));
        tableColumnBicicletaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnBicicletaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tableColumnBicicletaModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tableColumnBicicletaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tableColumnBicicletaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        
        listBicicletas = bicicletaDAO.listar();
        
        observableListBicicletas = FXCollections.observableList(listBicicletas);
        tableViewBicicletas.setItems(observableListBicicletas);

    }
    
    public void handleImprimir() throws JRException{
        URL url = getClass().getResource("/onmove/relatorios/FXMLRelatoriosQuantidadeBicicletasCadastradas.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);//null: caso não existam filtros
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);//false: não deixa fechar a aplicação principal
        jasperViewer.setVisible(true);
    }
}
