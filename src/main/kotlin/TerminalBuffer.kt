package org.bogdanmitrovic

class TerminalBuffer {
    var width:Int = 80
    var height:Int = 24
    var scrollbackLines:Int =10000 // only value I could find, default for ubuntu

    var foregroundColor: Color = Color.DEFAULT
    var backgroundColor: Color = Color.DEFAULT
    var styles: Set<Style> = emptySet()

    var cursorRow:Int =0
    var cursorColumn:Int =0

    fun setup(newWidth:Int, newHeight:Int, newScrollbackLines:Int) {
        width = newWidth
        height = newHeight
        scrollbackLines = newScrollbackLines
    }

    fun setAttributes(newForegroundColor: Color, newBackgroundColor: Color, newStyles: Set<Style>) {
        foregroundColor = newForegroundColor
        backgroundColor = newBackgroundColor
        styles = newStyles
    }

    fun setCursorPosition(row:Int, column:Int) {
        cursorRow = row.coerceIn(0, height - 1)
        cursorColumn = column.coerceIn(0, width - 1)
    }
    fun moveCursor(horizontal:Int, vertical:Int) {
        setCursorPosition(cursorRow + vertical, cursorColumn + horizontal)
    }

}