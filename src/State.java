package Tucil3_13523025_13523030.src;
import java.util.ArrayList;

public class State implements Comparable<State> {
    private Papan papan;
    private ArrayList<Gerakan> steps;
    private int score;

    public State(Papan papan, ArrayList<Gerakan> steps, int score) {
        this.papan = papan;
        this.steps = steps;
        this.score = score;
    }

    public Papan getPapan() {
        return papan;
    }

    public ArrayList<Gerakan> getSteps() {
        return steps;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.score, other.score);
    }
}

