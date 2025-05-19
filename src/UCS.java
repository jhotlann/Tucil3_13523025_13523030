    package Tucil3_13523025_13523030.src;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.PriorityQueue;

    public class UCS {
        UCS(Papan initBoard, Piece primary){
            System.out.println("Starting UCS class ");
            PriorityQueue<State> pq = new PriorityQueue<State>();
            ArrayList<Gerakan> step = new ArrayList<>();
            HashSet<String> visited = new HashSet<>();

            Papan papanCopy = new Papan(initBoard);

            State current_state = new State(papanCopy, step, 0);
            pq.add(current_state);
            // System.out.println("null");

            // if (pq.isEmpty()){
            //     System.out.println("EMPTY");
            // }

            // if (isAtExit(papanCopy, primary, initBoard.getExitRow(), initBoard.getExitCol())){
            //     System.err.println("Exit");
            // }

            while (!pq.isEmpty()){
                current_state = pq.poll();

                papanCopy = new Papan(current_state.getPapan());
                if (visited.contains(papanCopy.hashString())){
                    continue;
                }
                else{
                    visited.add(papanCopy.hashString());
                }

                
                if (isAtExit(papanCopy, papanCopy.getprimaryPiece(), initBoard.getExitRow(), initBoard.getExitCol())){
                    System.out.println("JAWABAN:");
                    initBoard.displayBoard();
                    for (Gerakan g : current_state.getSteps()){
                        initBoard.movePiece(g);
                        initBoard.displayBoard();
                        System.out.println();
                    }
                    System.out.println("Exit from loop");
                    System.out.printf("Baris: %d  ", papanCopy.getprimaryPiece().getBaris());
                    System.out.printf("Kolom: %d\n", papanCopy.getprimaryPiece().getKolom());
                    break;
                } 
                ArrayList<Piece> current_pieces = new ArrayList<>();
                for (Piece temp : papanCopy.getPieces()){
                    current_pieces.add(new Piece(temp));
                }
                // current_state.getPapan().displayBoard();
                System.out.println();
                for (Piece p : current_pieces){
                    Papan papanCopy1 = new Papan(current_state.getPapan());
                    Papan papanCopy2 = new Papan(current_state.getPapan());
                    
                    if (p.isHorizontal()){
                        boolean kananValid, kiriValid;
                        Gerakan stepKanan = new Gerakan(new Piece(p), "kanan", 1);
                        Gerakan stepKiri = new Gerakan(new Piece(p), "kiri", 1);
                        
                        ArrayList<Gerakan> stepsKanan = new ArrayList<>(current_state.getSteps());
                        ArrayList<Gerakan> stepsKiri = new ArrayList<>(current_state.getSteps());

                        stepsKanan.add(stepKanan);
                        stepsKiri.add(stepKiri);

                        kananValid = papanCopy1.movePiece(stepKanan);
                        kiriValid = papanCopy2.movePiece(stepKiri);
                        
                        State stateKanan = new State(papanCopy1, stepsKanan, current_state.getScore() + 1);
                        State stateKiri = new State(papanCopy2, stepsKiri, current_state.getScore() + 1);
                        
                        if (kananValid){
                            if (visited.contains(papanCopy1.hashString())){}
                            else{
                                pq.add(stateKanan);
                            }
                            
                        }
                        if (kiriValid){
                            if (visited.contains(papanCopy2.hashString())){}
                            else{
                                pq.add(stateKiri);
                            }
                        }
                    }else{
                        boolean atasValid, bawahValid;
                        Gerakan stepAtas = new Gerakan(new Piece(p), "atas", 1);
                        Gerakan stepBawah = new Gerakan(new Piece(p), "bawah", 1);
                        
                        ArrayList<Gerakan> stepsAtas = new ArrayList<>(current_state.getSteps());
                        ArrayList<Gerakan> stepsBawah = new ArrayList<>(current_state.getSteps());
                        
                        stepsAtas.add(stepAtas);
                        stepsBawah.add(stepBawah);
                        
                        atasValid = papanCopy1.movePiece(stepAtas);
                        bawahValid = papanCopy2.movePiece(stepBawah);

                        State stateAtas = new State(papanCopy1, stepsAtas, current_state.getScore() + 1);
                        State stateBawah = new State(papanCopy2, stepsBawah, current_state.getScore() + 1);
                        
                        if (atasValid){
                            if (visited.contains(papanCopy1.hashString())){}
                            else{
                                pq.add(stateAtas);
                            }
                        }
                        
                        if (bawahValid){
                            if (visited.contains(papanCopy2.hashString())){}
                            else{
                                pq.add(stateBawah);
                            }
                        }

                    }
                }
                
            }
        }
        
        private boolean isAtExit(Papan papan, Piece primary, int exitRow, int exitCol) {
                if (primary.isHorizontal()){
                    if (primary.getKolom() == exitCol && primary.getBaris() == exitRow){
                        return true;
                    }

                    else if (primary.getKolom() + primary.getPanjang() - 1 >= exitCol && primary.getBaris() == exitRow){
                        return true;
                    }

                    return false;
                }

                else{
                    if (primary.getKolom() == exitCol && primary.getBaris() == exitRow){
                        return true;
                    } 
                    else if (primary.getBaris() + primary.getPanjang() - 1 >= exitRow && primary.getKolom() == exitCol){
                        return true;
                    }
                    return false;

                }
            }
    }
