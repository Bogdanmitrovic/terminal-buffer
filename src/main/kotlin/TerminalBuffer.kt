package org.bogdanmitrovic

class TerminalBuffer {
    var width: Int = 80
    var height: Int = 24
    var maxHistoryLines: Int = 10000 // only value I could find, default for ubuntu

    var foregroundColor: Color = Color.DEFAULT
    var backgroundColor: Color = Color.DEFAULT
    var styles: Set<Style> = emptySet()

    val screen: Array<Line> = Array(height) { Line(width) }
    val history: ArrayDeque<Line> = ArrayDeque()


    var cursorRow: Int = 0
    var cursorColumn: Int = 0

    fun setup(newWidth: Int, newHeight: Int, newMaxHistoryLines: Int) {
        width = newWidth
        height = newHeight
        maxHistoryLines = newMaxHistoryLines
    }

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

    fun moveCursorUp(n: Int = 1) = moveCursor(vertical = n)
    fun moveCursorDown(n: Int = 1) = moveCursor(vertical = -n)
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
                    cursorRow--;
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
            cursorColumn++;
            if(cursorColumn == width) {
                cursorColumn = 0
                cursorRow++
                if (cursorRow == height) {
                    scrollLine()
                    cursorRow--
                }
            }
        }
    }

    private fun scrollLine() {
        val toMove = screen[0].createCopy()
        if (history.size == maxHistoryLines)
            history.removeFirst()
        history.addLast(toMove)
        for (row in 0..<height - 1) {
            screen[row] = screen[row + 1]
        }
        screen[height - 1] = Line(width)
    }
}