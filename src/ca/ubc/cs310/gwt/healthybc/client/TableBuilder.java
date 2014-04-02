package ca.ubc.cs310.gwt.healthybc.client;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Helper class for building tables from TableInfo
 *
 */
public class TableBuilder {
	private CellTable<TableInfo> table;

	public TableBuilder() {
		table = new CellTable<TableInfo>(400);
	}

	public CellTable<TableInfo> buildTable(List<TableInfo> tableInfos) {
		TextColumn<TableInfo> nameColumn = new TextColumn<TableInfo>() {
			@Override
			public String getValue(TableInfo tab) {
				return tab.getName();
			}
		};
		TextColumn<TableInfo> addressColumn = new TextColumn<TableInfo>() {
			@Override
			public String getValue(TableInfo tab) {
				return tab.getAddress();
			}
		};
		TextColumn<TableInfo> emailColumn = new TextColumn<TableInfo>() {
			@Override
			public String getValue(TableInfo tab) {
				return tab.getEmail();
			}
		};
		nameColumn.setSortable(true);
		addressColumn.setSortable(true);
		emailColumn.setSortable(true);

		table.addColumn(nameColumn, "Name");
		table.addColumn(addressColumn, "Address");
		table.addColumn(emailColumn, "E-mail");

		ListDataProvider<TableInfo> dataProvider = new ListDataProvider<TableInfo>();
		dataProvider.addDataDisplay(table);

		List<TableInfo> tableInfoList = dataProvider.getList();
		for (TableInfo tabInf : tableInfos) {
			tableInfoList.add(tabInf);
		}

		ListHandler<TableInfo> sortHandler = new ListHandler<TableInfo>(tableInfoList);
		Comparator<TableInfo> nameSortComparator = new Comparator<TableInfo>() {
			@Override
			public int compare(TableInfo a, TableInfo b) {
				if (a == b) {
					return 0;
				}

				if (a != null) {
					return (b != null) ? a.getName().compareTo(b.getName()) : 1;
				}

				return -1;
			}
		};
		Comparator<TableInfo> addressSortComparator = new Comparator<TableInfo>() {
			@Override
			public int compare(TableInfo a, TableInfo b) {
				if (a == b) {
					return 0;
				}

				if (a != null) {
					return (b != null) ? a.getAddress().compareTo(b.getAddress()) : 1;
				}

				return -1;
			}
		};
		Comparator<TableInfo> emailSortComparator = new Comparator<TableInfo>() {
			@Override
			public int compare(TableInfo a, TableInfo b) {
				if (a == b) {
					return 0;
				}

				if (a != null) {
					return (b != null) ? a.getEmail().compareTo(b.getEmail()) : 1;
				}

				return -1;
			}
		};

		sortHandler.setComparator(nameColumn, nameSortComparator);
		sortHandler.setComparator(addressColumn, addressSortComparator);
		sortHandler.setComparator(emailColumn, emailSortComparator);
		table.addColumnSortHandler(sortHandler);

		table.getColumnSortList().push(nameColumn);

		table.setWidth("100%", false);

		return table;
	}
}
