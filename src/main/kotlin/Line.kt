package org.bogdanmitrovic

class Line(val width: Int) {
    // for now buffer will be a list of lines, that way whole lines can go into history
    // I will see if it needs changing if it makes more sense
    val cells: Array<Cell> = Array(width) { Cell() }

}