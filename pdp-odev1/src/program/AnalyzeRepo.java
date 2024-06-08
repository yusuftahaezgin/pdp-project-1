/**
* PROGRAMLAMA DİLLERİNİN PRENSİPLERİ - 1. Ödev
* @author Yusuf Taha Ezgin G221210008 - yusuf.ezgin@ogr.sakarya.edu.tr
* @since 5.04.2024
* <p>
* 		2. Öğretim B 
* 		Dersi Veren: Prof.Dr. AHMET ZENGİN
* </p>
*/

package program;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class AnalyzeRepo {
        private File file;
              
        
        public AnalyzeRepo(File file) {
            this.file = file;
        }
        
        
        
        // javadoc satırlarını hesaplayan fonk
        public static int countJavadocLines(File file) {
            int javadocLineCount = 0;
            boolean insideJavadoc = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("/**")) {
                        // Javadoc satırının başlangıcı kontrolü
                        insideJavadoc = true;
                    } else if (line.startsWith("*/")) {
                        // Javadoc satırının sonu kontrolü
                        insideJavadoc = false;
                    } else if (insideJavadoc && line.startsWith("*")) {
                        // Javadoc satırı içinde olma durumu kontrolü ve satırın "*" ile başlayıp başlamadığını kontrol et
                        javadocLineCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return javadocLineCount;
        }

        
        // yorum satırlarını hesaplayan fonk
        public static int countComments(File file) {
            int totalComment = 0;
            boolean inJavadoc = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                	line = line.trim(); 
                    if (!inJavadoc) {
                        if (line.startsWith("/*") && !line.startsWith("/**")) { 
                        	inJavadoc = true;
                            if (line.endsWith("*/")) { 
                            	inJavadoc = false;
                            }
                        } else if (line.contains("//")) { 
                        	totalComment++;
                        }
                    } else if (line.endsWith("*/")) { 
                    	inJavadoc = false;
                    } else {
                    	totalComment++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return totalComment;
        }
       
              
        //Boş satırları bulan fonk
        public static int countEmptyLines(File file) {
            int emptyLineCount = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) { 
                        emptyLineCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return emptyLineCount;
        }
        
        // Kod satır sayısını bulan fonk
        private static int findCodeLine(File file) {
            int countLOC = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isComment = false;
                while ((line = reader.readLine()) != null) {
                	line = line.trim();
                    if (!isComment) {
                        if (!line.isEmpty() && !line.startsWith("//")) {
                            if (line.startsWith("/*")) {
                                if (!line.contains("*/")) {
                                	isComment = true;
                                }
                            } else {
                            	countLOC++;
                            }
                        }
                    } else {
                        if (line.contains("*/")) {
                        	isComment = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return countLOC;
        }
        
  
        //Fonksiyon sayısını bulan method
        public static int findCountFunction(File file) {
            int countFunc = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean inFunc = false;
                while ((line = reader.readLine()) != null) {
                	line = line.trim();
                    if (!inFunc) {
                        if (line.startsWith("public") && line.contains("(") && line.contains(")")) {
                        	countFunc++;
                        	inFunc = true;
                        }
                    } else {
                        if (line.contains("}")) {
                        	inFunc = false;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return countFunc;
        }

        
        // boşluklar hariç tüm kod sayısını hesaplayan fonk
        public static int LOC(File file) {
            int totalLineCount = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) { 
                        totalLineCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalLineCount;
        }
        

        public void printClassInfo() {
           System.out.println("Sınıf: " + file.getName());
           //System.out.println("boş satır: " + countEmptyLines(file));
           System.out.println("Javadoc Satır Sayısı: " + countJavadocLines(file));
           System.out.println("Yorum Satır Sayısı: " + countComments(file));
           System.out.println("Kod Satır Sayısı: " + findCodeLine(file));
           int loc = LOC(file)+countEmptyLines(file);
           System.out.println("LOC: " + loc);
           System.out.println("Fonksiyon Sayısı: " + findCountFunction(file));
           System.out.println("Yorum Sapma Yüzdesi: %" + sapmaYuzdesi());
        }

        private double sapmaYuzdesi() {
        	double YG = ((double)(countJavadocLines(file) + countComments(file)) * 0.8) / (double)findCountFunction(file);
            double YH = ((double)findCodeLine(file) / (double)findCountFunction(file)) * 0.3;
            double sonuc = ((100.0 * YG) / YH) - 100.0;
            
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#.##", symbols);
            return Double.parseDouble(df.format(sonuc).replace(',', '.'));
        }
        
        
    }