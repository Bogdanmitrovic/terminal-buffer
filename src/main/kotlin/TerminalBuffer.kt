package org.bogdanmitrovic

class TerminalBuffer(
    val width: Int = 80,
    val height: Int = 24,
    val maxScrollback: Int = 10000  // only value I could find, default for ubuntu
) {
    val scrollbackSize: Int get() = scrollback.size

    private var foregroundColor: Color = Color.DEFAULT
    private var backgroundColor: Color = Color.DEFAULT
    private var styles: Set<Style> = emptySet()

    private val screen: Array<Line> = Array(height) { Line(width) }
    private val scrollback: ArrayDeque<Line> = ArrayDeque()


    var cursorRow: Int = 0
        private set
    var cursorColumn: Int = 0
        private set

    fun setAttributes(newForegroundColor: Color, newBackgroundColor: Color, newStyles: Set<Style>) {
        foregroundColor = newForegroundColor
        backgroundColor = newBackgroundColor
        styles = newStyles
    }

    fun setCursorPosition(row: Int, column: Int) {
        cursorRow = row.coerceIn(0, height - 1)
        cursorColumn = column.coerceIn(0, width - 1)
    }

    fun moveCursor(horizontal: Int = 0, vertical: Int = 0) {
        setCursorPosition(cursorRow + vertical, cursorColumn + horizontal)
    }

    fun moveCursorUp(n: Int = 1) = moveCursor(vertical = -n)
    fun moveCursorDown(n: Int = 1) = moveCursor(vertical = n)
    fun moveCursorRight(n: Int = 1) = moveCursor(horizontal = n)
    fun moveCursorLeft(n: Int = 1) = moveCursor(horizontal = -n)

    fun writeText(text: String) {
        for (char in text) {
            screen[cursorRow].setCell(cursorColumn, Cell(char, foregroundColor, backgroundColor, styles))
            cursorColumn++
            if (cursorColumn == width) {
                cursorColumn = 0
                cursorRow++
                if (cursorRow == height) {
                    scrollLine()
                    cursorRow = height - 1
                }
            }
        }
    }

    fun insertText(text: String) {
        for (char in text) {
            val line = screen[cursorRow]
            for (col in width - 1 downTo cursorColumn + 1) {
                line.setCell(col, line.getCell(col - 1))
            }
            line.setCell(cursorColumn, Cell(char, foregroundColor, backgroundColor, styles))
            cursorColumn++
            if (cursorColumn == width) {
                cursorColumn = 0
                cursorRow++
                if (cursorRow == height) {
                    scrollLine()
                    cursorRow = height - 1
                }
            }
        }
    }

    private fun scrollLine() {
        val toMove = screen[0].createCopy()
        if (scrollback.size == maxScrollback)
            scrollback.removeFirst()
        scrollback.addLast(toMove)
        for (row in 0..<height - 1) {
            screen[row] = screen[row + 1]
        }
        screen[height - 1] = Line(width)
    }

    fun fillLine(row: Int, char: Char = ' ', fg: Color = Color.DEFAULT, bg: Color = Color.DEFAULT) {
        require(row in 0 ..< height) { "Row $row out of bounds" }
        screen[row].fill(char, fg, bg)
    }

    fun insertEmptyLine() {
        scrollLine()
    }

    fun clearScreen() {
        for (row in 0..<height) {
            val line = screen[row].createCopy()
            if (scrollback.size == maxScrollback) scrollback.removeFirst()
            scrollback.addLast(line)
            screen[row] = Line(width)
        }
        cursorRow = 0
        cursorColumn = 0
    }

    fun clearAll() {
        clearScreen()
        scrollback.clear()
    }

    fun getCharAt(row: Int, column: Int): Char {
        require(row in 0..<height + scrollback.size) { "Row $row is out of bounds" }
        require(column in 0..<width) { "Column $column is out of bounds" }
        return if (row < scrollback.size)
            scrollback[row].getCell(column).character
        else
            screen[row - scrollback.size].getCell(column).character
    }

    fun getAttributesAt(row: Int, column: Int): Triple<Color, Color, Set<Style>> {
        require(row in 0..<height + scrollback.size) { "Row $row is out of bounds" }
        require(column in 0..<width) { "Column $column is out of bounds" }
        val cell = if (row < scrollback.size)
            scrollback[row].getCell(column)
        else
            screen[row - scrollback.size].getCell(column)
        return Triple(cell.foregroundColor, cell.backgroundColor, cell.styles)
    }

    fun getLine(row: Int): String {
        require(row in 0..<height + scrollback.size) { "Row $row is out of bounds" }
        return if (row < scrollback.size) scrollback[row].toDisplayString()
        else screen[row - scrollback.size].toDisplayString()
    }

    fun getScreen(): String {
        val sb = StringBuilder()
        screen.forEach { sb.appendLine(it.toDisplayString()) }
        return sb.toString().trimEnd()
    }

    fun getScrollbackAndScreen(): String {
        val sb = StringBuilder()
        scrollback.forEach { sb.appendLine(it.toDisplayString()) }
        screen.forEach { sb.appendLine(it.toDisplayString()) }
        return sb.toString().trimEnd()
    }
}