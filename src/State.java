package Tucil3_13523025_13523030.src;
import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
        Papan papan;
        List<Gerakan> gerakan;
        int skor;

        public State(Papan papan, List<Gerakan> gerakan, int skor) {
            this.papan = papan;
            this.gerakan = gerakan;
            this.skor = skor;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.skor, other.skor);
        }

        public Papan getPapan() {
            return papan;
        }

        public List<Gerakan> getGerakan() {
            return gerakan;
        }

        public int getSkor() {
            return skor;
        }
    }