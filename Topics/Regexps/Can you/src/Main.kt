fun main() {
    val answer = readln()
    val option = Regex("I can.?.? do my homework on time!")
    println(option.matches(answer))

}
