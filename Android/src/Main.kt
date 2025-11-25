class Human(
    private var fullName: String,
    private var age: Int,
    private var speed: Double
) {
    fun getFullName(): String = fullName
    fun setFullName(newName: String) { fullName = newName }

    fun getAge(): Int = age
    fun setAge(newAge: Int) { age = newAge }

    fun getSpeed(): Double = speed
    fun setSpeed(newSpeed: Double) { speed = newSpeed }

    private var x: Double = 0.0
    private var y: Double = 0.0

    fun move() {
        x += (Math.random() * 2 - 1) * speed
        y += (Math.random() * 2 - 1) * speed

        println("$fullName переместился в точку (${"%.2f".format(x)}, ${"%.2f".format(y)})")
    }

    constructor() : this("Неизвестно", 0, 1.0)
}

fun main() {
    println("=== НАЧАЛО СИМУЛЯЦИИ ДВИЖЕНИЯ ===")
    val people = arrayOf(
        Human("Иванов Иван Иванович", 20, 2.5),
        Human("Петров Петр Петрович", 22, 1.8),
        Human("Сидорова Анна Сергеевна", 19, 3.0),
        Human("Козлов Алексей Дмитриевич", 21, 2.0),
        Human("Николаева Мария Петровна", 23, 2.2)
    )
    val simulationTime = 10
    for (second in 1..simulationTime) {
        println("\n--- Секунда $second ---")
        for (person in people) {
            person.move() }
        Thread.sleep(500)
    }
    println("\n=== СИМУЛЯЦИЯ ЗАВЕРШЕНА ===")
}