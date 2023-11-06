package database

import models.Model

class DslReducer {

    fun <M: Model> reduce(models: ArrayList<(M) -> Boolean>, model: M) {
        for (m in models) {
            if (m(model)) {
                break
            }
        }
    }

}