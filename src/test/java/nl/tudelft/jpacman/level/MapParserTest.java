package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import nl.tudelft.jpacman.PacmanConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class MapParserTest {

    @Mock private BoardFactory boardFactory;
    @Mock private LevelFactory levelFactory;
    @Mock private Square ground;
    @Mock private Square wall;
    @Mock private Board board;
    @Mock private Level level;
    @Mock private Blinky blinky;
    @Mock private Pellet pellet;

    @BeforeEach
    void setUp() {
        // lenient() avoids UnnecessaryStubbingException for unused ones
        lenient().when(boardFactory.createGround()).thenReturn(ground);
        lenient().when(boardFactory.createWall()).thenReturn(wall);
        lenient().when(boardFactory.createBoard(any(Square[][].class))).thenReturn(board);

        lenient().when(levelFactory.createGhost()).thenReturn(blinky);
        lenient().when(levelFactory.createPellet()).thenReturn(pellet);
        lenient().when(levelFactory.createLevel(any(Board.class), anyList(), anyList())).thenReturn(level);
    }

    /** Good map: walls border, one Player P, one Ghost G, and pellets '.' **/
    @Test
    void testParseMapGood() {
        MapParser parser = new MapParser(levelFactory, boardFactory);

        List<String> map = java.util.Arrays.asList(
            "############",
            "#P   .   G #",
            "############"
        );

        Level parsed = parser.parseMap(map);
        assertNotNull(parsed);

        verify(levelFactory, atLeastOnce()).createGhost();
        verify(levelFactory, atLeastOnce()).createPellet();
        verify(boardFactory, atLeastOnce()).createGround();
        verify(boardFactory, atLeastOnce()).createWall();
        verify(boardFactory).createBoard(any(Square[][].class));
        verify(levelFactory).createLevel(any(Board.class), anyList(), anyList());
    }

    /** Bad map #1: inconsistent widths **/
    @Test
    void testParseMapWrong_InconsistentWidths() {
        MapParser parser = new MapParser(levelFactory, boardFactory);

        List<String> bad = new ArrayList<>();
        bad.add("#####");
        bad.add("#P  #");
        bad.add("#######");

        assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(bad));
    }

    /** Bad map #2: invalid character **/
    @Test
    void testParseMapWrong_InvalidChar() {
        MapParser parser = new MapParser(levelFactory, boardFactory);

        List<String> bad = new ArrayList<>();
        bad.add("#####");
        bad.add("#X  #"); // invalid symbol
        bad.add("#####");

        assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(bad));
    }
}
