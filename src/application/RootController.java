package application;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import homebook.dao.HomeBookDAO;
import homebook.tools.ConnectionFactory;
import homebook.tools.TableViewFactory;
import homebook.vo.HomeBook;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class RootController implements Initializable {

	@FXML
	private SplitPane centerPanel;

	@FXML
	private TextField txtSerialno;

	@FXML
	private DatePicker txtDay;

	@FXML
	private RadioButton radioRevenue;

	@FXML
	private RadioButton radioExpense;

	@FXML
	private ComboBox<String> comboAccounttitle;

	@FXML
	private TextArea txtRemark;

	@FXML
	private TextField txtRevenue;

	@FXML
	private TextField txtExpense;

	@FXML
	private Button btnInsert;

	@FXML
	private Button btnUpdate;

	@FXML
	private Button btnDelete;

	@FXML
	private Button btnSelectByConditions;

	@FXML
	private Button btnSelectAll;

	@FXML
	private Button btnClear;

	@FXML
	private BorderPane contentPanel;

	// 추가한 컨트롤
	private TableView table;
	private HomeBookDAO dao;

	@FXML
	void clear(ActionEvent event) {
		txtSerialno.setText(null);
		txtDay.getEditor().setText(null);
		radioRevenue.setSelected(false);
		radioExpense.setSelected(false);
		comboAccounttitle.getSelectionModel().clearSelection();
		txtRemark.setText(null);
		txtRevenue.setText(null);
		txtExpense.setText(null);
	}

	@FXML
	void delete(ActionEvent event) {
		try {
			Map<String,String> map = (Map<String,String>)table.getSelectionModel().getSelectedItem();
			long serialno = Long.parseLong(map.get("SERIALNO"));
			dao.delete(serialno); //DB 반영
			int index = table.getSelectionModel().getSelectedIndex(); //선택된 번호 알아낼 수 있음
			table.getItems().remove(index); //UI 반영
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@FXML
	void insert(ActionEvent event) {
		try {
			dao.insert(getVo());// 디비에반영
			table.getItems().add(getMap()); // 유아이에 반영
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	HomeBook currentVo;

	private HomeBook getVo() {
		HomeBook vo = new HomeBook();
		//if(txtSerialno.getText()!=null) {
		try {
			vo.setSerialno(Long.parseLong(txtSerialno.getText()));
		} catch (NumberFormatException e) {}
		//}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = sdf.format(new java.util.Date());
		Timestamp ts = new Timestamp(0).valueOf(d);
		vo.setDay(ts);
//		LocalDate value = txtDay.getValue();
//		Timestamp timestamp = null;
//		if(value==null) {
//			timestamp = Timestamp.valueOf(value.toString());
//		}else if(value.toString().length()<= 10){
//			timestamp = Timestamp.valueOf(value.toString() + " 00:00:00");
//			vo.setDay(timestamp);
//		}
		
		// 수지구분정보
		if (radioRevenue.isSelected()) {
			vo.setSection("수입");
		} else {
			vo.setSection("지출");
		}
		// 계정과목
		String title = comboAccounttitle.getSelectionModel().getSelectedItem();
		vo.setAccounttitle(title);
		// 적요
		vo.setRemark(txtRemark.getText());
		// 수입금액
		try {
			vo.setRevenue(Long.parseLong(txtRevenue.getText()));
		} catch (NumberFormatException e1) {
			vo.setRevenue(0L);
		}
		// 지출금액
		try {
			vo.setExpense(Long.parseLong(txtExpense.getText()));
		} catch (NumberFormatException e) {
			vo.setExpense(0L);
		}
		currentVo = vo;
		return vo;
	}

	public long getMax() {
		long max = 0;
		try {
			max = dao.getMax();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return max;
	}

	private Map<String, String> getMap() {
		Map<String, String> map = new HashMap<>();
		currentVo.setSerialno(getMax());
		map.put("SERIALNO", currentVo.getSerialno()+"");
		map.put("DAY", currentVo.getDay().toString());
		map.put("SECTION", currentVo.getSection());
		map.put("ACCOUNTTITLE", currentVo.getAccounttitle());
		map.put("REMARK", currentVo.getRemark());
		map.put("REVENUE", currentVo.getRevenue() + "");
		map.put("EXPENSE", currentVo.getExpense() + "");
		return map;
	}
	
	private Map<String, String> getMap2() {
		Map<String, String> map = new HashMap<>();
		map.put("SERIALNO", currentVo.getSerialno()+"");
		map.put("DAY", currentVo.getDay().toString());
		map.put("SECTION", currentVo.getSection());
		map.put("ACCOUNTTITLE", currentVo.getAccounttitle());
		map.put("REMARK", currentVo.getRemark());
		map.put("REVENUE", currentVo.getRevenue() + "");
		map.put("EXPENSE", currentVo.getExpense() + "");
		return map;
	}
	

	@FXML
	void selectAll(ActionEvent event) {
		try {
			table = TableViewFactory.getTable("SELECT * FROM HOMEBOOK", 
					ConnectionFactory.create());
			setTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void selectByConditions(ActionEvent event) {
		String conditions = JOptionPane.showInputDialog("WHERE절로 ");
		try {
			table = TableViewFactory.getTable("SELECT * FROM HOMEBOOK "+conditions, 
					ConnectionFactory.create());
			setTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void update(ActionEvent event) {
		try {
			dao.update(getVo());// 디비에반영
			int index = table.getSelectionModel().getSelectedIndex();
			table.getItems().set(index, getMap2()); // 유아이에 반영
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dao = new HomeBookDAO();

		comboAccounttitle.getItems().addAll("급료", "상여금", "기타수입", "피복비", "주식비", "간식비", "외식비", "주택수선비", "소포품비", "주방용품비",
				"수도광열비", "도서인쇄비", "잡비");
		ToggleGroup group = new ToggleGroup();
		radioRevenue.setToggleGroup(group);
		radioExpense.setToggleGroup(group);
		//
		try {
			table = TableViewFactory.getTable("SELECT * FROM HOMEBOOK ", ConnectionFactory.create());
			setTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 새로운 테이블뷰가 만들어 지면 처리할 내용
	public void setTable() throws SQLException {

		table.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO 테이블의 한 행을 선택했을 때 그 행의 정보를
				// 읽어 입력ui에 나타내기
				try {
					Map<String, String> selected = (Map<String, String>) table.getSelectionModel().getSelectedItem();

					txtSerialno.setText(selected.get("SERIALNO"));
					txtDay.getEditor().setText(selected.get("DAY"));
					if (selected.get("SECTION").equals("수입")) {
						radioRevenue.setSelected(true);
						radioRevenue.requestFocus();
					} else {
						radioExpense.setSelected(true);
						radioExpense.requestFocus();
					}
					comboAccounttitle.getSelectionModel().select(selected.get("ACCOUNTTITLE"));
					txtRemark.setText(selected.get("REMARK"));
					txtRevenue.setText(selected.get("REVENUE"));
					txtExpense.setText(selected.get("EXPENSE"));

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}

			}

		});
		contentPanel.setCenter(table);
	}

}
