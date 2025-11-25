class Driver(
    fullName: String,
    age: Int,
    speed: Double,
    private val carModel: String
) : Human(fullName, age, speed) {

    private var directionX: Double = 1.0
    private var directionY: Double = 1.0

    override fun move() {
        x += directionX * speed
        y += directionY * speed

        println("Водитель ${getFullName()} на $carModel движется прямо в точку (${"%.2f".format(x)}, ${"%.2f".format(y)})")
    }
}