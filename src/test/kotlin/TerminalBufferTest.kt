import org.bogdanmitrovic.TerminalBuffer
import java.awt.SystemColor.text
import kotlin.test.*

class TerminalBufferTest {
    @Test
    fun `cursor starts at 0,0`() {
        val buf = TerminalBuffer()
        assertEquals(0, buf.cursorRow)
        assertEquals(0, buf.cursorColumn)
    }

    @Test
    fun `setCursorPosition clamps`() {
        val buf = TerminalBuffer()
        buf.setCursorPosition(buf.height, buf.width)
        assertEquals(buf.height - 1, buf.cursorRow)
        assertEquals(buf.width - 1, buf.cursorColumn)
        buf.setCursorPosition(-1, -1)
        assertEquals(0, buf.cursorRow)
        assertEquals(0, buf.cursorColumn)
    }

    @Test
    fun `moveCursor clamps`() {
        val buf = TerminalBuffer()
        buf.moveCursor(vertical = -1)
        assertEquals(0, buf.cursorRow)
        buf.moveCursor(horizontal = -1)
        assertEquals(0, buf.cursorColumn)
        buf.moveCursor(vertical = buf.height)
        assertEquals(buf.height - 1, buf.cursorRow)
        buf.moveCursor(horizontal = buf.width)
        assertEquals(buf.width - 1, buf.cursorColumn)
    }

    @Test
    fun `moveCursor up down left right`() {
        val buf = TerminalBuffer()
        buf.moveCursorDown(5)
        assertEquals(5, buf.cursorRow)
        buf.moveCursorUp(3)
        assertEquals(2, buf.cursorRow)
        buf.moveCursorRight(10)
        assertEquals(10, buf.cursorColumn)
        buf.moveCursorLeft(4)
        assertEquals(6, buf.cursorColumn)
    }

    @Test
    fun `writeText writes text`() {
        val buf = TerminalBuffer()
        buf.writeText("test test")
        assertEquals("test test", buf.getLine(0))
    }

    @Test
    fun `writeText moves cursor`() {
        val buf = TerminalBuffer()
        buf.writeText("test")
        assertEquals(0, buf.cursorRow)
        assertEquals(4, buf.cursorColumn)
    }

    @Test
    fun `writeText wraps cursor`() {
        val buf = TerminalBuffer(5, 5)
        buf.writeText("test test")
        assertEquals(1, buf.cursorRow)
        assertEquals(4, buf.cursorColumn)
    }

    @Test
    fun `writeText overwrites content`() {
        val buf = TerminalBuffer()
        buf.writeText("test test")
        buf.setCursorPosition(0, 0)
        buf.writeText("abcdef")
        assertEquals("abcdefest", buf.getLine(0))
    }

    @Test
    fun `writeText scrolls up`() {
        val buf = TerminalBuffer(5, 2)
        for (i in 0..<3) {
            buf.writeText("line$i")
        }
        assertEquals("line0", buf.getLine(0))
        assertEquals("line1", buf.getLine(buf.scrollbackSize - 1))
        assertEquals(2, buf.scrollbackSize)
    }

    //@Test
}