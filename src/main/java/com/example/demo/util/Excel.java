package com.example.demo.util;

import com.example.demo.entity.UserEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Excel {
	public static List<UserEntity> readSheetToUsers(InputStream inputStream) {
		List<UserEntity> userEntities = new ArrayList<>();

		try {
			Workbook workbook = new XSSFWorkbook(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			int rowNumber = 0;
			while (rows.hasNext()) {
				Row currentRow = rows.next();

				// skip header
				if (rowNumber == 0) {
					rowNumber++;
					continue;
				}

				Iterator<Cell> cellsInRow = currentRow.iterator();

				UserEntity userEntity = new UserEntity();

				int cellIdx = 0;
				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();

					switch (cellIdx) {

						case 0:
							userEntity.setName(currentCell.getStringCellValue());
							break;
						case 1:
							userEntity.setPhone(currentCell.getStringCellValue());
							break;
						case 2:
							userEntity.setAddress(currentCell.getStringCellValue());
							break;
						case 3:
							userEntity.setAge((int) currentCell.getNumericCellValue());
							break;
						default:
							break;
					}

					cellIdx++;
				}

				userEntities.add(userEntity);
			}

			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userEntities;
	}
}
