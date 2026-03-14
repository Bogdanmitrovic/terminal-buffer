package org.bogdanmitrovic

class Line(val width: Int) {
    // for now buffer will be a list of lines, that way whole lines can go into history
    // I will see if it needs changing if it makes more sense
    val cells: Array<Cell> = Array(width) { Cell() }

    fun getCell(column: Int): Cell {
        require(column in 0..<width) { "getCell - Column index out of bounds: $column" }
        return cells[column]
    }

    fun setCell(column: Int, cell: Cell) {
        require(column in 0..<width) { "setCell - Column index out of bounds: $column" }
        cells[column] = cell
    }

    fun createCopy(): Line {
        val copy = Line(width)
        cells.forEachIndexed { i, row -> copy.cells[i] = row }
        return copy
    }

    fun fill(char: Char, fg: Color, bg: Color) {
        for (col in 0..<width) {
            cells[col] = Cell(char, fg, bg)
        }
    }
}