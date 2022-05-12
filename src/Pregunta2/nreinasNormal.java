package Pregunta2;

import static java.lang.Math.abs;

public class nreinasNormal {
    int[] x;
    int numQueens;

    public nreinasNormal(int numQueens) {
        this.numQueens = numQueens;
        x = new int[numQueens + 1];
    }

    public void backtrack(int numCapas) {
        if (numCapas > numQueens) {
            for (int a = 1; a <= numQueens; a++) {
                System.out.print("( " + a + "Fila" + " " + x[a] + ")");
            }
            System.out.println();
        } else
            for (int i = 1; i <= numQueens; i++) {
                x[numCapas] = i;
                if (place(numCapas)) {
                    backtrack(numCapas + 1);
                }
            }
    }

    private boolean place(int k) {
        for (int j = 1; j < k; j++)
            if ((x[j] == x[k]) || (abs(k - j) == abs(x[j] - x[k])))
                return false;
        return true;
    }

    public static void main(String[] args) {
        nreinasNormal n_queen = new nreinasNormal(4);
        n_queen.backtrack(1);
    }
}
