package com.my.qfc.service.code.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.my.qfc.common.vo.UserEntity;

@Service
public class ExcelReaderService {

	public List<UserEntity> readExcelFile(InputStream file) {
		List<UserEntity> users = new ArrayList<>();

		try (Workbook workbook = new XSSFWorkbook(file)) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			Iterator<Row> iterator = sheet.iterator();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();

				// Skip the header row
				if (currentRow.getRowNum() == 0) {
					continue;
				}

				UserEntity user = new UserEntity();
				user.setUserid(getNumericValue(currentRow.getCell(0)));
				user.setUsername(getStringValue(currentRow.getCell(1)));
				user.setUseraddress(getStringValue(currentRow.getCell(2)));

				users.add(user);
			}
		} catch (IOException e) {
		}

		return users;
	}

	@SuppressWarnings({ "deprecation", "null" })
	private double getNumericValue(Cell cell) {
		if (cell == null) {
			return (Double) null;
		}

		cell.setCellType(CellType.NUMERIC);
		return cell.getNumericCellValue();
	}

	@SuppressWarnings("deprecation")
	private String getStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		cell.setCellType(CellType.STRING);
		return cell.getStringCellValue();
	}
}
