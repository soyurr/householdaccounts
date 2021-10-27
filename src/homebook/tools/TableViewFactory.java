package homebook.tools;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * 질의나 VO클래스로 테이블뷰어를 만들어 주는 클래스
 * 
 * @author 성주원
 *
 */
public class TableViewFactory {

	/**
	 * VO 클래스의 필드를 참조로 하여 테이블뷰의 컬럼을 만들어 붙여 주는 메소드
	 * 
	 * @param table   컬럼을 붙일 테이블
	 * @param voClazz 컬럼에 반영시킬 vo 클래스
	 */
	public static void setTable(TableView table, Class voClazz) {
		Field[] fields = voClazz.getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		TableColumn[] columns = new TableColumn[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
			columns[i] = new TableColumn(fieldNames[i].toUpperCase());
			columns[i].setCellValueFactory(new PropertyValueFactory(fieldNames[i]));
		}
		table.getColumns().addAll(columns);
	}

	/**
	 * VO클래스로 테이블뷰를 만들어 리턴시켜준다.
	 * 
	 * @param voClazz 테이블뷰와 매핑되는 VO클래스
	 * @return 테이블뷰생성하여 리턴시켜 준다.
	 */
	public static TableView getTable(Class voClazz) {
		TableView table = new TableView();
		Field[] fields = voClazz.getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		TableColumn[] columns = new TableColumn[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
			columns[i] = new TableColumn(fieldNames[i].toUpperCase());
			columns[i].setCellValueFactory(new PropertyValueFactory(fieldNames[i]));
		}
		table.getColumns().addAll(columns);
		return table;
	}

	/**
	 * 질의를 받아 수행시킨후 TableView객체를 생성시켜 리턴시켜주는 메소드
	 * 
	 * @param sql  테이블뷰어에 반영시킬 질의
	 * @param conn 사용할 컨넥션 객체
	 * @return 테이블 뷰어를 만들어 리턴시켜준다.
	 */
	public static TableView getTable(String sql, Connection conn) {

		TableView<Map> table = null;// 범용고려 
		TableColumn[] columns = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		int columnCount = 0;

		// 향상된 트라이 케치문 - 아래 conn객체를 사용후 close()가 자동수행됨
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			rs = pstmt.executeQuery();
			// 메타데이타 얻어옴
			rsmd = rs.getMetaData();
			// 메타데이타에서 컬럼이 몇 개인지 알 수 있다.
			columnCount = rsmd.getColumnCount();
			// 데이타컬럼수에 맞게 UI용 테이블 컬럼을 배열로 선언한다.
			columns = new TableColumn[columnCount];
			// 컬럼수 만큼 한바퀴 돌련서 ui용 컬럼을
			for (int i = 0; i < columnCount; i++) {
				columns[i] = new TableColumn<Map, String>(rsmd.getColumnName(i + 1));
				columns[i].setCellValueFactory(new MapValueFactory(rsmd.getColumnName(i + 1)));
				// c[i].setMinWidth(130);
				// Cell edit를 위한 전초작업
				columns[i].setCellFactory(TextFieldTableCell.forTableColumn());
				//
			}
			// 옵져버블리스트 만드는 방법 (특이함)
			ObservableList<Map> allData = FXCollections.observableArrayList();
			
			// 질의결과집합(RS)에서 한 레코드에 차례로 접근하여
			while (rs.next()) {
				// 한행의 데이타를 맵으로 만든다. 키와 벨류가 각각 String이다
				Map<String, String> dataRow = new HashMap<>();
				// 컬럼수 만큼 한바퀴 돌련서 테이블뷰에 반영시킬 데이타를
				// 맵형태로 만든다(컬럼이름을 키로 하고 해당값을 rs에서 읽어온것)
				for (int i = 0; i < columnCount; i++) {
					String value = rs.getString(i + 1);
					dataRow.put(rsmd.getColumnName(i + 1), value);
				}
				// 맵이 다만들어지면 옵저버블 리스터에 이 멥을 추가한다.
				allData.add(dataRow);
			}

			table = new TableView(allData);
			table.getColumns().setAll(columns);// 실제로 만들어진 테이블뷰에 컬럼을 세팅 시킴
			table.tableMenuButtonVisibleProperty().setValue(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}
	
	
	/**
	 * List에 담긴 로우 데이타(맵타입)를 분석하여 tableView를 만들어 준다.
	 * 
	 * @param data List에 담긴 로우 데이타(맵타입)
	 * @return 테이블뷰 객체를 리턴 시킨다.
	 */

	public static TableView getTable(List<Map<String, String>> data, String[] colNames) {

		Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				return new TextFieldTableCell(new StringConverter() {
					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});
			}
		};
		//
		TableView table = new TableView<>();

		// 에디터되게 사전조치(1)
		table.setEditable(true);
		// 에디터되게 하려면 사전에 이 부분을 활성화시켜야 한다.(2)
		table.getSelectionModel().setCellSelectionEnabled(true);

		Map<String, String> rowMap = data.get(0);
		// String[] columnNames = new String[rowMap.size()];
		TableColumn<Map, String>[] cols = new TableColumn[rowMap.size()];

		for (int i = 0; i < colNames.length; i++) {
			cols[i] = new TableColumn<>(colNames[i].toUpperCase());
			cols[i].setCellValueFactory(new MapValueFactory(colNames[i]));
			cols[i].setCellFactory(cellFactoryForMap);
		}

		table.getColumns().setAll(cols);
		table.getItems().addAll(data);
		return table;
	}

	/**
	 * 2차원배열로 테이블 뷰 만들어 준다.
	 * 
	 * @param contents 테이블을 만들 2차원 배열
	 * @param colNames 컬럼 배열
	 * @return
	 */
	public static TableView getTable(String[][] contents, String[] colNames) {
		List<Map<String, String>> data = new ArrayList<>();
		for (int i = 0; i < contents.length; i++) {
			Map<String, String> rowMap = new HashMap<>();
			for (int j = 0; j < contents[i].length; j++) {
				rowMap.put(colNames[j], contents[i][j]);
			}
			data.add(rowMap);
		}

		Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap = new Callback<TableColumn<Map, String>, TableCell<Map, String>>() {
			@Override
			public TableCell call(TableColumn p) {
				return new TextFieldTableCell(new StringConverter() {
					@Override
					public String toString(Object t) {
						return t.toString();
					}

					@Override
					public Object fromString(String string) {
						return string;
					}
				});
			}
		};
		//
		TableView table = new TableView<>();

		// 에디터되게 사전조치(1)
		table.setEditable(true);
		// 에디터되게 하려면 사전에 이 부분을 활성화시켜야 한다.(2)
		table.getSelectionModel().setCellSelectionEnabled(true);

		Map<String, String> rowMap = data.get(0);
		// String[] columnNames = new String[rowMap.size()];
		TableColumn<Map, String>[] cols = new TableColumn[rowMap.size()];

		for (int i = 0; i < colNames.length; i++) {
			cols[i] = new TableColumn<>(colNames[i].toUpperCase());
			cols[i].setCellValueFactory(new MapValueFactory(colNames[i]));
			cols[i].setCellFactory(cellFactoryForMap);
		}
		table.getColumns().setAll(cols);
		table.getItems().addAll(data);
		return table;
	}

}