package Tucil3_13523025_13523030.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Load {

    public static Papan Load_Papan(String path){
        try{                
            BufferedReader br = new BufferedReader(new FileReader(path));
    
            String line = br.readLine();
        
            String[] dimensi = line.split(" ");
            int baris = Integer.parseInt(dimensi[0]);
            int kolom = Integer.parseInt(dimensi[1]);
            
            line = br.readLine();
            String[] pieces = line.split(" ");
            int piecesCount = Integer.parseInt(pieces[0]);

            System.out.println("=========================");
            System.out.println("Papan Puzzle");
            System.out.printf("Ukuran papan: %d x %d\n", baris, kolom);
            System.out.printf("Jumlah piece: %d\n", piecesCount);

            char[][] papan = new char[baris][kolom];
            int barisK = -1;
            int kolomK = -1;
            int kolomConfig = -1;
            ArrayList<String> buf = new ArrayList<>();
            line = br.readLine();
            while (line != null){
                buf.add(line);
                
                if (line.length() >= kolomConfig){
                    kolomConfig = line.length();
                }
                line = br.readLine();
            }
            
            if (kolomConfig != kolom){//Exit at col 0 or last col
                if (buf.get(0).charAt(0) == 'K' || buf.get(0).charAt(0) == ' '){//Exit at first col
                    kolomK = 0;
                    for (int i = 0; i < baris; i++){
                        line = buf.get(i);
                        if (line.contains("K")){
                            barisK = i;
                        }
                        for (int j = 1; j < kolom + 1; j++){
                            papan[i][j-1] = line.charAt(j);
                        }
                    }
                } 
                
                else{//Exit at last col
                    kolomK = kolom - 1;
                    for (int i = 0; i < baris; i++){
                        line = buf.get(i);
                        if (line.contains("K")){
                            barisK = i;
                        }
                        for (int j = 0; j < kolom; j++){
                            papan[i][j] = line.charAt(j);
                        }
                    }
                }
            }
            else{
                line = buf.get(0);
                if (line.contains("K")){//Exit at first row
                    barisK = 0;
                    kolomK = line.indexOf("K");
                    
                    for (int i = 1; i < baris+1; i++){
                        line = buf.get(i);
                        for (int j = 0; j < kolom; j++){
                            papan[i - 1][j] = line.charAt(j);
                        }
                    }
                }

                else{//Exit at last row
                    barisK = baris - 1;

                    for (int i = 0; i < baris; i++){
                        line = buf.get(i);
                        for (int j = 0; j < kolom; j++){
                            papan[i][j] = line.charAt(j);
                        }
                    }
                    
                    line = buf.get(kolom);
                    kolomK = line.indexOf("K");
                }
            }
            br.close();   
            
            System.out.printf("Pintu Keluar: %d, %d\n", barisK, kolomK);
            Papan p = new Papan(baris, kolom, papan, barisK, kolomK);

            System.out.println("Primary Piece: " + p.getPrimaryPiece().getNama());

            System.out.println("\nPapan:");
            p.displayBoard();
            System.out.println("=========================\n");
            
            return p;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }


}
