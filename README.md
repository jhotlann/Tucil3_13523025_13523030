# Tucil 3 IF2211 Strategi Algoritma: Rush Hour Solver
## Deskripsi Singkat
Program ini menyelesaikan permainan puzzle Rush Hour secara otomatis menggunakan 3 algoritma, yakni Uniform Cost Search (UCS), Greedy Best Fit Search, and A* Search. Program ini akan menyediakan solusi berupa langkah demi langkah gerakan yang perlu dilakukan untuk memindahkan _piece_ utama menuju pintu keluar. Program tersedia dalam dua versi:

1. Versi CLI (Command Line Interface): Menyelesaikan puzzle dan mencetak solusi langkah-langkah ke terminal.

2. Versi GUI (Graphical User Interface): Menampilkan antarmuka grafis interaktif untuk melihat solusi per langkah secara visual.

## Requirement & Instalasi
Bahasa Pemrograman:
Java (disarankan Java 8 atau lebih baru)

## Cara Kompilasi
Pindah directory ke src
1. Jika ingin menggunakan CLI untuk menjalankan program
   ```
   javac .\Load.java .\Main.java .\Papan.java .\Piece.java .\Gerakan.java .\UCS.java .\State.java .\GreedyBestFirstSearch.java .\Node.java  .\BlockingDistanceHeuristic.java .\Heuristic.java .\GUI.java BoardPanel.java .\AStarSearch.java .\ConfigWindow.java .\MatrixInputGUI.java
   ```
2. Jika ingin menggunakan GUI
   ```
   javac .\Load.java .\MainGUI.java .\Papan.java .\Piece.java .\Gerakan.java .\UCS.java .\State.java .\GreedyBestFirstSearch.java .\Node.java  .\BlockingDistanceHeuristic.java .\Heuristic.java .\GUI.java BoardPanel.java .\AStarSearch.java .\ConfigWindow.java .\MatrixInputGUI.java
   ```

## Cara Menjalankan Progam
Pindah directory ke src
1. Jika menggunakan CLI
   a. Buat konfigurasi pada folder data, sesuai dengan contoh yang tersedia. Baris pertama merupakan baris dan kolom papan dan baris kedua adalah jumlah piece.
   b. Jalankan
   ```
    java .\Main.java     
   ```
2. Jika menggunakan GUI
   Jalankan
   ```
    java .\MainGUI.java 
   ```
