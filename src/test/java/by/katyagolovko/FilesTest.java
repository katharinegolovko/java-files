package by.katyagolovko;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesTest {


    @Test
    public void testForPdfFile() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("file.pdf")) {
            PDF parsed = new PDF(stream);
            assertThat(parsed.numberOfPages).isEqualTo(2);
            assertThat(parsed.text).contains("demonstration");
        }
    }

    @Test
    public void testForTXTFile() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("file.txt")) {
            Scanner myReader = new Scanner(stream);
            while (myReader.hasNextLine()) {
                String file = myReader.nextLine();
                assertThat(file).contains("fidibus");
            }
        }
    }

    @Test
    public void testForXlsFile() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("file.xls")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(3).getCell(1).getStringCellValue())
                    .isEqualTo("Philip");
            assertThat(parsed.excel.getSheetAt(0).getRow(7).getCell(4).getStringCellValue())
                    .isEqualTo("Great Britain");
        }
    }

    @Test
    public void testForCsvFile() throws Exception {
        URL url = getClass().getClassLoader().getResource("file.csv");
        CSVReader reader = new CSVReader(new FileReader(new File(url.toURI())));
        List<String[]> strings = reader.readAll();
        assertThat(strings).contains(
                new String[]{"", "SmartLock_WhatsNewUrl", "https://smart.link/ygbs9ownzisl9", "Link to Smart Lock on What's New page", "English", "Liftmaster", "USA"}
        );
    }

    @Test
    void testForZIPfile() throws Exception {
        ZipFile zipFile = new ZipFile("./src/test/resources/zipwithpass.zip");

        if (zipFile.isEncrypted()) {
            zipFile.setPassword("12345");
        }
        zipFile.extractAll("./src/test/resources/unzip");
        List fileHeaderList = zipFile.getFileHeaders();
        for (int i = 0; i < fileHeaderList.size(); i++) {
            FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
            assertThat(fileHeader.getFileName()).contains("file.txt");
        }
    }
}
