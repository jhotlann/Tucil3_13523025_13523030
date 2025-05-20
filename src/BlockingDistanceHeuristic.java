    package Tucil3_13523025_13523030.src;

    
    public class BlockingDistanceHeuristic implements Heuristic {
        
        @Override
        public int calculate(Papan state) {
            Piece primaryPiece = state.getPrimaryPiece();
            if (primaryPiece == null) {
                return Integer.MAX_VALUE; 
            }
            
            int exitRow = state.getExitRow();
            int exitCol = state.getExitCol();
            
            if (primaryPiece.isHorizontal()) {
                return calculateHorizontalPieceHeuristic(state, primaryPiece, exitRow, exitCol);
            } else {
                return calculateVerticalPieceHeuristic(state, primaryPiece, exitRow, exitCol);
            }
        }
        
    
        private int calculateHorizontalPieceHeuristic(Papan state, Piece primaryPiece, int exitRow, int exitCol) {
            int row = primaryPiece.getBaris();
            int col = primaryPiece.getKolom();
            int length = primaryPiece.getPanjang();
            int distance;
            int blockingPieces = 0;
            char[][] board = state.getPapan();

            if (row != exitRow) {
                return 100; 
            }

            if (exitCol < col) {
                if (col == exitCol) {
                    return 0;
                }

                distance = exitCol - col;

                for (int j = exitCol + 1; j < col; j++) {
                    if (board[row][j] != '.' && board[row][j] != primaryPiece.getNama()) {
                        blockingPieces++;
                    }
                }
            } else {
                int rightEnd = col + length - 1;
                
                if (rightEnd == exitCol) {
                    return 0; 
                }

                distance = exitCol - rightEnd;
            
                for (int j = rightEnd + 1; j < exitCol; j++) {
                    if (board[row][j] != '.' && board[row][j] != primaryPiece.getNama()) {
                        blockingPieces++;
                    }
                }
            }
            
            return distance + (blockingPieces * 2);
        }
        
        private int calculateVerticalPieceHeuristic(Papan state, Piece primaryPiece, int exitRow, int exitCol) {
            int row = primaryPiece.getBaris();
            int col = primaryPiece.getKolom();
            int length = primaryPiece.getPanjang();
            int distance;
            int blockingPieces = 0;
            char[][] board = state.getPapan();

            if (col != exitCol) {
                return 100; 
            }

            if (exitRow < row) {
                if (exitRow == row){
                    return 0;
                }

                distance = row - exitRow;

                for (int i = exitRow + 1; i < row; i++) {
                    if (board[i][col] != '.' && board[i][col] != primaryPiece.getNama()) {
                        blockingPieces++;
                    }
                }

            } else {
                int bottomEnd = row + length - 1;
                
                if (bottomEnd == exitRow) {
                    return 0; 
                }
                
                distance = exitRow - bottomEnd;
   
                for (int i = bottomEnd + 1; i < exitRow; i++) {
                    if (board[i][col] != '.' && board[i][col] != primaryPiece.getNama()) {
                        blockingPieces++;
                    }
                }
            }
            return distance + (blockingPieces * 2);
        }
    }