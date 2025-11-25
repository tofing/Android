open class Human(
    private var fullName: String,
    private var age: Int,
    protected var speed: Double
) {
    fun getFullName(): String = fullName
    fun setFullName(newName: String) { fullName = newName }

    fun getAge(): Int = age
    fun setAge(newAge: Int) { age = newAge }

    protected var x: Double = 0.0
    protected var y: Double = 0.0

    open fun move() {
        x += (Math.random() * 2 - 1) * speed
        y += (Math.random() * 2 - 1) * speed

        println("$fullName переместился в точку (${"%.2f".format(x)}, ${"%.2f".format(y)})")
    }

    constructor() : this("Неизвестно", 0, 1.0)
}

class Driver(
    fullName: String,
    age: Int,
    speed: Double,
    private val carModel: String) : Human(fullName, age, speed) {

    private var directionX: Double = 1.0
    private var directionY: Double = 1.0

    override fun move() {
        x += directionX * speed
        y += directionY * speed

        println("Водитель ${getFullName()} на $carModel движется прямо в точку (${"%.2f".format(x)}, ${"%.2f".format(y)})")
    }
}

fun main() {
    println("=== НАЧАЛО СИМУЛЯЦИИ ДВИЖЕНИЯ ===")

    val humans = listOf(
        Human("Иванов Иван Иванович", 20, 2.5),
        Human("Петров Петр Петрович", 22, 1.8),
        Human("Сидорова Анна Сергеевна", 19, 3.0),
        Human("Козлов Алексей Дмитриевич", 21, 2.0)
    )

    val driver = Driver("Волков Сергей Александрович", 35, 4.0, "Toyota Camry")

    val simulationTime = 10

    for (second in 1..simulationTime) {
        println("\n--- Секунда $second ---")

        val threads = mutableListOf<Thread>()

        humans.forEach { human ->
            val thread = Thread {
                human.move()
            }
            threads.add(thread)
            thread.start()
        }

        val driverThread = Thread {
            driver.move()
        }
        threads.add(driverThread)
        driverThread.start()

        threads.forEach { it.join() }

        Thread.sleep(1000)
    }

    println("\n=== СИМУЛЯЦИЯ ЗАВЕРШЕНА ===")
}
