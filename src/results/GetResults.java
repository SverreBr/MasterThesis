package results;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import utilities.Game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetResults {

    private final List<String> cellHeaders = Arrays.asList(
            "initiator_tom", "responder_tom",
            "initiator_lr", "responder_lr",
            "initiator_initpoints", "responder_initpoints",
            "initiator_finalpoints", "responder_finalpoints",
            "initiator_gain", "responder_gain",
            "nr_offers");

    private final List<ResultElement> data = new ArrayList<>();

    public void makeResults() {
        int cnt = 0;
        while (cnt++ < 10) {
            generateNewGame(0, 0, 0.5, 0.5);
            System.out.println("Finished iteration " + cnt);
        }
    }

    private void generateNewGame(int initTom, int respTom, double initLR, double respLR) {
        Game game = new Game(initTom, respTom, initLR, respLR);
        int WARMUP_ROUNDS = 200;
        for (int i = 0; i < WARMUP_ROUNDS; i++) {
            game.playTillEnd();
            game.reset(initTom, respTom, initLR, respLR);
        }
        game.playTillEnd();
        ResultElement resultElement = new ResultElement(game);
        data.add(resultElement);
    }

    public void writeExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("results");

        int rowCount = 0;
        Row row = spreadsheet.createRow(rowCount++);
        writeHeader(row);

        for (ResultElement resultElement : data) {
            row = spreadsheet.createRow(rowCount++);
            writeResult(resultElement, row);
        }

        String EXCEL_FILE_PATH = "C:\\Users\\sverr\\Documents\\Universiteit\\Master thesis\\Code\\MasterThesis\\results.xlsx";
        try (FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE_PATH)) {
            workbook.write(outputStream);
        }
    }

    private void writeHeader(Row row) {
        int cellCount = 0;
        for (String header : cellHeaders) {
            Cell cell = row.createCell(cellCount++);
            cell.setCellValue(header);
        }
    }

    private void writeResult(ResultElement resultElement, Row row) {
        int cellCount = 0;

        for (String header : cellHeaders) {
            Cell cell = row.createCell(cellCount++);
            switch (header) {
                case "initiator_tom" -> cell.setCellValue(resultElement.getInitToM());
                case "initiator_lr" -> cell.setCellValue(resultElement.getInitLR());
                case "initiator_initpoints" -> cell.setCellValue(resultElement.getInitInitialPoints());
                case "initiator_finalpoints" -> cell.setCellValue(resultElement.getInitFinalPoints());
                case "responder_tom" -> cell.setCellValue(resultElement.getRespToM());
                case "responder_lr" -> cell.setCellValue(resultElement.getRespLR());
                case "responder_initpoints" -> cell.setCellValue(resultElement.getRespInitialPoints());
                case "responder_finalpoints" -> cell.setCellValue(resultElement.getRespFinalPoints());
                case "initiator_gain" -> cell.setCellValue(resultElement.getInitGain());
                case "responder_gain" -> cell.setCellValue(resultElement.getRespGain());
                case "nr_offers" -> cell.setCellValue(resultElement.getNrOffers());
                default -> cell.setCellValue("");
            }
        }
    }
}
