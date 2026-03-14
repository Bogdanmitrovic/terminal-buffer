package org.bogdanmitrovic

data class Cell(
    val character: Char = ' ',
    val foregroundColor: Color = Color.DEFAULT,
    val backgroundColor: Color = Color.DEFAULT,
    val styles: Set<Style> = emptySet()
)