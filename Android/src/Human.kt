open class Human(
        private var fullName: String,
        private var age: Int,
        override var speed: Double
        ) : Movable {

        override var x: Double = 0.0
        override var y: Double = 0.0

        fun getFullName(): String = fullName
        fun setFullName(newName: String) { fullName = newName }

        fun getAge(): Int = age
        fun setAge(newAge: Int) { age = newAge }

        override open fun move() {
        x += (Math.random() * 2 - 1) * speed
        y += (Math.random() * 2 - 1) * speed

        println("$fullName переместился в точку (${"%.2f".format(x)}, ${"%.2f".format(y)})")
        }

        constructor() : this("Неизвестно", 0, 1.0)
        }