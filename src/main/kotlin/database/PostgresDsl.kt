package database

import models.Model

class PostgresDsl {

    val models = ArrayList<(M : Model) -> Boolean>()

    inline fun <reified M: Model> onModel(crossinline func: (M) -> Unit) {
        models += {
            if (it is M) {
                func(it)
                true
            } else false
        }
    }

}

fun pDsl(dsl: PostgresDsl.() -> Unit): PostgresDsl = PostgresDsl().apply(dsl)