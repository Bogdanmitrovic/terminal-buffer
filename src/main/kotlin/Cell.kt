package org.bogdanmitrovic

enum class Color {
    DEFAULT, BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE,
    BRIGHT_BLACK, BRIGHT_RED, BRIGHT_GREEN, BRIGHT_YELLOW, BRIGHT_BLUE,
    BRIGHT_MAGENTA, BRIGHT_CYAN, BRIGHT_WHITE
}

enum class Style {
    BOLD, ITALIC, UNDERLINE
}

class Cell {
    val character: Char = ' '
    val foregroundColor: Color = Color.DEFAULT
    val backgroundColor: Color = Color.DEFAULT
    val styles: Set<Style> = emptySet()
}