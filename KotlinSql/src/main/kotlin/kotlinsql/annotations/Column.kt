package kotlinsql.annotations

@Target(AnnotationTarget.FIELD)
annotation class Column(val name: String = "")
