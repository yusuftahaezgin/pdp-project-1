/**
* PROGRAMLAMA DİLLERİNİN PRENSİPLERİ - 1. Ödev
* @author Yusuf Taha Ezgin - G221210008 - yusuf.ezgin@ogr.sakarya.edu.tr
* @since 5.04.2024
* <p>
* 		2. Öğretim B 
* 		Dersi Veren: Prof.Dr. AHMET ZENGİN
* </p>
*/

package program;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
  
        // Kullanıcıdan GitHub reposu URL'si alma
        System.out.println("GitHub repository URL'sini girin:");
        String repoUrl = scanner.nextLine();
        scanner.close();

        // Geçici bir dizin oluştur
        File tempDir = CopyRepo.createTempDir();

        // Git deposunu klonla ve *.java dosyalarını al
        List<File> javaFiles = CopyRepo.cloneAndRetrieveJavaFiles(repoUrl, tempDir);

        // Sadece sınıf olan dosyaları ayıkla
        List<File> classFiles = CopyRepo.filterClassFiles(javaFiles);

        // Sınıf dosyalarını ekrana yazdır
        
        List<AnalyzeRepo> classes = new ArrayList<>();
        for (File classFile : classFiles) {
        	AnalyzeRepo javaClass = new AnalyzeRepo(classFile);
            classes.add(javaClass);
        }

        for (AnalyzeRepo javaClass : classes) {
            javaClass.printClassInfo();
            System.out.println("-----------------------------------------");
        }
                
    }
    
}