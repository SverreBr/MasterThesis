package results.createResults;

import lyingAgents.model.Game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetResults {

    private final List<ResultElement> data = new ArrayList<>();

    public void makeResults() {
        int cnt = 0;

        while (cnt++ < ResultSettings.NUM_REP) {
            for (int initTom : ResultSettings.initTomList) {
                for (int respTom : ResultSettings.respTomList) {
                    for (double initLR : ResultSettings.initLRList) {
                        for (double respLR : ResultSettings.respLRList) {
                            for (boolean initCanLie : ResultSettings.initCanLieList) {
                                for (boolean respCanLie : ResultSettings.respCanLieList) {
                                    if ((initCanLie && (initTom < 2)) || (respCanLie && (respTom < 2))) { // agents without 2nd order tom cannot lie
                                        System.out.println("\t  Skipping [" + initTom + ", " + respTom + ", " + initLR + ", " + respLR + ", " + initCanLie + ", " + respCanLie + "] Done;");
                                    } else {
                                        generateNewGame(initTom, respTom, initLR, respLR, initCanLie, respCanLie);
                                        System.out.println("\t[" + initTom + ", " + respTom + ", " + initLR + ", " + respLR + ", " + initCanLie + ", " + respCanLie + "] Done;");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("Finished iteration " + cnt + ".\n");
            try {
                writeExcel();
            } catch (IOException exception) {
                System.out.println("writing to excel did not succeed.");
            }
        }
    }

    private void generateNewGame(int initTom, int respTom, double initLR, double respLR, boolean initCanLie, boolean respCanLie) {
        Game game = new Game(initTom, respTom, initLR, respLR, initCanLie, respCanLie);
        for (int i = 0; i < ResultSettings.WARMUP_ROUNDS; i++) {
            game.playTillEnd();
            game.newRound();
        }

        for (int i = 0; i < ResultSettings.KEEP_RESULTS_NR_ROUNDS; i++) {
            game.newRound();
            game.playTillEnd();
        }

        ResultElement resultElement = new ResultElement(game);
        data.add(resultElement);
    }

    public void writeExcel() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
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
                System.out.println("Successfully written to Excel.");
            }
        }

    }

    private void writeHeader(Row row) {
        int cellCount = 0;
        for (String header : ResultSettings.CELL_HEADERS) {
            Cell cell = row.createCell(cellCount++);
            cell.setCellValue(header);
        }
    }

    private void writeResult(ResultElement resultElement, Row row) {
        int cellCount = 0;

        for (String header : ResultSettings.CELL_HEADERS) {
            Cell cell = row.createCell(cellCount++);
            switch (header) {
                case ResultSettings.initTom -> cell.setCellValue(resultElement.getInitToM());
                case ResultSettings.initLR -> cell.setCellValue(resultElement.getInitLR());
                case ResultSettings.initCanLie -> cell.setCellValue(resultElement.isInitCanLie());
                case ResultSettings.initInitPoints -> cell.setCellValue(resultElement.getInitInitialPoints());
                case ResultSettings.initFinalPoints -> cell.setCellValue(resultElement.getInitFinalPoints());
                case ResultSettings.respTom -> cell.setCellValue(resultElement.getRespToM());
                case ResultSettings.respLR -> cell.setCellValue(resultElement.getRespLR());
                case ResultSettings.respCanLie -> cell.setCellValue(resultElement.isRespCanLie());
                case ResultSettings.respInitPoints -> cell.setCellValue(resultElement.getRespInitialPoints());
                case ResultSettings.respFinalPoints -> cell.setCellValue(resultElement.getRespFinalPoints());
                case ResultSettings.initGain -> cell.setCellValue(resultElement.getInitGain());
                case ResultSettings.respGain -> cell.setCellValue(resultElement.getRespGain());
                case ResultSettings.nrOffers -> cell.setCellValue(resultElement.getNrOffers());
                default -> cell.setCellValue("");
            }
        }
    }
}
