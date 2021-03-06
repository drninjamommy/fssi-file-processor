package main.java.gov.gsa.fssi.helpers.mockdata;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import main.java.gov.gsa.fssi.files.sourcefiles.records.SourceFileRecord;
import main.java.gov.gsa.fssi.files.sourcefiles.records.datas.Data;

public class MockSourceFileRecord {

	public static SourceFileRecord make() {
		SourceFileRecord sourceFileRecord = new SourceFileRecord();
		return sourceFileRecord;
	}

	public static SourceFileRecord make(Data data) {
		SourceFileRecord sourceFileRecord = new SourceFileRecord();
		sourceFileRecord.addData(data);
		return sourceFileRecord;
	}

	public static SourceFileRecord make(HashMap<Integer, String> dataMap) {
		SourceFileRecord sourceFileRecord = new SourceFileRecord();
		Iterator<?> dataMapIterator = dataMap.entrySet().iterator();
		while (dataMapIterator.hasNext()) {
			Map.Entry<Integer, String> dataMapIteratorPairs = (Map.Entry<Integer, String>) dataMapIterator
					.next();
			sourceFileRecord.addData(MockData.make(
					dataMapIteratorPairs.getValue(),
					dataMapIteratorPairs.getKey()));
		}

		return sourceFileRecord;
	}

	public static SourceFileRecord make(List<Data> datas) {
		SourceFileRecord sourceFileRecord = new SourceFileRecord();
		for (Data data : datas) {
			sourceFileRecord.addData(data);
		}
		return sourceFileRecord;
	}

	public static SourceFileRecord make(String value) {
		SourceFileRecord sourceFileRecord = new SourceFileRecord();
		sourceFileRecord.addData(MockData.make(value));
		return sourceFileRecord;
	}

	public static SourceFileRecord make(String value, int headerIndex) {
		SourceFileRecord sourceFileRecord = new SourceFileRecord();
		sourceFileRecord.addData(MockData.make(value, headerIndex));
		return sourceFileRecord;
	}

}
