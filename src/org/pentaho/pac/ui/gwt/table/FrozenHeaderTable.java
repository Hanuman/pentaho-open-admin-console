package org.pentaho.pac.ui.gwt.table;

import org.pentaho.pac.ui.gwt.table.model.TableDataServiceAsync;
import org.pentaho.pac.ui.gwt.table.model.TableModel;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class FrozenHeaderTable extends Composite implements ScrollListener {

	private static final int SCRL_WIDTH = 20;

	private TableStyles styles = new TableStyles();

	private TableModel model;

	private AbsolutePanel root, headerContainer, header, tablePanel;

	private Grid table;

	private ScrollPanel scroller;
	private ColumnSupport colSupport;

	private RowSupport rowSupport;

	private int gridWidth;
	private boolean horizScrolling;
	private TableDataServiceAsync dataService;

	public FrozenHeaderTable(TableModel tableModel) {
		this.model = tableModel;
		addTableStyle(TableStyles.Type.TABLE_HEADER, "pentaho-tableHeader");
		addTableStyle(TableStyles.Type.EVEN_ROW, "pentaho-tableRow-even");
		addTableStyle(TableStyles.Type.ODD_ROW, "pentaho-tableRow-odd");
	}

	public void init() {
		headerContainer = new AbsolutePanel();
		header = new AbsolutePanel();
		headerContainer.add(header, 0, 0);
		table = new Grid();
		tablePanel = new AbsolutePanel();
		tablePanel.add(table);
		scroller = new ScrollPanel(tablePanel);
		scroller.addScrollListener(this);
		root = new AbsolutePanel();
		root.add(headerContainer, 0, 0);
		root.add(scroller, 0, 20);
		refresh();
		initWidget(root);
	}

	public RowSupport getRowSupport() {
		return rowSupport;
	}

	public void addTableStyle(TableStyles.Type type, String styleName) {
		styles.addStyle(type, styleName);
	}

	private void refresh() {
		reloadMetaData();
		redrawHeaders();
		redrawData();
	}

	private void reloadMetaData() {
		colSupport = new ColumnSupport(model, styles);
		rowSupport = new RowSupport(model, styles, dataService);

		int modelTotalWidth = model.getTotalWidth();

		gridWidth = modelTotalWidth < 0 ? colSupport.getWidth() + SCRL_WIDTH
				: modelTotalWidth;
		if (gridWidth > colSupport.getWidth() + SCRL_WIDTH)
			gridWidth = colSupport.getWidth() + SCRL_WIDTH;
		horizScrolling = gridWidth < colSupport.getWidth();

		rowSupport.removeRows(table, colSupport);
	}

	private void redrawHeaders() {
		headerContainer.setPixelSize(gridWidth, model.getPixelHeaderHeight());
		header
				.setPixelSize(colSupport.getWidth(), model
						.getPixelHeaderHeight());

		colSupport.addHeaders(header);

		int hPos = scroller.getHorizontalScrollPosition();
		if (hPos < 0)
			hPos = 0;

		headerContainer.setWidgetPosition(header, -hPos, 0);
	}

	private void redrawData() {

		dataService.getRowCount(new AsyncCallback<Integer>() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());

			}

			public void onSuccess(Integer result) {
				int visibleRows = model.visibleRows();
				int rowCount = result;
				if (rowCount < visibleRows)
					visibleRows = rowCount;
				int scrollerVertSize = visibleRows * model.getPixelRowHeight()
						+ (horizScrolling ? SCRL_WIDTH : 0);

				tablePanel.setPixelSize(colSupport.getWidth(), rowCount
						* model.getPixelRowHeight());
				table.setCellPadding(0);
				table.setCellSpacing(0);
				scroller.setPixelSize(gridWidth, scrollerVertSize);
				root.setWidgetPosition(scroller, 0, model
						.getPixelHeaderHeight());
				root.setPixelSize(gridWidth, model.getPixelHeaderHeight()
						+ scrollerVertSize);

				redrawCells();

			}
		});

	}

	private void redrawCells() {
		if (scroller == null)
			return;

		rowSupport.layoutRows(table, colSupport, scroller.getScrollPosition());
	}

	public void setTableModel(TableModel tableModel) {
		this.model = tableModel;
	}

	public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
		// TODO Auto-generated method stub

	}

	public void setTableDataService(TableDataServiceAsync dataService) {
		this.dataService = dataService;
	}

}
