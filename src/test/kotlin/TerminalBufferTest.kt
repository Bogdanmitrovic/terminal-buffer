import org.bogdanmitrovic.Color
import org.bogdanmitrovic.Style
import org.bogdanmitrovic.TerminalBuffer
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
        assertEquals("test test", buf.getLine(buf.scrollbackSize))
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
        assertEquals("abcdefest", buf.getLine(buf.scrollbackSize))
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

    @Test
    fun `insertText pushes content`() {
        val buf = TerminalBuffer()
        buf.insertText("HellWorld")
        buf.setCursorPosition(0, 4)
        buf.insertText("o ")
        assertEquals("Hello World", buf.getLine(0))
    }

    @Test
    fun `insertText drops content to the right`() {
        // I wasnt sure if this is expected behavior
        // but other terminals seem to work like this
        val buf = TerminalBuffer(10, 5)
        buf.writeText("abcdefghij")
        buf.setCursorPosition(0, 0)
        buf.insertText("000")
        assertEquals("000abcdefg", buf.getLine(buf.scrollbackSize))
    }

    @Test
    fun `scrollback receives lines off screen`() {
        val buf = TerminalBuffer()
        repeat(3) { buf.insertEmptyLine() }
        assertEquals(3, buf.scrollbackSize)
    }

    @Test
    fun `scrollback maintains max size`() {
        val buf = TerminalBuffer(maxScrollback = 3)
        repeat(5) { buf.insertEmptyLine() }
        assertEquals(3, buf.scrollbackSize)
    }

    @Test
    fun `attributes are applied`() {
        val buf = TerminalBuffer()
        buf.setAttributes(Color.BRIGHT_GREEN, Color.BLACK, setOf(Style.BOLD))
        buf.writeText("test test")
        val (fg, bg, styles) = buf.getAttributesAt(buf.scrollbackSize, 0)
        assertEquals(Color.BRIGHT_GREEN, fg)
        assertEquals(Color.BLACK, bg)
        assertTrue(Style.BOLD in styles)
    }

    @Test
    fun `clearScreen moves content to scrollback`() {
        val buf = TerminalBuffer()
        buf.writeText("test test")
        buf.clearScreen()
        assertTrue(buf.scrollbackSize > 0)
        assertEquals("test test", buf.getLine(0))
        assertTrue(buf.getLine(buf.scrollbackSize).isEmpty())
    }

    @Test
    fun `clearScreen resets cursor pos`() {
        val buf = TerminalBuffer(5)
        buf.writeText("test test")
        buf.clearScreen()
        assertEquals(0, buf.cursorRow)
        assertEquals(0, buf.cursorColumn)
    }

    @Test
    fun `clearAll clears everything`() {
        val buf = TerminalBuffer(5)
        buf.writeText("test test")
        buf.clearAll()
        assertEquals(0, buf.scrollbackSize)
        assertTrue(buf.getLine(0).isEmpty())
    }

    @Test
    fun `clearAll resets cursor pos`() {
        val buf = TerminalBuffer(5)
        buf.writeText("test test")
        buf.clearAll()
        assertEquals(0, buf.cursorRow)
        assertEquals(0, buf.cursorColumn)
    }
}