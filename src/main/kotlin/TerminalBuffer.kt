package org.bogdanmitrovic

class TerminalBuffer {
    var width:Int = 80
    var height:Int = 24
    var scrollbackLines:Int =10000 // only value I could find, default for ubuntu

    var foregroundColor: Color = Color.DEFAULT
    var backgroundColor: Color = Color.DEFAULT
    var styles: Set<Style> = emptySet()

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

}