package minesweeper

import java.util.*

const val size = 9
val field = Array(size) {Array(size) {Cell()} }
val bombs = mutableSetOf<Pair<Int, Int>>()
val starArea = mutableSetOf<Pair<Int, Int>>()
var gameContinues : Boolean = true
var markCount : Int = 0;
var firstRound : Boolean = true
var mines : Int = 0 ;
val mineUtils : MineUtils = MineUtils();

fun main() {
    val scanner = Scanner(System.`in`)
   println("How many mines do you want on the field?")
     mines = scanner.nextInt()
    while(gameContinues){
        printField()
        println("Set/unset mines marks or claim a cell as free: ")
        play(scanner);
    }

}




fun play(scanner: Scanner) {
    val col = scanner.nextInt() - 1
    val row = scanner.nextInt() - 1
    val command = scanner.next()
    val coordinates = Pair(row,col)
    if(firstRound && command == "free"){
        firstRound = false
        starArea.addAll( mineUtils.surroundingCoordinates(row,col))
        mineUtils.addBombs(mines, field)
        mineUtils.addFlags(field, bombs)
        mineUtils.floodFill(coordinates, field)
    }
    else if(command == "free" && field[row][col].status == State.HIDDEN) {
        if(field.getCell(Pair(row,col)).realValue == 'X') {
            println("You stepped on a mine and failed!")
            gameContinues = false;
            for(x in 0..8){
                for(y in 0..8)
                    field[x][y].status = State.REVEALED
            }

        }
        else{

            mineUtils.floodFill(coordinates, field)
        }

    }else if ( command == "mine") {
        mineUtils.changeCell(coordinates, field)
    }
    else if(State.REVEALED == field[row][col].status){

        if (field[row][col].realValue.isDigit()) {
            println("There is a number here!")
        } else {
            println("there is explored Free cell!")
        }
    }
    checkWinning()
    if(!gameContinues) {
        printField()
        print("Congratulations! You found all the mines!")
    }
    }


fun checkWinning() {
    for(row in 0 until size){
        for(col in 0 until size){
           if(field.getCell(Pair(row,col)).status == State.REVEALED && field.getCell(Pair(row,col)).realValue != 'X'){
               gameContinues = false
           }
            else if(field.getCell(Pair(row,col)).status == State.HIDDEN && field.getCell(Pair(row,col)).realValue != 'X')
               gameContinues = true
        }
    }

    if(markCount == bombs.size){
        for(bomb in bombs){
            if(field[bomb.first][bomb.second].realValue == '*')
            {
                gameContinues = false
            }else{
                gameContinues = true
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







