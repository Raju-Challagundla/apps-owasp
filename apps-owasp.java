import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitScannerApplication.class, args);
    }

    public static class GitScanner {
        
        private static final String DEPENDENCY_CHECK_COMMAND = "dependency-check.sh";
        private static final String[] DEPENDENCY_CHECK_COMMAND_ARGS = new String[] {
                "--format", "HTML", "--out", "report.html", "--suppression", "dependency-check-suppression.xml", "--project", "git-repo-scanner"
        };
        
        private final String repoUrl;
        private final String localDirPath;
        private final String scanDirPath;
        private final String reportDirPath;

        public GitScanner(String repoUrl, String localDirPath, String scanDirPath, String reportDirPath) {
            this.repoUrl = repoUrl;
            this.localDirPath = localDirPath;
            this.scanDirPath = scanDirPath;
            this.reportDirPath = reportDirPath;
        }
        
        public void run() throws Exception {
            cloneGitRepo(repoUrl, localDirPath);
            scanLibraries(scanDirPath);
            generateReport(reportDirPath);
        }

        private void cloneGitRepo(String repoUrl, String localDirPath) throws IOException, InterruptedException {
            Process process = Runtime.getRuntime().exec(String.format("git clone %s %s", repoUrl, localDirPath));
            process.waitFor();
        }
        
        private void scanLibraries(String scanDirPath) throws IOException, InterruptedException {
            String command = String.format("%s --scan %s", DEPENDENCY_CHECK_COMMAND, scanDirPath);
            Process process = Runtime.getRuntime().exec(command, DEPENDENCY_CHECK_COMMAND_ARGS);
            process.waitFor();
        }
        
        private void generateReport(String reportDirPath) throws IOException {
            List<String> htmlFiles = Files.walk(Paths.get(reportDirPath))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".html"))
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
            
            if (htmlFiles.isEmpty()) {
                System.out.println("No HTML report found.");
                return;
            }
            
            String reportPath = htmlFiles.get(0);
            String report = Files.readString(Paths.get(reportPath));
            System.out.println(report);
        }
    }
}
