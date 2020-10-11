package minesweeper

import kotlin.random.Random

class MineUtils {

    fun surroundingCoordinates(row: Int, col: Int): MutableSet<Pair<Int, Int>> {
    val  starArea = mutableSetOf<Pair<Int, Int>>()
        for(moveRow in -1..1){
            for (moveCol in -1..1) {

                if (isAcceptableValues( Pair(row+moveRow,col+moveCol) )) {
                    starArea.add(Pair(row+moveRow,col+moveCol ))
                }
            }
        }
        starArea.add(Pair(row,col))
        return  starArea
    }


    fun addBombs(mines: Int, field: Array<Array<Cell>>){
        val randomList =  MutableList(150){ Pair(Random.nextInt(0,9), Random.nextInt(0,9))}.iterator()

        while(bombs.size < mines && randomList.hasNext()) {
            val next = randomList.next()

            if ( next !in starArea) {

                if ( bombs.add(next))
                    field[next.first][next.second].realValue = 'X'

            }
        }

    }



    fun floodFill(coordinates: Pair<Int, Int>, field: Array<Array<Cell>>){
        if(! isAcceptableValues(coordinates))
            return;
        if(field.getCell(coordinates).realValue == 'X')
            return
        if(field.getCell(coordinates).status == State.REVEALED)
            return
        if(field.getCell(coordinates).realValue.isDigit()){
            field[coordinates.first][coordinates.second].status = State.REVEALED
            return
        }
        field.getCell(coordinates).status = State.REVEALED

        for(row in -1..1){
            for (col in -1..1) {
                floodFill(Pair(coordinates.first+row, coordinates.second+col), field)
            }
        }

    }

    fun changeCell(coordinates: Pair<Int, Int>, field: Array<Array<Cell>>){
        if(field.getCell(coordinates).status == State.HIDDEN && isAcceptableValues(coordinates) && field.getCell(coordinates).fakeValue == '.' ) {
            field.setFakeValue(coordinates, '*')
            markCount++

        } else {
            field.setFakeValue(coordinates, '.')
            markCount--
        }
    }

    fun addFlags(field: Array<Array<Cell>>, bombs: MutableSet<Pair<Int, Int>>) {
        for(coordinate in bombs){
            for(row in -1..1){
                for (col in -1..1){
                    addFlag(Pair(coordinate.first+row, coordinate.second+col), field)
                }
            }


        }
    }

    private fun addFlag(coordinates: Pair<Int, Int>, field: Array<Array<Cell>>){
        if( isAcceptableValues(coordinates) && notContainingBomb(coordinates) ){
            if(field[coordinates.first][coordinates.second].realValue.isDigit()){
                field[coordinates.first][coordinates.second].realValue = field[coordinates.first][coordinates.second].realValue.inc()
            }
            else {
                field[coordinates.first][coordinates.second].realValue = '1'
            }
        }

    }

    private fun isAcceptableValues(coordinates: Pair<Int, Int>) : Boolean{
        return coordinates.first in 0..8 && coordinates.second in 0..8
    }

    private fun notContainingBomb(coordinates: Pair<Int, Int>) :Boolean{
        return  ! bombs.contains(coordinates)
    }


    fun Array<Array<Cell>>.setFakeValue(coordinates: Pair<Int, Int>, value : Char){
        this[coordinates.first][coordinates.second].fakeValue = value
    }

    fun Array<Array<Cell>>.getCell(coordinates: Pair<Int, Int>): Cell{
        return this[coordinates.first][coordinates.second]
    }

}