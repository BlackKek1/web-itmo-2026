package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    private static final String PAGE_URL = "/ticTacToe";
    private static final String STATE_KEY = "ticTacToeState";
    private static final Pattern CELL_PARAMETER = Pattern.compile("cell_(\\d)(\\d)");

    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("state", getState(request.getSession()));
    }

    private void onMove(HttpServletRequest request) {
        State state = getState(request.getSession());

        for (String parameterName : request.getParameterMap().keySet()) {
            Matcher matcher = CELL_PARAMETER.matcher(parameterName);
            if (matcher.matches()) {
                int row = Integer.parseInt(matcher.group(1));
                int col = Integer.parseInt(matcher.group(2));
                state.makeMove(row, col);
                break;
            }
        }

        throw new RedirectException(PAGE_URL);
    }

    private void newGame(HttpServletRequest request) {
        request.getSession().setAttribute(STATE_KEY, new State());
        throw new RedirectException(PAGE_URL);
    }

    private State getState(HttpSession session) {
        State state = (State) session.getAttribute(STATE_KEY);
        if (state == null) {
            state = new State();
            session.setAttribute(STATE_KEY, state);
        }
        return state;
    }

    public enum Phase {
        RUNNING,
        WON_X,
        WON_O,
        DRAW
    }

    public static class State {
        private static final int SIZE = 3;

        private final String[][] cells = new String[SIZE][SIZE];
        private boolean crossesMove = true;
        private Phase phase = Phase.RUNNING;
        private int movesCount;

        public int getSize() {
            return SIZE;
        }

        public String[][] getCells() {
            return cells;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public Phase getPhase() {
            return phase;
        }

        private boolean isWinningMove(int row, int col) {
            String symbol = cells[row][col];
            return isFilledLine(symbol, row, 0, 0, 1)
                || isFilledLine(symbol, 0, col, 1, 0)
                || row == col && isFilledLine(symbol, 0, 0, 1, 1)
                || row + col == SIZE - 1 && isFilledLine(symbol, 0, SIZE - 1, 1, -1);
        }

        private boolean isFilledLine(String symbol, int startRow, int startCol, int rowStep, int colStep) {
            for (int i = 0; i < SIZE; ++i) {
                if (!symbol.equals(cells[startRow + i * rowStep][startCol + i * colStep])) {
                    return false;
                }
            }
            return true;
        }

        public void makeMove(int row, int col) {
            if (phase != Phase.RUNNING || !(row >= 0 && row < SIZE && col >= 0 && col < SIZE) || cells[row][col] != null) {
                return;
            }

            cells[row][col] = crossesMove ? "X" : "O";
            ++movesCount;

            if (isWinningMove(row, col)) {
                phase = crossesMove ? Phase.WON_X : Phase.WON_O;
            } else if (movesCount == SIZE * SIZE) {
                phase = Phase.DRAW;
            } else {
                crossesMove = !crossesMove;
            }
        }
    }
}