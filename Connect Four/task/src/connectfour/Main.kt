package connectfour

var player1 = ""
var player2 = ""
var boardInRange = 0
var rows = 0
var columns = 0
var table = ""
var numberColumns = 1
var listColumns = mutableListOf<String>()
var list1 = mutableListOf(listColumns)
var list2 = mutableListOf("")
var turn = 0
var exit = false
var optionPlayer = ""
var invalidOption = 0
var position = ""
var prueba = ""
var needsToWinP1 = mutableListOf(0, 0, 0)
var needsToWinP2 = mutableListOf(0, 0, 0)
var actualRow = 0
var actualPosition = 0
var onlyOne = 1
const val ESQ_INF_IZQ = '\u255A'  // ╚
const val ESQ_INF_DCHA = '\u255D' // ╝
const val DOB_LIN_HOR = '\u2550' // ═
const val DOB_LIN_VERT = '\u2551' // ║
const val SEP_COL = '\u2569' // ╩
var numberGames = ""
var scoreP1 = 0
var scoreP2 = 0
var actualGame = 0
var draw = 0

fun main() {
    println("Connect Four\nFirst player's name:")
    player1 = readln()
    println("Second player's name:")
    player2 = readln()
    //loop to get board size and avoid invalid keyboard input
    do {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        val input = readln()
        if (input.isEmpty()) {
            rows = 6
            columns = 7
            boardInRange = 1
            table = "6x7"
        } else {
            table = ""
            for (i in input) {
                if (i == ' ' || i == '\t') {
                    continue
                } else {
                    table += i
                }
            }
        }
        val regex = Regex(".?.[x|X]..?")
        if (table.first().isLetter() || table.last().isLetter()) {
            println("Invalid input")
            continue
        }
        if (regex.matches(table)) {
            rows = table.first().toString().toInt()
            columns = table.last().toString().toInt()
        } else {
            println("Invalid input")
            continue
        }
        if ((rows !in 5..9)) {
            println("Board rows should be from 5 to 9")
            continue
        }
        if (columns !in 5..9) {
            println("Board columns should be from 5 to 9")
            continue
        }
        if ((rows in 5..9) && (columns in 5..9)) {
            boardInRange = 1
        }
        do {
            println(
                "Do you want to play single or multiple games?\n" +
                        "For a single game, input 1 or press Enter\n" +
                        "Input a number of games:"
            )
            numberGames = readln()

            if (numberGames.isEmpty()) {
                numberGames = "1"
                continue
            }
            if (numberGames == "0") {
                println("Invalid input")
                continue
            }
            for (i in numberGames) {
                if (i == ' ' || i.isLetter()) {
                    numberGames = "0"
                    println("Invalid input")
                }
            }

        } while (numberGames == "0")
    } while (boardInRange == 0)
    //prints name´s players, board size and draw board
    println("$player1 VS $player2")
    println("$rows X $columns board")
    for (i in 1..numberGames.toInt()) {
        turn = 0
        actualGame++
        if (numberGames.toInt() == 1) {
            println("Single game")
        }
        if (numberGames.toInt() > 1 && actualGame == 1) {
            println("Total ${numberGames.toInt()} games")
            println("Game #$i")
        }
        if (numberGames.toInt() > 1 && (scoreP1 > 0 || scoreP2 > 0)) {
            println("Game #$i")
        }
        numberColumns = 1
        listColumns = mutableListOf<String>()
        list1 = mutableListOf(listColumns)
        list2 = mutableListOf("")
        table()
        drawTable()
        game()
        if (actualGame == numberGames.toInt()) {
            println("Game over!")
            println(
                "Score\n" +
                        "$player1: $scoreP1 $player2: $scoreP2"
            )
        }
        if (exit) {
            println("Game over!")
            return
        }
    }

}

fun game() {

    //loop for draw icon´s player in the board if it matches with rules
    do {
        if (actualGame % 2 != 0) {
            if (turn % 2 == 0){
                println("$player1's turn:")
            } else {
                println("$player2's turn:")
            }
        } else {
            if (turn % 2 == 0){
                println("$player2's turn:")
            } else {
                println("$player1's turn:")
            }
        }


        optionPlayer = ""
        optionPlayer = readln()
        if (optionPlayer.isEmpty()) {
            continue
        }
        if (optionPlayer == "end") {
            exit = true
            break
        }
        for (i in optionPlayer) {
            if (i.isLetter() || i == '.' || i == '\n' || i == '\t') {
                invalidOption = 1
            }
        }
        if (invalidOption == 1) {
            println("Incorrect column number")
            continue
        }

        if (optionPlayer.toInt() !in 1..columns) {
            println("The column number is out of range (1 - $columns)")
            continue
        }
        if ((list1[1][optionPlayer.toInt() * 2 - 1] == "*") || (list1[1][optionPlayer.toInt() * 2 - 1] == "o")) {
            println("Column $optionPlayer is full")
            continue
        }
        if (optionPlayer.toInt() in 1..columns) {
            checkPositionFree(rows, turn)
            checkWin()
            printVictory()
            if ((needsToWinP1[0] >= 4 || needsToWinP1[1] >= 4) || (needsToWinP2[0] >= 4 || needsToWinP2[1] >= 4)) {
                break
            }
            if (turn % 2 != 0) checkWinD1("o") else checkWinD1("*")
            printVictory()
            if (turn % 2 != 0) checkWinD2("o") else checkWinD2("*")
            printVictory()
            if (turn == rows * columns) {
                scoreP1++
                scoreP2++
                println("It is a draw")
                println(
                    "Score\n" +
                            "$player1: $scoreP1 $player2: $scoreP2"
                )
                break
            }

        }
    } while (!exit)
}

// function to create table
fun table() {
    repeat(columns) {
        listColumns.add(" $numberColumns")
        numberColumns++
    }
    repeat(rows) {
        list2 = mutableListOf<String>()
        for (i in 0..columns * 2) {
            if (i % 2 == 0) {
                list2.add(DOB_LIN_VERT.toString())
            } else {
                list2.add(" ")
            }
        }
        list1.add(list2)
    }
}

// function to draw table
fun drawTable() {
    for (i in list1.indices) {
        println(list1[i].joinToString(""))
    }
    print("$ESQ_INF_IZQ")
    repeat(columns - 1) {
        print("$DOB_LIN_HOR$SEP_COL")
    }
    print("$DOB_LIN_HOR$ESQ_INF_DCHA\n")
}

fun checkPositionFree(numberOfRows: Int, player: Int) {
    for (i in numberOfRows downTo 1) {
        actualRow = i
        actualPosition = optionPlayer.toInt() * 2 - 1

        if (list1[i][optionPlayer.toInt() * 2 - 1] == " ") {
            if (actualGame % 2 != 0 ) {
                if (turn % 2 == 0) {
                    list1[i][optionPlayer.toInt() * 2 - 1] = "o"
                    prueba = list1[i][optionPlayer.toInt() * 2 - 1]
                    turn++
                    drawTable()
                    position = list1[i][optionPlayer.toInt() * 2 - 1]
                    break
                } else {
                    list1[i][optionPlayer.toInt() * 2 - 1] = "*"
                    prueba = list1[i][optionPlayer.toInt() * 2 - 1]
                    turn++
                    drawTable()
                    break
                }
            } else  {
                if (turn % 2 == 0) {
                    list1[i][optionPlayer.toInt() * 2 - 1] = "*"
                    prueba = list1[i][optionPlayer.toInt() * 2 - 1]
                    turn++
                    drawTable()
                    position = list1[i][optionPlayer.toInt() * 2 - 1]
                    break
                } else {
                    list1[i][optionPlayer.toInt() * 2 - 1] = "o"
                    prueba = list1[i][optionPlayer.toInt() * 2 - 1]
                    turn++
                    drawTable()
                    break
                }

            }
        }
    }
}

fun checkWin() {
    needsToWinP1 = mutableListOf(0, 0, 0)
    needsToWinP2 = mutableListOf(0, 0, 0)

    //check for horizontal win
    for (i in 1..rows) {
        val prueba = mutableListOf<String>()
        for (a in list1[i]) {
            if (a == "o" || a == "*" || a == " ") {
                prueba.add(a)
            }
        }
        if ("oooo" in prueba.joinToString("")) {
            needsToWinP1[0] = 4
        }
        if ("****" in prueba.joinToString("")) {
            needsToWinP2[0] = 4
        }
    }

    //check for vertical win
    for (a in 1..columns) {
        val prueba = mutableListOf<String>()
        for (i in 1..rows) {
            prueba.add(list1[i][a * 2 - 1])
            if ("oooo" in prueba.joinToString("")) {
                needsToWinP1[0] = 4
            }
            if ("****" in prueba.joinToString("")) {
                needsToWinP2[0] = 4
            }
        }
    }
}

fun checkWinD1(char: String) {
    needsToWinP1[2] = 1
    needsToWinP2[2] = 1
    //for check to diagonal win

    if (list1[actualRow][actualPosition] == char && actualPosition - 2 >= 1 &&
        actualRow - 1 >= 1
    ) {
        if (list1[actualRow - 1][actualPosition - 2] == char) {
            if (char == "o") needsToWinP1[2]++
            if (char == "*") needsToWinP2[2]++
            if (actualRow - 2 >= 1 && actualPosition - 4 >= 1 && list1[actualRow - 2][actualPosition - 4] == char) {
                if (char == "o") needsToWinP1[2]++
                if (char == "*") needsToWinP2[2]++
                if (actualRow - 3 >= 1 && actualPosition - 6 >= 1 && list1[actualRow - 3][actualPosition - 6] == char) {
                    if (char == "o") needsToWinP1[2]++
                    if (char == "*") needsToWinP2[2]++
                }
            }
        }
    }

    if (list1[actualRow][actualPosition] == char && actualPosition + 2 <= columns * 2 &&
        actualRow + 1 <= rows
    ) {
        if (actualRow + 1 <= rows && actualPosition + 2 <= columns * 2 && list1[actualRow + 1][actualPosition + 2] == char) {
            if (char == "o") needsToWinP1[2]++
            if (char == "*") needsToWinP2[2]++
            if (actualRow + 2 <= rows && actualPosition + 4 <= columns * 2 && list1[actualRow + 2][actualPosition + 4] == char) {
                if (char == "o") needsToWinP1[2]++
                if (char == "*") needsToWinP2[2]++
                if (actualRow + 3 <= rows && actualPosition + 6 <= columns * 2 && list1[actualRow + 3][actualPosition + 6] == char) {
                    if (char == "o") needsToWinP1[2]++
                    if (char == "*") needsToWinP2[2]++
                }
            }
        }
    }
}

fun checkWinD2(char: String) {
    needsToWinP1[2] = 1
    needsToWinP2[2] = 1
    //for check to diagonal win

    if (list1[actualRow][actualPosition] == char && actualPosition - 2 >= 1 &&
        actualRow + 1 <= rows) {
        if (list1[actualRow + 1][actualPosition - 2] == char) {
            if (char == "o") needsToWinP1[2]++
            if (char == "*") needsToWinP2[2]++
            if (actualRow + 2 <= rows && actualPosition - 4 >= 1 && list1[actualRow + 2][actualPosition - 4] == char) {
                if (char == "o") needsToWinP1[2]++
                if (char == "*") needsToWinP2[2]++
                if (actualRow + 3 <= rows && actualPosition - 6 >= 1 && list1[actualRow + 3][actualPosition - 6] == char) {
                    if (char == "o") needsToWinP1[2]++
                    if (char == "*") needsToWinP2[2]++
                }
            }
        }
    }

    if (list1[actualRow][actualPosition] == char && actualPosition + 2 <= columns * 2 &&
        actualRow - 1 >= 1) {
        if (actualPosition + 2 <= columns * 2 && list1[actualRow - 1][actualPosition + 2] == char) {
            if (char == "o") needsToWinP1[2]++
            if (char == "*") needsToWinP2[2]++
            if (actualRow - 2 >= 1 && actualPosition + 4 <= columns * 2 && list1[actualRow - 2][actualPosition + 4] == char) {
                if (char == "o") needsToWinP1[2]++
                if (char == "*") needsToWinP2[2]++
                if (actualRow - 3 >= 1 && actualPosition + 6 <= columns * 2 && list1[actualRow - 3][actualPosition + 6] == char) {
                    if (char == "o") needsToWinP1[2]++
                    if (char == "*") needsToWinP2[2]++
                }
            }
        }
    }
}

fun printVictory() {
    if (needsToWinP1[0] >= 4 || needsToWinP1[1] >= 4 || needsToWinP1[2] >= 4 && onlyOne == 1) {
        println("Player $player1 won")
        onlyOne++
        scoreP1 += 2
        if (scoreP1 > 0 || scoreP2 > 0) {
            println(
                "Score\n" +
                        "$player1: $scoreP1 $player2: $scoreP2"
            )
        }
    }
    if (needsToWinP2[0] >= 4 || needsToWinP2[1] >= 4 || needsToWinP2[2] >= 4 && onlyOne == 1) {
        println("Player $player2 won")
        onlyOne++
        scoreP2 += 2
        if (scoreP1 > 0 || scoreP2 > 0) {
            println(
                "Score\n" +
                        "$player1: $scoreP1 $player2: $scoreP2"
            )
        }
    }
}

