package com.mycompany.tictactoegame;

import java.util.Scanner;

public class TICTACTOEGAME {
    public static void main(String[] args) {
        Permainan game = new Permainan();
        game.mulai();
    }
}

class Permainan {
    private Papan board;
    private Scanner scanner;
    private final int PEMAIN = -1;  // X
    private final int KOMPUTER = 1; // O
    
    public Permainan() {
        board = new Papan();
        scanner = new Scanner(System.in);
    }
    
    public void mulai() {
        System.out.println("Tic Tac Toe - Tingkat Kesulitan: Tidak Terkalahkan");
        System.out.println("Anda bermain sebagai X, Komputer sebagai O");
        System.out.println("Masukkan baris dan kolom (0-2) dipisahkan spasi");
        
        while (!board.permainanSelesai()) {
            board.tampilkan();
            
            if (board.getGiliran() == PEMAIN) {
                langkahPemain();
            } else {
                langkahKomputer();
            }
        }
        
        board.tampilkan();
        int pemenang = board.cekPemenang();
        
        if (pemenang == PEMAIN) {
            System.out.println("Tidak mungkin! Anda menang!");
        } else if (pemenang == KOMPUTER) {
            System.out.println("Komputer menang seperti yang diharapkan!");
        } else {
            System.out.println("Permainan berakhir seri!");
        }
    }
    
    private void langkahPemain() {
        while (true) {
            System.out.print("Langkah Anda (baris kolom): ");
            int baris = scanner.nextInt();
            int kolom = scanner.nextInt();
            
            if (baris >= 0 && baris < 3 && kolom >= 0 && kolom < 3 && board.setPapan(baris, kolom)) {
                return;
            }
            System.out.println("Langkah tidak valid. Coba lagi.");
        }
    }
    
    private void langkahKomputer() {
        System.out.println("Komputer sedang berpikir...");
        int[] langkahTerbaik = cariLangkahTerbaik();
        board.setPapan(langkahTerbaik[0], langkahTerbaik[1]);
    }
    
    private int[] cariLangkahTerbaik() {
        int skorTerbaik = Integer.MIN_VALUE;
        int[] langkahTerbaik = new int[]{-1, -1};
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getSel(i, j) == 0) {
                    board.setPapan(i, j);
                    int skor = minimax(false);
                    board.resetSel(i, j);
                    
                    if (skor > skorTerbaik) {
                        skorTerbaik = skor;
                        langkahTerbaik[0] = i;
                        langkahTerbaik[1] = j;
                    }
                }
            }
        }
        return langkahTerbaik;
    }
    
    private int minimax(boolean maksimalkan) {
        int hasil = board.cekPemenang();
        if (hasil != 0 || board.papanPenuh()) {
            return hasil;
        }
        
        if (maksimalkan) {
            int skorTerbaik = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getSel(i, j) == 0) {
                        board.setPapan(i, j);
                        int skor = minimax(false);
                        board.resetSel(i, j);
                        skorTerbaik = Math.max(skor, skorTerbaik);
                    }
                }
            }
            return skorTerbaik;
        } else {
            int skorTerbaik = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board.getSel(i, j) == 0) {
                        board.setPapan(i, j);
                        int skor = minimax(true);
                        board.resetSel(i, j);
                        skorTerbaik = Math.min(skor, skorTerbaik);
                    }
                }
            }
            return skorTerbaik;
        }
    }
}

class Papan {
    private int[][] data;
    private int giliran; // 1 untuk O (komputer), -1 untuk X (pemain)
    
    public Papan() {
        data = new int[3][3];
        giliran = -1; // Pemain (X) jalan pertama
    }
    
    public void tampilkan() {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                switch (data[i][j]) {
                    case 0 -> System.out.print(" - ");
                    case -1 -> System.out.print(" X ");
                    case 1 -> System.out.print(" O ");
                }
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("-----------");
        }
        System.out.println();
    }
    
    public boolean setPapan(int baris, int kolom) {
        if (baris < 0 || baris > 2 || kolom < 0 || kolom > 2 || data[baris][kolom] != 0) {
            return false;
        }
        data[baris][kolom] = giliran;
        giliran = -giliran;
        return true;
    }
    
    public int cekPemenang() {
        // Cek baris
        for (int i = 0; i < 3; i++) {
            if (data[i][0] != 0 && data[i][0] == data[i][1] && data[i][0] == data[i][2]) {
                return data[i][0];
            }
        }
        
        // Cek kolom
        for (int j = 0; j < 3; j++) {
            if (data[0][j] != 0 && data[0][j] == data[1][j] && data[0][j] == data[2][j]) {
                return data[0][j];
            }
        }
        
        // Cek diagonal
        if (data[0][0] != 0 && data[0][0] == data[1][1] && data[0][0] == data[2][2]) {
            return data[0][0];
        }
        if (data[0][2] != 0 && data[0][2] == data[1][1] && data[0][2] == data[2][0]) {
            return data[0][2];
        }
        
        return 0; // Belum ada pemenang
    }
    
    public boolean permainanSelesai() {
        return cekPemenang() != 0 || papanPenuh();
    }
    
    public boolean papanPenuh() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void resetPapan() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = 0;
            }
        }
        giliran = -1;
    }
    
    public void resetSel(int baris, int kolom) {
        data[baris][kolom] = 0;
        giliran = -giliran;
    }
    
    public int getSel(int baris, int kolom) {
        return data[baris][kolom];
    }
    
    public int getGiliran() {
        return giliran;
    }
}