package minesweeper

import java.util.*
import kotlin.random.Random

const val size = 9
val field = Array(size) {Array(size) {Cell()} }
val bombs = mutableSetOf<Pair<Int, Int>>()
val starArea = mutableSetOf<Pair<Int, Int>>()
var notAllMineFind = true
var markCount : Int = 0;
var firstRound = true
var mines = 0 ;

fun main() {
    val scanner = Scanner(System.`in`)
   println("How many mines do you want on the field?")
     mines = scanner.nextInt()
    while(notAllMineFind){
        printField()
        println("Set/unset mines marks or claim a cell as free: ")
        play(scanner);


    }

}



fun addBombs(mines:Int){
    val randomList =  MutableList(500){ Pair(Random.nextInt(0,9), Random.nextInt(0,9))}.iterator()

    while(bombs.size < mines && randomList.hasNext()) {
                val next = randomList.next()

        if ( next !in starArea) {

            if ( bombs.add(next))
                field[next.first][next.second].realValue = 'X'

        }
    }

    }

fun play(scanner: Scanner) {
    val col = scanner.nextInt() - 1
    val row = scanner.nextInt() - 1
    val command = scanner.next()
    if(firstRound && command == "free"){
        firstRound = false
        surroundingCoordinates(row, col)
        addBombs(mines)
        addFlags()
        floodFill(row,col)
    }
    else if(command == "free" && field[row][col].status == State.HIDDEN) {
        if(field[row][col].realValue == 'X') {
            println("You stepped on a mine and failed!")
            notAllMineFind = false;
            for(bomb in bombs){
                field[bomb.first][bomb.second].status = State.REVEALED
            }

        }
        else{

            floodFill(row, col)
        }

    }else if ( command == "mine") {
        changeCell(row, col)
    }
    else if(State.REVEALED == field[row][col].status){

        if (field[row][col].realValue.isDigit()) {
            println("There is a number here!")
        } else {
            println("there is explored Free cell!")
        }
    }
    checkWinning()
    if(!notAllMineFind) {
        printField()
        print("Congratulations! You found all the mines!")
    }
    }

fun surroundingCoordinates(row: Int, col: Int) {
    for(xdir in -1..1){
        for (ydir in -1..1) {

            if (isAcceptableValues(row+xdir,col+ydir)) {
                    starArea.add(Pair(row+xdir,col+ydir ))
            }
        }
    }
    starArea.add(Pair(row,col))
}

fun floodFill(row: Int, col: Int){
    if(! isAcceptableValues(row,col))
        return;
    if(field[row][col].realValue == 'X')
        return
    if(field[row][col].status == State.REVEALED)
        return
    if(field[row][col].realValue.isDigit()){
        field[row][col].status = State.REVEALED
        return
    }



    field[row][col].status = State.REVEALED
    floodFill(row+1, col)
    floodFill(row-1, col)
    floodFill(row, col+1)
    floodFill(row, col-1)
    floodFill(row-1, col-1)
    floodFill(row+1, col+1)
    floodFill(row+1, col-1)
    floodFill(row-1, col+1)
}

fun changeCell(row:Int, col:Int){
    if(field[row][col].status == State.HIDDEN && isAcceptableValues(row ,col) && field[row][col].fakeValue == '.' ) {
            field[row][col].fakeValue = '*'
            markCount++

        } else {
            field[row][col].fakeValue = '.'
            markCount--
        }
}

fun checkWinning() {
    for(row in 0 until size){
        for(col in 0 until size){
           if(field[row][col].status == State.REVEALED && field[row][col].realValue != 'X'){
               notAllMineFind = false
           }
            else if(field[row][col].status == State.HIDDEN && field[row][col].realValue != 'X')
               notAllMineFind = true
        }
    }

    if(markCount == bombs.size){
        for(bomb in bombs){
            if(field[bomb.first][bomb.second].realValue == '*')
            {
                notAllMineFind = false
            }else{
                notAllMineFind = true
                return
            }
        }


    }
}


fun printField(){
    print(" |")
    for(count in 1..size){
        print(count)
    }
    println("|")
    println("-|---------|")
    for(row in 0 until size){
        print("${row+1}|")
        for(col in 0 until size) {
            if (field[row][col].status == State.HIDDEN){
                print(field[row][col].fakeValue)
            }
            else{
            print(field[row][col].realValue)
            }
        }
        println("|")
    }
    println("-|---------|")
}

fun addFlags() {
    for(coordinate in bombs){
        for(row in -1..1){
            for (col in -1..1){
                addFlag(coordinate.first+row, coordinate.second+col)
            }
        }


    }
}

fun addFlag( row:Int,col:Int){
    if( isAcceptableValues(row,col) && notContainingBomb(row, col) ){
                if(field[row][col].realValue.isDigit()){
                    field[row][col].realValue = field[row][col].realValue.inc()
                }
        else {
                    field[row][col].realValue = '1'
                }
        }

}

fun isAcceptableValues(row:Int,col:Int) : Boolean{
    return row in 0..8 && col in 0..8
}

fun notContainingBomb(row: Int, col: Int,) :Boolean{
    return  ! bombs.contains(Pair(row,col))
}





