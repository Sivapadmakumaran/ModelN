package com.cognizant.framework;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class CSVDataAccess {

	String filePath;

	public CSVDataAccess(String filePath) {
		this.filePath = filePath;
	}

	public void writeToCSV(List<String[]> lines) {

		try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
			for (String[] line : lines) {
				writer.writeNext(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readValue(String headerName, int row) {

		List<String[]> list = new ArrayList<>();

		// CSVParser parser = new
		// CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();

		try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {

			// CSVReaderBuilder csvReaderBuilder = new
			// CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser);
			CSVReader csvReader = new CSVReaderBuilder(reader).build();
			try {
				list = csvReader.readAll();
				List<String> headers = Arrays.asList(list.get(0));
				int headerColumn = headers.indexOf(headerName);
				if (headerColumn != -1) {
					String value = list.get(row)[headerColumn];
					return value;
				} else {
					throw new CsvException("Column " + headerName + "not found");
				}

			} catch (CsvException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<String[]> readAll() throws CsvException {

		List<String[]> list = new ArrayList<>();

		//CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();

		try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {

			CSVReader csvReader = new CSVReaderBuilder(reader).build();
			
				list = csvReader.readAll();
				return list;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Map<String,String> readValues(String [] headerNames, int row) {
		
		Map<String,String> map = new LinkedHashMap<String,String>();
		
		List<String[]> list = new ArrayList<>();

		//CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();

		try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {

			//CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser);
			CSVReader csvReader = new CSVReaderBuilder(reader).build();
			try {
				list = csvReader.readAll();
				List<String> headers = Arrays.asList(list.get(0));
				
				for(String headerName : headerNames) {
					int headerColumn = headers.indexOf(headerName);	
					if(headerColumn != -1) {
						String value = list.get(row)[headerColumn];
						map.put(headerName, value);
					} else {
						throw new CsvException("Column " + headerName + "not found");
					}
				}
				
				return map;

			} catch (CsvException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
