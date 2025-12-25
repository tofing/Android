class Driver(
    fullName: String,
    age: Int,
    currentSpeed: Double,
    private val carModel: String
) : Human(fullName, age, currentSpeed) {

    private var directionX: Double = 1.0
    private var directionY: Double = 1.0

    override fun move() {
        x += directionX * currentSpeed
        y += directionY * currentSpeed

        println("Водитель ${getFullName()} на $carModel движется прямо в точку (${"%.2f".format(x)}, ${"%.2f".format(y)})")
    }
}