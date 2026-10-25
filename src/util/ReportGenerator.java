
package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

    public static void generate(Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;
        List<String> details = new ArrayList<>();

        Files.createDirectories(logsFolder);

        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(logsFolder)) {

            for (Path file : stream) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }

                try (BufferedReader reader = Files.newBufferedReader(
                        file, StandardCharsets.UTF_8)) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");

                        if (parts.length != 2) {
                            continue;
                        }

                        try {
                            long amount = Long.parseLong(parts[1].trim());

                            if (amount < 0) {
                                continue;
                            }

                            if (parts[0].trim().equalsIgnoreCase("DEPOSIT")) {
                                totalDeposits += amount;
                            } else if (parts[0].trim()
                                    .equalsIgnoreCase("WITHDRAW")) {
                                totalWithdrawals += amount;
                            }
                        } catch (NumberFormatException e) {
                            // Skip lines with invalid amounts.
                        }
                    }
                }

                BasicFileAttributes attrs = Files.readAttributes(
                        file, BasicFileAttributes.class);

                details.add(
                    file.getFileName()
                    + " | Size: " + attrs.size() + " bytes"
                    + " | Last modified: " + attrs.lastModifiedTime()
                );
            }
        }

        List<String> report = new ArrayList<>();
        report.add("MiniBank End-of-Day Report");
        report.add("==========================");
        report.add("Total Deposits: Rs. " + totalDeposits);
        report.add("Total Withdrawals: Rs. " + totalWithdrawals);
        report.add("");
        report.add("Log File Details:");

        report.addAll(details);

        Path parent = reportFile.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(reportFile, report, StandardCharsets.UTF_8);
    }
}
