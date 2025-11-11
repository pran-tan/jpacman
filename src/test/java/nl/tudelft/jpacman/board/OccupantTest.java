package nl.tudelft.jpacman.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 */
class OccupantTest {

    private Unit unit;

    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * A unit starts with NO square; calling getSquare() should assert-fail.
     */
    @Test
    void noStartSquare() {
        assertThat(unit.hasSquare()).isFalse();
        assertThatThrownBy(() -> unit.getSquare())
            .isInstanceOf(AssertionError.class);
    }

    /**
     * After occupying a square, the unit's base is that square.
     */
    @Test
    void testOccupy() {
        Square target = new BasicSquare();

        unit.occupy(target);

        assertThat(unit.hasSquare()).isTrue();
        assertThat(unit.getSquare()).isSameAs(target);
        assertThat(target.getOccupants()).contains(unit);
    }

    /**
     * Reoccupying moves the unit to the new square and updates both squares.
     */
    @Test
    void testReoccupy() {
        Square first = new BasicSquare();
        Square second = new BasicSquare();

        unit.occupy(first);
        unit.occupy(second);

        // now on second
        assertThat(unit.getSquare()).isSameAs(second);
        assertThat(second.getOccupants()).contains(unit);
        // no longer on first
        assertThat(first.getOccupants()).doesNotContain(unit);
    }
}
