package Pregunta2;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentNQueens {

    //LINK: https://www.baeldung.com/java-executor-service-tutorial
    //LINK: https://stackoverflow.com/questions/19078635/optimizing-n-queen-with-openmp
    //LINK: https://www.hindawi.com/journals/complexity/2021/6694944/
    //? SHOULD I USE EXCECUTOR SERVICE?
    //LINK: https://javarevisited.blogspot.com/2016/12/difference-between-thread-and-executor.html#:~:text=A%20Thread%20represents%20something%20which,abstraction%20for%20concurrent%20task%20execution.
    //NOTE: repos used as a guide:
    //LINK: https://github.com/Fantomas4/N-Queens-Parallel-Algorithm/blob/master/Αναφορά%20Εργασίας%20-%20Παράλληλη%20υλοποίηση%20αλγορίθμου%20Ν-Βασιλισσών.pdf
    //LINK:https://github.com/mauleenn/Parallelizing-NQueens
    //LINK:https://github.com/arrimurri/N-Queens

    int gridSize;
    ArrayList<Integer[]> results = new ArrayList<>();
    int threadsNumber;
    //NOTE: "A Thread represents something which is responsible for executing your code in parallel, while an Executor is an abstraction for concurrent task execution"
    ExecutorService taskExecutor;

    public ConcurrentNQueens(int gridSize, int threadsNumber) {
        this.gridSize = gridSize;
        this.threadsNumber = threadsNumber;
        this.taskExecutor = Executors.newFixedThreadPool(threadsNumber);
    }

    public static void main(String[] args) {
        ConcurrentNQueens concurrentNQueens = new ConcurrentNQueens(4, 2);
        concurrentNQueens.startSolving();
        concurrentNQueens.showAnswer();
    }

    public void startSolving() {
        for (int i = 0; i < gridSize; i++) {
            Integer[] cols = new Integer[this.gridSize];
            cols[0] = i;
            taskExecutor.execute(new QueenTask(1, cols));
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void solveQueens(int col, Integer[] cols) {
        if (col == this.gridSize) {
            synchronized (this) {
                this.results.add(cols.clone());
            }
        } else {
            for (int row = 0; row < this.gridSize; row++) {
                if (checkValidity(cols, col, row)) {
                    cols[col] = row;
                    solveQueens(col + 1, cols);
                }
            }
        }
    }

    private boolean checkValidity(Integer[] cols, int col1, int row1) {
        for (int col2 = 0; col2 < col1; col2++) {
            int row2 = cols[col2];
            if (row1 == row2) {
                return false;
            }
            int rowDistance = Math.abs(row2 - row1);
            int colDistance = col1 - col2;
            if (rowDistance == colDistance) {
                return false;
            }
        }
        return true;
    }

    public void showAnswer() {
        //LINK: https://stackoverflow.com/questions/53589529/validating-a-solution-for-n-queen-problem
        //link: https://helloacm.com/algorithm-to-valid-n-queens/
        int solCounter = 0;
        for (Integer[] solution : this.results) {
            solCounter++;
            System.out.printf(" Solution %d%n", solCounter);
            System.out.print(" Queens placed at:");
            for (int q = 0; q < solution.length; q++) {
                System.out.printf(" [%d, %d]", q, solution[q]);
                if (q != solution.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("\n\n");
        }
    }

    class QueenTask implements Runnable {
        int col;
        Integer[] cols;

        public QueenTask(int col, Integer[] cols) {
            this.col = col;
            this.cols = cols;
        }

        public void run() {
            solveQueens(this.col, this.cols);
        }
    }
}