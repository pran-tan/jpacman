package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/**
 * Tests for the Board class to verify valid construction and access.
 */
public class BoardTest {

    @Test
    void testValidBoard() {
        // Create a 1x1 grid with a single valid BasicSquare
        Square[][] grid = new Square[1][1];
        grid[0][0] = new BasicSquare();

        // Construct the board
        Board board = new Board(grid);

        // Invariant should hold (no null squares)
        assertThat(board.invariant()).isTrue();

        // The square at (0,0) should be the same as we put in
        assertThat(board.squareAt(0, 0)).isSameAs(grid[0][0]);

        // Within borders only true for (0,0)
        assertThat(board.withinBorders(0, 0)).isTrue();
        assertThat(board.withinBorders(1, 0)).isFalse();
        assertThat(board.withinBorders(0, 1)).isFalse();
    }

    @Test
    void testInvalidBoard_NullSquare() {
        Square[][] grid = new Square[1][1];
        grid[0][0] = null; // invalid by invariant

        // With assertions enabled, the constructor will assert-fail.
        // If assertions were disabled, the next call (squareAt) would assert-fail.
        assertThatThrownBy(() -> {
            Board b = new Board(grid);  // triggers AssertionError when -ea is on
            b.squareAt(0, 0);           // fallback assertion if constructor didn't throw
        }).isInstanceOf(AssertionError.class);
    }
}
